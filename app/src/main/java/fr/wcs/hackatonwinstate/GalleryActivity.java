package fr.wcs.hackatonwinstate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        ImageView cat1 = findViewById(R.id.cat1);
        ImageView cat2 = findViewById(R.id.cat2);
        ImageView cat3 = findViewById(R.id.cat3);
        ImageView cat4 = findViewById(R.id.cat4);
        ImageView cat5 = findViewById(R.id.cat5);
        ImageView cat6 = findViewById(R.id.cat6);
        ImageView cat7 = findViewById(R.id.cat7);
        ImageView cat8 = findViewById(R.id.cat8);
        ImageView cat9 = findViewById(R.id.cat9);
        ImageView cat10 = findViewById(R.id.cat10);
        ImageView cat11 = findViewById(R.id.cat11);
        ImageView cat12 = findViewById(R.id.cat12);

        cat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, PictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image",R.drawable.cat1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        cat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, PictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image",R.drawable.cat2);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        cat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, PictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image",R.drawable.cat3);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        cat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, PictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image",R.drawable.cat4);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        cat5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, PictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image",R.drawable.cat5);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        cat6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, PictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image",R.drawable.cat6);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        cat7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, PictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image",R.drawable.cat7);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        cat8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, PictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image",R.drawable.cat8);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        cat9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, PictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image",R.drawable.cat9);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        cat10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, PictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image",R.drawable.cat10);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        cat11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, PictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image",R.drawable.cat11);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        cat12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, PictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image",R.drawable.cat12);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
