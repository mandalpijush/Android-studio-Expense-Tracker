package yourstudio.com.expensetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.*;
import android.widget.*;

import java.util.List;

public class GroupedCategoryAdapter extends BaseAdapter {

    private Context context;
    private List<Category> items;
    private DatabaseHelper dbHelper;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CATEGORY = 1;

    public GroupedCategoryAdapter(Context context, List<Category> items, DatabaseHelper dbHelper) {
        this.context = context;
        this.items = items;
        this.dbHelper = dbHelper;
    }

    @Override
    public int getViewTypeCount() { return 2; }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).isHeader() ? TYPE_HEADER : TYPE_CATEGORY;
    }

    @Override
    public int getCount() { return items.size(); }

    @Override
    public Object getItem(int i) { return items.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        Category item = items.get(i);

        if (item.isHeader()) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_header, parent, false);
            TextView txt = view.findViewById(R.id.txtHeader);
            txt.setText(item.getName());
            return view;
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
            TextView txtName = view.findViewById(R.id.txtCategoryName);
            ImageButton btnEdit = view.findViewById(R.id.btnEdit);
            ImageButton btnDelete = view.findViewById(R.id.btnDelete);

            txtName.setText(item.getName());

            btnEdit.setOnClickListener(v -> showEditDialog(item, i));
            btnDelete.setOnClickListener(v -> {
                dbHelper.deleteCategory(item.getId());
                items.remove(i);
                notifyDataSetChanged();
            });

            return view;
        }
    }

    private void showEditDialog(Category category, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Category");

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(category.getName());
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty()) {
                dbHelper.updateCategory(category.getId(), newName);
                category.setName(newName);
                notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
