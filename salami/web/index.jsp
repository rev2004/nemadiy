<%-- 
    Document   : index
    Created on : May 19, 2011, 2:42:57 PM
    Author     : gzhu1
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Segmentation Plot Results - Segmentation Plots</title>

        <LINK REL=StyleSheet HREF="support/menu.css" TYPE="text/css" >
            <LINK REL=StyleSheet HREF="support/tableblue.css" TYPE="text/css" >

                <script type="text/javascript" src="support/jquery.min.js"></script>

                <!-- Combo-handled YUI CSS files: -->
                <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/combo?2.8.0r4/build/datatable/assets/skins/sam/datatable.css">
                    <!-- Combo-handled YUI JS files: -->
                    <script type="text/javascript" src="http://yui.yahooapis.com/combo?2.8.0r4/build/yahoo-dom-event/yahoo-dom-event.js&2.8.0r4/build/dragdrop/dragdrop-min.js&2.8.0r4/build/element/element-min.js&2.8.0r4/build/datasource/datasource-min.js&2.8.0r4/build/datatable/datatable-min.js"></script>

                    <script>
                        legend = {
                            table: [
                                { "Submission code":"BV1", "Submission name":"Sargent et al.", "Abstract PDF":"<a href=\"http://www.music-ir.org/mirex/abstracts/2010/BV1.pdf\">PDF</a>", "Contributors":"<a href=\"http://www.irisa.fr/activity/research/metiss?set_language=en\">Gabriel Sargent</a>, <a href=\"http://www.irisa.fr/metiss/members/bimbot\">Frederic Bimbot</a>, <a href=\"http://www.irisa.fr/activity/research/metiss?set_language=en\">Emmanuel Vincent</a>" },
                                { "Submission code":"BV2", "Submission name":"Sargent et al._Config2", "Abstract PDF":"<a href=\"http://www.music-ir.org/mirex/abstracts/2010/BV2.pdf\">PDF</a>", "Contributors":"<a href=\"http://www.irisa.fr/activity/research/metiss?set_language=en\">Gabriel Sargent</a>, <a href=\"http://www.irisa.fr/metiss/members/bimbot\">Frederic Bimbot</a>, <a href=\"http://www.irisa.fr/activity/research/metiss?set_language=en\">Emmanuel Vincent</a>" },
                                { "Submission code":"GP7", "Submission name":"ircamsummary", "Abstract PDF":"<a href=\"http://www.music-ir.org/mirex/abstracts/2010/GP7.pdf\">PDF</a>", "Contributors":"<a href=\"http://www.ircam.fr\">Geoffroy Peeters</a>" },
                                { "Submission code":"MHRAF2", "Submission name":"Simbals_Structure", "Abstract PDF":"<a href=\"http://www.music-ir.org/mirex/abstracts/2010/MHRAF2.pdf\">PDF</a>", "Contributors":"<a href=\"http://www.labri.fr\">Benjamin Martin</a>, <a href=\"http://simbals.labri.fr\">Pierre Hanna</a>, <a href=\"http://simbals.labri.fr\">Matthias Robine</a>, <a href=\"http://simbals.labri.fr\">Julien Allali</a>, <a href=\"http://simbals.labri.fr\">Pascal Ferraro</a>" },
                                { "Submission code":"MND1", "Submission name":"Music Editor's Structural Segmentation", "Abstract PDF":"<a href=\"http://www.music-ir.org/mirex/abstracts/2010/MND1.pdf\">PDF</a>", "Contributors":"<a href=\"http://matthiasmauch.net\">Matthias Mauch</a>, <a href=\"http://sony.com\">Katy Noland</a>, <a href=\"http://www.elec.qmul.ac.uk/digitalmusic/\">Simon Dixon</a>" },
                                { "Submission code":"WB1", "Submission name":"siplca-segmentation", "Abstract PDF":"<a href=\"http://www.music-ir.org/mirex/abstracts/2010/WB1.pdf\">PDF</a>", "Contributors":"<a href=\"http://www.ee.columbia.edu/~ronw\">Ron Weiss</a>, <a href=\"http://homepages.nyu.edu/~jb2843/Home.html\">Juan Bello</a>" }
                            ]
                        }

                        YAHOO.util.Event.addListener(window, "load", function() {
                            YAHOO.example.Basic = function() {
                                var myColumnDefs = [
                                    {key:"Submission code", sortable:true, resizeable:true },
                                    {key:"Submission name", sortable:true, resizeable:true },
                                    {key:"Abstract PDF", sortable:true, resizeable:true },
                                    {key:"Contributors", sortable:true, resizeable:true }		];

                                var myDataSource = new YAHOO.util.DataSource(legend.table);
                                myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
                                myDataSource.responseSchema = {
                                    fields: ["Submission code","Submission name","Abstract PDF","Contributors"]
                                };

                                var myDataTable = new YAHOO.widget.DataTable("legend",myColumnDefs, myDataSource, {});

                                return {
                                    oDS: myDataSource,
                                    oDT: myDataTable
                                };
                            }();
                        });

                    </script>

                    <script>
                        summaryresults = {
                            table: [
                                { "Algorithm":"GP7", "Normalised conditional entropy based over-segmentation score":"0.5024", "Normalised conditional entropy based under-segmentation score":"0.6758", "Frame pair clustering F-measure":"0.4848", "Frame pair clustering precision rate":"0.6675", "Frame pair clustering recall rate":"0.4283", "Random clustering index":"0.6587", "Segment boundary recovery evaluation measure @ 0.5sec":"0.1892", "Segment boundary recovery precision rate @ 0.5sec":"0.1474", "Segment boundary recovery recall rate @ 0.5sec":"0.3098", "Segment boundary recovery evaluation measure @ 3sec":"0.4393", "Segment boundary recovery precision rate @ 3sec":"0.3452", "Segment boundary recovery recall rate @ 3sec":"0.6986", "Median distance from an annotated segment boundary to the closest found boundary":"2.1093", "Median distance from a found segment boundary to the closest annotated one":"6.7053" },
                                { "Algorithm":"WB1", "Normalised conditional entropy based over-segmentation score":"0.6092", "Normalised conditional entropy based under-segmentation score":"0.5341", "Frame pair clustering F-measure":"0.5413", "Frame pair clustering precision rate":"0.5767", "Frame pair clustering recall rate":"0.6069", "Random clustering index":"0.6306", "Segment boundary recovery evaluation measure @ 0.5sec":"0.2415", "Segment boundary recovery precision rate @ 0.5sec":"0.2423", "Segment boundary recovery recall rate @ 0.5sec":"0.2779", "Segment boundary recovery evaluation measure @ 3sec":"0.3967", "Segment boundary recovery precision rate @ 3sec":"0.3953", "Segment boundary recovery recall rate @ 3sec":"0.4535", "Median distance from an annotated segment boundary to the closest found boundary":"10.6145", "Median distance from a found segment boundary to the closest annotated one":"3.9665" },
                                { "Algorithm":"MHRAF2", "Normalised conditional entropy based over-segmentation score":"0.5503", "Normalised conditional entropy based under-segmentation score":"0.5814", "Frame pair clustering F-measure":"0.5554", "Frame pair clustering precision rate":"0.6067", "Frame pair clustering recall rate":"0.5862", "Random clustering index":"0.6591", "Segment boundary recovery evaluation measure @ 0.5sec":"0.1996", "Segment boundary recovery precision rate @ 0.5sec":"0.2209", "Segment boundary recovery recall rate @ 0.5sec":"0.2022", "Segment boundary recovery evaluation measure @ 3sec":"0.4389", "Segment boundary recovery precision rate @ 3sec":"0.4856", "Segment boundary recovery recall rate @ 3sec":"0.4457", "Median distance from an annotated segment boundary to the closest found boundary":"7.3000", "Median distance from a found segment boundary to the closest annotated one":"5.4199" },
                                { "Algorithm":"MND1", "Normalised conditional entropy based over-segmentation score":"0.6261", "Normalised conditional entropy based under-segmentation score":"0.6188", "Frame pair clustering F-measure":"0.5539", "Frame pair clustering precision rate":"0.6395", "Frame pair clustering recall rate":"0.5864", "Random clustering index":"0.6643", "Segment boundary recovery evaluation measure @ 0.5sec":"0.2947", "Segment boundary recovery precision rate @ 0.5sec":"0.3034", "Segment boundary recovery recall rate @ 0.5sec":"0.3335", "Segment boundary recovery evaluation measure @ 3sec":"0.4708", "Segment boundary recovery precision rate @ 3sec":"0.4751", "Segment boundary recovery recall rate @ 3sec":"0.5409", "Median distance from an annotated segment boundary to the closest found boundary":"8.2948", "Median distance from a found segment boundary to the closest annotated one":"5.5076" },
                                { "Algorithm":"BV1", "Normalised conditional entropy based over-segmentation score":"0.6562", "Normalised conditional entropy based under-segmentation score":"0.4103", "Frame pair clustering F-measure":"0.5161", "Frame pair clustering precision rate":"0.4910", "Frame pair clustering recall rate":"0.7092", "Random clustering index":"0.5297", "Segment boundary recovery evaluation measure @ 0.5sec":"0.2032", "Segment boundary recovery precision rate @ 0.5sec":"0.2386", "Segment boundary recovery recall rate @ 0.5sec":"0.2797", "Segment boundary recovery evaluation measure @ 3sec":"0.4364", "Segment boundary recovery precision rate @ 3sec":"0.4367", "Segment boundary recovery recall rate @ 3sec":"0.6169", "Median distance from an annotated segment boundary to the closest found boundary":"12.7659", "Median distance from a found segment boundary to the closest annotated one":"6.9049" },
                                { "Algorithm":"BV2", "Normalised conditional entropy based over-segmentation score":"0.5166", "Normalised conditional entropy based under-segmentation score":"0.6613", "Frame pair clustering F-measure":"0.4407", "Frame pair clustering precision rate":"0.6434", "Frame pair clustering recall rate":"0.4232", "Random clustering index":"0.6181", "Segment boundary recovery evaluation measure @ 0.5sec":"0.2011", "Segment boundary recovery precision rate @ 0.5sec":"0.2246", "Segment boundary recovery recall rate @ 0.5sec":"0.2803", "Segment boundary recovery evaluation measure @ 3sec":"0.4383", "Segment boundary recovery precision rate @ 3sec":"0.4253", "Segment boundary recovery recall rate @ 3sec":"0.6241", "Median distance from an annotated segment boundary to the closest found boundary":"11.3138", "Median distance from a found segment boundary to the closest annotated one":"7.0789" }
                            ]
                        }

                        YAHOO.util.Event.addListener(window, "load", function() {
                            YAHOO.example.Basic = function() {
                                var myColumnDefs = [
                                    {key:"Algorithm", sortable:true, resizeable:true },
                                    {key:"Normalised conditional entropy based over-segmentation score", sortable:true, resizeable:true },
                                    {key:"Normalised conditional entropy based under-segmentation score", sortable:true, resizeable:true },
                                    {key:"Frame pair clustering F-measure", sortable:true, resizeable:true },
                                    {key:"Frame pair clustering precision rate", sortable:true, resizeable:true },
                                    {key:"Frame pair clustering recall rate", sortable:true, resizeable:true },
                                    {key:"Random clustering index", sortable:true, resizeable:true },
                                    {key:"Segment boundary recovery evaluation measure @ 0.5sec", sortable:true, resizeable:true },
                                    {key:"Segment boundary recovery precision rate @ 0.5sec", sortable:true, resizeable:true },
                                    {key:"Segment boundary recovery recall rate @ 0.5sec", sortable:true, resizeable:true },
                                    {key:"Segment boundary recovery evaluation measure @ 3sec", sortable:true, resizeable:true },
                                    {key:"Segment boundary recovery precision rate @ 3sec", sortable:true, resizeable:true },
                                    {key:"Segment boundary recovery recall rate @ 3sec", sortable:true, resizeable:true },
                                    {key:"Median distance from an annotated segment boundary to the closest found boundary", sortable:true, resizeable:true },
                                    {key:"Median distance from a found segment boundary to the closest annotated one", sortable:true, resizeable:true }		];

                                var myDataSource = new YAHOO.util.DataSource(summaryresults.table);
                                myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
                                myDataSource.responseSchema = {
                                    fields: ["Algorithm","Normalised conditional entropy based over-segmentation score","Normalised conditional entropy based under-segmentation score","Frame pair clustering F-measure","Frame pair clustering precision rate","Frame pair clustering recall rate","Random clustering index","Segment boundary recovery evaluation measure @ 0.5sec","Segment boundary recovery precision rate @ 0.5sec","Segment boundary recovery recall rate @ 0.5sec","Segment boundary recovery evaluation measure @ 3sec","Segment boundary recovery precision rate @ 3sec","Segment boundary recovery recall rate @ 3sec","Median distance from an annotated segment boundary to the closest found boundary","Median distance from a found segment boundary to the closest annotated one"]
                                };

                                var myDataTable = new YAHOO.widget.DataTable("summaryresults",myColumnDefs, myDataSource, {});

                                return {
                                    oDS: myDataSource,
                                    oDT: myDataTable
                                };
                            }();
                        });
                    </script>
                    </head>

                    <body>

                        <table id="h2table">
                            <tr>
                                <td><img src="support/logo.png" width="160"></td>
                                <td><h2>Segmentation Plot Results</h2></td>
                            </tr>
                        </table>

                        <div id="tabs">
                            <ul>
                                <li><a href="index.jsp" title="classical"><span>Summary</span></a></li>
                                <li><a href="classical.jsp" title="classical"><span>Classical</span></a></li>
                                <li><a href="jazz.jsp" title="Jazz"><span>Jazz</span></a></li>
                                <li><a href="live.jsp" title="Live"><span>Live</span></a></li>
                                <li><a href="pop.jsp" title="Pop"><span>Popular</span></a></li>
                                <li><a href="world.jsp" title="World"><span>World</span></a></li>
                            </ul>
                        </div>

                        <br><a name="top"></a>
                            <div id="content">
                                <h3>Summary</h3>
                                <a name="legend"></a>

                                <h4>Legend</h4>
                                <div id="legend"></div>
                                <br><br>
                                        <a name="summaryresults"></a>
                                        <h4>Summary Results&nbsp;&nbsp;&nbsp;&nbsp;<span class="toplink"><a href="#top">[top]</a></span></h4>
                                        <div id="summaryresults"></div>
                                        <br><br>


                                                </div>
                                                </body>
                                                </html>
