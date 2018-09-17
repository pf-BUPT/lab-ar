package wiar.bupt.com.lab_ar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import wiar.bupt.com.lab_ar.Target.Teacher;
import wiar.bupt.com.lab_ar.listview.item;
import wiar.bupt.com.lab_ar.listview.itemAdaptor;
import wiar.bupt.com.lab_ar.utils.HttpConnUtils;
import wiar.bupt.com.lab_ar.utils.JsonParser;
import wiar.bupt.com.lab_ar.utils.MultiBitmap;

/**
 * Created by pengfeng on 2017/4/11.
 */

public class TeachersActivity extends Activity implements GestureDetector.OnGestureListener, AdapterView.OnItemClickListener {
    private TextView textView;
    private ListView listView;
    private ViewFlipper flipper;
    private GestureDetector detector;
    private Animation[] animations = new Animation[4];
    final int FLIP_DISTANCE = 50;
    //private ArrayAdapter<CharSequence> adapter;
    private String[] strings;
    private String[] nullstrings = null;
    private Teacher teacher = null;
    private MultiBitmap multiBitmap;
    private itemAdaptor adapter;
    private List<item> itemList = new ArrayList<item>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers);
        Intent intent = this.getIntent();
        teacher = (Teacher) intent.getSerializableExtra("teacher");
        //strings = teacher.getPictureUrls();
        init();
    }

    private void init() {
        listView = (ListView) findViewById(R.id.list_view_teachers);
        flipper = (ViewFlipper) this.findViewById(R.id.flipper_teachers);
        textView = (TextView) findViewById(R.id.textView_teachers);
        detector = new GestureDetector(this, this);
        animations[0] = AnimationUtils.loadAnimation(this, R.anim.left_in);
        animations[1] = AnimationUtils.loadAnimation(this, R.anim.left_out);
        animations[2] = AnimationUtils.loadAnimation(this, R.anim.right_in);
        animations[3] = AnimationUtils.loadAnimation(this, R.anim.right_out);

        //adapter = ArrayAdapter.createFromResource(TeachersActivity.this, R.array.allteacher, android.R.layout.simple_list_item_1);
        addTeacherToList();
        adapter = new itemAdaptor(TeachersActivity.this, R.layout.item, itemList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(TeachersActivity.this);
        // new MultiBitmap(TeachersActivity.this, flipper,teacher.getPictureUrls());
        if (teacher == null) {
            textView.setText("null");
        } else {
            textView.setText(Html.fromHtml(teacher.getText()));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            multiBitmap = new MultiBitmap(TeachersActivity.this, flipper, teacher.getPictureUrls());
        }

    }

    private void addTeacherToList() {
        itemList.clear();
        item sgc = new item("寿国础", R.drawable.teacher_sgc);
        item hyh = new item("胡怡红", R.drawable.teacher_hyh);
        item gzg = new item("郭志刚", R.drawable.teacher_gzg);
        item gzh = new item("高泽华", R.drawable.teacher_gzh);
        item sws = new item("孙文生", R.drawable.teacher_sws);
        item lyq = new item("刘雅琼", R.drawable.teacher_lyq);
        itemList.add(sgc);itemList.add(hyh);itemList.add(gzg);itemList.add(sws);itemList.add(gzh);itemList.add(lyq);
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
                teacher = new Teacher(jsonPaser.getText(), jsonPaser.getPictureUrlStr(), jsonPaser.getVedioUrlStr(),jsonPaser.getDocumentStr());
                textView.setText(Html.fromHtml(teacher.getText()));
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                new MultiBitmap(TeachersActivity.this, flipper, teacher.getPictureUrls());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        item currentItem = adapter.getItem(position);//文字标题
        String itemName = currentItem.getName();
        //textView.setText("parent:"+parent+",id:" + id + ",position:" + position + ",itemName:" + itemName);
        String targetName = "";
        switch (itemName) {
            case "寿国础":
                targetName = "teacher_sgc";
                break;
            case "郭志刚":
                targetName = "teacher_gzg";
                break;
            case "胡怡红":
                targetName = "teacher_hyh";
                break;
            case "孙文生":
                targetName = "teacher_sws";
                break;
            case "高泽华":
                targetName = "teacher_gzh";
                break;
            case "刘雅琼":
                targetName = "teacher_lyq";
                break;
        }
        new TeachersActivity.DataTask().execute(targetName);


    }
}
