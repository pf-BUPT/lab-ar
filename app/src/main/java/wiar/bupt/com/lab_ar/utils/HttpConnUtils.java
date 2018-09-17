package wiar.bupt.com.lab_ar.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pengfeng on 2017/4/5.
 */

public class HttpConnUtils {

    private static HttpURLConnection urlConnection;
    public static String doGet(String targetName){
        //String path = "http://10.103.25.193:8080/WiAR/WiServlet?targetName="+targetName;
        String path="http://10.103.93.3:8080/ServletLoginDemo/servlet/LoginServlet?name="+targetName;
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(path);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }
        return result.toString();
    }
}
