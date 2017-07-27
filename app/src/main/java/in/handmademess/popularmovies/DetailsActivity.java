package in.handmademess.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    String title,overview,releaseDate, originalLng,poster,rating;
    public TextView releaseDt, tvOverView, tvTitle, tvRating;
    public ImageView posterImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        releaseDt = (TextView) findViewById(R.id.list_item_release_date);
        tvOverView = (TextView) findViewById(R.id.tv_overview);
        posterImg = (ImageView) findViewById(R.id.list_item_movie);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRating = (TextView) findViewById(R.id.tv_rating);


        Intent intent = getIntent();

        if (intent.getStringExtra("title")!=null)
        {
            title = intent.getStringExtra("title");
            overview = intent.getStringExtra("overview");
            releaseDate = intent.getStringExtra("releaseDate");
            originalLng = intent.getStringExtra("originalLng");
            poster = intent.getStringExtra("poster");
            rating = intent.getStringExtra("rating");


            releaseDt.setText(releaseDate);
            Picasso.with(this)
                    .load(WebAPI.IMAGE_URL + poster)
                    .into(posterImg);
            tvOverView.setText(overview);
            tvTitle.setText(title);
            tvRating.setText(rating);
        }

    }
}
