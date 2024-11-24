import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneratedJsonSerializerTest {

    @Test
    void testGeneratedSerializer() throws Exception {
        Book book = new Book("Effective Java", "Joshua Bloch", 416,
                Arrays.asList("Programming", "Java"),
                new String[]{"Best Practices", "Java"}, true);

        JsonSerializerFactory factory = new JsonSerializerFactory();
        JsonSerializer serializer = factory.generateSerializer(Book.class);

        JSONObject json = serializer.serialize(book);

        JSONObject expectedJson = new JSONObject();
        expectedJson.put("title", "Effective Java");
        expectedJson.put("author", "Joshua Bloch");
        expectedJson.put("pages", 416);
        expectedJson.put("genres", new JSONArray(Arrays.asList("Programming", "Java")));
        expectedJson.put("tags", new JSONArray(Arrays.asList("Best Practices", "Java")));
        expectedJson.put("available", true);

        assertEquals(expectedJson.toString(), json.toString());
    }
}
