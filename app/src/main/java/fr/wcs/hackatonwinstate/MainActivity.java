package fr.wcs.hackatonwinstate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private String mUserId;
    private DatabaseReference mUserReference;
    private UserModel mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        mUserId = sharedPreferences.getString("mUserId", mUserId);

        // Go to ProfileActivity
        ImageButton profilebutton = findViewById(R.id.imageButtonMember);
        profilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GiActivity.class);
                startActivity(intent);
            }
        });

        // Go to PictureActivity
        ImageButton takepicturebutton = findViewById(R.id.takepicturebutton);
        takepicturebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListUsersActivity.class);
                startActivity(intent);
            }
        });

        // Go to ListWinReceivedActivity
        ImageButton seewinsbutton = findViewById(R.id.seewinsbutton);
        seewinsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListWinReceivedActivity.class);
                startActivity(intent);
            }
        });

        TextView textViewNumberHackteurs = findViewById(R.id.textViewNumberHackteurs);
        TextView hackteursWinsNumber = findViewById(R.id.hackteursWinsNumber);

        final TextView memberName = findViewById(R.id.memberName);
        final TextView memberwinsNumber = findViewById(R.id.memberwinsNumber);

        mUserReference = FirebaseHelper.getDatabase().getReference("User").child(mUserId);
        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mUser = snapshot.getValue(UserModel.class);
                memberName.setText(mUser.getUser_name());
                memberwinsNumber.setText(String.valueOf(mUser.getUser_win_numbers()));
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }
}