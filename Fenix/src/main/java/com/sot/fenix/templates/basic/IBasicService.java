package com.sot.fenix.templates.basic;

public interface IBasicService<T extends IBasicDAO>{
	public T getDAO();
}
