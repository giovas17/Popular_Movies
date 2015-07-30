package softwaremobility.darkgeat.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import softwaremobility.darkgeat.adapters.ImageAdapter;
import softwaremobility.darkgeat.network.NetworkConnection;
import softwaremobility.darkgeat.objects.Movie;
import softwaremobility.darkgeat.popularmovies1.MainActivity;
import softwaremobility.darkgeat.popularmovies1.R;

/**
 * Created by darkgeat on 15/07/15.
 */
public class Principal extends Fragment {

    private RecyclerView grid;
    private ArrayList<Movie> movies;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movies = getArguments().getParcelableArrayList("key");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal,null,false);
        grid = (RecyclerView)view.findViewById(R.id.gridView);
        makeAnUpdate(movies);
        return view;
    }

    public void makeAnUpdate(ArrayList<Movie> movies){
        int numColumns = 3;
        int[] sizes = MainActivity.obtainingScreenSize(getActivity());
        int width = sizes[0];
        int height = sizes[1];
        if(grid.getTag().toString().equalsIgnoreCase(getActivity().getString(R.string.phone_tag))) {
            if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                numColumns = (width > 2000 && height > 1000) ? 5 : 3; //landscape mode for nexus 6
            }else{
                //set 3 columns in the grid if the device has more than 1000px width and more than 2000px like nexus 6 (portrait)
                numColumns = (width > 1000 && height  > 2000) ? 3 : 2;
            }
        }
        GridLayoutManager glm = new GridLayoutManager(getActivity(),numColumns);
        grid.setHasFixedSize(true);
        grid.setLayoutManager(glm);
        ImageAdapter adapter = new ImageAdapter(getActivity(),movies);
        grid.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
