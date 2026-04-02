package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.id.Id;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.person.Person;
import seedu.address.model.person.Role;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_CONTACTS = person -> true;

    /** {@code Predicate} that evaluates to true if the Person contains the 'Client' tag, and false otherwise */
    Predicate<Person> PREDICATE_SHOW_ALL_CLIENTS = person -> (person.hasSpecifiedRole(new Role("client")));

    /** {@code Predicate} that always evaluate to true if the Person contains the 'Vendor' tag, and false otherwise */
    Predicate<Person> PREDICATE_SHOW_ALL_VENDORS = person -> (person.hasSpecifiedRole(new Role("vendor")));

    /** {@code Predicate} that always evaluate to true */
    Predicate<Itinerary> PREDICATE_SHOW_ALL_ITINERARIES = itinerary -> true;

    /** {@code Predicate} that always evaluate to false */
    Predicate<Itinerary> PREDICATE_SHOW_NO_ITINERARIES = itinerary -> false;

    /** {@code Predicate} that always evaluate to false */
    Predicate<Person> PREDICATE_SHOW_NO_CONTACTS = person -> false;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /**
     * Updates the role of the person in any itineraries they are associated with.
     */
    void updatePersonRole(Id personId, Role newRole);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns true if an itinerary with the same identity as {@code itinerary} exists in the address book.
     */
    boolean hasItinerary(Itinerary itinerary);

    /**
     * Deletes the given itinerary.
     * The itinerary must exist in the address book.
     */
    void deleteItinerary(Itinerary target);

    /**
     * Adds the given itinerary.
     * {@code itinerary} must not already exist in the address book.
     */
    void addItinerary(Itinerary itinerary);

    /**
     * Replaces the given itinerary {@code target} with {@code editedItinerary}.
     * {@code target} must exist in the address book.
     * The itinerary identity of {@code editedItinerary} must not be the same as another existing itinerary in the
     * address book.
     */
    void setItinerary(Itinerary target, Itinerary editedItinerary);

    /** Returns an unmodifiable view of the filtered itinerary list */
    ObservableList<Itinerary> getFilteredItineraryList();

    /**
     * Updates the filter of the filtered itinerary list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
    */
    void updateFilteredItineraryList(Predicate<Itinerary> predicate);

}
