package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import android.util.Log;

public class MovieListViewAdapter extends ArrayAdapter<Movie> {
    private final ArrayList<Movie> movies;

    public MovieListViewAdapter(ArrayList<Movie> movies, Context context) {
        super(context, R.layout.row, movies);
        this.movies = movies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.row, parent, false);

        Movie movie = movies.get(position);

        TextView titleView = view.findViewById(R.id.title);
        TextView yearView = view.findViewById(R.id.year);
        TextView directorView = view.findViewById(R.id.director);
        TextView genresView = view.findViewById(R.id.genres);
        TextView starsView = view.findViewById(R.id.stars);

        titleView.setText(movie.getName());
        // need to cast the year to a string to set the label
        yearView.setText(movie.getYear() + "");
        directorView.setText("Director: "+movie.getDirector());

        String[] genres = movie.getGenres();
        String genres_str = "";
        for(int i = 0; i < genres.length; i++)
        {
            if(genres[i].equals("")) break;
            if(i > 0) genres_str+= ", ";
            genres_str += genres[i];
        }
        genresView.setText("Genres: "+genres_str);

        String[] stars = movie.getStars();
        String stars_str = "";
        for(int i = 0; i < stars.length; i++)
        {
            if(stars[i].equals("")) break;
            if(i > 0) stars_str+= ", ";
            stars_str += stars[i];
        }
        starsView.setText("Stars: "+stars_str);

        return view;
    }
}
