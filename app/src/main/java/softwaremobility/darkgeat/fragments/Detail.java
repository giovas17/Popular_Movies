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

import softwaremobility.darkgeat.objects.Movie;
import softwaremobility.darkgeat.popularmovies1.R;

/**
 * Created by darkgeat on 20/07/15.
 */
public class Detail extends Fragment {

    private Movie movie = null;
    public ImageView posterMovie;
    public TextView titleMovie;
    public TextView popularityMovie;
    public TextView ratingMovie;
    public TextView descriptionMovie;
    public TextView dateMovie;

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

        Picasso.with(getActivity()).load(movie.getPoster_thumbnail()).into(posterMovie);
        titleMovie.setText(movie.getTitle());
        popularityMovie.setText(format.format(movie.getPopularity()));
        descriptionMovie.setText(movie.getDescription());
        dateMovie.setText(movie.getRelease_date());
        ratingMovie.setText(getActivity().getString(R.string.rating_value,movie.getRating(),movie.getVote_count()));

        return view;
    }
}
