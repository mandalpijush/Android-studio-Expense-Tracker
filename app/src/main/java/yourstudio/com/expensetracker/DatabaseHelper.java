package yourstudio.com.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "finance_manager.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_CATEGORY_ID = "id";
    public static final String COLUMN_CATEGORY_NAME = "name";
    public static final String COLUMN_CATEGORY_TYPE = "type";


    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String TABLE_BUDGETS = "budgets";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCategories = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY_NAME + " TEXT NOT NULL, " +
                COLUMN_CATEGORY_TYPE + " TEXT NOT NULL CHECK (type IN ('Income', 'Expense')));";

        String createTransactions = "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount REAL, " +
                "date TEXT, " +
                "notes TEXT, " +
                "account TEXT, " +
                "category_id INTEGER, " +
                "FOREIGN KEY(category_id) REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + "));";

        String createBudgets = "CREATE TABLE " + TABLE_BUDGETS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "month TEXT, " +
                "category_id INTEGER, " +
                "amount REAL, " +
                "FOREIGN KEY(category_id) REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + "));";

        db.execSQL(createCategories);
        db.execSQL(createTransactions);
        db.execSQL(createBudgets);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    public Cursor getCategoriesByType(String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + COLUMN_CATEGORY_TYPE + " = ?", new String[]{type});
    }

    public int updateCategoryName(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        return db.update(TABLE_CATEGORIES, values, COLUMN_CATEGORY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Consider handling associated transactions and budgets here if needed
        // For example, set category_id to null or delete them
        //db.delete(TABLE_CATEGORIES, COLUMN_CATEGORY_ID + " = ?", new String[]{String.valueOf(id)});
        db.delete("categories", "id = ?", new String[]{String.valueOf(id)});
    }

    public void updateCategory(int id, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);
        db.update("categories", values, "id = ?", new String[]{String.valueOf(id)});
    }




    public List<Category> getCategoriesByTypeList(String type) {
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM categories WHERE type = ? ORDER BY name", new String[]{type});
        while (cursor.moveToNext()) {
            list.add(new Category(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    type
            ));
        }
        cursor.close();
        return list;
    }



}
