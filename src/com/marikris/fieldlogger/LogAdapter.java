package com.marikris.fieldlogger;

import android.database.Cursor;
import java.util.List;

/**
 * Interface containing methods to be implemented in DbOperations
 * 
 */

public interface LogAdapter {
	public void createDB();

	public boolean openDB();

	public void insertEntry();

	public void deleteEntry();

	public void addList(LogEntry log);
	
	public Cursor getInformation();
	
	public List<LogEntry> getDatabase();

	public List<LogEntry> getList();

	public void close();


}
