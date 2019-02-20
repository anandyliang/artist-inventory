public class Item implements Comparable<Item> {
    private int quantity;		//keeps track of the current quantity of the item
	private int maxQuantity;	//keeps track of the maximum quantity allowed to be kept of the item (use this for the clear all button)
    private String name;	//name of the item

    public int getCashRegisterQuantity() {
        return maxQuantity - quantity;
    }

    public Item(int quantity, String name) {
        this.quantity = quantity;
        this.maxQuantity = quantity;
        this.name = name.trim();
    }

    public void addQuantity() {
        quantity++;
    }

    public void minusQuantity() {
        quantity--;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void reset(){
        maxQuantity = quantity;
    }

    public void undo(){
        quantity = maxQuantity;
    }

    @Override
    public int compareTo(Item o) {
        return getName().compareTo(o.getName());
    }
}