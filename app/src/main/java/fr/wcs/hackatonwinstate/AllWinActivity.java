package fr.wcs.hackatonwinstate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AllWinActivity extends AppCompatActivity {

    private DatabaseReference mGeneralReference;
    private int mTotalWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_win);

        final TextView TVShowCount = (TextView) findViewById(R.id.textView2);

        mGeneralReference = FirebaseHelper.getDatabase().getReference("General").child("totalWin");
        mGeneralReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mTotalWin = Integer.valueOf((Integer) snapshot.getValue());
                TVShowCount.setText(mTotalWin);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }
}
