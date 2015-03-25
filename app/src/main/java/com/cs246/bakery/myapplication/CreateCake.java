package com.cs246.bakery.myapplication;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.cs246.bakery.myapplication.model.CakeType;
import com.cs246.bakery.myapplication.model.Category;
import com.cs246.bakery.myapplication.model.Helper;
import com.cs246.bakery.myapplication.model.Item;
import com.cs246.bakery.myapplication.model.Rules;

import java.util.ArrayList;
import java.util.List;

public class CreateCake extends ActionBarActivity {
    private Helper helper = new Helper(this);
    private Rules rules;
    @Override
    public void onStart() {
        super.onStart();
        helper.displayMessage("Customize Your Cake");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cake);

        new AsyncTask<Void, Void, List<CakeType>>() {
            @Override
            protected List<CakeType> doInBackground(Void... params) {
                return new CakeType(CreateCake.this).getCakeTypes();
            }
            @Override
            protected void onPostExecute(List<CakeType> cakeTypes) {
                // Create an ArrayAdapter using the cake type array
                ArrayAdapter<CakeType> adapter = new ArrayAdapter<CakeType>(CreateCake.this, android.R.layout.simple_gallery_item, cakeTypes);
                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

                // Apply the adapter to the spinner
                ((Spinner) findViewById(R.id.cakeType)).setAdapter(adapter);
                Spinner spinner = (Spinner) findViewById(R.id.cakeType);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Integer databaseId = ((CakeType)((Spinner) findViewById(R.id.cakeType)).getSelectedItem()).id;

                        loadRules();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }.execute(null, null, null);
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

    /**
     * Loads rules based on the cake type selected
     */
    private void loadRules() {
        // gets the selected cake type
        final Integer databaseId = ((CakeType)((Spinner) findViewById(R.id.cakeType)).getSelectedItem()).id;
        // gets the rules for this cake type id
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // creating the Rule obj is loading the categories behind the scenes
                rules = new Rules(databaseId, CreateCake.this);
                return null;
            }

            @Override
            protected void onPostExecute(Void param) {
                RelativeLayout layout = (RelativeLayout)findViewById(R.id.container);
                layout.removeAllViews();

                for (int i = 0; i < rules.categories.size(); i++) {
                    // Create text views at runtime
                    TextView textView = new TextView(CreateCake.this);
                    textView.setText(rules.categories.get(i).name + ": ");
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 100 * i, 0, 0);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    textView.setLayoutParams(layoutParams);
                    textView.setTextAppearance(CreateCake.this, R.style.boldText);
                    layout.addView(textView);

                    // Create spinners at runtime
                    Spinner spinner = new Spinner(CreateCake.this);
                    ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(CreateCake.this, android.R.layout.simple_gallery_item, rules.categories.get(i).items);
                    adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                    spinner.setAdapter(adapter);
                    layoutParams = new RelativeLayout.LayoutParams(600, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 100 * i, 0, 0);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    spinner.setLayoutParams(layoutParams);
                    layout.addView(spinner);
                }
            }
        }.execute(null, null, null);
    }

    public void save(View view) {
        loadRules();
    }
}
