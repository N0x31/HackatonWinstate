package fr.wcs.hackatonwinstate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListWinReceivedActivity extends AppCompatActivity {

    private DatabaseReference mUserReference;
    private ArrayList<UserModel> mUserWinSendList = new ArrayList<>();
    private RecyclerView mRecyclerViewWinReceived;
    private UserAdapter mUserAdapter;
    private String mUserId;
    private ArrayList<String> mLinkFirebase = new ArrayList<>();
    private ArrayList<String> mCitations = new ArrayList<>();
    private ArrayList<String> mCompliments = new ArrayList<>();
    private ArrayList<String> mDefis = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_win_received);

        // Get UserId
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ListWinReceivedActivity.this);
        mUserId = sharedPreferences.getString("mUserId", mUserId);

        mRecyclerViewWinReceived = findViewById(R.id.recyclerViewWinReceived);
        mUserAdapter = new UserAdapter(mUserWinSendList);

        mUserReference = FirebaseHelper.getDatabase().getReference("User");
        mUserReference.orderByChild(mUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    final UserModel userModel = dsp.getValue(UserModel.class);
                    mUserReference.child(dsp.getKey()).child("user_smiles").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                if(dsp.getKey().equals(mUserId)) {
                                    mUserWinSendList.add(userModel);
                                    mLinkFirebase.add(String.valueOf(dsp.getValue()));
                                }
                            }
                            mUserAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    mUserReference.child(dsp.getKey()).child("user_citations").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                if(dsp.getKey().equals(mUserId)) {
                                    mCitations.add(String.valueOf(dsp.getValue()));
                                }
                            }
                            mUserAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    mUserReference.child(dsp.getKey()).child("user_compliments").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                if(dsp.getKey().equals(mUserId)) {
                                    mCompliments.add(String.valueOf(dsp.getValue()));
                                }
                            }
                            mUserAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    mUserReference.child(dsp.getKey()).child("user_defis").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                if(dsp.getKey().equals(mUserId)) {
                                    mDefis.add(String.valueOf(dsp.getValue()));
                                }
                            }
                            mUserAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewWinReceived.setLayoutManager(mLayoutManager);
        mRecyclerViewWinReceived.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewWinReceived.addItemDecoration(new ListWinReceivedActivity.MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        mRecyclerViewWinReceived.setAdapter(mUserAdapter);

        mRecyclerViewWinReceived.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                mRecyclerViewWinReceived, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                UserModel userModel = mUserWinSendList.get(position);
                String otherUserUid = userModel.getUid();
                Intent intent = new Intent(ListWinReceivedActivity.this, WinRecuperationActivity.class);
                intent.putExtra("position", position);
                intent.putStringArrayListExtra("mLinkFirebase", mLinkFirebase);
                intent.putStringArrayListExtra("mCitations", mCitations);
                intent.putStringArrayListExtra("mCompliments", mCompliments);
                intent.putStringArrayListExtra("mDefis", mDefis);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public class MyDividerItemDecoration extends RecyclerView.ItemDecoration {
        private final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };

        public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
        public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

        private Drawable mDivider;
        private int mOrientation;
        private Context context;
        private int margin;

        public MyDividerItemDecoration(Context context, int orientation, int margin) {
            this.context = context;
            this.margin = margin;
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
            setOrientation(orientation);
        }

        public void setOrientation(int orientation) {
            if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
                throw new IllegalArgumentException("invalid orientation");
            }
            mOrientation = orientation;
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == VERTICAL_LIST) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }

        public void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left + dpToPx(margin), top, right - dpToPx(margin), bottom);
                mDivider.draw(c);
            }
        }

        public void drawHorizontal(Canvas c, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int left = child.getRight() + params.rightMargin;
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top + dpToPx(margin), right, bottom - dpToPx(margin));
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }

        private int dpToPx(int dp) {
            Resources r = context.getResources();
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
        }
    }
}

