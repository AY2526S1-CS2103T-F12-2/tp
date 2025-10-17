package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;


/**
 * Tests whether any of the given keywords is contained in a
 * person's name, phone number, or lesson time
 */
public class StudentFieldsContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;
    /**
     * Creates a predicate that matches if any keyword is contained in a person's
     * name, phone, or lesson time
     *
     * @param keywords list of keywords;
     */
    public StudentFieldsContainsKeywordsPredicate(List<String> keywords) {
        requireNonNull(keywords);
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        requireNonNull(person);
        String name = norm(person.getName().fullName);
        String phone = norm(person.getPhone().value);

        String lesson = "";
        if (person.getLessonTime() != null) {
            lesson = norm(person.getLessonTime().toString());
        }

        for (String kw : keywords) {
            String t = norm(kw);
            if (!t.isEmpty() && (name.contains(t) || phone.contains(t) || lesson.contains(t))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StudentFieldsContainsKeywordsPredicate)) {
            return false;
        }

        StudentFieldsContainsKeywordsPredicate otherNameContainsKeywordsPredicate =
                 (StudentFieldsContainsKeywordsPredicate) other;
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    private static String norm(String s) {
        return s == null ? "" : s.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }

    /**
     * Gets the keywords used in the predicate
     *
     * @return keywords used in the predicate as a list
     */
    public List<String> getKeywords() {
        return keywords;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
