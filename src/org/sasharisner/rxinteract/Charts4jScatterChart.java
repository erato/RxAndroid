package org.sasharisner.rxinteract;

import static com.googlecode.charts4j.Color.WHITE;
import static com.googlecode.charts4j.UrlUtil.normalize;

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

	    public static String getChartData() {
	        
	    	//x data
	        Data d1 = Data.newData(10, 50, 30, 45, 65, 95, 20, 80);
	        
	        //y data
	        Data d2 = Data.newData(20, 40, 40, 15, 85, 95, 80, 20);
	        Data pointSizes = Data.newData(100, 30, 50, 75, 40, 35, 80, 100);
	        ScatterPlotData data = Plots.newScatterPlotData(d1, d2, pointSizes);
	        data.setLegend("Diamond");
	        Color diamondColor = Color.newColor("FF471A");
	        data.addShapeMarkers(Shape.DIAMOND, diamondColor, 30);
	        data.setColor(diamondColor);
	        ScatterPlot chart = GCharts.newScatterPlot(data);
	        chart.setSize(300, 329);
	        chart.setGrid(20, 20, 3, 2);

	        AxisLabels axisLabels = AxisLabelsFactory.newNumericRangeAxisLabels(0, 100);
	        axisLabels.setAxisStyle(AxisStyle.newAxisStyle(WHITE, 13, AxisTextAlignment.CENTER));

	        chart.addXAxisLabels(axisLabels);
	        chart.addYAxisLabels(axisLabels);

	        chart.setTitle("Likelihood", WHITE, 16);
	        chart.setBackgroundFill(Fills.newSolidFill(Color.newColor("2F3E3E")));
	        LinearGradientFill fill = Fills.newLinearGradientFill(0, Color.newColor("3783DB"), 100);
	        fill.addColorAndOffset(Color.newColor("9BD8F5"), 0);
	        chart.setAreaFill(fill);
	        String url = chart.toURLString();
	       
	        return normalize( url );
	    }

}
