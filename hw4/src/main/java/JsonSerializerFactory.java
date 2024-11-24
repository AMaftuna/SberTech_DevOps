import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class JsonSerializerFactory {

    public JsonSerializer generateSerializer(Class<?> clazz) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String className = clazz.getSimpleName() + "GeneratedSerializer";
        String sourceCode = generateSerializerCode(clazz, className);

        // Запись исходного кода в файл
        try (FileWriter writer = new FileWriter(className + ".java")) {
            writer.write(sourceCode);
        }

        // Компиляция исходного кода
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int result = compiler.run(null, null, null, className + ".java");
        if (result != 0) {
            throw new RuntimeException("Compilation failed.");
        }

        // Загрузка скомпилированного класса
        Class<?> serializerClass = Class.forName(className);

        // Создание экземпляра и возврат
        return (JsonSerializer) serializerClass.getDeclaredConstructor().newInstance();
    }

    // Генерация исходного кода сериализатора
    private String generateSerializerCode(Class<?> clazz, String className) {
        StringBuilder code = new StringBuilder();
        code.append("import org.json.JSONObject;\n")
                .append("import org.json.JSONArray;\n")
                .append("import java.util.Collection;\n")
                .append("public class ").append(className).append(" implements JsonSerializer {\n")
                .append("    @Override\n")
                .append("    public JSONObject serialize(Object obj) {\n")
                .append("        ").append(clazz.getName()).append(" instance = (").append(clazz.getName()).append(") obj;\n")
                .append("        JSONObject json = new JSONObject();\n");

        // Добавление методов-геттеров в сгенерированный код
        for (Method method : clazz.getDeclaredMethods()) {
            if (isGetter(method)) {
                String propertyName = getPropertyName(method);
                String methodName = method.getName();
                if (Collection.class.isAssignableFrom(method.getReturnType())) {
                    code.append("        json.put(\"").append(propertyName).append("\", new JSONArray(instance.").append(methodName).append("()));\n");
                } else if (method.getReturnType().isArray()) {
                    code.append("        json.put(\"").append(propertyName).append("\", new JSONArray(instance.").append(methodName).append("()));\n");
                } else {
                    code.append("        json.put(\"").append(propertyName).append("\", instance.").append(methodName).append("());\n");
                }
            }
        }

        code.append("        return json;\n")
                .append("    }\n")
                .append("}\n");

        return code.toString();
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
