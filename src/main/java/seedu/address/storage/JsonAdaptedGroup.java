package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;

/**
 * Jackson-friendly version of {@code Group}.
 * Members are stored by student name (String) and resolved on load.
 */
public class JsonAdaptedGroup {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Group's %s field is missing!";

    private final String name;
    private final List<String> members;

    /** Create from JSON properties. */
    @JsonCreator
    public JsonAdaptedGroup(@JsonProperty("name") String name,
                            @JsonProperty("members") List<String> members) {
        this.name = name;
        this.members = members != null ? members : new ArrayList<>();
    }

    /** Create from model group (serialize). */
    public JsonAdaptedGroup(Group source) {
        this.name = source.getName().toString();
        this.members = source.getMembers().stream()
                .map(p -> p.getName().fullName)
                .collect(Collectors.toList());
    }

    /** Group name as string (for later resolution). */
    public String getName() {
        return name;
    }

    /** Member names as strings (for later resolution). */
    public List<String> getMemberNames() {
        return members;
    }

    /**
     * Convert to a model Group (name only; members are linked later after persons are loaded).
     */
    public Group toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "name"));
        }
        return new Group(GroupName.of(name));
    }
}
