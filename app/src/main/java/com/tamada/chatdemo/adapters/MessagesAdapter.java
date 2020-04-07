package com.tamada.chatdemo.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
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
public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<MessagesModel> messagesModelList;
    private final Context context;
    private String currentUserName;
    public static final int SENDER_VALUE=1;
    public static final int RECEIVER_VALUE=2;

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        public final TextView lblName;

        public SenderViewHolder(View view) {
            super(view);
            lblName = (TextView) view.findViewById(R.id.idMessage);
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        public final TextView lblName;

        public ReceiverViewHolder(View view) {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case SENDER_VALUE:
                View v1 = inflater.inflate(R.layout.item_sender_msg, parent, false);
                viewHolder = new SenderViewHolder(v1);
                break;

            default:
                View v = inflater.inflate(R.layout.item_reciver_msg, parent, false);
                viewHolder = new ReceiverViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        MessagesModel messagesModel=messagesModelList.get(position);
        if(currentUserName.equals(messagesModel.getFromName())){
            return SENDER_VALUE;
        }else {
           return RECEIVER_VALUE;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MessagesModel messagesModel = messagesModelList.get(position);
        //set gravity for current user messages
        if(holder.getItemViewType()==SENDER_VALUE){
            SenderViewHolder viewHolder=(SenderViewHolder) holder;
            viewHolder.lblName.setText(messagesModel.getMessage());
        }else{
            ReceiverViewHolder viewHolder=(ReceiverViewHolder) holder;
            viewHolder.lblName.setText(messagesModel.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messagesModelList.size();
    }

}
