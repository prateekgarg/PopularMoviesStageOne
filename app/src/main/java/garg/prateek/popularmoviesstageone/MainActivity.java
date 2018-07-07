package garg.prateek.popularmoviesstageone;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import garg.prateek.popularmoviesstageone.adapters.MovieAdapter;
import garg.prateek.popularmoviesstageone.utilities.Movie;
import garg.prateek.popularmoviesstageone.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";

    private final String QUERY_PARM_POPULARITY_DESC = "popularity.desc";
    private final String QUERY_PARAM_TOP_RATED_DESC = "vote_average.desc";
    private final String AUTH_TOKEN = BuildConfig.API_KEY;

    @BindView(R.id.movieLoadProgressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.pop_movies_gridview)
    GridView mGridView;

    ArrayList<Movie> mPopularMoviesList;
    ArrayList<Movie> mTopRatedMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Hide the progress bar unless movies are loading up in the background
        mProgressBar.setVisibility(View.INVISIBLE);

        new FetchMovies().execute();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getAdapter().getItem(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("movie_selected", movie);
                startActivity(intent);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuitem = item.getItemId();
        if (menuitem == R.id.action_refresh){
//            Context context = MainActivity.this;
//            String message = "Refresh clicked";
//            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            refreshList(mPopularMoviesList);
        }
        else if (menuitem == R.id.sort_popular_movies){
            refreshList(mPopularMoviesList);
        }
        else if (menuitem == R.id.sort_top_rated_movies){
            refreshList(mTopRatedMovieList);
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshList(ArrayList<Movie> movies) {
        MovieAdapter movieAdapter = new MovieAdapter(MainActivity.this, movies);
        mGridView.invalidateViews();
        mGridView.setAdapter(movieAdapter);
    }

    public class FetchMovies extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mPopularMoviesList = new ArrayList<>();
            mTopRatedMovieList = new ArrayList<>();

            if (NetworkUtils.getNetworkState(MainActivity.this)){

                try {
                    Uri uri = Uri.parse(BASE_URL)
                            .buildUpon()
                            .appendQueryParameter("api_key", AUTH_TOKEN)
                            .appendQueryParameter("sort_by", QUERY_PARM_POPULARITY_DESC)
                            .build();
                    mPopularMoviesList = NetworkUtils.fetchApi(uri.toString());

                    uri = Uri.parse(BASE_URL)
                            .buildUpon()
                            .appendQueryParameter("api_key", AUTH_TOKEN)
                            .appendQueryParameter("sort_by", QUERY_PARAM_TOP_RATED_DESC)
                            .build();
                    mTopRatedMovieList = NetworkUtils.fetchApi(uri.toString());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(MainActivity.this,
                        "No internet connection detected",
                        Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            refreshList(mPopularMoviesList);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
