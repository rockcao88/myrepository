package com.zk.driver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zk.bean.ZkMidInfo;
import com.zk.bean.Zkinfo;
import com.zk.service.ZkService;
import io.github.hengyunabc.zabbix.api.DefaultZabbixApi;
import io.github.hengyunabc.zabbix.api.Request;
import io.github.hengyunabc.zabbix.api.RequestBuilder;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;


public class GetZkinfo implements Runnable {
	private String systemapi = "http://22.122.32.157:83/portal/service/servers";
    private String zabbixapi = "http://22.122.16.207:94/zabbix/api_jsonrpc.php";
    private String auth = "f3bae7f23cfd1fa6d6d632343516ff1a";
    private String ip;
    @Autowired
    private ZkService service;

    //构造方法传入参数
    public GetZkinfo(String ip) {
        this.ip = ip;
    }

    @Override
    public void run() {
        this.getresult();
    }

    public void getresult(){
        List<ZkMidInfo> list = service.getip(ip);
        //取出itemid集合用于查询value
        List<Object> itemidlist = new ArrayList<Object>();
        for (ZkMidInfo zkMidInfo : list) {
            itemidlist.add(zkMidInfo.getItemid());
        }
        Map<String, String> map1 = this.getLastvalueByitemid(itemidlist);
        Map<String, String> map2 = this.getInfoByip();
        //将所有结果封装
        List<Zkinfo> result = new ArrayList<Zkinfo>();
        for (ZkMidInfo zkMidInfo : list) {
            Zkinfo info = new Zkinfo();
            String itemid = zkMidInfo.getItemid();
            String line1 = map1.getOrDefault(itemid, "");
            String[] string1 = line1.split("\t");
            String value=string1[0];
            String timelong=string1[1];
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String time = format.format(new Date(Long.parseLong(timelong)));

            String hostip = zkMidInfo.getHostip();
            String line2 = map2.getOrDefault(hostip, "");
            String[] string2 = line2.split("\t");

            info.setTime(time);
            info.setItem(zkMidInfo.getItem());
            info.setValue(value);
            info.setItemid(itemid);
            info.setHostip(hostip);
            info.setHostname(zkMidInfo.getHostname());
            info.setPort(zkMidInfo.getPort());
            info.setSystem_id(string2[5]);
            info.setOnline_time(string2[4]);
            info.setSys3_A(string2[3]);
            info.setShort_name(string2[2]);
            info.setSystem_name(string2[1]);
            //将所有结果封装
            System.out.println(info);
            result.add(info);
        }
        //写入数据库
        this.write2mysql(result);
    }
    //写入数据库
    public void write2mysql(List<Zkinfo> zkinfo){
        Map<String, String> map = new HashMap<String, String>();
        List<Zkinfo> list = service.getipinfo();
        for (Zkinfo zkinfo1 : list) {
            map.put(zkinfo1.getItemid(),zkinfo1.getValue());
        }
        for (Zkinfo zkinfo2 : zkinfo) {
            String itemid = zkinfo2.getItemid();
            if(map.containsKey(itemid)){
                //数据库中包含此id  更新记录
                service.update(zkinfo2);
            }else{
                //数据库中没有此id  写入数据库
                service.add(zkinfo2);
            }
        }
    }
    //通过itemid 获取lastvalue
    public Map<String, String> getLastvalueByitemid(List<Object> itemidlist) {
        Map<String, String> map = new HashMap<String, String>();
        DefaultZabbixApi zabbixApi = new DefaultZabbixApi(zabbixapi);
        zabbixApi.init();
        Request request = RequestBuilder.newBuilder()
                .method("item.get")
                .paramEntry("output", new String[]{"itemid", "lastvalue","lastclock"})
                .paramEntry("itemids", itemidlist)
                .id(1)
                .auth(auth)
                .build();
        JSONObject object = zabbixApi.call(request);
        JSONArray jsonArray = object.getJSONArray("result");
        if (jsonArray.size() > 0) {
            for (Object object2 : jsonArray) {
                JSONObject json = (JSONObject) object2;
                String itemid = json.getString("itemid");
                String lastvalue = json.getString("lastvalue");
                String time = json.getString("lastclock");
                String s=lastvalue+"\t"+time;
                map.put(itemid, s);
            }
        }
        return map;
    }

    public Map<String, String> getInfoByip() {
        Map<String, String> map = new HashMap<String, String>();
        String system_name = "";
        String short_name = "";
        String sys3_A = "";
        String online_time = "";
        String system_id = "";
        String bodys = "{\"hosts\": [\"" + ip + "\"]}";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        //String url = "http://22.122.32.157:83/portal/service/servers";
        HttpPost post = new HttpPost(systemapi);
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Trident/7.0; rv:11.0");
        post.setHeader("Content-Type", "application/json");
        post.setHeader("apikey", "p5a2L1zBCyvhEd6YuN4j06qSOS9RTQVM");

        try {
            post.setEntity(new StringEntity(bodys));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(post);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray array = jsonObject.getJSONArray("data");
        for (Object object2 : array) {
            JSONObject json = (JSONObject) object2;
            String ip = json.getString("ip");
            if (json.size() > 4) {
                system_name = json.getString("system_name");
                short_name = json.getString(" short_name");
                sys3_A = json.getString("sys3_A");
                online_time = json.getString("online_time");
                system_id = json.getString("system_id");
            } else {
                system_name = "";
                short_name = "";
                sys3_A = "";
                online_time = "";
                system_id = "";
            }
            if (!map.containsKey(ip)) {
                map.put(ip, ip + "\t" + system_name + "\t" + short_name + "\t" + sys3_A + "\t" + online_time + "\t" + system_id);
            }
        }
        return map;
    }

}
