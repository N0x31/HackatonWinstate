package fr.wcs.hackatonwinstate;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
    public String postUrl= "https://api-face.sightcorp.com/api/detect/";
    public String postBody= "{'app_key':'d6b95f4eaac74193837bacbfbc194021',"
            + "'url':'http://cache.cosmopolitan.fr/data/photo/w1000_ci/4z/femme-sourire-rouges-levres.jpg'}";
    public String mJSONResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

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
        }
        else {

        }
            }
        }, 3000);
    }

    void postRequest(String postUrl,String postBody) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("app_key", "d6b95f4eaac74193837bacbfbc194021")
                .addFormDataPart("url", "http://cache.cosmopolitan.fr/data/photo/w1000_ci/4z/femme-sourire-rouges-levres.jpg")
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
}