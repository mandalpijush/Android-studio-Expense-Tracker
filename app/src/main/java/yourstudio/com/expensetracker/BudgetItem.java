package yourstudio.com.expensetracker;

public class BudgetItem {
    private int categoryId;
    private String categoryName;
    private double budgetSet;
    private double spent;
    private double remaining;

    public BudgetItem(int categoryId, String categoryName, double budgetSet, double spent) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.budgetSet = budgetSet;
        this.spent = spent;
        this.remaining = budgetSet - spent;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getBudgetSet() {
        return budgetSet;
    }

    public void setBudgetSet(double budgetSet) {
        this.budgetSet = budgetSet;
        this.remaining = budgetSet - this.spent;
    }

    public double getSpent() {
        return spent;
    }

    public double getRemaining() {
        return remaining;
    }
}
