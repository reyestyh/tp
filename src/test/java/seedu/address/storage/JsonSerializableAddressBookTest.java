package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalItineraries.BALI_TRIP;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.testutil.AddressBookBuilder;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_ADDRESS_BOOK_FILE = TEST_DATA_FOLDER.resolve("typicalAddressBook.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonAddressBook.json");
    private static final Path INVALID_ITINERARY_FILE = TEST_DATA_FOLDER.resolve("invalidItineraryAddressBook.json");
    private static final Path DUPLICATE_ITINERARY_FILE = TEST_DATA_FOLDER.resolve("duplicateItineraryAddressBook.json");

    @Test
    public void toModelType_typicalFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_ADDRESS_BOOK_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalAddressBook = AddressBookBuilder.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalAddressBook);
    }

    @Test
    public void toModelType_invalidPersonFile_returnsEmptyAddressBook() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook expectedAddressBook = new AddressBook();
        assertEquals(expectedAddressBook, addressBookFromFile);
    }

    @Test
    public void toModelType_duplicatePersons_containsOnlyOneCopy() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook expectedAddressBook = new AddressBook();
        expectedAddressBook.addPerson(ALICE);
        assertEquals(expectedAddressBook, addressBookFromFile);
    }

    @Test
    public void toModelType_duplicateItineraries_containsOnlyOneCopy() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_ITINERARY_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook expectedAddressBook = new AddressBook();
        expectedAddressBook.addItinerary(BALI_TRIP);
        assertEquals(expectedAddressBook, addressBookFromFile);
    }

}
