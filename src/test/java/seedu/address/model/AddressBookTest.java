package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ITINERARY_NAME_BALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.AddressBookBuilder.getTypicalAddressBook;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.testutil.ItineraryBuilder;
import seedu.address.testutil.PersonBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newPersons, Collections.emptyList());

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void removePerson_personReferencedByItinerary_removesPersonIdFromItineraries() {
        Person person = new PersonBuilder().build();
        Itinerary itinerary = new ItineraryBuilder()
                .withClientIds(Set.of(person.getId()))
                .build();
        addressBook.addPerson(person);
        addressBook.addItinerary(itinerary);

        addressBook.removePerson(person);

        assertFalse(addressBook.hasPerson(person));
        Itinerary updatedItinerary = addressBook.getItineraryList().get(0);
        assertFalse(updatedItinerary.getVendorIds().contains(person.getId()));
    }

    @Test
    public void addItinerary_personReferencedByItineraryDoesNotExist() {
        Person person = new PersonBuilder().build();
        Itinerary itinerary = new ItineraryBuilder().withClientIds(Set.of(person.getId())).build();
        assertThrows(PersonNotFoundException.class, () -> addressBook.addItinerary(itinerary));
    }

    @Test
    public void setItinerary_personReferencedByItineraryDoesNotExist() {
        Person person = new PersonBuilder().build();
        Itinerary itinerary = new ItineraryBuilder().build();
        addressBook.addItinerary(itinerary);
        Itinerary editedItinerary = new ItineraryBuilder().withName(VALID_ITINERARY_NAME_BALI)
                .withClientIds(Set.of(person.getId())).build();
        assertThrows(PersonNotFoundException.class, () -> addressBook.setItinerary(itinerary, editedItinerary));
    }

    @Test
    public void addItinerary_personReferencedByItineraryHasWrongRole() {
        Person person = new PersonBuilder().build(); // client
        Itinerary itinerary = new ItineraryBuilder().withVendorIds(Set.of(person.getId())).build();
        addressBook.addPerson(person);
        assertThrows(PersonNotFoundException.class, () -> addressBook.addItinerary(itinerary));
    }

    @Test
    public void setItinerary_personReferencedByItineraryHasWrongRole() {
        Person person = new PersonBuilder().build(); // client
        Itinerary itinerary = new ItineraryBuilder().build();
        addressBook.addPerson(person);
        addressBook.addItinerary(itinerary);
        Itinerary editedItinerary = new ItineraryBuilder().withName(VALID_ITINERARY_NAME_BALI)
                .withVendorIds(Set.of(person.getId())).build();
        assertThrows(PersonNotFoundException.class, () -> addressBook.setItinerary(itinerary, editedItinerary));
    }

    @Test
    public void setItineraries_personReferencedByItinerariesDoesNotExist() {
        Person person = new PersonBuilder().build();
        Itinerary itinerary = new ItineraryBuilder().withClientIds(Set.of(person.getId())).build();
        assertThrows(PersonNotFoundException.class, () -> addressBook.setItineraries(List.of(itinerary)));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName()
                + "{persons=" + addressBook.getPersonList()
                + ", itineraries=" + addressBook.getItineraryList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Itinerary> itineraries = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons, Collection<Itinerary> itineraries) {
            this.persons.setAll(persons);
            this.itineraries.setAll(itineraries);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Itinerary> getItineraryList() {
            return itineraries;
        }
    }

}
