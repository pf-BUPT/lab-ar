package wiar.bupt.com.lab_ar.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by pengfeng on 2017/4/5.
 */

public class JsonParser {

    private String text;
    private String pictureUrlStr;
    private String vedioUrlStr;
    private String jsonString;
    private String documentStr;

    public JsonParser(String s) throws JSONException {
        JSONObject jsonObject = new JSONObject(s);

        text = jsonObject.getString("text");
        pictureUrlStr = jsonObject.getString("picture");
        vedioUrlStr = jsonObject.getString("video");
        jsonString = jsonObject.toString();
        documentStr = jsonObject.getString("document");
    }


    public String getText() {
        return text;
    }

    public String getPictureUrlStr() {
        return pictureUrlStr;
    }

    public String getVedioUrlStr() {
        return vedioUrlStr;
    }

    public String getJsonString() {
        return jsonString;
    }

    public String getDocumentStr(){
        return documentStr;
    }
}