package seedu.address.testutil;

import java.time.LocalDate;

import seedu.address.logic.commands.EditCommand.EditItineraryDescriptor;
import seedu.address.model.itinerary.Destination;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.itinerary.ItineraryName;

/**
 * A utility class to help with building EditItineraryDescriptor objects.
 */
public class EditItineraryDescriptorBuilder {

    private EditItineraryDescriptor descriptor;

    public EditItineraryDescriptorBuilder() {
        descriptor = new EditItineraryDescriptor();
    }

    public EditItineraryDescriptorBuilder(EditItineraryDescriptor descriptor) {
        this.descriptor = new EditItineraryDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditItineraryDescriptor} with fields containing {@code Itinerary}'s details
     */
    public EditItineraryDescriptorBuilder(Itinerary itinerary) {
        descriptor = new EditItineraryDescriptor();
        descriptor.setItineraryName(itinerary.getName());
        descriptor.setDestination(itinerary.getDestination());
        descriptor.setStartDate(itinerary.getDateRange().getStartDate());
        descriptor.setEndDate(itinerary.getDateRange().getEndDate());
    }

    /**
     * Sets the {@code ItineraryName} of the {@code EditItineraryDescriptor} that we are building.
     */
    public EditItineraryDescriptorBuilder withName(String name) {
        descriptor.setItineraryName(new ItineraryName(name));
        return this;
    }

    /**
     * Sets the {@code Destination} of the {@code EditItineraryDescriptor} that we are building.
     */
    public EditItineraryDescriptorBuilder withDestination(String destination) {
        descriptor.setDestination(new Destination(destination));
        return this;
    }

    /**
     * Sets the {@code StartDate} of the {@code EditItineraryDescriptor} that we are building.
     */
    public EditItineraryDescriptorBuilder withStartDate(LocalDate startDate) {
        descriptor.setStartDate(startDate);
        return this;
    }

    /**
     * Sets the {@code EndDate} of the {@code EditItineraryDescriptor} that we are building.
     */
    public EditItineraryDescriptorBuilder withEndDate(LocalDate endDate) {
        descriptor.setEndDate(endDate);
        return this;
    }

    public EditItineraryDescriptor build() {
        return descriptor;
    }
}
