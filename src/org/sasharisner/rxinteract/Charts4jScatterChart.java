package org.sasharisner.rxinteract;

import static com.googlecode.charts4j.UrlUtil.normalize;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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

		//the theory was to alternate random shapes with colors for each of the different drug interaction combinations.  however, this chart doesn't support different shapes or colors
	 	private static Shape[] aShape = {Shape.CIRCLE, Shape.ARROW, Shape.CROSS, Shape.DIAMOND, 
	 			Shape.HORIZONTAL_LINE, Shape.SQUARE, Shape.VERTICAL_LINE_FULL, Shape.VERTICAL_LINE_PARTIAL};
	 	
	 	private static Color[] aColor = {Color.RED, Color.WHITE, Color.BLACK, Color.AQUA, Color.BLUE, Color.CHOCOLATE, Color.CORAL, Color.DARKBLUE, 
	 			Color.DARKGREEN, Color.DARKMAGENTA, Color.GREEN, Color.GOLD, Color.LAVENDER, Color.LIGHTBLUE, Color.LIGHTGREEN,
	 			Color.PINK, Color.PURPLE, Color.RED, Color.TAN, Color.YELLOW};
	 	
	 	//this function returns the chart URL and the effects string
	    public static String getChartData(Context cHelper, AtomicReference<Object> sEffects, int iheight, int iwidth) {
	    	Log.i("Charts", "getChartData");
	        
	    	//get the data for the chart from the database helper class
	    	//select the different drugs from the different interactions
	    	//select drug1, drug2, effect, severity, & likelihood	    	
	    	DBHelper db = new DBHelper(cHelper);
	    	db.open();	    
	    	Drug[] drugs = db.getDrugEffects();
	    	db.close();
	    		 
	    	//there are no drug interactions, so return
	    	if (drugs == null)
	    			return "";
	    	
	    	int i = 0;
	    	double dMaxX = 0;
	    	double dMaxY = 0;
	    	
	    	ScatterPlot chart = null;
	    	
	    	String sLastDrugName = "";
	    	String sCurrDrugName = "";
	    	String sLocalEffects = "ADVERSE DRUG EVENTS:\n";
	    	
    		//Random r = new Random();
	    	//int iShape = 0;
	    	//int iColor = 0;
	    	String sEffect = "";
	    	double dLikelihood = 0;
	    	double dSeverity = 0;
	    	
	    	Data dX = null;
	    	Data dY = null;
	    	
	    	Data dPointSize = null;
	    	Integer iDrugCnt = 0;
	    	ScatterPlotData plot = null;
	    	
	    	//x data list, y data list, name data lise, and size data list
	    	List<Number> lX = new ArrayList<Number>();	    	
	    	List<Number> lY = new ArrayList<Number>();
	    	List<String> lName = new ArrayList<String>();
	    	List<Number> lSize = new ArrayList<Number>();
	    	
	    	
	    	while (i < drugs.length)
            {
	    		//change the current drug name to the combination of drug1 - drug2
	        	sCurrDrugName =  drugs[i].getDrug1()  + " - " + drugs[i].getDrug2();
	        	
	        	//new Drug-Drug combination
	        	if (!sLastDrugName.equals(sCurrDrugName))
	        	{
	        		iDrugCnt++;
	        		
	        		//get a random shape and a random color
	        		//iShape = r.nextInt(aShape.length);
	        		//iColor = r.nextInt(aColor.length);
	        		
	        		//add a line break before the line if we are after the first drug combination
	        		if (iDrugCnt > 1)
	        			sLocalEffects = sLocalEffects + "\n";
	        		
	        		//add the effects to the string listing, with the order number
	        		sLocalEffects = sLocalEffects + iDrugCnt.toString() + ") " + sCurrDrugName + "\n";	        	
	        	}
	        	else if (i != (drugs.length-1))
	        	{
	        		sLocalEffects = sLocalEffects + ", ";
	        	}

	        	//get the likelihood, severity, and effect from the class arrays
	        	dLikelihood = drugs[i].getLikelihood();
	        	dSeverity = drugs[i].getSeverity();
	        	sEffect = drugs[i].getEffect();
	        	
	        	//add the effect to the effect string
	        	sLocalEffects = sLocalEffects + sEffect;	        	

	        	//add the x & y coordinates to the chart list
	        	lX.add(dLikelihood);
	        	lY.add(dSeverity);
	        	
	        	//reset the max if the x or y values are greater than the max
	        	if (dLikelihood > dMaxX)
	        		dMaxX = dLikelihood;
	        	
	        	if (dSeverity > dMaxY)
	        		dMaxY = dSeverity;
	        		
	        	//set the chart plot size equal to the likelihood (adds some interesting contrast)
	        	lSize.add(dLikelihood);
	        	
	        	//add the current drug name to the name list
	        	lName.add(sCurrDrugName);
	        	
		        //now set the last drug name to the current drug so we can determine if we're onto a new drug-drug combination
		    	sLastDrugName = sCurrDrugName;		        
                i++;
             }
	    	    
	    	//finally, add the lists to x & y & point size data types
	        dX = Data.newData(lX);
	        dY = Data.newData(lY);	        
	        dPointSize = Data.newData(lSize);		    	
	        
	        //create a new scatter point with all the plots
	    	plot = Plots.newScatterPlotData(dX, dY, dPointSize);
			
	    	//set the legend name
	        plot.setLegend("Events");
	        //set the color of the plot points to the first in the color array
	        Color cPlot = aColor[0];
	        //set the shape of the plot points to the first in the shape array
	        plot.addShapeMarkers(aShape[0], cPlot, 30);
	        //set the color of the plot points
	        plot.setColor(cPlot);

	        //create the chart 
	        chart = GCharts.newScatterPlot(plot);	
	        //set the chart size and the grid sizes
	        chart.setSize(iwidth, iheight);
	        chart.setGrid(20, 20, 3, 2);

	        //label and color the x axis
	        AxisLabels axisLabelsX = AxisLabelsFactory.newNumericRangeAxisLabels(0, dMaxX + 1);
	        axisLabelsX.setAxisStyle(AxisStyle.newAxisStyle(Color.WHITE, 12, AxisTextAlignment.CENTER));

	        //label and color the y axis
	        AxisLabels axisLabelsY = AxisLabelsFactory.newNumericRangeAxisLabels(0, dMaxY + 1);
	        axisLabelsX.setAxisStyle(AxisStyle.newAxisStyle(Color.WHITE, 12, AxisTextAlignment.CENTER));

	        //now add the x and y axis labels to the chart
	        chart.addXAxisLabels(axisLabelsX);
	        chart.addYAxisLabels(axisLabelsY);

	        //set the title of the chart, the background color, fill the gradient background, and the area color around the chart	        
	        chart.setTitle("Drug Interactions", Color.WHITE, 18);
	        chart.setBackgroundFill(Fills.newSolidFill(Color.newColor("4e4e4e")));
	        LinearGradientFill fill = Fills.newLinearGradientFill(0, Color.newColor("9B7FFF"), 100);
	        fill.addColorAndOffset(Color.newColor("9c9c9c"), 0);
	        chart.setAreaFill(fill);
	        //let's get this url of the chart
	        String url = chart.toURLString();

	        //set the reference string to the effects string we've built here
	        sEffects.set(sLocalEffects);
	        
	        //return the url so we can display the chart
	        return normalize( url );
	    }

}
