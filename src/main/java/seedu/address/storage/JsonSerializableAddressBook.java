package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_ITINERARY = "Itineraries list contains duplicate itinerary(ies).";

    private static final Logger logger = LogsCenter.getLogger(JsonSerializableAddressBook.class);

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedItinerary> itineraries = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons and itineraries.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
            @JsonProperty("itineraries") List<JsonAdaptedItinerary> itineraries) {
        this.persons.addAll(persons);
        this.itineraries.addAll(itineraries);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
        itineraries.addAll(source.getItineraryList().stream().map(JsonAdaptedItinerary::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person;
            try {
                person = jsonAdaptedPerson.toModelType();
            } catch (IllegalValueException e) {
                logger.info("Illegal value found in field of a contact entry, skipping.");
                continue;
            }
            assert person != null;
            if (addressBook.hasPerson(person)) {
                logger.info(MESSAGE_DUPLICATE_PERSON + ". Skipping current person entry.");
                continue;
            }
            addressBook.addPerson(person);
        }

        for (JsonAdaptedItinerary jsonAdaptedItinerary : itineraries) {
            Itinerary itinerary;
            try {
                itinerary = jsonAdaptedItinerary.toModelType();
            } catch (IllegalValueException e) {
                logger.info("Illegal value found in field of an itinerary entry, skipping.");
                continue;
            }
            assert itinerary != null;
            if (addressBook.hasItinerary(itinerary)) {
                logger.info(MESSAGE_DUPLICATE_ITINERARY + ". Skipping current itinerary entry.");
                continue;
            }

            try {
                addressBook.addItinerary(itinerary);
            } catch (PersonNotFoundException nfe) {
                logger.info("Unknown Person Id found in itinerary entry, skipping.");
            }
        }
        return addressBook;
    }

}
