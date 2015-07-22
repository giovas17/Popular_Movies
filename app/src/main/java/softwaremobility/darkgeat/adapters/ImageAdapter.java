package softwaremobility.darkgeat.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import softwaremobility.darkgeat.fragments.Detail;
import softwaremobility.darkgeat.objects.Movie;
import softwaremobility.darkgeat.popularmovies1.R;

/**
 * Created by darkgeat on 14/07/15.
 */
public class ImageAdapter extends ArrayAdapter {

    private Context mContext;
    private List<Movie> movies;

    public ImageAdapter(Context context, List<Movie> all_movies) {
        super(context, R.layout.poster_preview_item, all_movies);
        mContext = context;
        movies = all_movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.poster_preview_item, null, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Picasso.with(mContext).load(movies.get(position).getPoster_image_path()).into(viewHolder.posterMovie);
        viewHolder.posterMovie.setTag(movies.get(position));
        viewHolder.posterMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putParcelable("movieSelected", movies.get(position));
                Detail detail = new Detail();
                detail.setArguments(args);

                ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.principal_container, detail).commit();
            }
        });
        viewHolder.titleMovie.setText(movies.get(position).getTitle());
        viewHolder.popularityMovie.setText(mContext.getString(R.string.popularity_text, movies.get(position).getPopularity()));
        viewHolder.ratingMovie.setText(mContext.getString(R.string.rating_text,movies.get(position).getRating()));

        return convertView;
    }

    static class ViewHolder {
        public final ImageView posterMovie;
        public final TextView titleMovie;
        public final TextView popularityMovie;
        public final TextView ratingMovie;

        public ViewHolder(View v){
            posterMovie = (ImageView)v.findViewById(R.id.imagePosterItem);
            titleMovie = (TextView)v.findViewById(R.id.textTitleMovie);
            popularityMovie = (TextView)v.findViewById(R.id.textPopularity);
            ratingMovie = (TextView)v.findViewById(R.id.textRating);
        }
    }
}
