package com.sot.fenix.components.json;

import java.util.List;

public class PageJSON<T>{
	public long total;
	public List<T> data;
	

	public PageJSON(long total, List<T> data) {
		this.total = total;
		this.data = data;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}

	
}
