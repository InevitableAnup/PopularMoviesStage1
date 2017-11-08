package in.handmademess.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import in.handmademess.popularmovies.DetailsActivity;
import in.handmademess.popularmovies.FavoritesContract;
import in.handmademess.popularmovies.R;
import in.handmademess.popularmovies.WebAPI;

/**
 * Created by Anup on 01-11-2017.
 */

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.FavoriteViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public FavoritesListAdapter(Context mContext, Cursor mCursor) {
        this.mContext = mContext;
        this.mCursor = mCursor;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movie_list, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavoriteViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){

            Toast.makeText(mContext, "No Favorite Movies to display.", Toast.LENGTH_SHORT).show();
            return;
        }
        final String releaseDate = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoriteListEntry.RELEASE_DATE));
        final String poster = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoriteListEntry.POSTER_PATH));
        final String title = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoriteListEntry.TITLE));
        final String originalLang = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoriteListEntry.ORIGINAL_LNG));
        String originalTitle = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoriteListEntry.ORIGINAL_TITLE));
        final String overview = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoriteListEntry.OVERVIEW));
        final String popularity = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoriteListEntry.POPULARARITY));
        final String rating = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoriteListEntry.VOTE_AVG));
        int id = mCursor.getInt(mCursor.getColumnIndex(FavoritesContract.FavoriteListEntry.MOVIE_ID));

        holder.releaseDate.setText(releaseDate);

        Picasso.with(mContext)
                .load(WebAPI.IMAGE_URL + poster)
                .placeholder(R.drawable.empty_gallery)
                .error(R.drawable.error_img)
                .into(holder.poster);

        holder.itemView.setTag(id);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int movie_id = (int) holder.itemView.getTag();
                Toast.makeText(mContext, "Movie Tag : "+title, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("movie_id", movie_id);
                intent.putExtra("originalLng", originalLang);
                intent.putExtra("title", title);
                intent.putExtra("overview", overview);
                intent.putExtra("popularity", popularity);
                intent.putExtra("poster", poster);
                intent.putExtra("releaseDate", releaseDate);
                intent.putExtra("rating", String.valueOf(rating));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }


    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder
    {
        public TextView releaseDate;
        public ImageView poster;


        public FavoriteViewHolder(View itemView) {
            super(itemView);
            releaseDate = (TextView) itemView.findViewById(R.id.list_item_release_date);
            poster = (ImageView) itemView.findViewById(R.id.list_item_movie);
        }
    }
}
