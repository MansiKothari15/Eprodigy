package com.bacancy.eprodigy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.VideoView;

import com.bacancy.eprodigy.R;

public class VideoActivity extends BaseActivity {
    String videoUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            videoUrl = intent.getString("videoPath");
        }

        VideoView videoView = (VideoView) findViewById(R.id.VideoView);
        //MediaController mediaController = new MediaController(this);
        // mediaController.setAnchorView(videoView);
        //videoView.setMediaController(mediaController);

        if (!TextUtils.isEmpty(videoUrl)) {
            Log.e("ad","videoPath="+videoUrl);
            videoView.setVideoPath(videoUrl);

            videoView.start();
        }
    }

}
