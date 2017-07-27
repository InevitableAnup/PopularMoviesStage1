package in.handmademess.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.handmademess.popularmovies.MoviesInfo;
import in.handmademess.popularmovies.R;
import in.handmademess.popularmovies.WebAPI;


/**
 * Created by Anup on 19-07-2017.
 */

public class MovieArrayAdapter extends RecyclerView.Adapter<MovieArrayAdapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private int listItemLayout;
    private ArrayList<MoviesInfo> itemList;
    public Context context;

    // Constructor of the class
    public MovieArrayAdapter(Context context, ArrayList<MoviesInfo> itemList) {
        this.itemList = itemList;
        this.context = context;
    }


    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView release = holder.releaseDate;
        ImageView posterImg = holder.poster;


        release.setText(itemList.get(listPosition).getRelease_date());
        Picasso.with(context)
                .load(WebAPI.IMAGE_URL + itemList.get(listPosition).getPoster_path())
                .placeholder(R.drawable.empty_gallery)
                .error(R.drawable.error_img)
                .into(posterImg);

    }

    public void clear() {
        int size = this.itemList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.itemList.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }


    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView releaseDate;
        public ImageView poster;

        public ViewHolder(View itemView) {
            super(itemView);
            releaseDate = (TextView) itemView.findViewById(R.id.list_item_release_date);
            poster = (ImageView) itemView.findViewById(R.id.list_item_movie);
        }

    }
}
