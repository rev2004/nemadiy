<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="classificationMenu.title"/></title>
    <meta name="heading" content="<fmt:message key='classificationMenu.heading'/>"/>
    <meta name="menu" content="ExperimentMenu"/>
    
</head>

<p><fmt:message key="classificationMenu.message"/></p>

<div class="separator"></div>

<ul class="glassList">

        <select name="">
        	<option value=""><fmt:message key="classificationMenu.featureExtractor"/></option>
	   		<option value="runExecute.jsp?jAudio">jMIR Genre Classifier</option>
			<option value="run.jsp?marsy">Marsyas Tag Classifier</option>
			<option value="run.jsp?marsyas">Marsyas Mood Classifier</option>
			<option value="run.jsp?marsyas">M2K General Classifier</option>
			
																															
	     </select>	    


         
         <select name="">
         	<option value=""><fmt:message key="classificationMenu.dataset"/></option>
	   		<option value="mirex05">MIREX 05 features extracted by jAudio on 08.11.09</option>
			<option value="custom">Jamendo  features extracted by XXX on date YYYY</option>																												
	     </select>	
                
  
    
    

</ul>

	
	
		<input type="submit" value="Next" />
	

	