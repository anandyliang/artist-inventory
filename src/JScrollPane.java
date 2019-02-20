import java.awt.*;

public class JScrollPane extends javax.swing.JScrollPane {
    public JScrollPane(Component component){
        super(component);
        getVerticalScrollBar().setUnitIncrement(16);
    }
}
