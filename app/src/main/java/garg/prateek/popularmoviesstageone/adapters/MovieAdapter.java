package garg.prateek.popularmoviesstageone.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import garg.prateek.popularmoviesstageone.R;
import garg.prateek.popularmoviesstageone.utilities.Movie;

public class MovieAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<Movie> mMovieList;
    public static final String MOVIE_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.mContext = context;
        this.mMovieList = movies;
    }

    @Override
    public int getCount() {
        return mMovieList.size();
    }

    @Override
    public Movie getItem(int position) {
        return mMovieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        Movie movie = getItem(position);
        RelativeLayout relativeLayout = new RelativeLayout(mContext);
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(200, 300));
        if (convertView == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setAdjustViewBounds(true);
            relativeLayout.addView(imageView);
        }
        else{
            imageView = (ImageView) convertView;
        }

        Picasso.get().load(MOVIE_POSTER_BASE_URL + movie.getPosterPath())
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        return imageView;
    }
}
