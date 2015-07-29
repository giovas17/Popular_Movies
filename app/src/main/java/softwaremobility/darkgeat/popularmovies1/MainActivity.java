package softwaremobility.darkgeat.popularmovies1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONObject;

import softwaremobility.darkgeat.fragments.Principal;
import softwaremobility.darkgeat.listeners.onNetworkDataListener;
import softwaremobility.darkgeat.network.NetworkConnection;
import softwaremobility.darkgeat.objects.Utils;

public class MainActivity extends AppCompatActivity implements onNetworkDataListener {

    public static final String FRAGMENT_PRINCIPAL_TAG = Principal.class.getSimpleName();
    private Toolbar toolbar;
    private JSONObject data;
    private String mSortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            mSortBy = Utils.getSortedBy(this);
            refreshData();

            getSupportFragmentManager().beginTransaction().replace(R.id.principal_container,
                    new Principal(), FRAGMENT_PRINCIPAL_TAG).commit();
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
            Principal postersFragment = (Principal) getSupportFragmentManager().findFragmentByTag(FRAGMENT_PRINCIPAL_TAG);
            if(postersFragment != null){
                postersFragment.makeAnUpdate(sortBy);
            }
            mSortBy = sortBy;
        }
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
    }
}
