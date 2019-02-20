import java.util.ArrayList;

public class Category implements Comparable<Category>{
    private String category_name;	//name of the category
    private int category_total;	//the total number of items sold in this category
    private int category_quantity; //used mainly to keep track of items eligable for discount
    private double category_price;	//normal price per item
    private int bundle_quantity;	//the number of items you have to buy to qualify for a bundle
    private double bundle_price;		//The entire price of the bundle
    private ArrayList<Item> items;	//Keeps track of all the items in the category

    public Category(String category_name, double category_price, int bundle_quantity, double bundle_price) {
        this.category_name = category_name;
        this.category_price = category_price;
        this.bundle_quantity = bundle_quantity;
        this.bundle_price = bundle_price;
        category_quantity = 0;
		category_total = 0;
        items = new ArrayList<>();
    }

    public int getBundle_quantity() {
        return bundle_quantity;
    }

    public int getCategory_total(){
        return category_total;
    }

    public double getBundle_price() {
        return bundle_price;
    }

    public double getPrice_difference() {
        return bundle_price - (category_price * bundle_quantity);
    }

    public String getCategory_Name() {
        return category_name;
    }

    public int getCategory_Quantity() {
        return category_quantity;
    }

    public double getCategory_Price() {
        return category_price;
    }

    public void addCategoryQuantity() {
        category_quantity++;
        category_total++;
    }

    public void minusCategoryQuantity() {
        category_quantity--;
        category_total--;
    }

    /**
     * Adds the selected item to the category
     * @param item  The item to be added to the category
     */
    public void addItem(Item item){
        items.add(item);
    }

    /**
     * Gives you the arraylist of items in this category
     * @return
     */
    public ArrayList<Item> getItems(){
        return items;
    }

    /**
     * checks if this category is eligible for a discount
     * @return
     */
    public boolean discountApplicable(){
        if (category_quantity == bundle_quantity) {
            category_quantity = 0;
            return true;
        }
        return false;
    }

    public void revertDiscount(){
        category_quantity = (bundle_quantity - 1);
    }

    public void reset(){
        category_quantity = 0;
        for (Item item :
                items) {
            item.reset();
        }
    }

    public void clearAll(){
        category_quantity = 0;
        for (Item item:
             items) {
            item.undo();
        }
    }

    @Override
    public int compareTo(Category o) {
        return getCategory_Name().compareTo(o.getCategory_Name());
    }
}
