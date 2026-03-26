package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.id.Id;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.itinerary.ItineraryNameMatchesPredicate;
import seedu.address.model.person.IdMatchesPredicate;

/**
 * Show an existing itinerary with its associated contacts.
 */
public class ShowCommand extends Command {

    public static final String COMMAND_WORD = "show";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows the details and associated contacts "
            + "of a specific itinerary.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 ";

    public static final String MESSAGE_SUCCESS = "Itinerary %1$s shown";

    private final Index index;
    /**
     * @param index of the itinerary in the filtered itinerary list to be shown
     */
    public ShowCommand(Index index) {
        requireNonNull(index);

        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Itinerary> lastShownList = model.getFilteredItineraryList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ITINERARY_DISPLAYED_INDEX);
        }

        Itinerary itineraryToShow = lastShownList.get(index.getZeroBased());

        ItineraryNameMatchesPredicate itineraryNameMatchesPredicate = new ItineraryNameMatchesPredicate(
                List.of(itineraryToShow.getName()));
        model.updateFilteredItineraryList(itineraryNameMatchesPredicate);

        List<Id> ids = Stream.concat(
                itineraryToShow.getClientIds().stream(),
                itineraryToShow.getVendorIds().stream())
                .collect(Collectors.toList());
        IdMatchesPredicate idMatchesPredicate = new IdMatchesPredicate(ids);
        model.updateFilteredPersonList(idMatchesPredicate);

        return new CommandResult(String.format(MESSAGE_SUCCESS, itineraryToShow.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ShowCommand)) {
            return false;
        }

        ShowCommand otherShowCommand = (ShowCommand) other;
        return index.equals(otherShowCommand.index);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .toString();
    }
}
