package garg.prateek.popularmoviesstageone;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

    private final String POPULAR_MOVIES_URL = "https://api.themoviedb.org/3/movie/popular";
    private final String TOP_RATED_URL = "https://api.themoviedb.org/3/movie/top_rated";

    private final String QUERY_PARM_POPULARITY_DESC = "popularity.desc";
    private final String QUERY_PARAM_TOP_RATED_DESC = "vote_average.desc";
    private final String AUTH_TOKEN = BuildConfig.API_KEY;

    private boolean IS_APP_INITIALIZED = false;
    private ArrayList<Movie> currentList;

    @BindView(R.id.movieLoadProgressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.pop_movies_gridview)
    GridView mGridView;
    @BindView(R.id.refresh_button)
    Button mRefreshButton;

    ArrayList<Movie> mPopularMoviesList;
    ArrayList<Movie> mTopRatedMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Hide the progress bar unless movies are loading up in the background
        mProgressBar.setVisibility(View.INVISIBLE);
        mRefreshButton.setVisibility(View.GONE);
        startApp();
    }

    private void startApp() {
        boolean internet_status = NetworkUtils.getNetworkState(MainActivity.this);
        if (internet_status) {

            mRefreshButton.setVisibility(View.GONE);
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
        } else {
            mRefreshButton.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No Internet connection detected", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem = item.getItemId();

        if (menuItem == R.id.sort_popular_movies) {
            currentList = mPopularMoviesList;
            refreshWithParameters(mPopularMoviesList);
            //refreshList(mPopularMoviesList);
        } else if (menuItem == R.id.sort_top_rated_movies) {
            currentList = mTopRatedMovieList;
            refreshWithParameters(mTopRatedMovieList);
        } else if (menuItem == R.id.about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshWithParameters(ArrayList<Movie> movieList) {
        boolean internet_status = NetworkUtils.getNetworkState(MainActivity.this);
        if (internet_status) {
            if (IS_APP_INITIALIZED) {
                mRefreshButton.setVisibility(View.GONE);
                //startApp();
                refreshList(movieList);
            } else {
                return;
            }
        } else {
            mRefreshButton.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No Internet connection detected", Toast.LENGTH_LONG).show();
        }
    }

    private void refreshList(ArrayList<Movie> movies) {
        if (!IS_APP_INITIALIZED) {
            new FetchMovies().execute();
        }
        MovieAdapter movieAdapter = new MovieAdapter(MainActivity.this, movies);
        mGridView.invalidateViews();
        mGridView.setAdapter(movieAdapter);
    }

    public void refreshApp(View view) {
        startApp();
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
            IS_APP_INITIALIZED = true;


            if (NetworkUtils.getNetworkState(MainActivity.this)) {

                try {
                    Uri uri = Uri.parse(POPULAR_MOVIES_URL)
                            .buildUpon()
                            .appendQueryParameter("api_key", AUTH_TOKEN)
                            .appendQueryParameter("sort_by", QUERY_PARM_POPULARITY_DESC)
                            .build();
                    mPopularMoviesList = NetworkUtils.fetchApi(uri.toString());

                    uri = Uri.parse(TOP_RATED_URL)
                            .buildUpon()
                            .appendQueryParameter("api_key", AUTH_TOKEN)
                            .appendQueryParameter("sort_by", QUERY_PARAM_TOP_RATED_DESC)
                            .build();
                    mTopRatedMovieList = NetworkUtils.fetchApi(uri.toString());

                    if (currentList == null || currentList.size() == 0) {
                        currentList = mPopularMoviesList;
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MainActivity.this,
                        "No internet connection detected",
                        Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            refreshList(currentList);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
