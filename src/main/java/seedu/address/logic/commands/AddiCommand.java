package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_DESTINATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_END;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_START;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.itinerary.Itinerary;

/**
 * Adds an itinerary to TripScribe.
 */
public class AddiCommand extends Command {

    public static final String COMMAND_WORD = "addi";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a itinerary to TripScribe. "
            + "Parameters: "
            + PREFIX_ITINERARY_NAME + "ITINERARY NAME "
            + PREFIX_ITINERARY_DESTINATION + "DESTINATION "
            + PREFIX_ITINERARY_START + "START DATE "
            + PREFIX_ITINERARY_END + "END DATE "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ITINERARY_NAME + "5D4N France Getaway "
            + PREFIX_ITINERARY_DESTINATION + "France "
            + PREFIX_ITINERARY_START + "2026-10-12 "
            + PREFIX_ITINERARY_END + "2026-10-17 ";

    public static final String MESSAGE_SUCCESS = "New itinerary added: %1$s";
    public static final String MESSAGE_DUPLICATE_ITINERARY = "This itinerary already exists in TripScribe";

    private final Itinerary toAdd;

    /**
     * Creates an AddiCommand to add the specified {@code Itinerary}
     */
    public AddiCommand(Itinerary itinerary) {
        requireNonNull(itinerary);
        toAdd = itinerary;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // TODO duplicate checking & adding to Model

        //  if (model.hasItinerary(toAdd)) {
        //      throw new CommandException(MESSAGE_DUPLICATE_ITINERARY);
        //  }

        //  model.addItinerary(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddiCommand)) {
            return false;
        }

        AddiCommand otherAddiCommand = (AddiCommand) other;
        return toAdd.getName().equals(otherAddiCommand.toAdd.getName())
                && toAdd.getDestination().equals(otherAddiCommand.toAdd.getDestination())
                && toAdd.getDateRange().equals(otherAddiCommand.toAdd.getDateRange());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}




