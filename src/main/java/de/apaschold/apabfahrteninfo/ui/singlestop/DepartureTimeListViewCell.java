package de.apaschold.apabfahrteninfo.ui.singlestop;

import de.apaschold.apabfahrteninfo.model.SingleStop;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/**
 * <h2>DepartureTimeListViewCell class</h2>
 * <li>Custom {@link ListCell} for displaying the arrival time of a {@link SingleStop} in a {@link ListView}.</li>
 * <li>Calls the departureAsString method from the {@link SingleStop}</li>
 */
public class DepartureTimeListViewCell extends ListCell<SingleStop> {
    @Override
    protected void updateItem(SingleStop stopTime, boolean empty){
        super.updateItem(stopTime,empty);

        if(empty|| stopTime == null){
            setText(null);
        } else {
            setText(stopTime.departureAsString());
        }
    }
}
