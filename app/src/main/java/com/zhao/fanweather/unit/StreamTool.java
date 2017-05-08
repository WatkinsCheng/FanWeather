package com.zhao.fanweather.unit;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Zhao on 2016/5/6.
 */
public class StreamTool {
    public static String transform(InputStream in) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int len = 0;
        byte[] buf = new byte[1024];
        try {
            while((len=in.read(buf))>0){
                baos.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toString();
    }
}
