package softwaremobility.darkgeat.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by darkgeat on 8/20/15.
 */
public class Review implements Parcelable{
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

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            Review aux = new Review();
            aux.setAuthor(source.readString());
            aux.setContent(source.readString());
            aux.setUrl(source.readString());
            aux.setPage(source.readInt());
            return aux;
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
        dest.writeInt(page);
    }
}
