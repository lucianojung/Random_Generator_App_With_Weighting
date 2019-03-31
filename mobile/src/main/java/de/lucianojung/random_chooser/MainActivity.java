package de.lucianojung.random_chooser;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class MainActivity<T extends Adapter> extends AppCompatActivity {

    private ArrayAdapter<Chooser> chooserAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chooser);
        setSupportActionBar(toolbar);

        //TaskList
        chooserAdapter = getChooserAdapter();
        addTestData();

        listView = findViewById(R.id.chooser_list);
        listView.setAdapter(chooserAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent valueListIntent = new Intent(view.getContext(), ValueListActivity.class);
                valueListIntent.putExtra("Chooser", chooserAdapter.getItem(adapterView.getPositionForView(view)));
                startActivity(valueListIntent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_chooser);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooserDialog();
            }
        });
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

    //shows Dialog to create a new Chooser
    private void showChooserDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_title_create_chooser));

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.new_chooser_dialog, null);

        final EditText text = view.findViewById(R.id.edit_text);

        builder.setView(view);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (text.getText() != null && text.getText().toString().length() > 0) {
                    chooserAdapter.add(new Chooser(text.getText().toString()));
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.empty_string_warning), Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }

    //returns ArrayAdapter to save Choosers
    private ArrayAdapter<Chooser> getChooserAdapter(){
        final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        ArrayAdapter<Chooser> adapter = new ArrayAdapter<Chooser>(this, R.layout.listitem_view_chooser){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view;
                if (convertView == null)
                    view = inflater.inflate(R.layout.listitem_view_chooser, parent, false);
                else
                    view = convertView;

                TextView text1 = view.findViewById(R.id.name_chooser);

                Chooser chooser = getItem(position);

                text1.setText(chooser.getName());

                return view;
            }
        };
        return adapter;
    }

    private void addTestData() {
        Chooser dice = new Chooser(getResources().getStringArray(R.array.chooser)[0]);
        Chooser loadedDice = new Chooser(getResources().getStringArray(R.array.chooser)[1]);
        for (int i = 0; i < 6; i++) {
            dice.getValueList().add(new ChooserValue(i+1,1));
            if (i == 0 || i == 5)
                loadedDice.getValueList().add(new ChooserValue(i+1,3));
            else
                loadedDice.getValueList().add(new ChooserValue(i+1,1));
        }
        chooserAdapter.addAll(dice, loadedDice);
    }
}
