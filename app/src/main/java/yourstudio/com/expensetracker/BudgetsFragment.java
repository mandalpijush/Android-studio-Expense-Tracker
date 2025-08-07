package yourstudio.com.expensetracker;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.*;
import android.widget.*;
import yourstudio.com.expensetracker.DatabaseHelper;
import yourstudio.com.expensetracker.R;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.*;

public class BudgetsFragment extends Fragment {

    private TextView txtSelectedMonth;
    private Button btnPickMonth, btnSaveBudgets;
    private LinearLayout budgetListContainer;

    private DatabaseHelper dbHelper;
    private String selectedMonth;

    private Map<String, EditText> budgetInputs = new HashMap<>();
    private List<String> allCategories = new ArrayList<>();

    public BudgetsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budgets, container, false);

        txtSelectedMonth = view.findViewById(R.id.txtSelectedMonth);
        btnPickMonth = view.findViewById(R.id.btnPickMonth);
        btnSaveBudgets = view.findViewById(R.id.btnSaveBudgets);
        budgetListContainer = view.findViewById(R.id.budgetListContainer);

        dbHelper = new DatabaseHelper(getContext());
        selectedMonth = new SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(new Date());
        txtSelectedMonth.setText(selectedMonth);

        loadCategories();
        displayBudgetInputs();

        btnPickMonth.setOnClickListener(v -> showMonthPicker());
        btnSaveBudgets.setOnClickListener(v -> saveBudgetsToDB());

        return view;
    }

    private void loadCategories() {
        allCategories.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORIES, null, null, null, null, null, "type ASC, name ASC");
        while (cursor.moveToNext()) {
            allCategories.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        }
        cursor.close();
    }

    private void displayBudgetInputs() {
        budgetListContainer.removeAllViews();
        budgetInputs.clear();

        for (String category : allCategories) {
            LinearLayout row = new LinearLayout(getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(0, 10, 0, 10);

            TextView catName = new TextView(getContext());
            catName.setText(category);
            catName.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            EditText amountInput = new EditText(getContext());
            amountInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
            amountInput.setHint("Amount");
            amountInput.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            row.addView(catName);
            row.addView(amountInput);
            budgetListContainer.addView(row);

            budgetInputs.put(category, amountInput);
        }
    }

    private void showMonthPicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                (view, year, month, day) -> {
                    selectedMonth = String.format(Locale.getDefault(), "%02d-%d", month + 1, year);
                    txtSelectedMonth.setText(selectedMonth);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.setTitle("Select Month");
        dialog.show();
    }

    private void saveBudgetsToDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (String category : budgetInputs.keySet()) {
            EditText input = budgetInputs.get(category);
            String amountStr = input.getText().toString().trim();

            if (!amountStr.isEmpty()) {
                double amount = Double.parseDouble(amountStr);

                // Get category_id
                Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORIES, new String[]{"id"}, "name = ?", new String[]{category}, null, null, null);
                if (cursor.moveToFirst()) {
                    int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));

                    // Insert or Replace
                    ContentValues values = new ContentValues();
                    values.put("month", selectedMonth);
                    values.put("category_id", categoryId);
                    values.put("amount", amount);

                    db.insertWithOnConflict(DatabaseHelper.TABLE_BUDGETS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                }
                cursor.close();
            }
        }
        Toast.makeText(getContext(), "Budgets saved", Toast.LENGTH_SHORT).show();
    }
}
