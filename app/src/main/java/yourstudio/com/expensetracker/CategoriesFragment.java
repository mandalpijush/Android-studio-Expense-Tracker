package yourstudio.com.expensetracker;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import yourstudio.com.expensetracker.DatabaseHelper;
import yourstudio.com.expensetracker.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {

    private ListView listView;
    private DatabaseHelper dbHelper;
    private GroupedCategoryAdapter adapter;
    private List<Category> displayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        listView = view.findViewById(R.id.listViewCategories);
        dbHelper = new DatabaseHelper(getContext());

        FloatingActionButton fab = view.findViewById(R.id.btnAddCategory);
        fab.setOnClickListener(v -> showAddCategoryDialog());


        displayList = new ArrayList<>();

        // Group Income
        displayList.add(new Category("🔵 Income Categories", true));
        List<Category> incomeCats = dbHelper.getCategoriesByTypeList("Income");
        displayList.addAll(incomeCats);

        // Group Expense
        displayList.add(new Category("🔴 Expense Categories", true));
        List<Category> expenseCats = dbHelper.getCategoriesByTypeList("Expense");
        displayList.addAll(expenseCats);

        adapter = new GroupedCategoryAdapter(getContext(), displayList, dbHelper);
        listView.setAdapter(adapter);

        return view;
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add New Category");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_category, null);
        EditText edtName = dialogView.findViewById(R.id.edtCategoryName);
        Spinner typeSpinner = dialogView.findViewById(R.id.spinnerCategoryType);

        // Setup spinner with Income/Expense
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new String[]{"Income", "Expense"});
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        builder.setView(dialogView);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = edtName.getText().toString().trim();
            String type = typeSpinner.getSelectedItem().toString();

            if (!name.isEmpty()) {
                dbHelper.insertCategory(name, type);

                // Reload categories
                reloadGroupedCategories();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void reloadGroupedCategories() {
        displayList.clear();

        displayList.add(new Category("🔵 Income Categories", true));
        displayList.addAll(dbHelper.getCategoriesByTypeList("Income"));

        displayList.add(new Category("🔴 Expense Categories", true));
        displayList.addAll(dbHelper.getCategoriesByTypeList("Expense"));

        adapter.notifyDataSetChanged();
    }




}
