package com.application.workshop2.historicalxperience.helper;

/**
 * Created by User on 8/12/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.application.workshop2.historicalxperience.ItemClickListener;
import com.application.workshop2.historicalxperience.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    Context context;

    List<GetDataAdapter> getDataAdapter;

    ImageLoader imageLoader1;

    ItemClickListener mItemClickListener;

    public RecyclerViewAdapter(List<GetDataAdapter> getDataAdapter, Context context){

        super();
        this.getDataAdapter = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrievemtinfo, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        GetDataAdapter getDataAdapter1 =  getDataAdapter.get(position);

        imageLoader1 = ServerImageParseAdapter.getInstance(context).getImageLoader();

        imageLoader1.get(getDataAdapter1.getImageServerUrl(),
                ImageLoader.getImageListener(
                        Viewholder.networkImageView,//Server Image
                        R.mipmap.ic_launcher,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )
        );

        Viewholder.networkImageView.setImageUrl(getDataAdapter1.getImageServerUrl(), imageLoader1);

        Viewholder.ImageTitleNameView.setText(getDataAdapter1.getImageTitleName());

        Viewholder.ImageTitleID.setText(getDataAdapter1.getImageTitleId());

    }

    @Override
    public int getItemCount() {

        return getDataAdapter.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView ImageTitleNameView, ImageTitleID;
        public NetworkImageView networkImageView ;

        public ViewHolder(View itemView) {

            super(itemView);

            ImageTitleNameView = (TextView) itemView.findViewById(R.id.textView_item) ;

            ImageTitleID = (TextView)itemView.findViewById(R.id.mtlistID);

            networkImageView = (NetworkImageView) itemView.findViewById(R.id.VollyNetworkImageView1) ;

            itemView.setOnClickListener(this);
            // ImageTitleNameView.setOnClickListener(this);
            // ImageTitleID.setOnClickListener(this);

        }

        public TextView getTitle() {
            return ImageTitleID;
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) mItemClickListener.onClick(view, getAdapterPosition());
            //Log.i("positon-of-clicked-item", String.valueOf(ImageTitleNameView.getText()));
        }
    }
}