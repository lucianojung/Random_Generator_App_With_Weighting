package de.lucianojung.random_generator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.lucianojung.random_generator.R;
import de.lucianojung.random_generator.persistence.generator.RandomGenerator;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class GeneratorsListFragment extends ListFragment {

    private OnItemSelectedListener listener;

    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        public void onItemSelected(AdapterView<?> adapterView, View view, int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.generators_listfragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<RandomGenerator> generatorsAdapter = getGeneratorsArrayAdapter();

        this.setListAdapter(generatorsAdapter);
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

    public void onSomeClick(AdapterView<?> adapterView, View view, int position) {
        listener.onItemSelected(adapterView, view, position);
    }

    //returns ArrayAdapter to save Choosers
    public ArrayAdapter<RandomGenerator> getGeneratorsArrayAdapter(){

        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);

        ArrayAdapter<RandomGenerator> adapter = new ArrayAdapter<RandomGenerator>(getActivity(), R.layout.listitem_view_generator){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view;
                if (convertView == null)
                    view = inflater.inflate(R.layout.listitem_view_generator, parent, false);
                else
                    view = convertView;

                TextView text1 = view.findViewById(R.id.name_chooser);

                RandomGenerator randomGenerator = getItem(position);

                assert randomGenerator != null;
                text1.setText(randomGenerator.getName());

                return view;
            }
        };
        return adapter;
    }
}
