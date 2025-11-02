---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# ClassConnect User Guide

ClassConnect is a desktop app designed for private tutors to **easily manage their students, lessons, and administrative tasks** — from lesson scheduling and homework tracking to payments and reminders — all in one place.

While ClassConnect is optimized for **fast command-based input (CLI)**, it also offers the convenience of a **clean and intuitive Graphical User Interface (GUI)**. This means you get the speed and precision of typing commands — perfect for tutors who want to make quick updates during or after lessons — without losing the visual clarity that helps you see all your students, schedules, and reminders at a glance.

ClassConnect helps you:

* **Maintain organized student profiles** including names, phone number, and lesson times.

* **Track homework progress, participation and payment status** with visual indicators.

* **Group students** for easier class and schedule management.

* **Set reminders** for important events, upcoming lessons, or follow-ups.

* Automatically **save your data**, ensuring your records are always up to date.

With ClassConnect, you can focus more on teaching and less on administrative work — **making student management simpler, faster, and more reliable.

<div style="page-break-after: always;"></div>

--------------------------------------------------------------------------------------------------------------------

## Table of Contents
<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

Follow the steps below to set up and start using ClassConnect:

1. **Check your Java version**<br>
   * Ensure you have Java `17` installed in your Computer. You can verify this by running the following command in your terminal or command prompt:`java -version`.
   * **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).
   * If you do not have Java installed, download it from the [official Oracle website](https://www.oracle.com/java/technologies/downloads/#java17?er=221886).

2. **Download ClassConnect**<br>
   * Download the latest `classconnect.jar` file from [the releases page](https://github.com/AY2526S1-CS2103T-F12-2/tp/releases).

<div style="page-break-after: always;"></div>

--------------------------------------------------------------------------------------------------------------------

3. **Run the application**
   * Choose a folder on your computer where you want to keep ClassConnect. This can be any folder you are comfortable with, such as `Documents/ClassConnect`.
   * Copy the `classconnect.jar` file you downloaded into that folder.
   * Open the terminal / command prompt:
     * Windows: Press `Win + R`, type `cmd`, and press Enter.
     * Mac: Open `Terminal` from Applications → Utilities.
     * Linux: Open your terminal application.
   * Navigate to the folder where you saved `classconnect.jar`.
     * For example, if you saved it in Documents/ClassConnect, type `cd Documents/ClassConnect` in the terminal and press `Enter`.
   * Run the application by typing `java -jar classconnect.jar` into the terminal and press `Enter`.

<box type="important" seamless>

**Note:** You may see warnings in your terminal or command prompt, similar to:
```
Nov 02, 2025 2:31:35 AM javafx.fxml.FXMLLoader$ValueElement processValue
WARNING: Loading FXML document with JavaFX API of version 17.0.12 by JavaFX runtime of version 17.0.7
```
This is safe to ignore. It occurs because ClassConnect's JavaFX version differs slightly from the runtime version. Functionality is **not affected**, and you can safely continue using the app as per normal.

</box>

4. **Familiarize yourself with the interface**
   * When ClassConnect launches, you will see both the command line panel (for typing commands) and the main display panel (showing your students, lessons, and reminders).
   ![Ui](images/Ui.png)

5. **Start managing your data**
   * Try entering your first command to add a student: `add-student n/John Doe p/98765432 t/1000 Wed`.
   * The student `John Doe` will appear at the bottom of the main panel immediately after the command is executed.

6. **Explore more commands**
   * Refer to the [Features](#features) section for a full list of commands and usage examples.

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

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

* `NAME` should not be blank and should only contain letters, spaces, comma, brackets, hyphens, apostrophes, slash, at sign, full stop, with a maximum length of 50 characters. It is also case-insensitive. e.g. `john doe` is the same as `John Doe`.
* `PHONE` should only contain numbers, and should be 8 digits long starting with 8 or 9, following Singapore's phone number format.
* `LESSON_TIME` should be in 24-hour format without a colon, followed by a 3-letter day abbreviation.
  e.g. `0900 Sun` for 9am Sunday, `1530 Thu` for 3:30pm Thursday. The time should be between `0000` and `2359`.

Examples:
* `add-student t/1330 Fri p/81234567 n/Betsy Crowe t/1100 Sat` Adds a student named `Betsy Crowe`, with phone number `81234567` and lesson times `01:30 pm Fri`, `11:00 am Sat`.
```
Student added: Betsy Crowe; Phone Number: 81234567; Lesson Time: 01:30 pm Fri, 11:00 am Sat;
```

### Listing all students : `list`

Shows a list of all students in the student list.

Format: `list`

<div style="page-break-after: always;"></div>

--------------------------------------------------------------------------------------------------------------------

### Editing a student : `edit-student`

Edits an existing student in the student list.

Format: `edit-student i/INDEX [n/NAME] [p/PHONE] [t/LESSON_TIME...]` or `edit-student i/INDEX [n/NAME] [p/PHONE] [t+/LESSON_TIME...] [t-/LESSON_TIME...]`

<box type="warning" seamless>

**Note:**<br>

* Using the prefix `t/` means the set of input lesson time will replacee all existing set of lesson time for that student.

* Using the prefix `t+/` adds the set of input lesson time onto the existing set of lesson time for that student.

* Using the prefix `t-/` removes the set of input lesson time from the existing set of lesson time for that student.

* Prefix `t/` cannot be used together with prefix `t+/` and/or `t-/`, but prefix `t+/` and `t-/` can be used together in a single command.

</box>

* Edits the student at the specified `INDEX`.
* `INDEX` refers to the index number shown in the displayed student list.
* The `INDEX` must be a positive integer 1, 2, 3, ...
* At least one of the optional fields must be provided.

<box type="warning" seamless>

**Note:** Existing values will be updated with/to the input values depending on the type of prefix used.

</box>

Examples:
*  `edit-student i/2 n/Betsy Crower t/0930 Tue` Edits the name of the 2nd student to be `Betsy Crower` and lesson time to be `09:30 am Tue`.
```
Edited Student: Betsy Crower; Phone Number: 98234492; Lesson Time: 09:30 am Tue;
```

Note:
* If input lesson time to be removed does not exist in the existing set of lesson time, the command ignores that lesson time and removes other lesson time that exists. User will be informed of those unmatched lesson time.
```
Could not find lesson time: 12:30 pm Sat;
Edited Student: Amy Bee; Phone Number: 85355255; Lesson Time: 10:00 am Sat;
```

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

<div style="page-break-after: always;"></div>

--------------------------------------------------------------------------------------------------------------------

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
   * The `INDEX` must be a positive integer 1, 2, 3, ...
   * The list cannot be empty.

2. By `KEYWORD`
   * Deletes the student whose name or phone number matches the given `KEYWORD`.
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

<div style="page-break-after: always;"></div>

--------------------------------------------------------------------------------------------------------------------

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
* Each `INDEX` must be a positive integer 1, 2, 3, ...
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
* Each `INDEX` must be a positive integer 1, 2, 3, ...
* You must specify at least one `INDEX` field.
* The list cannot be empty when using indices.
* Command fails if the `GROUP` does not exist.

<div style="page-break-after: always;"></div>

--------------------------------------------------------------------------------------------------------------------

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

### Marking paid status : `mark-paid`

Marks a student's payment status for a specific month as paid.

Format: `mark-paid i/INDEX m/MONTH`

* Marks the student at the specified `INDEX` as paid for the specified `MONTH`.
* `INDEX` refers to the position of the student in the displayed student list.
* The `INDEX` must be a positive integer 1, 2, 3, ...
* `MONTH` must be an integer from 1 to 12, representing each month from January to December.
* If the given student has already been marked as paid for the given month, the command is rejected and displays:<br>
`Student marcus ng is already marked as paid for January.`.
* Payment status is displayed as 12 colored boxes (🟩 for paid, 🟥 for unpaid) representing the 12 months January to December.

Examples:
* `mark-paid i/1 m/1` Marks the 1st student in the list as paid for the month January.
```
Marked student as paid: marcus ng
Month: January
Payment Status: 
Jan: ✓ Paid Feb: ✗ Paid Mar: ✗ Unpaid Apr: ✗ Unpaid May: ✗ Unpaid Jun: ✗ Unpaid
Jul: ✗ Unpaid Aug: ✗ Unpaid Sep: ✗ Unpaid Oct: ✗ Unpaid Nov: ✗ Unpaid Dec: ✗ Unpaid
```

<div style="page-break-after: always;"></div>

--------------------------------------------------------------------------------------------------------------------

### Marking unpaid status : `mark-unpaid`

Marks a student's payment status for a specific month as unpaid.

Format: `mark-unpaid i/INDEX m/MONTH`

* Marks the student at the specified `INDEX` as unpaid for the specified `MONTH`.
* `INDEX` refers to the position of the student in the displayed student list.
* The `INDEX` must be a positive integer 1, 2, 3, ...
* `MONTH` must be an integer from 1 to 12, representing each month from January to December.
* If the given student has already been marked as unpaid for the given month, the command is rejected and displays:<br>
  `Student marcus ng is already marked as unpaid for January.`.
* Payment status is displayed as 12 colored boxes (🟩 for paid, 🟥 for unpaid) representing the 12 months January to December.

Examples:
* `mark-unpaid i/1 m/1` Marks the 1st student in the list as unpaid for January.
```
Marked student as unpaid: marcus ng
Month: January
Payment Status: 
Jan: ✗ Unpaid Feb: ✓ Paid Mar: ✓ Paid Apr: ✓ Paid May: ✓ Paid Jun: ✓ Paid
Jul: ✓ Paid Aug: ✓ Paid Sep: ✓ Paid Oct: ✓ Paid Nov: ✓ Paid Dec: ✓ Paid
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

<div style="page-break-after: always;"></div>

--------------------------------------------------------------------------------------------------------------------

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

Records a student's participation score for a specific class date and updates the history shown on the student card.

Format: `participation n/NAME d/DATE s/SCORE`

* `NAME` is case-insensitive and matched even if you have extra spaces. e.g. `alex  yeoh` matches `Alex Yeoh`.
* `DATE` must be in `YYYY-MM-DD` format. e.g. `2025-09-19`.
* `SCORE` must be an integer from **0** to **5**.

Examples:
* `participation n/Alex Yeoh d/2025-09-19 s/3` Records a participation score of `3` for `Alex Yeoh` on `2025-09-19`.

Notes:
* If `SCORE` is not a number, you’ll see: `Invalid participation score. Use an integer 0 to 5.`
* If `SCORE` is outside the range, you’ll see: `Invalid participation score. Must be between 0 and 5 inclusive.`
* The history shows **up to five most recent** classes. The newest is on the **right**.
* If you record participation **multiple times for the same date**, the **latest score** for that date is shown (older one is effectively replaced in the view).
* Your data is saved automatically; the history will still be there after restarting the app.

<div style="page-break-after: always;"></div>

--------------------------------------------------------------------------------------------------------------------

**What you’ll see in the UI:**
- On each person card (right side), there are **two rows**:
  - **Top row:** Dates (`MM-dd`) for up to the last five classes, oldest → newest.
  - **Bottom row:** Five boxes showing the corresponding participation scores.
- When you add a new score, the timeline shifts **left** and the newest score appears at the **rightmost** box.

### Adding reminder: `add-reminder`

Adds a reminder to the reminder list.

Format: `add-reminder d/DUE_DATE desc/DESCRIPTION`

* Adds a reminder with the given `DUE_DATE` and `DESCRIPTION` to the reminder list.
* `DUE_DATE` refers to the due date of the reminder. It could either be in the `YYYY-MM-DD HHMM` format or `YYYY-MM-DD` format. e.g. `2025-10-27 1400` or `2025-10-27`.
* It should be set for a future date or time.
* `DESCRIPTION` refers to the details of the reminder. It should not be empty and must contain at least one letter or number. It must not exceed 200 characters.

Examples:
* `add-reminder d/2025-10-27 1500 desc/Tuition later at 3pm` Adds a reminder with due date `27 Oct 2025 03:00 pm` and description `Tuition later at 3pm`.
```
Reminder added. Due: 27 Oct 2025 03:00 pm; Description: Tuition later at 3pm;
```

Note:
* If a reminder is due in 3 days time, a `Due Soon!` text in red will be shown beside the due date of the reminder.
* If a reminder that was previously added has past the due date, the reminder will be greyed out.

### Editing reminder: `edit-reminder`

Edits an existing reminder in the reminder list.

Format: `edit-reminder i/INDEX [d/DUE_DATE] [desc/DESCRIPTION]`

* Edits the reminder at the specified `INDEX`.
* `INDEX` refers to the index number shown in the displayed reminder list.
* The `INDEX` must be a positive integer 1, 2, 3, ...
* At least one of the optional fields must be provided.

<box type="warning" seamless>

**Note:** Existing values will be updated to the input values.

</box>

Examples:
*  `edit-reminder i/2 d/2025-11-01 1500` Edits the due date of the 2nd reminder to be `01 Nov 2025 03:00 pm`.
```
Edited Reminder: Due: 01 Nov 2025 03:00 pm; Description: Tuition later at 3pm;
```

<box type="important" seamless>

**Take Note:**<br>

* Reminders, pertaining to payment status and undone homework that is due the next day, are auto generated by the system which cannot be modified manually via this command.

* They would only be removed if payment is mark as paid or homework is mark as done or homework is past the deadline.

</box>

<div style="page-break-after: always;"></div>

--------------------------------------------------------------------------------------------------------------------

### Deleting reminder: `delete-reminder`

Deletes a reminder from the reminder list.

Format: `delete-reminder i/INDEX` or `delete-reminder k/KEYWORD [MORE_KEYWORDS...]`

You can delete a reminder in two ways:
1. By `INDEX`
   * Deletes the reminder at the specified `INDEX`.
   * `INDEX` refers to the index number shown in the displayed reminder list.
   * The `INDEX` must be a positive integer 1, 2, 3, ...
   * The list cannot be empty.

2. By `KEYWORD`
   * Deletes the reminder whose description or due date matches the given `KEYWORD`.
   * If multiple reminders match the keyword, the app will list all possible matches and ask you to refine your search.
   * Matching is case-insensitive and partial matches are allowed. e.g. `assign` matches `assignment`.

Examples:
1. Deleting by `INDEX`
   * `list` followed by `delete-reminder i/2` deletes the 2nd reminder in the reminder list.
   * `search-reminder k/assignment` followed by `delete-reminder i/1` deletes the 1st reminder in the results of the `search-reminder` command.
    ```
    Reminder Deleted: Due: 01 Nov 2025 03:00 pm; Description: Prepare test assignment;
    ```

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

<box type="important" seamless>

**Take Note:**<br>

* Reminders, pertaining to payment status and undone homework that is due the next day, are auto generated by the system which cannot be modified manually via this command.

* They would only be removed if payment is mark as paid or homework is mark as done or homework is past the deadline.

</box>

### Clearing all entries : `clear`

Clears all entries from the student list and reminder list.

Format: `clear`

<div style="page-break-after: always;"></div>

--------------------------------------------------------------------------------------------------------------------

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
**Add Reminder**    | `add-reminder d/DATETIME desc/DESCRIPTION` <br> e.g., `add-reminder d/2025-10-27 1400 desc/Submit assignment`
**Add Student**    | `add-student n/NAME p/PHONE t/LESSON_TIME...` <br> e.g., `add-student n/James Ho p/98765432 t/1000 Mon t/1400 Wed`
**Add Student to Group**   | `group-add g/GROUP i/INDEX...` <br> e.g., `group-add g/Group A i/1 i/3`
**Clear**  | `clear`
**Create Group**   | `group-create g/GROUP` <br> e.g., `group-create g/Group A`
**Delete Group**   | `group-delete g/GROUP` <br> e.g., `group-delete g/Group A`
**Delete Homework**    | `delete-homework n/NAME i/INDEX` <br> e.g., `delete-homework n/Marcus i/1`
**Delete Reminder** | `delete-reminder i/INDEX` **or** `delete-reminder k/KEYWORD [MORE_KEYWORDS...]`<br> e.g., `delete-reminder i/3` **or** `delete-reminder k/assignment`
**Delete Student** | `delete-student i/INDEX` **or** `delete-student k/KEYWORD [MORE_KEYWORDS...]`<br> e.g., `delete-student i/3` **or** `delete-student k/marcus lee`
**Edit Reminder**   | `edit-reminder i/INDEX [d/DATETIME] [desc/DESCRIPTION]`<br> e.g.,`edit-reminder i/2 d/2025-11-01 1500 desc/Pay tuition fees`
**Edit Student**   | `edit-student i/INDEX [n/NAME] [p/PHONE] [t/LESSON_TIME...]` **or** `edit-student i/INDEX [n/NAME] [p/PHONE] [t+/LESSON_TIME...] [t-/LESSON_TIME...]`<br> e.g.,`edit-student i/2 n/James Lee t/1830 Fri t/1000 Sun` **or** `edit-student i/INDEX t+/1530 Fri t-/1830 Fri`
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
