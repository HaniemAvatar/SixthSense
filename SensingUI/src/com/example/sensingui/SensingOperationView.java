package com.example.sensingui;

import android.content.Context;
import android.graphics.Color;
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
public class SensingOperationView extends Fragment {
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
    private double graph2LastXValue = 5d;
    private GraphViewSeries exampleSeries3;
    private String graphType = "line";
    
    private double getRandom() {
        double high = 3;
        double low = 0.5;
        return Math.random() * (high - low) + low;
    }
 
    public static final String ARG_SECTION_NUMBER = "section_number";

    public SensingOperationView() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.operate_main, container, false);
        exampleSeries1 = new GraphViewSeries(new GraphViewData[] {
                new GraphViewData(1, 2.0d)
                , new GraphViewData(2, 1.5d)
                , new GraphViewData(3, 2.5d)
                , new GraphViewData(4, 1.0d)
                , new GraphViewData(5, 3.0d)
        });
        exampleSeries3 = new GraphViewSeries(new GraphViewData[] {});
        exampleSeries1.getStyle().color = Color.GREEN;
        exampleSeries3.getStyle().color = Color.RED;
 
         
        if (graphType.equalsIgnoreCase("bar")) {
            graphView = new BarGraphView(container.getContext(), "GraphViewDemo");
        } else {
            graphView = new LineGraphView(container.getContext(), "Usage Of LEDs");
            ((LineGraphView) graphView).setDrawBackground(true);
        }
        graphView.addSeries(exampleSeries1); 
        graphView.addSeries(exampleSeries3);
        graphView.setManualYAxisBounds(3, 0);
        graphView.getGraphViewStyle().setNumVerticalLabels(4);
 
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.opgraph);
        layout.addView(graphView);
        // ----------
        exampleSeries2 = new GraphViewSeries(new GraphViewData[] {
                new GraphViewData(1, 2.0d)
                , new GraphViewData(2, 1.5d)
                , new GraphViewData(2.5, 3.0d) 
                , new GraphViewData(3, 2.5d)
                , new GraphViewData(4, 1.0d)
                , new GraphViewData(5, 3.0d)
        });
 
     
        if (graphType.equalsIgnoreCase("bar")) {
            graphView = new BarGraphView(container.getContext(), "GraphViewDemo");
        } else {
            graphView = new LineGraphView(container.getContext(), "Usage Of Lights");        
            ((LineGraphView) graphView).setDrawBackground(true);
        }
         
        graphView.addSeries(exampleSeries2);
        graphView.setViewPort(1, 8);
        graphView.setScalable(true);
        graphView.getGraphViewStyle().setGridColor(Color.BLACK);
        graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.BLACK);
        graphView.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);
 
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
                        new GraphViewData(1, getRandom())
                        , new GraphViewData(2, getRandom())
                        , new GraphViewData(3, getRandom())
                        , new GraphViewData(4, getRandom())
                        , new GraphViewData(5, getRandom())
                });
                exampleSeries3.resetData(new GraphViewData[] {
                          new GraphViewData(1, getRandom())
                        , new GraphViewData(2, getRandom()) 
                        , new GraphViewData(3, getRandom())
                        , new GraphViewData(4, getRandom())
                        , new GraphViewData(5, getRandom())
                });
                mHandler.postDelayed(this, 300);
            }
        };
        mHandler.postDelayed(mTimer1, 300);
 
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
}
