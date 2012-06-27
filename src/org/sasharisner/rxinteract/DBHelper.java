package org.sasharisner.rxinteract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper {

    private static final String TAG = "DBHelper";
    private DatabaseHelper mDbHelper;
    private static SQLiteDatabase mDb;
    private static String DB_PATH = "/data/data/org.sasharisner.rxinteract/databases/";
    private static String DB_NAME;
	private static Integer DB_VERSION;

    private final Context adapterContext;

    public DBHelper(Context context) {
        this.adapterContext = context;
        
        DB_NAME = context.getString(R.string.DB_NAME);
   	   	DB_VERSION = Integer.parseInt(context.getString(R.string.DB_VERSION_NUMBER));
    }

    public DBHelper open() throws SQLException {
        mDbHelper = new DatabaseHelper(adapterContext);

    	Log.i(this.toString(), "DBHelper open");

        try {
            mDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            mDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        return this;
    }
 
    public Cursor ExampleSelect(String myVariable)
    {
        String query = "SELECT locale, ? FROM android_metadata";
        return mDb.rawQuery(query, new String[]{myVariable});
    }
    
    public String[] getAllDrugs() throws SQLException
    {
    	Log.i(this.toString(), "getAllDrugs");

    	String query = "SELECT DISTINCT drug1 as drug FROM tRxInteract " + 
    		" UNION " +
			" SELECT DISTINCT drug2 as drug from tRxInteract " + 
			" order by drug";
    	
    	try   	{
    		Cursor cDrugs = mDb.rawQuery(query, null);
    		
    		 if(cDrugs.getCount() >0)
    	        {
    	            String[] str = new String[cDrugs.getCount()];
    	            int i = 0;
    	 
    	            while (cDrugs.moveToNext())
    	            {
    	                 str[i] = cDrugs.getString(cDrugs.getColumnIndex("drug"));
    	                 i++;
    	             }
    	            return str;
    	        }
    	        else
    	        {
    	            return new String[] {};
    	        }
    	} catch (SQLException eSQL) {
            throw new Error("Unable to get drug listing");
        }
    	
       
    }
    
    public void InsertDrugSelection(String sDrug) throws SQLException {
        String command = "INSERT INTO tDrugList VALUES ('" + sDrug + "')";
        
        try{
        	mDb.execSQL(command);
        }
	    catch (SQLException sqle) {
	        throw new Error("Unable to insert selection into selection table.");
	    }
    }

    public String[] getDrugSelection() throws SQLException
    {
    	Log.i(this.toString(), "getDrugSelection");

    	String query = "SELECT DISTINCT drug FROM tDrugList";
    	
    	try   	{
    		Cursor cDrugs = mDb.rawQuery(query, null);
    		
    		 if(cDrugs.getCount() >0)
    	        {
    	            String[] str = new String[cDrugs.getCount()];
    	            int i = 0;
    	 
    	            while (cDrugs.moveToNext())
    	            {
    	                 str[i] = cDrugs.getString(cDrugs.getColumnIndex("drug"));
    	                 i++;
    	             }
    	            return str;
    	        }
    	        else
    	        {
    	            return new String[] {};
    	        }
    	} catch (SQLException eSQL) {
            throw new Error("Unable to get drug listing");
        }
    	
       
    }
    
    public String[] getDrugEffects() throws SQLException{
    	Log.i(this.toString(), "getDrugEffects");
    	
    	String[] sDrugs = getDrugSelection();
    	
    	String query = "select distinct drug1, drug2, event_name from tRxInteract WHERE ";
    	
    	int i = 0; 
    	
    	while (i < sDrugs.length)
    	{
    		if (i > 0)
    			query = query + " OR ";
    			
    		query = query + "(drug1 = '" + sDrugs[i] + "' or drug2 = '" + sDrugs[i] + "')";
    		i++;
    	}
    		
    	//query = query + " order by expected";
		
    	try{
    		Cursor cEffects = mDb.rawQuery(query, null);
    		
    		if(cEffects.getCount() >0)
 	        {
 	            String[] str = new String[cEffects.getCount()];
 	            i = 0;
 	 
 	            while (cEffects.moveToNext())
 	            {
 	                 str[i] = cEffects.getString(cEffects.getColumnIndex("event_name"));
 	                 i++;
 	             }
 	            return str;
 	        }
 	        else
 	        {
 	            return new String[] {};
 	        }
    		 
	    } catch (SQLException eSQL) {
	        throw new Error("Unable to get drug effects.");
	    }
				
    	
    }
    
    public void close() {
    	Log.i(this.toString(), "Close");

        mDbHelper.close();
    }

    public void ClearDrugList()
    {
    	Log.i(this.toString(), "ClearDrugList");

        String command = "DELETE FROM tDrugList";
        mDb.execSQL(command);
    }
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        Context helperContext;

        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            helperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database!!!!!");
            //db.execSQL("");
            onCreate(db);
        }

        public void createDataBase() throws IOException {
            boolean dbExist = checkDataBase();
            if (dbExist) {
            } else {
                try {
                    copyDataBase();
                } catch (IOException e) {
                	File f = new File(DB_PATH + DB_NAME);
                    if (f.exists()) {
                    	f.delete();
                    }
                    throw new Error("Error copying database");
                }
            }
        }

        public SQLiteDatabase getDatabase() {
            String sPath = DB_PATH + DB_NAME;
            return SQLiteDatabase.openDatabase(sPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        }

        private boolean checkDataBase() {
        	String sPath = DB_PATH + DB_NAME;
        	return new File(sPath).exists();
        }

        private void copyDataBase() throws IOException {

            // Open your local db as the input stream
        	
        	String sAssetName = DB_NAME; //.replace(".db", ".jpeg");
        	
            InputStream myInput = helperContext.getAssets().open(sAssetName);

            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;

            //TODO:  If this fails, delete this database..
            this.getReadableDatabase();
            File f = new File(DB_PATH);
            if (!f.exists()) {
            	f.mkdir();
            }
            
            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }

        public void openDataBase() throws SQLException {
            // Open the database
            String sPath = DB_PATH + DB_NAME;
            mDb = SQLiteDatabase.openDatabase(sPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        }

        @Override
        public synchronized void close() {

            if (mDb != null)
                mDb.close();

            super.close();

        }
    }

}
