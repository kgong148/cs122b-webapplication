package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private final String host = "10.0.2.2";
    private final String port = "8080";
    private final String domain = "cs122b-spring21-project1-war";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        setMovies();
    }

    public void setMovies()
    {
        ArrayList<Movie> movies = new ArrayList<>();
        // use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest movieListRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/movie-list",
                response -> {
                    try {
                        JSONArray responseJsonArray = new JSONArray(response);

                        for(int i = 0; i < responseJsonArray.length()-1; i++)
                        {
                            JSONObject jsonObject = (JSONObject) responseJsonArray.get(i);
                            Log.d("movie-list.success", jsonObject.toString());
                            String title = jsonObject.getString("movie_title");
                            String year = jsonObject.getString("movie_year");
                            String id = jsonObject.getString("movie_id");
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
                            movies.add(new Movie(title, id, Short.parseShort(year), director, genre_list, star_list));
                            Log.d("movie-within.success", ""+movies.size());
                        }
                        MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);

                        ListView listView = findViewById(R.id.list);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener((parent, view, position, id) -> {
                            Movie movie = movies.get(position);
                            String message = String.format("Clicked on position: %d, name: %s, %d", position, movie.getName(), movie.getYear());
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