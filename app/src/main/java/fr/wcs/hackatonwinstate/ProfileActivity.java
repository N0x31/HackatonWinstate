package fr.wcs.hackatonwinstate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference mUserReference;
    private CircleImageView mImageViewUserAvatar;
    private ProgressDialog mProgressDialog;
    private String mUserId;
    private UserModel mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        mUserId = sharedPreferences.getString("mUserId", mUserId);

        mImageViewUserAvatar = findViewById(R.id.imageViewUserAvatar);
        Button buttonTakeAvatar =  findViewById(R.id.buttonaddpicture);
        Button buttonConfirmAvatar =  findViewById(R.id.buttonconfirm);
        final TextView textViewUserName = findViewById(R.id.textViewHackteursName);
        final TextView textViewUserWinNumber = findViewById(R.id.textViewNumberWin);
        final TextView textViewUserLevelNumber = findViewById(R.id.textViewUserLevelNumber);
        final TextView textViewUserLevelName = findViewById(R.id.textViewLevelNameProfile);
        Button buttonSignOut = findViewById(R.id.buttonSignOut);

        mUserReference = FirebaseHelper.getDatabase().getReference("User").child(mUserId);
        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mUser = snapshot.getValue(UserModel.class);
                textViewUserName.setText(mUser.getUser_name());
                textViewUserWinNumber.setText(String.valueOf(mUser.getUser_win_numbers()));
                textViewUserLevelNumber.setText(String.valueOf(mUser.getUser_level_number()));
                textViewUserLevelName.setText(mUser.getUser_level_name());
                Glide.with(ProfileActivity.this)
                        .load(mUser.getUser_avatar())
                        .into(mImageViewUserAvatar);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        buttonTakeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(ProfileActivity.this);
            }
        });

        // Init Progress Dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle(getString(R.string.user_avatar_conf_send_photo_title));
        mProgressDialog.setMessage(getString(R.string.user_avatar_conf_send_photo_message));

        buttonConfirmAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                FirebaseHelper.uploadAvatar(mImageViewUserAvatar.getDrawable(), mUserId)
                        .setUploadImageListener(new FirebaseHelper.UploadImageListener() {
                            @Override
                            public void onSuccess(Uri imageUri) {
                                mProgressDialog.cancel();
                                mUserReference.child("user_avatar").setValue(imageUri.toString());
                                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(String error) {
                                mProgressDialog.cancel();
                            }
                        });
            }
        });

        // Fake déconnexion
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mImageViewUserAvatar.setImageDrawable(Drawable.createFromPath(resultUri.getPath()));
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
