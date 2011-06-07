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
                            <h3>Segmentation Plots</h3>   <a href="index.jsp">prev</a>|<a href="index_2.jsp">next</a>
                            <ul>
                                <li>salami000300 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000300')">Plot</button></li>
                                <li>salami000301 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000301')">Plot</button></li>
                                <li>salami000302 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000302')">Plot</button></li>
                                <li>salami000306 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000306')">Plot</button></li>
                                <li>salami000307 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000307')">Plot</button></li>
                                <li>salami000308 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000308')">Plot</button></li>
                                <li>salami000309 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000309')">Plot</button></li>
                                <li>salami000310 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000310')">Plot</button></li>
                                <li>salami000311 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000311')">Plot</button></li>
                                <li>salami000313 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000313')">Plot</button></li>
                                <li>salami000315 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000315')">Plot</button></li>
                                <li>salami000318 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000318')">Plot</button></li>
                                <li>salami000319 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000319')">Plot</button></li>
                                <li>salami000320 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000320')">Plot</button></li>
                                <li>salami000322 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000322')">Plot</button></li>
                                <li>salami000323 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000323')">Plot</button></li>
                                <li>salami000325 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000325')">Plot</button></li>
                                <li>salami000326 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000326')">Plot</button></li>
                                <li>salami000327 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000327')">Plot</button></li>
                                <li>salami000329 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000329')">Plot</button></li>
                                <li>salami000331 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000331')">Plot</button></li>
                                <li>salami000332 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000332')">Plot</button></li>
                                <li>salami000333 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000333')">Plot</button></li>
                                <li>salami000334 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000334')">Plot</button></li>
                                <li>salami000339 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000339')">Plot</button></li>
                                <li>salami000340 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000340')">Plot</button></li>
                                <li>salami000341 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000341')">Plot</button></li>
                                <li>salami000342 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000342')">Plot</button></li>
                                <li>salami000343 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000343')">Plot</button></li>
                                <li>salami000346 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000346')">Plot</button></li>
                                <li>salami000347 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000347')">Plot</button></li>
                                <li>salami000350 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000350')">Plot</button></li>
                                <li>salami000353 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000353')">Plot</button></li>
                                <li>salami000354 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000354')">Plot</button></li>
                                <li>salami000355 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000355')">Plot</button></li>
                                <li>salami000358 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000358')">Plot</button></li>
                                <li>salami000363 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000363')">Plot</button></li>
                                <li>salami000365 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000365')">Plot</button></li>
                                <li>salami000367 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000367')">Plot</button></li>
                                <li>salami000368 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000368')">Plot</button></li>
                                <li>salami000369 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000369')">Plot</button></li>
                                <li>salami000370 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000370')">Plot</button></li>
                                <li>salami000371 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000371')">Plot</button></li>
                                <li>salami000372 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000372')">Plot</button></li>
                                <li>salami000373 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000373')">Plot</button></li>
                                <li>salami000374 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000374')">Plot</button></li>
                                <li>salami000375 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000375')">Plot</button></li>
                                <li>salami000378 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000378')">Plot</button></li>
                                <li>salami000379 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000379')">Plot</button></li>
                                <li>salami000380 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000380')">Plot</button></li>
                                <li>salami000381 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000381')">Plot</button></li>
                                <li>salami000382 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000382')">Plot</button></li>
                                <li>salami000385 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000385')">Plot</button></li>
                                <li>salami000386 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000386')">Plot</button></li>
                                <li>salami000387 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000387')">Plot</button></li>
                                <li>salami000389 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000389')">Plot</button></li>
                                <li>salami000390 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000390')">Plot</button></li>
                                <li>salami000391 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000391')">Plot</button></li>
                                <li>salami000392 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000392')">Plot</button></li>
                                <li>salami000393 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000393')">Plot</button></li>
                                <li>salami000395 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000395')">Plot</button></li>
                                <li>salami000396 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000396')">Plot</button></li>
                                <li>salami000397 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000397')">Plot</button></li>
                                <li>salami000398 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000398')">Plot</button></li>
                                <li>salami000399 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000399')">Plot</button></li>
                                <li>salami000401 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000401')">Plot</button></li>
                                <li>salami000402 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000402')">Plot</button></li>
                                <li>salami000403 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000403')">Plot</button></li>
                                <li>salami000404 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000404')">Plot</button></li>
                                <li>salami000406 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000406')">Plot</button></li>
                                <li>salami000409 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000409')">Plot</button></li>
                                <li>salami000410 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000410')">Plot</button></li>
                                <li>salami000411 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000411')">Plot</button></li>
                                <li>salami000412 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000412')">Plot</button></li>
                                <li>salami000414 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000414')">Plot</button></li>
                                <li>salami000415 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000415')">Plot</button></li>
                                <li>salami000420 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000420')">Plot</button></li>
                                <li>salami000423 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000423')">Plot</button></li>
                                <li>salami000424 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000424')">Plot</button></li>
                                <li>salami000425 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000425')">Plot</button></li>
                                <li>salami000426 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000426')">Plot</button></li>
                                <li>salami000427 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000427')">Plot</button></li>
                                <li>salami000429 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000429')">Plot</button></li>
                                <li>salami000430 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000430')">Plot</button></li>
                                <li>salami000431 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000431')">Plot</button></li>
                                <li>salami000432 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000432')">Plot</button></li>
                                <li>salami000433 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000433')">Plot</button></li>
                                <li>salami000435 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000435')">Plot</button></li>
                                <li>salami000436 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000436')">Plot</button></li>
                                <li>salami000437 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000437')">Plot</button></li>
                                <li>salami000438 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000438')">Plot</button></li>
                                <li>salami000439 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000439')">Plot</button></li>
                                <li>salami000440 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000440')">Plot</button></li>
                                <li>salami000441 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000441')">Plot</button></li>
                                <li>salami000442 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000442')">Plot</button></li>
                                <li>salami000444 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000444')">Plot</button></li>
                                <li>salami000445 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000445')">Plot</button></li>
                                <li>salami000446 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000446')">Plot</button></li>
                                <li>salami000447 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000447')">Plot</button></li>
                                <li>salami000449 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000449')">Plot</button></li>
                                <li>salami000450 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000450')">Plot</button></li>
                                <li>salami000451 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000451')">Plot</button></li>
                                <li>salami000452 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000452')">Plot</button></li>
                                <li>salami000453 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000453')">Plot</button></li>
                                <li>salami000455 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000455')">Plot</button></li>
                                <li>salami000456 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000456')">Plot</button></li>
                                <li>salami000457 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000457')">Plot</button></li>
                                <li>salami000458 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000458')">Plot</button></li>
                                <li>salami000460 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000460')">Plot</button></li>
                                <li>salami000461 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000461')">Plot</button></li>
                                <li>salami000463 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000463')">Plot</button></li>
                                <li>salami000465 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000465')">Plot</button></li>
                                <li>salami000466 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000466')">Plot</button></li>
                                <li>salami000468 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000468')">Plot</button></li>
                                <li>salami000470 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000470')">Plot</button></li>
                                <li>salami000471 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000471')">Plot</button></li>
                                <li>salami000472 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000472')">Plot</button></li>
                                <li>salami000477 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000477')">Plot</button></li>
                                <li>salami000478 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000478')">Plot</button></li>
                                <li>salami000480 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000480')">Plot</button></li>
                                <li>salami000482 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000482')">Plot</button></li>
                                <li>salami000484 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000484')">Plot</button></li>
                                <li>salami000487 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000487')">Plot</button></li>
                                <li>salami000490 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000490')">Plot</button></li>
                                <li>salami000491 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000491')">Plot</button></li>
                                <li>salami000492 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000492')">Plot</button></li>
                                <li>salami000493 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000493')">Plot</button></li>
                                <li>salami000496 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000496')">Plot</button></li>
                                <li>salami000497 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000497')">Plot</button></li>
                                <li>salami000499 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000499')">Plot</button></li>
                                <li>salami000500 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000500')">Plot</button></li>
                                <li>salami000504 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000504')">Plot</button></li>
                                <li>salami000505 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000505')">Plot</button></li>
                                <li>salami000507 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000507')">Plot</button></li>
                                <li>salami000509 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000509')">Plot</button></li>
                                <li>salami000510 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000510')">Plot</button></li>
                                <li>salami000511 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000511')">Plot</button></li>
                                <li>salami000513 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000513')">Plot</button></li>
                                <li>salami000514 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000514')">Plot</button></li>
                                <li>salami000515 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000515')">Plot</button></li>
                                <li>salami000516 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000516')">Plot</button></li>
                                <li>salami000517 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000517')">Plot</button></li>
                                <li>salami000518 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000518')">Plot</button></li>
                                <li>salami000519 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000519')">Plot</button></li>
                                <li>salami000521 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000521')">Plot</button></li>
                                <li>salami000523 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000523')">Plot</button></li>
                                <li>salami000524 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000524')">Plot</button></li>
                                <li>salami000526 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000526')">Plot</button></li>
                                <li>salami000527 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000527')">Plot</button></li>
                                <li>salami000528 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000528')">Plot</button></li>
                                <li>salami000529 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000529')">Plot</button></li>
                                <li>salami000531 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000531')">Plot</button></li>
                                <li>salami000533 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000533')">Plot</button></li>
                                <li>salami000534 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000534')">Plot</button></li>
                                <li>salami000535 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000535')">Plot</button></li>
                                <li>salami000536 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000536')">Plot</button></li>
                                <li>salami000537 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000537')">Plot</button></li>
                                <li>salami000538 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000538')">Plot</button></li>
                                <li>salami000540 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000540')">Plot</button></li>
                                <li>salami000543 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000543')">Plot</button></li>
                                <li>salami000544 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000544')">Plot</button></li>
                                <li>salami000547 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000547')">Plot</button></li>
                                <li>salami000548 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000548')">Plot</button></li>
                                <li>salami000549 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000549')">Plot</button></li>
                                <li>salami000550 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000550')">Plot</button></li>
                                <li>salami000551 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000551')">Plot</button></li>
                                <li>salami000552 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000552')">Plot</button></li>
                                <li>salami000554 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000554')">Plot</button></li>
                                <li>salami000555 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000555')">Plot</button></li>
                                <li>salami000556 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000556')">Plot</button></li>
                                <li>salami000557 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000557')">Plot</button></li>
                                <li>salami000559 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000559')">Plot</button></li>
                                <li>salami000560 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000560')">Plot</button></li>
                                <li>salami000563 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000563')">Plot</button></li>
                                <li>salami000564 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000564')">Plot</button></li>
                                <li>salami000566 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000566')">Plot</button></li>
                                <li>salami000567 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000567')">Plot</button></li>
                                <li>salami000568 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000568')">Plot</button></li>
                                <li>salami000570 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000570')">Plot</button></li>
                                <li>salami000571 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000571')">Plot</button></li>
                                <li>salami000573 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000573')">Plot</button></li>
                                <li>salami000575 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000575')">Plot</button></li>
                                <li>salami000577 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000577')">Plot</button></li>
                                <li>salami000579 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000579')">Plot</button></li>
                                <li>salami000582 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000582')">Plot</button></li>
                                <li>salami000587 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000587')">Plot</button></li>
                                <li>salami000591 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000591')">Plot</button></li>
                                <li>salami000592 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000592')">Plot</button></li>
                                <li>salami000593 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000593')">Plot</button></li>
                                <li>salami000595 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000595')">Plot</button></li>
                                <li>salami000596 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000596')">Plot</button></li>
                                <li>salami000597 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000597')">Plot</button></li>
                                <li>salami000598 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000598')">Plot</button></li>
                                <li>salami000600 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000600')">Plot</button></li>
                                <li>salami000601 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000601')">Plot</button></li>
                                <li>salami000602 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000602')">Plot</button></li>
                                <li>salami000603 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000603')">Plot</button></li>
                                <li>salami000604 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000604')">Plot</button></li>
                                <li>salami000606 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000606')">Plot</button></li>
                                <li>salami000607 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000607')">Plot</button></li>
                            </ul>

                        </div>
                </body>
                </html>
