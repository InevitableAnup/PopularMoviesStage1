package in.handmademess.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import static in.handmademess.popularmovies.FavoritesContract.FavoriteListEntry.MOVIE_ID;
import static in.handmademess.popularmovies.FavoritesContract.FavoriteListEntry.ORIGINAL_LNG;
import static in.handmademess.popularmovies.FavoritesContract.FavoriteListEntry.ORIGINAL_TITLE;
import static in.handmademess.popularmovies.FavoritesContract.FavoriteListEntry.OVERVIEW;
import static in.handmademess.popularmovies.FavoritesContract.FavoriteListEntry.POPULARARITY;
import static in.handmademess.popularmovies.FavoritesContract.FavoriteListEntry.POSTER_PATH;
import static in.handmademess.popularmovies.FavoritesContract.FavoriteListEntry.RELEASE_DATE;
import static in.handmademess.popularmovies.FavoritesContract.FavoriteListEntry.TABLE_NAME;
import static in.handmademess.popularmovies.FavoritesContract.FavoriteListEntry.TITLE;
import static in.handmademess.popularmovies.FavoritesContract.FavoriteListEntry.VOTE_AVG;
import static in.handmademess.popularmovies.FavoritesContract.FavoriteListEntry.VOTE_COUNT;

/**
 * Created by Anup on 30-09-2017.
 */

public class FavoriteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite.db";
    private static final int DATABASE_VERSION = 1;
    Context context;


   public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_FAVORITELIST_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                FavoritesContract.FavoriteListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoritesContract.FavoriteListEntry.MOVIE_ID + " INTEGER NOT NULL, " +
                FavoritesContract.FavoriteListEntry.TITLE + " TEXT NOT NULL," +
                FavoritesContract.FavoriteListEntry.OVERVIEW + " TEXT NOT NULL," +
                FavoritesContract.FavoriteListEntry.VOTE_AVG + " REAL NOT NULL," +
                FavoritesContract.FavoriteListEntry.RELEASE_DATE + " TEXT NOT NULL," +
                FavoritesContract.FavoriteListEntry.VOTE_COUNT + " INTEGER NOT NULL," +
                FavoritesContract.FavoriteListEntry.POPULARARITY + " REAL NOT NULL," +
                FavoritesContract.FavoriteListEntry.POSTER_PATH + " TEXT NOT NULL," +
                FavoritesContract.FavoriteListEntry.ORIGINAL_LNG + " TEXT NOT NULL," +
                FavoritesContract.FavoriteListEntry.ORIGINAL_TITLE+ " TEXT NOT NULL," +
                " unique ("+MOVIE_ID+","+TITLE+","+OVERVIEW+","+VOTE_AVG+","+RELEASE_DATE+","+VOTE_COUNT+","+POPULARARITY+","+POSTER_PATH+","+ORIGINAL_LNG+","+ORIGINAL_TITLE+ ")"
                +");";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITELIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("ALTER TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    public ArrayList<MoviesInfo> getFavoriteMovies()
    {
        ArrayList movie_ver = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT "+ FavoritesContract.FavoriteListEntry.MOVIE_ID+","+ FavoritesContract.FavoriteListEntry.TITLE+","+ FavoritesContract.FavoriteListEntry.OVERVIEW+","
                + FavoritesContract.FavoriteListEntry.VOTE_AVG+","+ FavoritesContract.FavoriteListEntry.RELEASE_DATE+","+ FavoritesContract.FavoriteListEntry.VOTE_COUNT+","
                + FavoritesContract.FavoriteListEntry.POPULARARITY+","+ FavoritesContract.FavoriteListEntry.POSTER_PATH+","+ FavoritesContract.FavoriteListEntry.ORIGINAL_LNG+","
                + FavoritesContract.FavoriteListEntry.ORIGINAL_TITLE+ " FROM "+TABLE_NAME +";", null);
        if(c.moveToFirst()){
            do{

                MoviesInfo moviesInfo = new MoviesInfo();
                moviesInfo.setId(c.getInt(0));
                moviesInfo.setTitle(c.getString(1));
                moviesInfo.setOverview(c.getString(2));
                moviesInfo.setVote_average(c.getDouble(3));
                moviesInfo.setRelease_date(c.getString(4));
                moviesInfo.setVote_count(c.getInt(5));
                moviesInfo.setPopularity(c.getDouble(6));
                moviesInfo.setPoster_path(c.getString(7));
                moviesInfo.setOriginal_language(c.getString(8));
                moviesInfo.setOriginal_title(c.getString(9));
                movie_ver.add(moviesInfo);

            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return movie_ver;
    }

}
