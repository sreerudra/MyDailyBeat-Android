package edu.bu.met.vbalabhadrapatruni.mypeeps.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import edu.bu.met.vbalabhadrapatruni.mypeeps.R;
import edu.bu.met.vbalabhadrapatruni.mypeeps.comm.RestAPI;
import edu.bu.met.vbalabhadrapatruni.mypeeps.list.PostListAdapter;
import edu.bu.met.vbalabhadrapatruni.mypeeps.object.Group;
import edu.bu.met.vbalabhadrapatruni.mypeeps.object.Post;

/**
 * Created by Virinchi on 4/24/2017.
 */

public class GroupActivity extends AppCompatActivity {

    Group group;
    PostListAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        new GetPostsTask(this.group).execute();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_main);

        Intent intent = getIntent();

        group = (Group) intent.getSerializableExtra("GROUP");
        TextView groupNameTxtVw = (TextView) findViewById(R.id.groupNameTextView);
        groupNameTxtVw.setText(group.getName());

        Button newPostButton = (Button) findViewById(R.id.newPostButton);
        newPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupActivity.this.createPost();
            }
        });
        ListView postsList = (ListView) findViewById(R.id.postList);
        adapter = new PostListAdapter(GroupActivity.this);
        postsList.setAdapter(adapter);
    }

    private void createPost() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);
        builder.setTitle("New Post");
        final EditText postBodyText = new EditText(GroupActivity.this);
        postBodyText.setHint("Post Body");
        builder.setView(postBodyText);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // create group
                String body = postBodyText.getText().toString();
                new CreatePostTask(body).execute();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class GetPostsTask extends AsyncTask<Void, Void, ArrayList<Post>> {
        ProgressDialog mDialog;
        Group group;
        public GetPostsTask(Group group) {
            this.group = group;
            mDialog = new ProgressDialog(GroupActivity.this);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.setMessage("Fetching posts...");
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setIndeterminate(true);
            mDialog.show();
        }

        @Override
        protected ArrayList<Post> doInBackground(Void... params) {
            try {
                List<Post> list =  RestAPI.getInstance(GroupActivity.this).getPostsForGroup(this.group.getGROUP_ID());
                ArrayList<Post> realList = new ArrayList<>();
                realList.addAll(list);
                return realList;
            } catch (Exception e) {
                Log.e("MyPeeps", e.getLocalizedMessage());
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Post> posts) {
            super.onPostExecute(posts);
            mDialog.hide();
            adapter.setPosts(posts);

        }
    }

    private class CreatePostTask extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog mDialog;
        String postBody;
        public CreatePostTask(String postBody) {
            this.postBody = postBody;
            mDialog = new ProgressDialog(GroupActivity.this);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.setMessage("Creating post...");
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setIndeterminate(true);
            mDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return RestAPI.getInstance(GroupActivity.this).createPost(postBody, GroupActivity.this.group.getGROUP_ID());
            } catch (Exception e) {
                Log.e("MyPeeps", e.getLocalizedMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            mDialog.hide();
            new GetPostsTask(GroupActivity.this.group).execute();
        }
    }
}
