package yourstudio.com.expensetracker;

public class Category {
    private int id;
    private String name;
    private String type;
    private boolean isHeader;

    public Category(String name, boolean isHeader) {
        this.name = name;
        this.isHeader = isHeader;
    }

    public Category(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isHeader = false;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public boolean isHeader() { return isHeader; }

    public void setName(String name) { this.name = name; }
}
