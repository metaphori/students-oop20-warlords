package view;

import constants.ViewConstants;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

/**
 * 
 * Class that implements the confirm box EXIT.
 * 
 * */
public final class Exit {

    private static boolean answer;

    private Exit() {
        /**Not called. */
    }

    /**
     * Creation of the confirm box.
     * @param title String 
     * @param message String
     * @return answer boolean
     * */
    public static boolean display(final String title, final String message) {

        final Stage window = new Stage();

        /**Label. */
        final Label label = new Label();
        label.setText(message);
        label.setAlignment(Pos.CENTER);
        label.setPrefSize(ViewResolution.screenResolutionWidth(ViewConstants.DIVISOR_10), ViewResolution.screenResolutionHeight(ViewConstants.DIVISOR_25));
        label.setStyle("    -fx-text-fill: #FFFFFF;\r\n"
                + "    -fx-background-radius: 6;\r\n"
                + "    -fx-font-weight: bold;\r\n"
                + "     -fx-background-color: rgba(0, 0, 0, 0.5);\r\n"
                + "      -fx-font-size:" + ViewResolution.screenResolutionWidth(ViewConstants.DIVISOR_150) + ";");


        /**Buttons and events. */
        /**Button YES in the confirm box and his event. */
        final Button yesButton = new Button("YES");
        yesButton.setStyle(" -fx-background-radius: 6; "
                + "-fx-font-weight: bold;\r\n"
                + "-fx-text-fill: #FFFFFF;\r\n"
                + "-fx-background-color: linear-gradient(#000000, #696969);\r\n"
                + " -fx-font-size:" + ViewResolution.screenResolutionWidth(ViewConstants.DIVISOR_150) + ";");
        yesButton.setPrefSize(ViewResolution.screenResolutionWidth(ViewConstants.DIVISOR_25), ViewResolution.screenResolutionHeight(ViewConstants.DIVISOR_25));
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });

        /**Button NO in the confirm box and his event. */
        final Button noButton = new Button("NO");
        noButton.setStyle(" -fx-background-radius: 6; "
                + "-fx-font-weight: bold;\r\n"
                + "-fx-text-fill: #FFFFFF;\r\n"
                + "-fx-background-color: linear-gradient(#000000, #696969);\r\n"
                + " -fx-font-size:" + ViewResolution.screenResolutionWidth(ViewConstants.DIVISOR_150) + ";");
        noButton.setPrefSize(ViewResolution.screenResolutionWidth(ViewConstants.DIVISOR_25), ViewResolution.screenResolutionHeight(ViewConstants.DIVISOR_25));
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });


        /**Layout. */
        final VBox layout = new VBox(ViewConstants.DIVISOR_10);
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: grey;");


        /**Scene and stage preferences. */
        final Scene scene = new Scene(layout, ViewResolution.screenResolutionWidth(ViewConstants.DIVISOR_4), ViewResolution.screenResolutionHeight(ViewConstants.DIVISOR_4));
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();

        return answer;
    }
}
