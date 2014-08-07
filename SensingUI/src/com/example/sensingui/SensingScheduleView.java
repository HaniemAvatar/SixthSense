package com.example.sensingui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.example.sensingui.assets.AnimatedExpandableListView;
import com.example.sensingui.assets.AnimatedExpandableListView.AnimatedExpandableListAdapter;

/**
 * This is an example usage of the AnimatedExpandableListView class.
 * 
 * It is an activity that holds a listview which is populated with 100 groups
 * where each group has from 1 to 100 children (so the first group will have one
 * child, the second will have two children and so on...).
 */
public class SensingScheduleView extends Fragment {
	Context mContext;
	private AnimatedExpandableListView listView;
    private ListAdapter adapter;
    public int alarmnumber;
    Button addButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {        
    	final View rootView = inflater.inflate(R.layout.schedule_main, container, false);
    	mContext=rootView.getContext();
        final List<GroupItem> items = new ArrayList<GroupItem>();
        alarmnumber=0;
        
        // Populate our list with groups and it's children
        addButton = (Button)rootView.findViewById(R.id.addbutton);
        addButton.setFocusable(false);
        addButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

						// TODO Auto-generated method stub
						GroupItem item = new GroupItem();	            
				        item.title = "Alarm"+alarmnumber++;
				        {
				            ChildItem child = new ChildItem();
				            child.title = "Create New UI ";
				            child.hint = "Not Yet";                
				            item.items.add(child);
				        }
				        items.add(item);
				        
				        adapter = new ListAdapter(mContext);
				        adapter.setData(items);
				        
				        listView = (AnimatedExpandableListView) rootView.findViewById(R.id.listView);
				        listView.setAdapter(adapter);
				        
				        // In order to show animations, we need to use a custom click handler
				        // for our ExpandableListView.
				        listView.setOnGroupClickListener(new OnGroupClickListener() {

				            @Override
				            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				                // We call collapseGroupWithAnimation(int) and
				                // expandGroupWithAnimation(int) to animate group 
				                // expansion/collapse.
				                if (listView.isGroupExpanded(groupPosition)) {
				                    listView.collapseGroupWithAnimation(groupPosition);
				                } else {
				                    listView.expandGroupWithAnimation(groupPosition);
				                }
				                return true;
				            }
				            
				        });

				        }
        });

        return rootView;
    }
    
    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }
    
    private static class ChildItem {
        String title;
        String hint;
    }
    
    private static class ChildHolder {
        TextView title;
        TextView hint;
    }
    
    private static class GroupHolder {
        TextView title;
    }
    
    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    private class ListAdapter extends AnimatedExpandableListAdapter {
        private LayoutInflater inflater;
        
        private List<GroupItem> items;
        
        public ListAdapter(Context schejuleActivity) {
             inflater = LayoutInflater.from(schejuleActivity);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                holder.hint = (TextView) convertView.findViewById(R.id.textHint);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }
            
            holder.title.setText(item.title);
            holder.hint.setText(item.hint);
            
            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }
            
            holder.title.setText(item.title);
            
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }
        
    }
    
}
