package com.galante.martin.opentendsapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by Martin on 02/12/2016.
 */

public class HeroAdapter extends ArrayAdapter<HeroCharacter> {
    public HeroAdapter(Context context, ArrayList<HeroCharacter> heroes) {
        super(context, 0, heroes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HeroCharacter character = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_hero, parent, false);
        }
        TextView txName = (TextView) convertView.findViewById(R.id.chName);
        TextView txDescription = (TextView) convertView.findViewById(R.id.chDescription);

        txName.setText(character.hero_name);
        txDescription.setText(character.hero_description);

        if (!character.hero_image.isEmpty()) {
            NetworkImageView mNetworkImageView=(NetworkImageView)convertView.findViewById(R.id.network_image_view);
            ImageLoader mImageLoader = VolleySingleton.getInstance(getContext()).getImageLoader();
            mNetworkImageView.setImageUrl(character.hero_image,mImageLoader);
        }
        return convertView;
    }
}
