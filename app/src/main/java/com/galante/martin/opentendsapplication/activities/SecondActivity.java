package com.galante.martin.opentendsapplication.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.galante.martin.opentendsapplication.R;
import com.galante.martin.opentendsapplication.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.galante.martin.opentendsapplication.activities.FirstActivity.getMD5;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    private String ts = System.currentTimeMillis() + "";
    private String public_key = "cd31f94797faaf1c26a65f7a20cb086b";
    private String private_key = "33ae0776e56c8c24f8edf1f315c8c2e4dc68f609";
    private String hash = getMD5(ts + private_key + public_key);
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
    private TextView txtvwLinks;
    private Button btnDetails;
    private Button btnWiki;
    private Button btnComics;
    private TextView txtvwResources;
    private String heroCharacterID;

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
        txtvwLinks = (TextView) findViewById(R.id.txtvw_links);
        btnDetails = (Button) findViewById(R.id.btn_details);
        btnWiki = (Button) findViewById(R.id.btn_wiki);
        btnComics = (Button) findViewById(R.id.btn_comics);
        txtvwResources = (TextView) findViewById(R.id.txtvw_resources);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        /*
        btnDetails.setOnClickListener(this);
        btnWiki.setOnClickListener(this);
        btnComics.setOnClickListener(this);
*/

    }

    private void loadHeroInfo() {

        //First, get the Intent extras
        heroCharacterID = getIntent().getExtras().getString(EXTRA_PARAM_HERO_ID);
        textViewName.setText(getIntent().getExtras().getString(EXTRA_PARAM_NAME));
        textViewDescription.setText(getIntent().getExtras().getString(EXTRA_PARAM_DESCRIPTION));
        Bitmap _bitmap = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra(EXTRA_PARAM_IMAGE), 0, getIntent().getByteArrayExtra(EXTRA_PARAM_IMAGE).length);
        imageView.setImageBitmap(_bitmap);
        //TODO change this?
        if (_bitmap != null) {
            colorize(_bitmap);
        }


        //Second, get the data with a new request to the Marvel API

        //region StringRequest
        String api_url_id = "http://gateway.marvel.com/v1/public/characters/" + heroCharacterID + "?&ts=" + ts + "&apikey=" + public_key + "&hash=" + hash;
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, api_url_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ID", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject((response));
                            JSONObject jsonData = jsonResponse.getJSONObject("data");
                            JSONArray jsonResults = jsonData.getJSONArray("results");

                            //TODO try to get the image again to set the size

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

    }

    private void setAnimation(View v) {
        v.setEnabled(true);
        v.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);
        v.startAnimation(alphaAnimation);
    }


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


    private void colorize(Bitmap _bitmap) {
        int default_color = getResources().getColor(R.color.light_gray);
        Palette mPalette = Palette.from(_bitmap).generate();
        mainInformationHolder.setBackgroundColor(mPalette.getLightVibrantColor(default_color));
        //View view = this.getWindow().getDecorView();
        //view.setBackgroundColor(mPalette.getLightMutedColor(default_color));
    }

    @Override
    public void onClick(View v) {
        if (v == btnDetails) {
            // Handle clicks for btnDetails
        } else if (v == btnWiki) {
            // Handle clicks for btnWiki
        } else if (v == btnComics) {
            // Handle clicks for btnComics
        }
    }
}
