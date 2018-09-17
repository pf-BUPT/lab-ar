package wiar.bupt.com.lab_ar.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wiar.bupt.com.lab_ar.R;
import wiar.bupt.com.lab_ar.listview.item;

/**
 * Created by pengfeng on 2017/3/30.
 */

public class itemAdaptor extends ArrayAdapter<item> {

    private int resourceId;

    public itemAdaptor(Context context, int textViewResourceId, List<item> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        item it = getItem(position); // 获取当前项的Fruit实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.Image = (ImageView) view.findViewById(R.id.image);
            viewHolder.Name = (TextView) view.findViewById(R.id.name);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        //，如果 convertView 为空，则使 用 LayoutInflater去加载布局，如果不为空则直接对 convertView 进行重用。
        // 这样就大大 提高了 ListView 的运行效率，在快速滚动的时候也可以表现出更好的性能
        //借助一个 ViewHolder,避免每次在getView()方法中还是会调用View的findViewById()方法来获取一次控件的实 例
        viewHolder.Image.setImageResource(it.getImageId());
        viewHolder.Name.setText(it.getName());
        return view;
    }

    //重写了 getView()方法，这个方法在每个子项被滚动到屏幕内的 时候会被调用
    //首先通过 getItem()方法得到当前项的 Fruit实例，然 后使用 LayoutInflater 来为这个子项加载我们传入的布局，
    // 接着调用 View 的 findViewById()方法分别获取到 ImageView 和 TextView 的实例，并分别调用它们的
    // setImageResource()和 setText()方法来设置显示的图片和文字，最后将布局返回，这样 我们自定义的适配器就完成了
    //getView()方法中还有一个 convertView 参数，这个参数用于将之前加载 好的布局进行缓存，以便之后可以进行重用
    class ViewHolder {
        //这是一个内部类
        ImageView Image;
        TextView Name;
    }
}
