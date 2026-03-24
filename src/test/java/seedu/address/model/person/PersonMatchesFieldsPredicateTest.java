package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonMatchesFieldsPredicateTest {

    @Test
    public void equals() {
        PersonMatchesFieldsPredicate firstPredicate = new PersonMatchesFieldsPredicate(
                Collections.singletonList("Alice"),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());
        PersonMatchesFieldsPredicate secondPredicate = new PersonMatchesFieldsPredicate(
                Collections.singletonList("Bob"),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());

        assertTrue(firstPredicate.equals(firstPredicate));

        PersonMatchesFieldsPredicate firstPredicateCopy = new PersonMatchesFieldsPredicate(
                Collections.singletonList("Alice"),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_allSpecifiedFieldsMatch_returnsTrue() {
        Person person = new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("(+65) 12345")
                .withEmail("alice@example.com")
                .withAddress("Main Street")
                .withTags("friends")
                .build();

        PersonMatchesFieldsPredicate predicate = new PersonMatchesFieldsPredicate(
                Collections.singletonList("Alice"),
                Collections.singletonList("12345"),
                Collections.singletonList("example.com"),
                Collections.singletonList("Street"),
                Collections.singletonList("friend"));

        assertTrue(predicate.test(person));
    }

    @Test
    public void test_emptyFieldLists_areWildcards() {
        Person person = new PersonBuilder()
                .withName("Alice Bob")
                .withEmail("alice@example.com")
                .build();

        PersonMatchesFieldsPredicate predicate = new PersonMatchesFieldsPredicate(
                Collections.singletonList("aLIce"),
                Collections.emptyList(),
                Collections.singletonList("EXAMPLE.COM"),
                Collections.emptyList(),
                Collections.emptyList());

        assertTrue(predicate.test(person));
    }

    @Test
    public void test_anySpecifiedFieldDoesNotMatch_returnsFalse() {
        Person person = new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("(+65) 12345")
                .withEmail("alice@example.com")
                .withAddress("Main Street")
                .withTags("friends")
                .build();

        PersonMatchesFieldsPredicate predicate = new PersonMatchesFieldsPredicate(
                Collections.singletonList("Alice"),
                Collections.singletonList("99999"),
                Collections.singletonList("example.com"),
                Collections.singletonList("Street"),
                Collections.singletonList("friend"));

        assertFalse(predicate.test(person));
    }

    @Test
    public void test_allFieldListsEmpty_returnsTrue() {
        PersonMatchesFieldsPredicate predicate = new PersonMatchesFieldsPredicate(
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());

        assertTrue(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void toStringMethod() {
        List<String> nameKeywords = List.of("alice");
        List<String> phoneKeywords = List.of("12345");
        List<String> emailKeywords = List.of("example.com");
        List<String> addressKeywords = List.of("street");
        List<String> tagKeywords = List.of("friend");
        PersonMatchesFieldsPredicate predicate = new PersonMatchesFieldsPredicate(
                nameKeywords, phoneKeywords, emailKeywords, addressKeywords, tagKeywords);

        String expected = PersonMatchesFieldsPredicate.class.getCanonicalName()
                + "{nameKeywords=" + nameKeywords
                + ", phoneKeywords=" + phoneKeywords
                + ", emailKeywords=" + emailKeywords
                + ", addressKeywords=" + addressKeywords
                + ", tagKeywords=" + tagKeywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
