package com.citation.emmanuel.citation365.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.citation.emmanuel.citation365.R;
import com.citation.emmanuel.citation365.models.NavItem;


import java.util.List;

/**
 * Created by emmanuel on 02/10/2015.
 */
public class NavListAdapter extends ArrayAdapter<NavItem>{

    Context context;
    int resLayout;
    List<NavItem> listNavItems;

    public NavListAdapter(Context context, int resLayout, List<NavItem> listNavItems) {
        super(context, resLayout, listNavItems);

        this.context = context;
        this.resLayout = resLayout;
        this.listNavItems = listNavItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(context, resLayout, null);

        TextView tvTitle = (TextView) v.findViewById(R.id.title);
        ImageView ivIcon = (ImageView) v.findViewById(R.id.nav_icon);

        NavItem navItem = listNavItems.get(position);

        tvTitle.setText(navItem.getTitle());
        ivIcon.setImageResource(navItem.getNavIcon());

        return v;
    }
}
