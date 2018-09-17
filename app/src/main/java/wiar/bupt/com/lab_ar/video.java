package wiar.bupt.com.lab_ar;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

/**
 * Created by pengfeng on 2017/5/4.
 */

public class video extends Activity {
    VideoView videoView;
    //MediaController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);
        Button btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView = (VideoView) findViewById(R.id.videoView2);
                //mController = new MediaController(this);
                Uri uri = Uri.parse("http://10.103.93.3:8080/WiBUPTInfo/123.mp4");
                //videoView.setMediaController(mController);
                videoView.setVideoURI(uri);
                //videoView.requestFocus();
                videoView.start();
            }
        });
    }
}
