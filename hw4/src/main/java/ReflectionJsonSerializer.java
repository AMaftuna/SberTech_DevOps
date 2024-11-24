import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

public class ReflectionJsonSerializer implements JsonSerializer {

    @Override
    public JSONObject serialize(Object obj) {
        Class<?> clazz = obj.getClass();
        JSONObject jsonObject = new JSONObject();

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (isGetter(method)) {
                try {
                    String propertyName = getPropertyName(method);
                    Object value = method.invoke(obj); // Вызов геттера

                    if (value != null) {
                        if (value instanceof Collection) {
                            JSONArray jsonArray = new JSONArray((Collection<?>) value);
                            jsonObject.put(propertyName, jsonArray);
                        } else if (value.getClass().isArray()) {
                            JSONArray jsonArray = new JSONArray(value);
                            jsonObject.put(propertyName, jsonArray);
                        } else {
                            jsonObject.put(propertyName, value);
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    // Обработка исключения
                    e.printStackTrace(); // Выводим стек ошибки для отладки
                }
            }
        }

        return jsonObject;
    }

    // Проверка, является ли метод геттером
    private boolean isGetter(Method method) {
        if (method.getParameterCount() != 0) return false;
        if (method.getName().startsWith("get") && method.getReturnType() != void.class) return true;
        return method.getName().startsWith("is") && method.getReturnType() == boolean.class;
    }

    // Получение имени свойства из метода-геттера
    private String getPropertyName(Method method) {
        String methodName = method.getName();
        if (methodName.startsWith("get")) {
            return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
        } else if (methodName.startsWith("is")) {
            return Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
        }
        return null;
    }
}
