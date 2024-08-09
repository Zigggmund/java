package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Контроллер для окон с картинками
 */
public class Controller_for_images {
    @FXML
    private ImageView frame_image;

    private final Variables_singleton appVariables = Variables_singleton.getInstance();

    public void initialize() {
        Image img = new Image(new File(Json_utils.get_value("current_image")).toURI().toString(), false);
        image_scale(img);

    }

    /**
     * Функция изменения размера изображения под размер окна
     *
     * @param img изображение, размер которого нужно изменить
     */
    private void image_scale(Image img) {
        frame_image.setImage(img);

        // размещение по центру
        img = frame_image.getImage();
        double w;
        double h;
        double ratioX = frame_image.getFitWidth() / img.getWidth();
        double ratioY = frame_image.getFitHeight() / img.getHeight();

        double reducCoeff = Math.min(ratioY, ratioX);

        w = img.getWidth() * reducCoeff;
        h = img.getHeight() * reducCoeff;
        frame_image.setX((frame_image.getFitWidth() - w) / 2);
        frame_image.setY((frame_image.getFitHeight() - h) / 2);

    }

    /**
     * Функция создания окна для получения пути от пользователя
     */
    public void add_image(ActionEvent event) throws IOException {
        Stage add_image_window = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_image.fxml"));
        Parent root = loader.load();
        String css = Objects.requireNonNull(this.getClass().getResource("small_windows_styles.css")).toExternalForm();
        Scene scene = new Scene(root);

        add_image_window.setResizable(false);
        add_image_window.getIcons().add(appVariables.getIcon());
        add_image_window.setHeight(170);
        add_image_window.setWidth(415);
        scene.getStylesheets().add(css);
        add_image_window.setScene(scene);

        Controller_for_input controller_for_input = loader.getController();
        Event_handling.enter_close(add_image_window, scene, controller_for_input, "image");

        add_image_window.initOwner(((Node) event.getSource()).getScene().getWindow());
        add_image_window.initModality(Modality.APPLICATION_MODAL);
        add_image_window.showAndWait();

        Image img = new Image(new File(Json_utils.get_value("current_image")).toURI().toString(), false);
        image_scale(img);
    }
}
