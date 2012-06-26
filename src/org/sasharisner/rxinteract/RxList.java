package org.sasharisner.rxinteract;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

public class RxList extends Activity {

    private ListView rxListView;
    
	@Override
    protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		// We'll define a custom screen layout here (the one shown above), but
		// typically, you could just use the standard ListActivity layout.
		setContentView(R.layout.list_item);
		ListView rxListView = (ListView)findViewById(R.id.listDrug);
		   
		DBHelper db = new DBHelper(this);
			
		db.open();
		String[] sDrugs = db.getDrugSelection();
			
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, sDrugs);
		  
		// Bind to our new adapter.
		rxListView.setAdapter(adapter);
		    
		db.close();
        if (sDrugs.length >= 2)
        {
        	Button btnView = (Button)findViewById(R.id.btnView);        	
        	btnView.setVisibility(View.VISIBLE);
        	
            btnView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View btnView) {
                    // Perform action on click
                	//new intent to the chart screen
                }
            });
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
	            return true;
	        case R.id.itemSearch:
	            showSearch();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void showSearch()
	{
		Intent searchIntent = new Intent(getApplicationContext(), RxInteract.class);
        startActivity(searchIntent);
	}
	
	private void clearList(){
		//rxListView.setAdapter(null);
	}

	public ListView getRxListView() {
		return rxListView;
	}

	public void setRxListView(ListView rxListView) {
		this.rxListView = rxListView;
	}
}
