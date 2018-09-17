package wiar.bupt.com.lab_ar.rendering;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by pengfeng on 2017/3/29.
 */

public class CustomSurfaceView extends GLSurfaceView {
    public static final String TAG = "WTGLSurfaceView";
    private GLRenderer _renderer;

    public CustomSurfaceView(Context context) {
        super(context);
    }


    public CustomSurfaceView(final Context context, final GLRenderer renderer_) {
        this(context, renderer_, null);
    }


    public CustomSurfaceView(final Context context, final GLRenderer renderer_, final AttributeSet attrs) {
        super(context, attrs);

        if (CustomSurfaceView.this.getContext() == null || CustomSurfaceView.this.getContext() instanceof Activity && ((Activity) CustomSurfaceView.this.getContext()).isFinishing()) {
            return;
        }

        _renderer = renderer_;
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        setRenderer(_renderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);

        getHolder().setFormat(PixelFormat.TRANSLUCENT);

    }

    @Override
    public void onPause() {
        super.onPause();
        _renderer.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        _renderer.onResume();
    }
}
