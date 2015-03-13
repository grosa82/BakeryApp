package com.cs246.bakery.myapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs246.bakery.myapplication.adapters.OrderAdapter;
import com.cs246.bakery.myapplication.model.Helper;
import com.cs246.bakery.myapplication.model.Order;
import com.cs246.bakery.myapplication.model.RequestPackage;
import com.cs246.bakery.myapplication.model.User;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class account_summary extends ListActivity {
    public final static String CAKE_NICKNAME = "com.cs246.bakery.myapplication.MESSAGE";
    public final static String DATE = "com.cs246.bakery.myapplication.MESSAGE2";


    Helper helper = new Helper();
    @Override
    public void onStart() {
        super.onStart();
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/candy.ttf");
        ((TextView) findViewById(R.id.title)).setTypeface(tf, Typeface.NORMAL);
    }


    @Override
    protected void onResume() {
        super.onResume();
        String name = helper.getPreferences("name", account_summary.this.getApplicationContext());
        if (!name.isEmpty()) {
            TextView textView = ((TextView) findViewById(R.id.title));
            if (textView != null)
                textView.setText("Hi, " + name + "!");
        }

        new LoadOrders().execute();
    }

    class LoadOrders extends AsyncTask<Void, Void, List<Order>> {

        @Override
        protected List<Order> doInBackground(Void... params) {
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setMethod("GET");
            requestPackage.setUri("GetOrders");
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

                ListView listView1 = (ListView) findViewById(android.R.id.list);

                // start a new activity on each item click
                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        Intent intent = new Intent(account_summary.this, OrderedCake.class);
                        TextView nickname = (TextView)view.findViewById(R.id.nickname);
                        TextView date = (TextView)view.findViewById(R.id.date);
                        String message = nickname.getText().toString();
                        String message2 = date.getText().toString();
                        intent.putExtra(CAKE_NICKNAME, message);
                        intent.putExtra(DATE, message2);
                        startActivity(intent);
                    }
                });
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
