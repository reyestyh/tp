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
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CONTACTS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ITINERARIES;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";
    public static final String CONTACT_FLAG = "/contact";
    public static final String ITINERARY_FLAG = "/itinerary";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the contact or itinerary identified "
            + "by the index number used in the displayed list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters:\n"
            + "  " + CONTACT_FLAG + " INDEX (must be a positive integer) "
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
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com\n"
            + "  " + COMMAND_WORD + " " + ITINERARY_FLAG + " 2 "
            + PREFIX_ITINERARY_DESTINATION + "Japan "
            + PREFIX_ITINERARY_START + "2026-11-01 "
            + PREFIX_ITINERARY_END + "2026-11-07";

    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";
    public static final String MESSAGE_EDIT_ITINERARY_SUCCESS = "Edited Itinerary: %1$s";
    public static final String MESSAGE_DUPLICATE_ITINERARY= "This itinerary already exists in the address book.";

    /**
     * Represents the possible flags that can be used to delete entries.
     */
    public enum EditType { CONTACT, ITINERARY }

    private final Index index;
    private final EditCommand.EditType flag;
    private final EditPersonDescriptor editPersonDescriptor;
    private final EditItineraryDescriptor editItineraryDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param flag type of entity to edit
     */
    public EditCommand(Index index, EditType flag, EditPersonDescriptor editPersonDescriptor, EditItineraryDescriptor editItineraryDescriptor) {
        requireNonNull(index);
        requireNonNull(flag);

        this.index = index;
        this.flag = flag;
        this.editPersonDescriptor = editPersonDescriptor;
        this.editItineraryDescriptor = editItineraryDescriptor;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (flag.equals(EditCommand.EditType.CONTACT)) {
            List<Person> lastShownList = model.getFilteredPersonList();

            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Person personToEdit = lastShownList.get(index.getZeroBased());
            Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

            if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            }

            model.setPerson(personToEdit, editedPerson);
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_CONTACTS);
            return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
        } else if (flag.equals(EditCommand.EditType.ITINERARY)) {
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
        } else {
            throw new CommandException(Messages.MESSAGE_INVALID_COMMAND_FORMAT);
        }
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        UUID id = personToEdit.getId();
        Role updatedRole = personToEdit.getRole();
        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        return new Person(id, updatedRole, updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags);
    }

    /**
     * Creates and returns a {@code Itinerary} with the details of {@code itineraryToEdit}
     * edited with {@code editItineraryDescriptor}.
     */
    private static Itinerary createEditedItinerary(Itinerary itineraryToEdit, EditItineraryDescriptor editItineraryDescriptor) {
        assert itineraryToEdit != null;

        ItineraryName updatedItineraryName = editItineraryDescriptor.getItineraryName().orElse(itineraryToEdit.getName());
        Destination updatedDestination = editItineraryDescriptor.getDestination().orElse(itineraryToEdit.getDestination());
        LocalDate updatedStartDate = editItineraryDescriptor.getStartDate().orElse(itineraryToEdit.getDateRange().getStartDate());
        LocalDate updatedEndDate = editItineraryDescriptor.getEndDate().orElse(itineraryToEdit.getDateRange().getEndDate());
        DateRange updatedDateRange = new DateRange(updatedStartDate.toString(), updatedEndDate.toString());

        return new Itinerary(updatedItineraryName, updatedDestination, updatedDateRange,
                itineraryToEdit.getClientIds(), itineraryToEdit.getVendorIds());
    }

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
        return index.equals(otherEditCommand.index)
                && flag.equals(otherEditCommand.flag)
                && Objects.equals(editPersonDescriptor, otherEditCommand.editPersonDescriptor)
                && Objects.equals(editItineraryDescriptor, otherEditCommand.editItineraryDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("flag", flag)
                .add("editPersonDescriptor", editPersonDescriptor)
                .add("editItineraryDescriptor", editItineraryDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("tags", tags)
                    .toString();
        }
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
                    .add("itinerary name", itineraryName)
                    .add("destination", destination)
                    .add("start date", startDate)
                    .add("end date", endDate)
                    .toString();
        }
    }
}
