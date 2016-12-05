package com.galante.martin.opentendsapplication.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.galante.martin.opentendsapplication.R;
import com.galante.martin.opentendsapplication.adapters.ComicAdapter;
import com.galante.martin.opentendsapplication.adapters.EventAdapter;
import com.galante.martin.opentendsapplication.adapters.VolleySingleton;
import com.galante.martin.opentendsapplication.data.Comic;
import com.galante.martin.opentendsapplication.data.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.galante.martin.opentendsapplication.data.GlobalData.hash;
import static com.galante.martin.opentendsapplication.data.GlobalData.public_key;
import static com.galante.martin.opentendsapplication.data.GlobalData.ts;

public class SecondActivity extends AppCompatActivity {


    public static final String EXTRA_PARAM_HERO_ID = "HeroID";
    public static final String EXTRA_PARAM_NAME = "HeroName";
    public static final String EXTRA_PARAM_DESCRIPTION = "HeroDescription";
    public static final String EXTRA_PARAM_IMAGE = "HeroImage";
    private String detail_link;
    private String wiki_link;
    private String comic_link;
    private LinearLayout mainInformationHolder;
    private ImageView imageView;
    private TextView textViewName;
    private TextView textViewDescription;
    private Button btnDetails;
    private Button btnWiki;
    private Button btnComics;
    private TabHost tabs;
    private String heroCharacterID;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViews();
        loadHeroInfo();
    }


    private void findViews() {

        mainInformationHolder = (LinearLayout) findViewById(R.id.main_information_holder);
        imageView = (ImageView) findViewById(R.id.image_view);
        textViewName = (TextView) findViewById(R.id.text_view_name);
        textViewDescription = (TextView) findViewById(R.id.text_view_description);
        btnDetails = (Button) findViewById(R.id.btn_details);
        btnWiki = (Button) findViewById(R.id.btn_wiki);
        btnComics = (Button) findViewById(R.id.btn_comics);
        tabs = (TabHost) findViewById(android.R.id.tabhost);

        //region Set Tabs

        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Cómics");
        tabs.addTab(spec);

        spec = tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Eventos");
        tabs.addTab(spec);

        tabs.setCurrentTab(0);
        //endregion Set Tabs

        mRecyclerView = (RecyclerView) findViewById(R.id.comic_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(llm);

        mRecyclerViewEvents = (RecyclerView) findViewById(R.id.event_list);
        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        mRecyclerViewEvents.setLayoutManager(llm2);
    }

    private void loadHeroInfo() {

        //First, get the Intent extras
        heroCharacterID = getIntent().getExtras().getString(EXTRA_PARAM_HERO_ID);
        textViewName.setText(getIntent().getExtras().getString(EXTRA_PARAM_NAME));
        textViewDescription.setText(getIntent().getExtras().getString(EXTRA_PARAM_DESCRIPTION));
        Bitmap _bitmap = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra(EXTRA_PARAM_IMAGE), 0, getIntent().getByteArrayExtra(EXTRA_PARAM_IMAGE).length);
        imageView.setImageBitmap(_bitmap);
        if (_bitmap != null) {
            colorize(_bitmap);
        }

        //Second, get the urls links for the buttons with a new request to the Marvel API;
        //region StringRequest URL LINKS
        String api_url_id = "http://gateway.marvel.com/v1/public/characters/" + heroCharacterID + "?&ts=" + ts + "&apikey=" + public_key + "&hash=" + hash;
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, api_url_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ID", response);
                        try {
                            JSONObject jsonResponse = new JSONObject((response));
                            JSONObject jsonData = jsonResponse.getJSONObject("data");
                            JSONArray jsonResults = jsonData.getJSONArray("results");


                            JSONArray jsonUrlLinks = null;
                            for (int j = 0; j < jsonResults.length(); j++) {
                                jsonUrlLinks = jsonResults.getJSONObject(j).getJSONArray("urls");
                            }
                            for (int j = 0; j < jsonUrlLinks.length(); j++) {
                                if (jsonUrlLinks.getJSONObject(j).getString("type").equals("detail")) {
                                    detail_link = jsonUrlLinks.getJSONObject(j).getString("url");
                                    setAnimation(btnDetails);
                                }
                                if (jsonUrlLinks.getJSONObject(j).getString("type").equals("wiki")) {
                                    wiki_link = jsonUrlLinks.getJSONObject(j).getString("url");
                                    setAnimation(btnWiki);
                                }
                                if (jsonUrlLinks.getJSONObject(j).getString("type").equals("comiclink")) {
                                    comic_link = jsonUrlLinks.getJSONObject(j).getString("url");
                                    setAnimation(btnComics);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response", error.toString());
            }
        });
        //endregion StringRequest
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        //Third , get the comic list of the current Hero
        //region StringRequest COMICS
        String api_url_character_comics = "http://gateway.marvel.com/v1/public/characters/" + heroCharacterID + "/comics?&ts=" + ts + "&apikey=" + public_key + "&hash=" + hash;
        final StringRequest stringRequestComics = new StringRequest(Request.Method.GET, api_url_character_comics,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ID", response);
                        try {
                            JSONObject jsonResponse = new JSONObject((response));
                            JSONObject jsonData = jsonResponse.getJSONObject("data");
                            JSONArray jsonResults = jsonData.getJSONArray("results");

                            ArrayList<Comic> newComics = Comic.fromJson(jsonResults);
                            ComicAdapter comicAdapter = new ComicAdapter(newComics);
                            mRecyclerView.setAdapter(comicAdapter);

                            TextView tv = (TextView) tabs.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
                            tv.setText("(" + newComics.size() + ") Cómics");
                            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                            alphaAnimation.setDuration(1500);
                            tv.startAnimation(alphaAnimation);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response", error.toString());
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequestComics);
        //endregion

        //At the end, load the Event list of the current Hero
        //region Events
        String api_url_character_events = "http://gateway.marvel.com/v1/public/characters/" + heroCharacterID + "/events?&ts=" + ts + "&apikey=" + public_key + "&hash=" + hash;
        final StringRequest stringRequestEvents = new StringRequest(Request.Method.GET, api_url_character_events,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ID", response);
                        try {
                            JSONObject jsonResponse = new JSONObject((response));
                            JSONObject jsonData = jsonResponse.getJSONObject("data");
                            JSONArray jsonResults = jsonData.getJSONArray("results");

                            ArrayList<Event> newEvents = Event.fromJson(jsonResults);
                            EventAdapter eventAdapter = new EventAdapter(newEvents);
                            mRecyclerViewEvents.setAdapter(eventAdapter);

                            TextView tv = (TextView) tabs.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
                            tv.setText("(" + newEvents.size() + ") Eventos");
                            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                            alphaAnimation.setDuration(1500);
                            tv.startAnimation(alphaAnimation);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response", error.toString());
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequestEvents);
        //endregion

    }

    private void colorize(Bitmap _bitmap) {
        int default_color = ContextCompat.getColor(getApplicationContext(),R.color.light_gray);
        Palette mPalette = Palette.from(_bitmap).generate();
        mainInformationHolder.setBackgroundColor(mPalette.getLightVibrantColor(default_color));

        //Change the statusbar color
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(mPalette.getLightVibrantColor(default_color));
        }
    }

    private void setAnimation(View v) {
        v.setEnabled(true);
        v.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);
        v.startAnimation(alphaAnimation);
    }

    //OnClick buttons -> Go to the url provided
    public void goToDetail(View view) {
        goToUrl(detail_link);
    }

    public void goToWiki(View view) {
        goToUrl(wiki_link);
    }

    public void goToComics(View view) {
        goToUrl(comic_link);
    }

    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

}
