package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.id.Id;
import seedu.address.testutil.PersonBuilder;

public class IdMatchesPredicateTest {

    @Test
    public void equals() {
        List<Id> firstPredicateIdList = Collections.singletonList(new Id());
        List<Id> secondPredicateIdList = Collections.singletonList(new Id());

        IdMatchesPredicate firstPredicate = new IdMatchesPredicate(firstPredicateIdList);
        IdMatchesPredicate secondPredicate = new IdMatchesPredicate(secondPredicateIdList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        IdMatchesPredicate firstPredicateCopy = new IdMatchesPredicate(firstPredicateIdList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different values -> return false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_idMatches_returnsTrue() {
        // One id
        Person person = new PersonBuilder().build();
        IdMatchesPredicate predicate = new IdMatchesPredicate(Collections.singletonList(person.getId()));
        assertTrue(predicate.test(person));

        // Multiple ids
        predicate = new IdMatchesPredicate(Arrays.asList(person.getId(), new Id(), new Id()));
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_idDoesNotMatch_returnsFalse() {
        Person person = new PersonBuilder().build();
        // Zero ids
        IdMatchesPredicate predicate = new IdMatchesPredicate(Collections.emptyList());
        assertFalse(predicate.test(person));

        // Non-matching id
        predicate = new IdMatchesPredicate(Collections.singletonList(new Id()));
        assertFalse(predicate.test(person));
    }

    @Test
    public void toStringMethod() {
        List<Id> ids = List.of(new Id(), new Id());
        IdMatchesPredicate predicate = new IdMatchesPredicate(ids);

        String expected = IdMatchesPredicate.class.getCanonicalName() + "{ids=" + ids + "}";
        assertEquals(expected, predicate.toString());
    }
}
