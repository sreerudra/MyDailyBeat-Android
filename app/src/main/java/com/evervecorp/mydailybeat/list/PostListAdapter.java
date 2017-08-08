package com.evervecorp.mydailybeat.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import com.evervecorp.mydailybeat.R;
import com.evervecorp.mydailybeat.comm.RestAPI;
import com.evervecorp.mydailybeat.object.Post;

/**
 * Created by Virinchi on 4/24/2017.
 */

public class PostListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Post> posts;

    public PostListAdapter(Context context) {
        this.context = context;
        this.posts = new ArrayList<>();
    }

    public void setPosts(ArrayList<Post> newPosts) {
        this.posts = newPosts;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return posts.get(position).getPOST_ID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Post p = posts.get(position);
        boolean hasImage = (p.getIMG_URL() != null);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (hasImage) {
            view = inflater.inflate(R.layout.post_layout_with_picture, null);
        } else {
            view = inflater.inflate(R.layout.post_layout_no_picture, null);
        }

        String userName = RestAPI.getInstance(context).getCurrentUser().getScreenName();
        TextView userNameText = (TextView) view.findViewById(R.id.screenNameTextView);
        userNameText.setText(userName);
        TextView bdText = (TextView) view.findViewById(R.id.postTextView);
        bdText.setText(p.getBD_TEXT());
        Button deleteButton = (Button) view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Delete post
            }
        });

        return view;
    }
}
