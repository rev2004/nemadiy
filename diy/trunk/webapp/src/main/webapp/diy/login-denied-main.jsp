<div id="main">
	<div id="login">
	    <form method="POST" action="/admin/action-login.jsp">
	      <div id="errorMsg" >
	      	${param.errormsg}
		</div>
		<fieldset>
	        <p>Meandre is a semantic web-driven data flow execution environment. Meandre is built around semantic-web technologies. Please enter the your user name and password for the Meandre repository you want to access.</p>
	        <label for="name">Username: </label><br/>
	        <input type="text" id="username" name="username"/><br/>
	        <label for="password">Password: </label><br/>
	        <input type="password" id="password" name="password" /><br/>
	        <label for="meandrehost">Meandre engine: </label><br/>
	        <input type="input" id="meandrehost" name="meandrehost" value="localhost" /><br/>
	        <label for="meandreport">Meandre port: </label><br/>
	        <input type="input" id="meandreport" name="meandreport" value="1714" /><br/><br/>
	        <input type="submit" id="submit" value="Login &raquo;" /></div>
	     </fieldset>
	    </form>
	</div>
</div>
  