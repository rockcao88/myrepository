package com.zk.driver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zk.bean.ZkMidInfo;
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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GetMidtable {
	
    private String[] itemNames = {
            //"Java Heap Size","zk_max_file_descriptor_count","zk_open_file_descriptor_count",
            "zk_avg_latency", "zk_min_latency", "zk_max_latency",
            "zk_outstanding_requests", "zk_packets_received", "zk_packets_sent",
            "zk_followers", "zk_pending_syncs", "zk_znode_count",
            "zk_watch_count", "zk_server_state", "zk_alive_connections",
            "zk_ephemerals_count", "zk_approximate_data_size"};
    private String systemapi = "http://22.122.32.157:83/portal/service/servers";
    private String zabbixapi = "http://22.122.16.207:94/zabbix/api_jsonrpc.php";
    private String auth = "f3bae7f23cfd1fa6d6d632343516ff1a";

    //获取旧数据
    @Autowired
    private ZkService service;

    public Map<String, String> getOldInfoMap() {
        List<ZkMidInfo> list = service.getOldInfo();
        Map<String, String> map = new HashMap<String, String>();
        for (ZkMidInfo zkinfo : list) {
            String itemid = zkinfo.getItemid();
            String hostid = zkinfo.getHostid();
            map.put(itemid, hostid);
        }
        return map;
    }

    //获取systemid
    public String getSystemid(String hostip) {
        //获取system_id
        String system_id = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //String url = systemapi;
        HttpPost post = new HttpPost(systemapi);
        //请求头
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Trident/7.0; rv:11.0");
        post.setHeader("Content-Type", "application/json");
        post.setHeader("apikey", "p5a2L1zBCyvhEd6YuN4j06qSOS9RTQVM");
        //请求体
        if (!hostip.equals("")) {
            String body = "{\"hosts\": [\"" + hostip + "\"]}";
            try {
                post.setEntity(new StringEntity(body));
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
            //响应
            if (response.getStatusLine().getStatusCode() == 200) {
                try {
                    JSONObject object = JSON.parseObject(EntityUtils.toString(response.getEntity()));
                    JSONArray jsonArray = object.getJSONArray("data");
                    JSONObject json = (JSONObject) jsonArray.get(0);
                    String id = json.getString("system_id");
                    if (id != null) {
                        system_id = id;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return system_id;
    }

    //获取中间表
    public void getZkInfo(String[] itemNames) {
        //中间表的对象
        ZkMidInfo zkMidInfo = new ZkMidInfo();
        //旧数据
        Map<String, String> oldInfo = this.getOldInfoMap();
        //请求zabbix
        DefaultZabbixApi zabbixApi = new DefaultZabbixApi(zabbixapi);
        zabbixApi.init();
        for (String itemName : itemNames) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", itemName);
            Request request = RequestBuilder.newBuilder()
                    .method("item.get")
                    .paramEntry("output", new String[]{"itemid", "hostid", "value_type"})
                    .paramEntry("filter", jsonObject)
                    .auth(auth)
                    .id(1)
                    .build();
            JSONObject object = zabbixApi.call(request);
            JSONArray jsonArray = object.getJSONArray("result");

            if (jsonArray.size() <= 0) {
                System.out.println("没有该项指标");
            } else {
                for (Object object0 : jsonArray) {
                    JSONObject json = (JSONObject) object0;
                    String hostid = json.getString("hostid");
                    String itemid = json.getString("itemid");
                    String value_type = json.getString("value_type");
                    String item = itemName;
                    String port = "2181";
                    Boolean flag = false;
                    if (json.getString("hostid").equals("10296")) {
                        continue;
                    }
                    if (oldInfo.containsKey(itemid)) {
                        String hostidget = oldInfo.get(itemid);
                        if (hostid.equals(hostid)) {
                            flag = true;
                        }
                    }
                    if (flag) {
                        continue;
                    }
                    String ip ="";
                    String host="";
                    //通过hostid  获取hostip和hostname
                    Request request1 = RequestBuilder.newBuilder()
                            .method("hostinterface.get")
                            .paramEntry("output", new String[]{"ip"})
                            .paramEntry("hostids", hostid)
                            .auth(auth)
                            .id(1)
                            .build();
                    JSONObject object1 = zabbixApi.call(request1);
                    JSONArray jsonArray1 = object1.getJSONArray("result");
                    if (jsonArray1.size() <= 0) {
                        System.out.println("没有ip");
                        zkMidInfo.setHostip("");
                    } else {
                        JSONObject object2 = (JSONObject) jsonArray1.get(0);
                        ip = object2.getString("ip");
                    }
                    zkMidInfo.setHostip(ip);
                    //获取hostname
                    Request request2 = RequestBuilder.newBuilder()
                            .method("host.get")
                            .paramEntry("output", new String[]{"host"})
                            .paramEntry("hostids", hostid)
                            .auth(auth)
                            .id(1)
                            .build();
                    JSONObject object3 = zabbixApi.call(request2);
                    JSONArray jsonArray2 = object3.getJSONArray("result");
                    if (jsonArray2.size() <= 0) {
                        System.out.println("没有hostname");
                        zkMidInfo.setHostname("");
                    } else {
                        JSONObject object4 = (JSONObject) jsonArray2.get(0);
                        host = object4.getString("host");
                    }
                    zkMidInfo.setHostname(host);
                    String system_id = getSystemid(ip);

                    zkMidInfo.setSystem_id(system_id);
                    zkMidInfo.setItem(item);
                    zkMidInfo.setHostid(hostid);
                    zkMidInfo.setPort(port);
                    zkMidInfo.setValue_type(value_type);
                    zkMidInfo.setItemid(itemid);

                    System.out.println(zkMidInfo);
                    //写入
                    service.insert(zkMidInfo);
                }
            }
        }
    }

}
