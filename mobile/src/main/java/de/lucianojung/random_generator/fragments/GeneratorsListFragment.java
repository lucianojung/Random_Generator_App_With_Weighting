package de.lucianojung.random_generator.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.lucianojung.random_generator.R;
import de.lucianojung.random_generator.database.AppDatabase;
import de.lucianojung.random_generator.persistence.generator.Generator;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class GeneratorsListFragment extends ListFragment {

    private OnItemSelectedListener listener;
    ArrayAdapter<Generator> generatorsAdapter;

    //For Design
    @BindView(R.id.fragment_generator_imageView)
    ImageView imageView;
    @BindView(R.id.fragment_generator_textview)
    TextView textView;

    public GeneratorsListFragment() { }

    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        public void onItemSelected(AdapterView<?> adapterView, View view, int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_generators, container, false);
//        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        database = (AppDatabase) getArguments().getSerializable("database");
        AppDatabase database = AppDatabase.getAppDatabase(getActivity());

        generatorsAdapter = getGeneratorsArrayAdapter(database);
//        this.setListAdapter(generatorsAdapter);
        loadDatabase(database);
//        this.setRetainInstance(true);
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    public void onItemClicked(AdapterView<?> adapterView, View view, int position) {
        listener.onItemSelected(adapterView, view, position);
    }

    //returns ArrayAdapter to save Choosers
    public ArrayAdapter<Generator> getGeneratorsArrayAdapter(AppDatabase database){

        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);

        ArrayAdapter<Generator> adapter = new ArrayAdapter<Generator>(getActivity(),
                R.layout.listitem_view_generator, database.randomGeneratorDAO().getAllRandomGenerators().getValue() ){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view;
                if (convertView == null)
                    view = inflater.inflate(R.layout.listitem_view_generator, parent, false);
                else
                    view = convertView;

                TextView text1 = view.findViewById(R.id.fragment_generator_textview);

                Generator generator = getItem(position);

                assert generator != null;
                text1.setText(generator.getName());

                return view;
            }
        };
        return adapter;
    }

    @SuppressLint("StaticFieldLeak")
    private void loadDatabase(final AppDatabase database) {
        this.generatorsAdapter.clear();
        new AsyncTask<Void, Void, List<Generator>>(){
            @Override
            protected List<Generator> doInBackground(Void... params) {
                return database.randomGeneratorDAO().getAllRandomGenerators().getValue();
            }

            @Override
            protected void onPostExecute(List items){
                generatorsAdapter.addAll(items);
            }
        }.execute();
    }
}
