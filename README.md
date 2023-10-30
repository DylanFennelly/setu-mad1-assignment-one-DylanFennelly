[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/zN0YBtu9)
# setu-mad1-assignment-one

# Setup Instructions

Please fill in the following information and `commit` and `push`

* Student Number: 20093427
* Name: Dylan Fennelly
* GitHub username: DylanFennelly


----

# Dungeons & Dragons Character Manager

### A simple Kotlin-based terminal application to create and manage Dungeons & Dragons characters

---
## Functionality
The application allows for the creation, listing, update, deletion, and searching of D&D characters and items. This guide will take you through the application's functionality and how to best use its features.

### Running the application

---
To run the application, simply build it into a .jar file and execute the jar at the command line.

If you are using an IDE, you can also run the application in the IDE's terminal.

This application works best with terminals that support ANSI colour codes.

### Main Menu

---
Upon launching the application, you will be brought to the Main Menu, which is laid out as such:
```
Main Menu
 1. Add Character
 2. Update Character
 3. List All Characters
 4. Search Characters
 5. Delete Character

 6. Application Settings

 -1. Exit Application
```

From here, you can make a number of selections by entering the number shown to the left of the option:
- [**Add Character**](#add-character) - Create a new character
- [**Update Charcter**](#update-character) - Update an existing character
- [**List All Characters**](#list-all-characters) - Display all saved characters
- [**Search Characters**](#search-characters) - Search for characters by specific parameters
- [**Delete Character**](#delete-character) - Delete an existing character
- [**Application Settings**](#application-settings) - Manage applicaion settings and data

Entering -1 at this menu will exit the application.

### Add Character

---
Entering into the add character menu, you are presented with a blank character sheet and a number of options:
```
==============    Create New Character    ==============
While entering values, enter '-1' to return to this menu
========================================================

============   Current character attributes   ============
┌──────┬──────┬───────┬───────┬─────┬─────┬─────┬─────┬─────┬─────┬────────────┬────────┬────┬───────────┐
│ Name │ Race │ Class │ Level │ STR │ DEX │ CON │ INT │ WIS │ CHA │ Background │ Max HP │ AC │ No. Items │
├──────┼──────┼───────┼───────┼─────┼─────┼─────┼─────┼─────┼─────┼────────────┼────────┼────┼───────────┤
│      │      │       │   1   │ 10  │ 10  │ 10  │ 10  │ 10  │ 10  │            │   0    │ 10 │     0     │
└──────┴──────┴───────┴───────┴─────┴─────┴─────┴─────┴─────┴─────┴────────────┴────────┴────┴───────────┘
Select attribute
 1. Name
 2. Race
 3. Class
 4. Level
 5. Ability Scores
 6. Background

 7. Create character
 -1. Cancel character creation
```
Entering the number shown next to an attribute will allow you to enter a value for that attribute. At any point, when prompted for an input, **-1** can be entered to return to the previous menu.

The character has a number of variables:
- **Name** - The character's name
- **Race** - The character's race, from a selection of nine
- **Class** - The character's class, from a selection of twelve
- **Level** - The character's level, starting at 1 and maxing out at 20
- **Strength (STR)** - A measure of the character's physical strength. All ability scores must be between 1 and 30
- **Dexterity (DEX)** - A measure of the character's agility.
- **Constitution (CON)** - A measure of the character's endurance.
- **Intelligence (INT)** - A measure of the character's knowledge.
- **Wisdom (WIS)** - A measure of the character's mental fortitude.
- **Charisma (CHA)** - A measure of the character's people-skills.
- **Background** - The character's background - who they are/were - from a selection of thirteen
- **Max HP** - The maximum amount of Health Points (HP) the character has. This is automatically calculated from a combination of the character's class, level, and constitution score.
- **Armour class (AC)** - The strength of the character's defences. This can be set after character creation in the [update menu](#update-character).
- **Inventory** - The items the character has in their possession. These can be added after character creation in the [update menu](#update-character).

As values are input, the character sheet at the top of the menu will fill out with the entered details.

When "Create character" is selected, the application will check if all the required character attributes have been filled out. If they haven't, they system will highlight the attributes that still need to be entered and return to the add menu. If all required attributes have been entered, a prompt will appear to confirm character creation. After entering "yes", the character will be created.

Entering to "Cancel character creation" will present a prompt, asking for confirmation of character creation cancellation and warning that all unsaved work will be lost. Deny at this prompt will return to the add menu, while confirming will abort character creation and return to the [main menu](#main-menu). 

### Update Character

---
Entering into the update character menu, you are presented with a list of saved characters with highlighted ID values. If no characters have been created yet, an error will be printed instead and the application will return to the [main menu](#main-menu).

Entering the ID of the character you wish to edit presents you with the edit menu, displaying the character's attributes and a list of options for editing the character.

```
================    Update Character    ================
While entering values, enter '-1' to return to this menu
========================================================

============   Current character attributes   ============
┌─────────┬───────┬───────────┬───────┬─────┬─────┬─────┬─────┬─────┬─────┬────────────┬────────┬────┬───────────┐
│  Name   │ Race  │   Class   │ Level │ STR │ DEX │ CON │ INT │ WIS │ CHA │ Background │ Max HP │ AC │ No. Items │
├─────────┼───────┼───────────┼───────┼─────┼─────┼─────┼─────┼─────┼─────┼────────────┼────────┼────┼───────────┤
│ Rylanor │ Human │ Barbarian │   1   │ 18  │ 14  │ 16  │  7  │ 10  │  8  │ Outlander  │   15   │ 10 │     2     │
└─────────┴───────┴───────────┴───────┴─────┴─────┴─────┴─────┴─────┴─────┴────────────┴────────┴────┴───────────┘
Select attribute to update
 1. Level        6. Background
 2. Name         7. Armour Class
 3. Race         8. Max HP Override
 4. Class        9. Manage Items
 5. Ability Scores
```

The update functionality is much the same as [add character](#add-character), with three major additional options:
- **Armour Class** - Set the character's armour class (AC)
- **Max HP Override** - Manually set the character's HP. This notifies the user that any update to the character's level, class, or CON score will overwrite this override
- [**Manage Items**](#manage-items) - A sub menu that allows for the management of a character's items.

### Manage Items

---
Upon entering into the manage items menu, the existing items for the selected character will be displayed, if there are any to display, and number of options will be presented:

```
===================   Manage Items   ===================
While entering values, enter '-1' to return to this menu
========================================================
============   Current Inventory   ============
...
Manage Items
 1. Create New Item
 2. Update Existing Item
 3. Delete Item

 -1. Return to update menu
```

- [**Create New Item**](#create-new-item) - Add a new item to the character
- [**Update Existing Item**](#update-existing-item) - Updated an existing item belonging to the character
- [**Delete Item**](#delete-item) - Delete one of the character's items

#### Create New Item

---
Entering into the create item menu, a blank item block is printed with a number of options:
```
================    Create New Item     ================
While entering values, enter '-1' to return to this menu
========================================================

============   Current item attributes   ============
┌──────┬──────┬────────┬───────────┬─────────┬──────────┐
│ Name │ Type │ Rarity │ Cost (GP) │ Attuned │ Equipped │
├──────┼──────┼────────┼───────────┼─────────┼──────────┤
│      │      │        │     0     │  false  │  false   │
├──────┴──────┴────────┴───────────┴─────────┴──────────┤
│                      Decription                       │
├───────────────────────────────────────────────────────┤
│                                                       │
└───────────────────────────────────────────────────────┘
Select item attribute
 1. Name
 2. Type
 3. Rarity
 4. Description
 5. Cost
 6. Attunement
 7. Equipped

 8. Create item
 -1. Cancel item creation
```

Like the [add character menu](#add-character), entering the number shown next to an attribute will allow you to enter a value for that attribute. At any point, when prompted for an input, -1 can be entered to return to the previous menu.

Item has a number of variables:
- **Name** - The item's name
- **Type** - The type of item (e.g. Weapon, Armour, Wondrous, etc.)
- **Rarity** - The item's rarity: Common, Uncommon, Rare, Very Rare, or Legendary
- **Cost** - The item's cost, in gold pieces (GP)
- **Attunement** - Whether or not the item needs to be atttuned to. Usually reserved for magical items
- **Equipped** - Whether or not the item is currently equipped by the character

As values are input, the item block at the top of the menu will fill out with the entered details.

When "Create item" is selected, the application will check if all the required item attributes have been filled out. If they haven't, they system will highlight the attributes that still need to be entered and return to the add menu. If all required attributes have been entered, a prompt will appear to confirm item creation. After entering "yes", the item will be created and added to the character.

#### Update Existing Item

---
Upon entering into the manage items menu, you will be prompted for the ID of the item to edit. Once a valid ID is entered, the chosen item's details will be displayed and a confirmation prompt will ask for confirmation to delete the item. Deny at this prompt will return to the [item management menu](#manage-items), while confirmation will delete the item and remove it from the character.

#### Delete Item

---
Entering to "Cancel item creation" will present a prompt, asking for confirmation of item creation cancellation and warning that all unsaved work will be lost. Deny at this prompt will return to the item creation menu, while confirming will abort item creation and return to the [update menu](#update-character).


### List All Characters

---

This will list all the saved characters, and display them in a table.

### Search Characters

---

Entering into the search character menu, you are presented with a list of attributes to search by:

```
=================   Search Characters  =================
While entering values, enter '-1' to return to this menu
========================================================
Select attribute to search by
 1. Level        6. Background
 2. Name         7. Armour Class
 3. Race         8. Max HP
 4. Class        9. ID
 5. Ability Scores

 -1. Return to main menu
```

Each attribute allows you to search for characters matching the input search parameter.

Numerical values (level, ability scores, armour class, max HP) will additionally ask if you want to search for values greater than, less than, or equal to the input value.

### Delete Character

---

Entering into this menu will display a list of all characters in the system and will prompt for the ID of the character to delete. Upon entering a valid ID, the character sheet will be displayed and confirmation will be requested to delete the character.

If confirmation is granted, you will then be further asked to type the name of the character to doubly-confirm character deletion. Upon correct input, the character will be deleted. **-1** can be entered at this prompt to cancel deletion.

### Application settings

---

The application settings menu contains one option: Clear Save Data

This function clears all save data, deleting all characters and items. The application will ask for double confirmation of this deletion, firstly with a yes/no prompt, and secondly by asking the user to type 'DELETE' to confirm delete.

### Dependencies

---
The application contains a number of dependencies:
- **SLF4J-simple:1.6.1** - A logging facade for JVM languages
- **Kotlin-Logging:1.6.22** - A logger framework for Kotlin
- **Gson:2.8.9** - A JSON serialize/deserialize library for JVM languages
- **Mordant:2.2.0** - A terminal output formatting library for Kotlin

### Repository branching model

---

The repository contains a number of branches, that flowed as such

main -> refactor -> main -> character -> main -> user-exp -> main -> items -> main -> search-filtering -> main

- **refactor** - Refactoring the base lab work project to use 'character' instead of 'placemark'
- **character** - The introduction of the character dataClass and CRUD functionality
- **user-exp** - Implementation of Mordant, UI formatting, and allowing for most prompts to be returned from at any time
- **items** - Introduction of second dataClass - ItemModel - with parent-child relation with character + CRUD for Items
- **search-filtering** - Updating of search functionality to allow searching by specific attributes