package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * Контроллер для основного окна
 */
public class Controller_for_main {
    @FXML
    private Button btn_timer1;
    @FXML
    private Button btn_timer2;
    @FXML
    private Button btn_timer3;
    @FXML
    private Label lbl_timer1_work;
    @FXML
    private Label lbl_timer1_rest;
    @FXML
    private Label lbl_timer2_work;
    @FXML
    private Label lbl_timer2_rest;
    @FXML
    private Label lbl_timer3_work;
    @FXML
    private Label lbl_timer3_rest;
    @FXML
    private Button btn_stop;
    @FXML
    private Button btn_reset;
    @FXML
    private Button btn_pause;
    @FXML
    private Button timer1_work;
    @FXML
    private Button timer2_work;
    @FXML
    private Button timer3_work;
    @FXML
    private Button timer1_rest;
    @FXML
    private Button timer2_rest;
    @FXML
    private Button timer3_rest;

    private Variables_singleton appVariables = Variables_singleton.getInstance();
    private Map<String, String> colors;
    private Time_counter[] time_counters;
    private Scene scene; // хранит новую сцену, на которую мы переключаемся
    private Parent root;

    public void initialize() {
        appVariables = Variables_singleton.getInstance();
        colors = Map.of("red", "#FF4500", "green", "#9ACD32", "bright_green", "#ADFF2F", "bright_red", "#FA8072");

        btn_timer1.setStyle("-fx-background-color: " + colors.get("green") + "; ");
        btn_timer2.setStyle("-fx-background-color: " + colors.get("green") + "; ");
        btn_timer3.setStyle("-fx-background-color: " + colors.get("green") + "; ");
        btn_stop.setStyle("-fx-background-color: " + colors.get("green") + "; ");
        btn_reset.setStyle("-fx-background-color: " + colors.get("green") + "; ");
        timer1_work.setStyle("-fx-background-color: " + colors.get("bright_red") + "; ");
        timer1_rest.setStyle("-fx-background-color: " + colors.get("bright_red") + "; ");
        timer2_work.setStyle("-fx-background-color: " + colors.get("bright_red") + "; ");
        timer2_rest.setStyle("-fx-background-color: " + colors.get("bright_red") + "; ");
        timer3_work.setStyle("-fx-background-color: " + colors.get("bright_red") + "; ");
        timer3_rest.setStyle("-fx-background-color: " + colors.get("bright_red") + "; ");

        lbl_timer1_work.setText(Json_utils.get_value("timer1_work"));
        lbl_timer2_work.setText(Json_utils.get_value("timer2_work"));
        lbl_timer3_work.setText(Json_utils.get_value("timer3_work"));
        lbl_timer1_rest.setText(Json_utils.get_value("timer1_rest"));
        lbl_timer2_rest.setText(Json_utils.get_value("timer2_rest"));
        lbl_timer3_rest.setText(Json_utils.get_value("timer3_rest"));

        setup_pause(false, false);

        initialize_timers();
    }

    /**
     * Функция нужна для инициализации всех таймеров
     */
    private void initialize_timers() {

        Time_counter timer1 = new Time_counter(1, Json_utils.get_value("timer1_work"), Json_utils.get_value("timer1_rest"));
        Time_counter timer2 = new Time_counter(2, Json_utils.get_value("timer2_work"), Json_utils.get_value("timer2_rest"));
        Time_counter timer3 = new Time_counter(3, Json_utils.get_value("timer3_work"), Json_utils.get_value("timer3_rest"));

        time_counters = new Time_counter[]{timer1, timer2, timer3};
    }

    /**
     * Функция отвечает за активацию/деактивацию паузы
     *
     * @param disabled заблокировать/разблокировать
     * @param mode     задать зеленый/красный цвет
     */
    private void setup_pause(boolean disabled, boolean mode) {
        if (mode) {
            btn_pause.setStyle("-fx-background-color: " + colors.get("green") + "; ");
        } else {
            btn_pause.setStyle("-fx-background-color: " + colors.get("red") + "; ");
        }
        btn_pause.setDisable(!disabled);
    }

    /**
     * Функция отвечает за активацию/деактивацию всех кнопок при паузе/снятии с паузы
     *
     * @param mode задать зеленый/красный цвет
     */
    private void setup_except_for_pause(boolean mode) {
        btn_reset.setDisable(mode);
        btn_stop.setDisable(mode);
        btn_timer1.setDisable(mode);
        btn_timer2.setDisable(mode);
        btn_timer3.setDisable(mode);
        timer1_work.setDisable(mode);
        timer1_rest.setDisable(mode);
        timer2_work.setDisable(mode);
        timer2_rest.setDisable(mode);
        timer3_work.setDisable(mode);
        timer3_rest.setDisable(mode);
    }

