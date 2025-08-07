package yourstudio.com.expensetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

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

    @Override
    public int getItemViewType(int position) {
        return items.get(position).isHeader() ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.grouped_budget_item, parent, false);
            return new BudgetViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BudgetItem item = items.get(position);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).txtHeader.setText(item.getCategoryName());
        } else {
            BudgetViewHolder h = (BudgetViewHolder) holder;
            h.txtCategory.setText("📁 " + item.getCategoryName());
            h.txtBudgetAmount.setText("₹" + item.getBudgetSet());

            h.txtSpentRemaining.setText(
                    String.format("Spent: ₹%.2f   Remaining: ₹%.2f", item.getSpent(), item.getRemaining())
            );

            double percent = item.getBudgetSet() > 0 ? (item.getSpent() / item.getBudgetSet()) * 100 : 0;
            h.progressBar.setProgress((int) percent);
            h.txtPercentage.setText(String.format("%.0f%%", percent));

            h.btnEdit.setOnClickListener(v -> showEditDialog(item, position));
        }
    }

    private void showEditDialog(BudgetItem item, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Budget for " + item.getCategoryName());

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setText(String.valueOf(item.getBudgetSet()));
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newBudgetStr = input.getText().toString().trim();
            if (!newBudgetStr.isEmpty()) {
                double newBudget = Double.parseDouble(newBudgetStr);
                item.setBudgetSet(newBudget);
                dbHelper.saveOrUpdateBudget(item.getCategoryId(), selectedMonth, newBudget);
                notifyItemChanged(position);
                Toast.makeText(context, "Budget updated", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView txtHeader;
        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHeader = itemView.findViewById(R.id.txtHeader);
        }
    }

    static class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory, txtBudgetAmount, txtSpentRemaining, txtPercentage;
        ProgressBar progressBar;
        ImageButton btnEdit;

        BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategoryName);
            txtBudgetAmount = itemView.findViewById(R.id.txtBudgetAmount);
            txtSpentRemaining = itemView.findViewById(R.id.txtSpentRemaining);
            txtPercentage = itemView.findViewById(R.id.txtPercentage);
            progressBar = itemView.findViewById(R.id.progressBar);
            btnEdit = itemView.findViewById(R.id.btnEditBudget);
        }
    }
}
