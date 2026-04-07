package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's role in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidRole(String)}
 */
public class Role {
    /**
     * Represents the possible roles a Person can have.
     */
    public enum RoleType {
        CLIENT,
        VENDOR
    }

    public static final String DEFAULT_ROLE = "client";
    public static final String MESSAGE_CONSTRAINTS =
            "Invalid role: Please use either 'client' or 'vendor'.";

    public static final String VALIDATION_REGEX = "(?i)client|vendor";

    public final RoleType value;

    /**
     * Constructs an {@code Role}.
     *
     * @param role A valid role.
     */
    public Role(String role) {
        requireNonNull(role);
        checkArgument(isValidRole(role), MESSAGE_CONSTRAINTS);
        value = RoleType.valueOf(role.trim().toUpperCase());
    }

    /**
     * Returns true if a given string is a valid role.
     */
    public static boolean isValidRole(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public RoleType getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.name().toLowerCase();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Role)) {
            return false;
        }

        Role otherRole = (Role) other;
        return value.equals(otherRole.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
