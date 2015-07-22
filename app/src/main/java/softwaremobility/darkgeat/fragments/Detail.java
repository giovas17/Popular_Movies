package softwaremobility.darkgeat.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import softwaremobility.darkgeat.objects.Movie;
import softwaremobility.darkgeat.popularmovies1.R;

/**
 * Created by darkgeat on 20/07/15.
 */
public class Detail extends Fragment {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView header;
    private int mutedColor = R.attr.colorPrimary;
    private Movie movie = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie = getArguments().getParcelable("movieSelected");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail,container,false);

        collapsingToolbarLayout = (CollapsingToolbarLayout)view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(movie.getTitle());

        header = (ImageView)view.findViewById(R.id.header);
        Picasso.with(getActivity()).load(movie.getPreview_image_path()).into(header);

        /*Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                mutedColor = palette.getMutedColor(R.attr.colorPrimary);
                collapsingToolbarLayout.setContentScrimColor(mutedColor);
            }
        });*/

        return view;
    }
}
