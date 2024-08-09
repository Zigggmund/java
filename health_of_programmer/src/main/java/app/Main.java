package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Класс для запуска приложения
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main_screen.fxml")));
        Scene scene = new Scene(root);
        Variables_singleton appVariables = Variables_singleton.getInstance();
        appVariables.setScene(scene);
        String css = Objects.requireNonNull(this.getClass().getResource("main_window_styles.css")).toExternalForm();

        scene.getStylesheets().add(css);
        stage.setTitle("За ваше здоровье!");
        stage.setWidth(700);
        stage.setHeight(450);
        stage.getIcons().add(appVariables.getIcon());
        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();
    }

}
