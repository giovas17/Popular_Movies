package softwaremobility.darkgeat.popularmovies1;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import softwaremobility.darkgeat.fragments.Detail;
import softwaremobility.darkgeat.fragments.Principal;
import softwaremobility.darkgeat.listeners.onNetworkDataListener;
import softwaremobility.darkgeat.network.NetworkConnection;
import softwaremobility.darkgeat.objects.Movie;
import softwaremobility.darkgeat.objects.Utils;

public class MainActivity extends AppCompatActivity implements onNetworkDataListener {

    public static final String FRAGMENT_PRINCIPAL_TAG = Principal.class.getSimpleName();
    public static final String FRAGMENT_DETAIL_TAG = Detail.class.getSimpleName();
    private Toolbar toolbar;
    private JSONObject data;
    private String mSortBy;
    private ArrayList<Movie> mMovies;
    public static boolean two_views = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(5f);

        two_views = findViewById(R.id.detail_container) != null ? true : false;

        if(savedInstanceState == null) {
            if(two_views){
                Detail detail = new Detail();
                Bundle bundle = new Bundle();
                bundle.putParcelable("movieSelected",new Movie());
                detail.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.detail_container,
                        detail,FRAGMENT_DETAIL_TAG).commit();
            }
            mSortBy = Utils.getSortedBy(this);
            refreshData();
        }else{
            mMovies = savedInstanceState.getParcelableArrayList("key");
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
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key",mMovies);
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
        data = object;
        mMovies = getMoviesData(data);
        Principal main = new Principal();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("key", mMovies);
        main.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.principal_container,
                main, FRAGMENT_PRINCIPAL_TAG).commit();
    }

    public ArrayList<Movie> getMoviesData(JSONObject object){

        final String BASE_PATH_PICTURE = "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE_PX;
        final String RESULT_ARRAY = "results";
        final String POSTER = "poster_path";
        final String TITLE = "original_title";
        final String ID = "id";
        final String DESCRIPTION = "overview";
        final String RATING = "vote_average";
        final String POPULARITY = "popularity";
        final String TOTAL_VOTES = "vote_count";
        final String PREVIEW = "backdrop_path";
        final String RELEASE_DATE = "release_date";
        final String GENRES = "genre_ids";
        final String BIGGEST_IMAGE_SIZE = "w500//";
        final String SMALLER_IMAGE_SIZE;

        //if the width of the screen is bigger than 1000px will set w500
        int width = obtainingScreenSize(this)[0];
        IMAGE_SIZE_PX = width > 1000 ? "w500//" : "w342//";
        SMALLER_IMAGE_SIZE = (IMAGE_SIZE_PX.contains("342")) ? "w185//" : "w342//";

        ArrayList<Movie> movies;

        try {
            JSONArray moviesArray = object.getJSONArray(RESULT_ARRAY);
            movies = new ArrayList<>();

            for (int i = 0 ; i < moviesArray.length() ; i++){
                Movie movie = new Movie();
                JSONObject obj = moviesArray.getJSONObject(i);
                String poster_path = obj.getString(POSTER);
                movie.setPoster_thumbnail(BASE_PATH_PICTURE + SMALLER_IMAGE_SIZE + poster_path);
                poster_path = BASE_PATH_PICTURE + IMAGE_SIZE_PX + poster_path;
                String preview_path = obj.getString(PREVIEW);
                preview_path = BASE_PATH_PICTURE + BIGGEST_IMAGE_SIZE + preview_path;
                movie.setPoster_image_path(poster_path);
                movie.setPreview_image_path(preview_path);
                movie.setId(obj.getLong(ID));
                movie.setTitle(obj.getString(TITLE));
                movie.setRating(obj.getDouble(RATING));
                movie.setDescription(obj.getString(DESCRIPTION));
                movie.setPopularity(obj.getDouble(POPULARITY));
                movie.setVote_count(obj.getInt(TOTAL_VOTES));
                movie.setRelease_date(obj.getString(RELEASE_DATE));
                movie.setGenres(Utils.getGenresMovie(this, obj.getJSONArray(GENRES)));
                movies.add(movie);
            }
            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int[] obtainingScreenSize(Context context){
        Point size = new Point();
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return (new int[]{width,height});
    }
}
