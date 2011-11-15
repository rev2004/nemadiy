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
                            <h3>Segmentation Plots</h3>   <a href="index_2.jsp">prev</a>|<a href="index_4.jsp">next</a>
                            <ul>
                                <li>salami000895 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000895')">Plot</button></li>
                                <li>salami000897 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000897')">Plot</button></li>
                                <li>salami000898 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000898')">Plot</button></li>
                                <li>salami000899 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000899')">Plot</button></li>
                                <li>salami000900 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000900')">Plot</button></li>
                                <li>salami000901 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000901')">Plot</button></li>
                                <li>salami000904 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000904')">Plot</button></li>
                                <li>salami000905 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000905')">Plot</button></li>
                                <li>salami000906 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000906')">Plot</button></li>
                                <li>salami000907 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000907')">Plot</button></li>
                                <li>salami000908 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000908')">Plot</button></li>
                                <li>salami000910 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000910')">Plot</button></li>
                                <li>salami000912 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000912')">Plot</button></li>
                                <li>salami000914 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000914')">Plot</button></li>
                                <li>salami000915 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000915')">Plot</button></li>
                                <li>salami000917 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000917')">Plot</button></li>
                                <li>salami000918 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000918')">Plot</button></li>
                                <li>salami000920 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000920')">Plot</button></li>
                                <li>salami000921 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000921')">Plot</button></li>
                                <li>salami000922 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000922')">Plot</button></li>
                                <li>salami000923 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000923')">Plot</button></li>
                                <li>salami000924 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000924')">Plot</button></li>
                                <li>salami000925 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000925')">Plot</button></li>
                                <li>salami000928 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000928')">Plot</button></li>
                                <li>salami000929 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000929')">Plot</button></li>
                                <li>salami000932 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000932')">Plot</button></li>
                                <li>salami000934 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000934')">Plot</button></li>
                                <li>salami000936 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000936')">Plot</button></li>
                                <li>salami000937 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000937')">Plot</button></li>
                                <li>salami000938 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000938')">Plot</button></li>
                                <li>salami000941 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000941')">Plot</button></li>
                                <li>salami000942 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000942')">Plot</button></li>
                                <li>salami000944 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000944')">Plot</button></li>
                                <li>salami000947 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000947')">Plot</button></li>
                                <li>salami000948 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000948')">Plot</button></li>
                                <li>salami000949 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000949')">Plot</button></li>
                                <li>salami000951 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000951')">Plot</button></li>
                                <li>salami000952 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000952')">Plot</button></li>
                                <li>salami000954 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000954')">Plot</button></li>
                                <li>salami000955 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000955')">Plot</button></li>
                                <li>salami000957 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000957')">Plot</button></li>
                                <li>salami000958 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000958')">Plot</button></li>
                                <li>salami000959 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000959')">Plot</button></li>
                                <li>salami000960 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000960')">Plot</button></li>
                                <li>salami000961 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000961')">Plot</button></li>
                                <li>salami000962 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000962')">Plot</button></li>
                                <li>salami000963 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000963')">Plot</button></li>
                                <li>salami000964 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000964')">Plot</button></li>
                                <li>salami000965 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000965')">Plot</button></li>
                                <li>salami000967 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000967')">Plot</button></li>
                                <li>salami000968 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000968')">Plot</button></li>
                                <li>salami000969 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000969')">Plot</button></li>
                                <li>salami000970 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000970')">Plot</button></li>
                                <li>salami000974 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000974')">Plot</button></li>
                                <li>salami000975 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000975')">Plot</button></li>
                                <li>salami000976 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000976')">Plot</button></li>
                                <li>salami000982 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000982')">Plot</button></li>
                                <li>salami000985 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000985')">Plot</button></li>
                                <li>salami000986 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000986')">Plot</button></li>
                                <li>salami000988 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000988')">Plot</button></li>
                                <li>salami000990 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000990')">Plot</button></li>
                                <li>salami000992 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000992')">Plot</button></li>
                                <li>salami000993 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000993')">Plot</button></li>
                                <li>salami000994 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000994')">Plot</button></li>
                                <li>salami000996 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000996')">Plot</button></li>
                                <li>salami000997 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000997')">Plot</button></li>
                                <li>salami000998 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000998')">Plot</button></li>
                                <li>salami000999 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000999')">Plot</button></li>
                                <li>salami001000 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001000')">Plot</button></li>
                                <li>salami001001 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001001')">Plot</button></li>
                                <li>salami001003 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001003')">Plot</button></li>
                                <li>salami001004 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001004')">Plot</button></li>
                                <li>salami001005 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001005')">Plot</button></li>
                                <li>salami001006 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001006')">Plot</button></li>
                                <li>salami001008 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001008')">Plot</button></li>
                                <li>salami001011 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001011')">Plot</button></li>
                                <li>salami001012 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001012')">Plot</button></li>
                                <li>salami001014 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001014')">Plot</button></li>
                                <li>salami001015 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001015')">Plot</button></li>
                                <li>salami001017 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001017')">Plot</button></li>
                                <li>salami001018 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001018')">Plot</button></li>
                                <li>salami001019 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001019')">Plot</button></li>
                                <li>salami001020 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001020')">Plot</button></li>
                                <li>salami001021 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001021')">Plot</button></li>
                                <li>salami001022 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001022')">Plot</button></li>
                                <li>salami001023 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001023')">Plot</button></li>
                                <li>salami001025 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001025')">Plot</button></li>
                                <li>salami001026 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001026')">Plot</button></li>
                                <li>salami001027 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001027')">Plot</button></li>
                                <li>salami001028 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001028')">Plot</button></li>
                                <li>salami001030 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001030')">Plot</button></li>
                                <li>salami001031 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001031')">Plot</button></li>
                                <li>salami001032 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001032')">Plot</button></li>
                                <li>salami001033 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001033')">Plot</button></li>
                                <li>salami001034 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001034')">Plot</button></li>
                                <li>salami001036 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001036')">Plot</button></li>
                                <li>salami001037 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001037')">Plot</button></li>
                                <li>salami001038 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001038')">Plot</button></li>
                                <li>salami001039 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001039')">Plot</button></li>
                                <li>salami001042 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001042')">Plot</button></li>
                                <li>salami001043 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001043')">Plot</button></li>
                                <li>salami001044 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001044')">Plot</button></li>
                                <li>salami001045 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001045')">Plot</button></li>
                                <li>salami001046 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001046')">Plot</button></li>
                                <li>salami001049 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001049')">Plot</button></li>
                                <li>salami001050 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001050')">Plot</button></li>
                                <li>salami001052 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001052')">Plot</button></li>
                                <li>salami001053 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001053')">Plot</button></li>
                                <li>salami001055 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001055')">Plot</button></li>
                                <li>salami001057 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001057')">Plot</button></li>
                                <li>salami001058 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001058')">Plot</button></li>
                                <li>salami001061 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001061')">Plot</button></li>
                                <li>salami001064 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001064')">Plot</button></li>
                                <li>salami001065 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001065')">Plot</button></li>
                                <li>salami001067 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001067')">Plot</button></li>
                                <li>salami001068 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001068')">Plot</button></li>
                                <li>salami001069 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001069')">Plot</button></li>
                                <li>salami001072 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001072')">Plot</button></li>
                                <li>salami001075 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001075')">Plot</button></li>
                                <li>salami001076 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001076')">Plot</button></li>
                                <li>salami001077 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001077')">Plot</button></li>
                                <li>salami001078 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001078')">Plot</button></li>
                                <li>salami001079 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001079')">Plot</button></li>
                                <li>salami001080 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001080')">Plot</button></li>
                                <li>salami001081 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001081')">Plot</button></li>
                                <li>salami001082 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001082')">Plot</button></li>
                                <li>salami001083 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001083')">Plot</button></li>
                                <li>salami001085 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001085')">Plot</button></li>
                                <li>salami001087 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001087')">Plot</button></li>
                                <li>salami001088 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001088')">Plot</button></li>
                                <li>salami001091 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001091')">Plot</button></li>
                                <li>salami001092 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001092')">Plot</button></li>
                                <li>salami001093 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001093')">Plot</button></li>
                                <li>salami001094 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001094')">Plot</button></li>
                                <li>salami001095 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001095')">Plot</button></li>
                                <li>salami001097 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001097')">Plot</button></li>
                                <li>salami001098 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001098')">Plot</button></li>
                                <li>salami001100 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001100')">Plot</button></li>
                                <li>salami001105 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001105')">Plot</button></li>
                                <li>salami001106 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001106')">Plot</button></li>
                                <li>salami001107 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001107')">Plot</button></li>
                                <li>salami001108 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001108')">Plot</button></li>
                                <li>salami001109 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001109')">Plot</button></li>
                                <li>salami001110 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001110')">Plot</button></li>
                                <li>salami001114 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001114')">Plot</button></li>
                                <li>salami001117 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001117')">Plot</button></li>
                                <li>salami001118 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001118')">Plot</button></li>
                                <li>salami001122 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001122')">Plot</button></li>
                                <li>salami001126 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001126')">Plot</button></li>
                                <li>salami001127 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001127')">Plot</button></li>
                                <li>salami001129 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001129')">Plot</button></li>
                                <li>salami001131 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001131')">Plot</button></li>
                                <li>salami001134 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001134')">Plot</button></li>
                                <li>salami001135 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001135')">Plot</button></li>
                                <li>salami001136 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001136')">Plot</button></li>
                                <li>salami001137 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001137')">Plot</button></li>
                                <li>salami001138 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001138')">Plot</button></li>
                                <li>salami001139 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001139')">Plot</button></li>
                                <li>salami001140 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001140')">Plot</button></li>
                                <li>salami001141 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001141')">Plot</button></li>
                                <li>salami001144 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001144')">Plot</button></li>
                                <li>salami001145 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001145')">Plot</button></li>
                                <li>salami001146 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001146')">Plot</button></li>
                                <li>salami001148 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001148')">Plot</button></li>
                                <li>salami001149 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001149')">Plot</button></li>
                                <li>salami001150 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001150')">Plot</button></li>
                                <li>salami001151 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001151')">Plot</button></li>
                                <li>salami001152 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001152')">Plot</button></li>
                                <li>salami001153 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001153')">Plot</button></li>
                                <li>salami001155 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001155')">Plot</button></li>
                                <li>salami001157 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001157')">Plot</button></li>
                                <li>salami001158 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001158')">Plot</button></li>
                                <li>salami001159 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001159')">Plot</button></li>
                                <li>salami001160 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001160')">Plot</button></li>
                                <li>salami001163 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001163')">Plot</button></li>
                                <li>salami001164 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001164')">Plot</button></li>
                                <li>salami001165 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001165')">Plot</button></li>
                                <li>salami001166 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001166')">Plot</button></li>
                                <li>salami001167 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001167')">Plot</button></li>
                                <li>salami001169 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001169')">Plot</button></li>
                                <li>salami001170 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001170')">Plot</button></li>
                                <li>salami001171 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001171')">Plot</button></li>
                                <li>salami001172 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001172')">Plot</button></li>
                                <li>salami001173 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001173')">Plot</button></li>
                                <li>salami001174 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001174')">Plot</button></li>
                                <li>salami001175 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001175')">Plot</button></li>
                                <li>salami001177 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001177')">Plot</button></li>
                                <li>salami001178 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001178')">Plot</button></li>
                                <li>salami001179 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001179')">Plot</button></li>
                                <li>salami001180 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001180')">Plot</button></li>
                                <li>salami001181 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001181')">Plot</button></li>
                                <li>salami001182 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001182')">Plot</button></li>
                                <li>salami001184 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001184')">Plot</button></li>
                                <li>salami001186 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001186')">Plot</button></li>
                                <li>salami001187 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001187')">Plot</button></li>
                                <li>salami001189 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001189')">Plot</button></li>
                                <li>salami001190 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001190')">Plot</button></li>
                                <li>salami001191 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001191')">Plot</button></li>
                                <li>salami001192 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001192')">Plot</button></li>
                                <li>salami001193 <button onclick="window.open('DemoJune/plot.jsp?plot=salami001193')">Plot</button></li>
                            </ul>

                        </div>
                </body>
                </html>
