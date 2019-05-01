package de.lucianojung.random_generator.Dialogs;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import de.lucianojung.random_generator.R;
import de.lucianojung.random_generator.RandomGenerator;

public class GeneratorDialogFragment extends DialogFragment {

    public static final String GENERATOR_KEY = "GENERATOR_KEY";

//    public GeneratorDialogFragment(){}



    public static GeneratorDialogFragment newInstance(RandomGenerator generator){
        GeneratorDialogFragment fragment = new GeneratorDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(GENERATOR_KEY, generator);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.dialog_fragment_main, container);

        EditText etGeneratorName = rootView.findViewById(R.id.generator_title);
        RandomGenerator generator = getArguments().getParcelable(GENERATOR_KEY);

        etGeneratorName.setText(generator.getName());
        //todo add EditText and setText here to pass generatorType

        MaterialButton buttonOk = rootView.findViewById(R.id.dialog_ok_button);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        MaterialButton buttonCancel = rootView.findViewById(R.id.dialog_cancel_button);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
//        super.onViewCreated(view, savedInstanceState);
//        editGeneratorText = (EditText) view.findViewById(R.id.edit_generator_title);
//        String title = getArguments().getString("title", "Enter Name");
//        getDialog().setTitle(title);
//        editGeneratorText.requestFocus();
//        getDialog().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//    }

    /*@Override
    public Dialog onCreateDialog(Bundle b) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_generator_dialog, null);

        final EditText text = view.findViewById(R.id.edit_text);
        dialogBuilder
                .setView(view)
                .setTitle(getString(R.string.dialog_title_create_generator))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (text.getText() != null && text.getText().toString().length() > 0) {
                            // add: (new RandomGenerator(0, text.getText().toString()));
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.empty_string_warning), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return dialogBuilder.create();
    }*/
}
