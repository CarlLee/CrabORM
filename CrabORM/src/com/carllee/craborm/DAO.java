package com.carllee.craborm;

import java.util.List;

import com.carllee.craborm.interfaces.IDAO;
import com.carllee.craborm.interfaces.IModel;

public class DAO<T extends IModel> implements IDAO<T> {

	@Override
	public List<T> query(String where, String[] whereArgs) {
		return null;
	}

	@Override
	public int insert(T model) {
		return 0;
	}

	@Override
	public int insert(List<T> models) {
		return 0;
	}

	@Override
	public int update(T model, String where, String[] whereArgs) {
		return 0;
	}

	@Override
	public int delete(String where, String[] whereArgs) {
		return 0;
	}
}
