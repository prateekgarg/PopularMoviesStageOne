package garg.prateek.popularmoviesstageone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import garg.prateek.popularmoviesstageone.utilities.Movie;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185";

    @BindView(R.id.iv_backdrop)
    ImageView imageViewBackDrop;
    @BindView(R.id.iv_movie_thumbnail)
    ImageView imageViewThumbnail;
    @BindView(R.id.tv_original_title)
    TextView textViewOriginalTitle;
    @BindView(R.id.tv_user_ratings)
    TextView textViewUserRatings;
    @BindView(R.id.tv_release_date)
    TextView textViewReleaseDate;
    @BindView(R.id.tv_overview)
    TextView textViewOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        Movie movie_selected = (Movie) intent.getSerializableExtra("movie_selected");
        String backdrop, thumbnail;
        if (movie_selected.getBackdropPath() != null && !movie_selected.getBackdropPath().equals("")
                && !movie_selected.getBackdropPath().equals("null")) {
            backdrop = IMAGE_BASE_URL + movie_selected.getBackdropPath();

            Picasso.get().load(backdrop)
                    .placeholder(R.drawable.placeholder)
                    .into(imageViewBackDrop);
            imageViewBackDrop.setAdjustViewBounds(true);
        }
        else{
            imageViewBackDrop.setImageResource(R.drawable.placeholder);
            imageViewBackDrop.setVisibility(View.GONE);
        }

        if (movie_selected.getPosterPath() != null && !movie_selected.getPosterPath().equals("")
                && !movie_selected.getPosterPath().equals("null")) {
            thumbnail = IMAGE_BASE_URL + movie_selected.getPosterPath();

            Picasso.get().load(thumbnail)
                    .placeholder(R.drawable.placeholder)
                    .into(imageViewThumbnail);
            imageViewThumbnail.setAdjustViewBounds(true);
        }
        else{
            imageViewThumbnail.setImageResource(R.drawable.placeholder);
            imageViewThumbnail.setVisibility(View.GONE);
        }
        String originalTitle = movie_selected.getOriginalTitle();
        String overview = movie_selected.getOverview();
        String releaseDate = movie_selected.getReleaseDate();

        if (originalTitle == null || originalTitle.equals("")) {
            originalTitle = "Original title not available";
        }
        if ( overview == null || overview.equals("") ) {
            overview = "Synopsis not available";
        }
        if (releaseDate == null || releaseDate.equals("")){
            releaseDate = "Release date not available";
        }

        textViewOriginalTitle.setText(String.format("%s %s", textViewOriginalTitle.getText(), originalTitle));
        textViewOverview.setText(overview);
        textViewReleaseDate.setText(releaseDate);
        double averageVotes = movie_selected.getAverageVotes();
        textViewUserRatings.setText(String.format("%.2f", averageVotes));

    }
}
