import javax.swing.*;

/**
   This program allows the user to view font effects.
*/
public class InventoryViewer
{  

   public static void main(String[] args)
   {  
      JFrame frame = new InventoryFrame();
      frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      frame.setTitle("Inventory");
      frame.setVisible(true);
   }

}