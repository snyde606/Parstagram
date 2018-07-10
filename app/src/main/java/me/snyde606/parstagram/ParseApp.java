package me.snyde606.parstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.snyde606.parstagram.models.Post;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("snyder_instagram")
                .clientKey("fbu_snyder")
                .server("http://snyde606-fbu-instagram.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);
    }

}
