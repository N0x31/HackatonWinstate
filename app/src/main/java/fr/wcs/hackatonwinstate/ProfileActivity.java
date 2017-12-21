package fr.wcs.hackatonwinstate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference mUserReference;
    private CircleImageView mImageViewUserAvatar;
    private ProgressDialog mProgressDialog;
    private String mUserId, mUserAvatar;
    private Object mUserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mUserId = sharedPreferences.getString("mUserId", mUserId);
        mUserReference = FirebaseHelper.getDatabase().getReference("User").child(mUserId);

        mImageViewUserAvatar = findViewById(R.id.imageViewUserAvatar);
        Button buttonTakeAvatar =  findViewById(R.id.buttonTakeAvatar);
        Button buttonConfirmAvatar =  findViewById(R.id.buttonConfirmAvatar);
        TextView textViewUserName = findViewById(R.id.textViewUserName);
        TextView textViewUserWinNumber = findViewById(R.id.textViewUserWinNumber);
        TextView textViewUserLevelNumber = findViewById(R.id.textViewUserLevelNumber);
        TextView textViewUserLevelName = findViewById(R.id.textViewUserLevelName);
        Button buttonSignOut = findViewById(R.id.buttonSignOut);

//        StorageReference storageRef = FirebaseHelper.getsStorage().getReference("Avatars").child(mUserId);
//        DatabaseReference databaseReference = FirebaseHelper.getDatabase().getReference("User").child("user_avatar");
//        Glide.with(ProfileActivity.this)
//                .load(databaseReference)
//                .into(mImageViewUserAvatar);

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
