package com.ketekmall.ketekmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.data.ChatSession;
import com.ketekmall.ketekmall.data.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatSession_Adapter extends RecyclerView.Adapter<ChatSession_Adapter.ViewHolder> {

    Context context;
    private List<ChatSession> userList;
    private OnItemClickListener mListener;

    public ChatSession_Adapter(Context context, List<ChatSession> userList) {
        this.context = context;
        this.userList = userList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_chat_inbox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChatSession user = userList.get(position);

        String username = user.getChatWith();
        String photo = user.getChatWithPhoto();
        String chat_count = user.getChatCount();

        holder.username.setText(username);
        if(chat_count.equals("0")){
            holder.chat_badge.setVisibility(View.GONE);
        }
        holder.chat_badge.setText(chat_count);
        Picasso.get().load(photo).into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView username, chat_badge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.user_chatname);
            chat_badge = itemView.findViewById(R.id.chat_badge);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
