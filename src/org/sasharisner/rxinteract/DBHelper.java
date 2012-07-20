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

//this class queries and copies the sqlite database
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
        
        //get the db name and version from strings.xml
        DB_NAME = context.getString(R.string.DB_NAME);
   	   	DB_VERSION = Integer.parseInt(context.getString(R.string.DB_VERSION_NUMBER));
    }

    //let's open or create the db
    public DBHelper open() throws SQLException {
        mDbHelper = new DatabaseHelper(adapterContext);

    	Log.i(this.toString(), "DBHelper open");

    	//first let's try to create the database
        try {
            mDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        //now let's open it
        try {
            mDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        return this;
    }
 
    //get all the drugs for the autocompletelist box
    public String[] getAllDrugs() throws SQLException
    {
    	Log.i(this.toString(), "getAllDrugs");

    	String query = "SELECT DISTINCT drug1 as drug FROM tRxInteract " + 
    		" UNION " +
			" SELECT DISTINCT drug2 as drug from tRxInteract " + 
			" order by drug";
    	
    	try   	{
    		//let's open this cursor to query the database
    		Cursor cDrugs = mDb.rawQuery(query, null);
    		
    		//if we have drugs, then populate the string array of drubs
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
    	        	//if there are none, return an empty string array
    	            return new String[] {};
    	        }
    	} catch (SQLException eSQL) {
            throw new Error("Unable to get drug listing");
        }
    	
       
    }
    
    
    //this is called when a user selects a drug from the drop-down list
    public void InsertDrugSelection(String sDrug) throws SQLException {
    	
        String command = "INSERT INTO tDrugList VALUES ('" + sDrug + "')";
        
        try{
        	//insert the drug name into the table that keeps a listing of drugs selected
        	mDb.execSQL(command);
        }
	    catch (SQLException sqle) {
	        throw new Error("Unable to insert selection into selection table.");
	    }
    }

    //this returns the list of selected drugs
    public String[] getDrugSelection() throws SQLException
    {
    	Log.i(this.toString(), "getDrugSelection");

    	String query = "SELECT DISTINCT drug FROM tDrugList";
    	
    	try   	{
    		Cursor cDrugs = mDb.rawQuery(query, null);
    		
    		//if there are drugs selected, then return them in a string array
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
    	        	//if there are none, return an empty string array
    	            return new String[] {};
    	        }
    	} catch (SQLException eSQL) {
            throw new Error("Unable to get drug listing");
        }
    	
       
    }
    
    //this returns the list of drug effects, likelihoods, and severities, along with the selected drug combinations
    public Drug[] getDrugEffects() throws SQLException{
    	Log.i(this.toString(), "getDrugEffects");
    	
    	String sQuery = "select distinct drug1, drug2, event_name, proportion_reporting_ratio AS likelihood, observed * 100.0 AS severity " +
    			" from tRxInteract WHERE EXISTS (SELECT Null " +
    			"	FROM tDrugList d1 INNER JOIN tDrugList d2 " +
    			"	WHERE (d1.Drug = drug1 AND d2.Drug = drug2) " +
    			"		OR  (d1.Drug = drug2 AND d2.Drug = drug1)) " +
    			"    AND event_name <> 'ADVERSE DRUG EFFECT' " +    	
    			" ORDER BY drug1, drug2";
		
    	try{
    		
    		//this drug class stores all the drug information
    		Drug[] drugs = null;
    		Cursor cEffects = mDb.rawQuery(sQuery, null);

    		int iRowCount;
    		iRowCount = cEffects.getCount();
    		int i;

    		//if we have drug interactions
    		if(iRowCount > 0)
 	        {
        		
    			//let's add one to our array so we can populated it
            	drugs = new Drug[iRowCount];
 	            i = 0;
           	   	Log.i(this.toString(), "DrugMax=" + drugs.length);
 	 
           	   	//loop through the db cursor
 	            while (cEffects.moveToNext())
 	            {
 	            	
 	           	   	Log.i(this.toString(), "index=" + i);
 	           	   	
 	           	   	//pass the db info to the drug class and create a new drug array item
 	            	drugs[i] = new Drug(cEffects.getString(cEffects.getColumnIndex("drug1")),
 	            			cEffects.getString(cEffects.getColumnIndex("drug2")), 
 	            			cEffects.getString(cEffects.getColumnIndex("event_name")),
 	            			cEffects.getDouble(cEffects.getColumnIndex("likelihood")),
 	            			cEffects.getDouble(cEffects.getColumnIndex("severity")));
 	            	
 	                 i++;
 	             }
 	        }

    		//return this drug class
            return drugs;

	    } catch (SQLException eSQL) {
	        throw new Error("Unable to get drug effects.");
	    }
				    	
    }
    
    public void close() {
    	Log.i(this.toString(), "Close");

    	//close the database
        mDbHelper.close();
    }

    //this clears the list of selected drugs
    public void ClearDrugList()
    {
    	Log.i(this.toString(), "ClearDrugList");

    	//just clear the table
        String command = "DELETE FROM tDrugList";
        mDb.execSQL(command);
    }
    
    //this helper copies the sqlite db file from the assets folder into the application directory for use in the app
    private static class DatabaseHelper extends SQLiteOpenHelper {

        Context helperContext;

        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            helperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        //called when the database is upgraded to a new version
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database!!!!!");
            //db.execSQL("");
            onCreate(db);
        }

        //checks if the db exists and copies it to the app directory if doesn't exit
        public void createDataBase() throws IOException {
            boolean dbExist = checkDataBase();
            if (dbExist) {
            } else {
                try {
                    copyDataBase();
                } catch (IOException e) {
                	//delete the database if the copy failed so to prevent errors
                	File f = new File(DB_PATH + DB_NAME);
                    if (f.exists()) {
                    	f.delete();
                    }
                    throw new Error("Error copying database");
                }
            }
        }

        //gets the database for use
        public SQLiteDatabase getDatabase() {
            String sPath = DB_PATH + DB_NAME;
            return SQLiteDatabase.openDatabase(sPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        }

        //check if the database exists
        private boolean checkDataBase() {
        	String sPath = DB_PATH + DB_NAME;
        	return new File(sPath).exists();
        }

        //this function copies the database
        private void copyDataBase() throws IOException {

            // Open your local db as the input stream        	
        	String sAssetName = DB_NAME; 
        	
        	//create an input stream to the database in the assets folder
            InputStream myInput = helperContext.getAssets().open(sAssetName);

            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;

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

        //this function opens the database or calls the function to create it if it doesn't exist
        public void openDataBase() throws SQLException {
            // Open the database
            String sPath = DB_PATH + DB_NAME;
            mDb = SQLiteDatabase.openDatabase(sPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        }

        //close the database
        @Override
        public synchronized void close() {

            if (mDb != null)
                mDb.close();

            super.close();

        }
    }

}
