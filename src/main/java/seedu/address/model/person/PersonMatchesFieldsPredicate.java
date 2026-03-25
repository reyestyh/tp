package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests whether a {@code Person} matches the specified keywords in corresponding fields.
 * A person is considered a match if both non-empty keyword list matches its corresponding field.
 */
public class PersonMatchesFieldsPredicate implements Predicate<Person> {
    private final List<String> nameKeywords;
    private final List<String> phoneKeywords;
    private final List<String> emailKeywords;
    private final List<String> addressKeywords;
    private final List<String> tagKeywords;

    /**
     * Constructs a {@code PersonMatchesFieldsPredicate} with the specified keywords
     * for each searchable field.
     *
     * @param nameKeywords The keywords to be matched against a person's name.
     * @param phoneKeywords The keywords to be matched against a person's phone number.
     * @param emailKeywords The keywords to be matched against a person's email address.
     * @param addressKeywords The keywords to be matched against a person's address.
     * @param tagKeywords The keywords to be matched against a person's tags.
     */
    public PersonMatchesFieldsPredicate(List<String> nameKeywords,
                                        List<String> phoneKeywords,
                                        List<String> emailKeywords,
                                        List<String> addressKeywords,
                                        List<String> tagKeywords) {
        this.nameKeywords = nameKeywords;
        this.phoneKeywords = phoneKeywords;
        this.emailKeywords = emailKeywords;
        this.addressKeywords = addressKeywords;
        this.tagKeywords = tagKeywords;
    }


    @Override
    public boolean test(Person person) {
        return matchesName(person)
                && matchesPhone(person)
                && matchesEmail(person)
                && matchesAddress(person)
                && matchesTag(person);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonMatchesFieldsPredicate)) {
            return false;
        }

        PersonMatchesFieldsPredicate otherPersonMatchesFieldsPredicate = (PersonMatchesFieldsPredicate) other;
        return nameKeywords.equals(otherPersonMatchesFieldsPredicate.nameKeywords)
                && phoneKeywords.equals(otherPersonMatchesFieldsPredicate.phoneKeywords)
                && emailKeywords.equals(otherPersonMatchesFieldsPredicate.emailKeywords)
                && addressKeywords.equals(otherPersonMatchesFieldsPredicate.addressKeywords)
                && tagKeywords.equals(otherPersonMatchesFieldsPredicate.tagKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("phoneKeywords", phoneKeywords)
                .add("emailKeywords", emailKeywords)
                .add("addressKeywords", addressKeywords)
                .add("tagKeywords", tagKeywords)
                .toString();
    }

    /**
     * Checks if the given person's name contains any of the specified name keywords,
     * ignoring case differences. If there are no name keywords, this method returns true.
     *
     * @param person The person whose name is to be checked.
     * @return true if there are no name keywords, or if the person's name contains
     *         at least one keyword; false otherwise.
     */
    public boolean matchesName(Person person) {
        if (nameKeywords.isEmpty()) {
            return true;
        } else {
            return nameKeywords.stream()
                    .anyMatch(keyword -> person.getName().fullName.toLowerCase().contains(keyword.toLowerCase()));
        }
    }

    /**
     * Checks if the given person's phone number contains any of the specified phone keywords,
     * ignoring case differences. If there are no phone keywords, this method returns true.
     *
     * @param person The person whose phone number is to be checked.
     * @return true if there are no phone keywords, or if the person's phone number contains
     *         at least one keyword; false otherwise.
     */
    public boolean matchesPhone(Person person) {
        if (phoneKeywords.isEmpty()) {
            return true;
        } else {
            return phoneKeywords.stream()
                    .anyMatch(keyword -> person.getPhone().value.toLowerCase().contains(keyword.toLowerCase()));
        }

    }

    /**
     * Checks if the given person's email address contains any of the specified email keywords,
     * ignoring case differences. If there are no email keywords, this method returns true.
     *
     * @param person The person whose email address is to be checked.
     * @return true if there are no email keywords, or if the person's email address contains
     *         at least one keyword; false otherwise.
     */
    public boolean matchesEmail(Person person) {
        if (emailKeywords.isEmpty()) {
            return true;
        } else {
            return emailKeywords.stream()
                    .anyMatch(keyword -> person.getEmail().value.toLowerCase().contains(keyword.toLowerCase()));
        }
    }

    /**
     * Checks if the given person's address contains any of the specified address keywords,
     * ignoring case differences. If there are no address keywords, this method returns true.
     *
     * @param person The person whose address is to be checked.
     * @return true if there are no address keywords, or if the person's address contains
     *         at least one keyword; false otherwise.
     */
    public boolean matchesAddress(Person person) {
        if (addressKeywords.isEmpty()) {
            return true;
        } else {
            return addressKeywords.stream()
                    .anyMatch(keyword -> person.getAddress().value.toLowerCase().contains(keyword.toLowerCase()));
        }
    }

    /**
     * Checks if any tag of the given person contains any of the specified tag keywords,
     * ignoring case differences. If there are no tag keywords, this method returns true.
     *
     * @param person The person whose tags are to be checked.
     * @return true if there are no tag keywords, or if at least one tag of the person
     *         contains at least one keyword; false otherwise.
     */
    public boolean matchesTag(Person person) {
        if (tagKeywords.isEmpty()) {
            return true;
        } else {
            return tagKeywords.stream()
                    .anyMatch(keyword -> person.getTags().stream()
                            .anyMatch(tag -> tag.tagName.toLowerCase().contains(keyword.toLowerCase())));
        }
    }
}
