package de.lucianojung.random_generator.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import de.lucianojung.random_generator.R;
import de.lucianojung.random_generator.database.AppDatabase;
import de.lucianojung.random_generator.persistence.generator.RandomGenerator;

public class MainActivity<T extends Adapter> extends AppCompatActivity {

    private ArrayAdapter<RandomGenerator> generatorArrayAdapter;
    private ListView listView;
    private AppDatabase database;
    private enum DialogType{
        ADD, EDIT, REMOVE, ABOUT
    }
    boolean editViewEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_chooser);
        setSupportActionBar(toolbar);

        //TaskList
        generatorArrayAdapter = getGeneratorArrayAdapter();
        //create and get Database
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                showDialog(DialogType.REMOVE, generatorArrayAdapter.getItem(position));
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_chooser);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                AddGeneratorDialogFragment addGeneratorDialogFragment = AddGeneratorDialogFragment.newInstance("Some Title");
//                addGeneratorDialogFragment.show(fragmentManager, getString(R.string.dialog_title_create_generator));
                showDialog(DialogType.ADD, null);
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
        generatorArrayAdapter.clear();
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
    }

    @Override
    public void onStop() {
        generatorArrayAdapter.clear();
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
        if (id == R.id.action_about) {
            showDialog(DialogType.ABOUT, null);
            return true;
        } else if (id == R.id.action_edit) {
            if (editViewEnabled) {
                generatorArrayAdapter = getGeneratorArrayAdapter();
            } else {
                generatorArrayAdapter = getEditGeneratorArrayAdapter();
            }
            listView.setAdapter(generatorArrayAdapter);
            loadDatabase();

            editViewEnabled = !editViewEnabled;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    public void onEditButtonClicked(View view){
        view.getParent();
        TextView textView = ((ConstraintLayout) view.getParent()).findViewById(R.id.name_chooser);
        showEditRandomGenerator(textView.getText().toString());
    }

    private void showDialog(DialogType dialogType, final RandomGenerator randomGenerator){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_generator_dialog, null);

        final EditText text = view.findViewById(R.id.edit_generator_title);
        if (text == null){
            Toast.makeText(MainActivity.this, getString(R.string.null_pointer_warning), Toast.LENGTH_SHORT).show();
        }

        switch (dialogType) {
            case ADD:
                dialogBuilder
                        .setView(view)
                        .setTitle(getString(R.string.dialog_title_create_generator))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String generatorName = (text.getText().toString().length() > 0) ?
                                        text.getText().toString()
                                        : getString(R.string.default_generator);
                                insertRandomGenerator(RandomGenerator.builder().id(0).name(generatorName).build());
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
                                if (text.getText().toString().length() > 0) {
                                    randomGenerator.setName(text.getText().toString());
                                    updateRandomGenerator(randomGenerator);

                                    loadDatabase();
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
                dialogBuilder
                        .setMessage(getString(R.string.action_about_text))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//do nothing
                                }}).show();
                return;
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

        return new ArrayAdapter<RandomGenerator>(MainActivity.this, R.layout.listitem_view_generator){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view;
                if (convertView == null)
                    view = inflater.inflate(R.layout.listitem_view_generator, parent, false);
                else
                    view = convertView;

                TextView text1 = view.findViewById(R.id.name_chooser);
                final RandomGenerator randomGenerator = getItem(position);
                text1.setText(randomGenerator.getName());

                return view;
            }
        };
    }


    //returns ArrayAdapter to save Choosers
    private ArrayAdapter<RandomGenerator> getEditGeneratorArrayAdapter(){
        final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        ArrayAdapter<RandomGenerator> adapter = new ArrayAdapter<RandomGenerator>(this, R.layout.listitem_view_generator_edit){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view;
                if (convertView == null)
                    view = inflater.inflate(R.layout.listitem_view_generator_edit, parent, false);
                else
                    view = convertView;

                TextView text1 = view.findViewById(R.id.name_chooser);
                final RandomGenerator randomGenerator = getItem(position);
                text1.setText(randomGenerator.getName());

                return view;
            }
        };
        return adapter;
    }

    //Database Handler

    private void showEditRandomGenerator(final String name) {
        new AsyncTask<Void, Void, List<RandomGenerator>>(){
            @Override
            protected List<RandomGenerator> doInBackground(Void... params) {
                return database.randomGeneratorDAO().getAllRandomGenerators();
            }

            @Override
            protected void onPostExecute(List<RandomGenerator> randomGenerators) {
                super.onPostExecute(randomGenerators);
                for (RandomGenerator generator : randomGenerators) {
                    if (generator.getName().equals(name)) {
                        showDialog(DialogType.EDIT, generator);
                        break;
                    }
                }
            }
        }.execute();
    }

    private void insertRandomGenerator(final RandomGenerator randomGenerator) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.randomGeneratorDAO().insert(randomGenerator);
                return null;
            }
        }.execute();
        loadDatabase();
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

    private void updateRandomGenerator(final RandomGenerator randomGenerator) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.randomGeneratorDAO().update(randomGenerator);
                return null;
            }
        }.execute();
        generatorArrayAdapter.notifyDataSetChanged();
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