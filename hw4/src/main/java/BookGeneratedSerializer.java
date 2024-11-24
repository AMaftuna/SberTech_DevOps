import org.json.JSONObject;
import org.json.JSONArray;
import java.util.Collection;
public class BookGeneratedSerializer implements JsonSerializer {
    @Override
    public JSONObject serialize(Object obj) {
        Book instance = (Book) obj;
        JSONObject json = new JSONObject();
        json.put("tags", new JSONArray(instance.getTags()));
        json.put("title", instance.getTitle());
        json.put("author", instance.getAuthor());
        json.put("pages", instance.getPages());
        json.put("genres", new JSONArray(instance.getGenres()));
        json.put("available", instance.isAvailable());
        return json;
    }
}
