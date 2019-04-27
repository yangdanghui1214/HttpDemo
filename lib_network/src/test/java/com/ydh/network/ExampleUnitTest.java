package com.ydh.network;

import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getHttp() {
        System.out.println(isHost("http://192.168.0.138:8099"));
    }

    @Test
    public void getHttpS() {

        boolean status = false;
        try {
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress("192.168.0.63", 8003);
            socket.connect(socketAddress, 3000);
            socket.close();
            status = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(status);
    }


    /**
     * @param mainHttp 主机地址
     * @return 主机地址是否可用
     */
    private static boolean isHost(String mainHttp) {
        boolean urlExists = false;
        try {
            if (isHostIP(mainHttp)) {
                //校验IP是否有效
                String[] str = mainHttp.split("://")[1].split(":");
                int port = Integer.parseInt(str[1]);
                String ip = str[0];

                Socket socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress(ip, port);
                socket.connect(socketAddress, 3000);
                socket.close();
                urlExists = true;
            } else {
                //设置此类是否应该自动执行 HTTP 重定向（响应代码为 3xx 的请求）。
                HttpURLConnection.setFollowRedirects(false);
                //到 URL 所引用的远程对象的连接
                HttpURLConnection con = (HttpURLConnection) new URL(mainHttp).openConnection();
                /* 设置 URL 请求的方法， GET POST HEAD OPTIONS PUT DELETE TRACE 以上方法之一是合法的，具体取决于协议的限制。*/
                con.setRequestMethod("GET");
                //从 HTTP 响应消息获取状态码
                urlExists = con.getResponseCode() == HttpURLConnection.HTTP_OK;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlExists;
    }


    /**
     * @param mainHttp 主机地址
     * @return 主机地址是否为IP地址
     */
    private static boolean isHostIP(String mainHttp) {
        boolean urlExists = false;
        if (mainHttp.contains(":") && mainHttp.contains("://")) {
            try {
                String[] str = mainHttp.split("://");
                String[] str1 = str[1].split(":");
                String ip = str1[0];
                urlExists = isIP(ip);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return urlExists;
    }

    /**
     * 校验ip格式是否正确
     *
     * @param addr ip
     * @return
     */
    private static boolean isIP(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);

        boolean ipAddress = mat.find();

        //============对之前的ip判断的bug在进行判断
        if (ipAddress) {
            String ips[] = addr.split("\\.");

            if (ips.length == 4) {
                try {
                    for (String ip : ips) {
                        if (Integer.parseInt(ip) < 0 || Integer.parseInt(ip) > 255) {
                            return false;
                        }
                    }
                } catch (Exception e) {
                    return false;
                }
                return true;
            } else {
                return false;
            }
        }

        return ipAddress;
    }
}