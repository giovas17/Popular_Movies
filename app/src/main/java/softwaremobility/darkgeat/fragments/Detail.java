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

import java.text.DecimalFormat;

import softwaremobility.darkgeat.listeners.onMovieSelectedListener;
import softwaremobility.darkgeat.network.NetworkConnection;
import softwaremobility.darkgeat.objects.Movie;
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

        if(MainActivity.two_views){
            previewMoview = (ImageView)view.findViewById(R.id.image_preview);
        }else {
            Picasso.with(getActivity()).load(movie.getPoster_thumbnail()).into(posterMovie);
        }
        titleMovie.setText(movie.getTitle());
        popularityMovie.setText(format.format(movie.getPopularity()));
        descriptionMovie.setText(movie.getDescription());
        dateMovie.setText(movie.getRelease_date());
        ratingMovie.setText(getActivity().getString(R.string.rating_value,movie.getRating(),movie.getVote_count()));
        genresMovies.setText(movie.getGenres());

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
        NetworkConnection connection = new NetworkConnection(getActivity(), NetworkConnection.Request.videoRequest);
        connection.execute(new String[]{String.valueOf(movie.getId())});
    }
}
