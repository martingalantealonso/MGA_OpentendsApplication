package com.galante.martin.opentendsapplication;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Martin on 03/12/2016.
 */

public class HeroListRcVwAdapter extends RecyclerView.Adapter<HeroListRcVwAdapter.HeroViewHolder> {


    List<HeroCharacter> heroesList;

    public HeroListRcVwAdapter(List<HeroCharacter> heroes) {
        this.heroesList = heroes;
    }

    @Override
    public HeroListRcVwAdapter.HeroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_heroes, parent, false);
        HeroViewHolder mHeroViewHolder = new HeroViewHolder(v);
        return mHeroViewHolder;
    }

    @Override
    public void onBindViewHolder(HeroListRcVwAdapter.HeroViewHolder holder, int position) {
        holder.heroName.setText(heroesList.get(position).hero_name);
        holder.heroDescription.setText(heroesList.get(position).hero_description);
        // personViewHolder.personPhoto.setImageResource(persons.get(i).photoId);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return heroesList.size();
    }

    public static class HeroViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView heroName;
        TextView heroDescription;
        //ImageView heroimage;

        HeroViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.heroCard);
            heroName = (TextView) itemView.findViewById(R.id.text_view_name);
            heroDescription = (TextView) itemView.findViewById(R.id.text_view_description);
            //personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }

}
