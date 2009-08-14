<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="featureExtractionMenu.title"/></title>
    <meta name="heading" content="<fmt:message key='featureExtractionMenu.heading'/>"/>
    <meta name="menu" content="ExperimentMenu"/>
    
</head>

<p><fmt:message key="featureExtractionMenu.message"/></p>

<div class="separator"></div>

<ul class="glassList">

        <select name="">
        	<option value=""><fmt:message key="featureExtractionMenu.featureExtractor"/></option>
	   		<option value="runExecute.jsp?jAudio">jAudio Feature Extractor</option>
			<option value="run.jsp?marsyas">Marsyas</option>																												
	     </select>	    

 
         
         <select name="">
         	<option value=""><fmt:message key="featureExtractionMenu.dataset"/></option>
	   		<option value="mirex05">MIREX 2005 Audio Genre Classification Task Dataset</option>
			<option value="custom">Your custom saved dataset</option>																												
	     </select>	
                


    
    
    
</ul>

	<input type="submit" value="Next" />