package com.carllee.androidorm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.carllee.androidorm.annotations.DataField;
import com.carllee.androidorm.annotations.DataField.FieldType;
import com.carllee.androidorm.annotations.DataTable;
import com.carllee.androidorm.interfaces.IDAO;
import com.carllee.androidorm.interfaces.IModel;

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

	public static String getCreateTableStatement(
			Class<? extends IModel> modelClass) {
		boolean isDataTableDecleared = modelClass
				.isAnnotationPresent(DataTable.class);
		if (!isDataTableDecleared) {
			return null;
		}

		StringBuilder statement = new StringBuilder();

		DataTable dataTable = modelClass.getAnnotation(DataTable.class);
		String tableName = dataTable.name();
		if (tableName.equals(DataTable.DEFAULT_TABLE_NAME)) {
			statement.append("CREATE TABLE " + modelClass.getSimpleName()
					+ " IF NOT EXISTS ");
		} else {
			statement.append("CREATE TABLE " + tableName + " IF NOT EXISTS ");
		}
		// building collumn statement
		statement.append("(");
		Field[] fields = modelClass.getDeclaredFields();
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
	private static String getDataFieldStatement(Field field) {
		DataField annotation = field.getAnnotation(DataField.class);
		boolean unique = annotation.unique();
		boolean primaryKey = annotation.primaryKey();
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

		if (primaryKey) {
			sb.append(" PRIMARY KEY AUTOINCREMENT");
		} else {
			if (unique) {
				sb.append(" UNIQUE");
			}
		}
		return sb.toString();
	}
}
