package com.cs246.bakery.myapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cs246.bakery.myapplication.adapters.OrderAdapter;
import com.cs246.bakery.myapplication.model.Helper;
import com.cs246.bakery.myapplication.model.Cake;

import java.io.InputStream;
import java.net.URL;
import java.util.List;


public class MyCakes extends ActionBarActivity {
    private Helper helper = new Helper(this);
    private ProgressBar progressBar;
    List<Cake> orders;

    @Override
    public void onStart() {
        super.onStart();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();
    }


    @Override
    protected void onResume() {
        super.onResume();
        String name = helper.getPreferences("name");
        if (!name.isEmpty()) {
            TextView textView = ((TextView) findViewById(R.id.title));
            if (textView != null)
                textView.setText("Hi, " + name + "!");
        }

        new LoadOrders().execute();
    }

    class LoadOrders extends AsyncTask<Void, Void, List<Cake>> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Cake> doInBackground(Void... params) {
            Cake order = new Cake(MyCakes.this);
            orders = order.getCakes();

            if (orders != null) {
                for (Cake cake : orders) {
                    try {
                        InputStream in = (InputStream) new URL(cake.type.image).getContent();
                        Bitmap bitmap = BitmapFactory.decodeStream(in);
                        cake.type.bitmap = bitmap;
                        in.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            return orders;
        }

        @Override
        public void onPostExecute(List<Cake> orders) {
            progressBar.setVisibility(View.GONE);
            if (orders != null) {
                ((ListView)findViewById(R.id.listView)).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.message)).setVisibility(View.INVISIBLE);

                OrderAdapter adapter = new OrderAdapter(MyCakes.this.getApplicationContext(), R.layout.layout_orders, orders);
                ((ListView)findViewById(R.id.listView)).setAdapter(adapter);

                ListView listView1 = (ListView) findViewById(R.id.listView);

                // start a new activity on each item click
                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        Intent intent = new Intent(MyCakes.this, OrderDetails.class);
                        intent.putExtra("cakeId", ((TextView)view.findViewById(R.id.cakeId)).getText().toString());
                        startActivity(intent);
                    }
                });
            }
            else {
                ((ListView)findViewById(R.id.listView)).setVisibility(View.INVISIBLE);
                ((TextView)findViewById(R.id.message)).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cakes);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_about_us) {
            helper.displayCompanyInfo().show();
            return true;
        }

        if (id == R.id.call_me) {
            helper.displayOkCancelDialog("We can call you to help you with your order", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    helper.callMe();
                }
            }).show();
            return true;
        }

        if (id == R.id.my_profile) {
            helper.goToProfile();
            return true;
        }

        if (id == R.id.action_signOut) {
            helper.signOut();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }



    public void chooseType(View view) {
        Intent homepage = new Intent(MyCakes.this, CreateCake.class);
        startActivity(homepage);
    }

}