    /**
     * Функция отвечает за активацию/деактивацию кнопки "Все таймеры"
     *
     * @param mode задать зеленый/красный цвет
     */
    private void setup_stop(boolean mode) {
        if (mode) {
            btn_stop.setText("Выключить все");
            btn_stop.setStyle("-fx-background-color: " + colors.get("red") + "; ");
        } else {
            btn_stop.setText("Включить все");
            btn_stop.setStyle("-fx-background-color: " + colors.get("green") + "; ");
        }
    }

    /**
     * Функция вызывается при нажатии на кнопку "Пауза"
     */
    public void pause(ActionEvent event) {
        if (appVariables.getPause()) {
            appVariables.setPause(true);
            for (int i = 0; i < time_counters.length; i++) {
                String text1 = ((Button) appVariables.getScene().lookup("#timer" + (i + 1) + "_work")).getText();
                String text2 = ((Button) appVariables.getScene().lookup("#timer" + (i + 1) + "_rest")).getText();
                int seconds = Convert_time.time_to_int(text1) + Convert_time.time_to_int(text2);
                try {
                    String[] data = appVariables.getSaved_data();
                    data[i] = time_counters[i].getMode() + "_" + seconds;
                    appVariables.setSaved_data(data);
                    time_counters[i].deactivate_timer();
                } catch (Exception ignored) {
                }

            }
            System.out.println("Данные соханены: " + Arrays.toString(appVariables.getSaved_data()));
            setup_except_for_pause(true);
            setup_pause(true, false);
            System.out.println("Таймеры поставлены на паузу");
        } else {
            appVariables.setScene(((Node) event.getSource()).getScene());

            for (int i = 0; i < time_counters.length; i++) {
                if (!appVariables.getSaved_data()[i].split("_")[0].equals("deactivate")) {
                    time_counters[i].activate_timer();
                }
            }

            setup_except_for_pause(false);
            setup_pause(true, true);
            System.out.println("Таймеры сняты с паузы");
            appVariables.setPause(false);
        }
    }

    /**
     * Функция вызывается при нажатии на кнопку "Стоп"
     */
    public void stop(ActionEvent event) {
        appVariables.setScene(((Node) event.getSource()).getScene());
        if (btn_stop.getText().equals("Выключить все")) {
            if (Event_handling.make_alert("Выключить все", "Вы точно хотите сбросить значения всех таймеров \nи начать отсчет сначала?")) {
                System.out.println("Таймеры выключены");
                setup_stop(false);

                setup_pause(false, false);
                for (Time_counter timeCounter : time_counters) {
                    timeCounter.deactivate_timer();
                }
            }
        } else {
            setup_pause(true, true);

            System.out.println("Таймеры включены");
            setup_stop(true);

            for (Time_counter timeCounter : time_counters) {
                timeCounter.activate_timer();
            }
        }
    }

    /**
     * Функция вызывается при нажатии на кнопку "Включить/Выключить" таймер
     */
    public void turn_timer(ActionEvent event) {
        appVariables.setScene(((Node) event.getSource()).getScene());
        String id = ((Button) event.getSource()).getId();
        int number = Character.getNumericValue(id.charAt(id.length() - 1));

        if (time_counters[number - 1].getMode().equals("deactivate")) {
            System.out.println("Включение таймера " + number);
            time_counters[number - 1].activate_timer();

            // активирование кнопки Пауза
            setup_pause(true, true);

            // изменение кнопки stop на выключить
            setup_stop(true);

        } else {
            System.out.println("Отключение таймера " + number);
            time_counters[number - 1].deactivate_timer();

            // изменение кнопки stop на включить, если все таймеры были отключены
            boolean flag = true;
            for (Time_counter timeCounter : time_counters) {
                try {
                    if (!timeCounter.getMode().equals("deactivate")) {
                        flag = false;
                    }
                } catch (Exception ignored) {
                }
            }
            if (flag) {
                setup_stop(false);

                setup_pause(false, false);
            }
        }

    }

