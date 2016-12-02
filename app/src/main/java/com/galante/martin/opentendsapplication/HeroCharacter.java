package com.galante.martin.opentendsapplication;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Martin on 02/12/2016.
 */

public class HeroCharacter {
    public String hero_name;
    public String hero_description;
    public Bitmap hero_image;

    public HeroCharacter(String hero_name, String hero_description) {
        this.hero_name = hero_name;
        this.hero_description = hero_description;
    }

    public HeroCharacter(JSONObject object) {
        try {
            this.hero_name = object.getString("name");
            this.hero_description = object.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<HeroCharacter> fromJson(JSONArray jsonObjects) {
        ArrayList<HeroCharacter> characters = new ArrayList<HeroCharacter>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                characters.add(new HeroCharacter(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return characters;
    }
}
