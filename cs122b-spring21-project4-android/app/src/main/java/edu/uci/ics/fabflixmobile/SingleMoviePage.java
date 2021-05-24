package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SingleMoviePage extends ActionBarActivity {
    /*
  In Android, localhost is the address of the device or the emulator.
  To connect to your machine, you need to use the below IP address
 */
    private final String host = "ec2-3-15-192-28.us-east-2.compute.amazonaws.com";
    private final String port = "8080";
    private final String domain = "cs122b-spring21-project1";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;

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
        Bundle extras = getIntent().getExtras();
        String searchtext = "";
        String id = "";
        if(extras !=null) {
            searchtext = extras.getString("currMovie");
            id = extras.getString("id");
        }


        ArrayList<Movie> movie = new ArrayList<>();
        // use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        String finalId = id;
        final StringRequest singleMovieRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/single-movie?id=" + id,
                response -> {
                    try {
                        JSONArray responseJsonArray = new JSONArray(response);

                        JSONObject jsonObject = (JSONObject) responseJsonArray.get(0);
                        Log.d("movie-list.success", jsonObject.toString());
                        String title = jsonObject.getString("movie_title");
                        String year = jsonObject.getString("movie_year");
                        String movie_id = jsonObject.getString("movie_id");
                        String director = jsonObject.getString("movie_director");
                        String[] genre_list = new String[50];
                        for(int j = 0; j < jsonObject.getInt("genre_size"); j++)
                        {
                            String genre = jsonObject.getString("movie_genres_"+j);
                            if(genre == null) break;
                            genre_list[j] = genre;
                        }
                        String[] star_list = new String[responseJsonArray.length()];
                        for(int k = 0; k < responseJsonArray.length(); k++)
                        {
                            JSONObject jsonObject2 = (JSONObject) responseJsonArray.get(k);
                            String star = jsonObject2.getString("star_name");
                            if(star == null || star.equals("")) break;
                            star_list[k] = star;
                        }
                        movie.add(new Movie(title, movie_id, Short.parseShort(year), director, genre_list, star_list));
                        Log.d("movie-within.success", ""+movie.size());

                        currMovie = movie.get(0);
                        //SingleMovieAdapter adapter = new SingleMovieAdapter(movie, this);
                        movieTitle.setText("Title: " + currMovie.getName());
                        movieDirector.setText("Director: " + currMovie.getDirector());
                        movieYear.setText("Year: " + currMovie.getYear());
                        String[] genres = currMovie.getGenres();
                        String genres_str = "";
                        for(int j = 0; j < genres.length; j++)
                        {
                            if(genres[j] == null) break;
                            if(j > 0) genres_str+= ", ";
                            genres_str += genres[j];
                        }
                        movieGenres.setText("Genres: " + genres_str);
                        textStars.setText("Stars:");

                        StarListAdapter adapter = new StarListAdapter(new ArrayList<String>(Arrays.asList(currMovie.getStars())), this);

                        ListView listView = findViewById(R.id.movieStars);
                        listView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // error
                    Log.d("movie-list.error", error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                // POST request form data
                final Map<String, String> params = new HashMap<>();
                params.put("id", finalId);
                return params;
            }
        };

        queue.add(singleMovieRequest);









        /*Intent temp = getIntent();
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
        listView.setAdapter(adapter);*/

    }
}
