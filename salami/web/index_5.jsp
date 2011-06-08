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
        <title>SALAMI: Structural Segmentation Experiments</title>

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
                            <td><h2>SALAMI: Structural Segmentation Experiments</h2></td>
                        </tr>
                    </table>

                    <div id="tabs">

                    </div>

                    <br><a name="top"></a>
                        <div id="content">
                            <h3>Segmentation Plots</h3>   <a href="index_4.jsp">prev</a>|next
                            <ul>
                                <li>salami001496 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001496')">Plot</button></li>
                                <li>salami001497 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001497')">Plot</button></li>
                                <li>salami001498 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001498')">Plot</button></li>
                                <li>salami001499 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001499')">Plot</button></li>
                                <li>salami001501 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001501')">Plot</button></li>
                                <li>salami001502 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001502')">Plot</button></li>
                                <li>salami001504 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001504')">Plot</button></li>
                                <li>salami001507 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001507')">Plot</button></li>
                                <li>salami001508 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001508')">Plot</button></li>
                                <li>salami001511 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001511')">Plot</button></li>
                                <li>salami001512 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001512')">Plot</button></li>
                                <li>salami001513 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001513')">Plot</button></li>
                                <li>salami001514 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001514')">Plot</button></li>
                                <li>salami001516 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001516')">Plot</button></li>
                                <li>salami001517 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001517')">Plot</button></li>
                                <li>salami001518 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001518')">Plot</button></li>
                                <li>salami001520 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001520')">Plot</button></li>
                                <li>salami001521 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001521')">Plot</button></li>
                                <li>salami001522 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001522')">Plot</button></li>
                                <li>salami001524 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001524')">Plot</button></li>
                                <li>salami001526 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001526')">Plot</button></li>
                                <li>salami001527 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001527')">Plot</button></li>
                                <li>salami001528 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001528')">Plot</button></li>
                                <li>salami001530 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001530')">Plot</button></li>
                                <li>salami001533 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001533')">Plot</button></li>
                                <li>salami001534 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001534')">Plot</button></li>
                                <li>salami001535 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001535')">Plot</button></li>
                                <li>salami001537 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001537')">Plot</button></li>
                                <li>salami001538 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001538')">Plot</button></li>
                                <li>salami001539 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001539')">Plot</button></li>
                                <li>salami001540 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001540')">Plot</button></li>
                                <li>salami001541 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001541')">Plot</button></li>
                                <li>salami001542 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001542')">Plot</button></li>
                                <li>salami001543 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001543')">Plot</button></li>
                                <li>salami001544 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001544')">Plot</button></li>
                                <li>salami001545 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001545')">Plot</button></li>
                                <li>salami001546 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001546')">Plot</button></li>
                                <li>salami001549 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001549')">Plot</button></li>
                                <li>salami001552 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001552')">Plot</button></li>
                                <li>salami001557 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001557')">Plot</button></li>
                                <li>salami001558 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001558')">Plot</button></li>
                                <li>salami001559 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001559')">Plot</button></li>
                                <li>salami001561 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001561')">Plot</button></li>
                                <li>salami001568 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001568')">Plot</button></li>
                                <li>salami001570 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001570')">Plot</button></li>
                                <li>salami001571 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001571')">Plot</button></li>
                                <li>salami001572 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001572')">Plot</button></li>
                                <li>salami001573 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001573')">Plot</button></li>
                                <li>salami001574 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001574')">Plot</button></li>
                                <li>salami001575 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001575')">Plot</button></li>
                                <li>salami001577 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001577')">Plot</button></li>
                                <li>salami001578 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001578')">Plot</button></li>
                                <li>salami001579 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001579')">Plot</button></li>
                                <li>salami001581 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001581')">Plot</button></li>
                                <li>salami001582 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001582')">Plot</button></li>
                                <li>salami001584 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001584')">Plot</button></li>
                                <li>salami001585 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001585')">Plot</button></li>
                                <li>salami001587 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001587')">Plot</button></li>
                                <li>salami001588 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001588')">Plot</button></li>
                                <li>salami001590 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001590')">Plot</button></li>
                                <li>salami001592 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001592')">Plot</button></li>
                                <li>salami001594 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001594')">Plot</button></li>
                                <li>salami001597 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001597')">Plot</button></li>
                                <li>salami001598 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001598')">Plot</button></li>
                                <li>salami001599 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001599')">Plot</button></li>
                                <li>salami001600 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001600')">Plot</button></li>
                                <li>salami001602 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001602')">Plot</button></li>
                                <li>salami001603 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001603')">Plot</button></li>
                                <li>salami001604 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001604')">Plot</button></li>
                                <li>salami001605 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001605')">Plot</button></li>
                                <li>salami001607 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001607')">Plot</button></li>
                                <li>salami001608 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001608')">Plot</button></li>
                                <li>salami001610 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001610')">Plot</button></li>
                                <li>salami001611 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001611')">Plot</button></li>
                                <li>salami001613 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001613')">Plot</button></li>
                                <li>salami001614 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001614')">Plot</button></li>
                                <li>salami001615 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001615')">Plot</button></li>
                                <li>salami001617 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001617')">Plot</button></li>
                                <li>salami001618 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001618')">Plot</button></li>
                                <li>salami001619 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001619')">Plot</button></li>
                                <li>salami001620 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001620')">Plot</button></li>
                                <li>salami001621 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001621')">Plot</button></li>
                                <li>salami001622 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001622')">Plot</button></li>
                                <li>salami001623 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001623')">Plot</button></li>
                                <li>salami001624 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001624')">Plot</button></li>
                                <li>salami001625 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001625')">Plot</button></li>
                                <li>salami001626 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001626')">Plot</button></li>
                                <li>salami001627 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001627')">Plot</button></li>
                                <li>salami001628 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001628')">Plot</button></li>
                                <li>salami001630 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001630')">Plot</button></li>
                                <li>salami001634 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001634')">Plot</button></li>
                                <li>salami001635 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001635')">Plot</button></li>
                                <li>salami001636 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001636')">Plot</button></li>
                                <li>salami001639 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001639')">Plot</button></li>
                                <li>salami001640 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001640')">Plot</button></li>
                                <li>salami001641 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001641')">Plot</button></li>
                                <li>salami001642 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001642')">Plot</button></li>
                            </ul>

                        </div>
                </body>
                </html>
