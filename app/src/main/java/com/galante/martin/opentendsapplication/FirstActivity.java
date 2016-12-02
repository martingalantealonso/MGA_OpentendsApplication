package com.galante.martin.opentendsapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class FirstActivity extends AppCompatActivity {

    private long tsLong = System.currentTimeMillis();
    private String ts = System.currentTimeMillis() + "";
    private String public_key = "cd31f94797faaf1c26a65f7a20cb086b";
    private String private_key = "33ae0776e56c8c24f8edf1f315c8c2e4dc68f609";
    private String hash = getMD5(ts + private_key + public_key);
    String limit = "10";
    String api_url = "http://gateway.marvel.com/v1/public/characters?ts=" + ts + "&apikey=" + public_key + "&hash=" + hash + "&limit=" + limit;

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.first_activity_title);

        //mRecyclerView = (RecyclerView) findViewById(R.id.heroes_list);
        //mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        //mRecyclerView.setLayoutManager(mStaggeredLayoutManager);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response.toString());
                       try {
                            JSONObject jsonResponse=new JSONObject((response));
                            JSONObject jsonData= jsonResponse.getJSONObject("data");
                            JSONArray jsonResults=jsonData.getJSONArray("results");

                           getHeroes(jsonResults);
                        } catch (JSONException e) {
                         e.printStackTrace();
                        }

                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response", error.toString());
            }
        });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
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

    private void getHeroes(JSONArray jsonArray) {
        ArrayList<HeroCharacter> heroCharacterList = new ArrayList<HeroCharacter>();
        HeroAdapter adapter = new HeroAdapter(this, heroCharacterList);
        ArrayList<HeroCharacter> newCharacter = HeroCharacter.fromJson(jsonArray);
        adapter.addAll(newCharacter);
        ListView listView=(ListView)findViewById(R.id.lvHeroes);
        listView.setAdapter(adapter);
    }
}
