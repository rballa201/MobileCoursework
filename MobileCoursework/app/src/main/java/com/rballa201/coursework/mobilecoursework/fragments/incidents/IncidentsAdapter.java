///////////////////////////////////////////////////////////////////////////////
// Title:       Mobile Coursework
// Student:     Ryan Ballantine
// Student ID:  S1829049
///////////////////////////////////////////////////////////////////////////////
package com.rballa201.coursework.mobilecoursework.fragments.incidents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.rballa201.coursework.mobilecoursework.R;
import com.rballa201.coursework.mobilecoursework.fragments.incidents.IncidentsItem;

import java.util.ArrayList;
import java.util.Date;

public class IncidentsAdapter extends ArrayAdapter<IncidentsItem> implements Filterable {
//implements View.OnClickListener
    private ArrayList<IncidentsItem> incidents;
    private Filter incidentFilter;
    private ArrayList<IncidentsItem> origIncidents;

    private final Context context;

    private static class ViewHolder {
        TextView txtTitle;
        TextView txtDescription;
        TextView txtLink;
    }

    public IncidentsAdapter(Context context,ArrayList<IncidentsItem> incidents) {
        super(context, R.layout.incidents_list, incidents);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.incidents=incidents;
        this.origIncidents = incidents;
    }

    public int getCount(){
        return incidents.size();
    }
    public IncidentsItem getItem(int position) {
        return incidents.get(position);
    }

    public long getItemId(int position) {
        return incidents.get(position).hashCode();
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        IncidentsItem incidentsItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.incidents_list, parent, false);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.description);
            //viewHolder.txtLink = (TextView) convertView.findViewById(R.id.link);
            //viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        if (incidentsItem != null) {
            viewHolder.txtTitle.setText(incidentsItem.getTitle());
        }
        if (incidentsItem != null) {
            viewHolder.txtDescription.setText(incidentsItem.getDescription());
        }
//        if (incidentsItem != null) {
//            viewHolder.txtLink.setText(incidentsItem.getLink());
//        }
        //viewHolder.txtVersion.setText(dataModel.getVersion_number());
//        viewHolder.txtTitle.setOnClickListener(this);
        viewHolder.txtTitle.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }

    public void resetData() {
        incidents = origIncidents;
    }

    public Filter getFilter() {
        if (incidentFilter == null)
            incidentFilter = new TitleFilter();

        return incidentFilter;
    }

    private class TitleFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origIncidents;
                results.count = origIncidents.size();
            } else {
                // We perform filtering operation
                ArrayList<IncidentsItem> nList = new ArrayList<IncidentsItem>();

                for (IncidentsItem p : incidents) {
                    if (p.getTitle().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        nList.add(p);
                    }
                }

                results.values = nList;
                results.count = nList.size();

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
                incidents = (ArrayList<IncidentsItem>) results.values;
                notifyDataSetChanged();
            }

        }
    }


}