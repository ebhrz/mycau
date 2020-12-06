package com.mycau.mycau;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends AppCompatActivity {

    private SharedPreferences sp;
    private SharedPreferences.Editor speditor;
    private String username;
    private String password;

    private EditText phoneet;
    private EditText roomet;
    private EditText temperet;
    private EditText addresset;
    private EditText thinget;
    private EditText outtimeet;
    private EditText intimeet;
    private EditText transet;
    private Spinner transsp;
    private Spinner departsp;
    private Spinner partsp;
    private int depart;
    private int part;
    private int trans;

    private String phone;
    private String room;
    private String temper;
    private String address;
    private String thing;
    private String outtime;
    private String intime;
    private String transdetail;

    private long ts;
    private String date;
    private String dura;

    private ProgressDialog pd;

    private static String[] departmap = {
            "[{\"name\":\"东校区1号公寓（公主楼）A座\",\"value\":\"1\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"东校区1号公寓（公主楼）B座\",\"value\":\"2\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"东校区2号公寓（南楼，女生入口）\",\"value\":\"3\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"东校区2号公寓（北楼，男生入口）\",\"value\":\"4\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"东校区3号公寓\",\"value\":\"5\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"东校区5号公寓（五四楼）\",\"value\":\"6\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"东校区8号公寓（小红楼）\",\"value\":\"7\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"西校区1号楼\",\"value\":\"8\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"西校区2号楼\",\"value\":\"9\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"西校区3号楼\",\"value\":\"10\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"西校区4号楼\",\"value\":\"11\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"西校区5号楼\",\"value\":\"12\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"西校区6号楼\",\"value\":\"13\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"西校区7号楼\",\"value\":\"14\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"西校区8号楼\",\"value\":\"15\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"西校区10号楼\",\"value\":\"16\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"西校区11号楼\",\"value\":\"17\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"西校区12号楼\",\"value\":\"18\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"西校区13号楼\",\"value\":\"19\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"其他\",\"value\":\"20\",\"default\":0,\"imgdata\":\"\"}]"
    };

    private static String[] partmap = {
            "[{\"name\":\"东校区\",\"value\":\"1\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"西校区\",\"value\":\"2\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"全选\",\"value\":\"3\",\"default\":0,\"imgdata\":\"\"}]"
    };

    private static String[] transmap = {
            "[{\"name\":\"步行\",\"value\":\"1\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"自行车\",\"value\":\"2\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"公交车/地铁\",\"value\":\"3\",\"default\":0,\"imgdata\":\"\"}]",
            "[{\"name\":\"私家车/出租车\",\"value\":\"4\",\"default\":0,\"imgdata\":\"\"}]"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        sp=getSharedPreferences("shadow", Context.MODE_PRIVATE);
        speditor=sp.edit();
        username=sp.getString("username","");
        password=sp.getString("password","");

        phone = sp.getString("phone","");
        room = sp.getString("room","");
        temper = sp.getString("temper","36.5");
        address = sp.getString("address","");
        thing  = sp.getString("thing","");
        depart = sp.getInt("depart",0);
        part = sp.getInt("part",0);
        trans = sp.getInt("trans",0);
        transdetail = sp.getString("transdetail","无");

        ts = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        SimpleDateFormat formatter_display = new SimpleDateFormat("HH:mm:ss");
        Date datenow = new Date(ts);
        intime = formatter.format(datenow);
        date = intime.substring(0,10);
        ts = ts - 3600000*2;
        Date datebefore = new Date(ts);
        outtime = formatter.format(datebefore);

        phoneet = (EditText) findViewById(R.id.phone);
        phoneet.setText(phone);
        roomet = (EditText) findViewById(R.id.room);
        roomet.setText(room);
        temperet = (EditText) findViewById(R.id.temper);
        temperet.setText(temper);
        addresset = (EditText) findViewById(R.id.address);
        addresset.setText(address);
        thinget = (EditText)findViewById(R.id.thing);
        thinget.setText(thing);
        intimeet = (EditText)findViewById(R.id.intime);
        intimeet.setText(formatter_display.format(datenow));
        outtimeet = (EditText)findViewById(R.id.outtime);
        outtimeet.setText(formatter_display.format(datebefore));
        transet = (EditText)findViewById(R.id.transdetail);
        transet.setText(transdetail);
        departsp = (Spinner)findViewById(R.id.depart);
        partsp = (Spinner)findViewById(R.id.part);
        transsp = (Spinner)findViewById(R.id.trans);
        departsp.setSelection(depart);
        partsp.setSelection(part);
        transsp.setSelection(trans);



        //getlt();
    }

    public void outtimepick(View view){
        EditText timeet = (EditText)view;
        String thistime = timeet.getText().toString();
        TimePickerDialog pickerDialog = new TimePickerDialog(Main.this, (TimePickerDialog.OnTimeSetListener) (view1, hourOfDay, minute) -> {
            timeet.setText(hourOfDay + ":" + minute + ":00");
            ;
        }, Integer.parseInt(thistime.substring(0,2)), Integer.parseInt(thistime.substring(3,5)), true);

        pickerDialog.show();
    }

    public void intimepick(View view){
        EditText timeet = (EditText)view;
        String thistime = timeet.getText().toString();
        TimePickerDialog pickerDialog = new TimePickerDialog(Main.this, (TimePickerDialog.OnTimeSetListener) (view1, hourOfDay, minute) -> {
            timeet.setText(hourOfDay + ":" + minute + ":00");
            ;
        }, Integer.parseInt(thistime.substring(0,2)), Integer.parseInt(thistime.substring(3,5)), true);

        pickerDialog.show();
    }

    private void makesubmit(){
        Map header = new HashMap();
        header.put("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 14_0_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.17(0x1700112a) NetType/WIFI Language/zh_CN");
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        pd = new ProgressDialog(Main.this);
                        pd.setIndeterminate(false);
                        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        pd.setMessage("提交中...");
                        pd.setCancelable(false);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.show();
                            }
                        });

                        Map resmap;
                        JSONObject data;
                        int currform;
                        String res = myutil.get("http://onecas.cau.edu.cn/tpass/login",header);
                        String pattern = "name=\"lt\" value=\"(.*?)\"";
                        Pattern r = Pattern.compile(pattern);
                        Matcher m = r.matcher(res);
                        String logindes="";
                        String execution="";
                        if(!m.find()){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"请检查网络",Toast.LENGTH_LONG).show();
                                }
                            });
                            return;
                        }
                        //Log.d("loginresult",m.group());
                        String lt = m.group(1);
                        logindes=getdes(username,password,lt);
                        pattern = "name=\"execution\" value=\"(.*?)\"";
                        r = Pattern.compile(pattern);
                        m = r.matcher(res);
                        m.find();
                        execution=m.group(1);

                        Map st=new HashMap();
                        st.put("rsa",logindes);
                        st.put("ul",username.length()+"");
                        st.put("pl",password.length()+"");
                        st.put("sl","0");
                        st.put("lt",lt);
                        st.put("execution",execution);
                        st.put("_eventId","submit");

                        res = myutil.post("http://onecas.cau.edu.cn/tpass/login",st,header);

                        res = myutil.get("https://service.cau.edu.cn/site/form/start-data?app_id=366&draft=1",header);
                        //Log.d("loginresult",res);
                        resmap=JSONObject.parseObject(myutil.j2m(res).toString());
                        data=(JSONObject)resmap.get("d");
                        currform=(int)((JSONArray)data.get("currform")).get(0);
                        data=(JSONObject)((JSONObject)data.get("data")).get(currform+"");
                        data.put("Calendar_152",outtime);
                        data.put("Calendar_139",intime);
                        data.put("Calendar_154",intime);
                        data.put("User_135",phone);
                        data.put("Input_170",room);
                        data.put("Input_171",temper);
                        data.put("MultiInput_140",address);
                        data.put("MultiInput_101",thing);
                        data.put("SelectV2_169",JSONArray.parseArray(departmap[depart]));
                        data.put("SelectV2_173",JSONArray.parseArray(partmap[part]));

                        JSONObject formd = new JSONObject();
                        formd.put(currform+"",data);
                        JSONObject sdata = new JSONObject();
                        sdata.put("app_id",366);
                        sdata.put("node_id","");
                        sdata.put("form_data",formd);

                        Map senddata = new HashMap();
                        senddata.put("data",sdata.toJSONString());
                        //sdata.toString();
                        res = myutil.post("https://service.cau.edu.cn/site/apps/launch",senddata,header);
                        data = myutil.j2m(res);
                        String inst_id = (String)((JSONObject)data.get("d")).get("inst_id");

                        res = myutil.get("https://service.cau.edu.cn/site/process/log?inst_id="+inst_id,header);
                        data = myutil.j2m(res);
                        JSONObject tmpobject;
                        JSONArray tmparray;
                        tmpobject = (JSONObject)data.get("d");
                        tmparray = (JSONArray) tmpobject.get("lists");
                        tmpobject = (JSONObject) tmparray.get(1);
                        int task_id = (int)tmpobject.get("task_id");

                        JSONObject fdata = new JSONObject();
                        JSONObject detail = new JSONObject();
                        detail.put("Calendar_164",outtime);
                        detail.put("Input_161",address);
                        detail.put("SelectV2_162",JSONArray.parseArray(transmap[trans]));
                        detail.put("Input_163",transdetail);
                        detail.put("Input_165",dura);

                        JSONArray farray = new JSONArray();
                        farray.add(detail);
                        fdata.put("Calendar_144",outtime);
                        fdata.put("Calendar_146",intime);
                        fdata.put("RepeatTable_155",farray);
                        JSONObject form_data = new JSONObject();
                        form_data.put(currform+"",fdata);


                        senddata = new HashMap();
                        senddata.put("task_id",task_id+"");
                        senddata.put("form_data",form_data.toJSONString());
                        senddata.put("deal_data","{\"oversee\":\"no\",\"require_claim\":0,\"comment\":\"\",\"attachment\":[],\"reader\":[],\"operation\":{\"value\":3,\"name\":\"提交\"},\"sign_uid\":\"\"}");

                        res = myutil.post("https://service.cau.edu.cn/site/task/deal",senddata,header);
                        data = myutil.j2m(res);
                        res = (String)data.get("m");
                        if(res.equals("操作成功")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.hide();
                                    Toast.makeText(getApplicationContext(),"申请完成",Toast.LENGTH_LONG).show();
                                }
                            });
                            return;
                        }else{
                            Log.d("logstate",res);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"申请失败",Toast.LENGTH_LONG).show();
                                }
                            });
                            return;
                        }

                    }
                }
        ).start();
    }

    private String getdes(String username,String passwd,String lt){
        Map postdata=new HashMap();
        postdata.put("u",username);
        postdata.put("p",passwd);
        postdata.put("lt",lt);
        Map header = new HashMap();
        header.put("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 14_0_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.17(0x1700112a) NetType/WIFI Language/zh_CN");
        String res = myutil.post("http://120.27.101.140:2334",postdata,header);
        return res;
    }

    public void save(View view){
        phone = phoneet.getText().toString();
        room = roomet.getText().toString();
        temper = temperet.getText().toString();
        address = addresset.getText().toString();
        thing = thinget.getText().toString();
        depart = departsp.getSelectedItemPosition();
        part = partsp.getSelectedItemPosition();
        transdetail = transet.getText().toString();
        trans = transsp.getSelectedItemPosition();



        speditor.putString("phone",phone);
        speditor.putString("room",room);
        speditor.putString("temper",temper);
        speditor.putString("address",address);
        speditor.putString("thing",thing);
        speditor.putInt("depart",depart);
        speditor.putInt("part",part);
        speditor.putInt("trans",trans);
        speditor.putString("transdetail",transdetail);
        speditor.commit();
    }

    public void submit(View view){
        save(view);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        SimpleDateFormat formatter_display = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String intimestr = date+" "+intimeet.getText().toString();
        String outtimestr = date+" "+outtimeet.getText().toString();
        try{
            long ints,outts;
            ints = Long.parseLong(String.valueOf(formatter_display.parse(intimestr).getTime()));
            intime = formatter.format(new Date(ints));
            outts = Long.parseLong(String.valueOf(formatter_display.parse(outtimestr).getTime()));
            outtime = formatter.format(new Date(outts));
            String tmp1 = intimeet.getText().toString();
            String tmp2 = outtimeet.getText().toString();
            dura = (Integer.parseInt(tmp1.substring(0,2))-Integer.parseInt(tmp2.substring(0,2)))+"时"+(Integer.parseInt(tmp1.substring(3,5))-Integer.parseInt(tmp2.substring(3,5)))+"分";
            Log.d("duration",dura);
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"时间格式不正确",Toast.LENGTH_LONG).show();
        }
        makesubmit();
    }
}