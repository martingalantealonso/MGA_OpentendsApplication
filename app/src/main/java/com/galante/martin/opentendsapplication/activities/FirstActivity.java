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
import com.galante.martin.opentendsapplication.R;
import com.galante.martin.opentendsapplication.adapters.HeroListRcVwAdapter;
import com.galante.martin.opentendsapplication.adapters.VolleySingleton;
import com.galante.martin.opentendsapplication.data.HeroCharacter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.galante.martin.opentendsapplication.data.GlobalData.hash;
import static com.galante.martin.opentendsapplication.data.GlobalData.public_key;
import static com.galante.martin.opentendsapplication.data.GlobalData.ts;

public class FirstActivity extends AppCompatActivity {



    String api_url_namestartswith;
    private EditText edtxt_filter;
    private int numCharacters = 0;
    private RecyclerView mRecyclerView;

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
                                    Log.d("Response", response);
                                    try {
                                        JSONObject jsonResponse = new JSONObject((response));
                                        JSONObject jsonData = jsonResponse.getJSONObject("data");
                                        JSONArray jsonResults = jsonData.getJSONArray("results");
                                        ArrayList<HeroCharacter> newCharacter = HeroCharacter.fromJson(jsonResults);
                                        HeroListRcVwAdapter adapter2 = new HeroListRcVwAdapter(newCharacter);
                                        mRecyclerView.setAdapter(adapter2);
                                        adapter2.setOnItemClickListener(onItemClickListener); //Add the click listener
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

    HeroListRcVwAdapter.OnItemClickListener onItemClickListener = new HeroListRcVwAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {

            LinearLayout placeNameHolder = (LinearLayout) v.findViewById(R.id.main_information_holder);
            NetworkImageView heroImage = (NetworkImageView) v.findViewById(R.id.network_image_view);
            TextView heroID = (TextView) v.findViewById(R.id.text_view_id);
            TextView heroName = (TextView) v.findViewById(R.id.text_view_name);
            TextView heroDescription = (TextView) v.findViewById(R.id.text_view_description);


            //To get the Bitmap image
            Bitmap _bitmap = ((BitmapDrawable) heroImage.getDrawable()).getBitmap();
            ByteArrayOutputStream _bs = new ByteArrayOutputStream();
            if (_bitmap != null) {
                _bitmap.compress(Bitmap.CompressFormat.PNG, 50, _bs);
            }
            Intent transitionIntent = new Intent(FirstActivity.this, SecondActivity.class);
            transitionIntent.putExtra(String.valueOf(SecondActivity.EXTRA_PARAM_HERO_ID), heroID.getText().toString());
            transitionIntent.putExtra(String.valueOf(SecondActivity.EXTRA_PARAM_NAME), heroName.getText().toString());
            transitionIntent.putExtra(String.valueOf(SecondActivity.EXTRA_PARAM_DESCRIPTION), heroDescription.getText().toString());
            transitionIntent.putExtra(String.valueOf(SecondActivity.EXTRA_PARAM_IMAGE), _bs.toByteArray());


            Pair<View, String> holderPair = Pair.create((View) placeNameHolder, "tNameHolder");
            Pair<View, String> navPair=null;
            Pair<View, String> statusPair=null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                View navigationBar = findViewById(android.R.id.navigationBarBackground);
                View statusBar = findViewById(android.R.id.statusBarBackground);
                navPair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
                statusPair= Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
            }
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
