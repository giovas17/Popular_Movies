package softwaremobility.darkgeat.objects;

/**
 * Created by darkgeat on 8/20/15.
 */
public class Review {
    private String author, content,url;
    private int page;

    public Review() {
        this.author = "";
        this.content = "";
        this.url = "";
        this.page = 0;
    }

    public Review(String author, String content, String url, int page) {
        this.author = author;
        this.content = content;
        this.url = url;
        this.page = page;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
