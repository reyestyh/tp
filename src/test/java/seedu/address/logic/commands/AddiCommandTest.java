package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.time.LocalDate;
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

public class AddiCommandTest {

    private static Itinerary validItineraryA = new Itinerary("3D2N Bali", "Bali",
            LocalDate.of(2025, 2, 2),
            LocalDate.of(2025, 2, 5));

    @Test
    public void constructor_nullItinerary_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddiCommand(null));
    }

    @Test
    public void execute_itineraryAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingItineraryAdded modelStub = new ModelStubAcceptingItineraryAdded();
        Itinerary validItinerary = AddiCommandTest.validItineraryA;

        CommandResult commandResult = new AddiCommand(validItinerary).execute(modelStub);

        assertEquals(String.format(AddiCommand.MESSAGE_SUCCESS, Messages.format(validItinerary)),
                commandResult.getFeedbackToUser());
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
        public void addPerson(Person person) {
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
