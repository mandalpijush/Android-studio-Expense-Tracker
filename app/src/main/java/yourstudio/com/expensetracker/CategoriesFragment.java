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
}
