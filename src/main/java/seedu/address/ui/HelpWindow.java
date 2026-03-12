package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page.
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s2-cs2103t-f12-1.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE =
            "COMMAND SUMMARY\n"
                    + "----------------------------------\n"
                    + "addc - Add a contact\n"
                    + "  Format : addc n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]...\n"
                    + "  Example: addc n/John Doe p/98765432 e/john@email.com a/123 Street t/Client\n\n"
                    + "addi - Add an itinerary\n"
                    + "  Format : addi n/NAME d/DESTINATION sd/START_DATE ed/END_DATE [cid/UUID] [vid/UUID]\n"
                    + "  Example: addi n/5D4N France Getaway d/France sd/2026-06-10 ed/2026-06-14\n\n"
                    + "list - List entries by category\n"
                    + "  Format : list FLAG\n"
                    + "  FLAG   : /contact, /itinerary, /client, /vendor\n"
                    + "  Example: list /contact\n\n"
                    + "edit - Edit a contact\n"
                    + "  Format : edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]...\n"
                    + "  Example: edit 1 p/91234567 e/new@email.com\n\n"
                    + "find - Find contacts by name\n"
                    + "  Format : find KEYWORD [MORE_KEYWORDS]\n"
                    + "  Example: find Alex David\n\n"
                    + "delete - Delete an entry\n"
                    + "  Format : delete INDEX\n"
                    + "  Example: delete 3\n\n"
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
    private Label helpMessage;

    @FXML
    private AnchorPane mainLayout;

    @FXML
    private ScrollPane helpScrollPane;

    @FXML
    private HBox bottomBar;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);

        root.setWidth(700.0);
        root.setHeight(500.0);

        helpScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        helpScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        helpScrollPane.setFitToWidth(true);
        helpScrollPane.setVvalue(0.0);
        AnchorPane.setTopAnchor(helpScrollPane, 0.0);
        AnchorPane.setLeftAnchor(helpScrollPane, 0.0);
        AnchorPane.setRightAnchor(helpScrollPane, 0.0);
        AnchorPane.setBottomAnchor(helpScrollPane, 40.0);

        helpMessage.setText(HELP_MESSAGE);
        helpMessage.setPrefHeight(Region.USE_COMPUTED_SIZE);
        helpMessage.setPadding(new Insets(10, 15, 10, 15));

        bottomBar.setPrefHeight(40.0);
        bottomBar.setPadding(new Insets(5, 10, 5, 10));
        AnchorPane.setBottomAnchor(bottomBar, 0.0);
        AnchorPane.setLeftAnchor(bottomBar, 0.0);
        AnchorPane.setRightAnchor(bottomBar, 0.0);
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
