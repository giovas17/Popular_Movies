package softwaremobility.darkgeat.network;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.widget.GridView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import softwaremobility.darkgeat.adapters.ImageAdapter;
import softwaremobility.darkgeat.popularmovies1.R;

/**
 * Created by darkgeat on 14/07/15.
 */
public class NetworkConnection extends AsyncTask<String,String,Void> {

    private final String NETWORK_TAG = NetworkConnection.class.getSimpleName();
    private final Context mContext;
    private int width;
    private int height;

    public NetworkConnection(Context c){ mContext = c; }

    @Override
    protected Void doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        //Will contain the JSON response as a string
        String responseJsonStr = null;

        try{
            final String BASE_URL_MOVIES = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String APIKEY_PARAM = "api_key";

            String sortBy = (params[0]==null) ? mContext.getString(R.string.sort_list_default_value) : params[0];

            //Construction of the request URL
            Uri buildURI = Uri.parse(BASE_URL_MOVIES).buildUpon()
                    .appendQueryParameter(SORT_PARAM,sortBy)
                    .appendQueryParameter(APIKEY_PARAM, mContext.getString(R.string.api_key))
                    .build();

            //Final URL for request
            URL url = new URL(buildURI.toString());

            //Creation for the request of movies data
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null){
                return null; //Nothing to do.
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0){
                return null; //Has no lines. String empty.
            }

            responseJsonStr = buffer.toString();
            getMoviesData(responseJsonStr);
        }catch (IOException e){
            Log.e(NETWORK_TAG,e.toString());
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        GridView grid = (GridView) ((Activity)mContext).findViewById(R.id.gridView);
        int numColumns;
        if(grid.getTag().toString().equalsIgnoreCase(mContext.getString(R.string.phone_tag))) {
            if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                numColumns = (width > 2000 && height > 1000) ? 5 : 3; //landscape mode for nexus 6
            }else{
                //set 3 columns in the grid if the device has more than 1000px width and more than 2000px like nexus 6 (portrait)
                numColumns = (width > 1000 && height  > 2000) ? 3 : 2;
            }
            grid.setNumColumns(numColumns);
        }
        ImageAdapter adapter = new ImageAdapter(mContext,values);
        grid.setAdapter(adapter);
    }

    public void getMoviesData(String JSONStr){

        final String BASE_PATH_PICTURE = "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE_PX;
        final String RESULT_ARRAY = "results";
        final String POSTER = "poster_path";
        final String TITLE = "original_title";
        final String ID = "id";
        final String RATING = "vote_average";
        final String POPULARITY = "popularity";
        final String TOTAL_VOTES = "vote_count";
        final String PREVIEW = "backdrop_path";

        Point size = new Point();
        Display display = ((Activity)mContext).getWindowManager().getDefaultDisplay();
        display.getSize(size);
        width = size.x;
        height = size.y;

        //if the width of the screen is bigger than 1000px will set w500
        IMAGE_SIZE_PX = width > 1000 ? "w500//" : "w342//";

        String[] urls;

        try {
            JSONObject object = new JSONObject(JSONStr);
            JSONArray moviesArray = object.getJSONArray(RESULT_ARRAY);
            urls = new String[moviesArray.length()];

            for (int i = 0 ; i < moviesArray.length() ; i++){
                JSONObject obj = moviesArray.getJSONObject(i);
                String poster_path = obj.getString(POSTER);
                urls[i] = BASE_PATH_PICTURE + IMAGE_SIZE_PX + poster_path;
            }
            publishProgress(urls);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
