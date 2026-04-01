package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Region> {

    private static final String FXML = "ResultDisplay.fxml";
    private static final int MIN_RESULT_HEIGHT = 70;
    private static final int MAX_RESULT_HEIGHT = 160;
    private static final int RESULT_DISPLAY_PADDING = 20;

    @FXML
    private StackPane placeHolder;
    @FXML
    private TextArea resultDisplay;

    public ResultDisplay() {
        super(FXML);
    }

    public void setFeedbackToUser(String feedbackToUser) {
        requireNonNull(feedbackToUser);
        resultDisplay.setText(feedbackToUser);
    }

    /**
     * Adjusts the result display to fit its content.
     */
    private void adjustHeight() {
        Text textNode = (Text) resultDisplay.lookup(".text");
        if (textNode == null) {
            return;
        }
        double contentHeight = textNode.getBoundsInLocal().getHeight() + RESULT_DISPLAY_PADDING;
        double newHeight = Math.max(MIN_RESULT_HEIGHT, Math.min(contentHeight, MAX_RESULT_HEIGHT));
        resultDisplay.setPrefHeight(newHeight);
        placeHolder.setPrefHeight(newHeight);
    }
}
