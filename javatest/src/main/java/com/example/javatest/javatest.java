package com.example.javatest;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
public class javatest {


    private static String city;
    private static String number;
    private static String result;


    //定义获取手机信息的SoapAction与命名空间,作为常量
    private static final String AddressnameSpace = "http://WebXml.com.cn/";
    //天气查询相关参数
    private static final String Weatherurl = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx?wsdl";
    private static final String Weathermethod = "getWeather";
    private static final String WeathersoapAction = "http://WebXml.com.cn/getWeather";
//    private static final String WeathersoapAction = null;
    //归属地查询相关参数
    private static final String Addressurl = "http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx?wsdl";
    private static final String Addressmethod = "getMobileCodeInfo";
    private static final String AddresssoapAction = "http://WebXml.com.cn/getMobileCodeInfo";


    public static void main(String[] args) {
        System.out.println("JavaTestClass:main");
        //getWether();
        getland();
    }

    //定义一个获取某城市天气信息的方法：
    public static void getWether() {
        result = "";
        city = "洛阳";
        SoapObject soapObject = new SoapObject(AddressnameSpace, Weathermethod);
        soapObject.addProperty("theCityCode:", city);
        soapObject.addProperty("theUserID", "47f20981511b45de8090e4028875096e");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(Weatherurl);
        System.out.println("天气服务设置完毕,准备开启服务");
        try {
            httpTransportSE.call(WeathersoapAction, envelope);
            System.out.println("调用WebService服务成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用WebService服务失败");
        }

        //获得服务返回的数据,并且开始解析
        SoapObject object = (SoapObject) envelope.bodyIn;
        System.out.println("获得服务数据");
        result = object.getProperty(0).toString();
        System.out.println("结果显示：\n" + result);
        System.out.println("发送完毕,textview显示天气信息");
    }


    //定义一个获取号码归属地的方法：
    public static void getland() {
        result = "";
        SoapObject soapObject = new SoapObject(AddressnameSpace, Addressmethod);
        soapObject.addProperty("mobileCode", number);
        soapObject.addProperty("userid", "47f20981511b45de8090e4028875096e");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(Addressurl);
        //	System.out.println("号码信息设置完毕,准备开启服务");
        try {
            httpTransportSE.call(AddresssoapAction, envelope);
            System.out.println("调用WebService服务成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用WebService服务失败");
        }

        //获得服务返回的数据,并且开始解析
        SoapObject object = (SoapObject) envelope.bodyIn;//System.out.println("获得服务数据");
        result = object.getProperty(0).toString();//System.out.println("获取信息完毕,向主线程发信息");
        System.out.println("结果显示：\n" + result);
    }

}
