package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.itinerary.Itinerary;

/**
 * An UI component that displays information of a {@code Itinerary}.
 */
public class ItineraryCard extends UiPart<Region> {

    private static final String FXML = "ItineraryListCard.fxml";
    private static final double PADDING = 30;

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Itinerary itinerary;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label destination;
    @FXML
    private Label dateRange;

    /**
     * Creates a {@code ItineraryCard} with the given {@code Itinerary} and index to display.
     */
    public ItineraryCard(Itinerary itinerary, int displayedIndex) {
        super(FXML);
        this.itinerary = itinerary;
        id.setText(displayedIndex + ". ");
        name.setText(itinerary.getName().fullName);
        destination.setText(itinerary.getDestination().toString());
        dateRange.setText(itinerary.getDateRange().toString());

        name.maxWidthProperty().bind(cardPane.widthProperty().subtract(PADDING));
        destination.maxWidthProperty().bind(cardPane.widthProperty().subtract(PADDING));
    }
}
