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
                            <h3>Segmentation Plots</h3>   prev|<a href="index_1.jsp">next</a>
                            <ul>
                                <li>salami000000 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000000')">Plot</button></li>
                                <li>salami000004 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000004')">Plot</button></li>
                                <li>salami000005 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000005')">Plot</button></li>
                                <li>salami000006 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000006')">Plot</button></li>
                                <li>salami000007 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000007')">Plot</button></li>
                                <li>salami000008 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000008')">Plot</button></li>
                                <li>salami000009 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000009')">Plot</button></li>
                                <li>salami000010 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000010')">Plot</button></li>
                                <li>salami000011 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000011')">Plot</button></li>
                                <li>salami000013 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000013')">Plot</button></li>
                                <li>salami000014 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000014')">Plot</button></li>
                                <li>salami000015 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000015')">Plot</button></li>
                                <li>salami000016 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000016')">Plot</button></li>
                                <li>salami000017 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000017')">Plot</button></li>
                                <li>salami000019 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000019')">Plot</button></li>
                                <li>salami000020 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000020')">Plot</button></li>
                                <li>salami000021 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000021')">Plot</button></li>
                                <li>salami000022 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000022')">Plot</button></li>
                                <li>salami000023 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000023')">Plot</button></li>
                                <li>salami000024 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000024')">Plot</button></li>
                                <li>salami000025 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000025')">Plot</button></li>
                                <li>salami000028 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000028')">Plot</button></li>
                                <li>salami000029 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000029')">Plot</button></li>
                                <li>salami000030 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000030')">Plot</button></li>
                                <li>salami000033 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000033')">Plot</button></li>
                                <li>salami000034 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000034')">Plot</button></li>
                                <li>salami000035 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000035')">Plot</button></li>
                                <li>salami000036 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000036')">Plot</button></li>
                                <li>salami000037 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000037')">Plot</button></li>
                                <li>salami000038 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000038')">Plot</button></li>
                                <li>salami000042 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000042')">Plot</button></li>
                                <li>salami000043 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000043')">Plot</button></li>
                                <li>salami000044 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000044')">Plot</button></li>
                                <li>salami000046 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000046')">Plot</button></li>
                                <li>salami000047 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000047')">Plot</button></li>
                                <li>salami000048 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000048')">Plot</button></li>
                                <li>salami000049 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000049')">Plot</button></li>
                                <li>salami000053 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000053')">Plot</button></li>
                                <li>salami000054 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000054')">Plot</button></li>
                                <li>salami000055 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000055')">Plot</button></li>
                                <li>salami000056 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000056')">Plot</button></li>
                                <li>salami000060 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000060')">Plot</button></li>
                                <li>salami000062 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000062')">Plot</button></li>
                                <li>salami000063 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000063')">Plot</button></li>
                                <li>salami000064 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000064')">Plot</button></li>
                                <li>salami000066 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000066')">Plot</button></li>
                                <li>salami000067 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000067')">Plot</button></li>
                                <li>salami000068 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000068')">Plot</button></li>
                                <li>salami000069 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000069')">Plot</button></li>
                                <li>salami000070 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000070')">Plot</button></li>
                                <li>salami000079 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000079')">Plot</button></li>
                                <li>salami000080 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000080')">Plot</button></li>
                                <li>salami000081 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000081')">Plot</button></li>
                                <li>salami000083 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000083')">Plot</button></li>
                                <li>salami000086 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000086')">Plot</button></li>
                                <li>salami000089 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000089')">Plot</button></li>
                                <li>salami000090 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000090')">Plot</button></li>
                                <li>salami000091 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000091')">Plot</button></li>
                                <li>salami000092 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000092')">Plot</button></li>
                                <li>salami000093 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000093')">Plot</button></li>
                                <li>salami000094 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000094')">Plot</button></li>
                                <li>salami000095 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000095')">Plot</button></li>
                                <li>salami000096 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000096')">Plot</button></li>
                                <li>salami000097 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000097')">Plot</button></li>
                                <li>salami000098 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000098')">Plot</button></li>
                                <li>salami000099 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000099')">Plot</button></li>
                                <li>salami000100 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000100')">Plot</button></li>
                                <li>salami000101 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000101')">Plot</button></li>
                                <li>salami000105 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000105')">Plot</button></li>
                                <li>salami000106 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000106')">Plot</button></li>
                                <li>salami000108 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000108')">Plot</button></li>
                                <li>salami000113 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000113')">Plot</button></li>
                                <li>salami000119 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000119')">Plot</button></li>
                                <li>salami000120 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000120')">Plot</button></li>
                                <li>salami000121 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000121')">Plot</button></li>
                                <li>salami000122 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000122')">Plot</button></li>
                                <li>salami000123 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000123')">Plot</button></li>
                                <li>salami000124 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000124')">Plot</button></li>
                                <li>salami000125 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000125')">Plot</button></li>
                                <li>salami000126 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000126')">Plot</button></li>
                                <li>salami000127 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000127')">Plot</button></li>
                                <li>salami000128 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000128')">Plot</button></li>
                                <li>salami000129 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000129')">Plot</button></li>
                                <li>salami000131 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000131')">Plot</button></li>
                                <li>salami000132 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000132')">Plot</button></li>
                                <li>salami000135 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000135')">Plot</button></li>
                                <li>salami000139 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000139')">Plot</button></li>
                                <li>salami000140 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000140')">Plot</button></li>
                                <li>salami000141 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000141')">Plot</button></li>
                                <li>salami000142 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000142')">Plot</button></li>
                                <li>salami000143 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000143')">Plot</button></li>
                                <li>salami000144 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000144')">Plot</button></li>
                                <li>salami000145 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000145')">Plot</button></li>
                                <li>salami000146 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000146')">Plot</button></li>
                                <li>salami000148 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000148')">Plot</button></li>
                                <li>salami000150 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000150')">Plot</button></li>
                                <li>salami000151 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000151')">Plot</button></li>
                                <li>salami000153 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000153')">Plot</button></li>
                                <li>salami000158 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000158')">Plot</button></li>
                                <li>salami000161 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000161')">Plot</button></li>
                                <li>salami000162 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000162')">Plot</button></li>
                                <li>salami000163 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000163')">Plot</button></li>
                                <li>salami000166 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000166')">Plot</button></li>
                                <li>salami000168 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000168')">Plot</button></li>
                                <li>salami000169 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000169')">Plot</button></li>
                                <li>salami000170 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000170')">Plot</button></li>
                                <li>salami000171 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000171')">Plot</button></li>
                                <li>salami000173 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000173')">Plot</button></li>
                                <li>salami000175 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000175')">Plot</button></li>
                                <li>salami000176 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000176')">Plot</button></li>
                                <li>salami000177 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000177')">Plot</button></li>
                                <li>salami000179 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000179')">Plot</button></li>
                                <li>salami000180 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000180')">Plot</button></li>
                                <li>salami000182 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000182')">Plot</button></li>
                                <li>salami000183 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000183')">Plot</button></li>
                                <li>salami000185 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000185')">Plot</button></li>
                                <li>salami000186 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000186')">Plot</button></li>
                                <li>salami000187 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000187')">Plot</button></li>
                                <li>salami000188 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000188')">Plot</button></li>
                                <li>salami000190 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000190')">Plot</button></li>
                                <li>salami000191 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000191')">Plot</button></li>
                                <li>salami000192 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000192')">Plot</button></li>
                                <li>salami000193 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000193')">Plot</button></li>
                                <li>salami000196 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000196')">Plot</button></li>
                                <li>salami000197 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000197')">Plot</button></li>
                                <li>salami000198 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000198')">Plot</button></li>
                                <li>salami000199 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000199')">Plot</button></li>
                                <li>salami000200 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000200')">Plot</button></li>
                                <li>salami000202 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000202')">Plot</button></li>
                                <li>salami000203 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000203')">Plot</button></li>
                                <li>salami000204 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000204')">Plot</button></li>
                                <li>salami000205 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000205')">Plot</button></li>
                                <li>salami000207 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000207')">Plot</button></li>
                                <li>salami000208 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000208')">Plot</button></li>
                                <li>salami000209 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000209')">Plot</button></li>
                                <li>salami000212 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000212')">Plot</button></li>
                                <li>salami000213 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000213')">Plot</button></li>
                                <li>salami000214 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000214')">Plot</button></li>
                                <li>salami000215 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000215')">Plot</button></li>
                                <li>salami000216 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000216')">Plot</button></li>
                                <li>salami000217 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000217')">Plot</button></li>
                                <li>salami000218 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000218')">Plot</button></li>
                                <li>salami000219 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000219')">Plot</button></li>
                                <li>salami000220 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000220')">Plot</button></li>
                                <li>salami000221 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000221')">Plot</button></li>
                                <li>salami000222 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000222')">Plot</button></li>
                                <li>salami000223 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000223')">Plot</button></li>
                                <li>salami000224 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000224')">Plot</button></li>
                                <li>salami000225 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000225')">Plot</button></li>
                                <li>salami000226 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000226')">Plot</button></li>
                                <li>salami000227 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000227')">Plot</button></li>
                                <li>salami000231 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000231')">Plot</button></li>
                                <li>salami000232 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000232')">Plot</button></li>
                                <li>salami000233 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000233')">Plot</button></li>
                                <li>salami000234 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000234')">Plot</button></li>
                                <li>salami000235 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000235')">Plot</button></li>
                                <li>salami000237 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000237')">Plot</button></li>
                                <li>salami000238 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000238')">Plot</button></li>
                                <li>salami000239 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000239')">Plot</button></li>
                                <li>salami000240 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000240')">Plot</button></li>
                                <li>salami000241 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000241')">Plot</button></li>
                                <li>salami000242 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000242')">Plot</button></li>
                                <li>salami000244 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000244')">Plot</button></li>
                                <li>salami000245 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000245')">Plot</button></li>
                                <li>salami000246 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000246')">Plot</button></li>
                                <li>salami000247 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000247')">Plot</button></li>
                                <li>salami000248 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000248')">Plot</button></li>
                                <li>salami000249 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000249')">Plot</button></li>
                                <li>salami000252 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000252')">Plot</button></li>
                                <li>salami000253 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000253')">Plot</button></li>
                                <li>salami000254 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000254')">Plot</button></li>
                                <li>salami000255 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000255')">Plot</button></li>
                                <li>salami000256 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000256')">Plot</button></li>
                                <li>salami000258 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000258')">Plot</button></li>
                                <li>salami000261 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000261')">Plot</button></li>
                                <li>salami000262 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000262')">Plot</button></li>
                                <li>salami000263 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000263')">Plot</button></li>
                                <li>salami000264 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000264')">Plot</button></li>
                                <li>salami000265 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000265')">Plot</button></li>
                                <li>salami000267 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000267')">Plot</button></li>
                                <li>salami000269 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000269')">Plot</button></li>
                                <li>salami000270 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000270')">Plot</button></li>
                                <li>salami000271 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000271')">Plot</button></li>
                                <li>salami000272 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000272')">Plot</button></li>
                                <li>salami000273 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000273')">Plot</button></li>
                                <li>salami000274 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000274')">Plot</button></li>
                                <li>salami000275 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000275')">Plot</button></li>
                                <li>salami000277 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000277')">Plot</button></li>
                                <li>salami000280 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000280')">Plot</button></li>
                                <li>salami000281 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000281')">Plot</button></li>
                                <li>salami000285 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000285')">Plot</button></li>
                                <li>salami000288 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000288')">Plot</button></li>
                                <li>salami000289 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000289')">Plot</button></li>
                                <li>salami000290 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000290')">Plot</button></li>
                                <li>salami000291 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000291')">Plot</button></li>
                                <li>salami000292 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000292')">Plot</button></li>
                                <li>salami000294 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000294')">Plot</button></li>
                                <li>salami000296 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000296')">Plot</button></li>
                                <li>salami000297 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000297')">Plot</button></li>
                                <li>salami000299 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000299')">Plot</button></li>
                            </ul>

                        </div>
                </body>
                </html>
