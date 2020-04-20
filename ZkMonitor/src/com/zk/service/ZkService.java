package com.zk.service;

import com.zk.bean.ZkMidInfo;
import com.zk.bean.Zkinfo;
import java.util.List;

public interface ZkService {
    List<ZkMidInfo> getOldInfo();

    void insert(ZkMidInfo zkMidInfo);

    List<ZkMidInfo> getItem(String s);

    List<ZkMidInfo> getInfo(String s);

    List<Zkinfo> getzkinfo();

    void add(Zkinfo zkinfo);

    void update(Zkinfo zkinfo);

    List<ZkMidInfo> getip(String hostip);

    List<Zkinfo> getipinfo();

    List<ZkMidInfo> query();


}
