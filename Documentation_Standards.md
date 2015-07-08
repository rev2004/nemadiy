Substantial portions of this document are taken directly from the Ptolemy II project documentation (Volume 1: Introduction to Ptolemy II). The source code documentation in the Ptolemy II project is exemplary, and should serve as a baseline from which to judge the quality of our own comments.

## Introduction ##

Good comments are essential to easily readable and maintainable code. Good comments actually extend the life of code because well-commented code is more likely to be reused rather than rewritten. For open source projects, well-documented code communicates to casual observers and potential contributors that the project is committed to producing mature, quality code.

It is critical that documentation be written at the time of development when details are still fresh in your mind, rather than six months later as an afterthought. Likewise, when code is modified and/or extended, the comments and other documentation about the code should be immediately updated to reflect the changes.

Code comments fall into two categories:

  * Javadoc comments, which become part of the auto-generated documentation.
    * Provides an overview of the whole project (overview.html)
    * Explains the purpose of packages (package-info.java)
    * Explains the interface contract of classes (in each class file)
  * Code comments, which are available only by looking at the source code.
    * Explains how the code works

**All Javadoc and code comments should be complete thoughts, capitalized at the beginning and with a period at the end. Spelling and grammar should be correct.**

## Javadoc ##

Javadoc is a program distributed with the Java SDK that generates HTML documentation files from Java source code files. Javadoc comments begin with `/``*``*` and end with `*``/`. The comment immediately preceding a code construct (method, member, or class) documents that code construct. Javadoc documentation should be provided for all projects, packages, classes, interfaces, enums and all public, protected, and private members and methods contained therein.

Please take the time to familiarize yourself with the finer points of Javadoc at this URL: http://java.sun.com/javase/6/docs/technotes/guides/javadoc/index.html.

When writing Javadoc comments, pay special attention to the first sentence of each Javadoc comment. This first sentence is used as a summary in the Javadocs. It is extremely helpful if the first sentence is a cogent and complete summary.

Javadoc comments can include embedded HTML formatting. Please use HTML comments sparingly, and make sure the markup is well-formed. Any code included in comments should be wrapped in code tags: `<code>i = 1</code>`.

The Javadoc program gives extensive diagnostics when run on a source file. Comments should be formatted until there are no Javadoc warnings.

## Class Documentation ##

The class documentation is the Javadoc comment that immediately precedes the class definition line. It is a particularly important part of the documentation. It should describe what the class does and how it is intended to be used. When writing it, put yourself in the mind of the user of your class. What does that person need to know to clearly understand what the class is for? A clear explanation is especially important when a person doesn't have a clear understanding of the larger system. **Usually, it is of little help to explain implementation details of the class.**

A class may be intended to be a base class that is extended by other programmers. In this case, there may be two distinct sections to the class documentation. The first section should describe how a user of the class should use the class. The second section should describe how a programmer can meaningfully extend the class. Only the second section should reference protected members or methods. The first section has no use for them. Of course, if the class is abstract, it cannot be used directly and the first section can be omitted.

Each class comment should also include the following Javadoc tags:

  * @author
    * The @author tag should name the creator of the class or interface. For example:
      * @author shirk
    * SVN logins should be used for author/s, not the full name. If a class has been authored by multiple developers, their SVN logins should be included as well, on subsequent lines:
      * @author shirk
      * @author amitku
  * @since
    * The @since tag refers the release that the class first appeared in. For example:
      * @since 0.5 beta
    * Note that the @since tag can also be used when a method is added to an existing class, which will help users notice new features in older code.

## Constructor Documentation ##

Constructor documentation usually begins with the phrase "Constructs an instance that ..." and goes on to give the properties of that instance. As with class documentation, it is important to communicate the specifics of the constructor behavior in the first sentence as this is the one that will show up in method summary.

## Method Documentation ##

Method documentation needs to state what the method does and how it should be used. It is almost never useful to simply provide an English translation of the code. This is a good example of the type of method documentation that is expected:

```
/**
 * Marks this object invalid, indicating that when a method
 * is next called to get information from the object, that
 * information needs to be reconstructed from the database.
 */
public void invalidate() {
   valid = false;
}
```

By contrast, here is a poor method comment:

```
/**
 * Sets the variable valid to false.
 */
public void invalidate() {
   valid = false;
}
```

