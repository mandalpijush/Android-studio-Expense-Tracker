package yourstudio.com.expensetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.*;
import android.widget.*;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<Category> categoryList;
    private DatabaseHelper dbHelper;

    public CategoryAdapter(Context context, List<Category> categories, DatabaseHelper dbHelper) {
        this.context = context;
        this.categoryList = categories;
        this.dbHelper = dbHelper;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return categoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return categoryList.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);

        TextView name = view.findViewById(R.id.txtCategoryName);
        ImageButton edit = view.findViewById(R.id.btnEdit);
        ImageButton delete = view.findViewById(R.id.btnDelete);

        Category category = categoryList.get(i);
        name.setText(category.getType() + ": " + category.getName());

        edit.setOnClickListener(v -> showEditDialog(category, i));
        delete.setOnClickListener(v -> {
            dbHelper.deleteCategory(category.getId());
            categoryList.remove(i);
            notifyDataSetChanged();
        });

        return view;
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
