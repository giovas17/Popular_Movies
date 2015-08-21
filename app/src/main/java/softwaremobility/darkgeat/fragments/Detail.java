package softwaremobility.darkgeat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import softwaremobility.darkgeat.listeners.onMovieSelectedListener;
import softwaremobility.darkgeat.listeners.onNetworkDataListener;
import softwaremobility.darkgeat.network.NetworkConnection;
import softwaremobility.darkgeat.objects.Movie;
import softwaremobility.darkgeat.objects.Review;
import softwaremobility.darkgeat.popularmovies1.MainActivity;
import softwaremobility.darkgeat.popularmovies1.R;

/**
 * Created by darkgeat on 20/07/15.
 */
public class Detail extends Fragment implements onMovieSelectedListener {

    private Movie movie = null;
    public ImageView posterMovie;
    public ImageView previewMoview;
    public TextView titleMovie;
    public TextView popularityMovie;
    public TextView ratingMovie;
    public TextView descriptionMovie;
    public TextView dateMovie;
    public TextView genresMovies;
    private onNetworkDataListener listener;
    private NetworkConnection.Request type;
    private ArrayList<Review> mReviews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            movie = getArguments().getParcelable("movieSelected");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail,container,false);

        DecimalFormat format = new DecimalFormat("##0");

        posterMovie = (ImageView)view.findViewById(R.id.image_movie_detail);
        titleMovie = (TextView)view.findViewById(R.id.title_movie_detail);
        popularityMovie = (TextView)view.findViewById(R.id.popularity_movie_detail);
        ratingMovie = (TextView)view.findViewById(R.id.rating_movie_detail);
        descriptionMovie = (TextView)view.findViewById(R.id.description_movie_detail);
        dateMovie = (TextView)view.findViewById(R.id.date_release_movie_detail);
        genresMovies = (TextView)view.findViewById(R.id.genres_movie_detail);

        if(savedInstanceState != null){
            movie = savedInstanceState.getParcelable("movie");
            if(MainActivity.two_views) {
                previewMoview = (ImageView)view.findViewById(R.id.image_preview);
                Picasso.with(getActivity()).load(movie.getPoster_thumbnail()).into(posterMovie);
                Picasso.with(getActivity()).load(movie.getPreview_image_path()).into(previewMoview);
            }
        }
        titleMovie.setText(movie.getTitle());
        popularityMovie.setText(format.format(movie.getPopularity()));
        descriptionMovie.setText(movie.getDescription());
        dateMovie.setText(movie.getRelease_date());
        ratingMovie.setText(getActivity().getString(R.string.rating_value, movie.getRating(), movie.getVote_count()));
        genresMovies.setText(movie.getGenres());

        listener = new onNetworkDataListener() {
            @Override
            public void onReceivedData(JSONObject object) {
                if(type == NetworkConnection.Request.videoRequest) {
                    refreshDataReviews();
                }
            }
        };

        if(MainActivity.two_views){
            previewMoview = (ImageView)view.findViewById(R.id.image_preview);
        }else {
            Picasso.with(getActivity()).load(movie.getPoster_thumbnail()).into(posterMovie);
            type = NetworkConnection.Request.videoRequest;
            NetworkConnection connection = new NetworkConnection(getActivity(), type,listener);
            connection.execute(new String[]{String.valueOf(movie.getId())});
        }

        return view;
    }

    @Override
    public void onMovieSelected(Movie movieSelected) {
        movie = movieSelected;
        DecimalFormat format = new DecimalFormat("##0");
        Picasso.with(getActivity()).load(movie.getPoster_thumbnail()).into(posterMovie);
        Picasso.with(getActivity()).load(movie.getPreview_image_path()).into(previewMoview);
        titleMovie.setText(movie.getTitle());
        popularityMovie.setText(format.format(movie.getPopularity()));
        descriptionMovie.setText(movie.getDescription());
        dateMovie.setText(movie.getRelease_date());
        ratingMovie.setText(getActivity().getString(R.string.rating_value, movie.getRating(), movie.getVote_count()));
        genresMovies.setText(movie.getGenres());
        type = NetworkConnection.Request.videoRequest;
        NetworkConnection connection = new NetworkConnection(getActivity(), type,listener);
        connection.execute(new String[]{String.valueOf(movie.getId())});

    }

    private void refreshDataReviews(){
        type = NetworkConnection.Request.reviewsRequest;
        NetworkConnection connection = new NetworkConnection(getActivity(), type, listener);
        connection.execute(new String[]{String.valueOf(movie.getId())});
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movie",movie);
        if(mReviews != null && mReviews.size() > 0){
            outState.putParcelableArrayList("reviews",mReviews);
        }
        super.onSaveInstanceState(outState);
    }
    /**
    public ArrayList<Review> getReviews(JSONObject object){

        // ----- Keys from JSON object -------

        final String RESULT_ARRAY = "results";
        final String DESCRIPTION = "overview";
        final String RATING = "vote_average";
        final String POPULARITY = "popularity";
        final String TOTAL_VOTES = "vote_count";
        final String PREVIEW = "backdrop_path";
        final String RELEASE_DATE = "release_date";
        final String GENRES = "genre_ids";
        final String BIGGEST_IMAGE_SIZE = "w500//";
        final String SMALLER_IMAGE_SIZE;


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
                movie.setGenres(Utils.getGenresMovie(this, obj.getJSONArray(GENRES)));
                movies.add(movie);
            }
            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }**/
}
