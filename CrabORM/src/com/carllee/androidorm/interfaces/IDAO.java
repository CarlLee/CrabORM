package com.carllee.androidorm.interfaces;

import java.util.List;

public interface IDAO<T extends IModel> {

	public List<T> query(String where, String[] whereArgs);

	public int insert(T model);

	public int insert(List<T> models);

	public int update(T model, String where, String[] whereArgs);
	
	public int delete(String where, String[] whereArgs);
}
