package yourstudio.com.expensetracker;

import android.content.Context;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    private List<BudgetItem> items;
    private Context context;
    private String selectedMonth;
    private DatabaseHelper dbHelper;

    public BudgetAdapter(Context context, List<BudgetItem> items, String selectedMonth, DatabaseHelper dbHelper) {
        this.context = context;
        this.items = items;
        this.selectedMonth = selectedMonth;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.budget_item, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        BudgetItem item = items.get(position);
        holder.txtCategory.setText("📁 " + item.getCategoryName());
        holder.txtDetails.setText(String.format("Spent ₹%.2f | Remaining ₹%.2f", item.getSpent(), item.getRemaining()));
        holder.edtBudget.setText(String.valueOf(item.getBudgetSet()));

        holder.btnSave.setOnClickListener(v -> {
            String amtStr = holder.edtBudget.getText().toString().trim();
            if (!amtStr.isEmpty()) {
                double newBudget = Double.parseDouble(amtStr);
                item.setBudgetSet(newBudget);
                dbHelper.saveOrUpdateBudget(item.getCategoryId(), selectedMonth, newBudget);
                notifyItemChanged(position);
                Toast.makeText(context, "Updated budget for " + item.getCategoryName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory, txtDetails;
        EditText edtBudget;
        ImageButton btnSave;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategoryName);
            txtDetails = itemView.findViewById(R.id.txtBudgetDetails);
            edtBudget = itemView.findViewById(R.id.edtBudget);
            btnSave = itemView.findViewById(R.id.btnSaveBudget);
        }
    }
}
