package com.carllee.craborm.interfaces;

import android.content.ContentValues;
import android.database.Cursor;

public interface IModel {
	public ContentValues getByContentValues();

	public void setByContentValues(ContentValues cv);

	/**
	 * Make sure to move cursor before calling this method
	 * 
	 * @param c
	 */
	public void setByCursor(Cursor c);
}
