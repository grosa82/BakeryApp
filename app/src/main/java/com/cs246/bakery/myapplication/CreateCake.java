package com.cs246.bakery.myapplication;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.cs246.bakery.myapplication.model.CakeType;
import com.cs246.bakery.myapplication.model.Category;
import com.cs246.bakery.myapplication.model.Helper;
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

                setTextViewsInvisible();
                List<String> stringList = new ArrayList<String>();
                List<String> stringList2 = new ArrayList<String>();
                List<String> stringList3 = new ArrayList<String>();
                List<String> stringList4 = new ArrayList<String>();
                List<String> stringList5 = new ArrayList<String>();

                // BROWNIE has 2 categories:
                // category[0] = shapes
                // category[1] = sizes
                String cakeType = ((CakeType)((Spinner) findViewById(R.id.cakeType)).getSelectedItem()).name;
                System.out.println(cakeType);
                if (cakeType.matches("Brownie")) {
                    TextView textView = (TextView) findViewById(R.id.textView4);
                    textView.setText(rules.categories.get(0).name + ":");
                    textView.setVisibility(textView.VISIBLE);
                    TextView textView2 = (TextView) findViewById(R.id.textView5);
                    textView2.setText(rules.categories.get(1).name + ":");
                    textView2.setVisibility(textView.VISIBLE);

                    for (int i = 0; i < rules.categories.size() - 1; i++) {
                        for (int j = 0; j < rules.categories.get(i).items.size(); j++) {
                            stringList.add(rules.categories.get(i).items.get(j).name);
;
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateCake.this, android.R.layout.simple_gallery_item, stringList);
                        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

                        ((Spinner) findViewById(R.id.spinner2)).setAdapter(adapter);
                        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
                        spinner.setVisibility(spinner.VISIBLE);
                    }
                    for (int i = 1; i < rules.categories.size(); i++) {
                        for (int j = 0; j < rules.categories.get(i).items.size(); j++) {
                            stringList2.add(rules.categories.get(i).items.get(j).name);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateCake.this, android.R.layout.simple_gallery_item, stringList2);
                            adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

                            ((Spinner) findViewById(R.id.spinner3)).setAdapter(adapter);
                            Spinner spinner = (Spinner) findViewById(R.id.spinner3);
                            spinner.setVisibility(spinner.VISIBLE);
                        }
                    }
                }
                else if (cakeType.matches("Layer Cake")) {
                    stringList.clear();
                    stringList2.clear();
                    stringList3.clear();

                    Spinner spinners = (Spinner) findViewById(R.id.spinner2);
                    Spinner spinners2 = (Spinner) findViewById(R.id.spinner3);
                    Spinner spinners3 = (Spinner) findViewById(R.id.spinner4);
                    Spinner spinners4 = (Spinner) findViewById(R.id.spinner5);
                    Spinner spinners5 = (Spinner) findViewById(R.id.spinner);
                    spinners.setAdapter(null);
                    spinners2.setAdapter(null);

                    spinners.setVisibility(spinners.VISIBLE);
                    spinners2.setVisibility(spinners2.VISIBLE);
                    spinners3.setVisibility(spinners3.VISIBLE);
                    spinners4.setVisibility(spinners4.VISIBLE);
                    spinners5.setVisibility(spinners5.VISIBLE);

                    TextView textView = (TextView) findViewById(R.id.textView4);
                    textView.setText(rules.categories.get(0).name + ":");
                    textView.setVisibility(textView.VISIBLE);

                    TextView textView2 = (TextView) findViewById(R.id.textView5);
                    textView2.setText(rules.categories.get(1).name + ":");
                    textView2.setVisibility(textView2.VISIBLE);

                    TextView textView3 = (TextView) findViewById(R.id.textView6);
                    textView3.setText(rules.categories.get(2).name + ":");
                    textView3.setVisibility(textView3.VISIBLE);

                    TextView textView4 = (TextView) findViewById(R.id.textView);
                    textView4.setText(rules.categories.get(3).name + ":");
                    textView4.setVisibility(textView4.VISIBLE);

                    TextView textView5 = (TextView) findViewById(R.id.textView2);
                    textView5.setText(rules.categories.get(4).name + ":");
                    textView5.setVisibility(textView5.VISIBLE);

                    System.out.println(rules.categories.get(1).items.size());

                    for (int j = 0; j < rules.categories.get(0).items.size(); j++) {

                        stringList.add(rules.categories.get(0).items.get(j).name);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateCake.this, android.R.layout.simple_gallery_item, stringList);
                        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

                        ((Spinner) findViewById(R.id.spinner2)).setAdapter(adapter);
                    }

                    for (int j = 0; j < rules.categories.get(1).items.size(); j++) {

                        stringList2.add(rules.categories.get(1).items.get(j).name);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateCake.this, android.R.layout.simple_gallery_item, stringList2);
                        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

                        ((Spinner) findViewById(R.id.spinner3)).setAdapter(adapter);

                    }

                    for (int j = 0; j < rules.categories.get(2).items.size(); j++) {

                        stringList3.add(rules.categories.get(2).items.get(j).name);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateCake.this, android.R.layout.simple_gallery_item, stringList3);
                        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

                        ((Spinner) findViewById(R.id.spinner4)).setAdapter(adapter);

                    }

                    for (int j = 0; j < rules.categories.get(3).items.size(); j++) {

                        stringList4.add(rules.categories.get(3).items.get(j).name);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateCake.this, android.R.layout.simple_gallery_item, stringList4);
                        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

                        ((Spinner) findViewById(R.id.spinner)).setAdapter(adapter);
                    }

                    for (int j = 0; j < rules.categories.get(4).items.size(); j++) {

                        stringList5.add(rules.categories.get(4).items.get(j).name);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateCake.this, android.R.layout.simple_gallery_item, stringList5);
                        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

                        ((Spinner) findViewById(R.id.spinner5)).setAdapter(adapter);
                    }

                }
            }
        }.execute(null, null, null);
    }

    public void save(View view) {
        loadRules();
    }
    public void setTextViewsInvisible() {
        TextView textView = (TextView) findViewById(R.id.textView4);
        textView.setVisibility(textView.INVISIBLE);

        TextView textView2 = (TextView) findViewById(R.id.textView5);
        textView2.setVisibility(textView2.INVISIBLE);

        TextView textView3 = (TextView) findViewById(R.id.textView6);
        textView3.setVisibility(textView3.INVISIBLE);

        TextView textView4 = (TextView) findViewById(R.id.textView);
        textView4.setVisibility(textView4.INVISIBLE);

        TextView textView5 = (TextView) findViewById(R.id.textView2);
        textView5.setVisibility(textView5.INVISIBLE);

        Spinner spinners = (Spinner) findViewById(R.id.spinner2);
        Spinner spinners2 = (Spinner) findViewById(R.id.spinner3);
        Spinner spinners3 = (Spinner) findViewById(R.id.spinner4);
        Spinner spinners4 = (Spinner) findViewById(R.id.spinner5);
        Spinner spinners5 = (Spinner) findViewById(R.id.spinner);

        spinners.setVisibility(spinners.INVISIBLE);
        spinners2.setVisibility(spinners2.INVISIBLE);
        spinners3.setVisibility(spinners3.INVISIBLE);
        spinners4.setVisibility(spinners4.INVISIBLE);
        spinners5.setVisibility(spinners5.INVISIBLE);
    }
}
