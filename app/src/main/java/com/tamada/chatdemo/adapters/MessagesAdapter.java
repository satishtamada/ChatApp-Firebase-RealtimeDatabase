package com.tamada.chatdemo.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tamada.chatdemo.R;
import com.tamada.chatdemo.models.Message;

import java.util.List;

/**
 * Created by dev 18/2/16.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {
    private final List<Message> messageList;
    private final Context context;
    private String currentUserName;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView lblName;

        public MyViewHolder(View view) {
            super(view);
            lblName = (TextView) view.findViewById(R.id.idMessage);
        }
    }


    public MessagesAdapter(List<Message> messageList, Context context, String currentUserName) {
        this.messageList = messageList;
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
        final Message message = messageList.get(position);
        Log.e("name",message.getFromName());
        if (currentUserName.equals(message.getFromName())) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.lblName.setLayoutParams(params);
           // holder.lblName.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
            holder.lblName.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
        } else {
            holder.lblName.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);

        }
        holder.lblName.setText(message.getMessage());

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

}
