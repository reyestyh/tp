package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;


public class PersonMatchesFieldsPredicate implements Predicate<Person> {
    private final List<String> nameKeywords;
    private final List<String> phoneKeywords;
    private final List<String> emailKeywords;
    private final List<String> addressKeywords;
    private final List<String> tagKeywords;

    public PersonMatchesFieldsPredicate(List<String> nameKeywords,
                                        List<String > phoneKeywords,
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

    public boolean matchesName(Person person) {
        if (nameKeywords.isEmpty()) {
            return true;
        } else {
            return nameKeywords.stream()
                    .anyMatch(keyword -> person.getName().fullName.toLowerCase().contains(keyword.toLowerCase()));
        }
    }

    public boolean matchesPhone(Person person) {
        if (phoneKeywords.isEmpty()) {
            return true;
        } else {
            return phoneKeywords.stream()
                    .anyMatch(keyword -> person.getPhone().value.toLowerCase().contains(keyword.toLowerCase()));
        }

    }

    public boolean matchesEmail(Person person) {
        if (emailKeywords.isEmpty()) {
            return true;
        } else {
            return emailKeywords.stream()
                    .anyMatch(keyword -> person.getEmail().value.toLowerCase().contains(keyword.toLowerCase()));
        }
    }

    public boolean matchesAddress(Person person) {
        if (addressKeywords.isEmpty()) {
            return true;
        } else {
            return addressKeywords.stream()
                    .anyMatch(keyword -> person.getAddress().value.toLowerCase().contains(keyword.toLowerCase()));
        }
    }

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
