import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class InventoryFrame extends JFrame {
    private Inventory inventory;
    private CashRegister cashRegister;
    private JTextArea cashRegisterTextArea;

    public InventoryFrame(){
        inventory = new Inventory();
        cashRegister = new CashRegister(inventory);
        setSize(1000,800);
        setLayout(new BorderLayout());
        setExtendedState(MAXIMIZED_BOTH);

        add(new JScrollPane(createItemsPanel()),BorderLayout.CENTER);
        add(createCashPanel(),BorderLayout.EAST);
        createProgramExitConfirmation();
    }

    private JPanel createItemsPanel(){
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new GridLayout(1,0));
        ArrayList<Category> categories = inventory.getCategories();

        for (Category category: categories){
            CategoryPanel categoryPanel = new CategoryPanel();
            CategoryLabel categoryLabel = new CategoryLabel(category.getCategory_Name());
            categoryLabel.setAlignmentX(itemPanel.getAlignmentX());
            categoryPanel.add(categoryLabel);

            ArrayList<Item> items = category.getItems();
            for (Item item: items){
                categoryPanel.add(createItem(item));
            }

            itemPanel.add(new JScrollPane(categoryPanel));
        }
        itemPanel.setPreferredSize(new Dimension(itemPanel.getPreferredSize().width + 150, super.getHeight())); //Make the panel super wide so I can scroll horizontally
        return itemPanel;
    }

    private JPanel createItem(Item item){
        JLabel label = new JLabel(item.getName() + ": " + item.getQuantity());
        JPanel jPanel= new JPanel();
        jPanel.setMaximumSize(new Dimension(300,40));
        jPanel.add(buttonMakerMinus(label,item));
        jPanel.add(label);
        jPanel.add(buttonMakerPlus(label,item));
        return jPanel;
    }

    private JButton buttonMakerPlus(JLabel label, Item item){
        JButton jButton = new JButton("+");
        class AddListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                if (item.getQuantity() > 0) {
                    item.minusQuantity();
                    label.setText(item.getName() + ": " + item.getQuantity());
                    cashRegister.addItem(item);
                    cashRegister.display(cashRegisterTextArea);
                }
            }
        }
        ActionListener addListener = new AddListener();
        jButton.addActionListener(addListener);
        return jButton;
    }

    private JButton buttonMakerMinus(JLabel label,Item item){
        JButton jButton = new JButton("-");
        class MinusListener implements ActionListener{
            public void actionPerformed(ActionEvent e) {
                if (item.getQuantity() < item.getMaxQuantity()) {
                    item.addQuantity();
                    label.setText(item.getName() + ": " + item.getQuantity());
                    cashRegister.minusItem(item);
                    cashRegister.display(cashRegisterTextArea);
                }
            }
        }
        ActionListener minusListener = new MinusListener();
        jButton.addActionListener(minusListener);
        return jButton;
    }

    private JPanel createCashPanel(){
        JPanel cashPanel = new JPanel();
        cashPanel.setLayout(new BorderLayout());

        cashRegisterTextArea  = new JTextArea();
        cashRegisterTextArea.setFont(new Font("Monospaced",Font.PLAIN,12));     //sets the font for the above created text area
        cashRegisterTextArea.setEditable(false);
        cashRegister.display(cashRegisterTextArea);

        JScrollPane jScrollPane = new JScrollPane(cashRegisterTextArea);
        cashPanel.add(jScrollPane,BorderLayout.CENTER);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cashRegister.getTotal()>0)
                    createCheckoutMessage();
            }
        });

        JButton giftButton = new JButton("Gift");
        giftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure you would like to checkout this item as a gift?", "Gift Confirmation", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    cashRegister.clear(cashRegisterTextArea);   //clears the cash register, as well as the cash register text area
                }
            }
        });

        JButton clearAll_button = new JButton("Clear All");
        clearAll_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAll();
            }
        });

        JPanel checkoutbuttons =  new JPanel();
        checkoutbuttons.add(checkoutButton);
        checkoutbuttons.add(giftButton);
        checkoutbuttons.add(clearAll_button);
        cashPanel.add(checkoutbuttons,BorderLayout.SOUTH);
        cashPanel.setPreferredSize(new Dimension(300,500));

        return cashPanel;
    }

    private void createCheckoutMessage(){
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en","CA"));
        double TakeCash;
        boolean CheckingOut = true;
        while (true) {
            try {
                TakeCash = Double.parseDouble(JOptionPane.showInputDialog("Total Amount Due: " + currencyFormatter.format(cashRegister.getTotal()) + "\nInput Amount Given:"));
                if (TakeCash < cashRegister.getTotal()){
                    JOptionPane.showMessageDialog(this,"The number you inputted is less than the total required, try again");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "You didn't input a valid number, try again.");
            } catch (NullPointerException e1) {
                CheckingOut = false;
                TakeCash = 0;
                break;
            }
        }

        if (CheckingOut) {
            JOptionPane.showMessageDialog(this, "Total Amount Due: " + currencyFormatter.format(cashRegister.getTotal())
                    + "\nInput Amount Given: " + currencyFormatter.format(TakeCash)
                    + "\nGive Back: " + (currencyFormatter.format(TakeCash - cashRegister.getTotal())));
            cashRegister.clear(cashRegisterTextArea);   //clears the cash register, as well as the cash register text area
        }

    }

    private void createProgramExitConfirmation(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(null,"Would you like to save before you quit?","Save Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
                if (result == JOptionPane.YES_OPTION){
                    inventory.writeFiles();
                    cashRegister.writeFiles();
                    System.exit(0);
                }
                else if (result == JOptionPane.NO_OPTION){
                    System.exit(0);
                }
            }
        });
    }

    public void removeAll(){
        int result = JOptionPane.showConfirmDialog(null, "Are you sure you would like to return all the items in cart?", "Clear All Confirmation", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            cashRegister.removeAll(cashRegisterTextArea);
            this.repaint();
        }
    }
}