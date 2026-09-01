package puyo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for MainWindow. Provides the layout for the other controls.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Puyo puyo;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image puyoImage = new Image(this.getClass().getResourceAsStream("/images/puyo.png"));

    public void setPuyo(Puyo puyo) {
        this.puyo = puyo;
    }

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        // Welcome message dari Puyo
        String welcomeMessage = "Hello! I'm Puyo!\nWhat can I do for you today?";
        dialogContainer.getChildren().add(
                DialogBox.getPuyoDialog(welcomeMessage, puyoImage)
        );
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = puyo.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getPuyoDialog(response, puyoImage)
        );
        userInput.clear();

        if (input.trim().equalsIgnoreCase("bye")) {
            javafx.application.Platform.runLater(() -> {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                javafx.application.Platform.exit();
            });
        }
    }
}
