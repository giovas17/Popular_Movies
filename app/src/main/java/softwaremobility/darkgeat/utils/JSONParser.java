package softwaremobility.darkgeat.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import softwaremobility.darkgeat.objects.Movie;
import softwaremobility.darkgeat.objects.Review;

import static softwaremobility.darkgeat.popularmovies1.MainActivity.obtainingScreenSize;

/**
 * Created by darkgeat on 8/21/15.
 */
public class JSONParser {

    public static ArrayList<Movie> getMoviesData(JSONObject object, Context context){

        final String BASE_PATH_PICTURE = "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE_PX;
        final String RESULT_ARRAY = "results";
        final String POSTER = "poster_path";
        final String TITLE = "original_title";
        final String ID = "id";
        final String DESCRIPTION = "overview";
        final String RATING = "vote_average";
        final String POPULARITY = "popularity";
        final String TOTAL_VOTES = "vote_count";
        final String PREVIEW = "backdrop_path";
        final String RELEASE_DATE = "release_date";
        final String GENRES = "genre_ids";
        final String BIGGEST_IMAGE_SIZE = "w500//";
        final String SMALLER_IMAGE_SIZE;

        //if the width of the screen is bigger than 1000px will set w500
        int width = obtainingScreenSize(context)[0];
        IMAGE_SIZE_PX = width > 1000 ? "w500//" : "w342//";
        SMALLER_IMAGE_SIZE = (IMAGE_SIZE_PX.contains("342")) ? "w185//" : "w342//";

        ArrayList<Movie> movies;

        try {
            JSONArray moviesArray = object.getJSONArray(RESULT_ARRAY);
            movies = new ArrayList<>();

            for (int i = 0 ; i < moviesArray.length() ; i++){
                Movie movie = new Movie();
                JSONObject obj = moviesArray.getJSONObject(i);
                String poster_path = obj.getString(POSTER);
                movie.setPoster_thumbnail(BASE_PATH_PICTURE + SMALLER_IMAGE_SIZE + poster_path);
                poster_path = BASE_PATH_PICTURE + IMAGE_SIZE_PX + poster_path;
                String preview_path = obj.getString(PREVIEW);
                preview_path = BASE_PATH_PICTURE + BIGGEST_IMAGE_SIZE + preview_path;
                movie.setPoster_image_path(poster_path);
                movie.setPreview_image_path(preview_path);
                movie.setId(obj.getLong(ID));
                movie.setTitle(obj.getString(TITLE));
                movie.setRating(obj.getDouble(RATING));
                movie.setDescription(obj.getString(DESCRIPTION));
                movie.setPopularity(obj.getDouble(POPULARITY));
                movie.setVote_count(obj.getInt(TOTAL_VOTES));
                movie.setRelease_date(obj.getString(RELEASE_DATE));
                movie.setGenres(Utils.getGenresMovie(context, obj.getJSONArray(GENRES)));
                movies.add(movie);
            }
            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
/**
    public ArrayList<Review> parseToListReviews(JSONObject object) {

        // ----- Keys from JSON object -------

        final String RESULT_ARRAY = "results";
        final String PAGE = "page";
        final String CONTENT = "content";
        final String AUTHOR = "author";
        final String TOTAL_PAGES = "total_pages";
        final String TOTAL_RESULTS = "total_results";


        ArrayList<Movie> movies;

        try {
            JSONArray moviesArray = object.getJSONArray(RESULT_ARRAY);
            movies = new ArrayList<>();

            for (int i = 0; i < moviesArray.length(); i++) {
                Review review = new Review();
                JSONObject obj = moviesArray.getJSONObject(i);
                String poster_path = obj.getString(POSTER);
                review.setPoster_thumbnail(BASE_PATH_PICTURE + SMALLER_IMAGE_SIZE + poster_path);
                poster_path = BASE_PATH_PICTURE + IMAGE_SIZE_PX + poster_path;
                String preview_path = obj.getString(PREVIEW);
                preview_path = BASE_PATH_PICTURE + BIGGEST_IMAGE_SIZE + preview_path;
                review.setPoster_image_path(poster_path);
                review.setPreview_image_path(preview_path);
                review.setId(obj.getLong(ID));
                review.setTitle(obj.getString(TITLE));
                review.setRating(obj.getDouble(RATING));
                review.setDescription(obj.getString(DESCRIPTION));
                review.setPopularity(obj.getDouble(POPULARITY));
                review.setVote_count(obj.getInt(TOTAL_VOTES));
                review.setRelease_date(obj.getString(RELEASE_DATE));
                review.setGenres(Utils.getGenresMovie(this, obj.getJSONArray(GENRES)));
                movies.add(review);
            }
            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
**/
}
