package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {


    public static final String MESSAGE_CONSTRAINTS =
            "Invalid phone number format. Please use the format: (+<Country Code>) <Phone Number> "
                    + "(e.g., (+65) 98765432).";
    public static final String VALIDATION_REGEX = "\\(\\+\\d{1,3}\\)\\d{3,12}";
    public final String value;

    /**
     * Constructs a {@code Phone}.
     *
     * @param phone A valid phone number.
     */
    public Phone(String phone) {
        requireNonNull(phone);
        String normalized = normalizePhone(phone);
        checkArgument(isValidPhone(normalized), MESSAGE_CONSTRAINTS);
        value = normalized;
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPhone(String test) {
        requireNonNull(test);
        return normalizePhone(test).matches(VALIDATION_REGEX);
    }

    /**
     * Removes whitespace so UI input and JSON storage are validated consistently.
     */
    private static String normalizePhone(String phone) {
        return phone.replaceAll("\\s+", "");
    }

    @Override
    public String toString() {
        int closeBracketIndex = value.indexOf(')');
        if (closeBracketIndex == -1) {
            return value;
        }
        return value.substring(0, closeBracketIndex + 1) + " "
                + value.substring(closeBracketIndex + 1);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Phone)) {
            return false;
        }

        Phone otherPhone = (Phone) other;
        return value.equals(otherPhone.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
