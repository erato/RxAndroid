package org.sasharisner.rxinteract;


import android.app.Activity;
import android.content.Intent;
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
 * The main activity for RxInteraction
 * Displays the autocompletedropdown with a list of drugs and displays a list of selections below the drop-down
 */

public class RxInteract extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	
    	//clear the selected drug list
    	clearList();
    	
    	//populate the autocompletetext box
    	setupAutoComplete();    	
    }
    

    //this gets the drugs available in the db and populates the drop-down
    private void setupAutoComplete(){
    	Log.i(this.toString(), "setupAutoComplete");
    	
    	//call the db helper, open the db, and populate a new string array of drugs
    	DBHelper db = new DBHelper(this);
    	db.open();    	
    	String[] sDrugs = db.getAllDrugs();
    	db.close();
    	
    	//create an arrayadapter from the string array of drugs
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sDrugs);         
        final AutoCompleteTextView autoDrug;
        autoDrug = (AutoCompleteTextView)
                 findViewById(R.id.autocompleteDrugs);  
       
        //set the drop-down the array adapter
        autoDrug.setAdapter(adapter);
        
        //set the search hint to the string.xml
        autoDrug.setCompletionHint(getString(R.string.search_hint));
        
    	 // Define the on-click listener for the list items
        autoDrug.setOnItemClickListener(new OnItemClickListener() {

        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {            
                // determine the selected item and add it to the drug selection list, then display it below the drop-down
        	 	DBHelper dbI = new DBHelper(getApplicationContext());
        	    dbI.open();
        	    String sDrug = adapter.getItemAtPosition(position).toString();
        	    dbI.InsertDrugSelection(sDrug);
        	    dbI.close();
        	    refreshDrugList();
        	    
        	    //now reset the drop-down so the user doesn't have to clear it
        	    autoDrug.setText("");
        	    autoDrug.clearListSelection();
            }
        });

    }
    
    //show the selected drugs in the list
    public void refreshDrugList()
    {
    	Log.i(this.toString(), "refreshDrugList");

    	//get the list of selections from the db class
 		DBHelper db = new DBHelper(this);			
 		db.open();
 		String[] sDrugs = db.getDrugSelection();
 		db.close();
 		
 		//create an array adapter from the results
 		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
 				android.R.layout.simple_dropdown_item_1line, sDrugs);
 		
 		//link the listview with the arrayadapter
    	ListView rxListView = (ListView)findViewById(R.id.listDrug);		   

 		// Bind to our new adapter.
 		rxListView.setAdapter(adapter); 		 		
     	Button btnView = (Button)findViewById(R.id.btnView);        	

     	//if we have more than 2 drugs selected, show the button for interactions
 		if (sDrugs.length >= 2)
        {
         	btnView.setVisibility(View.VISIBLE);
         	
         	// Perform action on click
            btnView.setOnClickListener(new View.OnClickListener() {
                 public void onClick(View btnView) {
                    
                 	//new intent to the chart screen                	                 	
                 	Intent listIntent = new Intent(getApplicationContext(), Charts4jActivity.class);                
                 	startActivity(listIntent);
                 } 	      
             	
             });
        }
 		else{
 			//we don't have enough selections so hide the interactions button
 			btnView.setVisibility(View.GONE);
 		}
    }
    
    //create the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
       	inflater.inflate(R.menu.rxlistmenu, menu);
       	return true;
    }
     
    //this handles the click event of the clear menu button to clear the selections
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
   	
   	//this clears the list of selections
   	private void clearList(){
    	Log.i(this.toString(), "clearList");

 		DBHelper db = new DBHelper(this);			
   		db.open();
    	db.ClearDrugList();
    	db.close();  
    }

   	
    
}
