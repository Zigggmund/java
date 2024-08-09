package app;

import javafx.application.Platform;
import javafx.scene.control.Button;
import org.controlsfx.control.Notifications;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс для управления таймерами
 */
public class Time_counter {

    private final Variables_singleton appVariables = Variables_singleton.getInstance();
    private int saved_time;

    private Timer timer;
    private final int number;
    private int work_time;
    private int rest_time;
    private String mode;

    public void setRest_time(int rest_time) {
        this.rest_time = rest_time;
    }

    public void setWork_time(int work_time) {
        this.work_time = work_time;
    }

    public String getMode() {
        return mode;
    }

    /**
     * Конструктор для таймера
     *
     * @param number    номер таймера
     * @param work_time время работы
     * @param rest_time время отдыха
     */
    public Time_counter(int number, String work_time, String rest_time) {
        this.number = number;
        this.work_time = Convert_time.time_to_int(work_time);
        this.rest_time = Convert_time.time_to_int(rest_time);
        this.mode = "deactivate";
    }

    /**
     * Функция для обновления таймера
     *
     * @param number  номер таймера
     * @param mode    режим работы
     * @param seconds время, которое нужно задать
     * @param color   цвет, в который нужно окрасить таймер
     */
    public void update_timer(int number, String mode, int seconds, char color) {
        Button b_timer = (Button) appVariables.getScene().lookup("#" + "timer" + number + "_" + mode);
        if (seconds != 0) {
            System.out.println(seconds + " | " + b_timer.getId() + " | " + b_timer.getText());
        }
        if (Objects.equals(color, 'g')) {
            b_timer.setStyle("-fx-background-color: #ADFF2F; ");
        } else if (Objects.equals(color, 'r')) {
            b_timer.setStyle("-fx-background-color: #FA8072; ");
        }
        System.out.println("Число дополнительных потоков: " + (Thread.activeCount() - 7));
        if (appVariables.getPause()) {
            b_timer.setText(Convert_time.time_to_string(seconds));
        }
    }

    /**
     * Функция отвечает за деактивацию кнопки таймера при выключении самого таймера
     *
     * @param number номер таймера
     * @param mode   режим работы таймера
     */
    public void setup_timer_button(int number, boolean mode) {
        Button timer = (Button) appVariables.getScene().lookup("#btn_timer" + number);
        if (mode) {
            timer.setStyle("-fx-background-color: #FF4500; ");
            timer.setText("Выключить");
        } else {
            timer.setStyle("-fx-background-color: #9ACD32; ");
            timer.setText("Включить");
        }
    }

    /**
     * Функция для запуска таймера
     */
    public void activate_timer() {

        // если до этого счетчики стояли на паузе
        if (appVariables.getSaved_data()[this.number - 1] != null && !appVariables.getSaved_data()[this.number - 1].split("_")[0].equals("deactivate")) {
            this.mode = appVariables.getSaved_data()[number - 1].split("_")[0];
            // отнимаем 1, потому что seconds прибавляет 1 в начале каждого цикла, т.е. на 1 больше нужного
            this.saved_time = Integer.parseInt(appVariables.getSaved_data()[number - 1].split("_")[1]) - 1;
            update_timer(Time_counter.this.number, Time_counter.this.mode, this.saved_time, 'g');
        } else {
            this.mode = "rest";
            update_timer(Time_counter.this.number, Time_counter.this.mode, 0, 'r');
            this.mode = "work";
            update_timer(Time_counter.this.number, Time_counter.this.mode, 0, 'g');
        }

//        Controller_for_main.setup_timer_button(Time_counter.this.number, true);
        setup_timer_button(Time_counter.this.number, true);
        //
        //
        //
        String[] data = appVariables.getSaved_data();
        data[this.number - 1] = null;
        appVariables.setSaved_data(data);
//        appVariables.getSaved_data()[this.number - 1] = null;

        this.timer = new Timer();
        class Timer_task extends TimerTask {
            int seconds = Time_counter.this.saved_time;
            char color;
            String text;

            @Override
            public void run() {
                // РАЗБЛОК
                Platform.runLater(() -> {
                    seconds++;
                    if (Time_counter.this.mode.equals("work")) {
                        work();
                    } else {
                        rest();
                    }
                });
            }

            private void work() {
                if (seconds == 1) {
                    color = 'g';
                } else {
                    color = '-';
                }
                if (seconds >= Time_counter.this.work_time + 1) {
                    Time_counter.this.update_timer(Time_counter.this.number, Time_counter.this.mode, 0, 'r');
                    System.out.println("next_timer");
                    color = 'g';
                    Time_counter.this.mode = "rest";
                    seconds = 1;
                    if (Time_counter.this.number == 1) {
                        text = "Выпрямите спину_Время для принятия правильного положения - " + Convert_time.time_to_string(Time_counter.this.rest_time);
                    } else if (Time_counter.this.number == 2) {
                        text = "Сделайте упражнения для глаз_Время для комплекса упражнений для глаз - " + Convert_time.time_to_string(Time_counter.this.rest_time);
                    } else {
                        text = "Сделайте зарядку_Время для зарядки - " + Time_counter.this.rest_time;
                    }
                    Notifications.create()
                            .title(text.split("_")[0])
                            .text(text.split("_")[1])
                            .showInformation();
//                     иконка приложения
//                            .graphic(new ImageView(Time_counter.scene.getRe);
                }
                Time_counter.this.update_timer(Time_counter.this.number, Time_counter.this.mode, seconds, color);
            }

            private void rest() {
                if (seconds == 0) {
                    color = 'g';
                } else {
                    color = '-';
                }
                if (seconds >= Time_counter.this.rest_time + 1) {
                    Time_counter.this.update_timer(Time_counter.this.number, Time_counter.this.mode, 0, 'r');
                    color = 'g';
                    Time_counter.this.mode = "work";
                    seconds = 1;
                    if (Time_counter.this.number != 1) {
                        if (Time_counter.this.number == 2) {
                            text = "Время выполнения упражнений для глаз истекло_Время, после которого будет следующий подход - " + Convert_time.time_to_string(Time_counter.this.rest_time);
                        } else {
                            text = "Время выполнения зарядки истекло_Время, после которого будет следующий подход - " + Convert_time.time_to_string(Time_counter.this.rest_time);
                        }
                        Notifications.create()
                                .title(text.split("_")[0])
                                .text(text.split("_")[1])
                                .showInformation();
                    }
                }
                Time_counter.this.update_timer(Time_counter.this.number, Time_counter.this.mode, seconds, color);
            }

        }
        Timer_task timerTask = new Timer_task();
        this.timer.schedule(timerTask, 24 * 10 ^ 6, 1000);
        this.saved_time = 0;
    }

    /**
     * Функция для выключения таймера
     */
    public void deactivate_timer() {
        try {
            this.timer.cancel();
            System.out.println("timer" + this.number + " остановлен");
        } catch (Exception ignored) {
            System.out.println("timer" + this.number + " не остановлен");
        }

//        Controller_for_main.setup_timer_button(Time_counter.this.number, false);
        setup_timer_button(Time_counter.this.number, false);
        this.mode = "rest";

        update_timer(Time_counter.this.number, Time_counter.this.mode, 0, 'r');
        this.mode = "work";
        update_timer(Time_counter.this.number, Time_counter.this.mode, 0, 'r');
        this.mode = "deactivate";
    }
}

