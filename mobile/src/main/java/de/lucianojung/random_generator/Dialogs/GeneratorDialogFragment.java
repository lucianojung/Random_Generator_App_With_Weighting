package de.lucianojung.random_generator.Dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.lucianojung.random_generator.R;
import de.lucianojung.random_generator.RandomGenerator;

public class GeneratorDialogFragment extends DialogFragment {

    public static final String GENERATOR_KEY = "GENERATOR_KEY";
    public static final int MAXNAMELENGTH = 20;
    private EditText etGeneratorName;
    private RandomGenerator generator;
    private DataEntryListener dataEntryListener;

    public static GeneratorDialogFragment newInstance(RandomGenerator generator) {
        GeneratorDialogFragment fragment = new GeneratorDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(GENERATOR_KEY, generator);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataEntryListener = (DataEntryListener) context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_main, container);

        etGeneratorName = rootView.findViewById(R.id.generator_title);
        TextView dialogTitle = rootView.findViewById(R.id.dialog_title);
        assert getArguments() != null;
        generator = getArguments().getParcelable(GENERATOR_KEY);

        if (generator != null) {
            String generatorName = generator.getName();
            etGeneratorName.setText(generatorName);
            dialogTitle.setText(getString(R.string.edit) + generatorName);
            //todo add EditText and setText here to pass generatorType
        } else {
            dialogTitle.setText(getString(R.string.dialog_title_create_generator));
        }
        handleButtons(rootView);
        return rootView;
    }

    private void handleButtons(View rootView) {
        MaterialButton buttonOk = rootView.findViewById(R.id.dialog_ok_button);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etGeneratorName.getText() == null && etGeneratorName.getText().toString().length() <= 0) {
                    Toast.makeText(getActivity(), getString(R.string.empty_string_warning), Toast.LENGTH_SHORT).show();
                } else if (etGeneratorName.getText().toString().length() > MAXNAMELENGTH) {
                    Toast.makeText(getActivity(), "too long", Toast.LENGTH_SHORT).show();
                } else {
                    saveData();
                    dismiss();
                }
            }
        });
        MaterialButton buttonCancel = rootView.findViewById(R.id.dialog_cancel_button);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void saveData() {
        if (generator == null)
            generator = new RandomGenerator(0, "");
        generator.setName(etGeneratorName.getText().toString());
        dataEntryListener.onDataEntryComplete(generator, generator == null);
    }

    public interface DataEntryListener {
        void onDataEntryComplete(RandomGenerator generator, boolean newGenerator);
    }
}
