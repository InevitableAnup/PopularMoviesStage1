package in.handmademess.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.handmademess.popularmovies.Parser.ParseReviews;
import in.handmademess.popularmovies.Parser.ParseTrailers;
import in.handmademess.popularmovies.adapter.ReviewArrayAdapter;
import in.handmademess.popularmovies.adapter.TrailerArrayAdapter;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    String title,overview,releaseDate, originalLng,poster,rating;
    public TextView releaseDt, tvOverView, tvTitle, tvRating;
    public ImageView posterImg;
    ArrayList<TrailerInfo> trailerInfoArrayList;
    ArrayList<ReviewInfo> reviewInfoArrayList;
    RecyclerView trailerRecyclerView,reviewsRecyclerView;
    TrailerArrayAdapter trailerArrayAdapter;
    ReviewArrayAdapter reviewArrayAdapter;
    LinearLayoutManager trailerLayoutManager,reviewLayoutManager;
    int movie_id;
    ToggleButton bt_fav;
    private SQLiteDatabase mDb;
    FavoriteDbHelper favoriteDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setElevation(0f);
        favoriteDbHelper = new FavoriteDbHelper(this);
        mDb = favoriteDbHelper.getWritableDatabase();
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setCustomView(R.layout.actionbar_layout);
//        TextView tv_movie_title = (TextView) findViewById(R.id.tv_movie_title);
//        ImageButton iv_black = (ImageButton) findViewById(R.id.iv_black);
//        iv_black.setOnClickListener(this);


        releaseDt = (TextView) findViewById(R.id.list_item_release_date);
        tvOverView = (TextView) findViewById(R.id.tv_overview);
        posterImg = (ImageView) findViewById(R.id.list_item_movie);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRating = (TextView) findViewById(R.id.tv_rating);
        bt_fav = (ToggleButton) findViewById(R.id.bt_fav);



        initTrailer();
        initReviews();
        Intent intent = getIntent();

        if (intent.getStringExtra("title")!=null)
        {
            title = intent.getStringExtra("title");
            overview = intent.getStringExtra("overview");
            releaseDate = intent.getStringExtra("releaseDate");
            originalLng = intent.getStringExtra("originalLng");
            poster = intent.getStringExtra("poster");
            rating = intent.getStringExtra("rating");
            movie_id = intent.getIntExtra("movie_id",0);


            releaseDt.setText(releaseDate);
            Picasso.with(this)
                    .load(WebAPI.IMAGE_URL + poster)
                    .into(posterImg);
            tvOverView.setText(overview);
            tvTitle.setText(title);
//            tv_movie_title.setText(title);
            tvRating.setText(rating+"/10");
            getTrailers();
            getReviews();
        }


        setFavorites();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setFavorites() {
        bt_fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                {
                    Toast.makeText(DetailsActivity.this, "Add to favorite movies", Toast.LENGTH_SHORT).show();
                    addToFavorites(title,overview,releaseDate,originalLng,poster,rating,movie_id);
                }else{
                    Toast.makeText(DetailsActivity.this, "Delete from favorite movies", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private long addToFavorites(String title, String overview, String releaseDate, String originalLng, String poster, String rating, int id) {
        ContentValues cv = new ContentValues();
        cv.put(FavoritesContract.FavoriteListEntry.MOVIE_ID,id);
        cv.put(FavoritesContract.FavoriteListEntry.TITLE,title);
        cv.put(FavoritesContract.FavoriteListEntry.OVERVIEW,overview);
        cv.put(FavoritesContract.FavoriteListEntry.RELEASE_DATE,releaseDate);
        cv.put(FavoritesContract.FavoriteListEntry.VOTE_AVG,rating);
        cv.put(FavoritesContract.FavoriteListEntry.POSTER_PATH,poster);
        cv.put(FavoritesContract.FavoriteListEntry.ORIGINAL_LNG,originalLng);
        cv.put(FavoritesContract.FavoriteListEntry.VOTE_COUNT,2);
        cv.put(FavoritesContract.FavoriteListEntry.POPULARARITY,rating);
        cv.put(FavoritesContract.FavoriteListEntry.ORIGINAL_TITLE,title);

        return mDb.insert(FavoritesContract.FavoriteListEntry.TABLE_NAME,null,cv);

    }

    private void initTrailer() {
        ArrayList<TrailerInfo> trailerInfos = new ArrayList<>();
        trailerRecyclerView = (RecyclerView) findViewById(R.id.trailerRecyclerView);
        trailerLayoutManager = new LinearLayoutManager(DetailsActivity.this);
        trailerRecyclerView.setLayoutManager(trailerLayoutManager);
        trailerArrayAdapter = new TrailerArrayAdapter(DetailsActivity.this,trailerInfos);
    }

    private void initReviews() {
        ArrayList<ReviewInfo> reviewInfos = new ArrayList<>();
        reviewsRecyclerView = (RecyclerView) findViewById(R.id.reviewsRecyclerView);
        reviewLayoutManager = new LinearLayoutManager(DetailsActivity.this);
        reviewsRecyclerView.setLayoutManager(reviewLayoutManager);
        reviewArrayAdapter = new ReviewArrayAdapter(DetailsActivity.this,reviewInfos);
    }


    public void getTrailers()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebAPI.MOVIES +movie_id+"/videos?api_key=0a3f73387e7920fd5d01ac61646f262c&language=en-US", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.toString().trim();
                Log.d("Videos : ",resp);

                handleTrailerEvents(resp);

            }

            private void handleTrailerEvents(String resp) {
                ParseTrailers parseTrailers = new ParseTrailers(resp);
                parseTrailers.parseJSON();
                trailerInfoArrayList = parseTrailers.prepareTrailers();

                trailerArrayAdapter = new TrailerArrayAdapter(DetailsActivity.this,trailerInfoArrayList);
                trailerRecyclerView.setAdapter(trailerArrayAdapter);
                trailerRecyclerView.setHasFixedSize(true);
                trailerRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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

                            TrailerInfo trailerInfo = trailerInfoArrayList.get(position);
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+trailerInfo.getKey())));

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
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(DetailsActivity.this, "Please connect to the internet before continuing.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void getReviews()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebAPI.MOVIES +movie_id+"/reviews?api_key=0a3f73387e7920fd5d01ac61646f262c&language=en-US", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String resp = response.toString().trim();
                Log.d("Reviews : ",resp);

                handleReviewEvents(resp);

            }
            private void handleReviewEvents(String resp) {
                ParseReviews parseReviews = new ParseReviews(resp);
                parseReviews.parseJSON();
                reviewInfoArrayList = parseReviews.prepareReviews();

                reviewArrayAdapter = new ReviewArrayAdapter(DetailsActivity.this,reviewInfoArrayList);
                reviewsRecyclerView.setAdapter(reviewArrayAdapter);
                reviewsRecyclerView.setHasFixedSize(true);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(DetailsActivity.this, "Please connect to the internet before continuing.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    @Override
    public void onClick(View view) {
    }
}
