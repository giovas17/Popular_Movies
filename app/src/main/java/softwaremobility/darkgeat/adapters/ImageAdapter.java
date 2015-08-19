package softwaremobility.darkgeat.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import softwaremobility.darkgeat.fragments.Detail;
import softwaremobility.darkgeat.listeners.onMovieSelectedListener;
import softwaremobility.darkgeat.objects.Movie;
import softwaremobility.darkgeat.popularmovies1.DetailActivity;
import softwaremobility.darkgeat.popularmovies1.MainActivity;
import softwaremobility.darkgeat.popularmovies1.R;

/**
 * Created by darkgeat on 14/07/15.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Movie> movies;
    private onMovieSelectedListener listener;
    private boolean isFirstTime;

    public ImageAdapter(Context context, ArrayList<Movie> all_movies) {
        mContext = context;
        movies = all_movies;
        isFirstTime = true;
    }
    public ImageAdapter(Context context, ArrayList<Movie> all_movies, onMovieSelectedListener listener) {
        mContext = context;
        movies = all_movies;
        this.listener = listener;
        isFirstTime = true;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        View convertView = inflater.inflate(R.layout.poster_preview_item, null, false);
        viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Picasso.with(mContext).load(movies.get(position).getPoster_image_path()).into(viewHolder.posterMovie);
        viewHolder.posterMovie.setTag(movies.get(position));
        final int pos = position;
        viewHolder.posterMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.two_views) {
                    listener.onMovieSelected(movies.get(pos));
                } else {
                    Bundle args = new Bundle();
                    args.putParcelable("movieSelected", movies.get(pos));
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtras(args);
                    mContext.startActivity(intent);
                }
            }
        });
        viewHolder.titleMovie.setText(movies.get(position).getTitle());
        viewHolder.popularityMovie.setText(mContext.getString(R.string.popularity_text, movies.get(position).getPopularity()));
        viewHolder.ratingMovie.setText(mContext.getString(R.string.rating_text,movies.get(position).getRating()));
        if(MainActivity.two_views && position == 0 && isFirstTime){
            listener.onMovieSelected(movies.get(position));
            isFirstTime = false;
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView posterMovie;
        public final TextView titleMovie;
        public final TextView popularityMovie;
        public final TextView ratingMovie;

        public ViewHolder(View v){
            super(v);
            posterMovie = (ImageView)v.findViewById(R.id.imagePosterItem);
            titleMovie = (TextView)v.findViewById(R.id.textTitleMovie);
            popularityMovie = (TextView)v.findViewById(R.id.textPopularity);
            ratingMovie = (TextView)v.findViewById(R.id.textRating);
        }
    }
}
