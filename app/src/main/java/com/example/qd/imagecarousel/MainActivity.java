package com.example.qd.imagecarousel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qd.imagecarousel.bannder.ImageCycleViewAppAdsFindSellerNew;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageCycleViewAppAdsFindSellerNew imageSeller;//广告轮播
    private List<String> urlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageSeller = findViewById(R.id.imageSeller);
        urlList = new ArrayList<>();
        urlList.add("http://static.gamemm.com/images/toplist/game_dota11_id.png");
        urlList.add("http://static.gamemm.com/images/toplist/game_dota2_id.png");
        urlList.add("http://static.gamemm.com/images/toplist/game_battlefield_id.png");
        urlList.add("http://static.gamemm.com/images/toplist/game_personality_id.png");
        setImage();
    }

    private void setImage() {
        //urlList为图片列表。
        imageSeller.setImageResources(urlList, new ImageCycleViewAppAdsFindSellerNew.ImageCycleViewListener() {
            @Override
            public void displayImage(String imageURL, final ImageView imageView) {
                try {
                    //加载错误图片
                    Picasso.with(MainActivity.this).load(imageURL)
                            .error(R.drawable.ic_launcher_background).into(imageView);
                } catch (Exception e) {
                }
            }

            @Override
            public void onImageClick(int position, View imageView) {
                //点击事件
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
