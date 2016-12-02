package com.galante.martin.opentendsapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.galante.martin.opentendsapplication.R;

/**
 * Created by Martin on 02/12/2016.
 */

public class HeroListAdapter extends RecyclerView.Adapter<HeroListAdapter.ViewHolder> {

    Context mContext;
  public HeroListAdapter(Context context){
      this.mContext=context;
  }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HeroData().heroList().size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout heroHolder;
        public LinearLayout heroAttributesHolder;
        public TextView txtview_heroName;
        public TextView txtview_heroDescription;
        public ImageView imgview_heroImage;

        public ViewHolder(View itemView){
            super(itemView);
            heroHolder=(LinearLayout)itemView.findViewById(R.id.main_holder);
            heroAttributesHolder=(LinearLayout)itemView.findViewById(R.id.main_information_holder);
            txtview_heroName=(TextView)itemView.findViewById(R.id.text_view_name);
            txtview_heroDescription=(TextView)itemView.findViewById(R.id.text_view_description);
            imgview_heroImage=(ImageView) itemView.findViewById(R.id.imageViewList);
        }
    }
}
