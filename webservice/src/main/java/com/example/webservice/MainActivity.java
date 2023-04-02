package com.example.webservice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_param;
    private Button btn_attribution;
    private Button btn_weather;
    private TextView txt_result;

    private String city;
    private String number;
    private String result;


    //定义获取手机信息的SoapAction与命名空间,作为常量
    private static final String AddressnameSpace = "http://WebXml.com.cn/";
    //天气查询相关参数 61.147.124.120
    private static final String Weatherurl = "http://192.168.1.99/WebServices/WeatherWS.asmx?wsdl";
    private static final String Weathermethod = "getWeather";
    private static final String WeathersoapAction = "http://WebXml.com.cn/getWeather";
    //    private static final String WeathersoapAction = null;
    //归属地查询相关参数
    private static final String Addressurl = "http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx";
    private static final String Addressmethod = "getMobileCodeInfo";
    private static final String AddresssoapAction = "http://WebXml.com.cn/getMobileCodeInfo";


    //定义一个Handler用来更新页面：
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    txt_result.setText("结果显示：\n" + result);
                    Toast.makeText(MainActivity.this, "获取天气信息成功", Toast.LENGTH_SHORT).show();
                    break;
                case 0x002:
                    txt_result.setText("结果显示：\n" + result);
                    Toast.makeText(MainActivity.this, "号码归属地查询成功", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
    }

    private void bindViews() {
        edit_param = (EditText) findViewById(R.id.edit_param);
        btn_attribution = (Button) findViewById(R.id.btn_attribution);
        btn_weather = (Button) findViewById(R.id.btn_weather);
        txt_result = (TextView) findViewById(R.id.txt_result);
        btn_attribution.setOnClickListener(this);
        btn_weather.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_weather:
                new Thread() {
                    @Override
                    public void run() {
                        txt_result.append("获取天气信息服务不可用\n");
                        //getWether();
                    }
                }.start();
                break;
            case R.id.btn_attribution:
                new Thread(new Runnable() {
                    public void run() {
                        getland();
                    }
                }).start();
                break;
        }
    }


    //定义一个获取某城市天气信息的方法：
    public void getWether() {
        result = "";
        SoapObject soapObject = new SoapObject(AddressnameSpace, Weathermethod);
        soapObject.addProperty("theCityCode:", edit_param.getText().toString());
        txt_result.append(edit_param.getText().toString());
        soapObject.addProperty("theUserID", "47f20981511b45de8090e4028875096e");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(Weatherurl);
        System.out.println("天气服务设置完毕,准备开启服务");
        try {
            httpTransportSE.call(WeathersoapAction, envelope);
            txt_result.append("调用WebService服务成功");

//            System.out.println("调用WebService服务成功");
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println("调用WebService服务失败");
            txt_result.append("调用WebService服务失败");
        }

        //获得服务返回的数据,并且开始解析
        SoapObject object = (SoapObject) envelope.bodyIn;
        txt_result.append("获得服务数据");
        System.out.println("获得服务数据");

        result = object.getProperty(0).toString();

        handler.sendEmptyMessage(0x001);
        txt_result.append("发送完毕,textview显示天气信息");

        System.out.println("发送完毕,textview显示天气信息");
    }


    //定义一个获取号码归属地的方法：
    public void getland() {
        result = "";
        SoapObject soapObject = new SoapObject(AddressnameSpace, Addressmethod);
        soapObject.addProperty("mobileCode", edit_param.getText().toString());
        soapObject.addProperty("userid", "47f20981511b45de8090e4028875096e");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(Addressurl);
        //	System.out.println("号码信息设置完毕,准备开启服务");
        try {
            httpTransportSE.call(AddresssoapAction, envelope);
            //System.out.println("调用WebService服务成功");
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("调用WebService服务失败");
        }

        //获得服务返回的数据,并且开始解析
        SoapObject object = (SoapObject) envelope.bodyIn;//System.out.println("获得服务数据");
        result = object.getProperty(0).toString();//System.out.println("获取信息完毕,向主线程发信息");
        System.out.println(result);
        handler.sendEmptyMessage(0x001);
        //System.out.println("发送完毕,textview显示天气信息");
    }


}
