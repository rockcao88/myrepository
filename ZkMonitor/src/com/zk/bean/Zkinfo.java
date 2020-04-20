package com.zk.bean;

public class Zkinfo {
    private String port="";
    private String hostip="";
    private String system_id="";
    private String sys3_A="";
    private String system_name="";
    private String short_name="";
    private String online_time="";
    private String hostname="";
    private String value;
    private String itemid="";
    private String item="";

    private String time="";
    

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
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

	public String getSys3_A() {
		return sys3_A;
	}

	public void setSys3_A(String sys3_A) {
		this.sys3_A = sys3_A;
	}

	public String getSystem_name() {
		return system_name;
	}

	public void setSystem_name(String system_name) {
		this.system_name = system_name;
	}

	public String getShort_name() {
		return short_name;
	}

	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}

	public String getOnline_time() {
		return online_time;
	}

	public void setOnline_time(String online_time) {
		this.online_time = online_time;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Zkinfo [port=" + port + ", hostip=" + hostip + ", system_id=" + system_id + ", sys3_A=" + sys3_A
				+ ", system_name=" + system_name + ", short_name=" + short_name + ", online_time=" + online_time
				+ ", hostname=" + hostname + ", value=" + value + ", itemid=" + itemid + ", item=" + item + ", time="
				+ time + "]";
	}

	public Zkinfo() {
		
	}
	
	public Zkinfo(String port, String hostip, String system_id, String sys3_A, String system_name, String short_name,
			String online_time, String hostname, String value, String itemid, String item, String time) {
		this.port = port;
		this.hostip = hostip;
		this.system_id = system_id;
		this.sys3_A = sys3_A;
		this.system_name = system_name;
		this.short_name = short_name;
		this.online_time = online_time;
		this.hostname = hostname;
		this.value = value;
		this.itemid = itemid;
		this.item = item;
		this.time = time;
	}
    

}
