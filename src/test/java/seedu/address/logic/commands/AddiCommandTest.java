package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD;
import static seedu.address.testutil.TypicalItineraries.FRANCE_TRIP;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.id.Id;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.person.Person;
import seedu.address.model.person.Role;
import seedu.address.testutil.ItineraryBuilder;
import seedu.address.testutil.PersonBuilder;

public class AddiCommandTest {

    private Person client = new PersonBuilder().build();
    private Person vendor = new PersonBuilder().withRole("vendor").build();


    @Test
    public void constructor_nullItinerary_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddiCommand(null, new HashSet<>(), new HashSet<>()));
    }

    @Test
    public void constructor_nullIndexSet_throwsNullPointerException() {
        Itinerary validItinerary = new ItineraryBuilder().build();
        assertThrows(NullPointerException.class, () -> new AddiCommand(validItinerary, null, new HashSet<>()));
        assertThrows(NullPointerException.class, () -> new AddiCommand(validItinerary, new HashSet<>(), null));
        assertThrows(NullPointerException.class, () -> new AddiCommand(validItinerary, null, null));

    }

    @Test
    public void execute_itineraryAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingItineraryAdded modelStub = new ModelStubAcceptingItineraryAdded();
        Itinerary validItinerary = new ItineraryBuilder().build();

        CommandResult commandResult = new AddiCommand(validItinerary, new HashSet<>(), new HashSet<>())
                                          .execute(modelStub);

        assertEquals(String.format(AddiCommand.MESSAGE_SUCCESS, Messages.format(validItinerary)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validItinerary), modelStub.itineraries);
    }

    @Test
    public void execute_itineraryWithClientAcceptedByModel_addSuccessful() throws Exception {
        ModelStubWithClient modelStub = new ModelStubWithClient();
        Itinerary validItinerary = new ItineraryBuilder().build();

        CommandResult commandResult = new AddiCommand(validItinerary, new HashSet<>(Set.of(INDEX_FIRST)),
                                                      new HashSet<>()).execute(modelStub);

        assertEquals(String.format(AddiCommand.MESSAGE_SUCCESS, Messages.format(validItinerary)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validItinerary), modelStub.itineraries);
    }

    @Test
    public void execute_itineraryWithVendorAcceptedByModel_addSuccessful() throws Exception {
        ModelStubWithVendor modelStub = new ModelStubWithVendor();
        Itinerary validItinerary = new ItineraryBuilder().build();

        CommandResult commandResult = new AddiCommand(validItinerary, new HashSet<>(),
                                                      new HashSet<>(Set.of(INDEX_FIRST))).execute(modelStub);

        assertEquals(String.format(AddiCommand.MESSAGE_SUCCESS, Messages.format(validItinerary)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validItinerary), modelStub.itineraries);
    }

    @Test
    public void execute_duplicateItinerary_throwsCommandException() {
        Itinerary validItinerary = new ItineraryBuilder().build();
        AddiCommand addiCommand = new AddiCommand(validItinerary, new HashSet<>(), new HashSet<>());
        ModelStub modelStub = new ModelStubWithItinerary(validItinerary);

        assertThrows(CommandException.class,
                AddiCommand.MESSAGE_DUPLICATE_ITINERARY, () -> addiCommand.execute(modelStub));
    }

    @Test
    public void execute_argumentClientMismatch_throwsCommandException() {
        Itinerary validItinerary = new ItineraryBuilder().build();
        HashSet<Index> vendorIndexes = new HashSet<>();
        vendorIndexes.add(INDEX_FIRST);
        AddiCommand addiCommand = new AddiCommand(validItinerary, new HashSet<>(), vendorIndexes);
        ModelStub modelStub = new ModelStubWithClient();
        String expectedMessage = String.format(AddiCommand.MESSAGE_NOT_VENDOR, client.getName());

        assertThrows(CommandException.class, expectedMessage, () -> addiCommand.execute(modelStub));
    }

    @Test
    public void execute_argumentVendorMismatch_throwsCommandException() {
        Itinerary validItinerary = new ItineraryBuilder().build();
        HashSet<Index> clientIndexes = new HashSet<>();
        clientIndexes.add(INDEX_FIRST);
        AddiCommand addiCommand = new AddiCommand(validItinerary, clientIndexes, new HashSet<>());
        ModelStub modelStub = new ModelStubWithVendor();
        String expectedMessage = String.format(AddiCommand.MESSAGE_NOT_CLIENT, vendor.getName());

        assertThrows(CommandException.class, expectedMessage, () -> addiCommand.execute(modelStub));
    }
    @Test
    public void execute_indexNotFound_throwsCommandException() {
        Itinerary validItinerary = new ItineraryBuilder().build();
        HashSet<Index> clientIndexes = new HashSet<>();
        clientIndexes.add(INDEX_SECOND);
        AddiCommand addiCommandClient = new AddiCommand(validItinerary, clientIndexes, new HashSet<>());
        ModelStub modelStubClient = new ModelStubWithClient();

        assertThrows(CommandException.class, AddiCommand.MESSAGE_PERSON_INDEX_MISSING, ()
                     -> addiCommandClient.execute(modelStubClient));

        HashSet<Index> vendorIndexes = new HashSet<>();
        vendorIndexes.add(INDEX_THIRD);
        AddiCommand addiCommandVendor = new AddiCommand(validItinerary, new HashSet<>(), vendorIndexes);
        ModelStub modelStubVendor = new ModelStubWithVendor();

        assertThrows(CommandException.class, AddiCommand.MESSAGE_PERSON_INDEX_MISSING, ()
                     -> addiCommandVendor.execute(modelStubVendor));
    }


    @Test
    public void equals() {
        Itinerary france = new ItineraryBuilder().build();
        Itinerary bali = new ItineraryBuilder().withName("3D2N Bali").withDestination("Bali")
                            .withDateRange("2020-02-01", "2020-02-04").build();
        AddiCommand addFranceCommand = new AddiCommand(france, new HashSet<>(Set.of(INDEX_FIRST)),
                                                               new HashSet<>(Set.of(INDEX_SECOND)));
        AddiCommand addBaliCommand = new AddiCommand(bali, new HashSet<>(), new HashSet<>());

        // same object -> returns true
        assertTrue(addFranceCommand.equals(addFranceCommand));

        // same values -> returns true
        AddiCommand addFranceCommandCopy = new AddiCommand(france, new HashSet<>(Set.of(INDEX_FIRST)),
                                                                   new HashSet<>(Set.of(INDEX_SECOND)));
        assertTrue(addFranceCommand.equals(addFranceCommandCopy));


        // different types -> returns false
        assertFalse(addFranceCommand.equals(1));

        // null -> returns false
        assertFalse(addFranceCommand.equals(null));

        // different itinerary -> returns false
        assertFalse(addFranceCommand.equals(addBaliCommand));

        // different index sets -> returns false
        AddiCommand addFranceDifferentClientIndices = new AddiCommand(france, new HashSet<>(Set.of(INDEX_THIRD)),
                                                                      new HashSet<>());
        assertFalse(addFranceCommand.equals(addFranceDifferentClientIndices));

        AddiCommand addFranceDifferentVendorIndices = new AddiCommand(france, new HashSet<>(Set.of(INDEX_FIRST)),
                                                                      new HashSet<>());
        assertFalse(addFranceCommand.equals(addFranceDifferentVendorIndices));

    }

    @Test
    public void toStringMethod() {
        AddiCommand addiCommand = new AddiCommand(FRANCE_TRIP, new HashSet<>(), new HashSet<>());
        String expected = AddiCommand.class.getCanonicalName() + "{toAdd=" + FRANCE_TRIP
                                                               + ", clientIndices=[], vendorIndices=[]" + "}";
        assertEquals(expected, addiCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updatePersonRole(Id personId, Role newRole) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasItinerary(Itinerary itinerary) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteItinerary(Itinerary target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addItinerary(Itinerary itinerary) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setItinerary(Itinerary target, Itinerary editedItinerary) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Itinerary> getFilteredItineraryList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredItineraryList(Predicate<Itinerary> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single itinerary.
     */
    private class ModelStubWithItinerary extends ModelStub {
        private final Itinerary itinerary;

        ModelStubWithItinerary(Itinerary itinerary) {
            requireNonNull(itinerary);
            this.itinerary = itinerary;
        }

        @Override
        public boolean hasItinerary(Itinerary itinerary) {
            requireNonNull(itinerary);
            return this.itinerary.isSameItinerary(itinerary);
        }
    }

    /**
     * A Model stub that always accepts the itinerary being added.
     */
    private class ModelStubAcceptingItineraryAdded extends ModelStub {
        final ArrayList<Itinerary> itineraries = new ArrayList<>();
        final ArrayList<Person> persons = new ArrayList<>();

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableList(persons);
        }

        public boolean hasItinerary(Itinerary itinerary) {
            requireNonNull(itinerary);
            return false;
        }

        public void addItinerary(Itinerary itinerary) {
            requireNonNull(itinerary);
            itineraries.add(itinerary);
        }

    }

    /**
     * A Model stub with 1 client
     */
    private class ModelStubWithClient extends ModelStub {
        final ArrayList<Itinerary> itineraries = new ArrayList<>();
        final ArrayList<Person> persons = new ArrayList<>();


        ModelStubWithClient() {
            persons.add(client);
        }

        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableList(persons);
        }

        public boolean hasItinerary(Itinerary itinerary) {
            requireNonNull(itinerary);
            return false;
        }

        public void addItinerary(Itinerary itinerary) {
            requireNonNull(itinerary);
            itineraries.add(itinerary);
        }

    }

    /**
     * A Model stub with 1 Vendor
     */
    private class ModelStubWithVendor extends ModelStub {
        final ArrayList<Itinerary> itineraries = new ArrayList<>();
        final ArrayList<Person> persons = new ArrayList<>();

        ModelStubWithVendor() {
            persons.add(vendor);
        }

        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableList(persons);
        }

        public boolean hasItinerary(Itinerary itinerary) {
            requireNonNull(itinerary);
            return false;
        }

        public void addItinerary(Itinerary itinerary) {
            requireNonNull(itinerary);
            itineraries.add(itinerary);
        }

    }


}
