package seedu.address.logic.commands;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ITINERARIES;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.itinerary.DateRange;
import seedu.address.model.itinerary.Destination;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.itinerary.ItineraryName;

/**
 * Edits the details of an itinerary person in TripScribe.
 */
public class EditItineraryCommand extends EditCommand {
    public static final String MESSAGE_EDIT_ITINERARY_SUCCESS = "Edited Itinerary: %1$s";
    public static final String MESSAGE_DUPLICATE_ITINERARY = "This itinerary already exists in the address book.";
    public static final String MESSAGE_INVALID_START_DATE = "Start date cannot be after the end date.";
    public static final String MESSAGE_INVALID_END_DATE = "End date cannot be before the start date.";

    private final EditItineraryDescriptor editItineraryDescriptor;

    /**
     * Creates an EditItineraryCommand to edit the itinerary at the specified index.
     * @param index of the itinerary in the filtered itinerary list to edit
     */
    public EditItineraryCommand(Index index, EditItineraryDescriptor editItineraryDescriptor) {
        super(index);
        this.editItineraryDescriptor = editItineraryDescriptor;
    }

    /**
     * Edits the itinerary object.
     */
    public CommandResult executeEditCommand(Model model) throws CommandException {
        List<Itinerary> lastShownList = model.getFilteredItineraryList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ITINERARY_DISPLAYED_INDEX);
        }

        Itinerary itineraryToEdit = lastShownList.get(index.getZeroBased());
        Itinerary editedItinerary = createEditedItinerary(itineraryToEdit, editItineraryDescriptor);

        if (!itineraryToEdit.isSameItinerary(editedItinerary) && model.hasItinerary(editedItinerary)) {
            throw new CommandException(MESSAGE_DUPLICATE_ITINERARY);
        }

        model.setItinerary(itineraryToEdit, editedItinerary);
        model.updateFilteredItineraryList(PREDICATE_SHOW_ALL_ITINERARIES);
        return new CommandResult(String.format(MESSAGE_EDIT_ITINERARY_SUCCESS, Messages.format(editedItinerary)));
    }

    /**
     * Creates and returns a {@code Itinerary} with the details of {@code itineraryToEdit}
     * edited with {@code editItineraryDescriptor}.
     */
    private static Itinerary createEditedItinerary(Itinerary itineraryToEdit,
                                                   EditItineraryDescriptor editItineraryDescriptor)
            throws CommandException {
        assert itineraryToEdit != null;

        ItineraryName updatedItineraryName = editItineraryDescriptor.getItineraryName()
                .orElse(itineraryToEdit.getName());
        Destination updatedDestination = editItineraryDescriptor.getDestination()
                .orElse(itineraryToEdit.getDestination());
        LocalDate updatedStartDate = editItineraryDescriptor.getStartDate()
                .orElse(itineraryToEdit.getDateRange().getStartDate());
        LocalDate updatedEndDate = editItineraryDescriptor.getEndDate()
                .orElse(itineraryToEdit.getDateRange().getEndDate());

        if (updatedStartDate.isAfter(updatedEndDate)) {
            throw new CommandException(MESSAGE_INVALID_START_DATE);
        } else if (updatedEndDate.isBefore(updatedStartDate)) {
            throw new CommandException(MESSAGE_INVALID_END_DATE);
        }

        DateRange updatedDateRange = new DateRange(updatedStartDate.toString(), updatedEndDate.toString());

        return new Itinerary(updatedItineraryName, updatedDestination, updatedDateRange,
                itineraryToEdit.getClientIds(), itineraryToEdit.getVendorIds());
    }

    /**
     * Stores the details to edit the itinerary with. Each non-empty field value will replace the
     * corresponding field value of the itinerary.
     */
    public static class EditItineraryDescriptor {
        private ItineraryName itineraryName;
        private Destination destination;
        private LocalDate startDate;
        private LocalDate endDate;

        public EditItineraryDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditItineraryDescriptor(EditItineraryDescriptor toCopy) {
            setItineraryName(toCopy.itineraryName);
            setDestination(toCopy.destination);
            setStartDate(toCopy.startDate);
            setEndDate(toCopy.endDate);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(itineraryName, destination, startDate, endDate);
        }

        public void setItineraryName(ItineraryName itineraryName) {
            this.itineraryName = itineraryName;
        }

        public Optional<ItineraryName> getItineraryName() {
            return Optional.ofNullable(itineraryName);
        }

        public void setDestination(Destination destination) {
            this.destination = destination;
        }

        public Optional<Destination> getDestination() {
            return Optional.ofNullable(destination);
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public Optional<LocalDate> getStartDate() {
            return Optional.ofNullable(startDate);
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public Optional<LocalDate> getEndDate() {
            return Optional.ofNullable(endDate);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditItineraryDescriptor)) {
                return false;
            }

            EditItineraryDescriptor otherEditItineraryDescriptor = (EditItineraryDescriptor) other;
            return Objects.equals(itineraryName, otherEditItineraryDescriptor.itineraryName)
                    && Objects.equals(destination, otherEditItineraryDescriptor.destination)
                    && Objects.equals(startDate, otherEditItineraryDescriptor.startDate)
                    && Objects.equals(endDate, otherEditItineraryDescriptor.endDate);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("itineraryName", itineraryName)
                    .add("destination", destination)
                    .add("startDate", startDate)
                    .add("endDate", endDate)
                    .toString();
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditItineraryCommand)) {
            return false;
        }

        EditItineraryCommand otherEditCommand = (EditItineraryCommand) other;
        return index.equals(otherEditCommand.index)
                && editItineraryDescriptor.equals(otherEditCommand.editItineraryDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editItineraryDescriptor", editItineraryDescriptor)
                .toString();
    }
}
