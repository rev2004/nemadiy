# Introduction #

Good naming conventions and practices are as important (one might argue more important) than good code comments. Naming is perhaps the single most important factor that affects readability of code. This document is intended to address two aspects of naming: appearance and content.

# Details #

When you name a class, interface, method or constant, use a name that is, and will remain, meaningful to those programmers who must eventually read your code. Use meaningful words to create names. Avoid using a single character or generic names that do little to define the purpose of the entities they name. Use full words; do not attempt to shorten words by removing vowels. Strive for names that promote self documenting code.

Generally speaking, `CamelCase` will be used to name classes, and `lowerCamelCase` will be used to name methods and variables.

Acronyms should be avoided, but if they must be part of an identifier, capitalize only the first letter, `loadXml()` for example. If the acronym comes at the beginning of the identifier, the acronym should also start with a lowercase letter, `xmlLoader` for example.

## Packages ##

Package names should consist of only lowercase letters and periods. Abbreviations should only be used if they are commonly known or part of a defined vocabulary, i.e. "ui" for user interface. Plurals should be avoided.

All NEMA packages should begin with `org.imirsel.nema.` then the rest of the package name.

Correct:

  * `org.imirsel.nema.persistence.bean`

Incorrect:

  * `org.imirsel.nema.persistenceLayer.bean`
  * `org.imirsel.nema.persistence.beans`

## Classes ##

Class names begin with an uppercase letter and should not be pluralized unless it groups related items together. Classes represent entities, and thus should always be named using nouns, not verbs. Avoid making a noun out of a verb, example `DivideClass`. If you are having difficulty naming a class then perhaps it is a design problem and not a naming problem.

Correct:

  * `UrlParser`

Incorrect:

  * `URLParser`
  * `UrlParse`


## Interfaces ##

Like class names, interface names should be set in `CamelCase` and should not be pluralized unless it is meant to group related items together. Use nouns to name interfaces that act as service declarations, `ActionListener` for example. Use adjectives to name interfaces that act as descriptions of capabilities. Most interfaces that describe capabilities use an adjective created by tacking on "able" or "ible" suffix onto the end of a verb, Runnable or Accessible for example.

Correct:

  * `Calculable`
  * `PageableItems`

Incorrect

  * `Execute`
  * `Transfer`

## Enums ##

As of Java 5, enumerations no longer have to be simulated using a class or interface; they are now their own full-fledged type:

```
public enum Day {SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY}
```

Therefore, like classes and interfaces, their names should be set in `CamelCase`. Even though enums contain a collection of items, they should never be pluralized. This is mainly because of how they are used:

```
Day humpDay = Day.WEDNESDAY;
```

Enums contain constants, which should be always be capitalized. Because they are implicitly public, no special letter or symbol should be prepended to the constant name.

Correct:

  * `enum Size {SMALL, MEDIUM, LARGE}`
  * `enum Animal {DOG, CAT, BIRD, RABBIT}`

Incorrect:

  * `enum Roles {admin, user, guest}`
  * `enum Sweets {candyBar, sodaPop, gobstopper}`

## Methods ##

Methods perform actions. Choose method names that clearly and meaningfully describe the actions that will be performed, without including any implementation details. Sometimes a simple verb will suffice. Other times a longer phrase-like method name is needed to adequately communicate details and enhance clarity, `getJobSummary()` for example. A descriptive method is a self-documenting method. That said, don't overdo it. You should consider renaming method names over 25-30 characters long.

Methods that return a boolean type should usually be phrased as an assertion.

Method names should always be set in `lowerCamelCase`.


Correct:

  * `addEventListener()`
  * `calculateArea()`

Incorrect:

  * `spinner()`
  * `$kite()`
  * `open_file()`
  * `count_nodes()`

## Method Parameters ##

As with method names, parameter names should communicate meaningful information to the reader. Do not use single characters or abbreviations. No special characters should prefix method names. Use `lowerCamelCase` always.

Correct:

  * `pageNumber`
  * `absolutePath`

Incorrect:

  * `NumberOfTimes`
  * `c`
  * `file_path`

## Local Variables ##

The same rules for method parameters apply to local variables. However, exceptions are allowed for variables used strictly for looping through a collection, `for(int i=0;i<10;i++)` for example. Local variables referring to collections should describe what the collection contains, and should be pluralized.

Correct:

  * `productCount`
  * `isAvailable`
  * `carParts`

Incorrect:

  * `list`
  * `al`
  * `node_counter`

## Instance Variables ##

Name instance variables with great care paying special attention to reducing ambiguity. Try and place yourself in the mind of a programmer totally unfamiliar with your class implementation to help you determine if your variable name is clear enough.

Instance variables should be set in `lowerCamelCase`.

Correct:

  * `selectedFiles`
  * `currentAnalysisTechnique`

Incorrect:

  * `OpenConnection`
  * `fileDAO`
  * `color_picker`

## Public Class Constants ##

Public class constants should be set in all caps with underscores separating words. There is one exception to this rule: loggers. Visually, constants demand a great deal of attention since they are set in all caps. Because logger objects can be used heavily throughout a class, we want to minimize the visual clutter logging statements create. Therefore, logger objects should be set in lower case:

```
private static final Logger logger;
```

Correct:

  * `READ_METHOD`

Incorrect:

  * `write_method`