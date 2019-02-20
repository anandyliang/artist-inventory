import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CashRegister {
    private Inventory inventory;  
    private ArrayList<Item> shoppingCart = new ArrayList<>();
    private ArrayList<Category> discounts = new ArrayList<>();
    private double grandtotal;	//grand total that will be recorded at the end
    private double total;   //double of the total cost of all the items in the grocery cart
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en","CA"));	//formats the currency correctly

    public CashRegister(Inventory inventory){
        this.inventory = inventory;
    }

    /**
     * Adds the inputted item to the cash register and adds its price to the total
     * @param item  the item to be added to the cash register
     */
    public void addItem (Item item){
        if (!shoppingCart.contains(item)) {
            shoppingCart.add(item); //adds item to the shopping cart if it's not already there
        }
        Category itemCategory = findCategory(item);
        itemCategory.addCategoryQuantity();
        if (itemCategory.discountApplicable()) {
            applyDiscount(itemCategory);
        }
        total += itemCategory.getCategory_Price();   //adds the price to the total
        total = (Math.round(total * 20.0)) / 20.0;  //rounding to the nearest 5
    }

    public void minusItem (Item item){
        Category itemCategory = findCategory(item);
        itemCategory.minusCategoryQuantity();
        if (itemCategory.getCategory_Quantity() < 0)
            revertDiscount(itemCategory);
        total -= itemCategory.getCategory_Price();   //subtracts the price from the total
        total = (Math.round(total * 20.0)) / 20.0;
        if (shoppingCart.get(shoppingCart.indexOf(item)).getCashRegisterQuantity() <= 0){   //removes the  item from the shopping cart if its 0 quantity
            shoppingCart.remove(item);
        }
    }
    /**
     * Displays all cash register contents in the inputted JTextArea
     * @param area JTextArea that will display the contents
     */
    public void display(JTextArea area){
        area.setText("");   //resets text area
        for (Item item: shoppingCart) {  //for each item in the shoppingCart
            area.append(String.format("%-25s%17s%n",item.getName() + " " + currencyFormatter.format(findCategory(item).getCategory_Price()) + " x"  + item.getCashRegisterQuantity(), currencyFormatter.format(findCategory(item).getCategory_Price() * item.getCashRegisterQuantity()))); //add the label and price to the text area
        }
        area.append("\n");
        for (Category category: discounts) {
            area.append(String.format("%-25s%14s%n",category.getCategory_Name() + " (" + category.getBundle_quantity() + "/" + currencyFormatter.format(category.getBundle_price()) + ")", currencyFormatter.format(category.getPrice_difference()))); //add the label and price to the text area
        }
        for (int i = 0; i < 42; i++) {
             area.append("-");

        }
        area.append(String.format("%n%-25s%17s%n", "Total:", currencyFormatter.format(total))); //add the total at the bottom
//        area.append(Calendar.getInstance().getTime().toString());  //add the date at the very bottom
    }

    /**
     * Restarts the cash register
     * @param area  the JTextArea that is displaying the contents
     */
    public void clear(JTextArea area){
        shoppingCart.clear();   //clears the arraylists
        discounts.clear();
        inventory.reset();
        grandtotal+=total;
        total = 0;  //resets the total
        display(area);   //clears the textarea
    }

    public void removeAll(JTextArea area){
        shoppingCart.clear();   //clears the arraylists
        discounts.clear();
        inventory.clearAll();
        grandtotal+=total;
        total = 0;  //resets the total
        display(area);   //clears the textarea
    }
    public double getTotal(){
        return total;
    }

    private Category findCategory(Item item){
        ArrayList<Category> categories = inventory.getCategories();
        for (Category category:
            categories ) {
            if (category.getItems().contains(item))
                return category;
        }
        throw new IllegalArgumentException("cmon bruh");
    }

    private void applyDiscount(Category category){
        total += category.getPrice_difference();
        discounts.add(category);
    }

    private void revertDiscount(Category category){
        total -= category.getPrice_difference();
        category.revertDiscount();
        discounts.remove(category);
    }

    public void writeFiles(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("files/Grand total - " + simpleDateFormat.format(new Date()) + ".txt",true));
            bufferedWriter.write("Grand Total: " + grandtotal);
            bufferedWriter.close();
        }
        catch (IOException e){
            System.out.println("I can't write my files for some reason (Cash Register)");
        }
    }
}
