package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a predicate that performs a general search on a {@code Person}.
 * A person matches if any given keyword is found in any searchable field.
 */
public class PersonContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    /**
     * Constructs a {@code PersonContainsKeywordsPredicate} with the given keywords
     * for general matching against a person's searchable fields.
     *
     * @param keywords The keywords to match.
     */
    public PersonContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return matchesName(person)
                || matchesPhone(person)
                || matchesEmail(person)
                || matchesAddress(person)
                || matchesTag(person);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonContainsKeywordsPredicate)) {
            return false;
        }

        PersonContainsKeywordsPredicate otherPersonContainsKeywordsPredicate = (PersonContainsKeywordsPredicate) other;
        return keywords.equals(otherPersonContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }

    /**
     * Checks if the given person’s name contains any of the specified keywords,
     * ignoring case differences.
     *
     * @param person The person whose name is to be checked.
     * @return true if the person's name contains at least one keyword, false otherwise.
     */
    public boolean matchesName(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> person.getName().fullName.toLowerCase().contains(keyword.toLowerCase()));
    }

    /**
     * Checks if the given person’s phone number contains any of the specified keywords,
     * ignoring case differences.
     *
     * @param person The person whose phone number is to be checked.
     * @return true if the person's phone number contains at least one keyword, false otherwise.
     */
    public boolean matchesPhone(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> person.getPhone().value.toLowerCase().contains(keyword.toLowerCase()));
    }

    /**
     * Checks if the given person’s email address contains any of the specified keywords,
     * ignoring case differences.
     *
     * @param person The person whose email address is to be checked.
     * @return true if the person's email address contains at least one keyword, false otherwise.
     */
    public boolean matchesEmail(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> person.getEmail().value.toLowerCase().contains(keyword.toLowerCase()));
    }

    /**
     * Checks if the given person’s address contains any of the specified keywords,
     * ignoring case differences.
     *
     * @param person The person whose address is to be checked.
     * @return true if the person's address contains at least one keyword, false otherwise.
     */
    public boolean matchesAddress(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> person.getAddress().value.toLowerCase().contains(keyword.toLowerCase()));
    }

    /**
     * Checks if any tag of the given person contains any of the specified keywords,
     * ignoring case differences.
     *
     * @param person The person whose tags are to be checked.
     * @return true if at least one tag of the person contains at least one keyword, false otherwise.
     */
    public boolean matchesTag(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> person.getTags().stream()
                        .anyMatch(tag -> tag.tagName.toLowerCase().contains(keyword.toLowerCase())));
    }
}
