package at.trafalgar.utils;

import java.net.HttpURLConnection;
import java.net.URL;

public class HTTP {
    public static boolean isWellFormed(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isReachable(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            int code = connection.getResponseCode();
            connection.disconnect();
            return code >= 200 && code < 400;
        } catch (Exception e) {
            return false;
        }
    }
}
