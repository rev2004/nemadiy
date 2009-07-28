<div id="main">
	<div id="login">
	    <form method="POST" action="/admin/action-login.jsp">
	      <fieldset>
	        <p>Welcome to Nema Exactotron 1000 !</p>
	        <label for="name">Username: </label><br/>
	        <input type="text" id="username" name="username"/><br/>
	        <label for="password">Password: </label><br/>
	        <input type="password" id="password" name="password" /><br/>
	        <input type="hidden" id="meandrehost" name="meandrehost" value="nema.lis.uiuc.edu" /><br/>
	        <input type="hidden" id="meandreport" name="meandreport" value="1854" /><br/><br/>
	        <input type="submit" id="submit" value="Login &raquo;" />
	     </fieldset>
	    </form>
	</div>
</div>