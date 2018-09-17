package wiar.bupt.com.lab_ar;

import android.app.Activity;

import android.content.Intent;
import android.content.res.Resources;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.wikitude.WikitudeSDK;
import com.wikitude.WikitudeSDKStartupConfiguration;
import com.wikitude.common.camera.CameraSettings;
import com.wikitude.common.rendering.RenderExtension;
import com.wikitude.common.tracking.RecognizedTarget;
import com.wikitude.rendering.ExternalRendering;
import com.wikitude.tracker.ClientTracker;
import com.wikitude.tracker.ClientTrackerEventListener;
import com.wikitude.tracker.Tracker;


import java.util.ArrayList;
import java.util.List;

import wiar.bupt.com.lab_ar.Target.Teacher;
import wiar.bupt.com.lab_ar.listview.item;
import wiar.bupt.com.lab_ar.listview.itemAdaptor;
import wiar.bupt.com.lab_ar.rendering.CustomSurfaceView;
import wiar.bupt.com.lab_ar.rendering.Driver;
import wiar.bupt.com.lab_ar.rendering.GLRenderer;

import wiar.bupt.com.lab_ar.utils.DensityUtil;
import wiar.bupt.com.lab_ar.utils.HttpConnUtils;
import wiar.bupt.com.lab_ar.utils.JsonParser;
import wiar.bupt.com.lab_ar.utils.MultiBitmap;
import wiar.bupt.com.lab_ar.utils.tabMessage;

import static wiar.bupt.com.lab_ar.R.color.textviewBg;


