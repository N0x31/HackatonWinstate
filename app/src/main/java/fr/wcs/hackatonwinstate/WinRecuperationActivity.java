package fr.wcs.hackatonwinstate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class WinRecuperationActivity extends AppCompatActivity {

    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_recuperation);

        // OtherUserUid
        Intent intent = getIntent();
        final int position = intent.getIntExtra("position", 0);
        final ArrayList<String> mLinkFirebase = intent.getStringArrayListExtra("mLinkFirebase");

        String link = mLinkFirebase.get(position);

        // Get UserId
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WinRecuperationActivity.this);
        mUserId = sharedPreferences.getString("mUserId", mUserId);

        CircleImageView imageViewWinReceived = findViewById(R.id.imageViewWinReceived);
        Glide.with(WinRecuperationActivity.this)
                .load(link)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(imageViewWinReceived);

        TextView textViewCitations = findViewById(R.id.textViewCitations);
        TextView textViewCompliments = findViewById(R.id.textViewCompliments);
        TextView textViewDefi = findViewById(R.id.textViewDefi);
    }
}
