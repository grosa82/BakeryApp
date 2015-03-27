package com.cs246.bakery.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cs246.bakery.myapplication.adapters.ItemAdapter;
import com.cs246.bakery.myapplication.adapters.OrderAdapter;
import com.cs246.bakery.myapplication.model.Cake;
import com.cs246.bakery.myapplication.model.Category;
import com.cs246.bakery.myapplication.model.Helper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class OrderDetails extends Activity {
    Helper helper = new Helper(OrderDetails.this);
    ProgressBar progressBar;
    String cakeId;
    List<Category> categories;

    private class loadCake extends AsyncTask<Void, Void, Cake> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Cake doInBackground(Void... params) {
            Cake cake = new Cake(OrderDetails.this);
            cake = cake.getCake(cakeId);
            InputStream in = null;
            try {
                in = (InputStream) new URL(cake.type.image).getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            cake.type.bitmap = bitmap;
            return cake;
        }

        @Override
        protected void onPostExecute(Cake cake) {
            // display cake info
            TextView status = (TextView)findViewById(R.id.status);
            status.setText(cake.status.name);
            ((TextView)findViewById(R.id.orderName)).setText(cake.name);
            ((TextView)findViewById(R.id.cakeType)).setText(cake.type.name);
            ((TextView)findViewById(R.id.colors)).setText(cake.colors);
            ((TextView)findViewById(R.id.writing)).setText(cake.writing);
            ((TextView)findViewById(R.id.comments)).setText(cake.comments);
            ((TextView)findViewById(R.id.ageRange)).setText(cake.ageRange);
            ((TextView)findViewById(R.id.cakeEvent)).setText(cake.cakeEvent);
            ((ImageView)findViewById(R.id.cakeTypeImage)).setImageBitmap(cake.type.bitmap);
            ((TextView)findViewById(R.id.cost)).setText(String.format("%.2f", cake.price));

            if (cake.status.id == 1 || cake.status.id == 3) {
                status.setBackgroundResource(R.drawable.red_status);
            } else if (cake.status.id == 2) {
                status.setBackgroundResource(R.drawable.blue_status);
            } else if (cake.status.id == 5 || cake.status.id == 6) {
                status.setBackgroundResource(R.drawable.yellow_status);
            } else {
                status.setBackgroundResource(R.drawable.green_status);
            }

            ItemAdapter adapter = new ItemAdapter(OrderDetails.this.getApplicationContext(), R.layout.layout_items, cake.items);
            ((ListView)findViewById(R.id.characteristics)).setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();

        Intent intent = getIntent();
        cakeId = intent.getStringExtra("cakeId");
        categories = new ArrayList<>();

        new loadCake().execute(null, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
