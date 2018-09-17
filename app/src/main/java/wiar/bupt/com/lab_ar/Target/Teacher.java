package wiar.bupt.com.lab_ar.Target;

import java.io.Serializable;

/**
 * Created by pengfeng on 2017/4/11.
 */

public class Teacher implements Serializable {
    private String Text;
    private String[] pictureUrls;
    private String videoUrl;
    private String name;
    private String document;

    public Teacher(String Text, String pictureUrls, String videoUrl, String document) {
        this.name = name;
        this.Text = Text;
        this.videoUrl = videoUrl;
        this.pictureUrls = pictureUrls.split(";");
        this.document = document;
    }

    public String getText() {
        return Text;
    }

    public String[] getPictureUrls() {
        return pictureUrls;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getName() {
        return name;
    }

    public String getDocument(){
        return  document;
    }
}
