package in.handmademess.popularmovies.Parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.handmademess.popularmovies.MoviesInfo;

/**
 * Created by Anup on 18-07-2017.
 */
public class ParseMovies
{
    public static int[] vote_count,id;
    public static double[] vote_average,popularity;
    public static  String[] title,poster_path,original_language,original_title,overview,release_date;


    public static final String JSON_ARRAY ="results";
    public static final String KEY_VOTECOUNT = "vote_count";
    public static final String KEY_ID ="id";
    public static final String KEY_VOTE_AVERAGE ="vote_average";
    public static final String KEY_POPULARITY="popularity";
    public static final String KEY_TITLE="title";
    public static final String KEY_POSTER_PATH="poster_path";
    public static final String KEY_ORIGINAL_LNG="original_language";
    public static final String KEY_ORIGINAL_TITLE="original_title";
    public static final String KEY_OVERVIEW="overview";
    public static final String KEY_RELEASE_DATE="release_date";

    private JSONArray titles_list = null;

    private String json;

    public ParseMovies(String json)
    {
        this.json = json;
    }

    public void parseJSON()
    {
        JSONObject jsonObject=null;
        try
        {
            jsonObject = new JSONObject(json);
            titles_list = jsonObject.getJSONArray(JSON_ARRAY);

            vote_count= new int[titles_list.length()];
            id = new int[titles_list.length()];
            vote_average = new double[titles_list.length()];
            popularity = new double[titles_list.length()];
            title = new String[titles_list.length()];
            poster_path= new String[titles_list.length()];
            original_language= new String[titles_list.length()];
            original_title= new String[titles_list.length()];
            overview= new String[titles_list.length()];
            release_date= new String[titles_list.length()];


            for (int i = 0; i< titles_list.length(); i++)
            {
                JSONObject jo = titles_list.getJSONObject(i);
                vote_count[i] = jo.getInt(KEY_VOTECOUNT);
                id[i] = jo.getInt(KEY_ID);
                vote_average[i] = jo.getDouble(KEY_VOTE_AVERAGE);
                popularity[i] = jo.getDouble(KEY_POPULARITY);
                title[i] = jo.getString(KEY_TITLE);
                poster_path[i] = jo.getString(KEY_POSTER_PATH);
                original_language[i] = jo.getString(KEY_ORIGINAL_LNG);
                original_title[i] = jo.getString(KEY_ORIGINAL_TITLE);
                overview[i] = jo.getString(KEY_OVERVIEW);
                release_date[i] = jo.getString(KEY_RELEASE_DATE);
            }

        }catch (JSONException e)
        {
            e.printStackTrace();
        }





    }

    public ArrayList<MoviesInfo> prepareMovies() {
        ArrayList stock_ver = new ArrayList<>();
        if (id.length == 0) {
            Log.d("NO MOVIE INFO AVAILABLE", "NO MOVIE INFO AVAILABLE");
        } else {
            for (int i = 0; i < id.length; i++) {
                MoviesInfo moviesInfo = new MoviesInfo();
                moviesInfo.setId(id[i]);
                moviesInfo.setVote_count(vote_count[i]);
                moviesInfo.setVote_average(vote_average[i]);
                moviesInfo.setPopularity(popularity[i]);
                moviesInfo.setTitle(title[i]);
                moviesInfo.setPoster_path(poster_path[i]);
                moviesInfo.setOriginal_language(original_language[i]);
                moviesInfo.setOriginal_title(original_title[i]);
                moviesInfo.setOverview(overview[i]);
                moviesInfo.setRelease_date(release_date[i]);

                stock_ver.add(moviesInfo);
            }
        }


        return stock_ver;
    }
}
