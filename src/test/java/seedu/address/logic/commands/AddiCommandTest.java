package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.person.Person;
import seedu.address.testutil.ItineraryBuilder;

public class AddiCommandTest {


    @Test
    public void constructor_nullItinerary_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddiCommand(null));
    }

    @Test
    public void execute_itineraryAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingItineraryAdded modelStub = new ModelStubAcceptingItineraryAdded();
        Itinerary validItinerary = new ItineraryBuilder().build();

        CommandResult commandResult = new AddiCommand(validItinerary).execute(modelStub);

        assertEquals(String.format(AddiCommand.MESSAGE_SUCCESS, Messages.format(validItinerary)),
                commandResult.getFeedbackToUser());
    }

    @Test
    public void equals() {
        Itinerary france = new ItineraryBuilder().build();
        Itinerary bali = new ItineraryBuilder().withName("3D2N Bali").withDestination("Bali")
                            .withDateRange("2020-02-01", "2020-02-04").build();
        AddiCommand addFranceCommand = new AddiCommand(france);
        AddiCommand addBaliCommand = new AddiCommand(bali);

        // same object -> returns true
        assertTrue(addFranceCommand.equals(addFranceCommand));

        // same values -> returns true
        AddiCommand addFranceCommandCopy = new AddiCommand(france);
        assertTrue(addFranceCommand.equals(addFranceCommandCopy));

        // different types -> returns false
        assertFalse(addFranceCommand.equals(1));

        // null -> returns false
        assertFalse(addFranceCommand.equals(null));

        // different person -> returns false
        assertFalse(addFranceCommand.equals(addBaliCommand));

    }


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
     * A Model stub that always accepts the itinerary being added.
     */
    private class ModelStubAcceptingItineraryAdded extends ModelStub {
        final ArrayList<Itinerary> itineraries = new ArrayList<>();


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
