package de.lucianojung.random_generator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import java.util.List;

public class MainActivity<T extends Adapter> extends AppCompatActivity {

    private ArrayAdapter<RandomGenerator> generatorArrayAdapter;
    private ListView listView;
    private AppDatabase database;
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
        generatorArrayAdapter = getGeneratorArrayAdapter();
        //create Database;
        database = AppDatabase.getAppDatabase(this);

        listView = findViewById(R.id.chooser_list);
        listView.setAdapter(generatorArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent valueListIntent = new Intent(view.getContext(), ValueListActivity.class);
                valueListIntent.putExtra("RandomGenerator", generatorArrayAdapter.getItem(adapterView.getPositionForView(view)));
                startActivity(valueListIntent);
            }
        });
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // System.out.println(valueAdapter.getItem(position).getValue());
                showDialog(DialogType.EDIT, generatorArrayAdapter.getItem(position));
            }
        });*/
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                showDialog(DialogType.REMOVE, generatorArrayAdapter.getItem(position));
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
    public void onStart() {
        //load Database
        new AsyncTask<Void, Void, List<RandomGenerator>>(){
            @Override
            protected List<RandomGenerator> doInBackground(Void... params) {
                return database.randomGeneratorDAO().getAllRandomGenerators();
            }

            @Override
            protected void onPostExecute(List items){
                generatorArrayAdapter.addAll(items);
            }
        }.execute();
        super.onStart();
    }

    @Override
    public void onStop() {

        super.onStop();
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

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    private void showDialog(DialogType dialogType, final RandomGenerator randomGenerator){
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
                                    insertRandomGenerator(new RandomGenerator(0, text.getText().toString()));
                                } else {
                                    Toast.makeText(MainActivity.this, getString(R.string.empty_string_warning), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;

            case EDIT:
                text.setText(randomGenerator.getName());

                dialogBuilder
                        .setView(view)
                        .setTitle(getString(R.string.dialog_title_create_value))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (text.getText() != null && text.getText().toString().length() > 0) {
                                    deleteRandomGenerator(randomGenerator);
                                    insertRandomGenerator(new RandomGenerator(0, text.getText().toString()));
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
                                deleteRandomGenerator(randomGenerator);
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
    private ArrayAdapter<RandomGenerator> getGeneratorArrayAdapter(){
        final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        ArrayAdapter<RandomGenerator> adapter = new ArrayAdapter<RandomGenerator>(this, R.layout.listitem_view_chooser){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view;
                if (convertView == null)
                    view = inflater.inflate(R.layout.listitem_view_chooser, parent, false);
                else
                    view = convertView;

                TextView text1 = view.findViewById(R.id.name_chooser);

                RandomGenerator randomGenerator = getItem(position);

                text1.setText(randomGenerator.getName());

                return view;
            }
        };
        return adapter;
    }

    //Database Handler

    private void insertRandomGenerator(final RandomGenerator randomGenerator) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.randomGeneratorDAO().insertAll(randomGenerator);
                return null;
            }
        }.execute();
        generatorArrayAdapter.add(randomGenerator);
    }

    private void deleteRandomGenerator(final RandomGenerator randomGenerator) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.randomGeneratorDAO().delete(randomGenerator);
                return null;
            }
        }.execute();
        generatorArrayAdapter.remove(randomGenerator);
    }
}

/*execute Async in seperate method

final Executor executor = Executors.newFixedThreadPool(2);
MovieDao dao = db.movieDao();

public void addMovie(Movie m){
    executor.execute(() -> {
        dao.insert(m);
    });
}*/