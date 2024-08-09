package app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

/**
 * Класс для взаимодействия с json-файлом
 */
public class Json_utils {
    private static final Gson gson = new GsonBuilder().create();

    /**
     * Функция получения данных из json-файла
     *
     * @param value_name ключ, значение по которому хотим получить
     * @return значение по ключу
     */
    public static String get_value(String value_name) {
        try {
            FileReader json_file = new FileReader("src/main/resources/data.json");
            JsonObject jsonObject = gson.fromJson(json_file, JsonObject.class);
            String json_text = String.valueOf(jsonObject.get(value_name));

            System.out.println("get " + value_name + " = " + json_text);
            if (json_text.equals("null")) {
                Event_handling.make_error("Ошибка json", "Значения " + value_name + " нет в базе данных", false);
            }
            return json_text.substring(1, json_text.length() - 1);
        } catch (Exception e) {
            Event_handling.make_error("Ошибка json", "Не удалось получить запрашиваемые данные", true);
            return "";
        }
    }

    /**
     * Функция для получения массива
     *
     * @param value_name ключ, значение по которому хотим получить
     * @return значение по ключу
     */
    public static Map<String, String> get_array(String value_name) {
        try {
            FileReader json_file = new FileReader("src/main/resources/data.json");
            Map json = gson.fromJson(json_file, Map.class);
            Map<String, String> myMap = (Map<String, String>) json.get(value_name);

            System.out.println("get array " + value_name + " = " + myMap);
            if (myMap == null) {
                Event_handling.make_error("Ошибка json", "Значения " + value_name + " нет в базе данных", false);
            } else if (myMap.isEmpty()) {
                Event_handling.make_error("Ошибка json", "Значения " + value_name + " в базе данных пустое", false);
            }
            return myMap;
        } catch (Exception e) {
            Event_handling.make_error("Ошибка json", "Не удалось получить запрашиваемые данные", false);
            return null;
        }
    }

    /**
     * Функция для изменения значения в массиве
     *
     * @param value_name ключ, значение по которому хотим изменить
     * @param value      значение, которое хотим записать
     */
    public static void change_value(String value_name, String value) {
        try {
            // нужно, чтобы не засорять консоль
            if (!(value_name.equals("current") && value.isEmpty())) {
                System.out.println("change " + value_name + " -> " + value);
            }

            FileReader reader = new FileReader("src/main/resources/data.json");
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            JsonWriter writer = new JsonWriter(new FileWriter("src/main/resources/data.json"));
            jsonObject.addProperty(value_name, value);
            gson.toJson(jsonObject, writer);
            writer.close();
        } catch (Exception e) {
            Event_handling.make_error("Ошибка json", "Не удалось записать данные", true);
        }
    }
}