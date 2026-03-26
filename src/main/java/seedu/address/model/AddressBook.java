package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.id.Id;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.itinerary.UniqueItineraryList;
import seedu.address.model.person.Person;
import seedu.address.model.person.Role;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison and by .isSameItinerary comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueItineraryList itineraries;

    private final Set<Id> clientIds = new HashSet<>();
    private final Set<Id> vendorIds = new HashSet<>();

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        itineraries = new UniqueItineraryList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
        clientIds.clear();
        vendorIds.clear();
        for (Person person : persons) {
            addPersonId(person);
        }
    }

    /**
     * Replaces the contents of the itinerary list with {@code itineraries}.
     * {@code itineraries} must not contain duplicate itineraries.
     */
    public void setItineraries(List<Itinerary> itineraries) {
        for (Itinerary itinerary : itineraries) {
            if (!hasPersonsWithIds(itinerary)) {
                throw new PersonNotFoundException();
            }
        }
        this.itineraries.setItineraries(itineraries);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setItineraries(newData.getItineraryList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
        addPersonId(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
        removePersonId(target);
        addPersonId(editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        Id id = key.getId();
        persons.remove(key);
        itineraries.removePerson(id);
        removePersonId(key);
    }

    /// itinerary-level operations

    /**
     * Returns true if an itinerary with the same identity as {@code itinerary} exists in the address book.
     */
    public boolean hasItinerary(Itinerary itinerary) {
        requireNonNull(itinerary);
        return itineraries.contains(itinerary);
    }

    /**
     * Adds an itinerary to the address book.
     * The itinerary must not already exist in the address book.
     * Client and vendor ids of itinerary must already exist in the address book.
     */
    public void addItinerary(Itinerary i) {
        if (!hasPersonsWithIds(i)) {
            throw new PersonNotFoundException();
        }
        itineraries.add(i);
    }

    /**
     * Replaces the given itinerary {@code target} in the list with {@code editedItinerary}.
     * {@code target} must exist in the address book.
     * The itinerary identity of {@code editedItinerary} must not be the same as another existing itinerary in the
     * address book.
     */
    public void setItinerary(Itinerary target, Itinerary editedItinerary) {
        requireNonNull(editedItinerary);
        if (!hasPersonsWithIds(editedItinerary)) {
            throw new PersonNotFoundException();
        }
        itineraries.setItinerary(target, editedItinerary);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeItinerary(Itinerary key) {
        itineraries.remove(key);
    }

    //// util methods
    private void addPersonId(Person person) {
        switch (person.getRole().value) {
        case CLIENT -> clientIds.add(person.getId());
        case VENDOR -> vendorIds.add(person.getId());
        default -> throw new IllegalArgumentException(Role.MESSAGE_CONSTRAINTS);
        }
    }

    private void removePersonId(Person person) {
        switch (person.getRole().value) {
        case CLIENT -> clientIds.remove(person.getId());
        case VENDOR -> vendorIds.remove(person.getId());
        default -> throw new IllegalArgumentException(Role.MESSAGE_CONSTRAINTS);
        }
    }

    private boolean hasPersonsWithIds(Itinerary itinerary) {
        for (Id clientId : itinerary.getClientIds()) {
            if (!clientIds.contains(clientId)) {
                return false;
            }
        }

        for (Id vendorId : itinerary.getVendorIds()) {
            if (!vendorIds.contains(vendorId)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("itineraries", itineraries)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Itinerary> getItineraryList() {
        return itineraries.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons)
                && itineraries.equals(otherAddressBook.itineraries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persons, itineraries);

    }
}
