package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.LessonTime;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditPersonDescriptorBuilder {
    private EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
     */
    public EditPersonDescriptorBuilder(Person person) {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(person.getName());
        descriptor.setPhone(person.getPhone());
        descriptor.setLessonTime(person.getLessonTime());
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Parses the {@code lessonTime} into a {@code Set<LessonTime>} and set it to the {@code EditPersonDescriptor}
     * that we are building.
     */
    public EditPersonDescriptorBuilder withLessonTime(String... lessonTime) {
        Set<LessonTime> lessonTimeSet = Stream.of(lessonTime).map(LessonTime::new).collect(Collectors.toSet());
        descriptor.setLessonTime(lessonTimeSet);
        return this;
    }

    /**
     * Parses the {@code lessonTime} into a {@code Set<LessonTime>} and set it to the {@code EditPersonDescriptor}
     * that we are building.
     */
    public EditPersonDescriptorBuilder withLessonTimeToAdd(String... lessonTime) {
        Set<LessonTime> lessonTimeSet = Stream.of(lessonTime).map(LessonTime::new).collect(Collectors.toSet());
        descriptor.setLessonTimesToAdd(lessonTimeSet);
        return this;
    }

    /**
     * Parses the {@code lessonTime} into a {@code Set<LessonTime>} and set it to the {@code EditPersonDescriptor}
     * that we are building.
     */
    public EditPersonDescriptorBuilder withLessonTimeToRemove(String... lessonTime) {
        Set<LessonTime> lessonTimeSet = Stream.of(lessonTime).map(LessonTime::new).collect(Collectors.toSet());
        descriptor.setLessonTimesToRemove(lessonTimeSet);
        return this;
    }

    public EditPersonDescriptor build() {
        return descriptor;
    }
}
