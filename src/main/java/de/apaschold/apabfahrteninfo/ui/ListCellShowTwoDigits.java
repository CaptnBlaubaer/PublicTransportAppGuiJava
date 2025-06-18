package de.apaschold.apabfahrteninfo.ui;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

/**
 * <h2>ListCellShowTwoDigits class</h2>
 * <li>Custom ListCell that formats integers to always show two digits.</li>
 * <li>Used for {@link ComboBox} to display time units</li>
 */
public class ListCellShowTwoDigits extends ListCell<Integer> {
    @Override
    protected void updateItem(Integer integer, boolean empty){
        super.updateItem(integer, empty);

        if (integer == null || empty){
            setText(null);
        } else {
            // Format the integer to always show two digits
            setText(String.format("%02d", integer));
        }
    }
}
