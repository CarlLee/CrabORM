package com.carllee.androidorm.test;

import com.carllee.androidorm.Model;
import com.carllee.androidorm.annotations.DataField;
import com.carllee.androidorm.annotations.DataTable;

@DataTable(name = "SimpleModel")
public class SimpleModel extends Model {

	@DataField
	long var0;
	@DataField
	Long var1;
	@DataField
	int var2;
	@DataField
	Integer var3;
	@DataField
	float var4;
	@DataField
	Float var5;
	@DataField
	double var6;
	@DataField
	Double var7;
	@DataField
	byte var8;
	@DataField
	Byte var9;
	@DataField
	char var10;
	@DataField
	String var11;
}
