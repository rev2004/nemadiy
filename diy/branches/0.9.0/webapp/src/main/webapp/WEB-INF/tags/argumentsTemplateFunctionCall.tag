<%@ tag isELIgnored="false" %>
<%@ attribute name="plainTemplate" required="true" type="org.imirsel.nema.model.VanillaPredefinedCommandTemplate"%>
<%@ attribute name="niceParams" required="true" type="org.imirsel.nema.webapp.model.NiceParams"%>
<%@ attribute name="supportedInputFiles" required="true" type="java.util.List"%>
<%@ attribute name="supportedOutputFiles" required="true" type="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"   prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

	
	
	
	<fieldset >
			<label class="label">Environment Variables:</label>
			<input type="button" value="+" onclick="add($('envMain'),$('envParam1'),2)" />
			<br />
			<div id='envMain'>
				<c:forEach items="${plainTemplate.environmentMap}" var="item">
					<div id='envParam0'>
						<input name="variable" type="text" value="${item.key}" />
						=
						<input name="value" type="text" value="${item.value}" />
						<input type="button" value="-" onclick="removeNode(this,'envParam1')" />
					</div>
				</c:forEach>
				<div id='envParam1'>
					<input name="variable" type="text" />
					=
					<input name="value" type="text" />
					<input type="button" value="-" onclick="removeNode(this,'envParam1')" />
				</div>
			</div>

		</fieldset>



	<fieldset>
			<c:set var="nums">1,2,3,4</c:set>
			<div>
				<label class="label fixed">Input Files:</label>
					<select name="inputNo" id='inputParam'>
						<option value="">Select</option>
						<c:forEach items="${supportedInputFiles}" var="file">
							<option value="${file.value}">${file.name}</option>
						</c:forEach>
					</select>
				<label class="label "> Index:</label>		
					<select name="inputOrder" id='inputParamOrder'>
						<c:forEach items="${nums}" var="num">
							<option value="${num}">${num}</option>
						</c:forEach>
					</select>
				<input type="button" value="+" 
					onclick="insertAtCursor($('functionCall'),'$i',$('inputParam'),$('inputParamOrder'));" />
			</div>
			<div>
				<label class="label">Output Files:</label>
					<select name="outputNo" id='outputParam'>
						<option value="">Select</option>
						<c:forEach items="${supportedOutputFiles}" var="file">
							<option value="${file.value}">${file.name}</option>
						</c:forEach>
					</select>
				<label class="label "> Index:</label>		
					<select name="outputOrder" id='outputParamOrder'>
						<c:forEach items="${nums}" var="num">
							<option value="${num}">${num}</option>
						</c:forEach>
					</select>
					
				<input type="button" value="+" 
					onclick="insertAtCursor($('functionCall'),'$o',$('outputParam'),$('outputParamOrder'));" />
			</div>
			<div>
				<input type="button" value="Scratch Directory" 
					onclick="insertStrAtCursor($('functionCall'),'$s');" /></div>
			<div><label class="label">Function Call: </label></div>
			<form:textarea path="functionCall" id="functionCall" cols="70" rows="20"/>
			<div><input type="button" value="Clear" onclick="$('functionCall').value='';"/></div>
		</fieldset>
