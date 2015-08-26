package softwaremobility.darkgeat.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import softwaremobility.darkgeat.objects.Review;
import softwaremobility.darkgeat.popularmovies1.R;

/**
 * Created by darkgeat on 8/24/15.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<Review> data;

    public ReviewsAdapter(Context context, ArrayList<Review> reviews){
        mContext = context;
        data = reviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        View convertView = inflater.inflate(R.layout.layout_review, null, false);
        viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.authorReview.setText(mContext.getString(R.string.author,data.get(position).getAuthor()));
        holder.contentReview.setText(data.get(position).getContent());
        holder.urlReview.setText(data.get(position).getUrl());
        if (!data.get(position).getAuthor().equals(mContext.getString(R.string.app_name)))
            holder.labelUrl.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView authorReview;
        public final TextView contentReview;
        public final TextView urlReview;
        public final TextView labelUrl;

        public ViewHolder(View v){
            super(v);
            authorReview = (TextView)v.findViewById(R.id.authorText);
            contentReview = (TextView)v.findViewById(R.id.contentText);
            urlReview = (TextView)v.findViewById(R.id.urlText);
            labelUrl = (TextView)v.findViewById(R.id.labelURL);
        }
    }
}
