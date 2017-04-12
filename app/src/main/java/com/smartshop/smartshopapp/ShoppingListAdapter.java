package com.smartshop.smartshopapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingListAdapter extends ArrayAdapter<ShoppingListItem>
{
    ArrayList<ShoppingListItem> shoppingList;
    Context context;

    public ShoppingListAdapter(Context context, int listViewResourceId,
                               ArrayList<ShoppingListItem> shoppingList)
    {
        super(context, listViewResourceId, shoppingList);
        this.context = context;
        this.shoppingList = new ArrayList<ShoppingListItem>();
        this.shoppingList.addAll(shoppingList);
    }

    static class ViewHolder
    {
        @BindView(R.id.nameCheckBox)
        CheckBox name;
        @BindView(R.id.quantityText)
        TextView quantity;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        Log.v("ConvertView", String.valueOf(position));

        // If the view does not exist, create it for the given position
        // Otherwise, get the view holder for the existing position
        if (convertView == null)
        {
            // Inflate the shopping_list_row layout for the view
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.shopping_list_row, parent, false);

            // Create a new holder for the view and set the tag to the holder
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        // Get the current shopping item and update the holder name, quantity, and whether its
        // checked
        ShoppingListItem item = shoppingList.get(position);
        holder.name.setText(item.getName());
        holder.name.setTag(item);
        holder.quantity.setText(Integer.toString(item.getQuantity()));

        ListView view = (ListView)parent;
        holder.name.setChecked(view.isItemChecked(position));

        return convertView;
    }
}