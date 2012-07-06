package org.sasharisner.rxinteract;

import android.app.Activity;
import android.os.Bundle;

public class ChartActivity extends Activity {

	  
	  CustomDrawableView mCustomDrawableView;

	  protected void onCreate(Bundle savedInstanceState) {
	 	 super.onCreate(savedInstanceState);
	 	 mCustomDrawableView = new CustomDrawableView(this);

	 	 setContentView(mCustomDrawableView);
	  }
	  

}
