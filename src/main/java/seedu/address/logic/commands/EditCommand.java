package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_DESTINATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_END;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_START;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Edits the details of an existing person or itinerary as implemented by subclasses.
 */
public abstract class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";
    public static final String CONTACT_FLAG = "/contact";
    public static final String ITINERARY_FLAG = "/itinerary";

    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of the contact or itinerary identified "
            + "by the index number used in the displayed list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters:\n"
            + "  " + CONTACT_FLAG + " INDEX (must be a positive integer) "
            + "[" + PREFIX_ROLE + "ROLE] "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "  " + ITINERARY_FLAG + " INDEX (must be a positive integer) "
            + "[" + PREFIX_ITINERARY_NAME + "NAME] "
            + "[" + PREFIX_ITINERARY_DESTINATION + "DESTINATION] "
            + "[" + PREFIX_ITINERARY_START + "START_DATE] "
            + "[" + PREFIX_ITINERARY_END + "END_DATE]\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " " + CONTACT_FLAG + " 1 "
            + PREFIX_PHONE + "(+65) 91234567 "
            + PREFIX_EMAIL + "johndoe@example.com\n"
            + "  " + COMMAND_WORD + " " + ITINERARY_FLAG + " 2 "
            + PREFIX_ITINERARY_DESTINATION + "Japan "
            + PREFIX_ITINERARY_START + "2026-11-01 "
            + PREFIX_ITINERARY_END + "2026-11-07";

    protected final Index index;

    /**
     * Creates an EditCommand to edit the entity at the specified index.
     * @param index of the person or itinerary in the filtered person or itinerary list to edit
     */
    public EditCommand(Index index) {
        requireNonNull(index);

        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        return executeEditCommand(model);
    }

    /**
     * Edits the person or itinerary object, as implemented by subclasses.
     */
    protected abstract CommandResult executeEditCommand(Model model) throws CommandException;

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .toString();
    }
}
