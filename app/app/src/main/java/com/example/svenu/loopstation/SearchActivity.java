package com.example.svenu.loopstation;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sven Uitendaal.
 * Activity where the user can search songs via the genius api. When the user found a song and
 * clicks on it, the mainactivity is loaded with the lyrics of the chosen song.
 */

public class SearchActivity extends AppCompatActivity {

    private EditText editText;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Set activity title.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.search_name);
        }

        // Set global variables and listeners.
        setVariables();
        setButtonHandlers();
    }

    private class GoResultListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // Load mainactivity with the lyrics from the chosen song.
            TextView titleTextView = view.findViewById(R.id.textViewSongTitle);
            String title = titleTextView.getText().toString();
            String url = titleTextView.getTag().toString();
            loadNewActivity(title, url);
        }
    }

    private class GoSearchListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // When a new character is typed, search songs with the text from the edittext.
            searchSongs(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    private void loadNewActivity(String title, String url) {
        // Load the mainactivity with the lyrics from the chosen song.
        Intent intent = new Intent(this, MainActivity.class);

        // isCover tells the mainactivity to load the lyrics.
        intent.putExtra("isCover", true);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Load the menu with the right menuoptions.
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();

        // Tell the menu which menuoptions must be visible.
        new MenuVisibility(this, menu, inflater);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        // When an option is clicked, load a new activity.
        MenuOption menuOption = new MenuOption(item);
        menuOption.loadActivity(this);
        return true;
    }

    private void searchSongs(String searchText) {
        // Function to search songs at the genius api with a given query.

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.genius.com/search?access_token=oTGffkjbM7uilgg3gsJnjV3mwHUTQr3XzaOkJOGHfjCncEbw85_pTxpIsFg2ktM6&q=";
        url = url + searchText.replace(" ", "_");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // JSON response.
                    JSONArray hits = response.getJSONObject("response").getJSONArray("hits");

                    // Arraylist to store the results.
                    ArrayList<Song> songs = new ArrayList<>();

                    // Add results to arraylist.
                    int numberOfHits = hits.length();
                    for (int i = 0; i < numberOfHits; i++) {
                        JSONObject hit = hits.getJSONObject(i);
                        String type = hit.getString("type");

                        // If the type of the result is song, add the result to the arraylist.
                        if (type.equals("song")) {
                            JSONObject result = hit.getJSONObject("result");
                            String title = result.getString("title");
                            String artist = result.getJSONObject("primary_artist").getString("name");
                            String url = result.getString("url");
                            Song song = new Song(title, artist, url);
                            songs.add(song);
                        }
                    }

                    // Refresh the listview.
                    updateSongAdapter(songs);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Let the user know if the request failed.
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        // Add request to queue.
        queue.add(jsonObjectRequest);
    }

    private void setButtonHandlers() {
        // Set the listeners.
        editText.addTextChangedListener(new GoSearchListener());
        listView.setOnItemClickListener(new GoResultListener());
    }

    private void setVariables() {
        // Set the global variables.
        editText = findViewById(R.id.searchEditText);
        listView = findViewById(R.id.searchListView);
    }

    private void updateSongAdapter(ArrayList<Song> songs) {
        // Refresh the listview.
        SongAdapter songAdapter = new SongAdapter(this, songs);
        listView.setAdapter(songAdapter);
    }
}
