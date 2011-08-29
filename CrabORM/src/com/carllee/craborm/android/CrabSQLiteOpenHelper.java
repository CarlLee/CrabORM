package com.carllee.craborm.android;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.carllee.craborm.Model;

public abstract class CrabSQLiteOpenHelper extends SQLiteOpenHelper {

	private List<Class<? extends Object>> modelClasses;

	public CrabSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.modelClasses = getModelClasses();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (Class<? extends Object> modelclass : modelClasses) {
			String createStatement = Model.getCreateTableStatement(modelclass);
			db.execSQL(createStatement);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO upgrade db
	}

	public abstract List<Class<? extends Object>> getModelClasses();
}
