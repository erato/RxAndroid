package org.sasharisner.rxinteract;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * The main activity for the dictionary.
 * Displays search results triggered by the search dialog and handles
 * actions from search suggestions.
 */

public class RxInteract extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	
    	clearList();
    	setupAutoComplete();    	
    }
    

    private void setupAutoComplete(){
    	Log.i(this.toString(), "setupAutoComplete");
    	
    	DBHelper db = new DBHelper(this);
    	db.open();
    	
    	String[] sDrugs = db.getAllDrugs();        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sDrugs);         
        final AutoCompleteTextView autoDrug;
        autoDrug = (AutoCompleteTextView)
                 findViewById(R.id.autocompleteDrugs);  
       
        autoDrug.setAdapter(adapter);
        db.close();
        autoDrug.setCompletionHint(getString(R.string.search_hint));
        
    	 // Define the on-click listener for the list items
        autoDrug.setOnItemClickListener(new OnItemClickListener() {

        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {            
                // Build the Intent used to open WordActivity with a specific word Uri
        	 	DBHelper dbI = new DBHelper(getApplicationContext());
        	    dbI.open();
        	    String sDrug = adapter.getItemAtPosition(position).toString();
        	    dbI.InsertDrugSelection(sDrug);
        	    dbI.close();
        	    refreshDrugList();
        	    
        	    autoDrug.setText("");
        	    autoDrug.clearListSelection();
            }
        });

    }
    
    public void refreshDrugList()
    {
    	Log.i(this.toString(), "refreshDrugList");

 		DBHelper db = new DBHelper(this);			
 		db.open();
 		String[] sDrugs = db.getDrugSelection();
 			
 		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
 				android.R.layout.simple_dropdown_item_1line, sDrugs);
 		
    	ListView rxListView = (ListView)findViewById(R.id.listDrug);		   

 		// Bind to our new adapter.
 		rxListView.setAdapter(adapter); 		
 		db.close();
 		
     	Button btnView = (Button)findViewById(R.id.btnView);        	

 		if (sDrugs.length >= 2)
        {
         	btnView.setVisibility(View.VISIBLE);
         	
         	// Perform action on click
            btnView.setOnClickListener(new View.OnClickListener() {
                 public void onClick(View btnView) {
                    
                 	//new intent to the chart screen                	
                 	
                 	Intent listIntent = new Intent(getApplicationContext(), GeneratedChartDemo.class);                
                 	startActivity(listIntent);
                 } 	      
             	
             });
        }
 		else{
 			btnView.setVisibility(View.GONE);
 		}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
       	inflater.inflate(R.menu.rxlistmenu, menu);
       	return true;
    }
     
   	@Override
   	public boolean onOptionsItemSelected(MenuItem item) {
   	    // Handle item selection
   	    switch (item.getItemId()) {
   	        case R.id.itemClear:
   	            clearList();
   	            refreshDrugList();
   	            return true;
   	        default:
   	            return super.onOptionsItemSelected(item);
   	    }
   	}
   	
   	private void clearList(){
    	Log.i(this.toString(), "clearList");

 		DBHelper db = new DBHelper(this);			
   		db.open();
    	db.ClearDrugList();
    	db.close();  
    }

   	
    
}
