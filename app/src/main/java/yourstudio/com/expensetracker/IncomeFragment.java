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

public class IncomeFragment extends Fragment {

    private Spinner spinnerCategory;
    private EditText editAmount, editDate, editNote, editAccount;
    private Button btnAddIncome;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> categoryAdapter;
    private List<String> incomeCategories;

    public IncomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);

        spinnerCategory = view.findViewById(R.id.spinnerIncomeCategory);
        editAmount = view.findViewById(R.id.editIncomeAmount);
        editDate = view.findViewById(R.id.editIncomeDate);
        editNote = view.findViewById(R.id.editIncomeNote);
        editAccount = view.findViewById(R.id.editIncomeAccount);
        btnAddIncome = view.findViewById(R.id.btnAddIncome);

        dbHelper = new DatabaseHelper(getContext());
        loadIncomeCategories();

        editDate.setOnClickListener(v -> showDatePicker());

        btnAddIncome.setOnClickListener(v -> addIncome());

        return view;
    }

    private void loadIncomeCategories() {
        incomeCategories = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORIES, null, "type = ?", new String[]{"Income"}, null, null, null);
        while (cursor.moveToNext()) {
            incomeCategories.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        }
        cursor.close();

        categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, incomeCategories);
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

    private void addIncome() {
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
        values.put("type", "Income");
        values.put("category", category);
        values.put("amount", amount);
        values.put("date", date);
        values.put("note", note);
        values.put("account", account);
        db.insert(DatabaseHelper.TABLE_TRANSACTIONS, null, values);

        Toast.makeText(getContext(), "Income added", Toast.LENGTH_SHORT).show();
        editAmount.setText("");
        editDate.setText("");
        editNote.setText("");
        editAccount.setText("");
    }
}
