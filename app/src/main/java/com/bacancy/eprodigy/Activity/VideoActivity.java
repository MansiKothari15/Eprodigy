package com.bacancy.eprodigy.Activity;

import android.os.Bundle;
import android.widget.VideoView;

import com.bacancy.eprodigy.R;

public class VideoActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        VideoView videoView = (VideoView)findViewById(R.id.VideoView);
        //MediaController mediaController = new MediaController(this);
        // mediaController.setAnchorView(videoView);
        //videoView.setMediaController(mediaController);

        videoView.setVideoPath("");

        videoView.start();
    }

}
