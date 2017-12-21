package fr.wcs.hackatonwinstate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PhotoActivity extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public String postUrl = "https://api-face.sightcorp.com/api/detect/";
    public String postBody = "{'app_key':'d6b95f4eaac74193837bacbfbc194021',"
            + "'url':'http://cache.cosmopolitan.fr/data/photo/w1000_ci/4z/femme-sourire-rouges-levres.jpg'}";
    public String mJSONResponse;

    private DatabaseReference mUserReference;
    private CircleImageView mImageViewUserSmile;
    private ProgressDialog mProgressDialog;
    private String mUserId;
    private String mImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        // PopUp Metrics
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .75));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PhotoActivity.this);
        mUserId = sharedPreferences.getString("mUserId", mUserId);

        mImageViewUserSmile = findViewById(R.id.imageViewUserSmile);
        Button buttonTakeSmile = findViewById(R.id.buttonTakeSmile);
        Button buttonConfirmSmile = findViewById(R.id.buttonConfirmSmile);

        // Crop Image
        buttonTakeSmile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(PhotoActivity.this);
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
                                mImageUri = imageUri.toString();
                                mProgressDialog.cancel();
                                String globalEventPath = String.format("%s/%s/%s/%s", "User", mUserId,
                                        "user_smiles", mUserId);
                                try {
                                    postRequest(postUrl, postBody);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {

                                        if (mJSONResponse != null) {
                                            try {
                                                JSONObject jsonObj = new JSONObject(mJSONResponse);

                                                // Getting JSON Array node
                                                JSONArray people = jsonObj.getJSONArray("people");

                                                // looping through All Contacts
                                                for (int i = 0; i < people.length(); i++) {
                                                    JSONObject p = people.getJSONObject(i);
                                                    JSONObject emotions = p.getJSONObject("emotions");
                                                    String happiness = emotions.getString("happiness");
                                                    Log.d("TAG2", happiness);
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {

                                        }
                                    }
                                }, 3000);
                                HashMap<String, Object> data = new HashMap<>();
                                data.put(globalEventPath, imageUri.toString());
                                mUserReference = FirebaseHelper.getDatabase().getReference("User").child(mUserId);
                                FirebaseDatabase mDatabase = FirebaseHelper.getDatabase();
                                mDatabase.getReference().updateChildren(data);
                                Intent intent = new Intent(PhotoActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(String error) {
                                mProgressDialog.cancel();
                                Toast.makeText(PhotoActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

       /* try {
            postRequest(postUrl, postBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

        if (mJSONResponse != null) {
            try {
                JSONObject jsonObj = new JSONObject(mJSONResponse);

                // Getting JSON Array node
                JSONArray people = jsonObj.getJSONArray("people");

                // looping through All Contacts
                for (int i = 0; i < people.length(); i++) {
                    JSONObject p = people.getJSONObject(i);
                    JSONObject emotions = p.getJSONObject("emotions");
                    String happiness = emotions.getString("happiness");
                    Log.d("TAG2", happiness);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {

        }
            }
        }, 3000);
    }*/

    void postRequest(String postUrl, String postBody) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("app_key", "d6b95f4eaac74193837bacbfbc194021")
                //   .addFormDataPart("url", "http://cache.cosmopolitan.fr/data/photo/w1000_ci/4z/femme-sourire-rouges-levres.jpg")
                .addFormDataPart("url", mImageUri)
                .build();

        Request request = new Request.Builder()
                .url(postUrl)
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mJSONResponse = response.body().string();
                Log.d("TAG", mJSONResponse);
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
                Toast.makeText(PhotoActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}