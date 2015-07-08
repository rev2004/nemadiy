# Using Artifactory to Add Dependencies to Projects #

## Introduction ##

In order to add third party dependencies to the POM, we will be using artifactory http://nema-dev.lis.illinois.edu/artifactory/ to search for the dependency and modify the project's POM to include the dependency. If your changes should remain with the project then check in the pom.xml file after modifying and testing it.


## Instructions ##

  1. Go to http://nema-dev.lis.illinois.edu/artifactory/ and use the search box in the top right hand corner of the page to search for the dependency you want to include in the POM file (e.g. enter mysql when searching for a mysqlconnector jar file). <br><br>
<ol><li>Select the POM file for the dependency you want to include in the results (if it is not listed contact Amit and/or Andrew Shirk).  <br><br>
</li><li>From the contextual menu that will pop up, select <i>Show In Tree</i>.  <br><br>
</li><li>Click on the <b>POM view</b> tab (Note that this ma  not be visible initially, depending on your window size, so use the white triangle to get the next set of tab) and copy the<br>
<pre><code>&lt;dependency&gt;<br>
...<br>
&lt;/dependency&gt;<br>
</code></pre>
</li><li>Open the POM file for the project that you wish to add the dependency to (either in Eclipse or a text editor).<br>
<ul><li>In a text editor add the dependency XML fragment to the <code>&lt;dependencies&gt;</code> list.<br>
</li><li>Alternatively, in the eclipse Maven POM file editor fill in the fields using the data from the <code>&lt;dependency&gt;</code> XML fragment. <br><br>
</li></ul></li><li>Finally add a scope to the dependency.<br>
<ul><li>Usually the scope would be <b>compile</b> but there are times you would want the scope to be provided (for example servlet api is provided by the servlet container), or if your dependency is a database driver (where you compile against JDBC rather than the specific driver), you can use the scope <b>runtime</b>.<br>
</li><li>Further details on scopes are available at: <a href='http://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html#Dependency_Scope'>http://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html#Dependency_Scope</a>