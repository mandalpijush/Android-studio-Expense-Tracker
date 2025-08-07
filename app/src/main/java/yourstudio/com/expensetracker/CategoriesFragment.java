package yourstudio.com.expensetracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import yourstudio.com.expensetracker.DatabaseHelper;
import yourstudio.com.expensetracker.R;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {

    private ListView listView;
    private ArrayList<String> categoryDisplayList;
    private ArrayAdapter<String> adapter;
    private DatabaseHelper dbHelper;

    public CategoriesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        listView = view.findViewById(R.id.listViewCategories);
        dbHelper = new DatabaseHelper(getContext());
        categoryDisplayList = new ArrayList<>();

        loadGroupedCategories();

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categoryDisplayList);
        listView.setAdapter(adapter);

        return view;
    }

    private void loadGroupedCategories() {
        categoryDisplayList.clear();

        // Load Income categories
        categoryDisplayList.add("🔵 Income Categories:");
        Cursor incomeCursor = dbHelper.getCategoriesByType("Income");
        if (incomeCursor != null && incomeCursor.moveToFirst()) {
            do {
                String name = incomeCursor.getString(incomeCursor.getColumnIndex("name"));
                categoryDisplayList.add("   • " + name);
            } while (incomeCursor.moveToNext());
            incomeCursor.close();
        }

        // Load Expense categories
        categoryDisplayList.add("🔴 Expense Categories:");
        Cursor expenseCursor = dbHelper.getCategoriesByType("Expense");
        if (expenseCursor != null && expenseCursor.moveToFirst()) {
            do {
                String name = expenseCursor.getString(expenseCursor.getColumnIndex("name"));
                categoryDisplayList.add("   • " + name);
            } while (expenseCursor.moveToNext());
            expenseCursor.close();
        }
    }
}
