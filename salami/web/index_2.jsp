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
                            <h3>Segmentation Plots</h3>   <a href="index_1.jsp">prev</a>|<a href="index_3.jsp">next</a>
                            <ul>
                                <li>salami000608 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000608')">Plot</button></li>
                                <li>salami000609 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000609')">Plot</button></li>
                                <li>salami000610 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000610')">Plot</button></li>
                                <li>salami000611 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000611')">Plot</button></li>
                                <li>salami000613 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000613')">Plot</button></li>
                                <li>salami000614 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000614')">Plot</button></li>
                                <li>salami000615 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000615')">Plot</button></li>
                                <li>salami000616 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000616')">Plot</button></li>
                                <li>salami000618 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000618')">Plot</button></li>
                                <li>salami000619 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000619')">Plot</button></li>
                                <li>salami000620 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000620')">Plot</button></li>
                                <li>salami000621 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000621')">Plot</button></li>
                                <li>salami000622 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000622')">Plot</button></li>
                                <li>salami000624 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000624')">Plot</button></li>
                                <li>salami000625 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000625')">Plot</button></li>
                                <li>salami000626 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000626')">Plot</button></li>
                                <li>salami000627 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000627')">Plot</button></li>
                                <li>salami000628 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000628')">Plot</button></li>
                                <li>salami000629 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000629')">Plot</button></li>
                                <li>salami000630 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000630')">Plot</button></li>
                                <li>salami000631 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000631')">Plot</button></li>
                                <li>salami000632 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000632')">Plot</button></li>
                                <li>salami000633 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000633')">Plot</button></li>
                                <li>salami000635 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000635')">Plot</button></li>
                                <li>salami000636 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000636')">Plot</button></li>
                                <li>salami000637 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000637')">Plot</button></li>
                                <li>salami000639 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000639')">Plot</button></li>
                                <li>salami000640 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000640')">Plot</button></li>
                                <li>salami000642 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000642')">Plot</button></li>
                                <li>salami000643 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000643')">Plot</button></li>
                                <li>salami000644 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000644')">Plot</button></li>
                                <li>salami000646 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000646')">Plot</button></li>
                                <li>salami000649 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000649')">Plot</button></li>
                                <li>salami000651 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000651')">Plot</button></li>
                                <li>salami000652 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000652')">Plot</button></li>
                                <li>salami000653 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000653')">Plot</button></li>
                                <li>salami000654 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000654')">Plot</button></li>
                                <li>salami000656 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000656')">Plot</button></li>
                                <li>salami000657 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000657')">Plot</button></li>
                                <li>salami000659 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000659')">Plot</button></li>
                                <li>salami000660 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000660')">Plot</button></li>
                                <li>salami000662 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000662')">Plot</button></li>
                                <li>salami000663 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000663')">Plot</button></li>
                                <li>salami000664 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000664')">Plot</button></li>
                                <li>salami000666 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000666')">Plot</button></li>
                                <li>salami000667 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000667')">Plot</button></li>
                                <li>salami000669 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000669')">Plot</button></li>
                                <li>salami000670 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000670')">Plot</button></li>
                                <li>salami000671 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000671')">Plot</button></li>
                                <li>salami000673 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000673')">Plot</button></li>
                                <li>salami000674 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000674')">Plot</button></li>
                                <li>salami000675 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000675')">Plot</button></li>
                                <li>salami000676 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000676')">Plot</button></li>
                                <li>salami000677 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000677')">Plot</button></li>
                                <li>salami000678 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000678')">Plot</button></li>
                                <li>salami000679 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000679')">Plot</button></li>
                                <li>salami000681 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000681')">Plot</button></li>
                                <li>salami000682 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000682')">Plot</button></li>
                                <li>salami000683 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000683')">Plot</button></li>
                                <li>salami000684 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000684')">Plot</button></li>
                                <li>salami000686 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000686')">Plot</button></li>
                                <li>salami000687 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000687')">Plot</button></li>
                                <li>salami000688 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000688')">Plot</button></li>
                                <li>salami000689 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000689')">Plot</button></li>
                                <li>salami000691 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000691')">Plot</button></li>
                                <li>salami000694 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000694')">Plot</button></li>
                                <li>salami000695 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000695')">Plot</button></li>
                                <li>salami000696 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000696')">Plot</button></li>
                                <li>salami000697 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000697')">Plot</button></li>
                                <li>salami000698 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000698')">Plot</button></li>
                                <li>salami000700 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000700')">Plot</button></li>
                                <li>salami000701 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000701')">Plot</button></li>
                                <li>salami000704 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000704')">Plot</button></li>
                                <li>salami000707 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000707')">Plot</button></li>
                                <li>salami000710 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000710')">Plot</button></li>
                                <li>salami000712 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000712')">Plot</button></li>
                                <li>salami000714 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000714')">Plot</button></li>
                                <li>salami000715 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000715')">Plot</button></li>
                                <li>salami000718 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000718')">Plot</button></li>
                                <li>salami000719 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000719')">Plot</button></li>
                                <li>salami000721 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000721')">Plot</button></li>
                                <li>salami000722 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000722')">Plot</button></li>
                                <li>salami000723 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000723')">Plot</button></li>
                                <li>salami000724 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000724')">Plot</button></li>
                                <li>salami000725 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000725')">Plot</button></li>
                                <li>salami000726 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000726')">Plot</button></li>
                                <li>salami000729 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000729')">Plot</button></li>
                                <li>salami000731 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000731')">Plot</button></li>
                                <li>salami000732 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000732')">Plot</button></li>
                                <li>salami000733 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000733')">Plot</button></li>
                                <li>salami000734 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000734')">Plot</button></li>
                                <li>salami000735 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000735')">Plot</button></li>
                                <li>salami000737 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000737')">Plot</button></li>
                                <li>salami000738 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000738')">Plot</button></li>
                                <li>salami000739 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000739')">Plot</button></li>
                                <li>salami000740 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000740')">Plot</button></li>
                                <li>salami000742 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000742')">Plot</button></li>
                                <li>salami000744 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000744')">Plot</button></li>
                                <li>salami000746 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000746')">Plot</button></li>
                                <li>salami000747 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000747')">Plot</button></li>
                                <li>salami000749 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000749')">Plot</button></li>
                                <li>salami000751 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000751')">Plot</button></li>
                                <li>salami000752 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000752')">Plot</button></li>
                                <li>salami000753 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000753')">Plot</button></li>
                                <li>salami000754 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000754')">Plot</button></li>
                                <li>salami000755 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000755')">Plot</button></li>
                                <li>salami000758 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000758')">Plot</button></li>
                                <li>salami000759 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000759')">Plot</button></li>
                                <li>salami000760 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000760')">Plot</button></li>
                                <li>salami000761 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000761')">Plot</button></li>
                                <li>salami000762 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000762')">Plot</button></li>
                                <li>salami000764 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000764')">Plot</button></li>
                                <li>salami000765 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000765')">Plot</button></li>
                                <li>salami000766 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000766')">Plot</button></li>
                                <li>salami000767 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000767')">Plot</button></li>
                                <li>salami000769 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000769')">Plot</button></li>
                                <li>salami000770 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000770')">Plot</button></li>
                                <li>salami000771 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000771')">Plot</button></li>
                                <li>salami000772 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000772')">Plot</button></li>
                                <li>salami000773 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000773')">Plot</button></li>
                                <li>salami000775 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000775')">Plot</button></li>
                                <li>salami000776 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000776')">Plot</button></li>
                                <li>salami000778 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000778')">Plot</button></li>
                                <li>salami000779 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000779')">Plot</button></li>
                                <li>salami000780 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000780')">Plot</button></li>
                                <li>salami000781 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000781')">Plot</button></li>
                                <li>salami000782 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000782')">Plot</button></li>
                                <li>salami000784 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000784')">Plot</button></li>
                                <li>salami000786 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000786')">Plot</button></li>
                                <li>salami000787 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000787')">Plot</button></li>
                                <li>salami000788 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000788')">Plot</button></li>
                                <li>salami000789 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000789')">Plot</button></li>
                                <li>salami000790 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000790')">Plot</button></li>
                                <li>salami000792 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000792')">Plot</button></li>
                                <li>salami000793 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000793')">Plot</button></li>
                                <li>salami000794 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000794')">Plot</button></li>
                                <li>salami000795 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000795')">Plot</button></li>
                                <li>salami000796 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000796')">Plot</button></li>
                                <li>salami000797 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000797')">Plot</button></li>
                                <li>salami000798 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000798')">Plot</button></li>
                                <li>salami000799 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000799')">Plot</button></li>
                                <li>salami000801 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000801')">Plot</button></li>
                                <li>salami000804 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000804')">Plot</button></li>
                                <li>salami000807 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000807')">Plot</button></li>
                                <li>salami000808 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000808')">Plot</button></li>
                                <li>salami000810 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000810')">Plot</button></li>
                                <li>salami000813 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000813')">Plot</button></li>
                                <li>salami000816 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000816')">Plot</button></li>
                                <li>salami000818 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000818')">Plot</button></li>
                                <li>salami000819 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000819')">Plot</button></li>
                                <li>salami000820 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000820')">Plot</button></li>
                                <li>salami000821 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000821')">Plot</button></li>
                                <li>salami000822 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000822')">Plot</button></li>
                                <li>salami000825 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000825')">Plot</button></li>
                                <li>salami000827 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000827')">Plot</button></li>
                                <li>salami000828 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000828')">Plot</button></li>
                                <li>salami000831 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000831')">Plot</button></li>
                                <li>salami000832 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000832')">Plot</button></li>
                                <li>salami000834 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000834')">Plot</button></li>
                                <li>salami000836 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000836')">Plot</button></li>
                                <li>salami000837 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000837')">Plot</button></li>
                                <li>salami000838 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000838')">Plot</button></li>
                                <li>salami000839 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000839')">Plot</button></li>
                                <li>salami000840 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000840')">Plot</button></li>
                                <li>salami000841 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000841')">Plot</button></li>
                                <li>salami000842 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000842')">Plot</button></li>
                                <li>salami000843 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000843')">Plot</button></li>
                                <li>salami000844 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000844')">Plot</button></li>
                                <li>salami000845 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000845')">Plot</button></li>
                                <li>salami000846 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000846')">Plot</button></li>
                                <li>salami000847 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000847')">Plot</button></li>
                                <li>salami000849 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000849')">Plot</button></li>
                                <li>salami000850 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000850')">Plot</button></li>
                                <li>salami000852 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000852')">Plot</button></li>
                                <li>salami000853 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000853')">Plot</button></li>
                                <li>salami000854 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000854')">Plot</button></li>
                                <li>salami000855 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000855')">Plot</button></li>
                                <li>salami000860 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000860')">Plot</button></li>
                                <li>salami000862 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000862')">Plot</button></li>
                                <li>salami000863 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000863')">Plot</button></li>
                                <li>salami000866 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000866')">Plot</button></li>
                                <li>salami000867 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000867')">Plot</button></li>
                                <li>salami000868 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000868')">Plot</button></li>
                                <li>salami000870 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000870')">Plot</button></li>
                                <li>salami000872 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000872')">Plot</button></li>
                                <li>salami000873 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000873')">Plot</button></li>
                                <li>salami000875 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000875')">Plot</button></li>
                                <li>salami000877 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000877')">Plot</button></li>
                                <li>salami000879 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000879')">Plot</button></li>
                                <li>salami000881 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000881')">Plot</button></li>
                                <li>salami000882 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000882')">Plot</button></li>
                                <li>salami000884 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000884')">Plot</button></li>
                                <li>salami000886 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000886')">Plot</button></li>
                                <li>salami000887 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000887')">Plot</button></li>
                                <li>salami000888 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000888')">Plot</button></li>
                                <li>salami000889 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000889')">Plot</button></li>
                                <li>salami000890 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000890')">Plot</button></li>
                                <li>salami000891 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000891')">Plot</button></li>
                                <li>salami000892 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000892')">Plot</button></li>
                                <li>salami000894 <button onclick="window.open('DemoJune/plot.jsp?plot=salami000894')">Plot</button></li>
                            </ul>

                        </div>
                </body>
                </html>
