package com.galante.martin.opentendsapplication;

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
    public String hero_image;


    public HeroCharacter(JSONObject object) {
        try {
            this.hero_name = object.getString("name");
            this.hero_description = object.getString("description");
            this.hero_image="";

            JSONObject heroImgData=object.getJSONObject("thumbnail");
            String noImg="image_not_available";
            if(!heroImgData.getString("path").contains(noImg)){
                this.hero_image=heroImgData.getString("path") + "." +heroImgData.getString("extension");
            }
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
