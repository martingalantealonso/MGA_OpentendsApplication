package com.galante.martin.opentendsapplication.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.galante.martin.opentendsapplication.R;
import com.galante.martin.opentendsapplication.data.Comic;

import java.util.List;

/**
 * Created by Martin on 04/12/2016.
 *
 */

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {

    private List<Comic> comicList;


    public ComicAdapter(List<Comic> comics) {
        this.comicList = comics;
    }

    public class ComicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView comicTitle;
        TextView comicDescription;
        NetworkImageView comicImage;
        ImageLoader mImageLoader;

        ComicViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.comicCard);
            comicTitle = (TextView) itemView.findViewById(R.id.text_view_comic_title);
            comicDescription = (TextView) itemView.findViewById(R.id.text_view_comic_description);
            comicImage = (NetworkImageView) itemView.findViewById(R.id.network_image_view_comic);
            mImageLoader = VolleySingleton.getInstance(itemView.getContext()).getImageLoader();
        }


        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public ComicAdapter.ComicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comics, parent, false);
        ComicAdapter.ComicViewHolder mComicViewHolder = new ComicAdapter.ComicViewHolder(v);
        return mComicViewHolder;
    }

    @Override
    public void onBindViewHolder(ComicViewHolder holder, int position) {
        holder.comicTitle.setText(comicList.get(position).comicTitle);
        holder.comicDescription.setText(comicList.get(position).comicDescription);

        if (comicList.get(position).comicImage != null) {
            if (!comicList.get(position).comicImage.isEmpty()) {
                holder.comicImage.setImageUrl(comicList.get(position).comicImage, holder.mImageLoader);
            }
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }


}
