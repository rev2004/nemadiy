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
                            <h3>Segmentation Plots</h3>   <a href="index_3.jsp">prev</a>|<a href="index_5.jsp">next</a>
                            <ul>
                                <li>salami001194 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001194')">Plot</button></li>
                                <li>salami001195 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001195')">Plot</button></li>
                                <li>salami001196 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001196')">Plot</button></li>
                                <li>salami001197 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001197')">Plot</button></li>
                                <li>salami001198 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001198')">Plot</button></li>
                                <li>salami001199 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001199')">Plot</button></li>
                                <li>salami001200 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001200')">Plot</button></li>
                                <li>salami001201 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001201')">Plot</button></li>
                                <li>salami001202 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001202')">Plot</button></li>
                                <li>salami001203 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001203')">Plot</button></li>
                                <li>salami001204 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001204')">Plot</button></li>
                                <li>salami001205 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001205')">Plot</button></li>
                                <li>salami001207 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001207')">Plot</button></li>
                                <li>salami001208 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001208')">Plot</button></li>
                                <li>salami001211 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001211')">Plot</button></li>
                                <li>salami001212 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001212')">Plot</button></li>
                                <li>salami001213 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001213')">Plot</button></li>
                                <li>salami001214 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001214')">Plot</button></li>
                                <li>salami001215 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001215')">Plot</button></li>
                                <li>salami001216 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001216')">Plot</button></li>
                                <li>salami001219 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001219')">Plot</button></li>
                                <li>salami001220 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001220')">Plot</button></li>
                                <li>salami001224 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001224')">Plot</button></li>
                                <li>salami001226 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001226')">Plot</button></li>
                                <li>salami001227 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001227')">Plot</button></li>
                                <li>salami001229 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001229')">Plot</button></li>
                                <li>salami001231 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001231')">Plot</button></li>
                                <li>salami001232 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001232')">Plot</button></li>
                                <li>salami001234 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001234')">Plot</button></li>
                                <li>salami001235 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001235')">Plot</button></li>
                                <li>salami001236 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001236')">Plot</button></li>
                                <li>salami001238 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001238')">Plot</button></li>
                                <li>salami001241 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001241')">Plot</button></li>
                                <li>salami001242 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001242')">Plot</button></li>
                                <li>salami001243 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001243')">Plot</button></li>
                                <li>salami001244 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001244')">Plot</button></li>
                                <li>salami001246 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001246')">Plot</button></li>
                                <li>salami001247 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001247')">Plot</button></li>
                                <li>salami001249 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001249')">Plot</button></li>
                                <li>salami001250 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001250')">Plot</button></li>
                                <li>salami001253 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001253')">Plot</button></li>
                                <li>salami001254 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001254')">Plot</button></li>
                                <li>salami001255 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001255')">Plot</button></li>
                                <li>salami001257 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001257')">Plot</button></li>
                                <li>salami001259 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001259')">Plot</button></li>
                                <li>salami001260 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001260')">Plot</button></li>
                                <li>salami001262 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001262')">Plot</button></li>
                                <li>salami001264 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001264')">Plot</button></li>
                                <li>salami001267 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001267')">Plot</button></li>
                                <li>salami001268 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001268')">Plot</button></li>
                                <li>salami001269 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001269')">Plot</button></li>
                                <li>salami001270 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001270')">Plot</button></li>
                                <li>salami001272 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001272')">Plot</button></li>
                                <li>salami001273 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001273')">Plot</button></li>
                                <li>salami001275 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001275')">Plot</button></li>
                                <li>salami001276 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001276')">Plot</button></li>
                                <li>salami001278 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001278')">Plot</button></li>
                                <li>salami001279 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001279')">Plot</button></li>
                                <li>salami001282 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001282')">Plot</button></li>
                                <li>salami001287 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001287')">Plot</button></li>
                                <li>salami001288 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001288')">Plot</button></li>
                                <li>salami001289 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001289')">Plot</button></li>
                                <li>salami001291 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001291')">Plot</button></li>
                                <li>salami001292 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001292')">Plot</button></li>
                                <li>salami001294 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001294')">Plot</button></li>
                                <li>salami001297 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001297')">Plot</button></li>
                                <li>salami001298 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001298')">Plot</button></li>
                                <li>salami001300 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001300')">Plot</button></li>
                                <li>salami001302 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001302')">Plot</button></li>
                                <li>salami001303 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001303')">Plot</button></li>
                                <li>salami001304 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001304')">Plot</button></li>
                                <li>salami001305 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001305')">Plot</button></li>
                                <li>salami001306 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001306')">Plot</button></li>
                                <li>salami001307 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001307')">Plot</button></li>
                                <li>salami001308 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001308')">Plot</button></li>
                                <li>salami001312 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001312')">Plot</button></li>
                                <li>salami001313 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001313')">Plot</button></li>
                                <li>salami001314 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001314')">Plot</button></li>
                                <li>salami001316 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001316')">Plot</button></li>
                                <li>salami001317 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001317')">Plot</button></li>
                                <li>salami001319 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001319')">Plot</button></li>
                                <li>salami001322 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001322')">Plot</button></li>
                                <li>salami001323 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001323')">Plot</button></li>
                                <li>salami001324 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001324')">Plot</button></li>
                                <li>salami001326 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001326')">Plot</button></li>
                                <li>salami001327 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001327')">Plot</button></li>
                                <li>salami001328 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001328')">Plot</button></li>
                                <li>salami001330 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001330')">Plot</button></li>
                                <li>salami001332 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001332')">Plot</button></li>
                                <li>salami001333 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001333')">Plot</button></li>
                                <li>salami001334 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001334')">Plot</button></li>
                                <li>salami001336 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001336')">Plot</button></li>
                                <li>salami001338 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001338')">Plot</button></li>
                                <li>salami001339 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001339')">Plot</button></li>
                                <li>salami001342 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001342')">Plot</button></li>
                                <li>salami001343 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001343')">Plot</button></li>
                                <li>salami001344 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001344')">Plot</button></li>
                                <li>salami001347 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001347')">Plot</button></li>
                                <li>salami001350 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001350')">Plot</button></li>
                                <li>salami001352 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001352')">Plot</button></li>
                                <li>salami001354 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001354')">Plot</button></li>
                                <li>salami001356 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001356')">Plot</button></li>
                                <li>salami001357 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001357')">Plot</button></li>
                                <li>salami001358 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001358')">Plot</button></li>
                                <li>salami001359 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001359')">Plot</button></li>
                                <li>salami001360 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001360')">Plot</button></li>
                                <li>salami001361 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001361')">Plot</button></li>
                                <li>salami001363 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001363')">Plot</button></li>
                                <li>salami001364 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001364')">Plot</button></li>
                                <li>salami001367 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001367')">Plot</button></li>
                                <li>salami001368 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001368')">Plot</button></li>
                                <li>salami001369 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001369')">Plot</button></li>
                                <li>salami001371 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001371')">Plot</button></li>
                                <li>salami001372 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001372')">Plot</button></li>
                                <li>salami001373 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001373')">Plot</button></li>
                                <li>salami001375 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001375')">Plot</button></li>
                                <li>salami001379 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001379')">Plot</button></li>
                                <li>salami001380 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001380')">Plot</button></li>
                                <li>salami001381 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001381')">Plot</button></li>
                                <li>salami001383 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001383')">Plot</button></li>
                                <li>salami001384 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001384')">Plot</button></li>
                                <li>salami001386 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001386')">Plot</button></li>
                                <li>salami001387 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001387')">Plot</button></li>
                                <li>salami001388 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001388')">Plot</button></li>
                                <li>salami001389 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001389')">Plot</button></li>
                                <li>salami001390 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001390')">Plot</button></li>
                                <li>salami001391 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001391')">Plot</button></li>
                                <li>salami001393 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001393')">Plot</button></li>
                                <li>salami001394 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001394')">Plot</button></li>
                                <li>salami001395 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001395')">Plot</button></li>
                                <li>salami001396 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001396')">Plot</button></li>
                                <li>salami001397 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001397')">Plot</button></li>
                                <li>salami001399 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001399')">Plot</button></li>
                                <li>salami001400 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001400')">Plot</button></li>
                                <li>salami001401 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001401')">Plot</button></li>
                                <li>salami001402 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001402')">Plot</button></li>
                                <li>salami001403 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001403')">Plot</button></li>
                                <li>salami001406 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001406')">Plot</button></li>
                                <li>salami001407 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001407')">Plot</button></li>
                                <li>salami001408 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001408')">Plot</button></li>
                                <li>salami001409 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001409')">Plot</button></li>
                                <li>salami001410 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001410')">Plot</button></li>
                                <li>salami001411 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001411')">Plot</button></li>
                                <li>salami001412 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001412')">Plot</button></li>
                                <li>salami001414 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001414')">Plot</button></li>
                                <li>salami001416 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001416')">Plot</button></li>
                                <li>salami001418 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001418')">Plot</button></li>
                                <li>salami001419 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001419')">Plot</button></li>
                                <li>salami001421 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001421')">Plot</button></li>
                                <li>salami001422 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001422')">Plot</button></li>
                                <li>salami001423 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001423')">Plot</button></li>
                                <li>salami001424 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001424')">Plot</button></li>
                                <li>salami001428 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001428')">Plot</button></li>
                                <li>salami001430 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001430')">Plot</button></li>
                                <li>salami001432 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001432')">Plot</button></li>
                                <li>salami001433 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001433')">Plot</button></li>
                                <li>salami001434 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001434')">Plot</button></li>
                                <li>salami001435 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001435')">Plot</button></li>
                                <li>salami001437 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001437')">Plot</button></li>
                                <li>salami001438 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001438')">Plot</button></li>
                                <li>salami001440 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001440')">Plot</button></li>
                                <li>salami001442 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001442')">Plot</button></li>
                                <li>salami001443 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001443')">Plot</button></li>
                                <li>salami001445 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001445')">Plot</button></li>
                                <li>salami001446 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001446')">Plot</button></li>
                                <li>salami001447 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001447')">Plot</button></li>
                                <li>salami001449 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001449')">Plot</button></li>
                                <li>salami001452 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001452')">Plot</button></li>
                                <li>salami001453 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001453')">Plot</button></li>
                                <li>salami001456 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001456')">Plot</button></li>
                                <li>salami001457 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001457')">Plot</button></li>
                                <li>salami001458 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001458')">Plot</button></li>
                                <li>salami001459 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001459')">Plot</button></li>
                                <li>salami001460 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001460')">Plot</button></li>
                                <li>salami001461 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001461')">Plot</button></li>
                                <li>salami001462 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001462')">Plot</button></li>
                                <li>salami001463 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001463')">Plot</button></li>
                                <li>salami001464 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001464')">Plot</button></li>
                                <li>salami001467 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001467')">Plot</button></li>
                                <li>salami001468 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001468')">Plot</button></li>
                                <li>salami001469 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001469')">Plot</button></li>
                                <li>salami001470 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001470')">Plot</button></li>
                                <li>salami001471 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001471')">Plot</button></li>
                                <li>salami001472 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001472')">Plot</button></li>
                                <li>salami001474 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001474')">Plot</button></li>
                                <li>salami001476 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001476')">Plot</button></li>
                                <li>salami001478 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001478')">Plot</button></li>
                                <li>salami001479 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001479')">Plot</button></li>
                                <li>salami001480 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001480')">Plot</button></li>
                                <li>salami001481 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001481')">Plot</button></li>
                                <li>salami001482 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001482')">Plot</button></li>
                                <li>salami001483 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001483')">Plot</button></li>
                                <li>salami001484 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001484')">Plot</button></li>
                                <li>salami001485 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001485')">Plot</button></li>
                                <li>salami001487 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001487')">Plot</button></li>
                                <li>salami001488 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001488')">Plot</button></li>
                                <li>salami001490 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001490')">Plot</button></li>
                                <li>salami001493 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001493')">Plot</button></li>
                                <li>salami001494 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001494')">Plot</button></li>
                                <li>salami001496 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001496')">Plot</button></li>
                            </ul>

                        </div>
                </body>
                </html>
