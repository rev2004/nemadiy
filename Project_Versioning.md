Project version numbers have four parts:
**major.minor.revision-build**

**major:** Major changes indicate wide-sweeping changes to both the API and implementation classes. Changes are typically not backwards compatible.

**minor:** Minor changes indicate that some backward incompatible changes have been made since the previous version. An example of such a change would be the removal of a method from an interface.

**revision:** Revision changes indicate that some backwards compatible changes have been made to the project since the previous version. An example of such a change would be the addition of a method to an interface, or the deprecation of a method.

**build:** Build number changes will occur for each complete compilation of the code. The build tool will automatically increment this number. Maven replaces the "SNAPSHOT" string with a date string.

All projects start out with the version number: 0.1.0-SNAPSHOT

The project lead is in charge of determining when to increase the revision number.
The architect is in charge of determining when to increase the minor and major version numbers.

When a stable release is completed, the build portion of the version number should be removed: 1.0.0