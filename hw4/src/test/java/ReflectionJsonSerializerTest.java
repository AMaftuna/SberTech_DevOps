import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReflectionJsonSerializerTest {

    public static class Book {
        private final String title;
        private final String author;
        private final int pages;
        private final List<String> genres;
        private final String[] tags;
        private final boolean available;

        public Book(String title, String author, int pages, List<String> genres, String[] tags, boolean available) {
            this.title = title;
            this.author = author;
            this.pages = pages;
            this.genres = genres;
            this.tags = tags;
            this.available = available;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public int getPages() {
            return pages;
        }

        public List<String> getGenres() {
            return genres;
        }

        public String[] getTags() {
            return tags;
        }

        public boolean isAvailable() {
            return available;
        }
    }

    @Test
    void testSerialize() throws Exception {
        Book book = new Book("Effective Java", "Joshua Bloch", 416,
                Arrays.asList("Programming", "Java"),
                new String[]{"Best Practices", "Java"}, true);

        ReflectionJsonSerializer serializer = new ReflectionJsonSerializer();
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
