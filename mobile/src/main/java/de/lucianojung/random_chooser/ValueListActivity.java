package de.lucianojung.random_chooser;

import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ValueListActivity<T extends Adapter> extends AppCompatActivity {
    private ArrayAdapter<ChooserValue> valueAdapter;

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

        valueAdapter.add(new ChooserValue(5,5));
        valueAdapter.add(new ChooserValue(4,1));

        ListView listView = findViewById(R.id.value_list);
        listView.setAdapter(valueAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_value);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { showDialog();
            }
        });
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

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Custom Dialog");

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.new_value_dialog, null);

        final EditText value = view.findViewById(R.id.edit_value);
        final EditText weighting = view.findViewById(R.id.edit_weighting);

        builder.setView(view);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (value.getText() != null && value.getText().toString().length() > 0
                        && weighting.getText() != null && weighting.getText().toString().length() > 0) {
                    try {
                        valueAdapter.add(new ChooserValue(Integer.parseInt(value.getText().toString()), Integer.parseInt(weighting.getText().toString())));
                    } catch (Exception e){
                        Toast.makeText(ValueListActivity.this, "Please make sure to enter Numbers only.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ValueListActivity.this, "Please enter something.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

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
