package com.carllee.androidorm.test;

import android.test.AndroidTestCase;
import android.util.Log;

public class ModelTest extends AndroidTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetCreateTableStatement() {
		String statement = new SimpleModel().getCreateTableStatement();
		Log.v("AndroidORM", "" + statement);
	}

}
