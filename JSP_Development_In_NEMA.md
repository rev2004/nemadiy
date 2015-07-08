Adding New Pages:

JSP pages are in /src/main/webapp/WEB-INF

  * Create the new page xxx.jsp
  * include tag lib `<%@ include file="/comon/taglibs.jsp" %>`
  * Type your JSP or html content and save the file.

To make the page accessible

  * URL map the page in dispatcher-servlet.xml in the `<bean id="urlMapping">` in the property attribute named mappings
  * If it is a JSP page then the mapping is automatic because the `/*.jsp` is mapped to a `viewResolver`
  * If it is a JSP page but you want a different mapping such as `/xxx.html` to resolve to `/xxx.jsp` -map the `/xxx.jsp` to the `filenameController`.
  * By default all the pages in the WEB-INF/pages require authentication. You can modify the behavior by editing the security.xml file.
  * If you want the pages to be world readable -place them in the root /src/main/webapp


To add the page as the top level menu in the application

  * Edit menu.xml file and append `<menu:displayMenu>` element
  * Edit menu-config.xml and append `<Menu>` item with the link to the page mapping, appropriate title and roles.


The page/menu title, page content and all the static strings are defined in resource bundles for internationalization. The resource bundles are in /src/main/resources folder. Edit the `ApplicationResources.properties` files and add the various titles.