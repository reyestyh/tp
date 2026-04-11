package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_ITINERARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_CLIENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_DESTINATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_END;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_START;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_VENDOR;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.id.Id;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.person.Person;

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
            + PREFIX_ITINERARY_CLIENT + "CLIENT_INDEX "
            + PREFIX_ITINERARY_VENDOR + "VENDOR_INDEX \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ITINERARY_NAME + "5D4N France Getaway "
            + PREFIX_ITINERARY_DESTINATION + "France "
            + PREFIX_ITINERARY_START + "2026-10-12 "
            + PREFIX_ITINERARY_END + "2026-10-17 "
            + PREFIX_ITINERARY_CLIENT + "1 "
            + PREFIX_ITINERARY_VENDOR + "2 ";

    public static final String MESSAGE_SUCCESS = "New itinerary added: %1$s";
    public static final String MESSAGE_NOT_CLIENT = "Invalid role: %1s (at index %d) is not a client.";
    public static final String MESSAGE_NOT_VENDOR = "Invalid role: %1s (at index %d) is not a vendor.";


    private final Itinerary toAdd;
    private final Set<Index> clientIndices;
    private final Set<Index> vendorIndices;

    /**
     * Creates an AddiCommand to add the specified {@code Itinerary}
     */
    public AddiCommand(Itinerary itinerary, Set<Index> clientIndices, Set<Index> vendorIndices) {
        requireAllNonNull(itinerary, clientIndices, vendorIndices);
        toAdd = itinerary;
        this.clientIndices = clientIndices;
        this.vendorIndices = vendorIndices;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasItinerary(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_ITINERARY);
        }

        List<Person> lastShownContactList = model.getFilteredPersonList();
        Set<Id> clientIds = new HashSet<>();
        Set<Id> vendorIds = new HashSet<>();

        for (Index index : clientIndices) {
            if (index.getZeroBased() >= lastShownContactList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            Person person = lastShownContactList.get(index.getZeroBased());
            if (!person.isClient()) {
                throw new CommandException(String.format(MESSAGE_NOT_CLIENT, person.getName(), index.getOneBased()));
            }
            clientIds.add(person.getId());
        }

        for (Index index : vendorIndices) {
            if (index.getZeroBased() >= lastShownContactList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            Person person = lastShownContactList.get(index.getZeroBased());
            if (!person.isVendor()) {
                throw new CommandException(String.format(MESSAGE_NOT_VENDOR, person.getName(), index.getOneBased()));
            }
            vendorIds.add(person.getId());
        }

        toAdd.setClients(clientIds);
        toAdd.setVendors(vendorIds);

        model.addItinerary(toAdd);
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
                && toAdd.getDateRange().equals(otherAddiCommand.toAdd.getDateRange())
                && this.clientIndices.equals(otherAddiCommand.clientIndices)
                && this.vendorIndices.equals(otherAddiCommand.vendorIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .add("clientIndices", clientIndices)
                .add("vendorIndices", vendorIndices)
                .toString();
    }
}



