package softwaremobility.darkgeat.network;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import softwaremobility.darkgeat.adapters.ImageAdapter;
import softwaremobility.darkgeat.listeners.onNetworkDataListener;
import softwaremobility.darkgeat.objects.Movie;
import softwaremobility.darkgeat.objects.Utils;
import softwaremobility.darkgeat.popularmovies1.R;

/**
 * Created by darkgeat on 14/07/15.
 */
public class NetworkConnection extends AsyncTask<String,Void,Boolean> {

    private final String NETWORK_TAG = NetworkConnection.class.getSimpleName();
    private final Context mContext;
    private onNetworkDataListener listener;
    private JSONObject data;
    private String responseJsonStr = null;
    private List<Movie> movies;
    private Request typeRequest;

    public NetworkConnection(Context c){
        mContext = c;
        listener = (onNetworkDataListener) mContext;
        typeRequest = Request.dataRequest;
    }

    public NetworkConnection(Context c, Request type){
        mContext = c;
        listener = (onNetworkDataListener) mContext;
        typeRequest = type;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Uri requestURL = null;
        final String BASE_URL = "http://api.themoviedb.org/3";
        final String MOVIE_PATH = "movie";
        final String APIKEY_PARAM = "api_key";
        String idMovie, searchField;
        switch (typeRequest){
            case dataRequest:{
                final String BASE_URL_MOVIES = "discover";
                final String SORT_PARAM = "sort_by";
                searchField = params[0];

                String sortBy = (searchField == null) ? mContext.getString(R.string.sort_list_default_value) : searchField;

                //Construction of the request URL
                requestURL = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(BASE_URL_MOVIES)
                        .appendPath(MOVIE_PATH)
                        .appendQueryParameter(SORT_PARAM,sortBy)
                        .appendQueryParameter(APIKEY_PARAM, mContext.getString(R.string.api_key))
                        .build();
                break;
            }
            case videoRequest:{
                final String VIDEOS_PATH = "videos";
                idMovie = params[0];

                //Construction of the request URL
                requestURL = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(MOVIE_PATH)
                        .appendPath(idMovie)
                        .appendPath(VIDEOS_PATH)
                        .appendQueryParameter(APIKEY_PARAM, mContext.getString(R.string.api_key))
                        .build();
                break;
            }
        }
        Log.w(NETWORK_TAG,requestURL.toString());
        return retrieveData(requestURL);
    }

    private boolean retrieveData(Uri requestedURL){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try{

            //Final URL for request
            URL url = new URL(requestedURL.toString());

            //Creation for the request of movies data
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null){
                return false; //Nothing to do.
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0){
                return false; //Has no lines. String empty.
            }

            responseJsonStr = buffer.toString();
            Log.w(NETWORK_TAG,responseJsonStr);
            return true;
        }catch (IOException e){
            Log.e(NETWORK_TAG,e.toString());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(result){
            try {
                data = new JSONObject(responseJsonStr);
                if(typeRequest == Request.dataRequest) {
                    listener.onReceivedData(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public static enum Request {
        videoRequest,dataRequest,reviewsRequest
    }


}
