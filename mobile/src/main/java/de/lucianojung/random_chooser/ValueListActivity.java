package de.lucianojung.random_chooser;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.PointerIcon;
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

import java.util.ArrayList;
import java.util.List;

public class ValueListActivity<T extends Adapter> extends AppCompatActivity {
    private ArrayAdapter<ChooserValue> valueAdapter;
    private enum DialogType{
        EDIT, ADD, REMOVE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valuelist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_value);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TaskList
        valueAdapter = getValueAdapter();
        if (getIntent() != null){
            Chooser chooser = (Chooser) getIntent().getSerializableExtra("Chooser");
            for (ChooserValue value: chooser.getValueList()) {
                valueAdapter.add(value);
            }
        }

        ListView listView = findViewById(R.id.value_list);
        listView.setAdapter(valueAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // System.out.println(valueAdapter.getItem(position).getValue());
                showDialog(DialogType.EDIT, valueAdapter.getItem(position));
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                showDialog(DialogType.REMOVE, valueAdapter.getItem(position));
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

    private CharSequence chooseRandomValue() {
        if (valueAdapter.getCount() == 0) return "";
        //get all data and default elements
        List<ChooserValue> valueList = new ArrayList<>();
        int totalWeighting = 0;
        for (int i = 0; i < valueAdapter.getCount(); i++) {
            valueList.add(valueAdapter.getItem(i));
            totalWeighting += valueList.get(i).getWeighting();
        }
        int random = (int)(Math.random() * totalWeighting);

        //algorithm = subtract weighting && i++ until <0 and return value at i
        int i = -1;
        do {
            i++;
            random -= valueList.get(i).getWeighting();
        } while(random >= 0);
        return Integer.toString(valueList.get(i).getValue());
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog(DialogType dialogType, final ChooserValue chooserValue){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.new_value_dialog, null);

        final EditText value = view.findViewById(R.id.edit_value);
        final EditText weighting = view.findViewById(R.id.edit_weighting);

        switch (dialogType) {
            case ADD:
                dialogBuilder
                        .setView(view)
                        .setTitle(getString(R.string.dialog_title_create_value))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (value.getText() != null && value.getText().toString().length() > 0
                                        && weighting.getText() != null && weighting.getText().toString().length() > 0) {
                                    try {
                                        valueAdapter.add(new ChooserValue(Integer.parseInt(value.getText().toString()), Integer.parseInt(weighting.getText().toString())));
                                    } catch (Exception e) {
                                        Toast.makeText(ValueListActivity.this, getString(R.string.not_valid_value_warning), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(ValueListActivity.this, getString(R.string.empty_string_warning), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;

            case EDIT:
                value.setText(Integer.toString(chooserValue.getValue()));
                weighting.setText(Integer.toString(chooserValue.getWeighting()));

                dialogBuilder
                        .setView(view)
                        .setTitle(getString(R.string.dialog_title_create_value))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (value.getText() != null && value.getText().toString().length() > 0
                                        && weighting.getText() != null && weighting.getText().toString().length() > 0) {
                                    try {
                                        valueAdapter.remove(chooserValue);
                                        valueAdapter.add(new ChooserValue(Integer.parseInt(value.getText().toString()), Integer.parseInt(weighting.getText().toString())));
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
                                valueAdapter.remove(chooserValue);
                            }
                        });
                break;
            default:
                break;
        }

        dialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private ArrayAdapter<ChooserValue> getValueAdapter(){
        final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        ArrayAdapter<ChooserValue> adapter = new ArrayAdapter<ChooserValue>(this, R.layout.listitem_view_chooser){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view;
                if (convertView == null)
                    view = inflater.inflate(R.layout.listitem_view_value, parent, false);
                else
                    view = convertView;

                TextView valueText = view.findViewById(R.id.name_value);
                TextView weightingText = view.findViewById(R.id.name_weighting);

                ChooserValue value = getItem(position);

                valueText.setText(Integer.toString(value.getValue()));
                weightingText.setText(Integer.toString(value.getWeighting()));

                return view;
            }
        };
        return adapter;
    }
}
