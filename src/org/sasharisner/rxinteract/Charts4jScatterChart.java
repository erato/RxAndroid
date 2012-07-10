package org.sasharisner.rxinteract;

import static com.googlecode.charts4j.UrlUtil.normalize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.googlecode.charts4j.AxisLabels;
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.AxisStyle;
import com.googlecode.charts4j.AxisTextAlignment;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.Data;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.LinearGradientFill;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.ScatterPlot;
import com.googlecode.charts4j.ScatterPlotData;
import com.googlecode.charts4j.Shape;

public class Charts4jScatterChart {
	
	    //8 shapes
	 	private static Shape[] aShape = {Shape.ARROW, Shape.CIRCLE, Shape.CROSS, Shape.DIAMOND, 
	 			Shape.HORIZONTAL_LINE, Shape.SQUARE, Shape.VERTICAL_LINE_FULL, Shape.VERTICAL_LINE_PARTIAL};
	 	
	 	//18 colors
	 	private static Color[] aColor = {Color.AQUA, Color.BLACK, Color.BLUE, Color.CHOCOLATE, Color.CORAL, Color.DARKBLUE, 
	 			Color.DARKGREEN, Color.DARKMAGENTA, Color.GREEN, Color.GOLD, Color.LAVENDER, Color.LIGHTBLUE, Color.LIGHTGREEN,
	 			Color.PINK, Color.PURPLE, Color.RED, Color.TAN, Color.YELLOW};
	 	
	    public static String getChartData(Context cHelper) {
	    	Log.i("Charts", "getChartData");
	        
	    	//select the different drugs from the different interactions
	    	//select drug1, drug2, effect, severity, & likelihood
	    	
	    	DBHelper db = new DBHelper(cHelper);
	    	db.open();	    
	    	Drug[] drugs = db.getDrugEffects();
	    	db.close();
	    		    	
	    	int i;
	    	i = 0;
	    	
	    	ScatterPlot chart = null;
	    	
	    	String sLastDrugName = "";
	    	String sCurrDrugName = "";
    		Random r = new Random();
	    	int iShape = 0;
	    	int iColor = 0;
	    	
	    	Data dX = null;
	    	Data dY = null;
	    	
	    	Data dPointSize = null;
	    	Integer iDrugCnt = 0;
	    	ScatterPlotData plot = null;
	    	
	    	List<Number> lX = new ArrayList<Number>();	    	
	    	List<Number> lY = new ArrayList<Number>();
	    	List<String> lName = new ArrayList<String>();
	    	List<Number> lSize = new ArrayList<Number>();
	    	
	    	while (i < drugs.length)
            {
	        	sCurrDrugName =  drugs[i].getDrug1()  + " - " + drugs[i].getDrug2();
	        	
	        	//new Drug-Drug combination
	        	if (!sLastDrugName.equals(sCurrDrugName))
	        	{
	        		iDrugCnt++;
	        		
	        		//get a random shape and a random color
	        		iShape = r.nextInt(aShape.length);
	        		iColor = r.nextInt(aColor.length);
	        	}

	        	lX.add(drugs[i].getLikelihood());
	        	lY.add(drugs[i].getSeverity());
	        	lSize.add(10);
	        	
	        	lName.add(sCurrDrugName);
	        	
		        //point sizes correspond with high severity/high likelihood
		    	sLastDrugName = sCurrDrugName;		        
                i++;
             }
	    	        	
	        dX = Data.newData(lX);
	        dY = Data.newData(lY);	        
	        dPointSize = Data.newData(lSize);		    	
	        
	    	plot = Plots.newScatterPlotData(dX, dY, dPointSize);
			
	        plot.setLegend("" + iDrugCnt);
	        Color diamondColor = aColor[iColor];
	        plot.addShapeMarkers(aShape[iShape], diamondColor, 30);
	        plot.setColor(diamondColor);

	        chart = GCharts.newScatterPlot(plot);
		       
	        chart.setSize(300, 329);
	        chart.setGrid(20, 20, 3, 2);

	        AxisLabels axisLabels = AxisLabelsFactory.newNumericRangeAxisLabels(0, 100);
	        axisLabels.setAxisStyle(AxisStyle.newAxisStyle(Color.WHITE, 13, AxisTextAlignment.CENTER));

	        chart.addXAxisLabels(axisLabels);
	        chart.addYAxisLabels(axisLabels);

	        chart.setTitle("Drug Interactions", Color.WHITE, 16);
	        chart.setBackgroundFill(Fills.newSolidFill(Color.newColor("2F3E3E")));
	        LinearGradientFill fill = Fills.newLinearGradientFill(0, Color.newColor("3783DB"), 100);
	        fill.addColorAndOffset(Color.newColor("9BD8F5"), 0);
	        chart.setAreaFill(fill);
	        String url = chart.toURLString();
	       
	        return normalize( url );
	    }

}
