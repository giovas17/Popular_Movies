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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import softwaremobility.darkgeat.data.DataBase;
import softwaremobility.darkgeat.fragments.Detail;
import softwaremobility.darkgeat.fragments.Principal;
import softwaremobility.darkgeat.listeners.onFavouriteSelectedListener;
import softwaremobility.darkgeat.listeners.onNetworkDataListener;
import softwaremobility.darkgeat.network.NetworkConnection;
import softwaremobility.darkgeat.objects.Movie;
import softwaremobility.darkgeat.utils.Utils;

import static softwaremobility.darkgeat.utils.JSONParser.getMoviesData;

public class MainActivity extends AppCompatActivity implements onNetworkDataListener {

    public static final String FRAGMENT_PRINCIPAL_TAG = Principal.class.getSimpleName();
    public static final String FRAGMENT_DETAIL_TAG = Detail.class.getSimpleName();
    private Toolbar toolbar;
    private JSONObject data;
    private String mSortBy;
    private ArrayList<Movie> mMovies;
    private onFavouriteSelectedListener listener;
    private FloatingActionButton favorite;
    public static Movie movie = null;
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
                bundle.putParcelable("movieSelected", new Movie());
                detail.setArguments(bundle);
                listener = detail;
                favorite = (FloatingActionButton)findViewById(R.id.fab);
                favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        favorite.setImageResource(listener.onSelectedMovieAsFavorite(movie));
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
                listener = (onFavouriteSelectedListener) getSupportFragmentManager().findFragmentByTag(FRAGMENT_DETAIL_TAG);
                favorite = (FloatingActionButton)findViewById(R.id.fab);
                favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        favorite.setImageResource(listener.onSelectedMovieAsFavorite(movie));
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
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key",mMovies);
        outState.putString("sort",mSortBy);
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
        mMovies = getMoviesData(data,this);
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

}
