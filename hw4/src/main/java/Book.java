import java.util.List;

public class Book {
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
