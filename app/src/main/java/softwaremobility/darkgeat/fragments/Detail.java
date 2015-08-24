package softwaremobility.darkgeat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import softwaremobility.darkgeat.adapters.ReviewsAdapter;
import softwaremobility.darkgeat.listeners.onMovieSelectedListener;
import softwaremobility.darkgeat.listeners.onNetworkDataListener;
import softwaremobility.darkgeat.network.NetworkConnection;
import softwaremobility.darkgeat.objects.Movie;
import softwaremobility.darkgeat.objects.Review;
import softwaremobility.darkgeat.popularmovies1.MainActivity;
import softwaremobility.darkgeat.popularmovies1.R;
import softwaremobility.darkgeat.utils.JSONParser;

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
    private RecyclerView reviews;
    private ArrayList<Review> mReviews;
    private boolean isPreviusState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movie = getArguments().getParcelable("movieSelected");
        isPreviusState = false;
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
        reviews = (RecyclerView)view.findViewById(R.id.listReviews);

        if(savedInstanceState != null){
            movie = savedInstanceState.getParcelable("movie");
            mReviews = savedInstanceState.getParcelableArrayList("reviews");
            if(MainActivity.two_views) {
                previewMoview = (ImageView)view.findViewById(R.id.image_preview);
                Picasso.with(getActivity()).load(movie.getPoster_thumbnail()).into(posterMovie);
                Picasso.with(getActivity()).load(movie.getPreview_image_path()).into(previewMoview);
                updatingReviews(mReviews);
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
                }else if(type == NetworkConnection.Request.reviewsRequest){
                    refreshReviews(object);
                }
            }
        };

        if(MainActivity.two_views){
            previewMoview = (ImageView)view.findViewById(R.id.image_preview);
        }else {
            Picasso.with(getActivity()).load(movie.getPoster_thumbnail()).into(posterMovie);
            type = NetworkConnection.Request.videoRequest;
            if(savedInstanceState == null) {
                NetworkConnection connection = new NetworkConnection(getActivity(), type, listener);
                connection.execute(new String[]{String.valueOf(movie.getId())});
            }
        }

        return view;
    }

    private void refreshReviews(JSONObject object) {
        mReviews = JSONParser.parseToListReviews(object);
        updatingReviews(mReviews);
    }

    private void updatingReviews(ArrayList<Review> mReviews) {
        if(mReviews == null || mReviews.size() == 0){
            mReviews = new ArrayList<>();
            Review review = new Review(getString(R.string.app_name),"There are no Reviews","",0);
            mReviews.add(review);
        }
        reviews.setHasFixedSize(true);
        reviews.setLayoutManager(new LinearLayoutManager(getActivity()));
        ReviewsAdapter adapter = new ReviewsAdapter(getActivity(),mReviews);
        reviews.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
        if (MainActivity.two_views) {
            type = NetworkConnection.Request.videoRequest;
            NetworkConnection connection = new NetworkConnection(getActivity(), type, listener);
            connection.execute(new String[]{String.valueOf(movie.getId())});
        }

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

}
