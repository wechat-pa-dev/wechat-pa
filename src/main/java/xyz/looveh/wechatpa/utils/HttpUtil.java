package xyz.looveh.wechatpa.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @Author Administrator
 * @Date 2019/3/29
 */
public class HttpUtil {

    public static String post(String postUrl, String content) {
        try {
            //打开url连接
            URL url = new URL(postUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求属性
//            httpConn.setRequestProperty("Content-Type","application/json");
//            httpConn.setRequestProperty("x-adviewrtb-version", "2.1");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            if (null != content) {
                //防止乱码
                content = URLEncoder.encode(content, "utf-8");
                dos.writeBytes(content);
            }

            dos.flush();
            dos.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),
                    "utf-8"));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            return sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String post(String url) {
        return post(url, null);
    }
}
