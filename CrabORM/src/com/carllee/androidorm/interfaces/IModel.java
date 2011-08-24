package com.carllee.androidorm.interfaces;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

public interface IModel {
	public ContentValues getByContentValues();

	public void setByContentValues(ContentValues cv);

	public JSONObject getByJSON();

	public void setByJSON();

	public void setByCursor(Cursor c);

	public String getCreateTableStatement();
}
