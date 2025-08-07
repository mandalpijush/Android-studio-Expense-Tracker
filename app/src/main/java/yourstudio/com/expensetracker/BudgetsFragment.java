package yourstudio.com.expensetracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class BudgetsFragment extends Fragment {

    private TextView txtSelectedMonth;
    private Button btnPickMonth;
    private RecyclerView recyclerBudgets;

    private DatabaseHelper dbHelper;
    private String selectedMonth;
    private List<BudgetItem> budgetItems;
    private BudgetAdapter adapter;

    public BudgetsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budgets, container, false);

        txtSelectedMonth = view.findViewById(R.id.txtSelectedMonth);
        btnPickMonth = view.findViewById(R.id.btnPickMonth);
        recyclerBudgets = view.findViewById(R.id.recyclerBudgets);

        dbHelper = new DatabaseHelper(getContext());
        selectedMonth = new SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(new Date());
        txtSelectedMonth.setText(selectedMonth);

        recyclerBudgets.setLayoutManager(new LinearLayoutManager(getContext()));

        loadBudgetItems();

        btnPickMonth.setOnClickListener(v -> showMonthPicker());

        return view;
    }

    private void showMonthPicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                (view, year, month, day) -> {
                    selectedMonth = String.format(Locale.getDefault(), "%02d-%d", month + 1, year);
                    txtSelectedMonth.setText(selectedMonth);
                    loadBudgetItems();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setTitle("Select Month");
        dialog.show();
    }

    private void loadBudgetItems() {
        budgetItems = new ArrayList<>();
        List<Category> allCategories = dbHelper.getAllCategories();

        // Group headers
        budgetItems.add(new BudgetItem("🔵 Income Budgets", true));
        for (Category cat : allCategories) {
            if ("Income".equalsIgnoreCase(cat.getType())) {
                addBudgetItemFromCategory(cat);
            }
        }

        budgetItems.add(new BudgetItem("🔴 Expense Budgets", true));
        for (Category cat : allCategories) {
            if ("Expense".equalsIgnoreCase(cat.getType())) {
                addBudgetItemFromCategory(cat);
            }
        }

        adapter = new BudgetAdapter(getContext(), budgetItems, selectedMonth, dbHelper);
        recyclerBudgets.setAdapter(adapter);
    }

    private void addBudgetItemFromCategory(Category cat) {
        int categoryId = cat.getId();
        String categoryName = cat.getName();
        String type = cat.getType();
        double spent = dbHelper.getSpentForCategory(categoryId, selectedMonth);
        double budgetSet = dbHelper.getBudgetForCategory(categoryId, selectedMonth);

        BudgetItem item = new BudgetItem(categoryId, categoryName, budgetSet, spent, type);
        budgetItems.add(item);
    }
}
