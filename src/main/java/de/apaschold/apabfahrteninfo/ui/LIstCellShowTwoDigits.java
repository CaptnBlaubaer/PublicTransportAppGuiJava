package de.apaschold.apabfahrteninfo.ui;

import javafx.scene.control.ListCell;

//is used in DirectRouteSearchController and SingleStopController
public class LIstCellShowTwoDigits extends ListCell<Integer> {
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
