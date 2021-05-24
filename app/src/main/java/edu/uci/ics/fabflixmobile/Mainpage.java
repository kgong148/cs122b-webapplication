package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;


public class Mainpage extends ActionBarActivity {

    private EditText searchText;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // upon creation, inflate and initialize the layout
        setContentView(R.layout.mainpage);

        searchText = findViewById(R.id.searchBar);
        searchButton = findViewById(R.id.searchButton);

        //assign a listener to call a function to handle the user request when clicking a button
        searchButton.setOnClickListener(view -> search());
    }

    public void search() {


        Intent listPage = new Intent(Mainpage.this, ListViewActivity.class);
        listPage.putExtra("SEARCHTEXT", searchText.getText().toString());
        // activate the list page.
        startActivity(listPage);




    }
}