package com.zk.bean;

public class ZkMidInfo {
    private String itemid="";
    private String hostid="";
    private String port="";
    private String item="";
    private String value_type="";

    private String hostip="";
    private String system_id="";
    private String hostname="";
    
    
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public String getHostid() {
		return hostid;
	}
	public void setHostid(String hostid) {
		this.hostid = hostid;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getValue_type() {
		return value_type;
	}
	public void setValue_type(String value_type) {
		this.value_type = value_type;
	}
	public String getHostip() {
		return hostip;
	}
	public void setHostip(String hostip) {
		this.hostip = hostip;
	}
	public String getSystem_id() {
		return system_id;
	}
	public void setSystem_id(String system_id) {
		this.system_id = system_id;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	@Override
	public String toString() {
		return "ZkMidInfo [itemid=" + itemid + ", hostid=" + hostid + ", port=" + port + ", item=" + item
				+ ", value_type=" + value_type + ", hostip=" + hostip + ", system_id=" + system_id + ", hostname="
				+ hostname + "]";
	}
	
	public ZkMidInfo() {

	}
	
	public ZkMidInfo(String itemid, String hostid, String port, String item, String value_type, String hostip,
			String system_id, String hostname) {
		this.itemid = itemid;
		this.hostid = hostid;
		this.port = port;
		this.item = item;
		this.value_type = value_type;
		this.hostip = hostip;
		this.system_id = system_id;
		this.hostname = hostname;
	}
    
    

}
