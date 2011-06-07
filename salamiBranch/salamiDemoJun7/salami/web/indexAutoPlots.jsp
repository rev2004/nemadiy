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
        
        <c:set var="maxPlot" value="${1642}"/>
        <c:choose>
            <c:when test="${empty param.page}">
             <c:set  var="startPlot" value="${param.page*100}"/>
             <c:choose>
                 <c:when test="${param.page*100<=maxPlot}$">
                     <c:set var="endPlot" value="${param.page*100+99}"/>
                 </c:when>
                 <c:otherwise>
                     <c:set var="endPlot" value="${maxPlot}"/>
                 </c:otherwise>
             </c:choose>
            </c:when>
            <c:otherwise>
                <c:set  var="startPlot" value="${0}"/>
                <c:set var="endPlot" value="${99}"/>
            </c:otherwise>
        </c:choose>
     </head>

                <body>

                    <table id="h2table">
                        <tr>
                            <td><img src="support/logo.png" width="160"></td>
                            <td><h2>Segmentation Plot Results</h2></td>
                        </tr>
                    </table>

                    <div id="tabs">

                    </div>

                    <br><a name="top"></a>
                        <div id="content">
                            <h3>Segmentation Plots</h3>
                            <ul>
                                <c:forEach begin="${startPlot}" end="${endPlot}" var="currentPlot">
                                    <fmt:formatNumber value="${currentPlot}" pattern="salami000000" var="filename"/>
                                    <li>${filename}  <button  onclick="window.open('DemoJune/plot.jsp?plot=${filename}')">Plot</button>
                                    </li>
                                </c:forEach>
                            </ul>

                        </div>
                </body>
                </html>
