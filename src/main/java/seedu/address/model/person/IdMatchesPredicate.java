package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.id.Id;

/**
 * Tests that a {@code Person}'s {@code id} matches any of the ids given.
 */
public class IdMatchesPredicate implements Predicate<Person> {
    private final List<Id> ids;

    public IdMatchesPredicate(List<Id> ids) {
        this.ids = ids;
    }

    @Override
    public boolean test(Person person) {
        return ids.stream()
                .anyMatch(id -> id.equals(person.getId()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof IdMatchesPredicate)) {
            return false;
        }

        IdMatchesPredicate otherIdMatchesPredicate = (IdMatchesPredicate) other;
        return ids.equals(otherIdMatchesPredicate.ids);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("ids", ids).toString();
    }
}
