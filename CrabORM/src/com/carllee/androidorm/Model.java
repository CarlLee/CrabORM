package com.carllee.androidorm;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.carllee.androidorm.annotations.DataField;
import com.carllee.androidorm.annotations.DataField.FieldType;
import com.carllee.androidorm.annotations.DataTable;
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

	@Override
	public String getCreateTableStatement() {
		boolean isDataTableDecleared = this.getClass().isAnnotationPresent(
				DataTable.class);
		if (!isDataTableDecleared) {
			return null;
		}

		StringBuilder statement = new StringBuilder();

		DataTable dataTable = this.getClass().getAnnotation(DataTable.class);
		String tableName = dataTable.name();
		statement.append("CREATE TABLE " + tableName + " IF NOT EXISTS ");

		// building collumn statement
		statement.append("(");
		Field[] fields = this.getClass().getDeclaredFields();
		ArrayList<String> fieldStatements = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			boolean annotated = field.isAnnotationPresent(DataField.class);
			if (annotated) {
				String filedStatement = getDataFieldStatement(field);
				fieldStatements.add(filedStatement);
			}
		}

		// join them together
		for (int i = 0; i < fieldStatements.size(); i++) {
			statement.append(fieldStatements.get(i));
			if (i != fieldStatements.size() - 1) {
				statement.append(",");
			}
		}
		statement.append(");");
		return statement.toString();
	}

	/**
	 * Used to make SQL column statement for a DataField field
	 * 
	 * @param field
	 * @return
	 */
	private String getDataFieldStatement(Field field) {
		DataField annotation = field.getAnnotation(DataField.class);
		boolean unique = annotation.unique();
		FieldType type = annotation.filedType();
		StringBuilder sb = new StringBuilder();
		sb.append(field.getName() + " ");
		if (type == DataField.FieldType.DEFAULT) {
			// Get field from its deleared type
			Class<?> fieldType = field.getType();
			if (fieldType == String.class || fieldType == char.class
					|| fieldType == Character.class) {
				sb.append(DataField.FieldType.TEXT.name());
			}
			if (fieldType == int.class || fieldType == Integer.class
					|| fieldType == byte.class || fieldType == Byte.class
					|| fieldType == long.class || fieldType == Long.class
					|| fieldType == short.class || fieldType == Short.class
					|| fieldType == boolean.class || fieldType == Boolean.class) {
				sb.append(DataField.FieldType.INTEGER.name());
			}
			if (fieldType == float.class || fieldType == double.class
					|| fieldType == Float.class || fieldType == Double.class) {
				sb.append(DataField.FieldType.REAL.name());
			}
			// TODO blob for byte array and Serialazable data

		} else {
			sb.append(annotation.filedType().name());
		}

		sb.append(field.getName() + " ");
		if (unique) {
			sb.append(" UNIQUE");
		}
		return sb.toString();
	}
}
