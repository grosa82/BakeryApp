package com.cs246.bakery.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cs246.bakery.myapplication.R;
import com.cs246.bakery.myapplication.model.Category;
import com.cs246.bakery.myapplication.model.Characteristic;
import com.cs246.bakery.myapplication.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardo on 3/27/2015.
 */
public class CharacteristicAdapter extends ArrayAdapter<Characteristic> {
    private Context context;
    private List<Characteristic> itemList;

    public CharacteristicAdapter(Context context, int resource, List<Characteristic> objects) {
        super(context, resource, objects);
        this.context = context;
        this.itemList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_items, parent, false);

        Characteristic item = itemList.get(position);
        ((TextView)view.findViewById(R.id.key)).setText(item.key);
        ((TextView)view.findViewById(R.id.value)).setText(item.value);
        return view;
    }
}
