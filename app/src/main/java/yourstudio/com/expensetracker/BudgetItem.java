package yourstudio.com.expensetracker;

public class BudgetItem {
    private int categoryId;
    private String categoryName;
    private double budgetSet;
    private double spent;
    private double remaining;
    private String type; // "Income" or "Expense"
    private boolean isHeader;

    public BudgetItem(String headerTitle, boolean isHeader) {
        this.categoryName = headerTitle;
        this.isHeader = isHeader;
    }

    public BudgetItem(int categoryId, String categoryName, double budgetSet, double spent, String type) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.budgetSet = budgetSet;
        this.spent = spent;
        this.remaining = budgetSet - spent;
        this.type = type;
        this.isHeader = false;
    }

    public int getCategoryId() { return categoryId; }

    public String getCategoryName() { return categoryName; }

    public double getBudgetSet() { return budgetSet; }

    public void setBudgetSet(double budgetSet) {
        this.budgetSet = budgetSet;
        this.remaining = budgetSet - this.spent;
    }

    public double getSpent() { return spent; }

    public double getRemaining() { return remaining; }

    public String getType() { return type; }

    public boolean isHeader() { return isHeader; }
}
