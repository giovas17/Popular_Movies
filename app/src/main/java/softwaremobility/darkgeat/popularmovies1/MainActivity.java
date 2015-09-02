package softwaremobility.darkgeat.popularmovies1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import softwaremobility.darkgeat.data.DataBase;
import softwaremobility.darkgeat.fragments.Detail;
import softwaremobility.darkgeat.fragments.Principal;
import softwaremobility.darkgeat.listeners.onFavouriteSelectedListener;
import softwaremobility.darkgeat.listeners.onMovieSelectedListener;
import softwaremobility.darkgeat.listeners.onNetworkDataListener;
import softwaremobility.darkgeat.network.NetworkConnection;
import softwaremobility.darkgeat.objects.Movie;
import softwaremobility.darkgeat.utils.Utils;

import static softwaremobility.darkgeat.utils.JSONParser.getMoviesData;

public class MainActivity extends AppCompatActivity implements onNetworkDataListener,onMovieSelectedListener {

    public static final String FRAGMENT_PRINCIPAL_TAG = Principal.class.getSimpleName();
    public static final String FRAGMENT_DETAIL_TAG = Detail.class.getSimpleName();
    private String mSortBy;
    private ArrayList<Movie> mMovies;
    private onFavouriteSelectedListener listener;
    private FloatingActionButton favorite;
    public Movie movie = null;
    public static boolean two_views = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(5f);

        two_views = findViewById(R.id.detail_container) != null;

        if(savedInstanceState == null) {
            if(two_views){
                Detail detail = new Detail();
                detail.setMovieListener(this);
                Bundle bundle = new Bundle();
                bundle.putParcelable("movieSelected", new Movie());
                detail.setArguments(bundle);
                listener = detail;
                favorite = (FloatingActionButton)findViewById(R.id.fab);
                initFloatingButton();
                favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resource = listener.onSelectedMovieAsFavorite(movie);
                        favorite.setImageResource(resource);
                        if(resource == R.drawable.abc_btn_rating_star_off_mtrl_alpha && mSortBy.equals(getString(R.string.favorites_value)))
                            refreshData();
                    }
                });
                getSupportFragmentManager().beginTransaction().replace(R.id.detail_container,
                        detail,FRAGMENT_DETAIL_TAG).commit();
            }
            mSortBy = Utils.getSortedBy(this);
            refreshData();
        }else{
            mMovies = savedInstanceState.getParcelableArrayList("key");
            mSortBy = savedInstanceState.getString("sort");
            if (two_views){
                movie = savedInstanceState.getParcelable("movie");
                listener = (onFavouriteSelectedListener) getSupportFragmentManager().findFragmentByTag(FRAGMENT_DETAIL_TAG);
                ((Detail)getSupportFragmentManager().findFragmentByTag(FRAGMENT_DETAIL_TAG)).setMovieListener(this);
                favorite = (FloatingActionButton)findViewById(R.id.fab);
                initFloatingButton();
                favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resource = listener.onSelectedMovieAsFavorite(movie);
                        favorite.setImageResource(resource);
                        if(resource == R.drawable.abc_btn_rating_star_off_mtrl_alpha && mSortBy.equals(getString(R.string.favorites_value)))
                            refreshData();
                    }
                });
            }
        }
    }

    private void refreshData() {
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.execute(mSortBy);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sortBy = Utils.getSortedBy(this);
        if(sortBy != null && !sortBy.equals(mSortBy)){
            mSortBy = sortBy;
            refreshData();
        }else if(sortBy.equals(getString(R.string.favorites_value))){
            refreshData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key", mMovies);
        outState.putString("sort", mSortBy);
        if (two_views){
            outState.putParcelable("movie", movie);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,Settings.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReceivedData(JSONObject object) {
        JSONObject data = object;
        mMovies = getMoviesData(data,this);
        HideOrShowDetails();
        refreshingMovies();
    }

    @Override
    public void onReceivedData(ArrayList<Movie> movies) {
        mMovies = movies;
        HideOrShowDetails();
        refreshingMovies();
    }

    private void HideOrShowDetails() {
        if (two_views){
            FrameLayout detail = (FrameLayout)findViewById(R.id.detail_container);
            if(mMovies.size() > 0){
                detail.setVisibility(View.VISIBLE);
                favorite.setVisibility(View.VISIBLE);
            }else{
                detail.setVisibility(View.GONE);
                favorite.setVisibility(View.GONE);
            }
        }
    }

    public void refreshingMovies(){
        Principal main = new Principal();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("key", mMovies);
        main.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.principal_container,
                main, FRAGMENT_PRINCIPAL_TAG).commit();
    }

    public static int[] obtainingScreenSize(Context context){
        Point size = new Point();
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return (new int[]{width,height});
    }

    @Override
    public void onMovieSelected(Movie movieSelected) {
        movie = movieSelected;
        initFloatingButton();
    }

    public void initFloatingButton(){
        DataBase myDataBase = new DataBase(this);
        if (movie != null){
            int resource = R.drawable.abc_btn_rating_star_off_mtrl_alpha;
            if (myDataBase.isFavorite(movie.getId())){
                resource = R.drawable.abc_btn_rating_star_on_mtrl_alpha;
            }
            favorite.setImageResource(resource);
        }
    }
}
