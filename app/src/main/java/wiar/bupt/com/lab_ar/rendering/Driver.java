package wiar.bupt.com.lab_ar.rendering;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pengfeng on 2017/3/29.
 */

public class Driver {
    private final CustomSurfaceView _customSurfaceView;
    private final int _fps;
    private Timer _renderTimer = null;


    public Driver(final CustomSurfaceView customSurfaceView_, int fps_) {
        _customSurfaceView = customSurfaceView_;
        _fps = fps_;

    }

    public void start() {
        if (_renderTimer != null) {
            _renderTimer.cancel();
        }

        _renderTimer = new Timer();
        _renderTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                _customSurfaceView.requestRender();
            }
        }, 0, 1000 / _fps);
    }

    public void stop() {
        _renderTimer.cancel();
        _renderTimer = null;
    }
}
