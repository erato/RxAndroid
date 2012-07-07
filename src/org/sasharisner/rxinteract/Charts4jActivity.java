package org.sasharisner.rxinteract;

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
	    	
	        WebView webView;	        
	        webView = (WebView)findViewById(R.id.wvChart);
	        webView.loadUrl(Charts4jScatterChart.getChartData(this) );
	        	        
	        //String sEffects = "";
	        
	        //DBHelper db = new DBHelper(this);
	    	//db.open();
	    	//sEffects = db.getDrugEffects();
	    	//db.close();
	    	
	        //EditText editText;	        
	        //editText = (EditText)findViewById(R.id.txtEffect);
	        //editText.setText(sEffects);	        	       	        
	    }
	}
