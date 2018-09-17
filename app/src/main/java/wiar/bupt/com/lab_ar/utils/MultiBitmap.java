package wiar.bupt.com.lab_ar.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by pengfeng on 2017/4/10.
 */

public class MultiBitmap {
    ViewFlipper flipper;
    GestureDetector detector;
    Animation[] animations = new Animation[4];
    final int FLIP_DISTANCE = 50;
    Bitmap bitmap;
    Context context;
    public String[] strings;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                flipper.addView(addImageView(bitmap));

            }
        }
    };

    public MultiBitmap(Context context, ViewFlipper flipper, String[] strings) {
        flipper.removeAllViews();
        this.context = context;
        this.flipper = flipper;
        this.strings = strings;
        showIt();
    }

    private void showIt() {
        new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < strings.length; i++) {
                        URL url = new URL(strings[i]);
                        InputStream is = url.openStream();
                        bitmap = BitmapFactory.decodeStream(is);
                        handler.sendEmptyMessage(0x123);
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }

    private View addImageView(Bitmap bitmap) {
        ImageView imageView = new ImageView(context);
        //imageView.layout(10, 200, 210, 310);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(700, 800));
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setScaleType(ImageView.ScaleType.CENTER);
        return imageView;
    }
}
