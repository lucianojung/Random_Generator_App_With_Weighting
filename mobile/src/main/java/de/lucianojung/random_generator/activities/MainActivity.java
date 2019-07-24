package de.lucianojung.random_generator.activities;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import de.lucianojung.random_generator.database.AppDatabase;
import de.lucianojung.random_generator.fragments.GeneratorsListFragment;
import de.lucianojung.random_generator.persistence.generator.Generator;
import de.lucianojung.random_generator.R;

public class MainActivity<T extends Adapter> extends AppCompatActivity implements GeneratorsListFragment.OnItemSelectedListener {

//    private ArrayAdapter<Generator> generatorArrayAdapter;
    private ListView listView;
    private AppDatabase database;
    GeneratorsListFragment generatorsFragment;



    private enum DialogType{
        ADD, EDIT, REMOVE, ABOUT;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_chooser);
        setSupportActionBar(toolbar);

        //create and get Database
        database = AppDatabase.getAppDatabase(this);

        this.showFragment(savedInstanceState);

        //TaskList
//        generatorArrayAdapter = generatorsFragment.getGeneratorsArrayAdapter(database);

        listView = generatorsFragment.getView().findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                onItemSelected(adapterView, view, position);
//                Intent valueListIntent = new Intent(view.getContext(), ValueListActivity.class);
//                valueListIntent.putExtra("Generator", generatorArrayAdapter.getItem(adapterView.getPositionForView(view)));
//                startActivity(valueListIntent);
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
//                showDialog(DialogType.REMOVE, generatorArrayAdapter.getItem(position));
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_chooser);
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

    private void showFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            generatorsFragment = ((GeneratorsListFragment) this.getSupportFragmentManager().findFragmentById(R.id.list_fragment));

            Bundle bundle = new Bundle();
            bundle.putSerializable("database", database);
            generatorsFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.list_fragment, generatorsFragment, null)
                    .commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position) {
        if (generatorsFragment != null && generatorsFragment.isInLayout()) {
            Toast.makeText(this, "ON ITEM CLICKED WITH FRAGMENT", Toast.LENGTH_SHORT).show();
            Intent valueListIntent = new Intent(view.getContext(), ValueListActivity.class);
//            valueListIntent.putExtra("Generator", generatorArrayAdapter.getItem(adapterView.getPositionForView(view)));
            startActivity(valueListIntent);
        }
    }


    @Override
    public void onStop() {
//        generatorArrayAdapter.clear();
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    private void showDialog(DialogType dialogType, final Generator generator){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_generator, null);

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
                                insertRandomGenerator(Generator.builder().id(0).name(generatorName).build());
                            }
                        });
                break;

            case EDIT:
                text.setText(generator.getName());

                dialogBuilder
                        .setView(view)
                        .setTitle(getString(R.string.dialog_title_create_value))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (text.getText().toString().length() > 0) {
                                    generator.setName(text.getText().toString());
                                    updateRandomGenerator(generator);
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
                                deleteRandomGenerator(generator);
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


    //Database Handler

    private void insertRandomGenerator(final Generator generator) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.randomGeneratorDAO().insert(generator);
                return null;
            }
        }.execute();
//        loadDatabase();
    }

    private void deleteRandomGenerator(final Generator generator) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.randomGeneratorDAO().delete(generator);
                return null;
            }
        }.execute();
//        generatorArrayAdapter.remove(generator);
    }

    private void updateRandomGenerator(final Generator generator) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.randomGeneratorDAO().update(generator);
                return null;
            }
        }.execute();
//        generatorArrayAdapter.notifyDataSetChanged();
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