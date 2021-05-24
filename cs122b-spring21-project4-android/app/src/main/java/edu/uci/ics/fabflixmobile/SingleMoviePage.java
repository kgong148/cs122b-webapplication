package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class SingleMoviePage extends ActionBarActivity {
    private TextView movieTitle;
    private TextView movieDirector;
    private TextView movieYear;
    private TextView movieGenres;
    private TextView textStars;
    private Movie currMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Upon create initialize layout
        setContentView(R.layout.singlemovie);

        movieTitle = findViewById(R.id.movieTitle);
        movieDirector = findViewById(R.id.movieDirector);
        movieYear = findViewById(R.id.movieYear);
        movieGenres = findViewById(R.id.movieGenres);
        textStars = findViewById(R.id.textStars);
        setMovie();
    }

    public void setMovie(){
        Intent temp = getIntent();
        currMovie = (Movie)temp.getSerializableExtra("currMovie");

        movieTitle.setText("Title: " + currMovie.getName());
        movieDirector.setText("Director: " + currMovie.getDirector());
        movieYear.setText("Year: " + currMovie.getYear());
        String[] genres = currMovie.getGenres();
        String genres_str = "";
        for(int i = 0; i < genres.length; i++)
        {
            if(genres[i].equals("")) break;
            if(i > 0) genres_str+= ", ";
            genres_str += genres[i];
        }
        movieGenres.setText("Genres: " + genres_str);
        textStars.setText("Stars:");

        StarListAdapter adapter = new StarListAdapter(new ArrayList<String>(Arrays.asList(currMovie.getStars())), this);

        ListView listView = findViewById(R.id.movieStars);
        listView.setAdapter(adapter);

    }
}
