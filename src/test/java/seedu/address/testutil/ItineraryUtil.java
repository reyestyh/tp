package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_CLIENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_DESTINATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_END;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_START;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITINERARY_VENDOR;

import seedu.address.logic.commands.AddiCommand;
import seedu.address.logic.commands.EditItineraryCommand.EditItineraryDescriptor;
import seedu.address.model.itinerary.Itinerary;

/**
 * A utility class for itinerary.
 */
public class ItineraryUtil {

    /**
     * Returns an addi command string for adding the {@code itinerary}.
     */
    public static String getAddiCommand(Itinerary itinerary) {
        return AddiCommand.COMMAND_WORD + " " + getItineraryDetails(itinerary);
    }

    /**
     * Returns the part of command string for the given {@code itinerary}'s details.
     */
    public static String getItineraryDetails(Itinerary itinerary) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_ITINERARY_NAME + itinerary.getName().fullName + " ");
        sb.append(PREFIX_ITINERARY_DESTINATION + itinerary.getDestination().toString() + " ");
        sb.append(PREFIX_ITINERARY_START + itinerary.getDateRange().getStartDate().toString() + " ");
        sb.append(PREFIX_ITINERARY_END + itinerary.getDateRange().getEndDate().toString() + " ");
        sb.append(PREFIX_ITINERARY_CLIENT + itinerary.getClientIds().toString() + " ");
        sb.append(PREFIX_ITINERARY_VENDOR + itinerary.getVendorIds().toString() + " ");
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EdititineraryDescriptor}'s details.
     */
    public static String getEditItineraryDescriptorDetails(EditItineraryDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getItineraryName().ifPresent(itineraryName ->
                sb.append(PREFIX_ITINERARY_NAME).append(itineraryName.fullName).append(" "));
        descriptor.getDestination().ifPresent(destination ->
                sb.append(PREFIX_ITINERARY_DESTINATION).append(destination.toString()).append(" "));
        descriptor.getStartDate().ifPresent(startDate ->
                sb.append(PREFIX_ITINERARY_START).append(startDate.toString()).append(" "));
        descriptor.getEndDate().ifPresent(endDate ->
                sb.append(PREFIX_ITINERARY_END).append(endDate.toString()).append(" "));
        return sb.toString();
    }
}
