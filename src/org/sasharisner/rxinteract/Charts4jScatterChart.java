package org.sasharisner.rxinteract;

import static com.googlecode.charts4j.UrlUtil.normalize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
	
	    //8 shapes
	 	private static Shape[] aShape = {Shape.ARROW, Shape.CIRCLE, Shape.CROSS, Shape.DIAMOND, 
	 			Shape.HORIZONTAL_LINE, Shape.SQUARE, Shape.VERTICAL_LINE_FULL, Shape.VERTICAL_LINE_PARTIAL};
	 	
	 	//19 colors
	 	private static Color[] aColor = {Color.WHITE, Color.AQUA, Color.BLACK, Color.BLUE, Color.CHOCOLATE, Color.CORAL, Color.DARKBLUE, 
	 			Color.DARKGREEN, Color.DARKMAGENTA, Color.GREEN, Color.GOLD, Color.LAVENDER, Color.LIGHTBLUE, Color.LIGHTGREEN,
	 			Color.PINK, Color.PURPLE, Color.RED, Color.TAN, Color.YELLOW};
	 	
	    public static String getChartData(Context cHelper, AtomicReference<Object> sEffects) {
	    	Log.i("Charts", "getChartData");
	        
	    	//select the different drugs from the different interactions
	    	//select drug1, drug2, effect, severity, & likelihood
	    	
	    	DBHelper db = new DBHelper(cHelper);
	    	db.open();	    
	    	Drug[] drugs = db.getDrugEffects();
	    	db.close();
	    		    	
	    	int i = 0;
	    	double dMax = 0;	    	
	    	ScatterPlot chart = null;
	    	
	    	String sLastDrugName = "";
	    	String sCurrDrugName = "";
	    	String sLocalEffects = "<b>ADVERSE DRUG EVENTS</b>:\n";
	    	
    		Random r = new Random();
	    	int iShape = 0;
	    	//int iColor = 0;
	    	double dLikelihood = 0;
	    	double dSeverity = 0;
	    	String sEffect = "";
	    	
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
	        		//iColor = r.nextInt(aColor.length);
	        		
	        		if (iDrugCnt > 1)
	        			sLocalEffects = sLocalEffects + "\n";
	        		
	        		sLocalEffects = sLocalEffects + iDrugCnt.toString() + ") " + sCurrDrugName + "\n";	        	
	        	}
	        	else if (i != (drugs.length-1))
	        	{
	        		sLocalEffects = sLocalEffects + ", ";
	        	}

	        	
	        	dLikelihood = drugs[i].getLikelihood();
	        	dSeverity = drugs[i].getSeverity();
	        	sEffect = drugs[i].getEffect();
	        	
	        	
	        	sLocalEffects = sLocalEffects + drugs[i].getEffect();	        	

	        	lX.add(dLikelihood);
	        	lY.add(dSeverity);
	        	
	        	if (dLikelihood > dMax)
	        		dMax = dLikelihood;
	        	
	        	if (dSeverity > dMax)
	        		dMax = dSeverity;
	        		
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
	        Color cPlot = aColor[1];
	        plot.addShapeMarkers(aShape[iShape], cPlot, 30);
	        plot.setColor(cPlot);

	        chart = GCharts.newScatterPlot(plot);
		       
	        
	        chart.setSize(300, 329);
	        chart.setGrid(20, 20, 3, 2);

	        AxisLabels axisLabels = AxisLabelsFactory.newNumericRangeAxisLabels(0, dMax + 1);
	        axisLabels.setAxisStyle(AxisStyle.newAxisStyle(Color.WHITE, 13, AxisTextAlignment.CENTER));

	        chart.addXAxisLabels(axisLabels);
	        chart.addYAxisLabels(axisLabels);

	        chart.setTitle("Drug Interactions", Color.WHITE, 16);
	        chart.setBackgroundFill(Fills.newSolidFill(Color.newColor("4e4e4e")));
	        LinearGradientFill fill = Fills.newLinearGradientFill(0, Color.newColor("9B7FFF"), 100);
	        fill.addColorAndOffset(Color.newColor("FFFFFF"), 0);
	        chart.setAreaFill(fill);
	        String url = chart.toURLString();
	       	        	       
	        sEffects.set(sLocalEffects);
	        return normalize( url );
	    }

}
