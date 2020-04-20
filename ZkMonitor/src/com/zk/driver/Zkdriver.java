package com.zk.driver;

import com.zk.bean.ZkMidInfo;
import com.zk.service.ZkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component
public class Zkdriver {
    private String[] itemNames = {
            //"Java Heap Size","zk_max_file_descriptor_count","zk_open_file_descriptor_count",
            "zk_avg_latency", "zk_min_latency", "zk_max_latency",
            "zk_outstanding_requests", "zk_packets_received", "zk_packets_sent",
            "zk_followers", "zk_pending_syncs", "zk_znode_count",
            "zk_watch_count", "zk_server_state", "zk_alive_connections",
            "zk_ephemerals_count", "zk_approximate_data_size"};
    
    @Autowired
    private ZkService service;
    
    //每周一  执行一次用于获取中间表
    @Scheduled(cron = "0 0 0 ? * MON")
    public void fun2(){
        GetMidtable mid = new GetMidtable();
        mid.getZkInfo(itemNames);
    }

    //每2分钟 执行一次用于获取监控数据
    @Scheduled(cron = "0 */2 * * * ?")
    public void fun(){
        List<String> list = new ArrayList<String>();

        List<ZkMidInfo> iplist = service.query();
        for (ZkMidInfo zkMidInfo : iplist) {
            list.add(zkMidInfo.getHostip());
        }
        //使用线程池
        ExecutorService pool= Executors.newCachedThreadPool();
        for (String ip : list) {
        	//提交
            pool.submit(new GetZkinfo(ip));
        }
        //关闭线程池
        pool.shutdown();
    }



}
