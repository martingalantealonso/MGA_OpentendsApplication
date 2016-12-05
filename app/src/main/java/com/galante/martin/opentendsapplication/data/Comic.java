package com.galante.martin.opentendsapplication.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Martin Galante on 04/12/2016.
 *
 */

public class Comic {

    public String comicTitle;
    public String comicDescription;
    public String comicImage;

    public Comic(JSONObject object) {
        try {
            this.comicTitle = object.getString("title");
            this.comicDescription = object.getString("description");

            JSONObject imgData = object.getJSONObject("thumbnail");
            CharSequence noimg = "image_not_available";
            if (!imgData.getString("path").contains(noimg)) {
                this.comicImage = imgData.getString("path") + "." + imgData.getString("extension");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Comic> fromJson(JSONArray jsonArray) {

        ArrayList<Comic> comics = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                comics.add(new Comic(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return comics;
    }
}
