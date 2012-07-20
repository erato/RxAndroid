package org.sasharisner.rxinteract;

import java.util.concurrent.atomic.AtomicReference;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.EditText;

	public class Charts4jActivity extends Activity
	{
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate( Bundle savedInstanceState )
	    {
	        super.onCreate( savedInstanceState );
	        
	        setContentView(R.layout.charts4jlayout);
	      	        
	        AtomicReference<Object> sEffects = new AtomicReference<Object>("");
	        String sURL = Charts4jScatterChart.getChartData(this, sEffects);

	  	        	       
	        if (sURL != "")
	        {
	            WebView webView;	        
		        webView = (WebView)findViewById(R.id.wvChart);
	        	webView.loadUrl(sURL);
	        	        		        
		        //DBHelper db = new DBHelper(this);
		    	//db.open();
		    	//sEffects = db.getDrugEffects();
		    	//db.close();
		    	
			    
			    
		    }
	        else
	        {
	        	sEffects.set("No adverse events.");
	        }
	        
	        EditText editText;	        
		    editText = (EditText)findViewById(R.id.txtEffect);
		    editText.setText(sEffects.get().toString());
		    editText.setEnabled(false);
	    }
	}
