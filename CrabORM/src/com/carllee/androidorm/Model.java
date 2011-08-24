package com.carllee.androidorm;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

import com.carllee.androidorm.interfaces.IModel;

public class Model implements IModel {

	@Override
	public ContentValues getByContentValues() {
		return null;
	}

	@Override
	public void setByContentValues(ContentValues cv) {

	}

	@Override
	public JSONObject getByJSON() {
		return null;
	}

	@Override
	public void setByJSON() {
	}

	@Override
	public void setByCursor(Cursor c) {
	}

}
