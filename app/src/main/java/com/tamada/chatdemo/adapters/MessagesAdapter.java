package com.tamada.chatdemo.adapters;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tamada.chatdemo.R;
import com.tamada.chatdemo.models.MessagesModel;

import java.util.List;

/**
 * Created by satish .
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {
    private final List<MessagesModel> messagesModelList;
    private final Context context;
    private String currentUserName;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView lblName;

        public MyViewHolder(View view) {
            super(view);
            lblName = (TextView) view.findViewById(R.id.idMessage);
        }
    }


    public MessagesAdapter(List<MessagesModel> messagesModelList, Context context, String currentUserName) {
        this.messagesModelList = messagesModelList;
        this.context = context;
        this.currentUserName = currentUserName;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MessagesModel messagesModel = messagesModelList.get(position);
        //set gravity for current user messages
        if (currentUserName.equals(messagesModel.getFromName())) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.lblName.setLayoutParams(params);
            holder.lblName.setTextColor(ContextCompat.getColor(context,R.color.colorWhite));
            holder.lblName.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_sender_msg));
        } else {
            holder.lblName.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
            holder.lblName.setTextColor(ContextCompat.getColor(context,R.color.colorBlack));
            holder.lblName.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_receiver_msg));
        }
        holder.lblName.setText(messagesModel.getMessage());

    }

    @Override
    public int getItemCount() {
        return messagesModelList.size();
    }

}
