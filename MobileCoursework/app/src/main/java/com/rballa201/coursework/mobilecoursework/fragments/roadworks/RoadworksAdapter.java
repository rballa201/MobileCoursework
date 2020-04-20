///////////////////////////////////////////////////////////////////////////////
// Title:       Mobile Coursework
// Student:     Ryan Ballantine
// Student ID:  S1829049
///////////////////////////////////////////////////////////////////////////////
package com.rballa201.coursework.mobilecoursework.fragments.roadworks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.rballa201.coursework.mobilecoursework.R;

import java.util.ArrayList;
import java.util.Date;

public class RoadworksAdapter extends ArrayAdapter<RoadworksItem> implements Filterable {
        //implements View.OnClickListener {
    private ArrayList<RoadworksItem> roadworksItems;
    private final Context context;
    private Filter roadworkTitleFilter;
    private Filter roadworkStartDateFilter;
    private Filter roadworkEndDateFilter;

    private ArrayList<RoadworksItem> origRoadworksItems;

    private static class ViewHolder {
        TextView txtTitle;
        TextView txtStart;
        TextView txtEnd;
        TextView txtDelay;
        TextView txtLink;
//        Button btnMap;
        ImageView imageView;

    }

    public RoadworksAdapter(Context context, ArrayList<RoadworksItem> roadworksItems) {
        super(context, R.layout.roadworks_list, roadworksItems);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.roadworksItems = roadworksItems;
        this.origRoadworksItems = roadworksItems;
    }

    public int getCount() {
        return roadworksItems.size();
    }

    public RoadworksItem getItem(int position) {
        return roadworksItems.get(position);
    }

    public long getItemId(int position) {
        return roadworksItems.get(position).hashCode();
    }


    private int lastPosition = -1;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RoadworksItem roadworksItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.roadworks_list, parent, false);
            viewHolder.txtTitle = convertView.findViewById(R.id.title);
            viewHolder.txtStart = convertView.findViewById(R.id.StartDate);
            viewHolder.txtEnd = convertView.findViewById(R.id.EndDate);
            viewHolder.txtDelay = convertView.findViewById(R.id.DelayInfo);
            viewHolder.imageView = convertView.findViewById(R.id.icon);
            //viewHolder.txtLink = convertView.findViewById(R.id.link);
            //viewHolder.btnMap = convertView.findViewById(R.id.MapButton);
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
        if (roadworksItem != null) {
            StartDate = roadworksItem.getStartDate();
        }
        Date EndDate = null;
        if (roadworksItem != null) {
            EndDate = roadworksItem.getEndDate();
        }


        if (roadworksItem != null) {
            CharSequence constraint = ("closure").toUpperCase();
            if (((roadworksItem.getTitle()).toUpperCase()).contains(constraint.toString().toUpperCase())){
 //|| roadworksItem.getTitle().contains("Closure")){
                //convertView.setBackgroundColor(Color.parseColor("red"));
                viewHolder.imageView.setImageResource(R.drawable.ic_closure);
            }
            viewHolder.txtTitle.setText(roadworksItem.getTitle());
        }
        if (StartDate != null) {
            viewHolder.txtStart.setText("Start Date: " + StartDate.toString());
        }
        if (EndDate != null) {
            viewHolder.txtEnd.setText("End Date: " + EndDate.toString());
        }
        if (roadworksItem != null) {
            //String regex = ".*\\b" + Pattern.quote("closure") + "\\b.*"; // \b is a word boundary
            //if (roadworksItem.getDelayInfo().toLowerCase().matches(regex)){// || roadworksItem.getDelayInfo().contains("Closure")){
                //convertView.setBackgroundColor(Color.parseColor("red"));
                //viewHolder.imageView.setImageResource(R.drawable.ic_closure);
            //}
            viewHolder.txtDelay.setText(roadworksItem.getDelayInfo());
        }
//        if (roadworksItem != null) {
//            viewHolder.txtLink.setText(roadworksItem.getLink());
//        }
        //viewHolder.txtVersion.setText(dataModel.getVersion_number());
        //viewHolder.btnMap.setOnClickListener(this);
        if (StartDate != null && EndDate != null) {
            if (new Date().before(roadworksItem.getStartDate()))
            {
                //convertView.setBackgroundColor(Color.parseColor("yellow"));
            }else if (new Date().after(roadworksItem.getStartDate()) && new Date().before(roadworksItem.getEndDate()))
            {
                //convertView.setBackgroundColor(Color.parseColor("red"));
            }else if (new Date().after(roadworksItem.getEndDate())){
                //convertView.setBackgroundColor(Color.parseColor("green"));
            }
        }
        viewHolder.txtTitle.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }

    public void resetData() {
        roadworksItems = origRoadworksItems;
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
                results.values = origRoadworksItems;
                results.count = origRoadworksItems.size();
            } else {
                // We perform filtering operation
                ArrayList<RoadworksItem> nRoadworkList = new ArrayList<RoadworksItem>();

                for (RoadworksItem p : roadworksItems) {
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
                roadworksItems = (ArrayList<RoadworksItem>) results.values;
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
                results.values = origRoadworksItems;
                results.count = origRoadworksItems.size();
            } else {
                // We perform filtering operation
                ArrayList<RoadworksItem> nRoadworkList = new ArrayList<RoadworksItem>();

                for (RoadworksItem p : roadworksItems) {
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
                roadworksItems = (ArrayList<RoadworksItem>) results.values;
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
                results.values = origRoadworksItems;
                results.count = origRoadworksItems.size();
            } else {
                // We perform filtering operation
                ArrayList<RoadworksItem> nRoadworkList = new ArrayList<RoadworksItem>();

                for (RoadworksItem p : roadworksItems) {
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
                roadworksItems = (ArrayList<RoadworksItem>) results.values;
                notifyDataSetChanged();
            }

        }
    }

}
