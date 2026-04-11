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
- [Setting Up, Getting Started](#setting-up-getting-started)
- [Design](#design)
    - [Architecture](#architecture)
    - [UI Component](#ui-component)
    - [Logic Component](#logic-component)
    - [Model Component](#model-component)
    - [Storage Component](#storage-component)
    - [Common Classes](#common-classes)
- [Implementation](#implementation)
    - [Itinerary](#itinerary)
    - [Adding Itineraries](#adding-itineraries)
    - [Edit Command](#edit-command)
    - [Show Command](#show-command)
    - [Find Command](#find-command)
- [Documentation, Logging, Testing, Configuration, Dev-ops](#documentation-logging-testing-configuration-dev-ops)
- [Appendix: Requirements](#appendix-requirements)
    - [Product Scope](#product-scope)
    - [User Stories](#user-stories)
    - [Use Cases](#use-cases)
    - [Non-Functional Requirements](#non-functional-requirements)
    - [Glossary](#glossary)
- [Appendix: Instructions for Manual Testing](#appendix-instructions-for-manual-testing)
    - [Launch and Shutdown](#launch-and-shutdown)
    - [Deleting a Person](#deleting-a-person)
    - [Deleting an Itinerary](#deleting-an-itinerary)
    - [Saving Data](#saving-data)
- [Appendix: Effort](#appendix-effort)
    - [Difficulty and Challenges](#difficulty-and-challenges)
    - [Effort and Achievements](#effort-and-achievements)
- [Appendix: Planned Enhancements](#appendix-planned-enhancements)
--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_Libraries used: [JavaFX](https://openjfx.io/), [Jackson](https://github.com/FasterXML/jackson), [JUnit5](https://github.com/junit-team/junit5)_

--------------------------------------------------------------------------------------------------------------------

## **Setting Up, Getting Started**

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

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete /contact 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point).

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI Component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S2-CS2103T-F12-1/tp/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `ItineraryListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S2-CS2103T-F12-1/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S2-CS2103T-F12-1/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` and `Itinerary` objects that reside in `Model`.

### Logic Component

**API** : [`Logic.java`](https://github.com/AY2526S2-CS2103T-F12-1/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete /contact 1")` API call as an example.

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
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible (e.g, during testing).

### Model Component
**API** : [`Model.java`](https://github.com/AY2526S2-CS2103T-F12-1/tp/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="650" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object) and `Itinerary` objects (which are contained in a `UniqueItineraryList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change. Similarly, for currently 'selected' `Itinerary` objects (e.g. when viewing a specific itinerary).
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>

### Storage Component

**API** : [`Storage.java`](https://github.com/AY2526S2-CS2103T-F12-1/tp/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both TripScribe data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common Classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Itinerary

#### Implementation

`Itinerary` is a class which has the following mandatory fields `ItineraryName`, `Destination`, `DateRange`. They also hold 2 `Id` lists, `clientIds` and `vendorIds`.

* Both `ItineraryName` and `Destination` must be a non-empty `String`.
* `DateRange` contains a start date and end date, where the start date must be before or on the same day as the end date.
* Each `Id` in the list corresponds to a unique `Person` and cannot be repeated.
* An `Id` belonging to a person of the *client* `Role` cannot be in the client list, an `Id` belonging to a person of the *vendor* `Role` cannot be in the vendor list.

The class diagram for `Itinerary` is shown below:

<puml src="diagrams/ItineraryClassDiagram.puml" alt="ItineraryClassDiagram" />

#### Design considerations

**Aspect: Valid `ItineraryName` and `Destination` values**

* **Alternative 1 (current choice):** Allow any non-empty value.
    * Pros: Easy to implement. Allows for inputs in the form of `Japan Tour: Tokyo, Osaka, Kyoto` as users likely will use certain symbols like `:` and `,`
    * Cons: Uncommon symbols are accepted, which results in more erroneous inputs.

* **Alternative 2:** Only allow alphanumeric characters
    * Pros: No unusual symbols allowed (i.e. `{`, `~`, etc.).
    * Cons: Itinerary names and destinations that use symbols like `:` and `,` are not allowed.

### Adding Itineraries
#### Implementation
The `addi` command allows users to add itineraries to the address book. An itinerary can have any number of clients and vendors which are `Person` objects.
This section aims to detail how the association between `Person` and `Itinerary` objects is handled in TripScribe.

Every `Itinerary` object stores two lists of `Id`. One for `Id`s of clients and another for `Id`s of vendors involved in the itinerary. The following object diagram provides an illustration:
<puml src="diagrams/AddiObjectDiagram.puml" />
<br><br>

Recall that `AddressBook` in `Model` component stores a `UniquePersonList` and a `UniqueItineraryList`. <br>
When adding an `Itinerary` object into the `AddressBook` object, `AddressBook` will be responsible for ensuring the consistency of address book data.
That is, `AddressBook` will check if the `Id`s referenced by the itinerary belong to `Person` objects that already exist in the `UniquePersonList`. If not, the `AddressBook` will reject the itinerary and throw a `PersonNotFoundException`. In other words,
`AddressBook` will ensure that an itinerary can only be added if all the `Person` objects it references already exist in the address book. The following sequence diagram illustrates the interactions when adding an itinerary:

<div class="row">
  <div class="col-md-6">

<h6 align="center">Success</h4>

<puml src="diagrams/AddiSequenceDiagramSuccess.puml" />

  </div>
  <div class="col-md-6">

<h6 align="center">Failure</h4>

<puml src="diagrams/AddiSequenceDiagramFail.puml" />

  </div>
</div>

Since every `Person` has a unique `Id`, we have an unambiguous way to associate an itinerary with its clients and vendors without needing direct references to the objects. This decoupled design significantly simplifies the process of saving and reading TripScribe data.


When saving data, the `Storage` component can simply serialize the fields of the `Person` and `Itinerary` objects exactly as they are.
Their associations are already captured through the stored `Id` strings.


When reading the JSON file to construct the corresponding objects, we have the following two-step process:
1. Read and construct all `Person` objects, adding them to the `AddressBook`.
2. Read and construct all `Itinerary` objects, adding them to the `AddressBook`. When adding an `Itinerary`, the `AddressBook` will already contain all the necessary `Person` objects, allowing it to enforce data consistency as described above.

#### Design considerations
**Aspect: Managing the association between contacts and itineraries**
* **Alternative 1 (current choice):** `Itinerary` stores the `Id`s of `Person`s.
    * Pros: Reading and saving data to JSON file is simple to implement with minimal data duplication. Reduces coupling between `Itinerary` and `Person` classes.
    * Cons: Introduce slight overheads. When retrieving the clients and vendors of an itinerary, we need to resolve the `Id`s back to the `Person` objects.
* **Alternative 2:** `Itinerary` stores direct references to `Person`s.
    * Pros: The `addi` command would be simpler to implement.
    * Cons: Reading and saving data becomes more complex.

### Edit Command

The `edit` command modifies contact and itinerary details. The implementation uses an abstract `EditCommand` class with specialized subclasses to handle the editing of `Person` and `Itinerary` objects respectively.

#### Implementation

`EditCommand` is abstract, with `EditPersonCommand` and `EditItineraryCommand` providing concrete implementations to edit a `Person` or `Itinerary` respectively.
Each subclass contains a descriptor class, `EditPersonDescriptor` and `EditItineraryDescriptor` respectively, which are used to merge the unchanged fields with new edited fields.

The class diagram below shows the overall structure of the `edit` command implementation:

<puml src="diagrams/EditCommandClassDiagram.puml" width="600"/>


The `edit` command is executed in four main steps:
1. **Parsing:** `EditCommandParser` parses user input to create the appropriate `EditXYZDescriptor` and `EditXYZCommand` subclass (where `XYZ` is a placeholder for the type of entry being edited, either `Person` or `Itinerary`).
    * For example, the command `edit /contact 1 n/Alice` will create an `EditPersonDescriptor` and `EditPersonCommand`.
2. **Validation:** `EditCommandParser` also validates the index given and ensures at least one field is being edited.
3. **Merging:** The descriptor created in step 1 combines the target entry's unchanged fields with the updated values.
4. **Update:** `EditXYZCommand` creates an edited `XYZ` entry using the descriptor in step 3, and replaces the old entry in the list.

The sequence diagram below illustrates how an `edit` command is executed:

<puml src="diagrams/EditCommandSequenceDiagram.puml" width="600"/>

<box type="info" seamless>

**Note:** The lifeline for `EditCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

**Editing a person**

The following class diagram shows the attributes and methods used in `EditCommand`, `EditPersonCommand` and `EditPersonDescriptor`.

<puml src="diagrams/EditPersonClassDiagram.puml" width="600"/>

More on `EditPersonCommand`:
* `executeEditCommand()`  —  Implements the abstract method in `EditCommand` to execute the edit.
* `createEditedPerson()`  —  Creates a new `Person` based on the `EditPersonDescriptor` provided.

More on `EditPersonDescriptor`:
* Each `Person` field has a getter and setter
* For each field, if the field was edited, the setter method is used to set the field to the updated value. Otherwise, the original value is retrieved by the getter method.
* For example, if the person's name field was not edited, its old name is retrieved and used in the descriptor:<br>`Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());`

**Editing an itinerary**

Editing an itinerary follows the same pattern as editing a person (shown above) with these key differences:
* **Additional fields:** `EditItineraryDescriptor` stores `startDate` and `endDate` in addition to the `Itinerary` attributes (`itineraryName`,`destination`, `dateRange`). This is to enable additional date validation.
* **Date validation:** `createEditedItinerary()` checks that `startDate` is before `endDate`and throws a `CommandException` if invalid.

#### Design considerations

**Aspect: `EditCommand` class structure**
* **Alternative 1 (current choice):** Abstract `EditCommand` class with `EditPersonCommand` and `EditItineraryCommand` subclasses
    * Pros: Better type safety and follows the Open-Closed Principle.
    * Cons: More classes to maintain.

* **Alternative 2:** Single `EditCommand` class to handle both `Person` and `Itinerary` editing.
    * Pros: Fewer classes.
    * Cons: Less type-safety. Long `EditCommand` class.

### Show Command

#### Implementation

The `show` command shows the itinerary at the index specified by the user, and the contacts associated with it. Similar to how the `delete` command works [above](#logic-component), the show command is implemented in a similar way.

On execution, the command creates and passes 2 predicates to update the filtered lists in `Model`:
* `itineraryMatchesPredicate` — Return `true` if the itinerary is the same as the itinerary specified by the user.
* `idMatchesPredicate` — Return `true` if the `Id` of the `Person` is found in either the `clientList` or `vendorList` of the itinerary specified.

The sequence diagram below shows the interactions within the logic component.

<puml src="diagrams/ShowSequenceDiagram-Logic.puml" alt="ShowSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `ShowCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

#### Design considerations:

**Aspect: Showing association between contacts and itineraries :**

* **Alternative 1 (current choice):** The contacts and itineraries are shown in their respective lists.
    * Pros: Easier to implement.
    * Cons: Users need to scroll through contact list in GUI to see all associated contacts.

* **Alternative 2:** Show contacts as a part of `ItineraryListPanel`
    * Pros: Only 1 panel shown on GUI, resulting in cleaner user experience.
    * Cons: Complex implementation required to get respective `Person`s from `Model` for each itinerary. Every `Model` update requires updating each `ItineraryListPanel` for each itinerary.

### Find Command

#### Implementation

The `find` command allows users to search for contacts using either a **general search** format or a **multi-field search** format.

The general search format matches keywords against all searchable fields of a contact, while the multi-field search format matches keywords only against the user-specified fields.

The `find` command is facilitated by `FindCommandParser`, which parses the user input and constructs one of the following predicates:

- `PersonContainsKeywordsPredicate`— Performs a general search across all searchable fields.
- `PersonMatchesFieldsPredicate` — Performs a multi-field search on specific fields.

These predicates are then passed into `FindCommand`, which updates the filtered contact list in the model.

The searchable fields supported by the `find` command are:

- name
- phone
- email
- address
- tags

Given below are two example usage scenarios and how the `find` command behaves at each step.

**Scenario 1: General search**

1. The user executes `find alex david`.

2. `FindCommandParser` checks that the input does not contain any supported field prefixes (`n/`, `p/`, `e/`, `a/`, `t/`).

3. Since no prefixes are found, the parser treats the input as a general search. It splits the input into individual keywords and constructs a `PersonContainsKeywordsPredicate`.

4. `FindCommand` is created with this predicate and executed.

5. During execution, `Model#updateFilteredPersonList(...)` is called with the predicate. A contact is included in the filtered list if **any** keyword appears in **any** searchable field.

For example, `find alex david` will return contacts whose name, phone, email, address, or tags contain `alex` or `david`.

**Scenario 2: Multi-field search**

1. The user executes `find n/alex yu p/996`.

2. `FindCommandParser` detects the presence of supported prefixes and treats the input as a multi-field search.

3. The parser extracts the keywords associated with each prefix:
   - `n/` → `alex, yu`
   - `p/` → `996`

4. A `PersonMatchesFieldsPredicate` is created using these field-specific keyword lists.

5. `FindCommand` is created with this predicate and executed.

6. During execution, `Model#updateFilteredPersonList(...)` is called with the predicate. A contact is included in the filtered list only if it satisfies all specified fields:
   - within the same field, keywords are matched using `OR`
   - across different fields, fields are matched using `AND`

For example, `find n/alex yu p/996` will return contacts whose names contain `alex` or `yu`, and whose phone numbers contain `996`.

The following sequence diagram shows how the `find` operation goes through the `Logic` component:

<puml src="diagrams/FindSequenceDiagram-Logic.puml" alt="FindSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `FindCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how a find operation goes through the `Model` component is shown below:

<puml src="diagrams/FindSequenceDiagram-Model.puml" alt="FindSequenceDiagram-Model" />

<box type="info" seamless>

**Note**: The two formats cannot be mixed.
For example, `find alex p/996` is rejected as invalid input format.

</box> <box type="info" seamless>

**Note**: Matching is case-insensitive and based on substring matching.
For example, alex will match Alex, and lex will also match Alex.

</box>

#### Design considerations

**Aspect: How `find` supports two search formats**

* **Alternative 1 (current choice):** Support both a general search format and a multi-field search format.
  * Pros: More flexible for users. General search is fast for broad lookup, while multi-field search gives users more control.
  * Cons: Parser logic is more complex, since it must distinguish between the two formats and reject mixed usage.

* **Alternative 2:** Support only general search.
  * Pros: Simpler parser and predicate logic.
  * Cons: Users cannot restrict the search to specific fields, may return too much matching contacts.

* **Alternative 3:** Support only multi-field search.
  * Pros: Clearer and more structured command format.
  * Cons: Less convenient for users who want to perform a quick broad search.

**Aspect: How matching is performed**

* **Alternative 1 (current choice):** Use substring matching with case-insensitive comparison.
  * Pros: More user-friendly, since users do not need to type exact full-field values.
  * Cons: May return broader results than expected.

* **Alternative 2:** Match only full words or exact field values.
  * Pros: More precise results.
  * Cons: Less flexible and less convenient for users.

**Aspect: How multi-field search combines conditions**

* **Alternative 1 (current choice):** Use `OR` within the same field and `AND` across different fields.
  * Pros: Natural balance between flexibility and precision. Users can provide multiple possible matches for one field while still constraining other fields.
  * Cons: Slightly harder to explain in the User Guide and Developer Guide.

* **Alternative 2:** Use `OR` for all fields and all keywords.
  * Pros: Simpler mental model.
  * Cons: Results may be too broad for structured searches.

* **Alternative 3:** Use `AND` for all fields and all keywords.
  * Pros: Very strict filtering.
  * Cons: Often too restrictive for practical use.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, Logging, Testing, Configuration, Dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product Scope

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

### User Stories

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
| `* *`    | tour agency operation executive           | clear all existing data                                                           | start fresh with a new data set                         |
| `* *`    | tour agency operation executive           | find contacts with specific keywords                                              | search and view all contacts with the keywords          |
| `* *`    | tour agency operation executive           | see contacts associated with an itinerary                                         | view all contacts associated with an itinerary          |

### Use Cases

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

Similar to UC01, except that TripScribe also validates itinerary-specific fields such as dates and referenced contacts.

**Extensions**

* 1a. Similar to UC01 extension 1a.

* 1b. TripScribe detects invalid itinerary details (e.g., invalid date format, end date earlier than start date).
    * 1b1. TripScribe displays a validation error message.

      Use case ends.

* 1c. TripScribe cannot find a referenced client or vendor.
    * 1c1. TripScribe displays an error message indicating that the referenced contact does not exist.

      Use case ends.

* 1d. Similar to UC01 extension 1c.

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
1. User requests to delete a contact or itinerary by specifying the entry type and index.
2. TripScribe deletes the specified contact or itinerary.
3. If the deleted entry is a contact, TripScribe removes that contact from any associated itineraries.
4. TripScribe displays a success message and the updated list.

Use case ends.

**Extensions**

* 1a. TripScribe detects an error in the entered command format.
    * 1a1. TripScribe displays a format error message with the correct command usage.

      Use case ends.

* 1b. TripScribe detects an invalid index (e.g., not a positive integer or out of range).
    * 1b1. TripScribe shows an error message indicating that the specified entry does not exist.

      Use case ends.

* 1c. TripScribe detects an invalid flag.
    * 1c1. TripScribe displays an error message indicating the valid flags.

      Use case ends.

### Non-Functional Requirements

1. The system should run on any mainstream OS that has Java 17 or above installed.
2. The system should be able to support up to 1000 entries without a noticeable sluggishness in performance during typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. The system should function without an Internet connection, allowing users to access and run the application offline.

### Glossary

* **Alphanumeric characters**: Characters consisting of letters (A–Z, a–z) and numbers (0–9).
* **Case-Insensitive Text**: Text where uppercase and lowercase letters are treated as equivalent.
* **Client**: A person who participates in a tour organized by the agency.
* **Command Line Interface (CLI) Application**: An application that users interact with by typing commands.
* **Domain-label**: A part of a domain name separated by dots, consisting of alphanumeric characters and hyphens, but not beginning or ending with a hyphen.
* **Flag**: An option used with a command to specify or modify its behavior.
* **Graphical User Interface (GUI) Application**: An application that users interact with through graphical elements such as buttons, icons, and menus using a mouse or keyboard.
* **Index**: The one-based position number of an entry as shown in the currently displayed list. For example, the first entry shown has index `1`. The index changes depending on the current list view.
* **Itinerary**: A plan for a tour that includes the tour name, start and end dates, the clients participating in the tour, and the vendors involved.
* **Mainstream OS**: Windows, Linux, Unix and macOS.
* **Tag**: A label used to categorize any number of entries together.
* **Universally Unique Identifier (UUID)**: A unique 36-character value used to distinguish contacts in TripScribe. In particular, it consists of numbers (0 - 9) and letters (a - f) separated by hyphens. (e.g.123e4567-e89b-12d3-a456-426614174000).
* **Vendor**: A party that provides goods or services for a tour.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for Manual Testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and Shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   2. Run the jar file as per [quick start](UserGuide.md/#running-tripscribe). <br>
      Expected: GUI is shown with sample contacts

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

3. Exiting TripScribe using `exit`
   1. Type `exit` into the command bar of TripScribe <br>
      Expected: TripScribe application window is closed.

4. Exiting TripScribe using the exit button
   1. Click `File` in TripScribe and click the `Exit` button <br>
      Expected: TripScribe application window is closed.

### Deleting a Person

1. Deleting a person while all persons are being shown
   1. Prerequisites: List all persons using the `list /all` command. Ensure there are multiple persons in the list.
   2. Test case: `delete /contact 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   Other incorrect delete commands to try:
    1. Test case: `delete` (missing flag and index)<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.
    2. Test case: `delete /contact` (missing index)<br>
       Expected: Similar to previous.
    3. Test case: `delete /contact 0` (invalid index)<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.
    4. Test case: `delete /contact x` where x is larger than the list size (invalid index)<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

2. Deleting a person associated with an itinerary
    1. Prerequisites: Ensure a person is associated with an itinerary (e.g. person 1 linked to itinerary 2). Ensure the person is displayed in the person list (e.g. Using the `list /all` command).
    2. Test case: `delete /contact x` (where x is the person's index in the displayed list)<br>
       Expected: Contact is deleted.

   Follow-up:
    1. Ensure the itinerary is displayed in the itinerary list (e.g. Using the `list /all` command).
    2. Test case: `show y` (where y is the associated itinerary's index in the displayed list)<br>
       Expected: Deleted contact no longer appears in the itinerary’s associated contact list.

3. Deleting a person when only some persons are being shown
    1. Prerequisites: Use a filtering command (e.g. `list /vendor`, `list /client`) to display a subset of the full contact list. Ensure there is at least one person in the list.
    2. Test case: `delete /contact 1`<br>
       Expected: First contact is deleted from the filtered list, not the full list.

4. Deleting a person when no persons are being shown
    1. Prerequisites: Hide all persons and list all itineraries using the `list /itinerary` command.
    2. Test case: `delete /contact 1`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

### Deleting an Itinerary

These test cases follow the same steps as manual testing for [Deleting a person](#deleting-a-person), with the following differences:
- The command format is `delete /itinerary x`, where x is the index of the itinerary in the displayed list.
- All expected outcomes remain the same, with references to “person” replaced by “itinerary”.
- The scenario _Deleting a person associated with an itinerary_ does not apply.

### Saving Data

1. Dealing with missing data file

    1. Prerequisites: Data file is not in the `data` folder
    2. Test case: `tripscribe.json` is missing from `data` folder on launch. <br>
       Expected: TripScribe should still launch and function like normal, but with default set of data in view.

2. Dealing with incorrect fields in data file
    1. Prerequisites: Data file is in data folder
    2. Test case 1: One field in a `Person` is in the wrong format. <br>
       Example: `"role" : ""`<br>
       Expected: The person will not appear in the contacts panel when TripScribe is launched. <br>The data file is updated such that itineraries previously associated with the contact will no longer store the contact's `Id` under its client or vendor lists.<br>
       "Illegal value found in field of a contact entry, skipping." is logged into terminal.
    3. Test case 2: One field in an `Itinerary` is in the wrong format. <br>
       Example: `"startDate : "09-12-2026"`<br>
       Expected: The itinerary  will not appear in the itinerary panel when TripScribe is launched. <br>
       "Illegal value found in field of an itinerary entry, skipping." is logged into terminal.
    4. Test case 3: Multiple fields in a `Person` is in the wrong format. <br>
       Example: `"role" : ""` ,`"phone" : "98765432"`<br>
       Expected: Similar to test case 1.
    5. Test case 4: Multiple fields in an `Itinerary` is in the wrong format. <br>
       Example: `""startDate" : "09-12-2026"` , `"endDate" : "15 December 2026"`<br>
       Expected: Similar to test case 2.

## **Appendix: Effort**

### Difficulty and Challenges

1. AB3 handles only 1 type of entity. TripScribe handles 2, Person and Itinerary, making it more complex.
2. Required greater understanding of how AB3 stores contacts and conversion to JSON in order to apply it to our Itinerary implementation.

### Effort and Achievements

1. Higher than average implementation effort from our team due to larger scope.
2. Enhancement of existing features (i.e. `add` becomes `addc` & `addi`), and addition of new features to improve product (i.e. `show`).

## **Appendix: Planned Enhancements**

Team size: 4

1. Adding contacts to existing itineraries using the `edit` command.
