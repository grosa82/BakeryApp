package com.cs246.bakery.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs246.bakery.myapplication.R;
import com.cs246.bakery.myapplication.model.Cake;
import com.cs246.bakery.myapplication.model.Helper;

import java.util.List;

/**
 * Created by ricardo on 3/6/2015.
 */
public class OrderAdapter extends ArrayAdapter<Cake> {

    private Context context;
    private List<Cake> orderList;

    public OrderAdapter(Context context, int resource, List<Cake> objects) {
        super(context, resource, objects);
        this.context = context;
        this.orderList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_orders, parent, false);

        Cake order = orderList.get(position);
        TextView nickname = (TextView)view.findViewById(R.id.nickname);
        TextView date = (TextView)view.findViewById(R.id.date);
        ImageView image = (ImageView) view.findViewById(R.id.list_image);

        nickname.setText(order.name);
        date.setText(order.orderDate.toString());
        // image.setImageBitmap(new Helper().getImageBitmap(order.type.image));
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);

        return view;
    }
}
