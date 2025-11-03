---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# ClassConnect User Guide

ClassConnect is a **desktop app for managing students' profile, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, ClassConnect can get your student management tasks done faster than traditional GUI apps.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S1-CS2103T-F12-2/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Navigate to the folder you put the file in, open the terminal and use the `java -jar classconnect.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all students.

   * `add-student n/John Doe p/98765432 t/1000 Wed` : Adds a student named `John Doe` to the student list.

   * `delete-student i/3` : Deletes the 3rd student shown in the current list.

   * `clear` : Deletes all students.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>e.g. in `add-student n/NAME`, `NAME` is a parameter which can be used as `add-student n/John Doe`.

* Items in square brackets are optional.<br>e.g. `n/NAME [t/LESSON_TIME]` can be used as `n/John Doe t/1000` or as `n/John Doe`.

* Items with `...` after them can be used multiple times but minimally once.<br>
  e.g. `t/LESSON_TIME...` can be used as `t/1000 Sun`, `t/1230 Fri t/0900 Sat` etc.

* Parameters can be in any order.<br>e.g. if the command specifies `n/NAME p/PHONE`, `p/PHONE n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.

</box>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

### Adding a student: `add-student`

Adds a student to the student list.

Format: `add-student n/NAME p/PHONE t/LESSON_TIME...`

* `NAME` should not be blank and should only **contain letters, spaces, comma, brackets, hyphens, apostrophes, slash, at sign, full stop, with a maximum length of 50 characters**. It is also case-insensitive. e.g. `john doe` is the same as `John Doe`.
* `PHONE` should only contain numbers, and should be **8 digits long starting with 8 or 9**, following Singapore's phone number format.
* `LESSON_TIME` should be in **24-hour format without a colon**, followed by a **3-letter day abbreviation**.
  e.g. `0900 Sun` for 9am Sunday, `1530 Thu` for 3:30pm Thursday. The time should be between `0000` and `2359`.

Examples:
* `add-student n/John Doe p/98765432 t/1000 Wed` Adds a student named `John Doe`, with phone number `98765432` and lesson time `10:00 am Wed`.
* `add-student t/1330 Fri p/81234567 n/Betsy Crowe t/1100 Sat` Adds a student named `Betsy Crowe`, with phone number `81234567` and lesson times `01:30 pm Fri`, `11:00 am Sat`.

### Listing all students : `list`

Shows a list of all students in the student list.

Format: `list`

### Editing a student : `edit-student`

Edits an existing student in the student list.

Format: `edit-student i/INDEX [n/NAME] [p/PHONE] [t/LESSON_TIME]...`

* Edits the student at the specified `INDEX`.
* `INDEX` refers to the index number shown in the displayed student list.
* The `INDEX` must be a **positive integer** 1, 2, 3, ...
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* If one of the lesson times is to be updated, all other unchanged lesson times must also be provided.

Examples:
*  `edit-student i/1 p/91234567` Edits the phone number of the 1st student to be `91234567`.
*  `edit-student i/2 n/Betsy Crower t/0930 Tue` Edits the name of the 2nd student to be `Betsy Crower` and lesson time to be `09:30 am Tue`.

### Locating students: `search-student`

Finds students whose fields contain any of the given keywords.

Format: `search-student k/KEYWORD [MORE_KEYWORDS...]`

* The search is case-insensitive. e.g. `marcus` will match `Marcus`
* The order of the keywords does not matter. e.g. `Marcus Ng` will match `Ng Marcus`.
* Names, phone numbers and lesson times or days can be searched.
* Partial matches within a word is supported. e.g. `Mar` will match `Marcus`.
* Students matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Marcus 9876` will return `Marcus Ng (9876 1111)`, `John Tan (9876 5432)`.
* If in a group searching will return results in the group itself
* If no results shown is found, use command `list` to go back to student list

Examples:
* `search-student k/marcus` Returns `Marcus Ng` and `Marcus Tan`.
* `search-student k/9876` Returns all students whose phone number contains `9876`.
* `search-student k/10:00` Returns all students with lesson time `10:00`.
* `search-student k/Sun` Returns all students with lessons on `Sun`

```
Found 4 students
  ```
### Deleting a student : `delete-student`

Deletes the specified student from the student list.

Format: `delete-student i/INDEX` or `delete-student k/KEYWORD [MORE_KEYWORDS...]`

You can delete a student in two ways:
1. By `INDEX`
   * Deletes the student at the specified `INDEX`.
   * `INDEX` refers to the index number shown in the displayed student list.
   * The `INDEX` must be a **positive integer** 1, 2, 3, ...
   * The list cannot be empty.

