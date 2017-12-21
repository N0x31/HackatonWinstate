package fr.wcs.hackatonwinstate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainPopUpActivity extends AppCompatActivity {

    private DatabaseReference mUserReference;
    private CircleImageView mImageViewUserSmile;
    private ProgressDialog mProgressDialog;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pop_up);

        // PopUp Metrics
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .75));

        // Get UserId
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainPopUpActivity.this);
        mUserId = sharedPreferences.getString("mUserId", mUserId);

        mImageViewUserSmile = findViewById(R.id.imageViewUserSmile);
        Button buttonTakeSmile =  findViewById(R.id.buttonTakeSmile);
        Button buttonConfirmSmile =  findViewById(R.id.buttonConfirmSmile);

        // Crop Image
        buttonTakeSmile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(MainPopUpActivity.this);
            }
        });

        // Init Progress Dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle(getString(R.string.user_avatar_conf_send_photo_title));
        mProgressDialog.setMessage(getString(R.string.user_avatar_conf_send_photo_message));

        // Upload Smile picture
        buttonConfirmSmile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                mUserReference = FirebaseHelper.getDatabase().getReference("User")
                        .child(mUserId);
                FirebaseHelper.uploadSmile(mImageViewUserSmile.getDrawable(), mUserId)
                        .setUploadImageListener(new FirebaseHelper.UploadImageListener() {
                            @Override
                            public void onSuccess(Uri imageUri) {
                                mProgressDialog.cancel();
                                String globalEventPath = String.format("%s/%s/%s/%s", "User", mUserId,
                                        "user_smiles", mUserId);
                                HashMap<String, Object> data = new HashMap<>();
                                data.put(globalEventPath, imageUri.toString());
                                mUserReference = FirebaseHelper.getDatabase().getReference("User").child(mUserId);
                                FirebaseDatabase mDatabase = FirebaseHelper.getDatabase();
                                mDatabase.getReference().updateChildren(data);
                                Intent intent = new Intent(MainPopUpActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(String error) {
                                mProgressDialog.cancel();
                                Toast.makeText(MainPopUpActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
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
                mImageViewUserSmile.setImageDrawable(Drawable.createFromPath(resultUri.getPath()));
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(MainPopUpActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
