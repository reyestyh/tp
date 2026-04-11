package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page.
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s2-cs2103t-f12-1.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "addc - Add a contact\n"
                    + "  Format : addc r/ROLE n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]...\n"
                    + "  Example: addc r/vendor n/John Doe p/(+65) 98765432 e/john@email.com a/123 Street t/Client\n\n"
                    + "addi - Add an itinerary\n"
                    + "  Format : addi n/ITINERARY_NAME dest/DESTINATION from/START_DATE to/END_DATE\n"
                    + "                  [c/CLIENT_INDEX] [v/VENDOR_INDEX]\n"
                    + "  Example: addi n/5D4N France Getaway dest/France from/2026-10-12 to/2026-10-17 c/2 v/4\n\n"
                    + "list - List entries by category\n"
                    + "  Format : list /FLAG\n"
                    + "  FLAG   : contact, itinerary, client, vendor, all\n"
                    + "  Example: list /contact\n\n"
                    + "edit - Edit a contact or itinerary\n"
                    + "  Contact format   : edit /contact INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]...\n"
                    + "  Itinerary format : edit /itinerary INDEX [n/NAME] [dest/DESTINATION]\n"
                    + "                                  [from/START_DATE] [to/END_DATE]\n"
                    + "  Example: edit /contact 1 p/(+65) 91234567 e/new@email.com\n"
                    + "                    edit /itinerary 2 dest/Japan from/2026-11-01 to/2026-11-07\n\n"
                    + "find - Perform a general or multi-field search for contacts\n"
                    + "  General format    : find KEYWORD [MORE_KEYWORDS]...\n"
                    + "  Multi-field format: find [n/NAME_KEYWORDS] [p/PHONE_KEYWORDS] [e/EMAIL_KEYWORDS]\n"
                    + "                                    [a/ADDRESS_KEYWORDS] [t/TAG_KEYWORDS]\n"
                    + "  Example: find Alex David\n"
                    + "                   find n/Alex Yu p/996\n\n"
                    + "show - Show the details and associated contacts of a specific itinerary.\n"
                    + "  Format : show INDEX\n"
                    + "  Example: show 1\n\n"
                    + "delete - Delete an entry identified by the index used in the contact or itinerary list.\n"
                    + "  Format : delete FLAG INDEX\n"
                    + "  FLAG   : /contact, /itinerary\n"
                    + "  Example: delete /contact 2\n\n"
                    + "clear  - Clear all entries\n"
                    + "  Format : clear\n\n"
                    + "exit   - Exit the application\n"
                    + "  Format : exit\n\n"
                    + "help   - Show this help window\n"
                    + "  Format : help\n"
                    + "----------------------------------\n"
                    + "FOR MORE INFORMATION:\n"
                    + "Refer to the user guide: " + USERGUIDE_URL + "\n";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private TextArea helpMessage;

    @FXML
    private AnchorPane mainLayout;

    @FXML
    private HBox bottomBar;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);

        root.setWidth(750.0);
        root.setHeight(500.0);

        helpMessage.setText(HELP_MESSAGE);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }
}
