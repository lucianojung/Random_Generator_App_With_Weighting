package de.lucianojung.random_generator.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.lucianojung.random_generator.R;
import de.lucianojung.random_generator.database.AppDatabase;
import de.lucianojung.random_generator.persistence.generator.RandomGenerator;
import de.lucianojung.random_generator.persistence.settings.Setting;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SettingsListFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_listfragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<Setting> settingsAdapter = getSettingsListAdapter();

        this.setListAdapter(settingsAdapter);
    }

    public ArrayAdapter<Setting> getSettingsListAdapter() {
        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);

        ArrayAdapter<Setting> adapter = new ArrayAdapter<Setting>(getActivity(), R.layout.lititem_view_setting){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view;
                if (convertView == null)
                    view = inflater.inflate(R.layout.listitem_view_generator, parent, false);
                else
                    view = convertView;

                TextView text1 = view.findViewById(R.id.tv_setting_name);

                Setting setting = getItem(position);

                assert setting != null;
                text1.setText(setting.getName());

                return view;
            }
        };
        return adapter;
    }
}
