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

public class MainActivity<T extends Adapter> extends AppCompatActivity {

    private ArrayAdapter<Chooser> chooserAdapter;
    private ListView listView;
    private enum DialogType{
        ADD, EDIT, REMOVE
    }

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
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // System.out.println(valueAdapter.getItem(position).getValue());
                showDialog(DialogType.EDIT, chooserAdapter.getItem(position));
            }
        });*/
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                showDialog(DialogType.REMOVE, chooserAdapter.getItem(position));
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_chooser);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DialogType.ADD, null);
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

    private void showDialog(DialogType dialogType, final Chooser chooser){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.new_chooser_dialog, null);

        final EditText text = view.findViewById(R.id.edit_text);

        switch (dialogType) {
            case ADD:
                dialogBuilder
                        .setView(view)
                        .setTitle(getString(R.string.dialog_title_create_chooser))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (text.getText() != null && text.getText().toString().length() > 0) {
                                    chooserAdapter.add(new Chooser(text.getText().toString()));
                                } else {
                                    Toast.makeText(MainActivity.this, getString(R.string.empty_string_warning), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;

            case EDIT:
                text.setText(chooser.getName());

                dialogBuilder
                        .setView(view)
                        .setTitle(getString(R.string.dialog_title_create_value))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (text.getText() != null && text.getText().toString().length() > 0) {
                                    chooserAdapter.remove(chooser);
                                    chooserAdapter.add(new Chooser(text.getText().toString()));
                                } else {
                                    Toast.makeText(MainActivity.this, getString(R.string.empty_string_warning), Toast.LENGTH_SHORT).show();
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
                                chooserAdapter.remove(chooser);
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
            dice.getValueList().add(new ChooserValue(1,Integer.toString(i+1),1));
            if (i == 0 || i == 5)
                loadedDice.getValueList().add(new ChooserValue(2,Integer.toString(i+1),3));
            else
                loadedDice.getValueList().add(new ChooserValue(2,Integer.toString(i+1),1));
        }
        chooserAdapter.addAll(dice, loadedDice);
    }
}
