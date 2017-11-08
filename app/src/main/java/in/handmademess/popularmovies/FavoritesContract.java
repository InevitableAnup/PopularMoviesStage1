package in.handmademess.popularmovies;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Anup on 30-09-2017.
 */

public class FavoritesContract {
    public static final String AUTHORITY ="in.handmademess.popularmovies";
    public static final Uri BASE_CONTENT_URI =Uri.parse("content://"+AUTHORITY);
    public static final String PATH_MOVIES ="favoritelist";
    public static final class FavoriteListEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String TABLE_NAME ="favoritelist";
        public static final String MOVIE_ID ="id";//INTEGER
        public static final String TITLE="title";
        public static final String OVERVIEW="overview";
        public static final String RELEASE_DATE="release_date";
        public static final String VOTE_COUNT = "vote_count";
        public static final String VOTE_AVG ="vote_average";//DOUBLE
        public static final String POPULARARITY ="popularity";//DOUBLE
        public static final String POSTER_PATH ="poster_path";
        public static final String ORIGINAL_LNG ="original_language";
        public static final String ORIGINAL_TITLE ="original_title";
    }
}
