package fr.wcs.hackatonwinstate;

import android.content.ContentResolver;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.bumptech.glide.Glide;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.MediaType;
import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.giphy.sdk.core.network.response.ListMediaResponse;

import java.io.IOException;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageButton;

public class GiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gi);

        GifImageButton img1 = findViewById(R.id.imageButton1);
        GifImageButton img2 = findViewById(R.id.imageButton2);
        GifImageButton img3 = findViewById(R.id.imageButton3);
        GifImageButton img4 = findViewById(R.id.imageButton4);
        GifImageButton img5 = findViewById(R.id.imageButton5);
        GifImageButton img6 = findViewById(R.id.imageButton6);
        GifImageButton img7 = findViewById(R.id.imageButton7);
        GifImageButton img8 = findViewById(R.id.imageButton8);
        GifImageButton img9 = findViewById(R.id.imageButton9);


        GPHApi client = new GPHApiClient("akv9rME8CnVs9R1szFI1qgHcstn239J7");

        /// Gif Search
        client.search("cats", MediaType.gif, null, null, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                if (result == null) {
                    // Do what you want to do with the error
                } else {
                    if (result.getData() != null) {
                        for (Media gif : result.getData()) {
                            Log.v("giphy", gif.getId());
                        }
                    } else {
                        Log.e("giphy error", "No results found");
                    }
                }
            }
        });

        String uuri = "https://giphy.com/stickers/imoji-cats-l0IyfKMG8wCXoQCuA";
        //Uri
        ContentResolver contentResolver = getContentResolver(); //can be null for file:// Uris
        try {
            GifDrawable gifFromUri = new GifDrawable(contentResolver, Uri.parse(uuri));
            img1.setImageResource(gifFromUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

/*        for (int i = 1; i < 9; i++ ){
            Glide.with(GiActivity.this)
                    .load("https://giphy.com/stickers/imoji-cats-l0IyfKMG8wCXoQCuA")
                    .into(img1);
        }*/
    }
}
