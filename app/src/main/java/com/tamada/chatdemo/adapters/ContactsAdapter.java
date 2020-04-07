package com.tamada.chatdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tamada.chatdemo.R;
import com.tamada.chatdemo.models.ContactModel;
import com.tamada.chatdemo.models.UserModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by satish .
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {
    private final ArrayList<UserModel> contactModelList;
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


    public ContactsAdapter(ArrayList<UserModel> contactModelList, Context context) {
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
        final UserModel messagesModel = contactModelList.get(position);
        holder.lblName.setText(messagesModel.getUserName());
        holder.lblEmail.setText(messagesModel.getPhoneNumber());

        //set gravity for current user messages

    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }

}
