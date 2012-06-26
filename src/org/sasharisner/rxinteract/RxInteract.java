package org.sasharisner.rxinteract;


import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter;

/**
 * The main activity for the dictionary.
 * Displays search results triggered by the search dialog and handles
 * actions from search suggestions.
 */

public class RxInteract extends Activity {
	private Context cContextHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      cContextHelper = this;
      setupAutoComplete();
    }
    

    private void setupAutoComplete(){
    	Log.i(this.toString(), "setupAutoComplete");
    	DBHelper db = new DBHelper(cContextHelper);

    	db.open();
    	db.ClearDrugList();
    	String[] sDrugs = db.getAllDrugs();
        
         ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                 android.R.layout.simple_dropdown_item_1line, sDrugs);
         
         AutoCompleteTextView autoDrug;
         autoDrug = (AutoCompleteTextView)
                 findViewById(R.id.autocompleteDrugs);       
         autoDrug.setAdapter(adapter);
         db.close();
         autoDrug.setCompletionHint(getString(R.string.search_hint));
        
    	  // Define the on-click listener for the list items
         autoDrug.setOnItemClickListener(new OnItemClickListener() {

         public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {            
                // Build the Intent used to open WordActivity with a specific word Uri
        	 	DBHelper dbI = new DBHelper(cContextHelper);
        	    dbI.open();
        	    String sDrug = adapter.getItemAtPosition(position).toString();
        	    dbI.InsertDrugSelection(sDrug);
        	    dbI.close();
        	 
                Intent listIntent = new Intent(getApplicationContext(), RxList.class);                
                startActivity(listIntent);
            }
        });

    	
    
    }
    
}
