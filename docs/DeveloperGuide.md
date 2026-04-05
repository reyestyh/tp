---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# TripScribe Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------
## Table of Contents

- [Acknowledgements](#acknowledgements)
- [Setting up, getting started](#setting-up-getting-started)
- [Design](#design)
    - [Architecture](#architecture)
    - [UI component](#ui-component)
    - [Logic component](#logic-component)
    - [Model component](#model-component)
    - [Storage component](#storage-component)
    - [Common classes](#common-classes)
- [Implementation](#implementation)
    - [[Proposed] Undo/redo feature](#proposed-undoredo-feature)
        - [Proposed Implementation](#proposed-implementation)
        - [Design considerations](#design-considerations)
    - [[Proposed] Data archiving](#proposed-data-archiving)
- [Documentation, logging, testing, configuration, dev-ops](#documentation-logging-testing-configuration-dev-ops)
- [Appendix: Requirements](#appendix-requirements)
    - [Product scope](#product-scope)
    - [User stories](#user-stories)
    - [Use cases](#use-cases)
    - [Non-Functional Requirements](#non-functional-requirements)
    - [Glossary](#glossary)
- [Appendix: Instructions for manual testing](#appendix-instructions-for-manual-testing)
    - [Launch and shutdown](#launch-and-shutdown)
    - [Deleting a person](#deleting-a-person)
    - [Deleting an itinerary](#deleting-an-itinerary)

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_Libraries used: [JavaFX](https://openjfx.io/), [Jackson](https://github.com/FasterXML/jackson), [JUnit5](https://github.com/junit-team/junit5)_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S2-CS2103T-F12-1/tp/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2526S2-CS2103T-F12-1/tp/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point).

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S2-CS2103T-F12-1/tp/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `ItineraryListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S2-CS2103T-F12-1/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S2-CS2103T-F12-1/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` and `Itinerary` objects that reside in `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S2-CS2103T-F12-1/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>
**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
3. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2526S2-CS2103T-F12-1/tp/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S2-CS2103T-F12-1/tp/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both TripScribe data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Edit command
The edit feature modifies contact and itinerary details. The implementation uses an abstract `EditCommand` class with specialized subclasses to handle the editing of `Person` and `Itinerary` objects respectively.

#### Architecture
The class diagram below shows the overall structure of the edit command implementation:

<puml src="diagrams/EditCommandClassDiagram.puml" width="600"/>

`EditCommand` is abstract, with `EditPersonCommand` and `EditItineraryCommand` providing concrete implementations to edit a `Person` or `Itinerary` respectively. 
Each subclass contains a descriptor class, `EditPersonDescriptor` and `EditItineraryDescriptor` respectively, which are used to merge the unchanged fields with new edited fields.

#### Command Execution

The sequence diagram below illustrates how an edit command is executed in general:

<puml src="diagrams/EditCommandSequenceDiagram.puml" width="600"/>

The edit command is executed in four main steps:
1. **Parsing:** `EditCommandParser` parses user input to create the appropriate `EditXYZDescriptor` and `EditXYZCommand` subclass (where `XYZ` is a placeholder for the type of entry being edited, either `Person` or `Itinerary`).
    * For example, the command `edit /contact 1 n/Alice` will create an `EditPersonDescriptor` and `EditPersonCommand`.
2. **Validation:** `EditCommandParser` also validates the index given and ensures at least one field is being edited.
3. **Merging:** The descriptor created in step 1 combines the target entry's unchanged fields with the updated values.
4. **Update:** `EditXYZCommand` creates an edited `XYZ` entry using the descriptor in step 3, and replaces the old entry in the list.

#### Editing a person
The following class diagram shows the attributes and methods used in `EditCommand`, `EditPersonCommand` and `EditPersonDescriptor`.

<puml src="diagrams/EditPersonClassDiagram.puml" width="600"/>

More on `EditPersonCommand`:
* `executeEditCommand()`  —  Implements the abstract method in `EditCommand` to execute the edit.
* `createEditedPerson()`  —  Creates a new `Person` based on the `EditPersonDescriptor` provided.

More on `EditPersonDescriptor`:
* Each `Person` field has a getter and setter
* For each field, if the field was edited, the setter method is used to set the field to the updated value. Otherwise, the original value is retrieved by the getter method.
* For example, if the person's name field was not edited, its old name is retrieved and used in the descriptor:<br>`Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());`

#### Editing an itinerary
Editing an itinerary follows the same pattern as [Editing a person](#editing-a-person) with these key differences:
* **Additional fields:** `EditItineraryDescriptor` stores `startDate` and `endDate` in addition to the `Itinerary` attributes (`itineraryName`,`destination`, `dateRange`). This is to enable additional date validation.
* **Date validation:** `createEditedItinerary()` checks that `startDate` is before `endDate`and throws a `CommandException` if invalid.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

Operations executives in small to mid-size tour agencies who

* need to manage a significant number of client bookings and itineraries
* handle frequent updates to itineraries, client details and vendor booking notes
* coordinate between multiple groups (e.g. transport, tour guides, tourists, vendors)
* prefer desktop apps over other types
* can type fast
* prefers using CLI interfaces over mouse-based interfaces

**Value proposition**:

Existing tools are too heavy or fragmented. Our app is a lightweight, single‑user solution for tour agency executives, enabling rapid typed commands to manage client contacts, addresses, itineraries, and vendor details. Data is stored locally in editable text files.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                   | I want to …​                                                                      | So that I can…​                                         |
|----------|-------------------------------------------|-----------------------------------------------------------------------------------|---------------------------------------------------------|
| `* * *`  | tour agency operation executive           | add a new contact with name, role, phone number, email, address, and tags         | record details of clients, vendors, tour guides, etc    |
| `* * *`  | tour agency operation executive           | add a new itinerary with destination, duration and associated clients and vendors | record and view itinerary details at a glance           |
| `* * *`  | tour agency operation executive           | list all itineraries                                                              | check that all details are in order                     |
| `* * *`  | tour agency operation executive           | list all contacts which includes client and vendors                               | plan itineraries with the relevant information          |
| `* * *`  | tour agency operation executive           | delete a contact or itinerary                                                     | remove data that I no longer need                       |
| `* * *`  | tour agency operation executive           | work offline without internet                                                     | manage trips even in areas with poor connectivity       |
| `* * *`  | forgetful tour agency operation executive | view documentation through a help function                                        | find out how to use the app and what I can do with it   |
| `* *`    | tour agency operation executive           | update contact information                                                        | make sure my contact information is accurate            |
| `* *`    | tour agency operation executive           | update itinerary details                                                          | keep trip information accurate when plans change        |
| `* *`    | tour agency operation executive           | filter contacts by tags                                                           | search or view contacts associated to a tag             |
| `* *`    | tour agency operation executive           | sort itineraries by date                                                          | prioritize itineraries according to how urgent they are |
| `* *`    | tour agency operation executive           | sort contact in alphabetical order                                                | find a contact easily                                   |
| `* *`    | tour agency operation executive           | undo and redo commands                                                            | recover from mistakes                                   |
| `* *`    | tour agency operation executive           | archive completed itineraries                                                     | keep my workplace uncluttered                           |
| `* *`    | tour agency operation executive           | add pictures to contacts                                                          | recognize the people I work with                        |

### Use cases

(For all use cases below, `TripScribe` is the **System** and `User` is the **Actor**, unless specified otherwise)

**UC01: Add a contact**

**MSS**

1. User requests to add a contact and provides the contact details.
2. TripScribe creates the contact and displays a success message and the updated contact list.

Use case ends.

**Extensions**

* 1a. TripScribe detects an error in the entered command format.
    * 1a1. TripScribe shows a format error message and displays the correct command usage.
      
      Use case ends.

* 1b. TripScribe detects invalid values in the entered contact details (e.g., invalid email/phone, empty name).

    * 1b1. TripScribe shows a validation error message and displays the correct command usage (and/or which field is invalid).

      Use case ends.
* 1c. TripScribe detects a duplicate contact.
    * 1c1. TripScribe shows a duplicate message and does not create the contact.
    
      Use case ends.
---
**UC02: Add an itinerary**

**MSS**

1. User requests to add an itinerary and provides the itinerary details.
2. TripScribe creates the itinerary and displays a success message and the updated itinerary list.

Use case ends.

**Extensions**

* 1a. TripScribe detects an error in the entered command format.
    * 1a1. TripScribe displays a format error message with the correct command usage.

      Use case ends.

* 1b. TripScribe detects invalid values in the entered itinerary details (e.g., end date earlier than start date, invalid date format).
    * 1b1. TripScribe shows a validation error message and displays the correct command usage (and/or which field is invalid).

      Use case ends.
* 1c. TripScribe cannot find the referenced client or vendor(e.g., provided client id/vendor id does not exist).
    * 1c1. TripScribe displays an error message indicating the contact is not found and does not create the itinerary.
    
      Use case ends.
* 1d. TripScribe detects a duplicate itinerary.
    * 1d1. TripScribe shows a duplicate message and does not create the itinerary.
      
      Use case ends.

---
**UC03: List**

**MSS**

1. User requests to view a list of entries by specifying a category (e.g., all / contact / itinerary / vendor / client).
2. TripScribe retrieves the matching entries.
3. TripScribe displays the list of entries.

Use case ends.

**Extensions**

* 1a. TripScribe detects an error in the entered command format (e.g., Typo).
    * 1a1. TripScribe displays a format error message with the correct command usage.

      Use case ends.
* 1b. TripScribe detects an error in the entered command details (e.g., missing category / unrecognized category).
    * 1b1. TripScribe displays a format error message with the correct command usage and list of supported categories.

      Use case ends.
* 2a. TripScribe finds zero entries matching.
    * 2a1. TripScribe displays an empty result message.
  
      Use case ends.

---
**UC04: Delete**

**MSS**
1. User requests to delete a contact or itinerary by specifying the entry identifier.
2. TripScribe deletes the entry and displays a success message and the updated list.

Use case ends.

**Extensions**

* 1a. TripScribe detects an error in the entered command format.
    * 1a1. TripScribe displays a format error message with the correct command usage.
    
      Use case ends.
* 1b. TripScribe detects an invalid identifier (e.g., not a number / out of range / not found).
    * 1b1. TripScribe shows an error message indicating the target does not exist and does not delete anything.
  
      Use case ends.
* 1c. TripScribe detects that the specified entry is referenced by itineraries.
    * 1c1. TripScribe shows an error message describing the dependency and aborts deletion.
    
      Use case ends.


### Non-Functional Requirements

1. The system should run on any mainstream OS that has Java 17 or above installed.
2. The system should be able to support up to 1000 entries without a noticeable sluggishness in performance during typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. The system should function without an Internet connection, allowing users to access and run the application offline.

### Glossary

* **Case-Insensitive Text**: Text where uppercase and lowercase letters are treated as equivalent.
* **Client**: A person who participates in a tour organized by the agency.
* **Command Line Interface (CLI) Application**: An application that users interact with by typing commands.
* **Flag**: An option used with a command to specify or modify its behavior.
* **Graphical User Interface (GUI) Application**: An application that users interact with through graphical elements such as buttons, icons, and menus using a mouse or keyboard.
* **Index**: The one-based position number of an entry as shown in the currently displayed list. For example, the first entry shown has index `1`. The index changes depending on the current list view.
* **Itinerary**: A plan for a tour that includes the tour name, start and end dates, the clients participating in the tour, and the vendors involved.
* **Mainstream OS**: Windows, Linux, Unix and macOS.
* **Tag**: A label used to categorize any number of entries together.
* **Universally Unique Identifier (UUID)**: A unique 36-character value used to distinguish contacts in TripScribe. In particular, it consists of numbers (0 - 9) and letters (a - f) separated by hyphens. (e.g.123e4567-e89b-12d3-a456-426614174000).
* **Vendor**: A party that provides goods or services for a tour.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch
   1. Download the jar file and copy into an empty folder
   2. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences
   1. Resize the window to an optimum size. Move the window to a different location. Close the window.
   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

3. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown
   1. Prerequisites: List all persons using the `list /all` command. Ensure there are multiple persons in the list. 
   2. Test case: `delete \contact 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.
   
   Other incorrect delete commands to try:
    1. Test case: `delete` (missing flag and index)<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.
    2. Test case: `delete \contact` (missing index)<br>
       Expected: Similar to previous.
    3. Test case: `delete \contact 0` (invalid index)<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.
    4. Test case: `delete \contact x` where x is larger than the list size (invalid index)<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

2. Deleting a person associated with an itinerary
   1. Prerequisites: Ensure a person is associated with an itinerary (e.g. person 1 linked to itinerary 2). Ensure the person is displayed in the person list (e.g. Using the `list /all` command).
   2. Test case: `delete \contact x` (where x is the person's index in the displayed list)<br>
      Expected: Contact is deleted.
   
   Follow-up: 
   1. Ensure the itinerary is displayed in the itinerary list (e.g. Using the `list /all` command).
   2. Test case: `show y` (where y is the associated itinerary's index in the displayed list)<br>
      Expected: Deleted contact no longer appears in the itinerary’s associated contact list.

3. Deleting a person when only some persons are being shown
    1. Prerequisites: Use a filtering command (e.g. `list /vendor`, `list /client`) to display a subset of the full contact list. Ensure there is at least one person in the list.
     2. Test case: `delete \contact 1`<br>
       Expected: First contact is deleted from the filtered list, not the full list.

4. Deleting a person when no persons are being shown
    1. Prerequisites: Hide all persons and list all itineraries using the `list /itinerary` command.
    2. Test case: `delete \contact 1`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

### Deleting an itinerary

These test cases follow the same steps as manual testing for [Deleting a person](#deleting-a-person), with the following differences:
- The command format is `delete /itinerary x`, where x is the index of the itinerary in the displayed list.
- All expected outcomes remain the same, with references to “person” replaced by “itinerary”.
- The scenario _Deleting a person associated with an itinerary_ does not apply.

### Saving data

1. Dealing with missing/corrupted data files
   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

2. _{ more test cases …​ }_
