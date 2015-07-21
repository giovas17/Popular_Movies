package softwaremobility.darkgeat.objects;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
}
