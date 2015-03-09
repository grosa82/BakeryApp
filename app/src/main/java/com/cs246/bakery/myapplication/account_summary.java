package com.cs246.bakery.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cs246.bakery.myapplication.adapters.OrderAdapter;
import com.cs246.bakery.myapplication.model.Helper;
import com.cs246.bakery.myapplication.model.Order;
import com.cs246.bakery.myapplication.model.RequestPackage;
import com.cs246.bakery.myapplication.model.User;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class account_summary extends ListActivity {

    Helper helper = new Helper();

    @Override
    protected void onResume() {
        super.onResume();
        String name = helper.getPreferences("name", account_summary.this.getApplicationContext());
        if (!name.isEmpty()) {
            TextView textView = ((TextView) findViewById(R.id.title));
            if (textView != null)
                textView.setText("Welcome, " + name);
        }

        new LoadOrders().execute();
    }

    class LoadOrders extends AsyncTask<Void, Void, List<Order>> {

        @Override
        protected List<Order> doInBackground(Void... params) {
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setMethod("GET");
            requestPackage.setUri("order/get");
            requestPackage.setParam("id", helper.getPreferences("id", account_summary.this.getApplicationContext()));
            requestPackage.setParam("token", helper.getPreferences("token", account_summary.this.getApplicationContext()));

            List<Order> orders = new ArrayList<>();

            String jsonString = helper.getData(requestPackage);
            if (jsonString == null || jsonString.equals("null\n"))
                return null;
            else {
                try {
                    JSONArray orderJson = new JSONArray(jsonString);
                    for (int i = 0; i < orderJson.length(); i++) {
                        Order order = helper.parseOrder(orderJson.get(i).toString());
                        orders.add(order);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return orders;
            }
        }

        @Override
        public void onPostExecute(List<Order> orders) {
            if (orders != null) {
                ((ListView)getListView()).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.message)).setVisibility(View.INVISIBLE);

                OrderAdapter adapter = new OrderAdapter(account_summary.this.getApplicationContext(), R.layout.layout_orders, orders);
                setListAdapter(adapter);
            }
            else {
                ((ListView)getListView()).setVisibility(View.INVISIBLE);
                ((TextView)findViewById(R.id.message)).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_summary);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_summary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_signOut:
                helper.deletePreferences(account_summary.this.getApplicationContext());
                startActivity(new Intent(account_summary.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void signOut(View view) {
        helper.deletePreferences(account_summary.this.getApplicationContext());
        startActivity(new Intent(account_summary.this, MainActivity.class));
    }

    public void chooseType(View view) {
        Intent homepage = new Intent(account_summary.this, orderCatagory.class);
        startActivity(homepage);
    }

}
