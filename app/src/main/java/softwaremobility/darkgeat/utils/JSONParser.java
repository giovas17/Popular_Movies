package softwaremobility.darkgeat.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import softwaremobility.darkgeat.objects.Review;

/**
 * Created by darkgeat on 8/21/15.
 */
public class JSONParser {

    /**public ArrayList<Review> parseToListReviews(JSONObject object) {

        // ----- Keys from JSON object -------

        final String RESULT_ARRAY = "results";
        final String PAGE = "page";
        final String CONTENT = "content";
        final String AUTHOR = "author";
        final String TOTAL_PAGES = "total_pages";
        final String TOTAL_RESULTS = "total_results";
        final String RELEASE_DATE = "release_date";
        final String GENRES = "genre_ids";
        final String BIGGEST_IMAGE_SIZE = "w500//";
        final String SMALLER_IMAGE_SIZE;


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
    }**/

}
