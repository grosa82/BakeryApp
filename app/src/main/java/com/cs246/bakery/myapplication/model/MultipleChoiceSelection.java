package com.cs246.bakery.myapplication.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardo on 3/29/2015.
 */
public class MultipleChoiceSelection extends DialogFragment {

    private List<Item> items = new ArrayList<Item>();
    private String title;
    private Context context;
    private int maxSelections;
    private List<Item> itemsSelected = new ArrayList<Item>();
    private String[] itemsArray;
    private boolean[] checkedItems;
    private String defaultText;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
        itemsArray = new String[this.items.size()];
        for (int i = 0; i < this.items.size(); i++) {
            itemsArray[i] = this.items.get(i).name;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getMaxSelections() {
        return maxSelections;
    }

    public void setMaxSelections(int maxSelections) {
        this.maxSelections = maxSelections;
    }

    public List<Item> getItemsSelected() {
        return itemsSelected;
    }

    public void setItemsSelected(List<Item> itemsSelected) {
        this.itemsSelected = itemsSelected;
        checkedItems = new boolean[this.items.size()];
        for (int i = 0; i < this.items.size(); i++) {
            boolean isSelected = false;
            for (Item item : this.itemsSelected) {
                if (this.items.get(i).id == item.id)
                    isSelected = true;
            }
            checkedItems[i] = isSelected;
        }
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    private int getCheckedItem() {
        int id = 0;
        for (int i = 0; i < items.size(); i++)
            if (items.get(i).id == itemsSelected.get(0).id)
                id = i;
        return id;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title + " (" + maxSelections + " max)");

        if (maxSelections == 1) {
            // just one option allowed
            builder.setSingleChoiceItems(itemsArray, getCheckedItem(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    itemsSelected = null;
                    itemsSelected = new ArrayList<Item>();
                    itemsSelected.add(items.get(which));
                }
            });
        } else {
            // multiple options allowed
            builder.setMultiChoiceItems(itemsArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if (isChecked) {
                        if (itemsSelected.size() < maxSelections)
                            itemsSelected.add(items.get(which));
                        else {
                            new Helper((Activity) context).displayMessage("Please select only " + maxSelections + " option(s)");
                        }

                    } else if (itemsSelected.contains(items.get(which))) {
                        itemsSelected.remove(items.get(which));
                    }
                }
            });
        }
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public String toString() {
        String[] selectedItems = new String[this.itemsSelected.size()];
        boolean hasItems = false;
        for (int i = 0; i < this.itemsSelected.size(); i++) {
            selectedItems[i] = this.itemsSelected.get(i).name;
            hasItems = true;
        }
        if (hasItems)
            return TextUtils.join(", ", selectedItems);
        else
            return defaultText;
    }
}
