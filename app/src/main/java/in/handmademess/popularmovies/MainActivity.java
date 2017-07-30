package in.handmademess.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

import java.util.ArrayList;

import in.handmademess.popularmovies.Parser.ParseMovies;
import in.handmademess.popularmovies.adapter.MovieArrayAdapter;

public class MainActivity extends AppCompatActivity {

    GridView moviesGrid;

    RecyclerView recyclerView;
    private GridLayoutManager mLayoutManager;
    ArrayList<MoviesInfo> moviesList;
    MovieArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<MoviesInfo> movies = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.movie_recyclerView);
        mLayoutManager = new GridLayoutManager(MainActivity.this, numberOfColumns());
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns()));
        adapter = new MovieArrayAdapter(MainActivity.this, movies);

        getMovies(WebAPI.Popular_Movies);
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
                recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }

                    });

                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        View child = rv.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && gestureDetector.onTouchEvent(e)) {
                            int position = rv.getChildAdapterPosition(child);
                            MoviesInfo moviesInfo = moviesList.get(position);
                            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                            intent.putExtra("id", moviesInfo.getId());
                            intent.putExtra("originalLng", moviesInfo.getOriginal_language());
                            intent.putExtra("title", moviesInfo.getOriginal_title());
                            intent.putExtra("overview", moviesInfo.getOverview());
                            intent.putExtra("popularity", moviesInfo.getPopularity());
                            intent.putExtra("poster", moviesInfo.getPoster_path());
                            intent.putExtra("releaseDate", moviesInfo.getRelease_date());
                            intent.putExtra("rating", String.valueOf(moviesInfo.getVote_average()));
                            startActivity(intent);
                        }

                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                });


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


    public void getSorted() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebAPI.TopRated_Movies, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.toString().trim();
                Log.d("Sorted RESP", resp);

                ParseMovies parseMovies = new ParseMovies(resp);
                parseMovies.parseJSON();

                moviesList = parseMovies.prepareMovies();


                MovieArrayAdapter adapter = new MovieArrayAdapter(MainActivity.this, moviesList);
                recyclerView.setAdapter(adapter);

                recyclerView.setHasFixedSize(true);
                recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }

                    });

                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        View child = rv.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && gestureDetector.onTouchEvent(e)) {
                            int position = rv.getChildAdapterPosition(child);
                            MoviesInfo moviesInfo = moviesList.get(position);
                            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                            intent.putExtra("id", moviesInfo.getId());
                            intent.putExtra("originalLng", moviesInfo.getOriginal_language());
                            intent.putExtra("title", moviesInfo.getOriginal_title());
                            intent.putExtra("overview", moviesInfo.getOverview());
                            intent.putExtra("popularity", moviesInfo.getPopularity());
                            intent.putExtra("poster", moviesInfo.getPoster_path());
                            intent.putExtra("releaseDate", moviesInfo.getRelease_date());
                            intent.putExtra("rating", String.valueOf(moviesInfo.getVote_average()));
                            startActivity(intent);
                        }

                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                });


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