2. By `KEYWORD`
   * Deletes the student whose name, phone number or lesson time matches the given `KEYWORD`.
   * If multiple students match the keyword, the app will list all possible matches and ask you to refine your search.
   * Matching is case-insensitive and partial matches are allowed. e.g. `marc` matches `Marcus`.

Examples:
1. Deleting by `INDEX`
   * `list` followed by `delete-student i/2` Deletes the 2nd person in the student list.
   * `search-student k/Marcus` followed by `delete-student i/1` Deletes the 1st person in the results of the `search-student` command.

2. Deleting by `KEYWORD`
   * `delete-student k/marcus` Deletes the student named `marcus` if only one match is found.
   * If multiple matches are found, the app will show possible matches.<br>
   ```
   Multiple students match the given keyword(s). Please refine your search:
   1. marcus tan; Phone: 98765432; Lesson Time: 10:00 am Sat;
   2. marcus ng; Phone: 98765423; Lesson Time: 10:00 am Mon;
   ```
   * You can then refine your search by including full name, phone number or lesson time. e.g. `delete-student k/98765432`.
   * If no matches are found, the app will display `No students match the given keyword(s).`.

Notes:
* You cannot use `i/` and `k/` in the same command.
* The command is case-insensitive.

### Creating a group : `group-create`

Creates a new group.

Format: `group-create g/GROUP`

* Creates a group with the specified `GROUP` name.
* Group names are case-insensitive, trimmed, and must follow:
  - 1 to 30 characters.
  - Letters, digits, spaces, and the symbols `-` or `/` only.
  - Multiple spaces are collapsed.
* Command fails if a group with the same `GROUP` name already exists.

Examples:
* `group-create g/Group A` Creates a group called `Group A`.

Notes:
* Do not include any other prefixes besides `g/` in this command.
* Duplicate `g/` prefixes are not allowed.

### Adding students to a group : `group-add`

Adds one or more students to a group using their displayed indices.

Format: `group-add g/GROUP i/INDEX...`

* Adds the students at the specified indices to `GROUP`.
* `INDEX` refers to the index number shown in the displayed student list.
* Each `INDEX` must be a **positive integer** 1, 2, 3, ...
* You must specify at least one `INDEX` field.
* The list cannot be empty when using indices.
* Command fails if the `GROUP` does not exist.

Examples:
* `group-add g/Group A i/1 i/3` Adds the 1st and 3rd students to `Group A`.
* After `search-student k/Marcus`, `group-add g/Group A i/1` adds the 1st student from the search results to `Group A`.

Notes:
* Do not mix multiple `g/` prefixes; only one `g/` is allowed.
* Multiple `i/` prefixes are allowed only to specify multiple different indices. e.g. `i/1 i/3 i/5`.
* Repeating the same index is redundant and ignored by design.

### Removing students from a group : `group-remove`

Removes one or more students from a group using their displayed indices.

Format: `group-remove g/GROUP i/INDEX...`

* Removes the students at the specified indices from `GROUP`.
* `INDEX` refers to the index number shown in the displayed student list.
* Each `INDEX` must be a **positive integer** 1, 2, 3, ...
* You must specify at least one `INDEX` field.
* The list cannot be empty when using indices.
* Command fails if the `GROUP` does not exist.

Examples:
* `group-remove g/Group A i/2` Removes the 2nd student in the list from `Group A`.
* After `search-student k/Friday`, `group-remove g/Group A i/1 i/2` removes the 1st and 2nd students from the search results from `Group A`.

Notes:
* Do not mix multiple `g/` prefixes; only one `g/` is allowed.
* If a specified student is not in the group, the command succeeds for other valid indices and ignores that student.

### Deleting a group : `group-delete`

Deletes an existing group and its membership associations.

Format: `group-delete g/GROUP`

* Deletes the specified `GROUP`.
* All memberships associated with the group are removed.
* Command fails if the `GROUP` does not exist.

Examples:
* `group-delete g/Group A` Deletes the group named `Group A`.

Notes:
* Do not include any other prefixes besides `g/` in this command.
* When a group is deleted, students in the group are not deleted.

### Filtering by group (UI)

Use the **Filter by group** bar (top of the window) to show only students in a specific group.  
The dropdown always includes **(All students)** plus **every group you have created**.

**Where to find it:**  
At the top toolbar: `Filter by group: [ (All students) ▾ ]`

**Mouse usage**
1. Click the dropdown.
2. Choose a group to filter the list.
3. Choose **(All students)** to clear the filter.

