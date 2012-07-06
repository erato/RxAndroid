package org.sasharisner.rxinteract;

	import android.app.Activity;
	import android.os.Bundle;
	import android.webkit.WebView;

	public class Charts4jActivity extends Activity
	{
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate( Bundle savedInstanceState )
	    {
	        super.onCreate( savedInstanceState );
	        WebView webView = new WebView( this );
	        setContentView( webView );
	        webView.loadUrl( ScatterChartExample.example1() );
	    }
	}
