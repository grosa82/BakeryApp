package com.cs246.bakery.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs246.bakery.myapplication.R;
import com.cs246.bakery.myapplication.model.Cake;

import java.text.DateFormat;
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
        TextView status = (TextView)view.findViewById(R.id.status);
        TextView type = (TextView)view.findViewById(R.id.type);
        ImageView image = (ImageView) view.findViewById(R.id.list_image);
        TextView cakeId = (TextView)view.findViewById(R.id.cakeId);

        nickname.setText(order.name);
        DateFormat df = DateFormat.getDateTimeInstance();
        date.setText(df.format(order.orderDate));
        status.setText(order.status.name);
        type.setText(order.type.name);
        image.setImageBitmap(order.type.bitmap);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Integer orderId = order.id;
        cakeId.setText(orderId.toString());

        if (order.status.id == 1 || order.status.id == 3) {
            status.setBackgroundResource(R.drawable.red_status);
        } else if (order.status.id == 2) {
            status.setBackgroundResource(R.drawable.blue_status);
        } else if (order.status.id == 5 || order.status.id == 6) {
            status.setBackgroundResource(R.drawable.yellow_status);
        } else {
            status.setBackgroundResource(R.drawable.green_status);
        }

        return view;
    }
}
