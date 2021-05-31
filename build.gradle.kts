import de.aaschmid.gradle.plugins.cpd.Cpd


plugins {
    // Apply the java plugin to add support for Java
    java

    // Apply the application plugin to add support for building a CLI application
    // You can run your app via task "run": ./gradlew run
    application

    /*
     * Adds tasks to export a runnable jar.
     * In order to create it, launch the "shadowJar" task.
     * The runnable jar will be found in build/libs/projectname-all.jar
     */
    id("com.github.johnrengelman.shadow") version "6.1.0"

    checkstyle
    pmd
    id("de.aaschmid.cpd")
    id("com.github.spotbugs")
    `build-dashboard`
}

repositories {
    mavenCentral()
}

val javaFXModules = listOf(
    "base",
    "controls",
    "fxml",
    "swing",
    "graphics",
    "media"
)

val supportedPlatforms = listOf("linux", "mac", "win") // All required for OOP

val javaFxVersion = "15.0.1"

val jUnitVersion = "5.7.1"

dependencies {
     // Import of commons lang3 for Pair class
     implementation("org.apache.commons:commons-lang3:3.12.0")

     // Import of Gson by Google. It Serialize and Deserialize Java Object into JSON element
     implementation("com.google.code.gson:gson:2.8.6")

    // JavaFX: comment out if you do not need them
    for (platform in supportedPlatforms) {
        for (module in javaFXModules) {
            implementation("org.openjfx:javafx-$module:$javaFxVersion:$platform")
        }
    }

    // JUnit API and testing engine
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<Test> {
       ignoreFailures = true

        // Enables JUnit 5 Jupiter module
        useJUnitPlatform()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    // Define the main class for the application
    mainClass.set("application.Launcher")

    /*
     * mainClassName was deprecated by Gradle, but it is still required by John Engelman's Shadow plugin.
     * A pull request with a fix was already merged, but it hasn't been released yet;
     * see https://github.com/johnrengelman/shadow/issues/609 and https://github.com/johnrengelman/shadow/pull/612
     */
    @Suppress("DEPRECATION")
    mainClassName = mainClass.get()
}


spotbugs {
    setEffort("max")
    setReportLevel("low")
    showProgress.set(true)
    val excludeFile = File("${project.rootProject.projectDir}/config/spotbugs/excludes.xml")
    if (excludeFile.exists()) {
        excludeFilter.set(excludeFile)
    }
}

tasks.withType<com.github.spotbugs.snom.SpotBugsTask> {
    ignoreFailures = true
    reports {
        create("html") {
            enabled = true
        }
    }
}

pmd {
    ruleSets = listOf()
    ruleSetConfig = resources.text.fromFile("${project.rootProject.projectDir}/config/pmd/pmd.xml")
    isIgnoreFailures = true
}

cpd {
    isIgnoreFailures = true
}

tasks.withType<Cpd> {
    reports {
        xml.setEnabled(false)
        text.setEnabled(true)
    }
    language = "java"
    minimumTokenCount = 50
    ignoreFailures = true
    source = sourceSets["main"].allJava
}

checkstyle {
    isIgnoreFailures = true
}

typealias QAInfoContainer = Iterable<QAInfo>

interface QAInfo {
    val checker: String
    val lines: IntRange
    val details: String
    val file: String
    val blamedTo: Set<String>
}

fun List<String>.commandOutput(): String {
    val process = ProcessBuilder(this).redirectOutput(ProcessBuilder.Redirect.PIPE).start()
    process.waitFor(1, TimeUnit.MINUTES)
    return process.inputStream.bufferedReader().readText()
}

val authorMatch = Regex("^author\\s+(.+)$")
fun blameFor(file: String, lines: IntRange): Set<String> =
    listOf("git", "blame", "-L", "${lines.start},${lines.endInclusive}", "-p", file)
        .commandOutput().lines()
        .flatMap { line -> authorMatch.matchEntire(line)?.destructured?.toList() ?: emptyList() }
        .toSet()

data class QAInfoForChecker(
    override val checker: String,
    override val file: String,
    override val lines: IntRange = 1..File(file).readText().lines().size,
    override val details: String = "",
    private val blamed: Set<String>? = null,
) : QAInfo {
    override val blamedTo: Set<String> = blamed ?: blameFor(file, lines)
}

operator fun org.w3c.dom.Node.get(attribute: String): String =
    attributes?.getNamedItem(attribute)?.textContent
        ?: throw IllegalArgumentException("No attribute '$attribute' in $this")

fun org.w3c.dom.Node.childrenNamed(name: String): List<org.w3c.dom.Node> =
        childNodes.toIterable().filter { it.nodeName == name }

class PmdQAInfoExtractor(root: org.w3c.dom.Element) : QAInfoContainer by (
    root.childNodes.toIterable()
        .asSequence()
        .filter { it.nodeName == "file" }
        .flatMap { file -> file.childrenNamed("violation").map{ file to it } }
        .map { (file, violation) ->
            QAInfoForChecker(
                "Sub-optimal Java object-orientation",
                file["name"],
                violation["beginline"].toInt()..violation["endline"].toInt(),
                "[${violation["ruleset"].toUpperCase()}] ${violation.textContent.trim()}",
            )
        }
        .asIterable()
)

class CpdQAInfoExtractor(root: org.w3c.dom.Element) : QAInfoContainer by (
    root.childNodes.toIterable()
        .asSequence()
        .filter { it.nodeName == "duplication" }
        .map { duplication ->
            val files = duplication.childrenNamed("file")
            val filePaths = files.map { it["path"] }
            val lines = duplication["lines"].toInt()
            val shortFiles = files.map { "${File(it["path"]).name}:${it["line"]}" }
            val ranges = files.map {
                val begin = it["line"].toInt()
                begin..(begin + lines)
            }
            val blamed = filePaths.zip(ranges).flatMap { (file, lines) -> blameFor(file, lines) }.toSet()
            val description = "Duplication of $lines lines" +
                    " and ${duplication["tokens"]} tokens across ${filePaths.toSet().size}" +
                    " files: ${shortFiles.joinToString(prefix = "", postfix = "")}"
            QAInfoForChecker(
                    "Duplications and violations of the DRY principle",
                    files.first()["path"],
                    ranges.first(),
                    description,
                    blamed
            )
        }
        .asIterable()
)

class CheckstyleQAInfoExtractor(root: org.w3c.dom.Element) : QAInfoContainer by (
    root.childNodes.toIterable()
        .asSequence()
        .filter { it.nodeName == "file" }
        .flatMap { file -> file.childrenNamed("error").map{ file["name"] to it } }
        .map { (file, error) ->
            val line = error["line"].toInt()
            val lineRange = line..line
            QAInfoForChecker("Style errors", file, lineRange, error["message"])
        }
        .asIterable()
)

class SpotBugsQAInfoExtractor(root: org.w3c.dom.Element) : QAInfoContainer by (
    root.childNodes.let { childNodes ->
        val sourceDirs = childNodes.toIterable()
            .filter { it.nodeName == "Project" }
            .first()
            .childrenNamed("SrcDir")
        val sourceDir = if (sourceDirs.size == 1) {
                sourceDirs.first()
            } else {
                sourceDirs.find { it.textContent.endsWith("java") }
            } ?: throw IllegalStateException("Invalid source directories: ${sourceDirs.map { it.textContent }}")
        val sourcePath = sourceDir.textContent.trim()
        childNodes.toIterable()
            .asSequence()
            .filter { it.nodeName == "BugInstance" }
            .map { bugDescriptor ->
                val sourceLineDescriptor = bugDescriptor.childrenNamed("SourceLine").first()
                val category = bugDescriptor["category"].takeUnless { it == "STYLE" } ?: "UNSAFE"
                QAInfoForChecker(
                    "Potential bugs",
                    "$sourcePath${File.separator}${sourceLineDescriptor["sourcepath"]}",
                    sourceLineDescriptor["start"].toInt()..sourceLineDescriptor["end"].toInt(),
                    "[$category] ${bugDescriptor.childrenNamed("LongMessage").first().textContent.trim()}",
                )
            }
            .asIterable()
    }
)

fun org.w3c.dom.NodeList.toIterable() = Iterable {
    object : Iterator<org.w3c.dom.Node> {
        var index = 0;
        override fun hasNext(): Boolean = index < length - 1
        override fun next(): org.w3c.dom.Node = item(index++)
    }
}

fun String.endingWith(postfix: String): String = takeIf { endsWith(postfix) } ?: "$this$postfix"

tasks.register("blame") {
    val dependencies = tasks.withType<org.gradle.api.plugins.quality.Checkstyle>() +
            tasks.withType<org.gradle.api.plugins.quality.Pmd>() +
            tasks.withType<com.github.spotbugs.snom.SpotBugsTask>() +
            tasks.withType<de.aaschmid.gradle.plugins.cpd.Cpd>()
    dependsOn(dependencies)
    val output = "${project.buildDir}${File.separator}blame.md"
    outputs.file(output)
    doLast {
        val factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        val xmlParser = factory.newDocumentBuilder();
        val errors = dependencies
            .flatMap { task -> task.outputs.files.asIterable().filter { it.exists() && it.extension == "xml" } }
            .flatMap<File, QAInfo> {
                val root: org.w3c.dom.Element = xmlParser.parse(it).documentElement
                when (root.tagName) {
                    "pmd" -> PmdQAInfoExtractor(root)
                    "pmd-cpd" -> CpdQAInfoExtractor(root)
                    "checkstyle" -> CpdQAInfoExtractor(root)
                    "BugCollection" -> SpotBugsQAInfoExtractor(root)
                    else -> emptyList<QAInfo>().also { println("Unknown root type ${root.tagName}")}
                }
            }
        val errorsByStudentByChecker: Map<String, Map<String, List<QAInfo>>> = errors
            .flatMap { error -> error.blamedTo.map { it to error } }
            .groupBy { it.first }
            .mapValues { (_, errors) -> errors.map { it.second }.groupBy { it.checker } }
        val report = errorsByStudentByChecker.map { (student, errors) ->
            """
            |# $student
            |
            |${errors.map { it.value.size }.sum()} violations
            |${errors.map { (checker, violations) ->
                """
                |## $checker: ${violations.size} mistakes
                ${ violations.sortedBy { it.details }
                    .joinToString("") {
                        val fileName = File(it.file).name
                        "|* ${it.details.endingWith(".")} In: $fileName@[${it.lines}]\n"
                    }.trimEnd()
                }
                """
            }.joinToString(separator = "", prefix = "", postfix = "")}
            |
            """.trimMargin()
        }.joinToString(separator = "", prefix = "", postfix = "")
        println(report)
        file(output).writeText(report)
    }
}
