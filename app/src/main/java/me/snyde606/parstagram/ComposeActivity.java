package me.snyde606.parstagram;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import me.snyde606.parstagram.models.Post;
import me.snyde606.parstagram.models.PostObj;

public class ComposeActivity extends AppCompatActivity {

    private RecyclerView rvPosts;
    private PostAdapter postAdapter;
    private ImageView ivPost;
    private ArrayList<PostObj> posts;
    SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        rvPosts = findViewById(R.id.rvPosts);
        ivPost = findViewById(R.id.ivPost);
        posts = new ArrayList<PostObj>();
        postAdapter = new PostAdapter(posts, new PostAdapter.ClickListener() {
            @Override
            public void switchToDetail(int position) {
                Intent i = new Intent(ComposeActivity.this, DetailActivity.class);
                i.putExtra("post", Parcels.wrap(posts.get(position)));
                startActivity(i);
            }

            //TODO - favorite/unfavorite feature

        });

        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setAdapter(postAdapter);

        ivPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComposeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        populateTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void logOut(MenuItem mi){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParseUser.logOut();
                        Intent i = new Intent(ComposeActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public void populateTimeline(){
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                posts.clear();
                postAdapter.notifyDataSetChanged();
                if (objects == null) return;
                for(int i = 0; i < objects.size(); i++) {
                    if (e == null) {
                        PostObj post = new PostObj();
                        post.setDescription(objects.get(i).getDescription());
                        post.setUsername(objects.get(i).getUser().getUsername());
                        post.setCreatedAt(objects.get(i).getCreatedAtString());
                        post.setLikes(objects.get(i).getLikes());
                        try {
                            post.setImageFile(objects.get(i).getImage().getFile());
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        posts.add(0, post);
                        postAdapter.notifyItemInserted(0);
                    } else {
                        e.printStackTrace();
                    }
                }
                swipeContainer.setRefreshing(false);
            }
        });
    }

}
