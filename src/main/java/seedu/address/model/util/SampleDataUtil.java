package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
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
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Role("client"), new Name("Alex Yeoh"), new Phone("(+65) 87438807"),
                new Email("alexyeoh@example.com"), new Address("Blk 30 Geylang Street 29, #06-40"),
                getTagSet("Vegetarian")),
            new Person(new Role("client"), new Name("Bernice Yu"), new Phone("(+65) 99272758"),
                new Email("berniceyu@example.com"), new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                getTagSet("Teenager")),
            new Person(new Role("vendor"), new Name("Charlotte Oliveiro"), new Phone("(+33) 0112345678"),
                new Email("charlotte@example.com"), new Address("Apple Street 74, #11-04"),
                getTagSet("Bus", "FrenchSpeakingOnly")),
            new Person(new Role("client"), new Name("David Li"), new Phone("(+65) 91031282"),
                new Email("lidavid@example.com"), new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                getTagSet("Wheelchair")),
            new Person(new Role("vendor"), new Name("Irfan Ibrahim"), new Phone("(+33) 0297861111"),
                new Email("irfan@example.com"), new Address("Cherry lane 4, #17-35"),
                getTagSet("Hotel")),
            new Person(new Role("client"), new Name("Roy Balakrishnan"), new Phone("(+65) 92624417"),
                new Email("royb@example.com"), new Address("Blk 45 Aljunied Street 85, #11-31"),
                getTagSet("PeanutAllergy"))
        };
    }

    public static Itinerary[] getSampleItineraries() {

        return new Itinerary[] {
                new Itinerary(new ItineraryName("5D4N France Getaway"), new Destination("France"),
                        new DateRange("2026-10-12", "2026-10-17")),
                new Itinerary(new ItineraryName("Bali Adventure"), new Destination("Bali, Indonesia"),
                        new DateRange("2026-03-04", "2026-03-07")),
        };

    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        for (Itinerary sampleItinerary : getSampleItineraries()) {
            sampleAb.addItinerary(sampleItinerary);
        }

        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
