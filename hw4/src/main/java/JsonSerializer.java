import org.json.JSONObject;

public interface JsonSerializer {
    /**
     * Сериализует переданный объект в формат JSON.
     *
     * @param obj объект для сериализации
     * @return JSON представление объекта
     */
    JSONObject serialize(Object obj);
}
