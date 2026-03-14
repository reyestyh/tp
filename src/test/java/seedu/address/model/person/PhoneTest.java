package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // invalid phone numbers
        assertFalse(Phone.isValidPhone("")); // empty string
        assertFalse(Phone.isValidPhone(" ")); // spaces only
        assertFalse(Phone.isValidPhone("91")); // missing country code
        assertFalse(Phone.isValidPhone("phone")); // non-numeric
        assertFalse(Phone.isValidPhone("(+65)9011p041")); // alphabets within digits
        assertFalse(Phone.isValidPhone("(+65)-93121534")); // invalid separator

        // valid phone numbers
        assertTrue(Phone.isValidPhone("(+1)911")); // minimum valid length
        assertTrue(Phone.isValidPhone("(+65)93121534"));
        assertTrue(Phone.isValidPhone("(+123)124293842033")); // long phone numbers
        assertTrue(Phone.isValidPhone("(+65) 93121534")); // whitespace is normalized
    }

    @Test
    public void equals() {
        Phone phone = new Phone("(+1)999");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("(+1)999")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(5.0f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("(+1)995")));
    }
}