**Keyboard usage (no mouse)**
1. Press `Tab` until the **Filter by group** box is selected (you’ll see a focus ring).
2. Press `↓` to open the dropdown.
3. Use `↑/↓` to move between items; press `Enter` to select.
4. Press `Shift+Tab` to return to the command box.

**Tips**
- The dropdown is the quickest way to **see all groups currently available**. If a name appears here, that group already exists.
- Each student card shows small **group badge(s)** next to the name, so you can visually confirm membership while filtered.
- Deleting a group immediately removes it from this dropdown; creating a group adds it.

**Notes**
- If no groups exist yet, the dropdown only shows **(All students)**.
- Filtering affects only the **visible list**; it does not change your data.

### Viewing all groups (at a glance)

There is no separate `group-list` command. Use either of these:
- **Dropdown method (recommended):** Open the **Filter by group** dropdown — it lists **every existing group**.
- **On cards:** Scan the **group badges** shown beside each student’s name (useful when already filtered).

### Marking paid status : `mark-paid`

Marks a student's payment status for a specific month as paid.

Format: `mark-paid i/INDEX m/MONTH`

* Marks the student at the specified `INDEX` as paid for the specified `MONTH`.
* `INDEX` refers to the position of the student in the displayed student list.
* The `INDEX` must be a **positive integer** 1, 2, 3, ...
* `MONTH` must be an integer from 1 to 12, representing each month from January to December.
* If the given student has already been marked as paid for the given month, the command is rejected and displays:<br>
`Student marcus ng is already marked as paid for January.`.
* Payment status is displayed as 12 colored boxes (🟩 for paid, 🟥 for unpaid) representing the 12 months January to December.

Examples:
* `mark-paid i/1 m/1` Marks the 1st student in the list as paid for the month January.
```
Marked student as paid: marcus ng
Month: January
Payment Status: 🟩 🟥 🟥 🟥 🟥 🟥 🟥 🟥 🟥 🟥 🟥 🟥
  ```

### Marking unpaid status : `mark-unpaid`

Marks a student's payment status for a specific month as unpaid.

Format: `mark-upaid i/INDEX m/MONTH`

* Marks the student at the specified `INDEX` as unpaid for the specified `MONTH`.
* `INDEX` refers to the position of the student in the displayed student list.
* The `INDEX` must be a **positive integer** 1, 2, 3, ...
* `MONTH` must be an integer from 1 to 12, representing each month from January to December.
* If the given student has already paid for the given month, the command is rejected and displays:<br>
  `Student marcus ng is already marked as unpaid for January.`.
* Payment status is displayed as 12 colored boxes (🟩 for paid, 🟥 for unpaid) representing the 12 months January to December.

Examples:
* `mark-upaid i/1 m/1` Marks the 1st student in the list as unpaid for January.
```
Marked student as unpaid: marcus ng
Month: January
Payment Status: 🟥 🟥 🟥 🟥 🟥 🟥 🟥 🟥 🟥 🟥 🟥 🟥
```

### Adding homework: `add-homework`

Adds a homework entry to the specified student.

Format: `add-homework n/NAME desc/DESCRIPTION by/DEADLINE`

* Adds a homework with the given `DESCRIPTION` and `DEADLINE` to the student with the specified `NAME`.
* `NAME` refers to the name of the student in the displayed student list. It must match the full name of the student in the list.
* `DESCRIPTION` refers to the details of the homework.
* `DEADLINE` refers to the due date of the homework. It is in the `YYYY-MM-DD` format.

Examples:
* `add-homework n/Marcus desc/Math Worksheet 1 by/2025-10-27` Assigns the homework `Math Worksheet 1` and its due date `2025-10-27` to the student `Marcus`.
```
Added homework for Marcus: Math Worksheet 1 (due 2025-10-27)
  ```

### Deleting homework: `delete-homework`

Deletes a homework entry of the specified student.

Format: `delete-homework n/NAME i/INDEX`

* Deletes the homework with the given `INDEX` from the student with the specified `NAME`.
* `NAME` refers to the name of the student in the displayed student list. It must match the full name of the student in the list.
* `INDEX` refers to the number shown beside the homework.

Examples:
* `delete-homework n/Marcus i/1` Deletes the homework at index 1 that is assigned to the student `Marcus`.
```
Deleted homework for Marcus: Math Worksheet 3
  ```

### Marking homework as done: `mark-done`

Marks a homework entry of the specified student as done.

Format: `mark-done n/NAME i/INDEX`

