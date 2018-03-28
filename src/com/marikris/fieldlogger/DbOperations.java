package com.marikris.fieldlogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

public class DbOperations extends SQLiteOpenHelper implements LogAdapter {

	public static final String ID = "id";
	public static final String DATE = "date";
	public static final String CONDUCT = "conduct";
	public static final String PH = "ph";
	public static final String MOISTURE = "moist";
	public static final String OXYGEN = "oxygen";
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "log_entry.db";
	private static final String TAG = "DBOperations";
	String DB_TABLE = "";
	SQLiteDatabase sqlDB;
	Context context;
	public List<LogEntry> list;
	public List<ProfileEntry> profile;
	public static String DB_PATH;

	@SuppressLint("SdCardPath")
	public DbOperations(Context c, String logTable) {
		super(c, DB_NAME, null, DB_VERSION);
		Log.w(TAG, "Create.......");
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			DB_PATH = c.getApplicationInfo().dataDir + "/databases/";
		} else {
			DB_PATH = "/data/data/" + c.getPackageName() + "/databases/";
		}
		this.context = c;
		DB_TABLE = logTable;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// LogEntry.d(TAG, "Table created....");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		/*
		 * if(newVersion>oldVersion) try { copyDB(); } catch (IOException e) {
		 * Log.w(TAG, "Upgrading database from version " + oldVersion + " to " +
		 * newVersion + ", which will destroy all old data");
		 * e.printStackTrace(); }
		 */
	}

	// ---create and/or open a database---
	@Override
	public void createDB() {
		boolean isExist = checkDB(DB_PATH + DB_NAME);
		if (!isExist) {
			try {
				this.getReadableDatabase();
				copyDB();
				this.close();
			} catch (IOException e) {
				throw new Error("Create Error");
			}
		}
	}

	// ---open the database---
	@Override
	public boolean openDB() {
		try {
			String mPath = DB_PATH + DB_NAME;
			sqlDB = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
		} catch (SQLException sql) {
		}
		return sqlDB != null;
	}

	// ---tries to get read/write access to the database---
	public void insertEntry() {
		File dbFile = new File(context.getFilesDir() + "/" + DB_TABLE);
		boolean isExist = dbFile.exists();
		if (isExist) {
			// get a writable database
			sqlDB = this.getWritableDatabase();
			// run the query
			sqlDB.execSQL("DELETE FROM " + DB_TABLE);
			getList();
			for (int i = 0; i < list.size(); i++) {
				ContentValues initialValues = new ContentValues();
				initialValues.put(DATE, list.get(i).getDate());
				initialValues.put(CONDUCT, list.get(i).getConduct());
				initialValues.put(PH, list.get(i).getPh());
				initialValues.put(MOISTURE, list.get(i).getMoist());
				initialValues.put(OXYGEN, list.get(i).getOxygen());
				sqlDB.insert(DB_TABLE, null, initialValues);
			}
			Log.w(TAG, "Record Saved Successfully!");
			dbFile.delete();
		} else {
			Log.w(TAG, "Failed to create database");
		}

	}

	// ---deleting data in the database---
	@Override
	public void deleteEntry() {
		sqlDB = this.getWritableDatabase();
		sqlDB.delete(DB_TABLE, null, null);
	}

	// ---writing to files---
	@Override
	public void addList(LogEntry log) {
		list.add(log);
		try {
			// the file will override
			FileOutputStream fileO = context.openFileOutput(DB_TABLE, Context.MODE_PRIVATE);
			ObjectOutputStream objO = new ObjectOutputStream(fileO);
			objO.writeObject(list);
			objO.close();
			fileO.close();
		} catch (IOException e) {
		}
	}

	// ---copy the database from the assets folder into the databases folder---
	private void copyDB() throws IOException {
		String filePath = DB_PATH + DB_NAME;
		OutputStream output = new FileOutputStream(filePath);
		InputStream input = context.getAssets().open(DB_NAME);
		try {
			//---copy 1K bytes at a time---
			byte[] buffer = new byte[1024];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		output.flush();
		output.close();
		input.close();
	}

	// ---check file if exist in database---
	public boolean checkDB(String fileName) {
		File dbFile = new File(fileName);
		return dbFile.exists();
	}

	// ---get all data from the database---
	@Override
	public Cursor getInformation() {
		sqlDB = this.getReadableDatabase();
		String[] projections = { ID, DATE, CONDUCT, PH, MOISTURE, OXYGEN };
		// create Cursor in order to parse sqlite results
		Cursor cursor = sqlDB.query(DB_TABLE, projections, null, null, null, null, null);
		return cursor;
	}

	// ---get data from cursor---
	@Override
	public List<LogEntry> getDatabase() {
		list = new ArrayList<LogEntry>();
		Cursor c = getInformation();
		if (c.moveToFirst()) {
			do {
				LogEntry log = new LogEntry(c.getString(1), c.getFloat(2), c.getFloat(3), c.getInt(4), c.getInt(5));
				list.add(log);
			} while (c.moveToNext());
		}
		close();
		return list;
	}

	// ---reading from files---
	@SuppressWarnings("unchecked")
	@Override
	public List<LogEntry> getList() {
		list = new ArrayList<LogEntry>();
		try {
			FileInputStream fileI = context.openFileInput(DB_TABLE);
			ObjectInputStream objI = new ObjectInputStream(fileI);
			list = (List<LogEntry>) objI.readObject();

		} catch (IOException e) {
		} catch (ClassNotFoundException cnfe) {
		}
		return list;
	}
	
	// ---close any open database object---
	public synchronized void close() {
		if (sqlDB != null)
			sqlDB.close();
		super.close();
	}

}
