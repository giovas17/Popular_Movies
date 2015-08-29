package softwaremobility.darkgeat.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import softwaremobility.darkgeat.objects.Trailer;
import softwaremobility.darkgeat.popularmovies1.R;

/**
 * Created by darkgeat on 8/26/15.
 */
public class TrailerFragment extends Fragment implements View.OnClickListener {

    private Trailer trailer;
    private ImageView imageTrailer;
    private TextView name;
    private TextView type;
    private ImageView imageYoutube;
    private ImageView imagePlayTrailer;
    private TextView sizeTrailer;
    private RelativeLayout containerTrailer;

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

        imageTrailer = (ImageView)v.findViewById(R.id.imageTrailer);
        imageTrailer.setOnClickListener(this);
        Picasso.with(getActivity()).load(getArguments().getString("image")).into(imageTrailer);

        imageYoutube = (ImageView)v.findViewById(R.id.imageYoutube);
        imageYoutube.setOnClickListener(this);

        containerTrailer = (RelativeLayout)v.findViewById(R.id.container_trailer);
        containerTrailer.setOnClickListener(this);

        name = (TextView)v.findViewById(R.id.textNameTrailer);
        name.setText(trailer.getName());
        name.setOnClickListener(this);

        imagePlayTrailer = (ImageView)v.findViewById(R.id.imagePlayTrailer);
        imagePlayTrailer.setOnClickListener(this);

        sizeTrailer = (TextView)v.findViewById(R.id.textYouTube);
        sizeTrailer.setText(getString(R.string.size_video, trailer.getSize()));
        sizeTrailer.setOnClickListener(this);

        type = (TextView)v.findViewById(R.id.typeTrailer);
        type.setText(trailer.getType());
        type.setOnClickListener(this);

        return v;
    }

    public void WatchYoutubeVideo(){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getKey()));
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            Intent intent=new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        WatchYoutubeVideo();
    }
}
