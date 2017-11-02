package in.handmademess.popularmovies.Parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.handmademess.popularmovies.TrailerInfo;

/**
 * Created by Anup on 18-07-2017.
 */
public class ParseTrailers
{
    public static String[] id,key,name;


    public static final String JSON_ARRAY ="results";
    public static final String KEY_ID ="id";
    public static final String KEY_KEY="key";
    public static final String KEY_NAME="name";


    private JSONArray trailer_list = null;

    private String json;

    public ParseTrailers(String json)
    {
        this.json = json;
    }

    public void parseJSON()
    {
        JSONObject jsonObject=null;
        try
        {
            jsonObject = new JSONObject(json);
            trailer_list = jsonObject.getJSONArray(JSON_ARRAY);


            id = new String[trailer_list.length()];
            key = new String[trailer_list.length()];
            name = new String[trailer_list.length()];

            for (int i = 0; i< trailer_list.length(); i++)
            {
                JSONObject jo = trailer_list.getJSONObject(i);
                id[i] = jo.getString(KEY_ID);
                key[i] = jo.getString(KEY_KEY);
                name[i] = jo.getString(KEY_NAME);
            }

        }catch (JSONException e)
        {
            e.printStackTrace();
        }





    }

    public ArrayList<TrailerInfo> prepareTrailers() {
        ArrayList trailer_ver = new ArrayList<>();
        if (id.length == 0) {
            Log.d("NOTRAILER INFOAVAILABLE", "NOTRAILER INFO AVAILABLE");
        } else {
            for (int i = 0; i < id.length; i++) {
                TrailerInfo trailerInfo = new TrailerInfo();
                trailerInfo.setId(id[i]);
                trailerInfo.setKey(key[i]);
                trailerInfo.setName(name[i]);

                trailer_ver.add(trailerInfo);
            }
        }


        return trailer_ver;
    }
}
