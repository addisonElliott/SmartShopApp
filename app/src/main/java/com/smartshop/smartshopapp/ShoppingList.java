package com.smartshop.smartshopapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.view.ActionMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ShoppingList extends AppCompatActivity
{
    ShoppingListAdapter dataAdapter = null;

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.toolbar_main)
    Toolbar mainToolbar;

    @BindView(R.id.toolbar_shopping_list_selected)
    Toolbar selectedToolbar;
    @BindView(R.id.selectAllCheckBox)
    CheckBox selectAllCheckBox;
    @BindView(R.id.titleTextView)
    TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        // Initialize all variables annotated with @BindView and other variants
        ButterKnife.bind(this);

        // Set action bar to be the mainToolbar, set the title and subtitle and disable the
        // logo and home button
        setSupportActionBar(mainToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle("Shopping List");
        supportActionBar.setSubtitle("Required");
        supportActionBar.setDisplayHomeAsUpEnabled(false);
        supportActionBar.setDisplayShowHomeEnabled(false);
        supportActionBar.setDisplayUseLogoEnabled(false);

        displayListView();
    }

    @OnClick(R.id.selectAllCheckBox)
    public void selectAllCheckBox_onClick(View v)
    {
        CheckBox checkBox = (CheckBox)v;

        if (checkBox.isChecked())
        {
            for (int i = 0; i < listView.getCount(); ++i)
                listView.setItemChecked(i, true);

            titleTextView.setText(String.format("%d selected", listView.getCheckedItemCount()));
            dataAdapter.notifyDataSetChanged();
        }
        else
        {
            listView.clearChoices();
            getSupportActionBar().show();
            selectedToolbar.setVisibility(GONE);
            dataAdapter.notifyDataSetChanged();
        }
    }

    @OnItemClick(R.id.listView)
    public void listView_onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // Get the referenced item
        ShoppingListItem item = (ShoppingListItem)parent.getItemAtPosition(position);

        // Notify data adapter that a list item has changed
        dataAdapter.notifyDataSetChanged();

        // If an item is selected, show the selected toolbar and update the text that
        // indicates the number of selected items. Otherwise, hide selected toolbar
        if (listView.getCheckedItemCount() > 0)
        {
            titleTextView.setText(String.format("%d selected", listView.getCheckedItemCount()));

            getSupportActionBar().hide();
            selectedToolbar.setVisibility(VISIBLE);
        }
        else
        {
            getSupportActionBar().show();
            selectedToolbar.setVisibility(GONE);
        }
    }

    private void displayListView()
    {
        //Array list of countries
        ArrayList<ShoppingListItem> shoppingList = new ArrayList<ShoppingListItem>();
        ShoppingListItem item = new ShoppingListItem(1, "AFGAFGAFGAFGAFGAFGAFG", 100);
        shoppingList.add(item);
        item = new ShoppingListItem(2, "ABCMountain Dew", 100);
        shoppingList.add(item);
        item = new ShoppingListItem(3, "MONLong Long Text", 100);
        shoppingList.add(item);
        item = new ShoppingListItem(4, "YYY", 12);
        shoppingList.add(item);
        item = new ShoppingListItem(5, "XXX", 12);
        shoppingList.add(item);
        item = new ShoppingListItem(5, "XXX", 12);
        shoppingList.add(item);
        item = new ShoppingListItem(5, "XXX", 12);
        shoppingList.add(item);
        item = new ShoppingListItem(5, "XXX", 12);
        shoppingList.add(item);
        item = new ShoppingListItem(5, "XXX", 12);
        shoppingList.add(item);
        item = new ShoppingListItem(5, "XXX", 12);
        shoppingList.add(item);
        item = new ShoppingListItem(5, "XXX", 12);
        shoppingList.add(item);
        item = new ShoppingListItem(5, "XXX", 12);
        shoppingList.add(item);

        //create an ArrayAdaptar from the String Array
        dataAdapter = new ShoppingListAdapter(this, R.layout.shopping_list_row, shoppingList);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }

    private class ActionModeCallback implements ActionMode.Callback
    {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            // inflate contextual menu
            //mode.getMenuInflater().inflate(R.menu.menu_req_shopping_list_contextual, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            Toast.makeText(ShoppingList.this, "Testing!", Toast.LENGTH_LONG).show();

            /*// retrieve selected items and print them out
            SelectableAdapter adapter = (SelectableAdapter) ListViewActivity.this.getListAdapter();
            SparseBooleanArray selected = adapter.getSelectedIds();
            StringBuilder message = new StringBuilder();
            for (int i = 0; i < selected.size(); i++){
                if (selected.valueAt(i)) {
                    String selectedItem = adapter.getItem(selected.keyAt(i));
                    message.append(selectedItem + "\n");
                }
            }
            Toast.makeText(ListViewActivity.this, message.toString(), Toast.LENGTH_LONG).show();

            // close action mode
            mode.finish();*/
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode)
        {
            /*// remove selection
            SelectableAdapter adapter = (SelectableAdapter) getListAdapter();
            adapter.removeSelection();
            mActionMode = null;*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}