* Marks the homework with the given `INDEX` from the student with the specified `NAME` as done.
* `NAME` refers to the name of the student in the displayed student list. It must match the full name of the student in the list.
* `INDEX` refers to the number shown beside the homework.
* Marking a homework that is already marked will not result in any error

Examples:
* `mark-done n/Marcus i/1` Marks the homework at index 1 that is assigned to the student `Marcus` as done.
```
Marked homework as done for Marcus: Science Worksheet 2
  ```

### Marking homework as undone: `mark-undone`

Marks a homework entry of the specified student as undone.

Format: `mark-undone n/NAME i/INDEX`

* Marks the homework with the given `INDEX` from the student with the specified `NAME` as undone.
* `NAME` refers to the name of the student in the displayed student list. It must match the full name of the student in the list.
* `INDEX` refers to the number shown beside the homework.
* Unmarking a homework that is already unmarked will not result in any error

Examples:
* `mark-undone n/Marcus i/1` Marks the homework at index 1 that is assigned to the student `Marcus` as undone.
```
Marked homework as undone for Marcus: Science Worksheet 2
  ```
### Recording participation: `participation`

Records a student's participation score for a specific class date.

Format: `participation n/NAME d/DATE s/SCORE`

* Records the participation score `SCORE` for the student with the specified `NAME` on the given class date `DATE`.
* `NAME` refers to the student’s full name. It is case-insensitive.
* `DATE` refers to the class date in `YYYY-MM-DD` format. e.g. `2025-09-19`.
* `SCORE` refers to the participation score for the student. It is an integer from 0 to 5.

Examples:
* `participation n/Alex Yeoh d/2025-09-19 s/3` Records a participation score of `3` for `Alex Yeoh` on `2025-09-19`.

Notes:
* `NAME` are matched ignoring extra spaces and letter case. e.g., `alex  yeoh` matches `Alex Yeoh`.
* If the `SCORE` is not a number, you’ll see: `Invalid participation score. Use an integer 0 to 5.`.
* If the `SCORE` is outside the range, you’ll see: `Invalid participation score. Must be between 0 and 5 inclusive.`
* The history shows **up to five most recent** entries.

**What you’ll see in the UI:**
- The score is shown on the right side of each person card which has **two rows**:
    - **Top row:** Dates (formatted `MM-dd`) for up to the last five classes, oldest → newest.
    - **Bottom row:** Five boxes showing the corresponding participation scores.
- When you record a new score, the date and score shift left as the history grows; the newest class is the **rightmost** box.

### Adding reminder: `add-reminder`

Adds a reminder to the reminder list.

Format: `add-reminder d/DUE_DATE desc/DESCRIPTION`

* Adds a reminder with the given `DUE_DATE` and `DESCRIPTION` to the reminder list.
* `DUE_DATE` refers to the due date of the reminder. It could either be in the `YYYY-MM-DD HHMM` format or `YYYY-MM-DD` format. e.g. `2025-10-27 1400` or `2025-10-27`.
* `DESCRIPTION` refers to the details of the reminder.

Examples:
* `add-reminder d/2025-10-27 1400 desc/Tuition later at 3pm` Adds a reminder with due date `10 Oct 2025 10:10 am` and description `Tuition later at 3pm`.
* `add-reminder d/2025-10-27 desc/Submit assignment` Adds a reminder with due date `27 Oct 2025` and description `Submit assignment`.

### Editing reminder: `edit-reminder`

Edits an existing reminder in the reminder list.

Format: `edit-reminder i/INDEX [d/DUE_DATE] [desc/DESCRIPTION]`

* Edits the reminder at the specified `INDEX`.
* `INDEX` refers to the index number shown in the displayed reminder list.
* The `INDEX` must be a **positive integer** 1, 2, 3, ...
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.

Examples:
*  `edit-reminder i/1 desc/Pay tuition fees` Edits the description of the 1st reminder to be `Pay tuition fees`.
*  `edit-reminder i/2 desc/2025-11-01 1500` Edits the due date of the 2nd reminder to be `01 Nov 2025 03:00 pm`.

### Deleting reminder: `delete-reminder`

Deletes a reminder from the reminder list.

Format: `delete-reminder i/INDEX` or `delete-reminder k/KEYWORD [MORE_KEYWORDS...]`

You can delete a reminder in two ways:
1. By `INDEX`
   * Deletes the reminder at the specified `INDEX`.
   * `INDEX` refers to the index number shown in the displayed reminder list.
   * The `INDEX` must be a **positive integer** 1, 2, 3, ...
   * The list cannot be empty.

