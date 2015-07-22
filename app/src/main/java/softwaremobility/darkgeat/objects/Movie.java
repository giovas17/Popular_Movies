package softwaremobility.darkgeat.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by darkgeat on 21/07/15.
 */
public class Movie implements Parcelable{
    private String title;
    private long id;
    private String description;
    private String release_date;
    private double popularity;
    private double rating;
    private int vote_count;
    private String poster_image_path;
    private String preview_image_path;

    public Movie() {
        title = "";
        id = 0;
        description = "";
        release_date = "";
        popularity = 0f;
        rating = 0f;
        vote_count = 0;
        poster_image_path = "";
        preview_image_path = "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public String getPoster_image_path() {
        return poster_image_path;
    }

    public void setPoster_image_path(String poster_image_path) {
        this.poster_image_path = poster_image_path;
    }

    public String getPreview_image_path() {
        return preview_image_path;
    }

    public void setPreview_image_path(String preview_image_path) {
        this.preview_image_path = preview_image_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            Movie aux = new Movie();
            aux.setId(source.readLong());
            aux.setTitle(source.readString());
            aux.setDescription(source.readString());
            aux.setRelease_date(source.readString());
            aux.setPopularity(source.readDouble());
            aux.setRating(source.readDouble());
            aux.setVote_count(source.readInt());
            aux.setPoster_image_path(source.readString());
            aux.setPreview_image_path(source.readString());
            return aux;
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(release_date);
        dest.writeDouble(popularity);
        dest.writeDouble(rating);
        dest.writeInt(vote_count);
        dest.writeString(poster_image_path);
        dest.writeString(preview_image_path);
    }
}
