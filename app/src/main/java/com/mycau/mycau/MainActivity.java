package com.mycau.mycau;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private SharedPreferences.Editor speditor;
    private String username;
    private String password;
    private Map<String,String> headers;
    private EditText usered;
    private EditText passed;
    private Button sub;
    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp=getSharedPreferences("shadow", Context.MODE_PRIVATE);
        speditor=sp.edit();
        headers=getHeaders();
        username=sp.getString("username","");
        password=sp.getString("password","");
        setContentView(R.layout.activity_main);
        usered = (EditText)findViewById(R.id.username);
        passed = (EditText)findViewById(R.id.password);
        sub = (Button)findViewById(R.id.submit);
        pb = (ProgressBar)findViewById(R.id.progressBar);
        if(!username.equals("")){//无账号
            login();
        }
    }
    private void login(){
        Map<String,String> postdata = new HashMap<>();
        postdata.put("username",username);
        postdata.put("password",password);
        usered.setEnabled(false);
        passed.setEnabled(false);
        sub.setEnabled(false);
        pb.setVisibility(View.VISIBLE);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        String res = myutil.post("https://wep.cau.edu.cn/uc/wap/login/check",postdata,headers);
                        JSONObject resmap;
                        Log.d("loginresult",res);
                        try{
                            resmap = myutil.j2m(res);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(resmap.getInteger("e") != 0){
                                        usered.setEnabled(true);
                                        passed.setEnabled(true);
                                        sub.setEnabled(true);
                                        pb.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(),"登陆失败",Toast.LENGTH_LONG).show();//跳转

                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"登陆成功",Toast.LENGTH_LONG).show();//跳转
                                        Intent intent = new Intent(MainActivity.this,Main.class);
                                        startActivity(intent);
                                        MainActivity.this.finish();
                                    }
                                }
                            });
                        } catch(java.lang.IllegalStateException e){

                        }
                    }
                }
        ).start();
    }

    public void submit(View view){
        username = usered.getText().toString();
        password = passed.getText().toString();
        speditor.putString("username",username);
        speditor.putString("password",password);
        speditor.commit();
        login();

    }

    private Map<String,String> getHeaders(){
        Map headersmap;
        JSONObject headers;
        //headers.put("Host", "wep.cau.edu.cn");
        String headerjson="{\"Host\":\"wep.cau.edu.cn\",\"Origin\":\"https://wep.cau.edu.cn\",\"Connection\":\"keep-alive\",\"Accept\":\"application/json, text/javascript, */*; q=0.01\",\"X-Requested-With\":\"XMLHttpRequest\",\"Accept-Language\":\"zh-cn\",\"Content-Type\":\"application/x-www-form-urlencoded; charset=UTF-8\",\"User-Agent\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 14_0_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.17(0x1700112a) NetType/WIFI Language/zh_CN\"}";

        headers=myutil.j2m(headerjson);
        headersmap = JSONObject.parseObject(headers.toJSONString());
        return headersmap;

    }
}