package com.zk.serviceImpl;


import com.zk.bean.ZkMidInfo;
import com.zk.bean.Zkinfo;
import com.zk.mapper.Zkmapper;
import com.zk.service.ZkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ZkServiceImpl implements ZkService {
	
    @Autowired
    private Zkmapper mapper;

    @Override
    public List<ZkMidInfo> getOldInfo() {
        return mapper.getOldInfo();
    }

    @Override
    public void insert(ZkMidInfo zkMidInfo) {
        mapper.insert(zkMidInfo);
    }

    @Override
    public List<ZkMidInfo> getItem(String s) {
        return mapper.getItem(s);
    }

    @Override
    public List<ZkMidInfo> getInfo(String s) {
        return mapper.getInfo(s);
    }

    @Override
    public List<Zkinfo> getzkinfo() {
        return mapper.getzkinfo();
    }

    @Override
    public void add(Zkinfo zkinfo) {
        mapper.add(zkinfo);
    }

    @Override
    public void update(Zkinfo zkinfo) {
        mapper.update(zkinfo);
    }

    @Override
    public List<ZkMidInfo> getip(String hostip) {
        return mapper.getip(hostip);
    }

    @Override
    public List<Zkinfo> getipinfo() {
        return mapper.getipinfo();
    }

    @Override
    public List<ZkMidInfo> query() {
        return mapper.query();
    }



}
