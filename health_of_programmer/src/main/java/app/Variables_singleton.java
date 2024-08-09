package app;

import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.io.File;

/**
 * Класс для хранения глобальных переменных, использующихся в других классах
 */
public class Variables_singleton {
    // ссылка на сцену Controller_for_main для возвращения на главный экран
    private Scene scene;
    // указатель паузы
    private boolean isPause;
    // изображение иконки
    private final Image icon = new Image(new File("src/main/resources/images/icon.png").toURI().toString(), false);
    // массив с сохраненными данными
    private String[] saved_data;

    // единственный объект класса
    private static Variables_singleton instance;

    public static synchronized Variables_singleton getInstance() {
        if (instance == null){
            instance = new Variables_singleton();
        }
        return instance;
    }

    private Variables_singleton(){
        this.isPause = false;
        this.saved_data = new String[3];
    }

    public void setPause(boolean pause) {
        this.isPause = pause;
    }

    public void setSaved_data(String[] saved_data) {
        this.saved_data = saved_data;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Image getIcon() {
        return this.icon;
    }

    public Scene getScene() {
        return scene;
    }

    public boolean getPause() {
        return !isPause;
    }

    public String[] getSaved_data() {
        return saved_data;
    }
}
