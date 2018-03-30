package com.example.root.child;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.root.child.R.layout.user_single_layout;

/**
 * Created by root on 1/3/18.
 */

public class userAdapter extends RecyclerView.Adapter<userAdapter.userAdapterViewHolder> {


    public interface OnItemClickListener{
        void onItemClick(SingleUserclass item);
    }

    private ArrayList<SingleUserclass> user;
    private OnItemClickListener listener;
    public  userAdapter(ArrayList<SingleUserclass> user,OnItemClickListener listner)
    {
       this.user = user;
       this.listener = listner;
    }
    @Override
    public userAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_single_layout,parent,false);
        return new userAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(userAdapterViewHolder holder, int position) {
         SingleUserclass title = user.get(position);
         holder.singleUserEmail.setText(title.getEmail());
         holder.singleUserName.setText(title.getName());
         if(!title.getImage().equals("Default"))
            Picasso.with(holder.itemView.getContext()).load(title.getImage()).into(holder.singleUserImage);
         holder.bind(user.get(position),listener);

    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    public class userAdapterViewHolder extends RecyclerView.ViewHolder{
        private ImageView singleUserImage;
        private TextView singleUserName,singleUserEmail;
        public userAdapterViewHolder(View itemView) {
            super(itemView);
            singleUserImage = itemView.findViewById(R.id.singleUserPhoto);
            singleUserName = itemView.findViewById(R.id.singleUserName);
            singleUserEmail = itemView.findViewById(R.id.singleUserEmail);
        }
        public void bind(final SingleUserclass demoUser, final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(demoUser);
                }
            });
        }
    }
}
