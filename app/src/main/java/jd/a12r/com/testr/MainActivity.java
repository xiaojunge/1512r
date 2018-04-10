package jd.a12r.com.testr;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import jd.a12r.com.testr.bean.Category;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDataFromServer();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Category category = (Category) msg.obj;
            Log.e("myMessage","== ="+category.getData().get(0).getName());
        }
    };

    public void getDataFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://www.zhaoapi.cn/product/getCatagory");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = inputStream.read(buffer))!=-1){
                        outputStream.write(buffer,0,len);
                    }
                    String s = outputStream.toString();
                    Gson gson = new Gson();
                    Category category = gson.fromJson(s, Category.class);

                    Message message = handler.obtainMessage();
                    message.obj = category;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }
}
