package com.example.svenu.loopstation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

public class SearchActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setTitle(R.string.search_name);

        listView = findViewById(R.id.searchListView);
        listView.setOnItemClickListener(new GoResultListener());

        EditText editText = findViewById(R.id.searchEditText);
        editText.addTextChangedListener(new GoSearchListener());
    }

    private class GoResultListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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
            searchSongs(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    private void loadNewActivity(String title, String url) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isCover", true);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();

        new MenuVisibility(menu, inflater, false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        MenuOption menuOption = new MenuOption(item);
        menuOption.loadActivity(this);
        return true;
    }

    private void searchSongs(String searchText) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.genius.com/search?access_token=oTGffkjbM7uilgg3gsJnjV3mwHUTQr3XzaOkJOGHfjCncEbw85_pTxpIsFg2ktM6&q=";
        url = url + searchText.replace(" ", "_");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray hits = response.getJSONObject("response").getJSONArray("hits");
                    ArrayList<Song> songs = new ArrayList<>();
                    int numberOfHits = hits.length();
                    for (int i = 0; i < numberOfHits; i++) {
                        JSONObject hit = hits.getJSONObject(i);
                        String type = hit.getString("type");
                        if (type.equals("song")) {
                            JSONObject result = hit.getJSONObject("result");
                            String title = result.getString("title");
                            Log.d("JsonObjectRequest", title);
                            String artist = result.getJSONObject("primary_artist").getString("name");
                            String url = result.getString("url");
                            Song song = new Song(title, artist, url);
                            songs.add(song);
                        }
                    }
                    updateSongAdapter(songs);
                }
                catch (JSONException exception) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void updateSongAdapter(ArrayList<Song> songs) {
        SongAdapter songAdapter = new SongAdapter(this, songs);
        listView.setAdapter(songAdapter);
    }
}
