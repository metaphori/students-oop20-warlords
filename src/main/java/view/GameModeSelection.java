package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import constants.ViewConstants;
import constants.ViewImages;
import controllers.ControllerImpl;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundPosition;

public class GameModeSelection extends Region {

    private MainMenu scenaMenu;
    private GameView scenaGame;
    private int scenarioSelezionato = 1;
    private int laneSelezionate = 1;
    private int timerSelezionato = 300;
    private String stringaScenario;
    private String stringaLane;
    private String stringaTimer;
            
    //screen size
    final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    final int sw = (int) screen.getWidth();
    final int sh = (int) screen.getHeight();

    public Parent createGameModeSelection() throws IOException {

        Pane pane = new Pane();
        Label settingsSelected = new Label();
        stringaScenario = new String("SELECTED SCENARIO: " + scenarioSelezionato);
        stringaLane = new String("\n SELECTED LANES: " + laneSelezionate);
        stringaTimer = new String("\n SELECTED TIMER: "+ timerSelezionato+ "S");
        //background image
        Image backgroundimg  = new Image(this.getClass().getResourceAsStream(ViewImages.GAME_SETTINGS));
        ImageView backG = new ImageView(backgroundimg);
        backG.setFitWidth(sw / ViewConstants.DIVISOR_1_5);
        backG.setFitHeight(sh / ViewConstants.DIVISOR_1_5);
       
        final DropShadow drop = new DropShadow(20, Color.RED);
        drop.setInput(new Glow());
  
        //scenario buttons
        Button scenarioButtons;
        List<Button> listaScenario = new ArrayList<>();

        for (int i = 1; i < ViewConstants.N_BUTTON_5 - 1; i++) {
            scenarioButtons = new Button("SCENARIO: " + i);
            scenarioButtons.setPrefSize(sw / ViewConstants.DIVISOR_10, sh / ViewConstants.DIVISOR_15);
            scenarioButtons.setStyle(Style.BUTTON_1);
            listaScenario.add(scenarioButtons);
        }

        
        listaScenario.get(0).setOnAction(e -> {
            scenarioSelezionato = 1;
            stringaScenario = new String("SELECTED SCENARIO: " + scenarioSelezionato);
            System.out.println(scenarioSelezionato);
            settingsSelected.setText(stringaScenario+stringaLane+stringaTimer);
        });
        listaScenario.get(1).setOnAction(e -> {
            scenarioSelezionato = 2;
            stringaScenario = new String("SELECTED SCENARIO: " + scenarioSelezionato);
            System.out.println(scenarioSelezionato);
            settingsSelected.setText(stringaScenario+stringaLane+stringaTimer);
        });
        listaScenario.get(2).setOnAction(e -> {
            scenarioSelezionato = 3;
            stringaScenario = new String("SELECTED SCENARIO: " + scenarioSelezionato);
            System.out.println(scenarioSelezionato);
            settingsSelected.setText(stringaScenario+stringaLane+stringaTimer);
        });
       
         
         //lane buttons
        Button laneButtons;
        List<Button> listaLane = new ArrayList<>();

        for (int i = 1; i < ViewConstants.N_BUTTON_6; i += 2) {
            laneButtons = new Button("LANE'S NUMBER: " + i);
            laneButtons.setPrefSize(sw / ViewConstants.DIVISOR_10, sh / ViewConstants.DIVISOR_15);
            laneButtons.setStyle(Style.BUTTON_1);
            listaLane.add(laneButtons);
        }
        listaLane.get(0).setOnAction( e -> {
            laneSelezionate = 1;
            stringaLane = new String("\n SELECTED LANES: " + laneSelezionate);
            System.out.println(laneSelezionate);
            settingsSelected.setText(stringaScenario+stringaLane+stringaTimer);
        });
        listaLane.get(1).setOnAction( e -> {
            laneSelezionate = 3;
            stringaLane = new String("\n SELECTED LANES: " + laneSelezionate);
            System.out.println(laneSelezionate);
            settingsSelected.setText(stringaScenario+stringaLane+stringaTimer);
        });
        listaLane.get(2).setOnAction( e -> {
            laneSelezionate = 5;
            stringaLane = new String("\n SELECTED LANES: " + laneSelezionate);
            System.out.println(laneSelezionate);
            settingsSelected.setText(stringaScenario+stringaLane+stringaTimer);
        });
        
        
       // timer Buttons
        Button timerButtons;
        List<Button> listaTimer = new ArrayList<>();

        for (int i = ViewConstants.N_BUTTON_5; i < ViewConstants.N_BUTTON_16; i += ViewConstants.N_BUTTON_5) {
            timerButtons = new Button(i + " MINUTES");
            timerButtons.setPrefSize(sw / ViewConstants.DIVISOR_10, sh / ViewConstants.DIVISOR_15);
            timerButtons.setStyle(Style.BUTTON_1);
            listaTimer.add(timerButtons);
            }
        listaTimer.get(0).setOnAction( e -> {
            timerSelezionato = 300;
            stringaTimer = new String("\n SELECTED LANES: " + timerSelezionato);
            System.out.println(timerSelezionato);
            settingsSelected.setText(stringaScenario+stringaLane+stringaTimer);
        });
        listaTimer.get(1).setOnAction( e -> {
            timerSelezionato = 600;
            stringaTimer = new String("\n SELECTED LANES: " + timerSelezionato);
            System.out.println(timerSelezionato);
            settingsSelected.setText(stringaScenario+stringaLane+stringaTimer);
        });
        listaTimer.get(2).setOnAction( e -> {
            timerSelezionato = 900;
            stringaTimer = new String("\n SELECTED LANES: " + timerSelezionato);
            System.out.println(timerSelezionato);
            settingsSelected.setText(stringaScenario+stringaLane+stringaTimer);
        });
        
        
        //back button
        Button back = new Button("BACK");
        back.setPrefSize(sw / ViewConstants.DIVISOR_10, sh / ViewConstants.DIVISOR_15);
        back.setStyle(Style.BUTTON_2);
        back.setOnAction(e ->{
            scenaMenu = new MainMenu();
            try {
                pane.getChildren().setAll(scenaMenu.createMainMenu());
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        Button start = new Button("START");
        start.setPrefSize(sw / ViewConstants.DIVISOR_10, sh / ViewConstants.DIVISOR_15);
        start.setStyle(Style.BUTTON_2);
        start.setOnAction(e -> {
//            scenaGame = new GameView();
            ControllerImpl c = new ControllerImpl();
           try {
            pane.getChildren().setAll(c.getView().createGameView());
           } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
           }
       });

        //label
        Label scenario = new Label("Scenario:");
        scenario.setAlignment(Pos.CENTER);
        scenario.setPrefSize(sw / ViewConstants.DIVISOR_10, sh / ViewConstants.DIVISOR_15);
        scenario.setStyle(Style.LABEL);

        Label lane = new Label("Number of lane:");
        lane.setAlignment(Pos.CENTER);
        lane.setPrefSize(sw / ViewConstants.DIVISOR_10, sh / ViewConstants.DIVISOR_15);
        lane.setStyle(Style.LABEL);

        Label timer = new Label("Timer:");
        timer.setAlignment(Pos.CENTER);
        timer.setPrefSize(sw / ViewConstants.DIVISOR_10, sh / ViewConstants.DIVISOR_15);
        timer.setStyle(Style.LABEL);
        

        settingsSelected.setAlignment(Pos.CENTER);
        settingsSelected.setPrefSize(sw / ViewConstants.DIVISOR_4, sh / ViewConstants.DIVISOR_10);
        settingsSelected.setStyle(Style.LABEL);
        settingsSelected.setText("SELECTED SCENARIO: " + scenarioSelezionato +"\n SELECTED LANES: " + laneSelezionate + "\n SELECTED TIMER: "+ timerSelezionato+ "S");
       
        //layout
        HBox scenarioBox = new HBox(sw / ViewConstants.DIVISOR_15);
        scenarioBox.setAlignment(Pos.CENTER);
        scenarioBox.getChildren().add(scenario);
        scenarioBox.getChildren().addAll(listaScenario);

        HBox laneBox = new HBox(sw / ViewConstants.DIVISOR_15);
        laneBox.setAlignment(Pos.CENTER);
        laneBox.getChildren().add(lane);
        laneBox.getChildren().addAll(listaLane);

        HBox timerBox = new HBox(sw / ViewConstants.DIVISOR_15);
        timerBox.setAlignment(Pos.CENTER);
        timerBox.getChildren().add(timer);
        timerBox.getChildren().addAll(listaTimer);

        HBox backStartBox = new HBox(sw / ViewConstants.DIVISOR_15);
        backStartBox.setPadding(new Insets(0, 0, 0, sw / ViewConstants.DIVISOR_30));
        backStartBox.getChildren().addAll(back, start, settingsSelected);
        VBox vBox = new VBox(sh / ViewConstants.DIVISOR_15);
        vBox.setAlignment(Pos.CENTER);

        vBox.setPrefSize(sw / ViewConstants.DIVISOR_1_5, sh / ViewConstants.DIVISOR_1_5);
        vBox.getChildren().addAll(scenarioBox, laneBox, timerBox, backStartBox);
        pane.getChildren().add(backG);
        pane.getChildren().addAll(vBox);
        return pane;
    }
}
