package wiar.bupt.com.lab_ar.listview;

/**
 * Created by pengfeng on 2017/3/30.
 */

public class item {
    private String name;

    private int imageId;

    public item(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
