package softwaremobility.darkgeat.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import softwaremobility.darkgeat.fragments.Detail;
import softwaremobility.darkgeat.popularmovies1.R;

/**
 * Created by darkgeat on 14/07/15.
 */
public class ImageAdapter extends ArrayAdapter {

    private Context mContext;
    private String[] images;

    public ImageAdapter(Context context, String[] urls) {
        super(context, R.layout.poster_preview_item, urls);
        mContext = context;
        images = urls;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.poster_preview_item, null, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

            Picasso.with(mContext).load(images[position]).into(viewHolder.posterMovie);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
            Picasso.with(mContext).load(images[position]).into(viewHolder.posterMovie);
        }
        viewHolder.posterMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActionBarActivity)mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.principal_container,new Detail()).commit();
            }
        });


        return convertView;
    }

    static class ViewHolder {
        public final ImageView posterMovie;

        public ViewHolder(View v){
            posterMovie = (ImageView)v.findViewById(R.id.imagePosterItem);
        }
    }
}
