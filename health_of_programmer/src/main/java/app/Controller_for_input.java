package app;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Контроллер для получения ввода пользователя
 */
public class Controller_for_input {
    @FXML
    private TextField enter_text;

    /**
     * Функция для получения ввода от пользователя
     *
     * @return ввод пользователя
     */
    public String enter_field() {
        return enter_text.getText();
    }

}
