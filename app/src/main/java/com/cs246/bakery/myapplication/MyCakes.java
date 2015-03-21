package com.cs246.bakery.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
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


public class MyCakes extends ListActivity {
    private Helper helper = new Helper(this);
    private ProgressBar progressBar;
    public final static String CAKE_NICKNAME = "com.cs246.bakery.myapplication.MESSAGE";
    public final static String DATE = "com.cs246.bakery.myapplication.MESSAGE2";
    List<Cake> orders;

    @Override
    public void onStart() {
        super.onStart();
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/candy.ttf");
        ((TextView) findViewById(R.id.title)).setTypeface(tf, Typeface.NORMAL);
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
                ((ListView)getListView()).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.message)).setVisibility(View.INVISIBLE);

                OrderAdapter adapter = new OrderAdapter(MyCakes.this.getApplicationContext(), R.layout.layout_orders, orders);
                setListAdapter(adapter);

                ListView listView1 = (ListView) findViewById(android.R.id.list);

                // start a new activity on each item click
                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        Intent intent = new Intent(MyCakes.this, OrderedCake.class);
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
        setContentView(R.layout.activity_my_cakes);
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
                helper.deletePreferences();
                startActivity(new Intent(MyCakes.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void signOut(View view) {
        helper.deletePreferences();
        startActivity(new Intent(MyCakes.this, MainActivity.class));
    }

    public void chooseType(View view) {
        Intent homepage = new Intent(MyCakes.this, orderCatagory.class);
        startActivity(homepage);
    }

}