2. By `KEYWORD`
   * Deletes the reminder whose description or due date matches the given `KEYWORD`.
   * If multiple reminders match the keyword, the app will list all possible matches and ask you to refine your search.
   * Matching is case-insensitive and partial matches are allowed. e.g. `assign` matches `assignment`.

Examples:
1. Deleting by `INDEX`
   * `list` followed by `delete-reminder i/2` deletes the 2nd reminder in the reminder list.
   * `search-reminder k/assignment` followed by `delete-reminder i/1` deletes the 1st reminder in the results of the `search-reminder` command.

2. Deleting by `KEYWORD`
   * `delete-reminder k/feedback` Deletes the reminder with description containing `feedback` if only one match is found.
   * If multiple matches are found, the app will show possible matches.<br>
   ```
   Multiple reminders match the given keyword(s). Please refine your search:
   1. Due: 01 Nov 2025; Description: Feedback session for Alice;
   2. Due: 03 Nov 2025 02:00 pm; Description: Feedback session for Benson;
    ```
    * You can then refine your search by including the full description. e.g. `delete-reminder k/Feedback session for Alice`.
    * If no matches are found, the app will display `No reminders match the given keyword(s).`.

Notes:
* You cannot use `i/` and `k/` in the same command.
* The command is case-insensitive.

### Clearing all entries : `clear`

Clears all entries from the student list and reminder list.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

ClassConnect data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

ClassConnect data are saved automatically as a JSON file `[JAR file location]/data/classconnect.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, ClassConnect will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the ClassConnect to behave in unexpected ways (e.g. if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.

</box>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous ClassConnect home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action     | Format, Examples
-----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------
**Add Homework**    | `add-homework n/NAME desc/DESCRIPTION by/DEADLINE` <br> e.g., `add-homework n/Marcus desc/Math Worksheet 1 by/2025-10-27`
**Add Reminder**    | `add-reminder d/DATETIME desc/DESCRIPTION` <br> e.g., `add-reminder d/2025-10-12 1500 desc/Submit assignment`
**Add Student**    | `add-student n/NAME p/PHONE_NUMBER t/LESSON_TIME...` <br> e.g., `add-student n/James Ho p/98765432 t/1000 Mon t/1400 Wed`
**Add Student to Group**   | `group-add g/GROUP i/INDEX...` <br> e.g., `group-add g/Group A i/1 i/3`
**Clear**  | `clear`
**Create Group**   | `group-create g/GROUP` <br> e.g., `group-create g/Group A`
**Delete Group**   | `group-delete g/GROUP` <br> e.g., `group-delete g/Group A`
**Delete Homework**    | `delete-homework n/NAME i/INDEX` <br> e.g., `delete-homework n/Marcus i/1`
**Delete Reminder** | `delete-reminder i/INDEX` **or** `delete-reminder k/KEYWORD [MORE_KEYWORDS...]`<br> e.g., `delete-reminder i/3` **or** `delete-reminder k/assignment`
**Delete Student** | `delete-student i/INDEX` **or** `delete-student k/KEYWORD [MORE_KEYWORDS...]`<br> e.g., `delete-student i/3` **or** `delete-student k/marcus lee`
**Edit Reminder**   | `edit-reminder i/INDEX [d/DATETIME] [desc/DESCRIPTION]`<br> e.g.,`edit-reminder i/2 d/2025-11-01 1500 desc/Pay tuition fees`
**Edit Student**   | `edit-student i/INDEX [n/NAME] [p/PHONE_NUMBER] [t/LESSON_TIME]...`<br> e.g.,`edit-student i/2 n/James Lee t/1830 Fri t/1000 Sun`
**Exit**   | `exit`
**Help**   | `help`
**List**   | `list`
**Mark Homework as Done**    | `mark-done n/NAME i/INDEX` <br> e.g., `mark-done n/Marcus i/1`
**Mark Homework as Undone**    | `mark-undone n/NAME i/INDEX` <br> e.g., `mark-undone n/Marcus i/1`
**Mark as paid**   | `mark-paid i/INDEX m/MONTH` <br> e.g., `mark-paid i/1 m/1`
**Mark as unpaid**   | `mark-unpaid i/INDEX m/MONTH` <br> e.g., `mark-unpaid i/1 m/1`
**Participation**    | `participation n/NAME d/DATE s/SCORE` <br> e.g., `participation n/James Ho d/2025-09-19 s/1`
**Remove Student from Group**   | `group-remove g/GROUP i/INDEX...` <br> e.g., `group-remove g/Group A i/2`
**Search Student**   | `search-student k/KEYWORD [MORE_KEYWORDS...]` <br> e.g., `search-student k/marcus lee`
