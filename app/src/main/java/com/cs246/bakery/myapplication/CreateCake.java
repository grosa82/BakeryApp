package com.cs246.bakery.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cs246.bakery.myapplication.model.Cake;
import com.cs246.bakery.myapplication.model.CakeType;
import com.cs246.bakery.myapplication.model.Helper;
import com.cs246.bakery.myapplication.model.Item;
import com.cs246.bakery.myapplication.model.Response;
import com.cs246.bakery.myapplication.model.Rules;

import java.util.ArrayList;
import java.util.List;

public class CreateCake extends ActionBarActivity {
    private Helper helper = new Helper(this);
    private Rules rules;
    private ProgressDialog progressDialog;
    private Spinner ageRange, cakeType;
    private EditText colors, orderName, cakeEvent, writings, comments;
    private String cakeId;
    private Button actionButton;
    private CakeType cakeTypeSelected = null;
    private Cake cakeSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cake);

        // get cake id if it is an update call
        Intent intent = getIntent();
        cakeId = intent.getStringExtra("cakeId");

        // get views for future use
        actionButton = (Button) findViewById(R.id.actionButton);
        cakeType = (Spinner) findViewById(R.id.cakeType);
        ageRange = (Spinner) findViewById(R.id.ageRange);
        colors = (EditText) findViewById(R.id.colors);
        orderName = (EditText) findViewById(R.id.orderName);
        cakeEvent = (EditText) findViewById(R.id.cakeEvent);
        writings = (EditText) findViewById(R.id.writing);
        comments = (EditText) findViewById(R.id.comments);

        loadCakeTypes();
    }

    private void loadCakeTypes() {

        new AsyncTask<Void, Void, List<CakeType>>() {
            @Override
            protected void onPreExecute() {
                progressDialog = ProgressDialog.show(CreateCake.this, "Please wait ...", "Loading Categories", true);
            }

            @Override
            protected List<CakeType> doInBackground(Void... params) {
                return new CakeType(CreateCake.this).getCakeTypes();
            }

            @Override
            protected void onPostExecute(final List<CakeType> cakeTypes) {
                cakeType.setAdapter(null);

                // Create an ArrayAdapter using the cake type array
                final ArrayAdapter<CakeType> adapter = new ArrayAdapter<CakeType>(CreateCake.this, android.R.layout.simple_gallery_item, cakeTypes);
                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

                // Apply the adapter to the spinner
                cakeType.setAdapter(adapter);

                loadRulesCallBackFunction();
            }
        }.execute(null, null, null);
    }

    private void loadRulesCallBackFunction() {
        if (cakeId != null) {
            // update cake
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doAction(true);
                }
            });

            // load the age range options
            Spinner spinner = (Spinner) findViewById(R.id.ageRange);
            final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.ageRange_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            // load current information
            new AsyncTask<Void, Void, Cake>() {

                List<CakeType> cakeTypes = null;

                @Override
                protected Cake doInBackground(Void... params) {
                    Cake cake = new Cake(CreateCake.this);
                    cakeTypes = new CakeType(CreateCake.this).getCakeTypes();
                    cakeSelected = cake.getCake(cakeId);
                    return cakeSelected;
                }

                @Override
                protected void onPostExecute(Cake cake) {
                    // fill the input fields
                    ageRange.setSelection(adapter.getPosition(cake.ageRange));
                    colors.setText(cake.colors);
                    orderName.setText(cake.name);
                    cakeEvent.setText(cake.cakeEvent);
                    writings.setText(cake.writing);
                    comments.setText(cake.comments);
                    CakeType cakeTypeSelected = null;
                    for (CakeType cakeType : cakeTypes) {
                        if (cake.type.id == cakeType.id)
                            cakeTypeSelected = cakeType;
                    }
                    ArrayAdapter<CakeType> cakeTypeAdapter = new ArrayAdapter<CakeType>(CreateCake.this, android.R.layout.simple_gallery_item, cakeTypes);
                    cakeType.setSelection(cakeTypeAdapter.getPosition(cakeTypeSelected));

                    loadRules();
                }
            }.execute(null, null, null);
        } else {
            // save cake
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doAction(false);
                }
            });
        }
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
        final Integer databaseId = ((CakeType) ((Spinner) findViewById(R.id.cakeType)).getSelectedItem()).id;
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
                RelativeLayout layout = (RelativeLayout) findViewById(R.id.container);
                layout.removeAllViews();

                for (int i = 0; i < rules.categories.size(); i++) {
                    // Create text views at runtime
                    TextView textView = new TextView(CreateCake.this);
                    textView.setText(rules.categories.get(i).name + ": ");
                    // define layout for text view
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 100 * i, 0, 0);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    textView.setLayoutParams(layoutParams);
                    textView.setTextAppearance(CreateCake.this, R.style.boldText);
                    // add the text view to the parent view
                    layout.addView(textView);

                    // Create spinners at runtime
                    Spinner spinner = new Spinner(CreateCake.this);
                    // create adapter with the options to populate the spinner
                    ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(CreateCake.this, android.R.layout.simple_gallery_item, rules.categories.get(i).items);
                    // enable multiple choice if the max quantity is greater than 1
                    if (rules.categories.get(i).maxQuantity > 1)
                        adapter.setDropDownViewResource(android.R.layout.select_dialog_multichoice);
                    else
                        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                    spinner.setAdapter(adapter);

                    if (cakeSelected != null) {
                        Item itemSelected = null;
                        for (Item item : cakeSelected.items) {
                            spinner.setSelection(adapter.getPosition(item));
                        }
                    }

                    // define layout
                    layoutParams = new RelativeLayout.LayoutParams(600, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 100 * i, 0, 0);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    spinner.setLayoutParams(layoutParams);
                    // add spinner to the parent view
                    layout.addView(spinner);
                }

                cakeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Integer databaseId = ((CakeType) cakeType.getSelectedItem()).id;
                        loadRules();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                progressDialog.dismiss();
            }
        }.execute(null, null, null);
    }

    public void doAction(final boolean isUpdate) {
        // enable progress bar
        progressDialog = ProgressDialog.show(CreateCake.this, "Please wait ...", "Saving Cake", true);

        Response colorsResponse = helper.validateRequiredText(colors.getText().toString(), "Colors");
        Response orderNameResponse = helper.validateRequiredText(orderName.getText().toString(), "Order Name");

        // validate the user information before call the web service
        if (!orderNameResponse.success) {
            orderName.requestFocus();
            helper.displayMessage(orderNameResponse.message);
            progressDialog.dismiss();
        } else if (!colorsResponse.success) {
            colors.requestFocus();
            helper.displayMessage(colorsResponse.message);
            progressDialog.dismiss();
        } else {
            // call the web service
            new AsyncTask<Void, Void, Response>() {
                @Override
                protected Response doInBackground(Void... params) {
                    // get cake type id
                    Integer cakeTypeId = ((CakeType) ((Spinner) findViewById(R.id.cakeType)).getSelectedItem()).id;
                    // get selected items
                    List<String> selectedItems = new ArrayList<>();
                    RelativeLayout parentRelativeLayout = (RelativeLayout) findViewById(R.id.container);
                    for (int i = 0; i < parentRelativeLayout.getChildCount(); i++) {
                        View child = parentRelativeLayout.getChildAt(i);
                        if (child instanceof Spinner) {
                            // get selected item
                            Spinner childSpinner = (Spinner) child;
                            Integer selectedId = ((Item) childSpinner.getSelectedItem()).id;
                            selectedItems.add(selectedId.toString());
                        }
                    }
                    // create a new instance of cake
                    Cake newCake = new Cake(CreateCake.this);

                    if (!isUpdate) {
                        return newCake.createCake(ageRange.getSelectedItem().toString(), colors.getText().toString(),
                                writings.getText().toString(), comments.getText().toString(), cakeTypeId.toString(), selectedItems,
                                cakeEvent.getText().toString(), orderName.getText().toString());
                    } else {
                        return newCake.updateCake(cakeId, ageRange.getSelectedItem().toString(), colors.getText().toString(),
                                writings.getText().toString(), comments.getText().toString(), cakeTypeId.toString(), selectedItems,
                                cakeEvent.getText().toString(), orderName.getText().toString());
                    }
                }

                @Override
                protected void onPostExecute(Response response) {
                    // disable progress bar
                    helper.displayMessage(response.message);

                    if (response.success) {
                        Intent intent = new Intent(CreateCake.this, OrderDetails.class);
                        Integer cakeId = response.createdId;
                        intent.putExtra("cakeId", cakeId.toString());
                        startActivity(intent);
                    }
                    progressDialog.dismiss();
                }
            }.execute(null, null, null);
        }
    }
}
