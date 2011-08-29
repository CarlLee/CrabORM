package com.carllee.craborm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.carllee.craborm.annotations.DataField;
import com.carllee.craborm.annotations.DataField.FieldType;
import com.carllee.craborm.annotations.DataTable;
import com.carllee.craborm.interfaces.IModel;

public class Model implements IModel {

	@Override
	public ContentValues getByContentValues() {
		ContentValues cv = new ContentValues();

		Class<? extends Model> modelClass = this.getClass();
		Field[] fields = modelClass.getDeclaredFields();
		for (Field field : fields) {
			boolean annotated = field.isAnnotationPresent(DataField.class);
			if (annotated && !field.getAnnotation(DataField.class).primaryKey()) {
				try {
					Object data = field.get(this);
					if (data instanceof Character) {
						cv.put(field.getName(), String.valueOf(data));
						continue;
					}
					if (data instanceof String) {
						cv.put(field.getName(), (String) data);
						continue;
					}
					if (data instanceof Byte) {
						cv.put(field.getName(), (String) data);
						continue;
					}
					if (data instanceof Integer) {
						cv.put(field.getName(), (Integer) data);
						continue;
					}
					if (data instanceof Short) {
						cv.put(field.getName(), (Short) data);
						continue;
					}
					if (data instanceof Long) {
						cv.put(field.getName(), (Long) data);
						continue;
					}
					if (data instanceof Boolean) {
						cv.put(field.getName(), (Boolean) data);
						continue;
					}
					if (data instanceof Float) {
						cv.put(field.getName(), (Float) data);
						continue;
					}
					if (data instanceof Double) {
						cv.put(field.getName(), (Double) data);
						continue;
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return cv;
	}

	@Override
	public void setByContentValues(ContentValues cv) {
		Class<? extends Model> modelClass = this.getClass();
		Field[] fields = modelClass.getDeclaredFields();
		for (Field field : fields) {
			boolean annotated = field.isAnnotationPresent(DataField.class);
			if (annotated) {
				Object data = cv.get(field.getName());
				try {
					field.set(this, data);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void setByCursor(Cursor c) {
		Class<? extends Model> modelClass = this.getClass();
		Field[] fields = modelClass.getDeclaredFields();
		for (Field field : fields) {
			boolean annotated = field.isAnnotationPresent(DataField.class);
			if (annotated) {
				int columnIndex = c.getColumnIndex(field.getName());
				Class<?> fieldType = field.getType();
				Object data = null;
				if (fieldType == String.class) {
					data = c.getString(columnIndex);
				}
				if (fieldType == int.class || fieldType == Integer.class) {
					data = c.getInt(columnIndex);
				}
				if (fieldType == short.class || fieldType == Short.class) {
					data = c.getShort(columnIndex);
				}
				if (fieldType == long.class || fieldType == Long.class) {
					data = c.getLong(columnIndex);
				}
				if (fieldType == float.class || fieldType == Float.class) {
					data = c.getFloat(columnIndex);
				}
				if (fieldType == double.class || fieldType == Double.class) {
					data = c.getDouble(columnIndex);
				}
				try {
					field.set(this, data);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static <T extends IModel>  List<T> generateFromCursor(Cursor cursor,
			Class<? extends T> modelClass) throws IllegalAccessException,
			InstantiationException {
		ArrayList<T> result = new ArrayList<T>();
		if (!cursor.moveToFirst()) {
			return null;
		}
		while (cursor.moveToNext()) {
			T model = modelClass.newInstance();
			model.setByCursor(cursor);
			result.add(model);
		}
		return result;
	}

	public static String[] getColumns(Class<? extends Object> modelClass) {
		ArrayList<String> columns = new ArrayList<String>();
		Field[] fields = modelClass.getDeclaredFields();
		for (Field field : fields) {
			boolean annotated = field.isAnnotationPresent(DataField.class);
			if (annotated) {
				columns.add(field.getName());
			}
		}
		return columns.toArray(new String[1]);
	}

	public static String getCreateTableStatement(
			Class<? extends Object> modelClass) {
		boolean isDataTableDecleared = modelClass
				.isAnnotationPresent(DataTable.class);
		if (!isDataTableDecleared) {
			return null;
		}

		StringBuilder statement = new StringBuilder();

		DataTable dataTable = modelClass.getAnnotation(DataTable.class);
		String tableName = dataTable.name();
		if (tableName.equals(DataTable.DEFAULT_TABLE_NAME)) {
			statement
					.append("CREATE TABLE " + modelClass.getSimpleName() + " ");
		} else {
			statement.append("CREATE TABLE " + tableName + " ");
		}
		// building column statement
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
