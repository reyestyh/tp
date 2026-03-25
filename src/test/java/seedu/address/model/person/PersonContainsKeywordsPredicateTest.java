package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        PersonContainsKeywordsPredicate firstPredicate =
                new PersonContainsKeywordsPredicate(firstPredicateKeywordList);
        PersonContainsKeywordsPredicate secondPredicate =
                new PersonContainsKeywordsPredicate(secondPredicateKeywordList);

        assertTrue(firstPredicate.equals(firstPredicate));

        PersonContainsKeywordsPredicate firstPredicateCopy =
                new PersonContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_matchesAnyField_returnsTrue() {
        Person person = new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("(+65) 12345")
                .withEmail("alice@example.com")
                .withAddress("Main Street")
                .withTags("friends")
                .build();

        assertTrue(new PersonContainsKeywordsPredicate(Collections.singletonList("Alice")).test(person));
        assertTrue(new PersonContainsKeywordsPredicate(Collections.singletonList("12345")).test(person));
        assertTrue(new PersonContainsKeywordsPredicate(Collections.singletonList("example.com")).test(person));
        assertTrue(new PersonContainsKeywordsPredicate(Collections.singletonList("Street")).test(person));
        assertTrue(new PersonContainsKeywordsPredicate(Collections.singletonList("friend")).test(person));
        assertTrue(new PersonContainsKeywordsPredicate(List.of("Alice", "Ben", "Jason")).test(person));
    }

    @Test
    public void test_mixedCaseKeywords_returnsTrue() {
        Person person = new PersonBuilder()
                .withName("Alice Bob")
                .withEmail("alice@example.com")
                .withTags("friends")
                .build();

        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Arrays.asList("aLIce", "EXAMPLE.COM", "FrIeNd"));

        assertTrue(predicate.test(person));
    }

    @Test
    public void test_noMatchingFields_returnsFalse() {
        Person person = new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("(+65) 12345")
                .withEmail("alice@example.com")
                .withAddress("Main Street")
                .withTags("friends")
                .build();

        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Arrays.asList("Carol", "98765", "office"));

        assertFalse(predicate.test(person));
    }

    @Test
    public void test_zeroKeywords_returnsFalse() {
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.emptyList());

        assertFalse(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);

        String expected = PersonContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
