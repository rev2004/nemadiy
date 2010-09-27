<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Contributor</title>
        <link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/dojo/1.4/dijit/themes/tundra/tundra.css"/>
        <script  djConfig="parseOnLoad:true, isDebug:true" type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/dojo/1.4/dojo/dojo.xd.js"></script>

        <script type="text/javascript">
            startWaiting=false;
            function search(keyword){
                if ((keyword)&&(keyword.length>1)){
                    startWaiting=true;
                    dojo.xhrGet(         {
                        url : "<c:url value='/get/MirexManager/contributorList.json'/>",
                        content:{
                            str:keyword
                        },
                        handleAs : "json",
                        load : function(data) {
                            dojo.byId('search-results').innerHTML="";
                            contributors=data.contributorList;
                            for (index in contributors){
                                person=contributors[index];
                                str="";
                                if (person.title){str+=person.title+" ";}
                                str+=person.lastname+", "+person.firstname;
                                str+="<input type=button onclick='window.opener.addContributor("+person.id+")' value='Add'></input>"
                                str+="<br/>";
                                str+="<small>";
                                if (person.unit) {str+=person.unit+", ";}
                                if (person.department) {str+=person.department+", ";}
                                if (person.organization){str+=person.organization;}
                                str+="<small> "
                                dojo.create("div",{innerHTML:str,style : {backgroundColor:"green",borderStyle:"groove"} },"search-results");
                            }
                        }

                    });
                };
            }
        </script>
    </head>
    <body>

        <div style="padding:10px">
            <h2>Search</h2>

            <input type="text" id="contrib-search" value="search for contributors" onchange="search(this.value)" onkeyup="search(this.value)"/>
            <div id="search-results"></div>
            <p>Can't find the person you're looking for?
                <a href="<c:url value='/get/MirexManager/addContributor.frag'/>">Create a new Contributor Profile</a> for them.</p>
            <input type="button" onclick="window.close()" value="Close Window"/>
        </div>
    </body>
</html>