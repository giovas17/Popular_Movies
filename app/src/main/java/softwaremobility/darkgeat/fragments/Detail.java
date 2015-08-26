package softwaremobility.darkgeat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import softwaremobility.darkgeat.objects.Trailer;
import softwaremobility.darkgeat.popularmovies1.MainActivity;
import softwaremobility.darkgeat.popularmovies1.R;
import softwaremobility.darkgeat.utils.JSONParser;

/**
 * Created by darkgeat on 20/07/15.
 */
public class Detail extends Fragment implements onMovieSelectedListener {

    private Movie movie = null;
    private ImageView posterMovie;
    private ImageView previewMoview;
    private TextView titleMovie;
    private TextView popularityMovie;
    private TextView ratingMovie;
    private TextView descriptionMovie;
    private TextView dateMovie;
    private TextView genresMovies;
    private ViewPager viewPagerTrailers;
    private onNetworkDataListener listener;
    private NetworkConnection.Request type;
    private RecyclerView reviews;
    private ArrayList<Review> mReviews;
    private ArrayList<Trailer> mTrailers;
    private ImageView backNavigation;
    private ImageView nextNavigation;
    private PagerAdapter adapter;

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
        reviews = (RecyclerView)view.findViewById(R.id.listReviews);
        viewPagerTrailers = (ViewPager)view.findViewById(R.id.pagerTrailers);

        if(savedInstanceState != null){
            movie = savedInstanceState.getParcelable("movie");
            mReviews = savedInstanceState.getParcelableArrayList("reviews");
            if(MainActivity.two_views) {
                previewMoview = (ImageView)view.findViewById(R.id.image_preview);
                Picasso.with(getActivity()).load(movie.getPoster_thumbnail()).into(posterMovie);
                Picasso.with(getActivity()).load(movie.getPreview_image_path()).into(previewMoview);
            }
            updatingReviews(mReviews);
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
                    refreshTrailers(object);
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

        backNavigation = (ImageView) view.findViewById(R.id.navBack);
        backNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (viewPagerTrailers != null) {
                    if (viewPagerTrailers.getCurrentItem() > 0) {
                        backNavigation.setEnabled(true);
                        viewPagerTrailers.setCurrentItem(viewPagerTrailers.getCurrentItem() - 1);
                        if (viewPagerTrailers.getCurrentItem() == 0) {
                            backNavigation.setEnabled(false);
                        }
                    } else {
                        backNavigation.setEnabled(false);
                    }

                    if (viewPagerTrailers.getCurrentItem() == adapter.getCount() - 1) {
                        nextNavigation.setEnabled(false);
                    } else {
                        nextNavigation.setEnabled(true);
                    }
                }
            }
        });

        nextNavigation = (ImageView) view.findViewById(R.id.navForward);
        nextNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (viewPagerTrailers != null) {
                    viewPagerTrailers.setCurrentItem(viewPagerTrailers.getCurrentItem() + 1);
                    if (viewPagerTrailers.getCurrentItem() == adapter.getCount() - 1) {
                        nextNavigation.setEnabled(false);
                    } else {
                        nextNavigation.setEnabled(true);
                    }

                    if (viewPagerTrailers.getCurrentItem() > 0) {
                        backNavigation.setEnabled(true);
                    } else {
                        backNavigation.setEnabled(false);
                    }
                }
            }
        });

        return view;
    }

    private void refreshTrailers(JSONObject object){
        mTrailers = JSONParser.parseToListTrailers(object);
        updatingTrailers(mTrailers);
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
        updatingTrailers(mTrailers);
        type = NetworkConnection.Request.reviewsRequest;
        NetworkConnection connection = new NetworkConnection(getActivity(), type, listener);
        connection.execute(new String[]{String.valueOf(movie.getId())});
    }

    private void updatingTrailers(ArrayList<Trailer> trailerArrayList) {
        adapter = new PagerAdapter(getActivity().getSupportFragmentManager(),trailerArrayList,movie.getPoster_image_path());
        viewPagerTrailers.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movie",movie);
        if(mReviews != null && mReviews.size() > 0){
            outState.putParcelableArrayList("reviews",mReviews);
        }
        super.onSaveInstanceState(outState);
    }

    public class PagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Trailer> trailers;
        private String imagePath;

        public PagerAdapter(FragmentManager fm, ArrayList<Trailer> trailerArrayList, String image){
            super(fm);
            trailers = trailerArrayList;
            imagePath = image;
        }

        @Override
        public Fragment getItem(int position) {
            return TrailerFragment.Create(trailers.get(position),imagePath);
        }

        @Override
        public int getCount() {
            return trailers.size();
        }
    }



}
