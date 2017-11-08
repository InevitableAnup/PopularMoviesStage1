package in.handmademess.popularmovies;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;

import in.handmademess.popularmovies.adapter.FavoritesListAdapter;

public class FavoriteActivity extends AppCompatActivity  {
    private static final String TAG = FavoriteActivity.class.getSimpleName();
    private SQLiteDatabase mDb;
    FavoritesListAdapter favoritesListAdapter;
    FavoriteDbHelper favoriteDbHelper;
    RecyclerView recyclerView;
    private GridLayoutManager mLayoutManager;
    private static final int TASK_LOADER_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        recyclerView = (RecyclerView) findViewById(R.id.fav_recyclerView);
        favoriteDbHelper = new FavoriteDbHelper(this);
        mDb = favoriteDbHelper.getWritableDatabase();
        Cursor cursor = getAllFavoriteMovies();
        favoritesListAdapter = new FavoritesListAdapter(this,cursor);
        mLayoutManager = new GridLayoutManager(FavoriteActivity.this, numberOfColumns());
        recyclerView.setLayoutManager(new GridLayoutManager(FavoriteActivity.this, numberOfColumns()));
        recyclerView.setAdapter(favoritesListAdapter);

        recyclerView.setHasFixedSize(true);

    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }



    private Cursor getAllFavoriteMovies(){
        return mDb.query(
                FavoritesContract.FavoriteListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavoritesContract.FavoriteListEntry.MOVIE_ID
        );
    }

}
