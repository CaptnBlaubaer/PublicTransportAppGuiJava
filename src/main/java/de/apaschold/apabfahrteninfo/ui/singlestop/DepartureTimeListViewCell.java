package de.apaschold.apabfahrteninfo.ui.singlestop;

import de.apaschold.apabfahrteninfo.model.StopTime;
import javafx.scene.control.ListCell;

public class DepartureTimeListViewCell extends ListCell<StopTime> {
    @Override
    protected void updateItem(StopTime stopTime, boolean empty){
        super.updateItem(stopTime,empty);

        if(empty|| stopTime == null){
            setText(null);
        } else {
            setText(stopTime.departureAsString());
        }
    }
}
