package com.galante.martin.opentendsapplication.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Martin Galante on 04/12/2016.
 *
 */

public class Event {

    public String eventTitle;
    public String eventDescription;
    public String eventImage;

    public Event(JSONObject object) {
        try {
            this.eventTitle = object.getString("title");
            this.eventDescription = object.getString("description");

            JSONObject imgData = object.getJSONObject("thumbnail");
            CharSequence noimg = "image_not_available";
            if (!imgData.getString("path").contains(noimg)) {
                this.eventImage = imgData.getString("path") + "." + imgData.getString("extension");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Event> fromJson(JSONArray jsonArray) {

        ArrayList<Event> events = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                events.add(new Event(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return events;
    }
}
