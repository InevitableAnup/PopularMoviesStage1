package in.handmademess.popularmovies.PMContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import in.handmademess.popularmovies.FavoriteDbHelper;
import in.handmademess.popularmovies.FavoritesContract;

/**
 * Created by Anup on 08-11-2017.
 */

public class MoviesContentProvider extends ContentProvider {

    public static final int MOVIES =100;
    public static final int MOVIES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);

        return uriMatcher;

    }

    private FavoriteDbHelper mMoviesDbHelper;
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMoviesDbHelper = new FavoriteDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mMoviesDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor retCursor;

        switch (match)
        {
            case MOVIES:
               retCursor= db.query(
                        FavoritesContract.FavoriteListEntry.TABLE_NAME,
                        projection,
                        selection,
                        null,
                        null,
                        null,
                        FavoritesContract.FavoriteListEntry.MOVIE_ID
                );

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri :"+uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        int match =sUriMatcher.match(uri);

        Uri returnUri;
        switch (match)
        {
            case MOVIES:
                long id =db.insert(FavoritesContract.FavoriteListEntry.TABLE_NAME, null,contentValues);
                if (id>0)
                {
                    returnUri = ContentUris.withAppendedId(FavoritesContract.FavoriteListEntry.CONTENT_URI,id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into "+uri);
                }
                break;
            default:
            throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
