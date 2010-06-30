<%@ include file="/common/taglibs.jsp"%>

<menu:useMenuDisplayer name="Velocity" config="cssHorizontalMenu.vm" permissions="rolesAdapter">
<ul id="primary-nav" class="menuList" style="z-index:20000;">
    <li class="pad">&nbsp;</li>
   <c:if test="${pageContext.request.remoteUser == null}">
 	<menu:displayMenu name="MainMenu"/>
 	</c:if>

	<menu:displayMenu name="JobMenu"/>
    <menu:displayMenu name="UserMenu"/>
    <menu:displayMenu name="AdminMenu"/>
    <menu:displayMenu name="Logout"/>
    <menu:displayMenu name="helpMenu"/>

</ul>
</menu:useMenuDisplayer>