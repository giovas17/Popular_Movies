package softwaremobility.darkgeat.objects;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import softwaremobility.darkgeat.popularmovies1.R;

/**
 * Created by darkgeat on 21/07/15.
 */
public class Utils {

    public static String getSortedBy(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.sort_list_key),
                context.getString(R.string.sort_list_default_value));
    }

    public static String getGenresMovie(Context context, JSONArray genre_list) throws JSONException {
        String genres = "";
        for(int i = 0 ; i < genre_list.length() ; i++){
            String genre = getGenre((int) genre_list.get(i),context);
            if(genre != null){
                genres += "- " + genre + "\n";
            }
        }
        if(genres.length() > 0){
            genres = genres.substring(0,genres.length()-1);
        }
        return genres;
    }

    private static String getGenre(int id, Context context){
        String genre = null;
        switch (id){
            case 28:
                genre = context.getString(R.string.genre_action);
                break;
            case 12:
                genre = context.getString(R.string.genre_adventure);
                break;
            case 16:
                genre = context.getString(R.string.genre_animation);
                break;
            case 35:
                genre = context.getString(R.string.genre_comedy);
                break;
            case 80:
                genre = context.getString(R.string.genre_crime);
                break;
            case 99:
                genre = context.getString(R.string.genre_documentary);
                break;
            case 18:
                genre = context.getString(R.string.genre_drama);
                break;
            case 10751:
                genre = context.getString(R.string.genre_family);
                break;
            case 14:
                genre = context.getString(R.string.genre_fantasy);
                break;
            case 10769:
                genre = context.getString(R.string.genre_foreign);
                break;
            case 36:
                genre = context.getString(R.string.genre_history);
                break;
            case 27:
                genre = context.getString(R.string.genre_horror);
                break;
            case 10402:
                genre = context.getString(R.string.genre_music);
                break;
            case 9648:
                genre = context.getString(R.string.genre_mystery);
                break;
            case 10749:
                genre = context.getString(R.string.genre_romance);
                break;
            case 878:
                genre = context.getString(R.string.genre_fiction);
                break;
            case 10770:
                genre = context.getString(R.string.genre_tv);
                break;
            case 53:
                genre = context.getString(R.string.genre_thriller);
                break;
            case 10752:
                genre = context.getString(R.string.genre_war);
                break;
            case 37:
                genre = context.getString(R.string.genre_western);
                break;
        }
        return genre;
    }
}
