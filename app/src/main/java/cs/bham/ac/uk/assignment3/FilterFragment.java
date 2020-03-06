package cs.bham.ac.uk.assignment3;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    CallFilterRefresh mCallback;

    public interface CallFilterRefresh {
        public void refresh();
    }

    public FilterFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner spinner = (Spinner) view.findViewById(R.id.mealSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.mealArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner = (Spinner) view.findViewById(R.id.orderSpinner);
        adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.orderArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("Settings",Context.MODE_PRIVATE);
        String prefMeal = sharedPref.getString("mealPref", "Any");
        ((Spinner) view.findViewById(R.id.mealSpinner)).setSelection(prefMeal.equals("Any") ? 0 : prefMeal.equals("Breakfast") ? 1 : prefMeal.equals("Lunch") ? 2 : 3);
        ((Spinner) view.findViewById(R.id.mealSpinner)).setOnItemSelectedListener(this);

        String prefOrder = sharedPref.getString("orderPref", "Default");
        ((Spinner) view.findViewById(R.id.orderSpinner)).setSelection(prefOrder.equals("Default") ? 0 : prefOrder.equals("Asc") ? 1 : 2);
        ((Spinner) view.findViewById(R.id.orderSpinner)).setOnItemSelectedListener(this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Settings",Context.MODE_PRIVATE).edit();
        switch (parent.getId()) {
            case R.id.mealSpinner:
                editor.putString("mealPref", parent.getItemAtPosition(position).toString());
                break;
            case R.id.orderSpinner:
                editor.putString("orderPref", parent.getItemAtPosition(position).toString());
                break;
        }
        editor.apply();
        mCallback.refresh();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (CallFilterRefresh) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement TextClicked");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }
}
