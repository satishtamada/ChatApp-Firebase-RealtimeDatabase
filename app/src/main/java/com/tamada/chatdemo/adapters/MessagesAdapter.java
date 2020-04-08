package com.tamada.chatdemo.adapters;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    public static final int SENDER_VALUE = 1;
    public static final int RECEIVER_VALUE = 2;
    private int longClickedPosition = -1;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        public final TextView lblName;

        public SenderViewHolder(View view) {
            super(view);
            lblName = (TextView) view.findViewById(R.id.idMessage);
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        public final TextView lblName;
        public final TextView idReply;
        public final TextView idReplyOne, idReplyTwo;
        public final LinearLayout replyContainer;

        public ReceiverViewHolder(View view) {
            super(view);
            lblName = (TextView) view.findViewById(R.id.idMessage);
            idReply = (TextView) view.findViewById(R.id.idReply);
            idReplyOne = (TextView) view.findViewById(R.id.idReplyOne);
            idReplyTwo = (TextView) view.findViewById(R.id.idReplyTwo);
            replyContainer = (LinearLayout) view.findViewById(R.id.idReplyContainer);
        }
    }


    public MessagesAdapter(List<MessagesModel> messagesModelList, Context context, String currentUserName, FirebaseDatabase firebaseDatabase, DatabaseReference databaseReference) {
        this.messagesModelList = messagesModelList;
        this.context = context;
        this.currentUserName = currentUserName;
        this.firebaseDatabase = firebaseDatabase;
        this.databaseReference = databaseReference;
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
        MessagesModel messagesModel = messagesModelList.get(position);
        if (currentUserName.equals(messagesModel.getFromName())) {
            return SENDER_VALUE;
        } else {
            return RECEIVER_VALUE;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MessagesModel messagesModel = messagesModelList.get(position);
        //set gravity for current user messages
        if (holder.getItemViewType() == SENDER_VALUE) {
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.lblName.setText(messagesModel.getMessage());
        } else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.lblName.setText(messagesModel.getMessage());
            if (!TextUtils.isEmpty(messagesModel.getMessageReply())) {
                viewHolder.idReply.setText(messagesModel.getMessageReply());
                viewHolder.idReply.setVisibility(View.VISIBLE);
            } else {
                viewHolder.idReply.setVisibility(View.GONE);
            }

            viewHolder.lblName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (TextUtils.isEmpty(messagesModel.getMessageReply())) {
                        Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                        viewHolder.replyContainer.setVisibility(View.VISIBLE);
                        viewHolder.replyContainer.startAnimation(slideUp);
                    }
                    return true;
                }
            });
            viewHolder.lblName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewHolder.replyContainer.getVisibility() == View.VISIBLE) {
                        viewHolder.replyContainer.setVisibility(View.GONE);
                    }
                }
            });

            viewHolder.idReplyOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.child("1111").child(messagesModel.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().child("messageReply").setValue(viewHolder.idReplyOne.getText().toString());
                            viewHolder.replyContainer.setVisibility(View.GONE);
                            messagesModel.setMessage(viewHolder.idReplyOne.getText().toString());
                            notifyItemChanged(position);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d("User", databaseError.getMessage());
                        }
                    });
                }
            });

            viewHolder.idReplyTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.child("1111").child(messagesModel.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().child("messageReply").setValue(viewHolder.idReplyTwo.getText().toString());
                            viewHolder.replyContainer.setVisibility(View.GONE);
                            messagesModel.setMessage(viewHolder.idReplyTwo.getText().toString());
                            notifyItemChanged(position);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d("User", databaseError.getMessage());
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messagesModelList.size();
    }

}