public class MainActivity extends Activity implements ClientTrackerEventListener, ExternalRendering, GestureDetector.OnGestureListener {
    private static final String TAG = "MainActivity";
    private WikitudeSDK _wikitudeSDK;
    private CustomSurfaceView _view;
    private Driver _driver;
    private GLRenderer _glRenderer;
    private RelativeLayout layout;
    private TextView textView;
    private ScrollView scrollView;
    private VideoView videoView;
    private Teacher teacher = null;
    private GestureDetector detector;
    private ViewFlipper flipper;
    private Animation[] animations = new Animation[4];
    final int FLIP_DISTANCE = 50;
    //List<String> itemList = new ArrayList<String>();
    //ArrayAdapter<CharSequence> adapter;
    private String GROUP_NAME;
    private String ITEM_NAME;
    private itemAdaptor adapter;
    private List<item> itemList = new ArrayList<item>();
    private ViewGroup.LayoutParams para;
    private BottomBar bottomBar;
    private MultiBitmap multiBitmap;
    private Resources res;
    private int mask_c;
    private Drawable mask_d;
    private int orinheight;
    private Button button;
    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_campus_recoder);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        _wikitudeSDK = new WikitudeSDK(this);
        WikitudeSDKStartupConfiguration startupConfiguration = new WikitudeSDKStartupConfiguration(WikitudeSDKConstants.WIKITUDE_SDK_KEY, CameraSettings.CameraPosition.BACK, CameraSettings.CameraFocusMode.CONTINUOUS);
        _wikitudeSDK.onCreate(getApplicationContext(), startupConfiguration);
        ClientTracker tracker = _wikitudeSDK.getTrackerManager().create2dClientTracker("file:///android_asset/tracker2.wtc");
        tracker.registerTrackerEventListener(this);
        init();
    }

    private void init() {
        //listView = (ListView) findViewById(R.id.list_view);
        res = getResources();
        scrollView = (ScrollView) findViewById(R.id.scrollview);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                intent = new Intent(MainActivity.this, video.class);
//                intent.putExtras(bundle);
//                startActivity(intent);
                VideoView vv = new VideoView(MainActivity.this);
                vv.setVideoURI(Uri.parse("http://10.103.93.3:8080/WiBUPTInfo/123.mp4"));
                vv.start();
            }
        });
        //videoView = (VideoView) findViewById(R.id.videoView);

        para = scrollView.getLayoutParams();
        orinheight = para.height;
        textView = (TextView) findViewById(R.id.textView);
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        detector = new GestureDetector(this, this);
        animations[0] = AnimationUtils.loadAnimation(this, R.anim.left_in);
        animations[1] = AnimationUtils.loadAnimation(this, R.anim.left_out);
        animations[2] = AnimationUtils.loadAnimation(this, R.anim.right_in);
        animations[3] = AnimationUtils.loadAnimation(this, R.anim.right_out);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_scan) {
//                    _wikitudeSDK.onResume();
//                    _view.onResume();
                    _driver.start();

                    if (para.height > DensityUtil.dip2px(MainActivity.this, 250)) {
                        para.height = DensityUtil.dip2px(MainActivity.this, 200);
                        scrollView.setLayoutParams(para);
                        mask_d = res.getDrawable(R.drawable.radius_corner);
                        scrollView.setBackgroundDrawable(mask_d);
//                        mask_c = res.getColor(R.color.fontColor_white);
//                        textView.setTextColor(mask_c);
                        flipper.setVisibility(View.GONE);
                        multiBitmap = null;

                        textView.setText("识别...");
                        teacher = null;
                        button.setVisibility(View.GONE);
                        //videoView.setVisibility(View.GONE);
                    }
                } else if (tabId == R.id.tab_document) {
//                    _wikitudeSDK.onPause();
//                    _view.onPause();
                    _driver.stop();
                    if (teacher == null) {
                        Toast.makeText(MainActivity.this, "no target!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (para.height < DensityUtil.dip2px(MainActivity.this, 250)) {
                            para.height = DensityUtil.dip2px(MainActivity.this, 560);
                            scrollView.setLayoutParams(para);

//                            videoView.setVideoURI(Uri.parse("http://10.103.93.3:8080/WiBUPTInfo/123.mp4"));
//                            videoView.start();
//                            videoView.setVisibility(View.VISIBLE);

                            multiBitmap = new MultiBitmap(MainActivity.this, flipper, teacher.getPictureUrls());
                            flipper.setVisibility(View.VISIBLE);
                            textView.setGravity(Gravity.LEFT);
                            textView.setText(Html.fromHtml(teacher.getDocument()));
                            textView.setMovementMethod(LinkMovementMethod.getInstance());
                            mask_c = res.getColor(R.color.textviewBg);
                            scrollView.setBackgroundColor(mask_c);
                            mask_d = res.getDrawable(R.drawable.document_radius_corner);
                            scrollView.setBackgroundDrawable(mask_d);
//                            mask_c = res.getColor(R.color.fontColor_black);
//                            textView.setTextColor(mask_c);


                            if (teacher.getVideoUrl() != "") {
                                bundle = new Bundle();
                                bundle.putSerializable("videourl", teacher.getVideoUrl());
                                button.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > FLIP_DISTANCE) {
            flipper.setInAnimation(animations[0]);
            flipper.setOutAnimation(animations[1]);
            flipper.showPrevious();
            return true;
        } else if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
            flipper.setInAnimation(animations[2]);
            flipper.setOutAnimation(animations[3]);
            flipper.showNext();
            return false;
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    class DataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String s = HttpConnUtils.doGet(params[0]);
            return s;//s是json字符串
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JsonParser jsonPaser = new JsonParser(s);
                teacher = new Teacher(jsonPaser.getText(), jsonPaser.getPictureUrlStr(), jsonPaser.getVedioUrlStr(), jsonPaser.getDocumentStr());
                textView.setGravity(Gravity.LEFT);
                textView.setText(Html.fromHtml(teacher.getText()));
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                //textView.setVisibility(View.VISIBLE);
                // new MultiBitmap(MainActivity.this, flipper, teacher.getPictureUrls());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onResume() {
        super.onResume();
        _wikitudeSDK.onResume();
        _view.onResume();
        _driver.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        _wikitudeSDK.onPause();
        _view.onPause();
        _driver.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _wikitudeSDK.onDestroy();
    }

    @Override
    public void onRenderExtensionCreated(final RenderExtension renderExtension_) {
        _glRenderer = new GLRenderer(renderExtension_);
        _view = new CustomSurfaceView(getApplicationContext(), _glRenderer);
        _driver = new Driver(_view, 30);

        FrameLayout viewHolder = new FrameLayout(getApplicationContext());
        setContentView(viewHolder);

        viewHolder.addView(_view);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        layout = (RelativeLayout) inflater.inflate(R.layout.activity_campus_recoder, null);
        viewHolder.addView(layout);
    }

    @Override
    public void onErrorLoading(final ClientTracker clientTracker_, final String errorMessage_) {
    }

    @Override
    public void onTrackerFinishedLoading(final ClientTracker clientTracker_, final String trackerFilePath_) {
    }

    @Override
    public void onTargetRecognized(final Tracker tracker_, final String targetName_) {
        new DataTask().execute(targetName_);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //ListView listView = (ListView) findViewById(R.id.list_view);
//                String groupName = targetName_.split("_")[0];
//                String itemName = targetName_.split("_")[1];
                //adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.teacher_model2, android.R.layout.);
                //addTeacherToList();
                //adapter = new itemAdaptor(MainActivity.this, R.layout.item, itemList);
                //listView.setVisibility(View.VISIBLE);


                scrollView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addTeacherToList() {
//        itemList.clear();
//        item more = new item("更多教师", R.drawable.teacher);
//        item recruit = new item("招生/调剂信息", R.drawable.recruit);
//        itemList.add(more);itemList.add(recruit);
    }

    @Override
    public void onTracking(final Tracker tracker_, final RecognizedTarget recognizedTarget_) {
        _glRenderer.setCurrentlyRecognizedTarget(recognizedTarget_);
    }

    @Override
    public void onTargetLost(final Tracker tracker_, final String targetName_) {
        _glRenderer.setCurrentlyRecognizedTarget(null);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText("");
                textView.setGravity(Gravity.CENTER);
                scrollView.setVisibility(View.INVISIBLE);
                //flipper.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onExtendedTrackingQualityUpdate(final Tracker tracker_, final String targetName_, final int oldTrackingQuality_, final int newTrackingQuality_) {
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        //Toast.makeText(MainActivity.this, "You clicked "+position, Toast.LENGTH_SHORT).show();
//        String itemName = adapter.getItem(position).getName();//文字标题
//        //textView.setText("parent:" + parent + ",id:" + id + ",position:" + position + ",itemName:" + itemName);
//        Intent intent;
//        switch (itemName) {
//            case "视频":
//                intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse("http://10.103.25.231:8080/WiBUPTInfo/123.mp4"), "video/mp4");
//                startActivity(intent);
//                break;
//            case "更多教师":
//                intent = new Intent(MainActivity.this, TeachersActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("teacher", teacher);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                break;
//            case "招生/调剂信息":
//                intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("http://sice.bupt.edu.cn/rcpy/yjsjy/zsgz.htm"));
//                startActivity(intent);
//                break;
//            default:
//                break;
//        }
//    }

}
