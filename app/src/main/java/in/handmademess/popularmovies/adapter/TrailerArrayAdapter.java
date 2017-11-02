package in.handmademess.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.handmademess.popularmovies.R;
import in.handmademess.popularmovies.TrailerInfo;


/**
 * Created by Anup on 19-07-2017.
 */

public class TrailerArrayAdapter extends RecyclerView.Adapter<TrailerArrayAdapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private int listItemLayout;
    private ArrayList<TrailerInfo> itemList;
    public Context context;

    // Constructor of the class
    public TrailerArrayAdapter(Context context, ArrayList<TrailerInfo> itemList) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_view, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView trailerName = holder.tvTrailerName;


        trailerName.setText(itemList.get(listPosition).getName());


    }


    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTrailerName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTrailerName = (TextView) itemView.findViewById(R.id.tvTrailerName);
        }

    }
}