    /**
     * Функция вызывается при нажатии на кнопку "Сбросить настройки"
     */
    public void reset() {
        if (Event_handling.make_alert("Сбросить настройки", "Вы точно хотите поставить значения \nтаймеров по умолчанию?")) {
            Map<String, String> values = Json_utils.get_array("default_timers");
            if (!(values == null || values.isEmpty())) {
                for (Map.Entry<String, String> entry : values.entrySet()) {
                    Json_utils.change_value(entry.getKey(), entry.getValue());
                    Label lbl = (Label) appVariables.getScene().lookup("#" + "lbl_" + entry.getKey());
                    lbl.setText(entry.getValue());
                }
                for (int i = 0; i < time_counters.length; i++) {
                    try {
                        time_counters[i].deactivate_timer();
                    } catch (Exception ignored) {
                    }
                    // использование сеттеров
                    time_counters[i].setWork_time(Convert_time.time_to_int(Json_utils.get_value("timer" + (i + 1) + "_work")));
                    time_counters[i].setRest_time(Convert_time.time_to_int(Json_utils.get_value("timer" + (i + 1) + "_rest")));

                }
                if (appVariables.getPause()) {
                    setup_pause(false, false);
                }

                setup_stop(false);
                btn_stop.setText("Включить все");
                btn_stop.setStyle("-fx-background-color: " + colors.get("green") + "; ");

                System.out.println("Настройки сброшены");
            }
        }

    }

    /**
     * Функция вызывается при нажатии на сам счетчик таймера
     */
    public void change_timer(ActionEvent event) throws IOException {
        Stage time_window = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("change_timer.fxml"));
        root = loader.load();
        scene = new Scene(root);
        String css = Objects.requireNonNull(this.getClass().getResource("small_windows_styles.css")).toExternalForm();

        time_window.setResizable(false);
        time_window.setWidth(258);
        time_window.setHeight(200);
        scene.getStylesheets().add(css);
        time_window.getIcons().add(appVariables.getIcon());
        time_window.setScene(scene);

        Controller_for_input controller_for_input = loader.getController();
        Event_handling.enter_close(time_window, scene, controller_for_input, "timer");

        time_window.initOwner(((Node) event.getSource()).getScene().getWindow());
        time_window.initModality(Modality.APPLICATION_MODAL);
        time_window.showAndWait();

        Node source = (Node) event.getSource();
        String id = source.getId();
        Scene scene = source.getScene();
        Label label = (Label) scene.lookup("#" + "lbl_" + id);

        String json_text = Json_utils.get_value("current");

        if (!json_text.isEmpty()) {
            label.setText(json_text);
            Json_utils.change_value(id, json_text);

            setup_pause(false, false);
            setup_stop(false);
            for (Time_counter timeCounter : time_counters) {
                try {
                    timeCounter.deactivate_timer();
                } catch (Exception ignored) {
                }
            }

            if (id.substring(6).equals("_work")) {
                time_counters[Character.getNumericValue(id.charAt(5)) - 1].setWork_time(Convert_time.time_to_int(json_text));
            } else {
                time_counters[Character.getNumericValue(id.charAt(5)) - 1].setRest_time(Convert_time.time_to_int(json_text));
            }
        }
    }

    /**
     * Функция вызывается при нажатии на кнопки из меню
     */
    public void new_window(ActionEvent event) throws IOException {
        if (appVariables.getPause() && !btn_pause.isDisable()) {
            pause(event);
        }

        String item = ((MenuItem) event.getSource()).getText();
        String name;
        if (item.equals("Правильная осанка")) {
            name = "pose";
        } else if (item.equals("Зарядка")) {
            name = "charger";
        } else {
            name = "vision";
        }
        String image_path = Json_utils.get_value(name + "_path");
        Json_utils.change_value("current_image", image_path);
        Json_utils.change_value("current_window", name);

        Stage new_window = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("image_window.fxml"));
        root = loader.load();
        scene = new Scene(root);
        String css = Objects.requireNonNull(this.getClass().getResource("main_window_styles.css")).toExternalForm();

        new_window.setWidth(600);
        new_window.setHeight(700);
        scene.getStylesheets().add(css);
        new_window.setResizable(false);
        new_window.getIcons().add(appVariables.getIcon());
        new_window.setTitle(item);
        new_window.setScene(scene);
        new_window.setOnCloseRequest(e -> new FXMLLoader().setLocation(getClass().getClassLoader().getResource("main_screen.fxml.fxml")));

        new_window.initModality(Modality.APPLICATION_MODAL);
        new_window.showAndWait();
    }
}