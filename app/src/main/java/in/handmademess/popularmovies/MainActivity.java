package in.handmademess.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.ArrayList;

import in.handmademess.popularmovies.Parser.ParseMovies;
import in.handmademess.popularmovies.adapter.FavoritesListAdapter;
import in.handmademess.popularmovies.adapter.MovieArrayAdapter;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    GridView moviesGrid;

    RecyclerView recyclerView;
    private GridLayoutManager mLayoutManager;
    ArrayList<MoviesInfo> moviesList;
    MovieArrayAdapter adapter;
    FavoriteDbHelper favoriteDbHelper;
    Parcelable mListState;

    String ApiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        favoriteDbHelper = new FavoriteDbHelper(this);
        String dbName = favoriteDbHelper.getDatabaseName();
        Toast.makeText(this, dbName, Toast.LENGTH_SHORT).show();

       ApiKey= this.getString(R.string.api_key);

        ArrayList<MoviesInfo> movies = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.movie_recyclerView);
        mLayoutManager = new GridLayoutManager(MainActivity.this, numberOfColumns());
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns()));
        adapter = new MovieArrayAdapter(MainActivity.this, movies);

        getMovies(WebAPI.Popular_Movies+ApiKey);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save list state
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable("popular_key", mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Retrieve list state and list/item positions
        if(savedInstanceState != null)
            mListState = savedInstanceState.getParcelable("popular_key");

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
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

    @Override
    protected void onPause() {
        super.onPause();

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular_movies:

                if (isNetworkAvailable()) {
                    adapter.clear();

                    getMovies(WebAPI.Popular_Movies);

                } else {
                    Toast.makeText(this, R.string.noConnection, Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.action_top_rated_movies:
                if (isNetworkAvailable()) {
                    adapter.clear();
                    getMovies(WebAPI.TopRated_Movies);
                } else {
                    Toast.makeText(this, R.string.noConnection, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.action_favorite_movies:
                Intent favIntent = new Intent(this, FavoriteActivity.class);
                startActivity(favIntent);

                break;


        }
        return super.onOptionsItemSelected(item);
    }

    public void getMovies(String webAPI) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, webAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.toString().trim();
                Log.d("RESP", resp);

                ParseMovies parseMovies = new ParseMovies(resp);
                parseMovies.parseJSON();

                moviesList = parseMovies.prepareMovies();


                adapter = new MovieArrayAdapter(MainActivity.this, moviesList);
                recyclerView.setAdapter(adapter);

                recyclerView.setHasFixedSize(true);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(MainActivity.this, R.string.noConnection, Toast.LENGTH_SHORT).show();
                }

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



}
