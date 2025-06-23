package de.apaschold.apabfahrteninfo.ui.singlestop;

import de.apaschold.apabfahrteninfo.model.SingleStop;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/**
 * <h2>ArrivalTimeListViewCell class</h2>
 * <li>Custom {@link ListCell} for displaying the arrival time of a {@link SingleStop} in a {@link ListView}.</li>
 * <li>Calls the arrivalAsString method from the {@link SingleStop}</li>
 */
public class ArrivalTimeListViewCell extends ListCell<SingleStop> {
    @Override
    protected void updateItem(SingleStop singleStop, boolean empty){
        super.updateItem(singleStop,empty);

        if(empty|| singleStop == null){
            setText(null);
        } else {
            setText(singleStop.arrivalAsString());
        }
    }
}
