package com.tamada.chatdemo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamada.chatdemo.R;
import com.tamada.chatdemo.models.Message;

import java.util.List;

/**
 * Created by dev 18/2/16.
 */
public class LatLongAdapter extends RecyclerView.Adapter<LatLongAdapter.MyViewHolder> {
    private final List<Message> latLongModelList;
    private final Context context;
    private String userId;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView lblName;

        public MyViewHolder(View view) {
            super(view);
            lblName = (TextView) view.findViewById(R.id.idMessage);
        }
    }


    public LatLongAdapter(List<Message> latLongModelList, Context context, String userId) {
        this.latLongModelList = latLongModelList;
        this.context = context;
        this.userId = userId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Message latLongModel = latLongModelList.get(position);
        holder.lblName.setGravity(Gravity.RIGHT);
        holder.lblName.setText(latLongModel.getMessage());
        //  holder.lblAddress.setText(getCompleteAddressString(latLongModel.getLatitude(), latLongModel.getLongitude()) + " (" + latLongModel.getLatitude() + "," + latLongModel.getLongitude()+")");
    }

    @Override
    public int getItemCount() {
        return latLongModelList.size();
    }

}
