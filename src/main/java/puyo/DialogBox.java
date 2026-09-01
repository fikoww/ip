package puyo;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a Label containing text from the speaker.
 */
public class DialogBox extends HBox {

    @FXML
    private Label text;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogBox.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.text.setText(text);
        displayPicture.setImage(img);

        // Clip the avatar image into a circle
        Circle clip = new Circle(49.5, 49.5, 49.5);
        displayPicture.setClip(clip);

        // Prevent text clipping for longer responses
        this.text.setMinHeight(Region.USE_PREF_SIZE);
        this.setMinHeight(Region.USE_PREF_SIZE);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(tmp);
        this.getChildren().setAll(tmp);
    }

    /**
     * Creates a user dialog box with the specified text and avatar.
     *
     * @param s The user's input text.
     * @param img The user's avatar image.
     * @return A custom DialogBox instance formatted for the user.
     */
    public static DialogBox getUserDialog(String s, Image img) {
        var db = new DialogBox(s, img);
        db.text.getStyleClass().add("user-label");
        return db;
    }

    /**
     * Creates a Puyo dialog box with the specified response and avatar.
     *
     * @param s The chatbot's response text.
     * @param img The chatbot's avatar image.
     * @return A custom DialogBox instance formatted for Puyo.
     */
    public static DialogBox getPuyoDialog(String s, Image img) {
        var db = new DialogBox(s, img);
        db.flip();
        db.text.getStyleClass().add("puyo-label");
        return db;
    }
}
