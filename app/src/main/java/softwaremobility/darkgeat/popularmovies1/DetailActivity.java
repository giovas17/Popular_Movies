package softwaremobility.darkgeat.popularmovies1;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import softwaremobility.darkgeat.fragments.Detail;
import softwaremobility.darkgeat.listeners.onFavouriteSelectedListener;
import softwaremobility.darkgeat.objects.Movie;

/**
 * Created by darkgeat on 22/07/15.
 */
public class DetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView header;
    private FloatingActionButton favorites;
    private Movie movie;
    private onFavouriteSelectedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movie = getIntent().getExtras().getParcelable("movieSelected");

        toolbar = (Toolbar)findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(5f);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(movie.getTitle());

        header = (ImageView)findViewById(R.id.header);
        Picasso.with(this).load(movie.getPreview_image_path()).into(header);

        favorites = (FloatingActionButton)findViewById(R.id.fab);

        if(savedInstanceState == null) {
            Detail detail = new Detail();
            Bundle bundle = new Bundle();
            bundle.putParcelable("movieSelected", movie);
            detail.setArguments(bundle);
            listener = detail;

            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, detail, MainActivity.FRAGMENT_DETAIL_TAG).commit();
        }else{
            listener = (onFavouriteSelectedListener) getSupportFragmentManager().findFragmentByTag(MainActivity.FRAGMENT_DETAIL_TAG);
        }

        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorites.setImageResource(listener.onSelectedMovieAsFavorite(movie));
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
