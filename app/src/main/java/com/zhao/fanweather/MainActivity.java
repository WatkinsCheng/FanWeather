package com.zhao.fanweather;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhao.fanweather.domain.Weather;
import com.zhao.fanweather.domain.WeatherInfo;
import com.zhao.fanweather.unit.StreamTool;
import com.zhao.fanweather.unit.WeekTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final static int SUCCESS = 1;
    private final static int FAIL = 0;
    private EditText et_city;
    private Button bt_inquiry;

    private TextView tv_now;

    private TextView tv_aqi;

    private TextView tv_city;

    private TextView tv_feng;
    private TextView tv_note;

    private TextView tv_type0;
    private TextView tv_type1;
    private TextView tv_type2;
    private TextView tv_type3;
    private TextView tv_type4;

    private TextView tv_date2;
    private TextView tv_date3;
    private TextView tv_date4;

    private TextView tv_max0;
    private TextView tv_max1;
    private TextView tv_max2;
    private TextView tv_max3;
    private TextView tv_max4;

    private TextView tv_min0;
    private TextView tv_min1;
    private TextView tv_min2;
    private TextView tv_min3;
    private TextView tv_min4;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    WeatherInfo info = (WeatherInfo) msg.obj;


                    updateUI(info);
                    break;
                case FAIL:
                    String str = (String) msg.obj;
                    Toast.makeText(MainActivity.this,str , Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /**
     * {
     "desc": "OK",
     "status": 1000,
     "data": {
     "wendu": "25",
     "ganmao": "天气转凉，空气湿度较大，较易发生感冒，体质较弱的朋友请注意适当防护。",
     "forecast": [
     {
     "fengxiang": "南风",
     "fengli": "3-4级",
     "high": "高温 28℃",
     "type": "阵雨",
     "low": "低温 22℃",
     "date": "5日星期四"
     },
     {
     "fengxiang": "南风",
     "fengli": "微风级",
     "high": "高温 28℃",
     "type": "阵雨",
     "low": "低温 22℃",
     "date": "6日星期五"
     },
     {
     "fengxiang": "南风",
     "fengli": "微风级",
     "high": "高温 25℃",
     "type": "中雨",
     "low": "低温 17℃",
     "date": "7日星期六"
     },
     {
     "fengxiang": "北风",
     "fengli": "微风级",
     "high": "高温 20℃",
     "type": "小雨",
     "low": "低温 16℃",
     "date": "8日星期天"
     },
     {
     "fengxiang": "北风",
     "fengli": "微风级",
     "high": "高温 18℃",
     "type": "中雨",
     "low": "低温 15℃",
     "date": "9日星期一"
     }
     ],
     "yesterday": {
     "fl": "微风",
     "fx": "南风",
     "high": "高温 30℃",
     "type": "阵雨",
     "low": "低温 20℃",
     "date": "4日星期三"
     },
     "aqi": "57",
     "city": "长沙"
     }
     }

     */
    private void init() {
        tv_aqi = (TextView) findViewById(R.id.tv_aqi);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_now = (TextView) findViewById(R.id.tv_now_0);
        tv_feng   = (TextView)findViewById(R.id.tv_feng);
        tv_date2 = (TextView) findViewById(R.id.tv_date2);
        tv_date3 = (TextView) findViewById(R.id.tv_date3);
        tv_date4 = (TextView) findViewById(R.id.tv_date4);
        tv_max0 = (TextView) findViewById(R.id.tv_max_0);
        tv_max1 = (TextView) findViewById(R.id.tv_max_1);
        tv_max2 = (TextView) findViewById(R.id.tv_max_2);
        tv_max3 = (TextView) findViewById(R.id.tv_max_3);
        tv_max4 = (TextView) findViewById(R.id.tv_max_4);
        tv_min0 = (TextView) findViewById(R.id.tv_min_0);
        tv_min1 = (TextView) findViewById(R.id.tv_min_1);
        tv_min2 = (TextView) findViewById(R.id.tv_min_2);
        tv_min3 = (TextView) findViewById(R.id.tv_min_3);
        tv_min4 = (TextView) findViewById(R.id.tv_min_4);
        tv_type0 = (TextView) findViewById(R.id.tv_type_0);
        tv_type1 = (TextView) findViewById(R.id.tv_type_1);
        tv_type2 = (TextView) findViewById(R.id.tv_type_2);
        tv_type3 = (TextView) findViewById(R.id.tv_type_3);
        tv_type4 = (TextView) findViewById(R.id.tv_type_4);
        tv_note = (TextView) findViewById(R.id.tv_note_0);
        et_city = (EditText) findViewById(R.id.et_city);
        bt_inquiry = (Button) findViewById(R.id.bt_inquiry);
        bt_inquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String city = et_city.getText().toString();
                        WeatherInfo info = new WeatherInfo(city);
                        String sUrl = getResources().getString(R.string.api)+city;
                        Log.d("TAG_APP", sUrl);
                        try {
                            URL url = new URL(sUrl);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(5000);
                            conn.setRequestMethod("GET");
                            int code = conn.getResponseCode();
                            if (code != 200){
                                fail("获取数据失败");
                                return;
                            }
                            InputStream in = conn.getInputStream();
                            String content = StreamTool.transform(in);
                            JSONObject jsonObject = new JSONObject(content);
                            if (!"OK".equals(jsonObject.get("desc"))){
                                fail("获取数据失败");
                                return;
                            }
                            jsonObject = jsonObject.optJSONObject("data");
                            String wendu = jsonObject.optString("wendu");
                            String note = jsonObject.optString("ganmao");
                            String aqi = jsonObject.optString("aqi");
                            info.setWendu(wendu);
                            if (!"".equals(aqi)){
                                info.setAqi(Integer.parseInt(aqi));
                            }
                            info.setNote(note);
                            JSONArray jsonArray = jsonObject.optJSONArray("forecast");
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject obj = jsonArray.optJSONObject(i);
                                Weather weather = new Weather();
                                weather.fengxiang = obj.optString("fengxiang");
                                weather.fengli = obj.optString("fengli");
                                weather.date = obj.optString("date");
                                weather.heigh = obj.optString("high");
                                weather.low = obj.optString("low");
                                weather.type = obj.optString("type");
                                info.addWeather(weather);
                            }
                            Message msg = Message.obtain();
                            msg.what=SUCCESS;
                            msg.obj=info;
                            handler.sendMessage(msg);
                            Log.d("TAG_APP","");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });

    }

    private void fail(String str) {
        Message msg = Message.obtain();
        msg.what = FAIL;
        msg.obj = str;
        handler.sendMessage(msg);
    }

    private void updateUI(WeatherInfo info) {
        tv_city.setText(info.getCity()+"");
        String now =info.getWendu()+"";
        tv_now.setText(now);
        tv_note.setText(info.getNote()+"");
        tv_aqi.setText(info.getAqi()+"");


        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int week = cal.get(Calendar.DAY_OF_WEEK);
        setData(info.get(0),tv_max0,tv_min0,tv_type0,null);
        setData(info.get(1),tv_max1,tv_min1,tv_type1,null);
        setData(info.get(2),tv_max2,tv_min2,tv_type2,tv_date2);
        setData(info.get(3),tv_max3,tv_min3,tv_type3,tv_date3);
        setData(info.get(4),tv_max4,tv_min4,tv_type4,tv_date4);

    }

    protected void setData(Weather weather, TextView r_max, TextView r_min, TextView r_type, TextView r_date) {
        r_max.setText(weather.heigh);
        r_min.setText(weather.low);
        r_type.setText(weather.type);
        if (r_date!=null)
            r_date.setText(WeekTool.transform(weather.date));
    }

}
