package me.snyde606.parstagram;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Locale;

import me.snyde606.parstagram.models.PostObj;

public class DetailActivity extends AppCompatActivity {

    private TextView tvDetailDescription;
    private TextView tvDetailUser;
    private TextView tvTimeAgo;
    private ImageView ivDetailImage;
    private PostObj post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        post = (PostObj) Parcels.unwrap(getIntent().getParcelableExtra("post"));

        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailUser = findViewById(R.id.tvDetailUser);
        ivDetailImage = findViewById(R.id.ivDetailImage);
        tvTimeAgo = findViewById(R.id.tvTimeAgo);

        tvDetailUser.setText(post.getUsername());
        tvDetailDescription.setText(post.getDescription());
        ivDetailImage.setImageBitmap(BitmapFactory.decodeFile(post.getImageFile().getAbsolutePath()));
        tvTimeAgo.setText(getRelativeTimeAgo(post.getCreatedAt()));
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
                        Intent i = new Intent(DetailActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        Log.d("PostAdapter", rawJsonDate);
        String twitterFormat = "EEE MMM dd HH:mm:ss zzz yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

}
