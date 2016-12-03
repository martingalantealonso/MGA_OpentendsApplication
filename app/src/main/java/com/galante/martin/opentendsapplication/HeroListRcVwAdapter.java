package com.galante.martin.opentendsapplication;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by Martin on 03/12/2016.
 */

public class HeroListRcVwAdapter extends RecyclerView.Adapter<HeroListRcVwAdapter.HeroViewHolder> {

    private Context mContext;
    private List<HeroCharacter> heroesList;
    private OnItemClickListener mItemClickListener;

    public HeroListRcVwAdapter(Context context) {
        this.mContext = context;
    }

    public HeroListRcVwAdapter(List<HeroCharacter> heroes) {
        this.heroesList = heroes;
    }

    public class HeroViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout placeHolder;
        CardView cv;
        TextView heroName;
        TextView heroDescription;
        NetworkImageView heroimage;
        ImageLoader mImageLoader;

        HeroViewHolder(View itemView) {
            super(itemView);
            placeHolder = (LinearLayout) itemView.findViewById(R.id.main_holder);
            cv = (CardView) itemView.findViewById(R.id.heroCard);
            heroName = (TextView) itemView.findViewById(R.id.text_view_name);
            heroDescription = (TextView) itemView.findViewById(R.id.text_view_description);
            heroimage = (NetworkImageView) itemView.findViewById(R.id.network_image_view);
            mImageLoader = VolleySingleton.getInstance(itemView.getContext()).getImageLoader();
            placeHolder.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public HeroListRcVwAdapter.HeroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_heroes, parent, false);
        HeroViewHolder mHeroViewHolder = new HeroViewHolder(v);
        return mHeroViewHolder;
    }

    @Override
    public void onBindViewHolder(final HeroListRcVwAdapter.HeroViewHolder holder, int position) {
        holder.heroName.setText(heroesList.get(position).hero_name);
        holder.heroDescription.setText(heroesList.get(position).hero_description);
        if (!heroesList.get(position).hero_image.isEmpty()) {
            holder.heroimage.setImageUrl(heroesList.get(position).hero_image, holder.mImageLoader);

        /*
        //TODO try to solvethis
            Bitmap bitmap = ((BitmapDrawable)holder.heroimage.getDrawable()).getBitmap();
            Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {
                    int bgColor = palette.getMutedColor(mContext.getResources().getColor(android.R.color.black));
                    holder.placeHolder.setBackgroundColor(bgColor);
                }
            });
*/
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return heroesList.size();
    }


}
