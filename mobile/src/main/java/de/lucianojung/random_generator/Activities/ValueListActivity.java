package de.lucianojung.random_generator.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import de.lucianojung.random_generator.Database.AppDatabase;
import de.lucianojung.random_generator.Model.Generator.RandomGenerator;
import de.lucianojung.random_generator.Model.Variable.RandomVariable;
import de.lucianojung.random_generator.R;

public class ValueListActivity<T extends Adapter> extends AppCompatActivity {
    private ArrayAdapter<RandomVariable> variableAdapter;
    private AppDatabase database;

    private enum DialogType{
        EDIT, ADD, REMOVE, ABOUT
    }
    RandomGenerator parentRandomGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valuelist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_value);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TaskList
        variableAdapter = getVariableAdapter();
        //create Database;
        database = AppDatabase.getAppDatabase(this);
        if (getIntent() != null){
            parentRandomGenerator = (RandomGenerator) getIntent().getSerializableExtra("RandomGenerator");
        }

        ListView listView = findViewById(R.id.value_list);
        listView.setAdapter(variableAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // System.out.println(variableAdapter.getItem(position).getValue());
                showDialog(DialogType.EDIT, variableAdapter.getItem(position));
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                showDialog(DialogType.REMOVE, variableAdapter.getItem(position));
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_value);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DialogType.ADD, null);
            }
        });

        Button chooseRandomButton = (Button) findViewById(R.id.choose_random_button);
        chooseRandomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button) v).setText(chooseRandomValue());
            }
        });
    }

    @Override
    public void onStart() {
        loadDatabase();
        super.onStart();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadDatabase() {
        variableAdapter.clear();
        new AsyncTask<Void, Void, List<RandomVariable>>(){
            @Override
            protected List<RandomVariable> doInBackground(Void... params) {
                return database.randomVariableDAO().getAllRandomVariablesByGID(parentRandomGenerator.getGid());
            }

            @Override
            protected void onPostExecute(List items){
                variableAdapter.addAll(items);
            }
        }.execute();
    }

    private CharSequence chooseRandomValue() {
        if (variableAdapter.getCount() == 0) return "";
        //get all data and default elements
        List<RandomVariable> valueList = new ArrayList<>();
        for (int i = 0; i < variableAdapter.getCount(); i++) {
            valueList.add(variableAdapter.getItem(i));
        }
        int random = (int)(Math.random() * getTotalWeighting());

        //algorithm = subtract weighting && i++ until <0 and return value at i
        int i = -1;
        do {
            i++;
            random -= valueList.get(i).getWeighting();
        } while(random >= 0);
        return valueList.get(i).getValue();
    }

    private int getTotalWeighting(){
        int totalWeighting = 0;
        for (int i = 0; i < variableAdapter.getCount(); i++) {
            totalWeighting += variableAdapter.getItem(i).getWeighting();
        }
        return totalWeighting;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            showDialog(DialogType.ABOUT, null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        variableAdapter.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    private void showDialog(DialogType dialogType, final RandomVariable randomVariable){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_variable_dialog, null);

        final EditText value = view.findViewById(R.id.edit_value);
        final EditText weighting = view.findViewById(R.id.edit_weighting);

        switch (dialogType) {
            case ADD:
                weighting.setText(Integer.toString(1));
                dialogBuilder
                        .setView(view)
                        .setTitle(getString(R.string.dialog_title_create_value))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (value.getText() != null && value.getText().toString().length() > 0
                                        && weighting.getText() != null && weighting.getText().toString().length() > 0) {
                                    try {
                                        insertRandomVariable(new RandomVariable(
                                                0, parentRandomGenerator.getGid(),
                                                value.getText().toString(),
                                                Integer.parseInt(weighting.getText().toString())));
                                    } catch (Exception e) {
                                        Toast.makeText(ValueListActivity.this,
                                                getString(R.string.not_valid_value_warning), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(ValueListActivity.this,
                                            getString(R.string.empty_string_warning), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;

            case EDIT:
                value.setText(randomVariable.getValue());
                weighting.setText(Integer.toString(randomVariable.getWeighting()));

                dialogBuilder
                        .setView(view)
                        .setTitle(getString(R.string.dialog_title_create_value))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (value.getText() != null && value.getText().toString().length() > 0
                                        && weighting.getText() != null && weighting.getText().toString().length() > 0) {
                                    try {
                                        randomVariable.setValue(value.getText().toString());
                                        randomVariable.setWeighting(Integer.parseInt(weighting.getText().toString()));
                                        updateRandomVariable(randomVariable);
                                    } catch (Exception e) {
                                        Toast.makeText(ValueListActivity.this, getString(R.string.not_valid_value_warning), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(ValueListActivity.this, getString(R.string.empty_string_warning), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;

            case REMOVE:
                dialogBuilder
                        .setMessage(getString(R.string.delete_item_message))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteRandomVariable(randomVariable);
                            }
                        });
                break;
            default:
                dialogBuilder
                        .setMessage(getString(R.string.action_about_text))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).show();
                return;
        }

        dialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private ArrayAdapter<RandomVariable> getVariableAdapter(){
        final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        return new ArrayAdapter<RandomVariable>(ValueListActivity.this, R.layout.listitem_view_generator){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view;
                if (convertView == null)
                    view = inflater.inflate(R.layout.listitem_view_variable, parent, false);
                else
                    view = convertView;

                TextView valueText = view.findViewById(R.id.name_value);
                TextView weightingText = view.findViewById(R.id.name_weighting);

                RandomVariable value = getItem(position);

                assert value != null;
                valueText.setText(value.getValue());
                NumberFormat defaultFormat = NumberFormat.getPercentInstance();
                defaultFormat.setMinimumFractionDigits(1);
                weightingText.setText(defaultFormat.format(((double)value.getWeighting()/(double)getTotalWeighting())));

                return view;
            }
        };
    }

    //Database Handler

    private void insertRandomVariable(final RandomVariable randomVariable) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.randomVariableDAO().insertAll(randomVariable);
                return null;
            }
        }.execute();
        loadDatabase();
    }

    private void deleteRandomVariable(final RandomVariable randomVariable) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.randomVariableDAO().delete(randomVariable);
                return null;
            }
        }.execute();
        variableAdapter.remove(randomVariable);
    }

    private void updateRandomVariable(final RandomVariable randomVariable) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.randomVariableDAO().update(randomVariable);
                return null;
            }
        }.execute();
        variableAdapter.notifyDataSetChanged();
    }
}
