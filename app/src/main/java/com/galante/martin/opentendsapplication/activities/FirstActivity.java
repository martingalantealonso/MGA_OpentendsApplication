package com.galante.martin.opentendsapplication.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.galante.martin.opentendsapplication.HeroCharacter;
import com.galante.martin.opentendsapplication.HeroListRcVwAdapter;
import com.galante.martin.opentendsapplication.R;
import com.galante.martin.opentendsapplication.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class FirstActivity extends AppCompatActivity {

    //TODO generate s String file
    private String ts = System.currentTimeMillis() + "";
    private String public_key = "cd31f94797faaf1c26a65f7a20cb086b";
    private String private_key = "33ae0776e56c8c24f8edf1f315c8c2e4dc68f609";
    private String hash = getMD5(ts + private_key + public_key);
    String limit = "10";
    String api_url = "http://gateway.marvel.com/v1/public/characters?ts=" + ts + "&apikey=" + public_key + "&hash=" + hash + "&limit=" + limit;
    String api_url_namestartswith;
    private EditText edtxt_filter;
    private int numCharacters = 0;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.first_activity_title);

        mRecyclerView = (RecyclerView) findViewById(R.id.heroes_recycler_view);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        edtxt_filter = (EditText) findViewById(R.id.ed_txt_search);

        edtxt_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                numCharacters = edtxt_filter.length();
                if (numCharacters < 3) {
                    mRecyclerView.setAdapter(null);
                } else if (numCharacters >= 3) {
                    api_url_namestartswith = "http://gateway.marvel.com/v1/public/characters?nameStartsWith=" + edtxt_filter.getText() + "&ts=" + ts + "&apikey=" + public_key + "&hash=" + hash;
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, api_url_namestartswith,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("Response", response.toString());
                                    try {
                                        JSONObject jsonResponse = new JSONObject((response));
                                        JSONObject jsonData = jsonResponse.getJSONObject("data");
                                        JSONArray jsonResults = jsonData.getJSONArray("results");
                                        ArrayList<HeroCharacter> newCharacter = HeroCharacter.fromJson(jsonResults);
                                        HeroListRcVwAdapter adapter2 = new HeroListRcVwAdapter(newCharacter);
                                        mRecyclerView.setAdapter(adapter2);
                                        adapter2.setOnItemClickListener(onItemClickListener); //Add the clicl listener
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

                    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                }

            }
        });


    }

    public static String getMD5(String input) {
        try {
            Log.i("string = ", input);
            MessageDigest mMessageDigest = MessageDigest.getInstance("MD5");
            byte[] messageDigest = mMessageDigest.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    HeroListRcVwAdapter.OnItemClickListener onItemClickListener = new HeroListRcVwAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {

            LinearLayout placeNameHolder = (LinearLayout) v.findViewById(R.id.main_information_holder);
            NetworkImageView heroImage = (NetworkImageView) v.findViewById(R.id.network_image_view);
            TextView heroName = (TextView) v.findViewById(R.id.text_view_name);
            TextView heroDescription = (TextView) v.findViewById(R.id.text_view_description);


            //To get the Bitmap image
            Bitmap _bitmap = ((BitmapDrawable) heroImage.getDrawable()).getBitmap();
            ByteArrayOutputStream _bs = new ByteArrayOutputStream();
            if (_bitmap != null) {
                _bitmap.compress(Bitmap.CompressFormat.PNG, 50, _bs);
            }
            Intent transitionIntent = new Intent(FirstActivity.this, SecondActivity.class);
            transitionIntent.putExtra(String.valueOf(SecondActivity.EXTRA_PARAM_ID), position);
            transitionIntent.putExtra(String.valueOf(SecondActivity.EXTRA_PARAM_NAME), heroName.getText().toString());
            transitionIntent.putExtra(String.valueOf(SecondActivity.EXTRA_PARAM_DESCRIPTION), heroDescription.getText().toString());
            transitionIntent.putExtra(String.valueOf(SecondActivity.EXTRA_PARAM_IMAGE), _bs.toByteArray());


            View navigationBar = findViewById(android.R.id.navigationBarBackground);
            View statusBar = findViewById(android.R.id.statusBarBackground);

            Pair<View, String> holderPair = Pair.create((View) placeNameHolder, "tNameHolder");
            Pair<View, String> navPair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
            Pair<View, String> statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);


            // If the device has NavigationBar
            ActivityOptionsCompat options;
            if (ViewConfiguration.get(getApplicationContext()).hasPermanentMenuKey()) {
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(FirstActivity.this, holderPair, navPair, statusPair);
            } else {
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(FirstActivity.this, holderPair, statusPair);
            }
            ActivityCompat.startActivity(FirstActivity.this, transitionIntent, options.toBundle());
        }


        public boolean hasNavBar(Resources resources) {
            int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
            return id > 0 && resources.getBoolean(id);
        }
    };
}
