package softwaremobility.darkgeat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import softwaremobility.darkgeat.objects.Trailer;
import softwaremobility.darkgeat.popularmovies1.R;

/**
 * Created by darkgeat on 8/26/15.
 */
public class TrailerFragment extends Fragment {

    private Trailer trailer;

    public static TrailerFragment Create(Trailer trailer, String pathImage){
        TrailerFragment tr = new TrailerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("trailer",trailer);
        bundle.putString("image", pathImage);
        tr.setArguments(bundle);
        return tr;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        trailer = getArguments().getParcelable("trailer");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video_trailer, container, false);

        ImageView imageTrailer = (ImageView)v.findViewById(R.id.imageTrailer);
        Picasso.with(getActivity()).load(getArguments().getString("image")).into(imageTrailer);

        TextView name = (TextView)v.findViewById(R.id.textNameTrailer);
        name.setText(trailer.getName());

        TextView type = (TextView)v.findViewById(R.id.typeTrailer);
        type.setText(trailer.getType());

        return v;
    }
}