While this certainly describes what the method does from the perspective of the coder, it says nothing useful from the perspective of the user of the class, who cannot see the (presumably private) variable valid nor how that variable is used. On closer examination, this comment describes how the method is accomplishing what it does, but it does not describe what it accomplishes.

Here is an even worse method comment:

```
/**
 * Invalidates this object.
 */
public void invalidate() {
   valid = false;
}
```

This is no more helpful than reading the method name.

Comments for base class methods that are intended to be overridden should include information about what the method generally does, plus information that a programmer may need to override it. If the derived class uses the base class method (by calling `super.methodName()`), but then appends to its behavior, then the documentation in the derived class should describe both what the base class does and what the derived class does.

## Tags in method documentation ##

All methods (default, private, protected, public) should include the following Javadoc tags:

  * `@param`
    * One should be provided for each parameter no matter how trivial it seems at the time.
    * The annotation should meaningfully explain what the method does, and should address the larger implications of its use. (See previous `invalidate()` example)
    * Although the annotations do not need to be complete sentences, they should always begin with a capitol letter and end it with a period.
  * `@return`
    * Unless the return type is void, this tag should always be used.
    * As with `@param`, the comment should meaningfully explain what is being returned.
    * `@returns` is not a valid Javadoc tag.
  * `@throws`
    * One should be provided for each checked exception that is thrown.
    * One should also be provided for each runtime exception that might be thrown as a result of some parameter that has been passed to the method. Such information is important to document because it is part of the entire interface contract. For example, if the value of a certain numeric parameter is checked by the method code to be within a certain range, and an `IllegalArgumentException` is thrown if it is not, this exception needs to be documented and the legal range of values should be specified.
    * An `@throws` tag should read like this:
      * `@throws IllegalArgumentException if the value of the <code>cost</code> parameter is a negative number.`
      * Always start the comment with "if blah blah..."
      * Note the lower case "if" at the beginning of the documentation and the period at the end. This is the only Javadoc case where a sentence should begin with a lower case letter. This is because the Javadoc tool converts the comment to a properly formed and punctuated sentence when it compiles the documentation.
      * In the case of an interface or base class that does not throw the exception, documentation should still be provided. The exception still has to be declared so that derived classes can throw it:
        * `@throws SomeException in some derived classes. Derived classes may throw it if such and such happens.`


## Referring to methods in comments ##

When referring to methods and members of the current class, as well as methods and members of outside classes, try to always use the @link tag rather than just using the method or member's name. When done consistently and properly, renaming and refactoring of code is much simpler because Eclipse will automatically change the name of the method or member everywhere it is used with the link tag. Maintenance therefore is automatically performed by the IDE, thus saving a ton of time. For example, a link to a local method would look like:

```
/** Unlike the {@link #foo(String)} method, this method... */
```

A link to an outside method would look like:

```
/** Unlike the {@link org.imirsel.nema.flowservice.FlowService#getJob(long)} method, this method... */
```

Note the octothorpe (# sign) instead of a dot operator between the class and the member.

Eclipse provides typing assistance when creating links such as these. Start typing your class name, then type ctrl+space to activate the auto-completion. If there is ambiguity, Eclipse will prompt you to make a selection from a menu of options.

## Referring to classes in comments ##

When referring to domain objects, entities, and other types of classes in comments, try to always use the @link tag rather than just using the class's name. When done consistently and properly, renaming and refactoring of code is much simpler because Eclipse will automatically change the name of the class everywhere it is used with the link tag. Maintenance therefore is automatically performed by the IDE, thus saving a ton of time. For example:

```
/**
  * Kill a running {@link Job}.
  * 
  * @param jobId Unique ID of the {@link Job} to kill.
  * @throws IllegalStateException if the {@link Job} is not running.
  */
public void abortJob(long jobId) throws IllegalStateException;
```

Here, `Job` is an entity. The `Job` type is not referenced in the method itself, only the Javadoc. But by using the @link tag, if the `Job` class is renamed to `JobDetails` for example, all Javadoc references to `Job` will be updated with the new name.

In the above example, assume that the `Job` type has been imported into the class. Otherwise, the fully qualified name of the class would be required for Javadoc to successfully create the link when the Javadoc was created.