package garg.prateek.popularmoviesstageone.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkUtils{
    private static final String TAG = NetworkUtils.class.getSimpleName();
    public static ArrayList<Movie> fetchApi(String url) throws MalformedURLException {

        ArrayList<Movie> moviesFetched = new ArrayList<Movie>();
        URL apiUrl = new URL(url);

        String parsedResult = "";
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) apiUrl.openConnection();
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                parsedResult = scanner.next();
            } else {
                return null;
            }
        } catch (MalformedURLException e){
            Log.e(TAG, "fetchApi: ", e);
        } catch (IOException e){
            Log.e(TAG, "fetchApi: ", e);
        } finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }
        parseJson(parsedResult, moviesFetched);
        return moviesFetched;
    }

    private static void parseJson(String result, ArrayList<Movie> movies){

        try {
            JSONObject object = new JSONObject(result);
            JSONArray resultsArray = object.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); ++i){
                JSONObject jsonObject = resultsArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setId(jsonObject.getInt("id"));
                movie.setVoteCounts(jsonObject.getInt("vote_count"));
                movie.setTitle(jsonObject.getString("title"));
                movie.setReleaseDate(jsonObject.getString("release_date"));
                movie.setPosterPath(jsonObject.getString("poster_path"));
                movie.setPopularity(jsonObject.getDouble("popularity"));
                movie.setOverview(jsonObject.getString("overview"));
                movie.setOriginalTitle(jsonObject.getString("original_title"));
                movie.setBackdropPath(jsonObject.getString("backdrop_path"));
                movie.setAverageVotes((float)jsonObject.getDouble("vote_average"));
                movies.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean getNetworkState(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }

}


