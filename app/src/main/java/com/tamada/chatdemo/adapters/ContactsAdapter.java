package com.tamada.chatdemo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamada.chatdemo.R;
import com.tamada.chatdemo.models.ContactModel;

import java.util.List;

/**
 * Created by satish .
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {
    private final List<ContactModel> contactModelList;
    private final Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView lblName;
        public final TextView lblEmail;

        public MyViewHolder(View view) {
            super(view);
            lblName = (TextView) view.findViewById(R.id.friendName);
            lblEmail = (TextView) view.findViewById(R.id.friendEmail);
        }
    }


    public ContactsAdapter(List<ContactModel> contactModelList, Context context) {
        this.contactModelList = contactModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ContactModel messagesModel = contactModelList.get(position);
        holder.lblName.setText(messagesModel.getUserName());
        holder.lblName.setText(messagesModel.getEmail());

        //set gravity for current user messages

    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }

}
