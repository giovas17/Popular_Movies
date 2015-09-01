package softwaremobility.darkgeat.listeners;

import org.json.JSONObject;

import java.util.ArrayList;

import softwaremobility.darkgeat.objects.Movie;

/**
 * Created by darkgeat on 29/07/15.
 */
public interface onNetworkDataListener {
    void onReceivedData(JSONObject object);
    void onReceivedData(ArrayList<Movie> movies);
}
