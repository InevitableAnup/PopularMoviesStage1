package in.handmademess.popularmovies.Parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.handmademess.popularmovies.ReviewInfo;

/**
 * Created by Anup on 18-07-2017.
 */
public class ParseReviews
{
    public static String[] id,author,content;


    public static final String JSON_ARRAY ="results";
    public static final String KEY_ID ="id";
    public static final String KEY_AUTHOR="author";
    public static final String KEY_CONTENT="content";


    private JSONArray review_list = null;

    private String json;

    public ParseReviews(String json)
    {
        this.json = json;
    }

    public void parseJSON()
    {
        JSONObject jsonObject=null;
        try
        {
            jsonObject = new JSONObject(json);
            review_list = jsonObject.getJSONArray(JSON_ARRAY);


            id = new String[review_list.length()];
            author = new String[review_list.length()];
            content = new String[review_list.length()];

            for (int i = 0; i< review_list.length(); i++)
            {
                JSONObject jo = review_list.getJSONObject(i);
                id[i] = jo.getString(KEY_ID);
                author[i] = jo.getString(KEY_AUTHOR);
                content[i] = jo.getString(KEY_CONTENT);
            }

        }catch (JSONException e)
        {
            e.printStackTrace();
        }





    }

    public ArrayList<ReviewInfo> prepareReviews() {
        ArrayList review_ver = new ArrayList<>();
        if (id.length == 0) {
            Log.d("NOREVIEW INFOAVAILABLE", "NOREVIEW INFO AVAILABLE");
        } else {
            for (int i = 0; i < id.length; i++) {
                ReviewInfo reviewInfo = new ReviewInfo();
                reviewInfo.setId(id[i]);
                reviewInfo.setAuthor(author[i]);
                reviewInfo.setContent(content[i]);

                review_ver.add(reviewInfo);
            }
        }


        return review_ver;
    }
}
