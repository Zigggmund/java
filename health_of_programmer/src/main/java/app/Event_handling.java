package app;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.File;

/**
 * Класс для обработки ввода пользователя
 */
public class Event_handling {
    /**
     * Функция для закрытия окна по нажатию на enter
     *
     * @param stage                текущее окно
     * @param scene                текущая сцена
     * @param controller_for_input контроллер для ввода
     * @param mode                 режим работы
     */
    public static void enter_close(Stage stage, Scene scene, Controller_for_input controller_for_input, String mode) {
        scene.setOnKeyPressed(ke -> {
            if (ke.getCode() == KeyCode.ENTER) {
                String user_text = controller_for_input.enter_field();
                if (mode.equals("timer")) {
                    int int_time = Convert_time.time_to_int(user_text);
                    Json_utils.change_value("current", "");
                    if (int_time > 0 && int_time < 24 * 60 * 60) {
                        String final_time = Convert_time.time_to_string(int_time);
                        System.out.println("Длительность изменена на " + final_time);
                        Json_utils.change_value("current", final_time);
                    } else if (int_time >= 24 * 60 * 60) {
                        System.out.println("Длительность не изменена, получено " + Convert_time.time_to_string(int_time));
                        make_error("Длительность не изменена", "Введено слишком большое число; >= 24 часов", false);
                    } else if (int_time == 0) {
                        System.out.println("Длительность не изменена");
                        make_error("Длительность не изменена", "Вы ввели строку или неподходящее число", false);
                    } else {
                        System.out.println("Длительность не изменена");
                        make_error("Длительность не изменена", "Выход за диапозон; Вы ввели число длиной больше 8 символов длиной", false);
                    }
                } else if (mode.equals("image")) {
                    if (new File(user_text).exists()) {
                        String extension = user_text.substring(user_text.lastIndexOf(".") + 1);
                        if (extension.equals("png") || extension.equals("jpg")) {
                            String value_name = Json_utils.get_value("current_window") + "_path";
                            System.out.println("Изображение загружено");
                            Json_utils.change_value(value_name, user_text);
                            Json_utils.change_value("current_image", user_text);
                        } else {
                            System.out.println("Изображение не загружено");
                            make_error("Изображение не загружено", "Неправильное расширение; поддерживаются png и jpg", false);
                        }
                    } else {
                        System.out.println("Изображение не загружено");
                        make_error("Изображение не загружено", "Неправильный путь; изображение по этому пути не найдено", false);
                    }
                }
                stage.close();
            }
        });
    }

    /**
     * Функция для создания окна предупреждения
     *
     * @param text1 заголовок
     * @param text2 основной текст
     * @return пользователь хочет продолжить, несмотря на предупреждение?
     */
    public static boolean make_alert(String text1, String text2) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Предупреждение");
        alert.setHeaderText(text1);
        alert.setContentText(text2);

        return (alert.showAndWait().get() == ButtonType.OK);
    }

    /**
     * Функция для создания окна ошибки
     *
     * @param text1    заголовок
     * @param text2    основной текст
     * @param critical уровень значимости ошибки
     */
    public static void make_error(String text1, String text2, boolean critical) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Ошибка");
        alert.setHeaderText(text1);
        alert.setContentText(text2);

        alert.showAndWait();
        if (critical) {
            Platform.exit();
            System.exit(0);
        }
    }
}
