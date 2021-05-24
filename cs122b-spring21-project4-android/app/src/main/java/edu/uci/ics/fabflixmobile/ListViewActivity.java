package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListViewActivity extends Activity {
    /*
  In Android, localhost is the address of the device or the emulator.
  To connect to your machine, you need to use the below IP address
 */
    private final String host = "ec2-3-137-222-2.us-east-2.compute.amazonaws.com";
    private final String port = "8080";
    private final String domain = "cs122b-spring21-project1";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;

    private Button prevButton;
    private Button nextButton;

    private int page = 1;
    private boolean last_page = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        prevButton.setOnClickListener(view -> setMovies(-1));
        nextButton.setOnClickListener(view -> setMovies(1));
        setMovies(0);

    }

    public void setMovies(int action)
    {
        if(action == -1 && page-1 <= 0) return;
        if(action == 1 && last_page) return;
        page+= action;
        Bundle extras = getIntent().getExtras();
        String searchtext = "";
        if(extras !=null) {
            searchtext = extras.getString("SEARCHTEXT");
        }
        if(searchtext.length() > 0)
            searchtext = "&title=" +searchtext;
        else
            searchtext = "";
        Log.d("Search-text-sucess", searchtext);
        ArrayList<Movie> movies = new ArrayList<>();
        // use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest movieListRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/movie-list?amount=20&page="+(page+action)+searchtext,
                response -> {
                    try {
                        JSONArray responseJsonArray = new JSONArray(response);

                        for(int i = 0; i < responseJsonArray.length()-1; i++)
                        {
                            JSONObject jsonObject = (JSONObject) responseJsonArray.get(i);
                            Log.d("movie-list.success", jsonObject.toString());
                            String title = jsonObject.getString("movie_title");
                            String year = jsonObject.getString("movie_year");
                            String movie_id = jsonObject.getString("movie_id");
                            String director = jsonObject.getString("movie_director");
                            String[] genre_list = new String[3];
                            for(int j = 0; j < 3; j++)
                            {
                                String genre = jsonObject.getString("movie_genre_"+j);
                                if(genre == null) break;
                                genre_list[j] = genre;
                            }
                            String[] star_list = new String[3];
                            for(int k = 0; k < 3; k++)
                            {
                                String star = jsonObject.getString("movie_stars_"+k);
                                if(star == null) break;
                                star_list[k] = star;
                            }
                            movies.add(new Movie(title, movie_id, Short.parseShort(year), director, genre_list, star_list));
                            Log.d("movie-within.success", ""+movies.size());
                        }

                        JSONObject jsonObject = (JSONObject) responseJsonArray.get(responseJsonArray.length()-1);
                        last_page = Boolean.parseBoolean(jsonObject.getString("EndOfQuery"));

                        MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);

                        ListView listView = findViewById(R.id.list);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener((parent, view, position, id) -> {
                            Movie movie = movies.get(position);
                            String message = String.format("Clicked on position: %d, name: %s, %d", position, movie.getName(), movie.getYear());
                            Log.d("click.success", message);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        });
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
                return params;
            }
        };

        queue.add(movieListRequest);
    }
}
