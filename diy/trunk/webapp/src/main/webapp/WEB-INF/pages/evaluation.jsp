<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="evaluationMenu.title"/></title>
    <meta name="heading" content="<fmt:message key='evaluationMenu.heading'/>"/>
    <meta name="menu" content="ExperimentMenu"/>
    
</head>

<p><fmt:message key="evaluationMenu.message"/></p>

<div class="separator"></div>

<ul class="glassList">

        <select name="">
        	<option value=""><fmt:message key="evaluationMenu.featureExtractor"/></option>
	   		<option value="runExecute.jsp?jAudio">IMIRSEL MIREX05 Genre Evaluator </option>
			<option value="run.jsp?marsy">Somebody else`s Genre Evaluator</option>
			<option value="run.jsp?marsy">IMIRSEL mood Evaluator</option>
																																			
	     </select>	    


         
         <select name="">
         	<option value=""><fmt:message key="evaluationMenu.dataset"/></option>
	   		<option value="mirex05">Marsyas MIREX05 genre classification results</option>
			<option value="custom">Your genre classification results </option>	
			<option value="custom">Your mood classification results </option>																												
	     </select>	
                
   
</ul>

	
	

	
	<input type="submit" value="Next" />
	