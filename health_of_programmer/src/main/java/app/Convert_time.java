package app;

/**
 * Класс для конвертации времени
 */
public class Convert_time {
    /**
     * Функция для преобразования времени в число
     *
     * @param str_time строка со временем
     * @return количество секунд
     */
    public static int time_to_int(String str_time) {
        int int_time = 0;
        try {
            if (str_time.length() > 8) {
                int_time = -1; // если число вышло за рамки диапозона int
                throw new NumberFormatException();
            }

            String[] nums = str_time.split(":");
            if (nums.length == 3 || nums.length == 2) {
                int h = 0;
                int m = Integer.parseInt(nums[0]);
                int s = Integer.parseInt(nums[1]);
                if (nums.length == 3) {
                    h = Integer.parseInt(nums[0]);
                    m = Integer.parseInt(nums[1]);
                    s = Integer.parseInt(nums[2]);
                }
                if (h >= 0 && m >= 0 && s >= 0) {
                    int_time = h * 60 * 60 + m * 60 + s;
                }
            } else {
                int_time = Integer.parseInt(str_time);
            }
            if (int_time <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ignored) {
        }
        return int_time;
    }

    /**
     * Функция для преобразования времени в строку
     *
     * @param int_time количество секунд
     * @return строка со временем
     */
    public static String time_to_string(int int_time) {
        // приводим число к виду __:__:__
        int[] hms = new int[3];
        String string_time = "";
        hms[0] = int_time / 3600;
        hms[1] = (int_time % 3600) / 60;
        hms[2] = (int_time % 3600) % 60;
        for (int el : hms) {
            if (el < 10) string_time += "0" + el + ":";
            else string_time += el + ":";
        }
        string_time = string_time.substring(0, string_time.length() - 1);

        // исключаем незначащие цифры
        int i = 0;
        while (true) {
            try {
                if (string_time.charAt(i) == '0' || string_time.charAt(i) == ':') {
                    string_time = string_time.substring(1);
                } else {
                    break;
                }
            } catch (StringIndexOutOfBoundsException e) {
                string_time = "00:00";
                break;
            }
        }
        return string_time;
    }
}
