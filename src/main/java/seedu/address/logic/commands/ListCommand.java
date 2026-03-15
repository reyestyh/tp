package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CLIENTS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CONTACTS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_VENDORS;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all entries of the specified flag.\n"
            + "Parameters: FLAG (must be one of: /contact, /itinerary, /client, /vendor)\n"
            + "Example: " + COMMAND_WORD + " /contact";

    public static final String MESSAGE_SUCCESS_CONTACTS = "Listed all contacts";
    public static final String MESSAGE_SUCCESS_CLIENTS = "Listed all clients";
    public static final String MESSAGE_SUCCESS_VENDORS = "Listed all vendors";

    /**
     * Represents the possible flags that can be used to list entries.
     */
    public enum Flag { CONTACT, ITINERARY, CLIENT, VENDOR }

    private final Flag flag;

    public ListCommand(Flag flag) {
        this.flag = flag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        switch (flag) {
        case CONTACT:
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_CONTACTS);
            return new CommandResult(MESSAGE_SUCCESS_CONTACTS);
        case CLIENT:
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_CLIENTS);
            return new CommandResult(MESSAGE_SUCCESS_CLIENTS);
        case VENDOR:
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_VENDORS);
            return new CommandResult(MESSAGE_SUCCESS_VENDORS);
        default:
            throw new CommandException("Unknown flag: " + flag);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ListCommand)) {
            return false;
        }

        ListCommand otherListCommand = (ListCommand) other;
        return flag.equals(otherListCommand.flag);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("flag", flag)
                .toString();
    }
}
