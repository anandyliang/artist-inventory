import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Inventory {
    private ArrayList<Category> categories = new ArrayList<>();
    public Inventory() {
        addCategories();
        addItems();
    }

    private void addCategories(){
        try {
            Scanner scanner = new Scanner(new File("files/Categories.txt"));
//            scanner.useDelimiter("\\S");
            while (scanner.hasNext()){
                categories.add(new Category(scanner.nextLine(), Double.parseDouble(scanner.nextLine()), Integer.parseInt(scanner.nextLine()), Double.parseDouble(scanner.nextLine())));
            }
        }
        catch(FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"\"Categories.txt\" was not found in files directory, this program will now quit.");
            System.exit(404);
        }
        Collections.sort(categories);
    }

    private void addItems(){
        try {
//            Scanner scanner = new Scanner(new File("files/InventoryTest.txt"));
            Scanner scanner = new Scanner(new File("files/Inventory.txt"));
            while (scanner.hasNext()){
                String categoryName = scanner.nextLine().trim();
                while (scanner.hasNextInt()) {      //While the next character is not a tab, this will add items to the categories
                    int quantity = scanner.nextInt();
                    String name = scanner.nextLine();
                    Item item = new Item(quantity, name);
                    assignCategory(categoryName).addItem(item);
                }
            }
        }
        catch(FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"\"Inventory.txt\" was not found in working directory, this program will now quit.");
            System.exit(404);
        }
        for (Category category:categories) {
            Collections.sort(category.getItems());
        }
    }
	
    public ArrayList<Category> getCategories() {
        return categories;
    }

    private Category assignCategory(String categoryName){
        for (Category category: categories){
            if (categoryName.equals(category.getCategory_Name()))
                return category;
        }
        throw new IllegalArgumentException("Category name in Inventory doesn't line up with Category name in Categories");
    }

    public void reset(){
        for (Category category :
                categories) {
            category.reset();
        }
    }

    public void clearAll(){
        for (Category category :
                categories) {
            category.clearAll();
        }

    }

    //TODO Add Log of all transactions made
    public void writeFiles(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy - hh-mm-ss");
        try {
            File old_file = new File("files/inventory.txt");
            new File("files/old inventories").mkdirs();
            File new_file = new File("files/old inventories/inventory - " + simpleDateFormat.format(new Date()) + ".txt");
            old_file.renameTo(new_file);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("files/inventory.txt"));
            for (Category category :
                    categories) {
                bufferedWriter.write("\n\t" + category.getCategory_Name()+ "\n\n");
                for (Item item :
                        category.getItems()) {
                    bufferedWriter.write(item.getQuantity() + "\t" + item.getName() + " " + "\n");
                }
            }
            bufferedWriter.close();
            simpleDateFormat = new SimpleDateFormat("MM-dd");
            bufferedWriter = new BufferedWriter(new FileWriter("files/Grand total - " + simpleDateFormat.format(new Date()) + ".txt"));
            for (Category category :
                    categories) {
                bufferedWriter.write(category.getCategory_Name() + ": " + category.getCategory_total()+ "\n");
            }
            bufferedWriter.close();
        }
        catch (IOException e){
            System.out.println("I can't write my files for some reason");
        }
    }
}
