/**
 * Copyright (C) 2009, 2010 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sasharisner.rxinteract;


import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

public class GeneratedChartDemo extends ListActivity {
  private static final int SERIES_NR = 2;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
 
    Intent intent = ChartFactory.getScatterChartIntent(this, getDemoDataset(), getDemoRenderer());
    
    startActivity(intent);
  }


  private XYMultipleSeriesDataset getDemoDataset() {
    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    final int nr = 10;
    Random r = new Random();
    for (int i = 0; i < SERIES_NR; i++) {
      XYSeries series = new XYSeries("Demo series " + (i + 1));
      for (int k = 0; k < nr; k++) {
        series.add(k, 20 + r.nextInt() % 100);
      }
      dataset.addSeries(series);
    }
    return dataset;
  }

  private XYMultipleSeriesRenderer getDemoRenderer() {
    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
    renderer.setAxisTitleTextSize(16);
    renderer.setChartTitleTextSize(20);
    
    renderer.setLabelsTextSize(15);
    renderer.setLegendTextSize(15);
    renderer.setPointSize(5f);
    renderer.setAxesColor(Color.WHITE);
    
    setChartSettings(renderer, this.getString(R.string.chart_title), this.getString(R.string.x_label), 
    		this.getString(R.string.y_label), 0,
            1, 0, 10, Color.BLACK, Color.BLACK, Color.WHITE);
    
    renderer.setMargins(new int[] {20, 30, 15, 0});
    XYSeriesRenderer r = new XYSeriesRenderer();
    r.setColor(Color.RED);
    r.setPointStyle(PointStyle.SQUARE);
    r.setFillBelowLine(true);
    r.setFillBelowLineColor(Color.WHITE);
    r.setFillPoints(true);
    renderer.addSeriesRenderer(r);
    r = new XYSeriesRenderer();
    r.setPointStyle(PointStyle.CIRCLE);
    r.setColor(Color.YELLOW);
    r.setFillPoints(true);
    renderer.addSeriesRenderer(r);
    return renderer;
  }

  
  protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
	      String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
	      int labelsColor, int backcolor) {
	    renderer.setChartTitle(title);
	    renderer.setXTitle(xTitle);
	    renderer.setYTitle(yTitle);
	    renderer.setXAxisMin(xMin);
	    renderer.setXAxisMax(xMax);
	    renderer.setYAxisMin(yMin);
	    renderer.setYAxisMax(yMax);
	    renderer.setAxesColor(axesColor);
	    renderer.setLabelsColor(labelsColor);
	    renderer.setBackgroundColor(backcolor);
	    renderer.setApplyBackgroundColor(true);
	    //renderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
  }
}
