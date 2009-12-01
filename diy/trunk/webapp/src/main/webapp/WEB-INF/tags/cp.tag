<%@ page language="java" import="org.imirsel.nema.annotatons.parser.beans.*, java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>

<%@ attribute name="component" required="true" type="java.lang.String" %>
<%@ attribute name="value" required="true" type="org.imirsel.nema.model.Property" %>
<%@ tag dynamic-attributes="map"%>

<%
List<DataTypeBean> ltb =value.getDataTypeBeanList();
%>
<%
if(ltb.isEmpty()){
%>

<%}else{%>

<%}%>