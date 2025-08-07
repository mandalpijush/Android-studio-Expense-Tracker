package yourstudio.com.expensetracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "finance_manager.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String TABLE_BUDGETS = "budgets";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCategories = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "type TEXT NOT NULL CHECK (type IN ('Income', 'Expense')));";

        String createTransactions = "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount REAL, " +
                "date TEXT, " +
                "notes TEXT, " +
                "account TEXT, " +
                "category_id INTEGER, " +
                "FOREIGN KEY(category_id) REFERENCES " + TABLE_CATEGORIES + "(id));";

        String createBudgets = "CREATE TABLE " + TABLE_BUDGETS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "month TEXT, " +
                "category_id INTEGER, " +
                "amount REAL, " +
                "FOREIGN KEY(category_id) REFERENCES " + TABLE_CATEGORIES + "(id));";

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
        //return db.rawQuery("SELECT * FROM categories WHERE parent_type = ?", new String[]{parentType});
        //return db.rawQuery("SELECT * FROM categories WHERE parent_type = ?", new String[]{parentType});
        return db.rawQuery("SELECT * FROM categories WHERE type = ?", new String[]{type});
    }
}


