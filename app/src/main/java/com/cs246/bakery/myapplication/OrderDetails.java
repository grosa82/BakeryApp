package com.cs246.bakery.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cs246.bakery.myapplication.adapters.CharacteristicAdapter;
import com.cs246.bakery.myapplication.model.Cake;
import com.cs246.bakery.myapplication.model.CakeType;
import com.cs246.bakery.myapplication.model.Category;
import com.cs246.bakery.myapplication.model.Characteristic;
import com.cs246.bakery.myapplication.model.Helper;
import com.cs246.bakery.myapplication.model.RequestPackage;
import com.cs246.bakery.myapplication.model.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class OrderDetails extends Activity {
    Helper helper = new Helper(OrderDetails.this);
    ProgressBar progressBar;
    String cakeId;
    Map<Integer, String> categories;
    Button button1, button2, editButton;

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

            // fill the map of categories
            Category category = new Category(OrderDetails.this);
            categories = category.getCategories();

            return cake;
        }

        @Override
        protected void onPostExecute(Cake cake) {
            // display cake info
            TextView status = (TextView) findViewById(R.id.status);
            status.setText(cake.status.name);
            ((TextView) findViewById(R.id.orderName)).setText(cake.name);
            ((TextView) findViewById(R.id.cakeType)).setText(cake.type.name);
            ((ImageView) findViewById(R.id.cakeTypeImage)).setImageBitmap(cake.type.bitmap);

            List<Characteristic> characteristics = new ArrayList<>();
            characteristics.add(new Characteristic("Colors ", helper.parseText(cake.colors)));
            characteristics.add(new Characteristic("Writings ", helper.parseText(cake.writing)));
            characteristics.add(new Characteristic("Comments ", helper.parseText(cake.comments)));
            characteristics.add(new Characteristic("Age range ", helper.parseText(cake.ageRange)));
            characteristics.add(new Characteristic("Cake event ", helper.parseText(cake.cakeEvent)));
            DateFormat df = DateFormat.getDateTimeInstance();
            characteristics.add(new Characteristic("Order date ", df.format(cake.orderDate)));
            for (int i = 0; i < cake.items.size(); i++) {
                characteristics.add(new Characteristic(
                        categories.get(cake.items.get(i).categoryId),
                        cake.items.get(i).name));
            }
            String price = (cake.price == 0) ? " - " : String.format("$ %.2f", cake.price);
            characteristics.add(new Characteristic("Price ", price));

            if (cake.status.id == 1 || cake.status.id == 3) {
                status.setBackgroundResource(R.drawable.red_status);
            } else if (cake.status.id == 2) {
                status.setBackgroundResource(R.drawable.blue_status);
            } else if (cake.status.id == 5 || cake.status.id == 6) {
                status.setBackgroundResource(R.drawable.yellow_status);
            } else {
                status.setBackgroundResource(R.drawable.green_status);
            }

            // back listener
            View.OnClickListener backListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.goToMyCakes();
                }
            };
            // show company info listener
            View.OnClickListener infoListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.displayCompanyInfo().show();
                }
            };
            // cancel order listener
            View.OnClickListener cancelListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeStatusAction(4, "Order Canceled", "Are you sure you want to cancel the order?\n\nThis action is irreversible");
                }
            };
            // buy listener
            View.OnClickListener buyListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeStatusAction(5, "Price Confirmed", "After you confirm the price, the cake will be baked. Do you want to proceed?");
                }
            };
            // request price listener
            View.OnClickListener requestPriceListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeStatusAction(2, "Status Updated", null);
                }
            };

            if (cake.status.id == 2 || cake.status.id == 5 || cake.status.id == 6) {
                editButton.setVisibility(View.GONE);
                button1.setText("Back");
                button1.setOnClickListener(backListener);
                button2.setVisibility(View.GONE);
            } else if (cake.status.id == 3) {
                editButton.setVisibility(View.VISIBLE);
                button1.setText("Buy Cake");
                button1.setOnClickListener(buyListener);
                button2.setVisibility(View.VISIBLE);
                button2.setText("Cancel Order");
                button2.setOnClickListener(cancelListener);
            } else if (cake.status.id == 1) {
                editButton.setVisibility(View.VISIBLE);
                button1.setText("Request Price");
                button1.setOnClickListener(requestPriceListener);
                button2.setVisibility(View.VISIBLE);
                button2.setText("Cancel Order");
                button2.setOnClickListener(cancelListener);
            } else if (cake.status.id == 7) {
                editButton.setVisibility(View.GONE);
                button1.setText("Back");
                button1.setOnClickListener(backListener);
                button2.setVisibility(View.VISIBLE);
                button2.setText("Show Location");
                button2.setOnClickListener(infoListener);
            }

            CharacteristicAdapter adapter = new CharacteristicAdapter(OrderDetails.this.getApplicationContext(), R.layout.layout_items, characteristics);
            ((ListView) findViewById(R.id.characteristics)).setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
        }
    }

    private void changeStatusAction(final int statusId, final String successMessage, final String question) {
        if (question != null) {
            helper.displayOkCancelDialog(question, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    changeStatus(statusId, successMessage, question);
                }
            }).show();
        } else {
            changeStatus(statusId, successMessage, null);
        }
    }

    private void changeStatus(final Integer statusId, final String successMessage, final String question) {
        new AsyncTask<Void, Void, Response>() {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Response doInBackground(Void... params) {
                RequestPackage requestPackage = new RequestPackage();
                requestPackage.setMethod("POST");
                requestPackage.setUri("UpdateStatus");
                requestPackage.setParam("userId", helper.getPreferences("id"));
                requestPackage.setParam("userToken", helper.getPreferences("token"));
                requestPackage.setParam("id", cakeId);
                requestPackage.setParam("statusId", statusId.toString());
                return helper.callWebService(requestPackage).toResponse();
            }

            @Override
            protected void onPostExecute(Response response) {
                if (response.success) {
                    helper.displayMessage(successMessage);
                    helper.goToMyCakes();
                } else
                    helper.displayMessage(response.message);
                progressBar.setVisibility(View.GONE);
            }
        }.execute(null, null, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        editButton = (Button) findViewById(R.id.edit);

        Intent intent = getIntent();
        cakeId = intent.getStringExtra("cakeId");

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

    public void edit(View view) {
        Intent intent = new Intent(OrderDetails.this, CreateCake.class);
        intent.putExtra("cakeId", cakeId);
        startActivity(intent);
    }
}
