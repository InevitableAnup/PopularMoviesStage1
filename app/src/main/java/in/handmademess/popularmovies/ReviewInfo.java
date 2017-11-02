package in.handmademess.popularmovies;

/**
 * Created by Anup on 26-09-2017.
 */

public class ReviewInfo {

    public String id,author,content;

    public ReviewInfo() {
    }

    public ReviewInfo(String id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
