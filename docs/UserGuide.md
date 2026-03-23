---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# TripScribe User Guide

TripScribe allows you to manage contacts and itineraries on your desktop using keyboard commands with an informative Graphical User Interface (GUI). If you type fast, you can complete your contact and itinerary management tasks faster with TripScribe than traditional mouse-based apps.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

2. Download the latest `.jar` file from [here](https://github.com/AY2526S2-CS2103T-F12-1/tp/releases).

3. Copy the file to the folder you want to use as the folder for TripScribe.

4. Open a command terminal, and navigate into the folder you put the jar file in. 

5. Type the command `java -jar tripscribe.jar` to run the application.<br>
   A pop-up window similar to the below should appear in a few seconds. On first start, the app has some sample data.<br>
   ![Ui](images/Ui.png)

6. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list /contact` : Lists all contacts.

   * `addc r/client n/John Doe p/(+65) 98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to TripScribe.
   
   * `addi n/Bali Getaway dest/Bali from/2026-07-01 to/2026-07-05`: Adds an itinerary named `Bali Getaway` to TripScribe.

   * `delete /contact 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts and itineraries.

   * `exit` : Exits the app.

You can refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` is information you supply to TripScribe.<br>
  e.g. in `addc r/ROLE`, `ROLE` is used as `addc r/client`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/Bus` or `n/John Doe`.

* You can use inputs with `…`​ after them either zero or multiple times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/Bus`, `t/Bus t/Speaks English` etc.

* The information you supply can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Additional parameters for commands that do not require them (such as `help`, `exit` and `clear`) will be ignored.<br>
  e.g. if you type `help 123`, it will be interpreted as `help`.

* When using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as there may be formatting issues.
</box>

### Viewing help : `help`

Opens a help window showing a summary of all available commands and a link to the full User Guide.

![help message](images/helpMessage.png)

Format: `help`


### Adding a contact: `addc`

Adds a contact to TripScribe.

Format: `addc r/ROLE n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​`

<box type="tip" seamless>

**Tip:**
- `ROLE` must be either `client` or `vendor`
- A contact can have any number of tags (including 0)
- Phone numbers should follow the format `(+<Country Code>) <Phone Number>`, for example `(+65) 98765432`.
- Contacts with the same name and phone number are considered duplicates. Duplicate contacts cannot be added.
</box>

Examples:
* `addc r/client n/John Doe p/(+65) 98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `addc r/vendor n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/(+44) 1234567 t/hotel`

A successful `addc` command will add the contact to the contact list.

![Result of adding a contact](images/AddcSuccess.png)

### Adding an itinerary: `addi`

Adds an itinerary to TripScribe.

Format: `addi n/ITINERARY_NAME dest/DESTINATION from/START_DATE to/END_DATE [c/CLIENT_ID]…​ [v/VENDOR_ID]…​`

* `ITINERARY_NAME` and `DESTINATION` can be any non-empty string of characters. For example, ` ` is not allowed
while `Island Time: Bali` and `Bali, Indonesia` are allowed.
* Adding of duplicate itinerary is not allowed. An itinerary is considered a duplicate if the name (case-insensitive)
matches an existing entry. For example, itineraries with names `ISLAND TIME: Bali` and `Island Time: Bali` are
considered duplicates.
* `START_DATE` and `END_DATE` must be in the format `yyyy-mm-dd`. For example, `20th March 2026` will be written as
`2026-03-20`. `END_DATE` must be **equal to or after** `START_DATE`. For example, `from/2026-03-20 to/2026-03-19` is 
not allowed.
* `CLIENT_ID` and `VENDOR_ID` are the unique identity numbers given to the contacts after adding them into 
TripScribe.

**Tip:** An itinerary can have any number of client and vendor ids (including 0)

Examples:
* `addi n/Island Time: Bali dest/Bali from/2026-12-01 to/2026-12-05`
* `addi n/5D4N France Getaway dest/France from/2026-10-12 to/2026-10-17 c/236075fd-4619-4b41-8d9f-9d98eadedd89 v/5b8511e5-12d0-49fa-b1da-d84fa7df756a`

| ![add itinerary command typed in TripScribe](images/AddItineraryBefore.png)<br>Input | ![Add itinerary executed in TripScribe](images/AddItineraryAfter.png)<br>Expected Output |
|:------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------:|

### Listing contacts and itineraries : `list`

Shows a list of contacts or itineraries based on the specified flag.

Format: `list /FLAG`

* `FLAG` must be one of: `contact`, `client`, `vendor`, `itinerary`
  * `contact` displays all contacts regardless of role.
  * `client` displays only contacts with the role `client`.
  * `vendor` displays only contacts with the role `vendor`.
  * `itinerary` displays all itineraries.
* When viewing contacts (`/contact`, `/client`, `/vendor`), the itinerary panel is hidden.
* When viewing itineraries (`/itinerary`), the contact panel is hidden.

Examples:
* `list /contact` lists all contacts.
* `list /client` lists all clients.
* `list /vendor` lists all vendors.
* `list /itinerary` lists all itineraries.

### Editing a person : `edit`

Edits an existing person in TripScribe.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 p/(+65) 91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `(+65) 91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Deleting a contact or itinerary : `delete`

You can delete a specified contact or itinerary from TripScribe.

Format: `delete /FLAG INDEX`

* Deletes the person or itinerary at the specified `INDEX`.
* `/FLAG` specifies the entry type you are deleting.
  * Valid values for `/FLAG`: `/contact` , `/itinerary`
* `INDEX` is the index number shown in the displayed person or itinerary list.
* The index **must be a positive, non-zero number** 1, 2, 3, …​

Examples:
* `list /contact` followed by `delete 2` deletes the 2nd contact in TripScribe.

| ![delete contact command typed in TripScribe](images/DeleteContactBefore.png)<br>Input | ![delete contact command executed in TripScribe](images/DeleteContactAfter.png)<br>Expected Output |
|:--------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------:|

* `list /itinerary` followed by `delete 1` deletes the 1st itinerary in TripScribe.

 | ![delete itinerary command typed in TripScribe](images/DeleteItineraryBefore.png)<br>Input | ![delete itinerary executed in TripScribe](images/DeleteItineraryAfter.png)<br>Expected Output |
 |:------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|


### Clearing all entries : `clear`

Clears all entries from TripScribe.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

TripScribe data are saved in the hard disk automatically after any command that changes the data. You do not need to save manually.

### Editing the data file

TripScribe data is saved automatically as a JSON file `[JAR file location]/data/tripscribe.json`. Advanced users can update data directly by editing the data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, TripScribe will discard all data and start with an empty data file at the next run.  Hence, you are recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the TripScribe to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you know what you are doing.
</box>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and replace the empty data file it creates with your file that contains the data of your previous TripScribe application folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. 
   * To fix this, delete the `preferences.json` file created by TripScribe before running the application again.
<br><br>
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. 
   * To fix this, close the minimized Help Window and type the command again.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action     | Format, Examples
-----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------
**Addc**   | `addc r/ROLE n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `addc r/client n/James Ho p/(+65) 22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague`
**Addi**   | `addi n/ITINERARY_NAME dest/DESTINATION from/START_DATE to/END_DATE [c/CLIENT_ID]…​ [v/VENDOR_ID]…​` <br> e.g., `addi n/5D4N France Getaway dest/France from/2026-10-12 to/2026-10-17 c/236075fd-4619-4b41-8d9f-9d98eadedd89 v/5b8511e5-12d0-49fa-b1da-d84fa7df756a`
**Clear**  | `clear`
**Delete** | `delete /FLAG INDEX`<br> e.g., `delete /contact 3`
**Edit**   | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Find**   | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List**   | `list /FLAG` <br> e.g., `list /contact`
**Help**   | `help`
