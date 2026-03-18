package seedu.address.testutil;

import static seedu.address.testutil.TypicalItineraries.getTypicalItineraries;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;

import seedu.address.model.AddressBook;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.person.Person;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new AddressBookBuilder().withPerson("John", "Doe").build();}
 */
public class AddressBookBuilder {

    private AddressBook addressBook;

    public AddressBookBuilder() {
        addressBook = new AddressBook();
    }

    public AddressBookBuilder(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    /**
     * Adds a new {@code Person} to the {@code AddressBook} that we are building.
     */
    public AddressBookBuilder withPerson(Person person) {
        addressBook.addPerson(person);
        return this;
    }

    /**
     * Adds a new {@code Itinerary} to the {@code AddressBook} that we are building.
     */
    public AddressBookBuilder withItinerary(Itinerary itinerary) {
        addressBook.addItinerary(itinerary);
        return this;
    }

    /**
     * Returns an {@code AddressBook} with all the typical persons and itineraries.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        for (Itinerary itinerary : getTypicalItineraries()) {
            ab.addItinerary(itinerary);
        }
        return ab;
    }

    public AddressBook build() {
        return addressBook;
    }
}
