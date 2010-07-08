<%@ tag isELIgnored="false" %>
<%@ attribute name="plainTemplate" required="true" type="org.imirsel.nema.model.VanillaPredefinedCommandTemplate"%>
<%@ attribute name="niceParams" required="true" type="org.imirsel.nema.webapp.model.NiceParams"%>
<%@ attribute name="supportedInputFiles" required="true" type="java.util.List"%>
<%@ attribute name="supportedOutputFiles" required="true" type="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"   prefix="c" %>

	
	
	
	<fieldset id="pt1">
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

		<fieldset id="pt1">
			<label class="label">Input Files:</label>
			<input type="button" value="+" onclick="addSelect($('inputMain'),$('inputParam1'))" />
			<br />
			<div id='inputMain'>
				<c:forEach items="${niceParams.inputs}" var="item">
					<div id='inputParam0'>
						<select name="input">
							<option value="">Select</option>
							<c:forEach items="${supportedInputFiles}" var="file">
								<option value="${file.value}"	
									<c:if test="${item==file.value}"> selected="selected" 	</c:if>
									>
									${file.name}
								</option>
							</c:forEach>
						</select>
						<input type="button" value="-" onclick="removeNode(this,'inputParam1')" />
					</div>
				</c:forEach>
				<div id='inputParam1'>
					<select name="input">
						<option value="">Select</option>
						<c:forEach items="${supportedInputFiles}" var="file">
							<option value="${file.value}">${file.name}</option>
						</c:forEach>
					</select>
					<input type="button" value="-" onclick="removeNode(this,'inputParam1')" />
				</div>
			</div>
		</fieldset>
		<fieldset id="pt1">
			<label class="label">Output Files:</label>
			<input type="button" value="+" onclick="addSelect($('outputMain'),$('outputParam1'))" />
			<br />
			<div id='outputMain'>
				<c:forEach items="${niceParams.outputs}" var="item">
					<div id='outputParam0'>
						<select name="output">
							<option value="">Select</option>
							<c:forEach items="${supportedOutputFiles}" var="file">
								<option value="${file.value}"	
									<c:if test="${item==file.value}"> selected="selected" 	</c:if>
									>
									${file.name}
								</option>
							</c:forEach>
						</select>
						<input type="button" value="-" onclick="removeNode(this,'outputParam1')" />
					</div>
				</c:forEach>
				<div id='outputParam1'>
					<select name="output">
						<option value="">Select</option>
						<c:forEach items="${supportedOutputFiles}" var="file">
							<option value="${file.value}">${file.name}</option>
						</c:forEach>
					</select>
					<input type="button" value="-" onclick="removeNode(this,'outputParam1')" />
				</div>
			</div>
		</fieldset>
		<fieldset id="pt1">
			<label class="label">Other Argument Flags:</label>
			<input type="button" value="+" onclick="add($('otherMain'),$('otherParam1'),1)" />
			<br />
			<div id='otherMain'>
				<c:forEach items="${niceParams.others}" var="item">
					<div id='otherParam0'>
						<input name="other" type="text" value="${item}" />
						<input type="button" value="-" onclick="removeNode(this,'otherParam1')" />
					</div>
				</c:forEach>
				<div id='otherParam1'>
					<input name="other" type="text" />
					<input type="button" value="-" onclick="removeNode(this,'otherParam1')" />
				</div>
			</div>
		</fieldset>
