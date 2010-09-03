<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Contributor</title>
        <script type="text/javascript">
            function search(keyword){
                dojo.xhrGet(         {
                    url : "<c:url value='/get/MirexManager.contributorList.json'/>",
                    handleAs : "json",
                    load : function(data) {
                        for (person in data.contributorList){
                            str="";
                            if (data.title){str+=data.title+" ";}
                            str+=data.lastname+", "+data.firstname;
                            str+="<input type=button onclick='window.opener.addContributor("+data.id+")'>Add</input>"
                            str+="<br/>";
                            str+="<small>";
                            if (data.unit) {str+=data.unit+", ";}
                            if (data.department) {str+=data.department+", ";}
                            if (data.organization){str+=data.organization;}
                            str+="<small> "
                            dojo.create("div",{innerHTML:str,style : {backgroundColor:"green",borderStyle:"groove"} },"search-results");
                        }
                    }

                });
            };

        </script>
    </head>
    <body>

        <div style="padding:10px">
            <h2>Search</h2>

            <input type="text" id="contrib-search" value="search for contributors" onchange="search(this.value)"/>
            <div id="search-results"></div>
            <p>Can't find the person you're looking for?
                <a href="<c:url value='/get/MirexManager/addContributor.frag'/>">Create a new Contributor Profile</a> for them.</p>
            <input type="button" onclick="window.close()" value="Close Window"/>
        </div>
    </body>
</html>