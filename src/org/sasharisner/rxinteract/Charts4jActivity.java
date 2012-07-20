package org.sasharisner.rxinteract;

//This Activity populates the charts4jlayout with the selected drug data and effects 

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
	      	
	        //since java doesn't allow pass by reference, doing it through an atomicreferenceobject
	        AtomicReference<Object> sEffects = new AtomicReference<Object>("");
	        
	        //call the chart class to get the chart url and the effects string
	        String sURL = Charts4jScatterChart.getChartData(this, sEffects);

	  	        	       
	        if (sURL != "")
	        {
	        	//this is where the chart is populated with the URL
	            WebView webView;	        
		        webView = (WebView)findViewById(R.id.wvChart);
	        	webView.loadUrl(sURL);

		    }
	        else
	        {
	        	//there are no adverse effects so set the string
	        	sEffects.set("No adverse events.");
	        }
	        
	        //show the adverse effects in the edittext box
	        EditText editText;	        
		    editText = (EditText)findViewById(R.id.txtEffect);
		    editText.setText(sEffects.get().toString());
		    editText.setEnabled(false);
	    }
	}
