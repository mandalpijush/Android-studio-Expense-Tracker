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

public class ExpenseFragment extends Fragment {

    private Spinner spinnerCategory;
    private EditText editAmount, editDate, editNote, editAccount;
    private Button btnAddExpense;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> categoryAdapter;
    private List<String> expenseCategories;

    public ExpenseFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        spinnerCategory = view.findViewById(R.id.spinnerExpenseCategory);
        editAmount = view.findViewById(R.id.editExpenseAmount);
        editDate = view.findViewById(R.id.editExpenseDate);
        editNote = view.findViewById(R.id.editExpenseNote);
        editAccount = view.findViewById(R.id.editExpenseAccount);
        btnAddExpense = view.findViewById(R.id.btnAddExpense);

        dbHelper = new DatabaseHelper(getContext());
        loadExpenseCategories();

        editDate.setOnClickListener(v -> showDatePicker());

        btnAddExpense.setOnClickListener(v -> addExpense());

        return view;
    }

    private void loadExpenseCategories() {
        expenseCategories = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORIES, null, "type = ?", new String[]{"Expense"}, null, null, null);
        while (cursor.moveToNext()) {
            expenseCategories.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        }
        cursor.close();

        categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, expenseCategories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    editDate.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }

    private void addExpense() {
        String category = spinnerCategory.getSelectedItem().toString();
        String amountStr = editAmount.getText().toString();
        String date = editDate.getText().toString();
        String note = editNote.getText().toString();
        String account = editAccount.getText().toString();

        if (amountStr.isEmpty() || date.isEmpty() || account.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", "Expense");
        values.put("category", category);
        values.put("amount", amount);
        values.put("date", date);
        values.put("note", note);
        values.put("account", account);
        db.insert(DatabaseHelper.TABLE_TRANSACTIONS, null, values);

        Toast.makeText(getContext(), "Expense added", Toast.LENGTH_SHORT).show();
        editAmount.setText("");
        editDate.setText("");
        editNote.setText("");
        editAccount.setText("");
    }
}
