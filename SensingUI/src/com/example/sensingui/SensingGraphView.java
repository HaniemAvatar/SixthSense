package com.example.sensingui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class SensingGraphView extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
	Context mContext;
    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private Runnable mTimer2;
    private GraphView graphView;
    private GraphViewSeries exampleSeries1;
    private GraphViewSeries exampleSeries2;
    private double graph2LastXValue = 10d;
    private GraphViewSeries exampleSeries3;
    private String graphType = "line";
    private int i;
    private double[] graph1data = {0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d};
    private double[] graph2data = {0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d};
    SensorManager sm;
	Sensor light_sensor;
	int LightBulb;
    
    private double getRandom() {
        double high = 3;
        double low = 0.5;
        return Math.random() * (high - low) + low;
    }
 
    public static final String ARG_SECTION_NUMBER = "section_number";

    public SensingGraphView() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.graph_main, container, false);
        mContext=rootView.getContext();
        sm = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
        light_sensor= sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        exampleSeries1 = new GraphViewSeries(new GraphViewData[] {
                new GraphViewData(1, 0.0d)
                , new GraphViewData(2, 0.0d)
                , new GraphViewData(3, 0.0d)
                , new GraphViewData(4, 0.0d)
                , new GraphViewData(5, 0.0d)
                , new GraphViewData(6, 0.0d)
                , new GraphViewData(7, 0.0d)
                , new GraphViewData(8, 0.0d)
                , new GraphViewData(9, 0.0d)
                , new GraphViewData(10, 0.0d)
        });
        exampleSeries3 = new GraphViewSeries(new GraphViewData[] {});
        exampleSeries1.getStyle().color = 0XDF729FCF;
        exampleSeries3.getStyle().color = 0XDF729FCF;
 
         
        if (graphType.equalsIgnoreCase("bar")) {
            graphView = new BarGraphView(container.getContext(), "GraphViewDemo");
        } else {
            graphView = new LineGraphView(container.getContext(), "");
            ((LineGraphView) graphView).setDrawBackground(true);
            graphView.setBackgroundColor(0X55729FCF);
        }
        graphView.addSeries(exampleSeries1); 
        graphView.addSeries(exampleSeries3);
        graphView.setManualYAxisBounds(5,0);
        graphView.getGraphViewStyle().setNumVerticalLabels(6);
        graphView.getGraphViewStyle().setNumHorizontalLabels(1);
        graphView.getGraphViewStyle().setVerticalLabelsWidth(0);
        graphView.getGraphViewStyle().setVerticalLabelsAlign(Align.CENTER);
 
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.opgraph);
        layout.addView(graphView);
        // ----------
        exampleSeries2 = new GraphViewSeries(new GraphViewData[] {
                new GraphViewData(1, 0.0d)
                , new GraphViewData(2, 0.0d)
                , new GraphViewData(3, 0.0d)
                , new GraphViewData(4, 0.0d)
                , new GraphViewData(5, 0.0d)
                , new GraphViewData(6, 0.0d)
                , new GraphViewData(7, 0.0d)
                , new GraphViewData(8, 0.0d)
                , new GraphViewData(9, 0.0d)
                , new GraphViewData(10, 0.0d)
        });
 
     
        if (graphType.equalsIgnoreCase("bar")) {
            graphView = new BarGraphView(container.getContext(), "GraphViewDemo");
        } else {
            graphView = new LineGraphView(container.getContext(), "");        
            ((LineGraphView) graphView).setDrawBackground(true);
            graphView.setBackgroundColor(0X55729FCF);
        }
         
        graphView.addSeries(exampleSeries2);
        graphView.setViewPort(1, 8);
        graphView.setScalable(true);
        graphView.getGraphViewStyle().setGridColor(Color.BLACK);
        graphView.setScrollable(true);
 
        layout = (LinearLayout) rootView.findViewById(R.id.opgraph);
        layout.addView(graphView);
    return rootView;
    }
    @Override
	public void onPause() {
        mHandler.removeCallbacks(mTimer1);
        mHandler.removeCallbacks(mTimer2);
        super.onPause();
    }
 
    @Override
	public void onResume() {
        super.onResume();
        mTimer1 = new Runnable() {
            @Override
            public void run() {
                exampleSeries1.resetData(new GraphViewData[] {
                        new GraphViewData(1, graph1data[0])
                        , new GraphViewData(2, graph1data[1])
                        , new GraphViewData(3, graph1data[2])
                        , new GraphViewData(4, graph1data[3])
                        , new GraphViewData(5, graph1data[4])
                        , new GraphViewData(6, graph1data[5])
                        , new GraphViewData(7, graph1data[6])
                        , new GraphViewData(8, graph1data[7])
                        , new GraphViewData(9, graph1data[8])
                        , new GraphViewData(10, graph1data[9])                        
                });
                exampleSeries3.resetData(new GraphViewData[] {
                          new GraphViewData(1, graph2data[0])
                        , new GraphViewData(2, graph2data[1])
                        , new GraphViewData(3, graph2data[2])
                        , new GraphViewData(4, graph2data[3])
                        , new GraphViewData(5, graph2data[4])
                        , new GraphViewData(6, graph2data[5])
                        , new GraphViewData(7, graph2data[6])
                        , new GraphViewData(8, graph2data[7])
                        , new GraphViewData(9, graph2data[8])
                        , new GraphViewData(10, graph2data[9])   
                });
                mHandler.postDelayed(this, 600);
                for(i=0;i<9;i++){
                	graph1data[i]=graph1data[i+1];
                	graph2data[i]=graph2data[i+1];
                }
                sm.registerListener(listener, light_sensor, SensorManager.SENSOR_DELAY_NORMAL);
                graph1data[9]=Math.log10(LightBulb+1);
                graph2data[9]=Math.log10(LightBulb+1);
            }
        };
        mHandler.postDelayed(mTimer1, 600);
 
        mTimer2 = new Runnable() {
            @Override
            public void run() {
                graph2LastXValue += 1d;
                exampleSeries2.appendData(new GraphViewData(graph2LastXValue, getRandom()), true, 10);
                mHandler.postDelayed(this, 200);
            }
        };
        mHandler.postDelayed(mTimer2, 1000);
    }
    SensorEventListener listener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			LightBulb=(int)event.values[0];
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
		}
	};
}
