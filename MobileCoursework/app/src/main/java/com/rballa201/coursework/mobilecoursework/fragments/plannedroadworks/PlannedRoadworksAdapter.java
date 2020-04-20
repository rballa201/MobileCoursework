///////////////////////////////////////////////////////////////////////////////
// Title:       Mobile Coursework
// Student:     Ryan Ballantine
// Student ID:  S1829049
///////////////////////////////////////////////////////////////////////////////
package com.rballa201.coursework.mobilecoursework.fragments.plannedroadworks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;

import com.rballa201.coursework.mobilecoursework.R;
import java.util.ArrayList;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.Date;

public class PlannedRoadworksAdapter extends ArrayAdapter<PlannedRoadworksItem> implements Filterable {
    //implements View.OnClickListener
    private ArrayList<PlannedRoadworksItem> plannedRoadworksItems;
    private ArrayList<PlannedRoadworksItem> origPlannedRoadworksItems;
    private final Context context;
    private Filter roadworkTitleFilter;
    private Filter roadworkStartDateFilter;
    private Filter roadworkEndDateFilter;

    private static class ViewHolder {
        TextView txtTitle;
        TextView txtStart;
        TextView txtEnd;
        TextView txtDelay;
        TextView txtLink;
    }

    public PlannedRoadworksAdapter(Context context, ArrayList<PlannedRoadworksItem> plannedRoadworksItems) {
        super(context, R.layout.plannedroadworks_list, plannedRoadworksItems);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.plannedRoadworksItems = plannedRoadworksItems;
        this.origPlannedRoadworksItems = plannedRoadworksItems;

    }

    public int getCount() {
        return plannedRoadworksItems.size();
    }

    public PlannedRoadworksItem getItem(int position) {
        return plannedRoadworksItems.get(position);
    }

    public long getItemId(int position) {
        return plannedRoadworksItems.get(position).hashCode();
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PlannedRoadworksItem plannedRoadworksItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.plannedroadworks_list, parent, false);
            viewHolder.txtTitle = convertView.findViewById(R.id.title);
            viewHolder.txtStart = convertView.findViewById(R.id.StartDate);
            viewHolder.txtEnd = convertView.findViewById(R.id.EndDate);
            viewHolder.txtDelay = convertView.findViewById(R.id.DelayInfo);
            //viewHolder.txtLink = convertView.findViewById(R.id.link);
            //viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
        Date StartDate = null;
        if (plannedRoadworksItem != null) {
            StartDate = plannedRoadworksItem.getStartDate();
        }
        Date EndDate = null;
        if (plannedRoadworksItem != null) {
            EndDate = plannedRoadworksItem.getEndDate();
        }

        if (plannedRoadworksItem != null) {
            if (plannedRoadworksItem.getTitle().matches(".*Closure.*")) { //|| roadworksItem.getTitle().contains("Closure")){

            }
            viewHolder.txtTitle.setText(plannedRoadworksItem.getTitle());
        }
        if (StartDate != null) {
            viewHolder.txtStart.setText("Start Date: " + StartDate.toString());
        }
        if (EndDate != null) {
            viewHolder.txtEnd.setText("End Date: " + EndDate.toString());
        }
        if (plannedRoadworksItem != null) {
            if (plannedRoadworksItem.getDelayInfo().matches(".*Closure.*")) {// || roadworksItem.getDelayInfo().contains("Closure")){
            }
            viewHolder.txtDelay.setText(plannedRoadworksItem.getDelayInfo());

        }
//        if (plannedRoadworksItem != null) {
//            viewHolder.txtLink.setText(plannedRoadworksItem.getLink());
//        }
        //viewHolder.txtVersion.setText(dataModel.getVersion_number());
        //viewHolder.btnMap.setOnClickListener(this);
        viewHolder.txtTitle.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }

    public void resetData() {
        plannedRoadworksItems = origPlannedRoadworksItems;
    }
    public Filter getTitleFilter() {
        if (roadworkTitleFilter == null)
            roadworkTitleFilter = new RoadworkTitleFilter();

        return roadworkTitleFilter;
    }

    private class RoadworkTitleFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origPlannedRoadworksItems;
                results.count = origPlannedRoadworksItems.size();
            } else {
                // We perform filtering operation
                ArrayList<PlannedRoadworksItem> nRoadworkList = new ArrayList<>();

                for (PlannedRoadworksItem p : plannedRoadworksItems) {
                    if (p.getTitle().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        nRoadworkList.add(p);
                    }
                }

                results.values = nRoadworkList;
                results.count = nRoadworkList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                plannedRoadworksItems = (ArrayList<PlannedRoadworksItem>) results.values;
                notifyDataSetChanged();
            }

        }
    }

    public Filter getStartDateFilter() {
        if (roadworkStartDateFilter == null)
            roadworkStartDateFilter = new RoadworkStartDateFilter();

        return roadworkStartDateFilter;
    }


    private class RoadworkStartDateFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origPlannedRoadworksItems;
                results.count = origPlannedRoadworksItems.size();
            } else {
                // We perform filtering operation
                ArrayList<PlannedRoadworksItem> nRoadworkList = new ArrayList<>();

                for (PlannedRoadworksItem p : plannedRoadworksItems) {
                    Date StartDate;
                    if (p != null) {
                        StartDate = p.getStartDate();
                        if (StartDate != null) {
                            String Start = StartDate.toString().toUpperCase();
                            String DateConstraint = constraint.toString().toUpperCase();
                            if (Start.startsWith(DateConstraint)) {
                                nRoadworkList.add(p);
                            }
                        }
                    }
                }

                results.values = nRoadworkList;
                results.count = nRoadworkList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                plannedRoadworksItems = (ArrayList<PlannedRoadworksItem>) results.values;
                notifyDataSetChanged();
            }

        }
    }


    public Filter getEndDateFilter() {
        if (roadworkEndDateFilter == null)
            roadworkEndDateFilter = new RoadworkEndDateFilter();

        return roadworkEndDateFilter;
    }


    private class RoadworkEndDateFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origPlannedRoadworksItems;
                results.count = origPlannedRoadworksItems.size();
            } else {
                // We perform filtering operation
                ArrayList<PlannedRoadworksItem> nRoadworkList = new ArrayList<>();

                for (PlannedRoadworksItem p : plannedRoadworksItems) {
                    Date EndDate;
                    if (p != null) {
                        EndDate = p.getEndDate();
                        if (EndDate != null) {
                            String End = EndDate.toString().toUpperCase();
                            String DateConstraint = constraint.toString().toUpperCase();
                            if (End.startsWith(DateConstraint)) {
                                nRoadworkList.add(p);
                            }
                        }
                    }
                }

                results.values = nRoadworkList;
                results.count = nRoadworkList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                plannedRoadworksItems = (ArrayList<PlannedRoadworksItem>) results.values;
                notifyDataSetChanged();
            }

        }
    }





}

