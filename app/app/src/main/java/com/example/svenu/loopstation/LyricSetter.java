package com.example.svenu.loopstation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Class which parses the html until the lyrics are kept.
 */

public class LyricSetter {

    private Context context;

    public LyricSetter(Context aContext) {
        context = aContext;
    }

    private String responseToLyrics(String response) {
        // Function to convert raw html to lyrics.

        // Lyrics start at div class lyrics.
        String lyrics = response.split("<div class=\"lyrics\">\n")[1];

        // Count the div and /div to check where the lyrics end.
        String div = "<div";
        int divLen = div.length();
        String endDiv = "</div";
        int endDivLen = endDiv.length();
        int divCount = 1;
        int endDivCount = 0;

        int lyricsLength = lyrics.length();
        int lyricsIndex = 0;
        while (lyricsIndex < lyricsLength - endDivLen) {
            if (lyrics.substring(lyricsIndex, lyricsIndex + divLen).equals(div)) {
                divCount += 1;
            }
            else if (lyrics.substring(lyricsIndex, lyricsIndex + endDivLen).equals(endDiv)) {
                endDivCount += 1;
            }
            if (divCount == endDivCount) {
                lyrics = lyrics.substring(0, lyricsIndex);
                break;
            }
            lyricsIndex += 1;
        }

        // Delete everything between < and >
        lyrics = lyrics.replaceAll("(?s)<.*?>", "");

        // Delete spaces and enters in front of the string.
        while (lyrics.substring(0, 1).equals(" ") || lyrics.substring(0,1).equals("\n")) {
            lyrics = lyrics.substring(1);
            // Log the first 10 characters.
            Log.d("lyrics", unEscapeString(lyrics));
        }

        // Delete spaces and enters at the end of the string.
        lyricsLength = lyrics.length();
        while (lyrics.substring(lyricsLength - 1, lyricsLength).equals(" ") || lyrics.substring(lyricsLength - 1, lyricsLength).equals("\n")) {
            lyrics = lyrics.substring(0, lyricsLength - 1);
            lyricsLength = lyrics.length();
            // Log the last 10 characters.
            Log.d("end", unEscapeString(lyrics.substring(lyricsLength - 10, lyricsLength)));
        }

        lyrics = lyrics.replace("&amp;", "&");

        return lyrics;
    }

    public void setLyrics(String url, final TextView lyricTextView) {
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Convert html to lyrics.
                        String lyrics = responseToLyrics(response);
                        lyricTextView.setText(lyrics);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // If the request failed, let the user know.
                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the queue.
        queue.add(stringRequest);
    }

    @NonNull
    private static String unEscapeString(String s){
        // Function to show escapecharacters.
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<s.length(); i++)
            switch (s.charAt(i)){
                case '\n': sb.append("\\n"); break;
                case '\t': sb.append("\\t"); break;
                case ' ': sb.append("<space>"); break;
                default: sb.append(s.charAt(i));
            }
        return sb.toString();
    }
}
