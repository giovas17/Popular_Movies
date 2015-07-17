package softwaremobility.darkgeat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import softwaremobility.darkgeat.network.NetworkConnection;
import softwaremobility.darkgeat.popularmovies1.R;

/**
 * Created by darkgeat on 15/07/15.
 */
public class Principal extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal,null,false);

        NetworkConnection nc = new NetworkConnection(getActivity());
        nc.execute();

        return view;
    }
}
