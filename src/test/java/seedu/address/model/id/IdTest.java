package seedu.address.model.id;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class IdTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Id(null));
    }

    @Test
    public void constructor_invalidId_throwsIllegalArgumentException() {
        String emptyId = "";
        assertThrows(IllegalArgumentException.class, () -> new Id(emptyId));

        String spacesOnlyId = "   ";
        assertThrows(IllegalArgumentException.class, () -> new Id(spacesOnlyId));

        String invalidId = "abc";
        assertThrows(IllegalArgumentException.class, () -> new Id(invalidId));
    }

    @Test
    public void isValidId() {
        // null id
        assertThrows(NullPointerException.class, () -> Id.isValidId(null));

        assertFalse(Id.isValidId(""));
        assertFalse(Id.isValidId("   "));
        assertFalse(Id.isValidId("abc"));

        // valid id
        assertTrue(Id.isValidId(UUID.randomUUID().toString()));
    }

    @Test
    public void equals() {
        UUID uuid = UUID.randomUUID();
        Id id = new Id(uuid.toString());

        // same object -> returns true
        assertTrue(id.equals(id));

        // same values -> returns true
        assertTrue(id.equals(new Id(uuid.toString())));
        assertTrue(id.equals(new Id(id.toString())));

        // null -> returns false
        assertFalse(id.equals(null));

        // different types -> returns false
        assertFalse(id.equals(1));

        // different values -> returns false
        assertFalse(id.equals(new Id(UUID.randomUUID().toString())));
    }

    @Test
    public void toStringMethod() {
        UUID uuid = UUID.randomUUID();
        String expected = uuid.toString();
        assertEquals(expected, new Id(uuid.toString()).toString());
    }
}
