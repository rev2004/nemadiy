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

        <LINK REL=StyleSheet HREF="support/menu.css" TYPE="text/css" />
            <LINK REL=StyleSheet HREF="support/tableblue.css" TYPE="text/css" />

                <script type="text/javascript" src="support/jquery.min.js"></script>

       <!--[if !mso]>
<style>
v\:* {behavior:url(#default#VML);}
o\:* {behavior:url(#default#VML);}
w\:* {behavior:url(#default#VML);}
.shape {behavior:url(#default#VML);}
</style>
<![endif]--><!--[if gte mso 9]><xml>
 <o:DocumentProperties>
  <o:Author>Mert</o:Author>
  <o:LastAuthor>Mert</o:LastAuthor>
  <o:Revision>15</o:Revision>
  <o:TotalTime>62</o:TotalTime>
  <o:Created>2011-06-09T01:51:00Z</o:Created>
  <o:LastSaved>2011-06-09T02:42:00Z</o:LastSaved>
  <o:Pages>2</o:Pages>
  <o:Words>456</o:Words>
  <o:Characters>2602</o:Characters>
  <o:Lines>21</o:Lines>
  <o:Paragraphs>6</o:Paragraphs>
  <o:CharactersWithSpaces>3052</o:CharactersWithSpaces>
  <o:Version>12.00</o:Version>
 </o:DocumentProperties>
</xml><![endif]-->
<link rel=themeData href="ismir_tables_files/themedata.thmx">
<link rel=colorSchemeMapping href="ismir_tables_files/colorschememapping.xml">
<!--[if gte mso 9]><xml>
 <w:WordDocument>
  <w:TrackMoves>false</w:TrackMoves>
  <w:TrackFormatting/>
  <w:PunctuationKerning/>
  <w:ValidateAgainstSchemas/>
  <w:SaveIfXMLInvalid>false</w:SaveIfXMLInvalid>
  <w:IgnoreMixedContent>false</w:IgnoreMixedContent>
  <w:AlwaysShowPlaceholderText>false</w:AlwaysShowPlaceholderText>
  <w:DoNotPromoteQF/>
  <w:LidThemeOther>EN-US</w:LidThemeOther>
  <w:LidThemeAsian>X-NONE</w:LidThemeAsian>
  <w:LidThemeComplexScript>X-NONE</w:LidThemeComplexScript>
  <w:Compatibility>
   <w:BreakWrappedTables/>
   <w:SnapToGridInCell/>
   <w:WrapTextWithPunct/>
   <w:UseAsianBreakRules/>
   <w:DontGrowAutofit/>
   <w:SplitPgBreakAndParaMark/>
   <w:DontVertAlignCellWithSp/>
   <w:DontBreakConstrainedForcedTables/>
   <w:DontVertAlignInTxbx/>
   <w:Word11KerningPairs/>
   <w:CachedColBalance/>
  </w:Compatibility>
  <w:BrowserLevel>MicrosoftInternetExplorer4</w:BrowserLevel>
  <m:mathPr>
   <m:mathFont m:val="Cambria Math"/>
   <m:brkBin m:val="before"/>
   <m:brkBinSub m:val="&#45;-"/>
   <m:smallFrac m:val="off"/>
   <m:dispDef/>
   <m:lMargin m:val="0"/>
   <m:rMargin m:val="0"/>
   <m:defJc m:val="centerGroup"/>
   <m:wrapIndent m:val="1440"/>
   <m:intLim m:val="subSup"/>
   <m:naryLim m:val="undOvr"/>
  </m:mathPr></w:WordDocument>
</xml><![endif]--><!--[if gte mso 9]><xml>
 <w:LatentStyles DefLockedState="false" DefUnhideWhenUsed="true"
  DefSemiHidden="true" DefQFormat="false" DefPriority="99"
  LatentStyleCount="267">
  <w:LsdException Locked="false" Priority="0" SemiHidden="false"
   UnhideWhenUsed="false" QFormat="true" Name="Normal"/>
  <w:LsdException Locked="false" Priority="9" SemiHidden="false"
   UnhideWhenUsed="false" QFormat="true" Name="heading 1"/>
  <w:LsdException Locked="false" Priority="9" QFormat="true" Name="heading 2"/>
  <w:LsdException Locked="false" Priority="9" QFormat="true" Name="heading 3"/>
  <w:LsdException Locked="false" Priority="0" QFormat="true" Name="heading 4"/>
  <w:LsdException Locked="false" Priority="0" QFormat="true" Name="heading 5"/>
  <w:LsdException Locked="false" Priority="0" QFormat="true" Name="heading 6"/>
  <w:LsdException Locked="false" Priority="0" QFormat="true" Name="heading 7"/>
  <w:LsdException Locked="false" Priority="0" QFormat="true" Name="heading 8"/>
  <w:LsdException Locked="false" Priority="0" QFormat="true" Name="heading 9"/>
  <w:LsdException Locked="false" Priority="39" Name="toc 1"/>
  <w:LsdException Locked="false" Priority="39" Name="toc 2"/>
  <w:LsdException Locked="false" Priority="39" Name="toc 3"/>
  <w:LsdException Locked="false" Priority="39" Name="toc 4"/>
  <w:LsdException Locked="false" Priority="39" Name="toc 5"/>
  <w:LsdException Locked="false" Priority="39" Name="toc 6"/>
  <w:LsdException Locked="false" Priority="39" Name="toc 7"/>
  <w:LsdException Locked="false" Priority="39" Name="toc 8"/>
  <w:LsdException Locked="false" Priority="39" Name="toc 9"/>
  <w:LsdException Locked="false" Priority="0" Name="footnote text"/>
  <w:LsdException Locked="false" Priority="0" QFormat="true" Name="caption"/>
  <w:LsdException Locked="false" Priority="0" Name="footnote reference"/>
  <w:LsdException Locked="false" Priority="10" SemiHidden="false"
   UnhideWhenUsed="false" QFormat="true" Name="Title"/>
  <w:LsdException Locked="false" Priority="1" Name="Default Paragraph Font"/>
  <w:LsdException Locked="false" Priority="0" Name="Body Text"/>
  <w:LsdException Locked="false" Priority="11" SemiHidden="false"
   UnhideWhenUsed="false" QFormat="true" Name="Subtitle"/>
  <w:LsdException Locked="false" Priority="22" SemiHidden="false"
   UnhideWhenUsed="false" QFormat="true" Name="Strong"/>
  <w:LsdException Locked="false" Priority="20" SemiHidden="false"
   UnhideWhenUsed="false" QFormat="true" Name="Emphasis"/>
  <w:LsdException Locked="false" Priority="0" SemiHidden="false"
   UnhideWhenUsed="false" Name="Table Grid"/>
  <w:LsdException Locked="false" UnhideWhenUsed="false" Name="Placeholder Text"/>
  <w:LsdException Locked="false" Priority="1" SemiHidden="false"
   UnhideWhenUsed="false" QFormat="true" Name="No Spacing"/>
  <w:LsdException Locked="false" Priority="60" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light Shading"/>
  <w:LsdException Locked="false" Priority="61" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light List"/>
  <w:LsdException Locked="false" Priority="62" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light Grid"/>
  <w:LsdException Locked="false" Priority="63" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Shading 1"/>
  <w:LsdException Locked="false" Priority="64" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Shading 2"/>
  <w:LsdException Locked="false" Priority="65" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium List 1"/>
  <w:LsdException Locked="false" Priority="66" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium List 2"/>
  <w:LsdException Locked="false" Priority="67" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 1"/>
  <w:LsdException Locked="false" Priority="68" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 2"/>
  <w:LsdException Locked="false" Priority="69" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 3"/>
  <w:LsdException Locked="false" Priority="70" SemiHidden="false"
   UnhideWhenUsed="false" Name="Dark List"/>
  <w:LsdException Locked="false" Priority="71" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful Shading"/>
  <w:LsdException Locked="false" Priority="72" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful List"/>
  <w:LsdException Locked="false" Priority="73" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful Grid"/>
  <w:LsdException Locked="false" Priority="60" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light Shading Accent 1"/>
  <w:LsdException Locked="false" Priority="61" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light List Accent 1"/>
  <w:LsdException Locked="false" Priority="62" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light Grid Accent 1"/>
  <w:LsdException Locked="false" Priority="63" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Shading 1 Accent 1"/>
  <w:LsdException Locked="false" Priority="64" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Shading 2 Accent 1"/>
  <w:LsdException Locked="false" Priority="65" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium List 1 Accent 1"/>
  <w:LsdException Locked="false" UnhideWhenUsed="false" Name="Revision"/>
  <w:LsdException Locked="false" Priority="34" SemiHidden="false"
   UnhideWhenUsed="false" QFormat="true" Name="List Paragraph"/>
  <w:LsdException Locked="false" Priority="29" SemiHidden="false"
   UnhideWhenUsed="false" QFormat="true" Name="Quote"/>
  <w:LsdException Locked="false" Priority="30" SemiHidden="false"
   UnhideWhenUsed="false" QFormat="true" Name="Intense Quote"/>
  <w:LsdException Locked="false" Priority="66" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium List 2 Accent 1"/>
  <w:LsdException Locked="false" Priority="67" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 1 Accent 1"/>
  <w:LsdException Locked="false" Priority="68" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 2 Accent 1"/>
  <w:LsdException Locked="false" Priority="69" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 3 Accent 1"/>
  <w:LsdException Locked="false" Priority="70" SemiHidden="false"
   UnhideWhenUsed="false" Name="Dark List Accent 1"/>
  <w:LsdException Locked="false" Priority="71" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful Shading Accent 1"/>
  <w:LsdException Locked="false" Priority="72" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful List Accent 1"/>
  <w:LsdException Locked="false" Priority="73" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful Grid Accent 1"/>
  <w:LsdException Locked="false" Priority="60" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light Shading Accent 2"/>
  <w:LsdException Locked="false" Priority="61" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light List Accent 2"/>
  <w:LsdException Locked="false" Priority="62" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light Grid Accent 2"/>
  <w:LsdException Locked="false" Priority="63" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Shading 1 Accent 2"/>
  <w:LsdException Locked="false" Priority="64" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Shading 2 Accent 2"/>
  <w:LsdException Locked="false" Priority="65" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium List 1 Accent 2"/>
  <w:LsdException Locked="false" Priority="66" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium List 2 Accent 2"/>
  <w:LsdException Locked="false" Priority="67" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 1 Accent 2"/>
  <w:LsdException Locked="false" Priority="68" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 2 Accent 2"/>
  <w:LsdException Locked="false" Priority="69" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 3 Accent 2"/>
  <w:LsdException Locked="false" Priority="70" SemiHidden="false"
   UnhideWhenUsed="false" Name="Dark List Accent 2"/>
  <w:LsdException Locked="false" Priority="71" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful Shading Accent 2"/>
  <w:LsdException Locked="false" Priority="72" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful List Accent 2"/>
  <w:LsdException Locked="false" Priority="73" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful Grid Accent 2"/>
  <w:LsdException Locked="false" Priority="60" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light Shading Accent 3"/>
  <w:LsdException Locked="false" Priority="61" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light List Accent 3"/>
  <w:LsdException Locked="false" Priority="62" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light Grid Accent 3"/>
  <w:LsdException Locked="false" Priority="63" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Shading 1 Accent 3"/>
  <w:LsdException Locked="false" Priority="64" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Shading 2 Accent 3"/>
  <w:LsdException Locked="false" Priority="65" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium List 1 Accent 3"/>
  <w:LsdException Locked="false" Priority="66" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium List 2 Accent 3"/>
  <w:LsdException Locked="false" Priority="67" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 1 Accent 3"/>
  <w:LsdException Locked="false" Priority="68" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 2 Accent 3"/>
  <w:LsdException Locked="false" Priority="69" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 3 Accent 3"/>
  <w:LsdException Locked="false" Priority="70" SemiHidden="false"
   UnhideWhenUsed="false" Name="Dark List Accent 3"/>
  <w:LsdException Locked="false" Priority="71" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful Shading Accent 3"/>
  <w:LsdException Locked="false" Priority="72" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful List Accent 3"/>
  <w:LsdException Locked="false" Priority="73" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful Grid Accent 3"/>
  <w:LsdException Locked="false" Priority="60" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light Shading Accent 4"/>
  <w:LsdException Locked="false" Priority="61" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light List Accent 4"/>
  <w:LsdException Locked="false" Priority="62" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light Grid Accent 4"/>
  <w:LsdException Locked="false" Priority="63" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Shading 1 Accent 4"/>
  <w:LsdException Locked="false" Priority="64" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Shading 2 Accent 4"/>
  <w:LsdException Locked="false" Priority="65" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium List 1 Accent 4"/>
  <w:LsdException Locked="false" Priority="66" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium List 2 Accent 4"/>
  <w:LsdException Locked="false" Priority="67" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 1 Accent 4"/>
  <w:LsdException Locked="false" Priority="68" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 2 Accent 4"/>
  <w:LsdException Locked="false" Priority="69" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 3 Accent 4"/>
  <w:LsdException Locked="false" Priority="70" SemiHidden="false"
   UnhideWhenUsed="false" Name="Dark List Accent 4"/>
  <w:LsdException Locked="false" Priority="71" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful Shading Accent 4"/>
  <w:LsdException Locked="false" Priority="72" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful List Accent 4"/>
  <w:LsdException Locked="false" Priority="73" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful Grid Accent 4"/>
  <w:LsdException Locked="false" Priority="60" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light Shading Accent 5"/>
  <w:LsdException Locked="false" Priority="61" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light List Accent 5"/>
  <w:LsdException Locked="false" Priority="62" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light Grid Accent 5"/>
  <w:LsdException Locked="false" Priority="63" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Shading 1 Accent 5"/>
  <w:LsdException Locked="false" Priority="64" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Shading 2 Accent 5"/>
  <w:LsdException Locked="false" Priority="65" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium List 1 Accent 5"/>
  <w:LsdException Locked="false" Priority="66" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium List 2 Accent 5"/>
  <w:LsdException Locked="false" Priority="67" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 1 Accent 5"/>
  <w:LsdException Locked="false" Priority="68" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 2 Accent 5"/>
  <w:LsdException Locked="false" Priority="69" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 3 Accent 5"/>
  <w:LsdException Locked="false" Priority="70" SemiHidden="false"
   UnhideWhenUsed="false" Name="Dark List Accent 5"/>
  <w:LsdException Locked="false" Priority="71" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful Shading Accent 5"/>
  <w:LsdException Locked="false" Priority="72" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful List Accent 5"/>
  <w:LsdException Locked="false" Priority="73" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful Grid Accent 5"/>
  <w:LsdException Locked="false" Priority="60" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light Shading Accent 6"/>
  <w:LsdException Locked="false" Priority="61" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light List Accent 6"/>
  <w:LsdException Locked="false" Priority="62" SemiHidden="false"
   UnhideWhenUsed="false" Name="Light Grid Accent 6"/>
  <w:LsdException Locked="false" Priority="63" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Shading 1 Accent 6"/>
  <w:LsdException Locked="false" Priority="64" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Shading 2 Accent 6"/>
  <w:LsdException Locked="false" Priority="65" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium List 1 Accent 6"/>
  <w:LsdException Locked="false" Priority="66" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium List 2 Accent 6"/>
  <w:LsdException Locked="false" Priority="67" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 1 Accent 6"/>
  <w:LsdException Locked="false" Priority="68" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 2 Accent 6"/>
  <w:LsdException Locked="false" Priority="69" SemiHidden="false"
   UnhideWhenUsed="false" Name="Medium Grid 3 Accent 6"/>
  <w:LsdException Locked="false" Priority="70" SemiHidden="false"
   UnhideWhenUsed="false" Name="Dark List Accent 6"/>
  <w:LsdException Locked="false" Priority="71" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful Shading Accent 6"/>
  <w:LsdException Locked="false" Priority="72" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful List Accent 6"/>
  <w:LsdException Locked="false" Priority="73" SemiHidden="false"
   UnhideWhenUsed="false" Name="Colorful Grid Accent 6"/>
  <w:LsdException Locked="false" Priority="19" SemiHidden="false"
   UnhideWhenUsed="false" QFormat="true" Name="Subtle Emphasis"/>
  <w:LsdException Locked="false" Priority="21" SemiHidden="false"
   UnhideWhenUsed="false" QFormat="true" Name="Intense Emphasis"/>
  <w:LsdException Locked="false" Priority="31" SemiHidden="false"
   UnhideWhenUsed="false" QFormat="true" Name="Subtle Reference"/>
  <w:LsdException Locked="false" Priority="32" SemiHidden="false"
   UnhideWhenUsed="false" QFormat="true" Name="Intense Reference"/>
  <w:LsdException Locked="false" Priority="33" SemiHidden="false"
   UnhideWhenUsed="false" QFormat="true" Name="Book Title"/>
  <w:LsdException Locked="false" Priority="37" Name="Bibliography"/>
  <w:LsdException Locked="false" Priority="39" QFormat="true" Name="TOC Heading"/>
 </w:LatentStyles>
</xml><![endif]-->
<style>
<!--
 /* Font Definitions */
 @font-face
	{font-family:Helvetica;
	panose-1:2 11 6 4 2 2 2 2 2 4;
	mso-font-charset:0;
	mso-generic-font-family:swiss;
	mso-font-pitch:variable;
	mso-font-signature:-536859905 -1073711037 9 0 511 0;}
@font-face
	{font-family:"MS Mincho";
	panose-1:2 2 6 9 4 2 5 8 3 4;
	mso-font-alt:"\FF2D\FF33 \660E\671D";
	mso-font-charset:128;
	mso-generic-font-family:modern;
	mso-font-pitch:fixed;
	mso-font-signature:-536870145 1791491579 18 0 131231 0;}
@font-face
	{font-family:"Cambria Math";
	panose-1:2 4 5 3 5 4 6 3 2 4;
	mso-font-charset:0;
	mso-generic-font-family:roman;
	mso-font-pitch:variable;
	mso-font-signature:-1610611985 1107304683 0 0 415 0;}
@font-face
	{font-family:Cambria;
	panose-1:2 4 5 3 5 4 6 3 2 4;
	mso-font-charset:0;
	mso-generic-font-family:roman;
	mso-font-pitch:variable;
	mso-font-signature:-1610611985 1073741899 0 0 415 0;}
@font-face
	{font-family:Calibri;
	panose-1:2 15 5 2 2 2 4 3 2 4;
	mso-font-charset:0;
	mso-generic-font-family:swiss;
	mso-font-pitch:variable;
	mso-font-signature:-520092929 1073786111 9 0 415 0;}
@font-face
	{font-family:Tahoma;
	panose-1:2 11 6 4 3 5 4 4 2 4;
	mso-font-charset:0;
	mso-generic-font-family:swiss;
	mso-font-pitch:variable;
	mso-font-signature:-520081665 -1073717157 41 0 66047 0;}
@font-face
	{font-family:"\30D2\30E9\30AE\30CE\89D2\30B4 Pro W3";
	mso-font-charset:78;
	mso-generic-font-family:auto;
	mso-font-pitch:variable;
	mso-font-signature:-536870145 2059927551 18 0 131085 0;}
@font-face
	{font-family:"\@MS Mincho";
	panose-1:2 2 6 9 4 2 5 8 3 4;
	mso-font-charset:128;
	mso-generic-font-family:modern;
	mso-font-pitch:fixed;
	mso-font-signature:-536870145 1791491579 18 0 131231 0;}
 /* Style Definitions */
 p.MsoNormal, li.MsoNormal, div.MsoNormal
	{mso-style-unhide:no;
	mso-style-qformat:yes;
	mso-style-parent:"";
	margin-top:0in;
	margin-right:0in;
	margin-bottom:10.0pt;
	margin-left:0in;
	line-height:115%;
	mso-pagination:widow-orphan;
	font-size:11.0pt;
	font-family:"Calibri","sans-serif";
	mso-ascii-font-family:Calibri;
	mso-ascii-theme-font:minor-latin;
	mso-fareast-font-family:Calibri;
	mso-fareast-theme-font:minor-latin;
	mso-hansi-font-family:Calibri;
	mso-hansi-theme-font:minor-latin;
	mso-bidi-font-family:"Times New Roman";
	mso-bidi-theme-font:minor-bidi;}
h1
	{mso-style-priority:9;
	mso-style-unhide:no;
	mso-style-qformat:yes;
	mso-style-link:"Heading 1 Char";
	mso-style-next:Normal;
	margin-top:24.0pt;
	margin-right:0in;
	margin-bottom:0in;
	margin-left:0in;
	margin-bottom:.0001pt;
	line-height:115%;
	mso-pagination:widow-orphan lines-together;
	page-break-after:avoid;
	mso-outline-level:1;
	font-size:14.0pt;
	font-family:"Cambria","serif";
	mso-ascii-font-family:Cambria;
	mso-ascii-theme-font:major-latin;
	mso-fareast-font-family:"Times New Roman";
	mso-fareast-theme-font:major-fareast;
	mso-hansi-font-family:Cambria;
	mso-hansi-theme-font:major-latin;
	mso-bidi-font-family:"Times New Roman";
	mso-bidi-theme-font:major-bidi;
	color:#365F91;
	mso-themecolor:accent1;
	mso-themeshade:191;
	mso-font-kerning:0pt;}
h2
	{mso-style-noshow:yes;
	mso-style-priority:9;
	mso-style-qformat:yes;
	mso-style-link:"Heading 2 Char";
	mso-style-next:Normal;
	margin-top:10.0pt;
	margin-right:0in;
	margin-bottom:0in;
	margin-left:0in;
	margin-bottom:.0001pt;
	line-height:115%;
	mso-pagination:widow-orphan lines-together;
	page-break-after:avoid;
	mso-outline-level:2;
	font-size:13.0pt;
	font-family:"Cambria","serif";
	mso-ascii-font-family:Cambria;
	mso-ascii-theme-font:major-latin;
	mso-fareast-font-family:"Times New Roman";
	mso-fareast-theme-font:major-fareast;
	mso-hansi-font-family:Cambria;
	mso-hansi-theme-font:major-latin;
	mso-bidi-font-family:"Times New Roman";
	mso-bidi-theme-font:major-bidi;
	color:#4F81BD;
	mso-themecolor:accent1;}
h3
	{mso-style-noshow:yes;
	mso-style-priority:9;
	mso-style-qformat:yes;
	mso-style-link:"Heading 3 Char";
	mso-style-next:Normal;
	margin-top:10.0pt;
	margin-right:0in;
	margin-bottom:0in;
	margin-left:0in;
	margin-bottom:.0001pt;
	line-height:115%;
	mso-pagination:widow-orphan lines-together;
	page-break-after:avoid;
	mso-outline-level:3;
	font-size:11.0pt;
	font-family:"Cambria","serif";
	mso-ascii-font-family:Cambria;
	mso-ascii-theme-font:major-latin;
	mso-fareast-font-family:"Times New Roman";
	mso-fareast-theme-font:major-fareast;
	mso-hansi-font-family:Cambria;
	mso-hansi-theme-font:major-latin;
	mso-bidi-font-family:"Times New Roman";
	mso-bidi-theme-font:major-bidi;
	color:#4F81BD;
	mso-themecolor:accent1;}
h4
	{mso-style-unhide:no;
	mso-style-qformat:yes;
	mso-style-link:"Heading 4 Char";
	mso-style-next:Normal;
	margin-top:12.0pt;
	margin-right:0in;
	margin-bottom:3.0pt;
	margin-left:-132.0pt;
	text-align:justify;
	text-indent:0in;
	mso-pagination:widow-orphan;
	page-break-after:avoid;
	mso-outline-level:4;
	mso-list:l0 level4 lfo1;
	tab-stops:list -132.0pt;
	font-size:9.0pt;
	mso-bidi-font-size:10.0pt;
	font-family:"Times New Roman","serif";
	mso-fareast-font-family:"MS Mincho";
	mso-bidi-font-weight:normal;
	font-style:italic;
	mso-bidi-font-style:normal;}
h5
	{mso-style-unhide:no;
	mso-style-qformat:yes;
	mso-style-link:"Heading 5 Char";
	mso-style-next:Normal;
	margin-top:12.0pt;
	margin-right:0in;
	margin-bottom:3.0pt;
	margin-left:-132.0pt;
	text-align:justify;
	text-indent:0in;
	mso-pagination:widow-orphan;
	mso-outline-level:5;
	mso-list:l0 level5 lfo1;
	tab-stops:list -132.0pt;
	font-size:9.0pt;
	mso-bidi-font-size:10.0pt;
	font-family:"Times New Roman","serif";
	mso-fareast-font-family:"MS Mincho";
	font-weight:normal;}
h6
	{mso-style-unhide:no;
	mso-style-qformat:yes;
	mso-style-link:"Heading 6 Char";
	mso-style-next:Normal;
	margin-top:12.0pt;
	margin-right:0in;
	margin-bottom:3.0pt;
	margin-left:-132.0pt;
	text-align:justify;
	text-indent:0in;
	mso-pagination:widow-orphan;
	mso-outline-level:6;
	mso-list:l0 level6 lfo1;
	tab-stops:list -132.0pt;
	font-size:11.0pt;
	mso-bidi-font-size:10.0pt;
	font-family:"Arial","sans-serif";
	mso-fareast-font-family:"MS Mincho";
	mso-bidi-font-family:"Times New Roman";
	font-weight:normal;
	font-style:italic;
	mso-bidi-font-style:normal;}
p.MsoHeading7, li.MsoHeading7, div.MsoHeading7
	{mso-style-unhide:no;
	mso-style-qformat:yes;
	mso-style-link:"Heading 7 Char";
	mso-style-next:Normal;
	margin-top:12.0pt;
	margin-right:0in;
	margin-bottom:3.0pt;
	margin-left:-132.0pt;
	text-align:justify;
	text-indent:0in;
	mso-pagination:widow-orphan;
	mso-outline-level:7;
	mso-list:l0 level7 lfo1;
	tab-stops:list -132.0pt;
	font-size:10.0pt;
	font-family:"Arial","sans-serif";
	mso-fareast-font-family:"MS Mincho";
	mso-bidi-font-family:"Times New Roman";}
p.MsoHeading8, li.MsoHeading8, div.MsoHeading8
	{mso-style-unhide:no;
	mso-style-qformat:yes;
	mso-style-link:"Heading 8 Char";
	mso-style-next:Normal;
	margin-top:12.0pt;
	margin-right:0in;
	margin-bottom:3.0pt;
	margin-left:-132.0pt;
	text-align:justify;
	text-indent:0in;
	mso-pagination:widow-orphan;
	mso-outline-level:8;
	mso-list:l0 level8 lfo1;
	tab-stops:list -132.0pt;
	font-size:10.0pt;
	font-family:"Arial","sans-serif";
	mso-fareast-font-family:"MS Mincho";
	mso-bidi-font-family:"Times New Roman";
	font-style:italic;
	mso-bidi-font-style:normal;}
p.MsoHeading9, li.MsoHeading9, div.MsoHeading9
	{mso-style-unhide:no;
	mso-style-qformat:yes;
	mso-style-link:"Heading 9 Char";
	mso-style-next:Normal;
	margin-top:12.0pt;
	margin-right:0in;
	margin-bottom:3.0pt;
	margin-left:-132.0pt;
	text-align:justify;
	text-indent:0in;
	mso-pagination:widow-orphan;
	mso-outline-level:9;
	mso-list:l0 level9 lfo1;
	tab-stops:list -132.0pt;
	font-size:10.0pt;
	font-family:"Arial","sans-serif";
	mso-fareast-font-family:"MS Mincho";
	mso-bidi-font-family:"Times New Roman";
	font-style:italic;
	mso-bidi-font-style:normal;}
p.MsoFootnoteText, li.MsoFootnoteText, div.MsoFootnoteText
	{mso-style-noshow:yes;
	mso-style-unhide:no;
	mso-style-link:"Footnote Text Char";
	margin:0in;
	margin-bottom:.0001pt;
	text-align:justify;
	mso-pagination:widow-orphan;
	font-size:10.0pt;
	font-family:"Times New Roman","serif";
	mso-fareast-font-family:"MS Mincho";}
p.MsoCaption, li.MsoCaption, div.MsoCaption
	{mso-style-unhide:no;
	mso-style-qformat:yes;
	mso-style-next:Normal;
	margin-top:6.0pt;
	margin-right:.2in;
	margin-bottom:6.0pt;
	margin-left:.2in;
	text-align:justify;
	mso-pagination:widow-orphan;
	font-size:9.0pt;
	mso-bidi-font-size:10.0pt;
	font-family:"Times New Roman","serif";
	mso-fareast-font-family:"MS Mincho";}
span.MsoFootnoteReference
	{mso-style-noshow:yes;
	mso-style-unhide:no;
	vertical-align:super;}
p.MsoBodyText, li.MsoBodyText, div.MsoBodyText
	{mso-style-unhide:no;
	mso-style-link:"Body Text Char";
	margin:0in;
	margin-bottom:.0001pt;
	text-align:justify;
	mso-pagination:widow-orphan;
	font-size:10.0pt;
	font-family:"Times New Roman","serif";
	mso-fareast-font-family:"MS Mincho";}
p.MsoAcetate, li.MsoAcetate, div.MsoAcetate
	{mso-style-noshow:yes;
	mso-style-priority:99;
	mso-style-link:"Balloon Text Char";
	margin:0in;
	margin-bottom:.0001pt;
	mso-pagination:widow-orphan;
	font-size:8.0pt;
	font-family:"Tahoma","sans-serif";
	mso-fareast-font-family:Calibri;
	mso-fareast-theme-font:minor-latin;}
span.Heading4Char
	{mso-style-name:"Heading 4 Char";
	mso-style-unhide:no;
	mso-style-locked:yes;
	mso-style-link:"Heading 4";
	mso-ansi-font-size:9.0pt;
	mso-bidi-font-size:10.0pt;
	font-family:"Times New Roman","serif";
	mso-ascii-font-family:"Times New Roman";
	mso-fareast-font-family:"MS Mincho";
	mso-hansi-font-family:"Times New Roman";
	mso-bidi-font-family:"Times New Roman";
	font-weight:bold;
	mso-bidi-font-weight:normal;
	font-style:italic;
	mso-bidi-font-style:normal;}
span.Heading5Char
	{mso-style-name:"Heading 5 Char";
	mso-style-unhide:no;
	mso-style-locked:yes;
	mso-style-link:"Heading 5";
	mso-ansi-font-size:9.0pt;
	mso-bidi-font-size:10.0pt;
	font-family:"Times New Roman","serif";
	mso-ascii-font-family:"Times New Roman";
	mso-fareast-font-family:"MS Mincho";
	mso-hansi-font-family:"Times New Roman";
	mso-bidi-font-family:"Times New Roman";}
span.Heading6Char
	{mso-style-name:"Heading 6 Char";
	mso-style-unhide:no;
	mso-style-locked:yes;
	mso-style-link:"Heading 6";
	mso-bidi-font-size:10.0pt;
	font-family:"Arial","sans-serif";
	mso-ascii-font-family:Arial;
	mso-fareast-font-family:"MS Mincho";
	mso-hansi-font-family:Arial;
	mso-bidi-font-family:"Times New Roman";
	font-style:italic;
	mso-bidi-font-style:normal;}
span.Heading7Char
	{mso-style-name:"Heading 7 Char";
	mso-style-unhide:no;
	mso-style-locked:yes;
	mso-style-link:"Heading 7";
	mso-ansi-font-size:10.0pt;
	mso-bidi-font-size:10.0pt;
	font-family:"Arial","sans-serif";
	mso-ascii-font-family:Arial;
	mso-fareast-font-family:"MS Mincho";
	mso-hansi-font-family:Arial;
	mso-bidi-font-family:"Times New Roman";}
span.Heading8Char
	{mso-style-name:"Heading 8 Char";
	mso-style-unhide:no;
	mso-style-locked:yes;
	mso-style-link:"Heading 8";
	mso-ansi-font-size:10.0pt;
	mso-bidi-font-size:10.0pt;
	font-family:"Arial","sans-serif";
	mso-ascii-font-family:Arial;
	mso-fareast-font-family:"MS Mincho";
	mso-hansi-font-family:Arial;
	mso-bidi-font-family:"Times New Roman";
	font-style:italic;
	mso-bidi-font-style:normal;}
span.Heading9Char
	{mso-style-name:"Heading 9 Char";
	mso-style-unhide:no;
	mso-style-locked:yes;
	mso-style-link:"Heading 9";
	mso-ansi-font-size:10.0pt;
	mso-bidi-font-size:10.0pt;
	font-family:"Arial","sans-serif";
	mso-ascii-font-family:Arial;
	mso-fareast-font-family:"MS Mincho";
	mso-hansi-font-family:Arial;
	mso-bidi-font-family:"Times New Roman";
	font-style:italic;
	mso-bidi-font-style:normal;}
p.Third-LevelHeadinds, li.Third-LevelHeadinds, div.Third-LevelHeadinds
	{mso-style-name:"Third-Level Headinds";
	mso-style-unhide:no;
	mso-style-qformat:yes;
	mso-style-parent:"Heading 3";
	margin-top:12.0pt;
	margin-right:0in;
	margin-bottom:0in;
	margin-left:-132.0pt;
	margin-bottom:.0001pt;
	text-align:justify;
	text-indent:0in;
	line-height:107%;
	mso-pagination:widow-orphan;
	page-break-after:avoid;
	mso-outline-level:3;
	mso-list:l0 level3 lfo1;
	font-size:10.0pt;
	font-family:"Times New Roman","serif";
	mso-fareast-font-family:"MS Mincho";
	mso-bidi-font-family:"MS Mincho";
	font-style:italic;}
p.Second-LevelHeadings, li.Second-LevelHeadings, div.Second-LevelHeadings
	{mso-style-name:"Second-Level Headings";
	mso-style-unhide:no;
	mso-style-qformat:yes;
	mso-style-parent:"Heading 2";
	margin-top:6.0pt;
	margin-right:0in;
	margin-bottom:0in;
	margin-left:0in;
	margin-bottom:.0001pt;
	text-align:justify;
	text-indent:0in;
	line-height:107%;
	mso-pagination:widow-orphan;
	page-break-after:avoid;
	mso-outline-level:2;
	mso-list:l0 level2 lfo1;
	tab-stops:list .25in;
	font-size:10.0pt;
	font-family:"Times New Roman","serif";
	mso-fareast-font-family:"MS Mincho";
	mso-bidi-font-family:"MS Mincho";
	font-weight:bold;}
p.First-LevelHeadings, li.First-LevelHeadings, div.First-LevelHeadings
	{mso-style-name:"First-Level Headings";
	mso-style-unhide:no;
	mso-style-qformat:yes;
	mso-style-parent:"Heading 1";
	margin-top:12.0pt;
	margin-right:0in;
	margin-bottom:6.0pt;
	margin-left:33.0pt;
	text-align:center;
	text-indent:-21.0pt;
	line-height:107%;
	mso-pagination:widow-orphan;
	page-break-after:avoid;
	mso-outline-level:1;
	mso-list:l0 level1 lfo1;
	tab-stops:12.0pt;
	font-size:10.0pt;
	font-family:"Times New Roman","serif";
	mso-fareast-font-family:"MS Mincho";
	mso-bidi-font-family:"MS Mincho";
	text-transform:uppercase;
	font-weight:bold;}
span.Heading3Char
	{mso-style-name:"Heading 3 Char";
	mso-style-noshow:yes;
	mso-style-priority:9;
	mso-style-unhide:no;
	mso-style-locked:yes;
	mso-style-link:"Heading 3";
	font-family:"Cambria","serif";
	mso-ascii-font-family:Cambria;
	mso-ascii-theme-font:major-latin;
	mso-fareast-font-family:"Times New Roman";
	mso-fareast-theme-font:major-fareast;
	mso-hansi-font-family:Cambria;
	mso-hansi-theme-font:major-latin;
	mso-bidi-font-family:"Times New Roman";
	mso-bidi-theme-font:major-bidi;
	color:#4F81BD;
	mso-themecolor:accent1;
	font-weight:bold;}
span.Heading2Char
	{mso-style-name:"Heading 2 Char";
	mso-style-noshow:yes;
	mso-style-priority:9;
	mso-style-unhide:no;
	mso-style-locked:yes;
	mso-style-link:"Heading 2";
	mso-ansi-font-size:13.0pt;
	mso-bidi-font-size:13.0pt;
	font-family:"Cambria","serif";
	mso-ascii-font-family:Cambria;
	mso-ascii-theme-font:major-latin;
	mso-fareast-font-family:"Times New Roman";
	mso-fareast-theme-font:major-fareast;
	mso-hansi-font-family:Cambria;
	mso-hansi-theme-font:major-latin;
	mso-bidi-font-family:"Times New Roman";
	mso-bidi-theme-font:major-bidi;
	color:#4F81BD;
	mso-themecolor:accent1;
	font-weight:bold;}
span.Heading1Char
	{mso-style-name:"Heading 1 Char";
	mso-style-priority:9;
	mso-style-unhide:no;
	mso-style-locked:yes;
	mso-style-link:"Heading 1";
	mso-ansi-font-size:14.0pt;
	mso-bidi-font-size:14.0pt;
	font-family:"Cambria","serif";
	mso-ascii-font-family:Cambria;
	mso-ascii-theme-font:major-latin;
	mso-fareast-font-family:"Times New Roman";
	mso-fareast-theme-font:major-fareast;
	mso-hansi-font-family:Cambria;
	mso-hansi-theme-font:major-latin;
	mso-bidi-font-family:"Times New Roman";
	mso-bidi-theme-font:major-bidi;
	color:#365F91;
	mso-themecolor:accent1;
	mso-themeshade:191;
	font-weight:bold;}
p.Title1, li.Title1, div.Title1
	{mso-style-name:Title1;
	mso-style-unhide:no;
	mso-style-next:Normal;
	margin-top:5.0pt;
	margin-right:.5in;
	margin-bottom:0in;
	margin-left:56.7pt;
	margin-bottom:.0001pt;
	text-align:center;
	mso-pagination:widow-orphan;
	font-size:12.0pt;
	mso-bidi-font-size:10.0pt;
	font-family:"Times New Roman","serif";
	mso-fareast-font-family:"MS Mincho";
	font-weight:bold;
	mso-bidi-font-weight:normal;}
span.BodyTextChar
	{mso-style-name:"Body Text Char";
	mso-style-unhide:no;
	mso-style-locked:yes;
	mso-style-link:"Body Text";
	mso-ansi-font-size:10.0pt;
	mso-bidi-font-size:10.0pt;
	font-family:"Times New Roman","serif";
	mso-ascii-font-family:"Times New Roman";
	mso-fareast-font-family:"MS Mincho";
	mso-hansi-font-family:"Times New Roman";
	mso-bidi-font-family:"Times New Roman";}
span.FootnoteTextChar
	{mso-style-name:"Footnote Text Char";
	mso-style-noshow:yes;
	mso-style-unhide:no;
	mso-style-locked:yes;
	mso-style-link:"Footnote Text";
	mso-ansi-font-size:10.0pt;
	mso-bidi-font-size:10.0pt;
	font-family:"Times New Roman","serif";
	mso-ascii-font-family:"Times New Roman";
	mso-fareast-font-family:"MS Mincho";
	mso-hansi-font-family:"Times New Roman";
	mso-bidi-font-family:"Times New Roman";}
p.Body, li.Body, div.Body
	{mso-style-name:Body;
	mso-style-unhide:no;
	mso-style-parent:"";
	margin:0in;
	margin-bottom:.0001pt;
	mso-pagination:widow-orphan;
	font-size:12.0pt;
	mso-bidi-font-size:10.0pt;
	font-family:"Helvetica","sans-serif";
	mso-fareast-font-family:"\30D2\30E9\30AE\30CE\89D2\30B4 Pro W3";
	mso-bidi-font-family:"Times New Roman";
	color:black;}
p.indent, li.indent, div.indent
	{mso-style-name:indent;
	mso-style-unhide:no;
	mso-style-qformat:yes;
	mso-style-parent:"Body Text";
	mso-style-link:"indent Char";
	margin:0in;
	margin-bottom:.0001pt;
	text-align:justify;
	text-indent:11.35pt;
	line-height:107%;
	mso-pagination:widow-orphan;
	font-size:10.0pt;
	font-family:"Times New Roman","serif";
	mso-fareast-font-family:"MS Mincho";}
span.indentChar
	{mso-style-name:"indent Char";
	mso-style-unhide:no;
	mso-style-locked:yes;
	mso-style-parent:"Body Text Char";
	mso-style-link:indent;
	mso-ansi-font-size:10.0pt;
	mso-bidi-font-size:10.0pt;
	font-family:"Times New Roman","serif";
	mso-ascii-font-family:"Times New Roman";
	mso-fareast-font-family:"MS Mincho";
	mso-hansi-font-family:"Times New Roman";
	mso-bidi-font-family:"Times New Roman";}
span.BalloonTextChar
	{mso-style-name:"Balloon Text Char";
	mso-style-noshow:yes;
	mso-style-priority:99;
	mso-style-unhide:no;
	mso-style-locked:yes;
	mso-style-link:"Balloon Text";
	mso-ansi-font-size:8.0pt;
	mso-bidi-font-size:8.0pt;
	font-family:"Tahoma","sans-serif";
	mso-ascii-font-family:Tahoma;
	mso-hansi-font-family:Tahoma;
	mso-bidi-font-family:Tahoma;}
.MsoChpDefault
	{mso-style-type:export-only;
	mso-default-props:yes;
	mso-ascii-font-family:Calibri;
	mso-ascii-theme-font:minor-latin;
	mso-fareast-font-family:Calibri;
	mso-fareast-theme-font:minor-latin;
	mso-hansi-font-family:Calibri;
	mso-hansi-theme-font:minor-latin;
	mso-bidi-font-family:"Times New Roman";
	mso-bidi-theme-font:minor-bidi;}
.MsoPapDefault
	{mso-style-type:export-only;
	margin-bottom:10.0pt;
	line-height:115%;}
 /* Page Definitions */
 @page
	{mso-footnote-separator:url("ismir_tables_files/header.htm") fs;
	mso-footnote-continuation-separator:url("ismir_tables_files/header.htm") fcs;
	mso-endnote-separator:url("ismir_tables_files/header.htm") es;
	mso-endnote-continuation-separator:url("ismir_tables_files/header.htm") ecs;}
@page WordSection1
	{size:8.5in 11.0in;
	margin:1.0in 1.0in 1.0in 1.0in;
	mso-header-margin:.5in;
	mso-footer-margin:.5in;
	mso-paper-source:0;}
div.WordSection1
	{page:WordSection1;}
 /* List Definitions */
 @list l0
	{mso-list-id:-5;
	mso-list-template-ids:563231098;}
@list l0:level1
	{mso-level-style-link:"First-Level Headings";
	mso-level-tab-stop:none;
	mso-level-number-position:left;
	margin-left:33.0pt;
	text-indent:-21.0pt;}
@list l0:level2
	{mso-level-style-link:"Second-Level Headings";
	mso-level-text:"%1\.%2";
	mso-level-tab-stop:256.5pt;
	mso-level-number-position:left;
	margin-left:238.5pt;
	text-indent:0in;}
@list l0:level3
	{mso-level-style-link:"Third-Level Headinds";
	mso-level-suffix:space;
	mso-level-text:"%1\.%2\.%3";
	mso-level-tab-stop:none;
	mso-level-number-position:left;
	margin-left:-132.0pt;
	text-indent:0in;}
@list l0:level4
	{mso-level-style-link:"Heading 4";
	mso-level-text:"%1\.%2\.%3\.%4";
	mso-level-tab-stop:-132.0pt;
	mso-level-number-position:left;
	margin-left:-132.0pt;
	text-indent:0in;}
@list l0:level5
	{mso-level-style-link:"Heading 5";
	mso-level-text:"%1\.%2\.%3\.%4\.%5";
	mso-level-tab-stop:-132.0pt;
	mso-level-number-position:left;
	margin-left:-132.0pt;
	text-indent:0in;}
@list l0:level6
	{mso-level-style-link:"Heading 6";
	mso-level-text:"%1\.%2\.%3\.%4\.%5\.%6";
	mso-level-tab-stop:-132.0pt;
	mso-level-number-position:left;
	margin-left:-132.0pt;
	text-indent:0in;}
@list l0:level7
	{mso-level-style-link:"Heading 7";
	mso-level-text:"%1\.%2\.%3\.%4\.%5\.%6\.%7";
	mso-level-tab-stop:-132.0pt;
	mso-level-number-position:left;
	margin-left:-132.0pt;
	text-indent:0in;}
@list l0:level8
	{mso-level-style-link:"Heading 8";
	mso-level-text:"%1\.%2\.%3\.%4\.%5\.%6\.%7\.%8";
	mso-level-tab-stop:-132.0pt;
	mso-level-number-position:left;
	margin-left:-132.0pt;
	text-indent:0in;}
@list l0:level9
	{mso-level-style-link:"Heading 9";
	mso-level-text:"%1\.%2\.%3\.%4\.%5\.%6\.%7\.%8\.%9";
	mso-level-tab-stop:-132.0pt;
	mso-level-number-position:left;
	margin-left:-132.0pt;
	text-indent:0in;}
@list l0:level1 lfo2
	{mso-level-start-at:3;}
ol
	{margin-bottom:0in;}
ul
	{margin-bottom:0in;}
-->
</style>
<!--[if gte mso 10]>
<style>
 /* Style Definitions */
 table.MsoNormalTable
	{mso-style-name:"Table Normal";
	mso-tstyle-rowband-size:0;
	mso-tstyle-colband-size:0;
	mso-style-noshow:yes;
	mso-style-priority:99;
	mso-style-qformat:yes;
	mso-style-parent:"";
	mso-padding-alt:0in 5.4pt 0in 5.4pt;
	mso-para-margin-top:0in;
	mso-para-margin-right:0in;
	mso-para-margin-bottom:10.0pt;
	mso-para-margin-left:0in;
	line-height:115%;
	mso-pagination:widow-orphan;
	font-size:11.0pt;
	font-family:"Calibri","sans-serif";
	mso-ascii-font-family:Calibri;
	mso-ascii-theme-font:minor-latin;
	mso-hansi-font-family:Calibri;
	mso-hansi-theme-font:minor-latin;
	mso-bidi-font-family:"Times New Roman";
	mso-bidi-theme-font:minor-bidi;}
table.MsoTableGrid
	{mso-style-name:"Table Grid";
	mso-tstyle-rowband-size:0;
	mso-tstyle-colband-size:0;
	mso-style-unhide:no;
	border:solid windowtext 1.0pt;
	mso-border-alt:solid windowtext .5pt;
	mso-padding-alt:0in 5.4pt 0in 5.4pt;
	mso-border-insideh:.5pt solid windowtext;
	mso-border-insidev:.5pt solid windowtext;
	mso-para-margin:0in;
	mso-para-margin-bottom:.0001pt;
	text-align:justify;
	mso-pagination:widow-orphan;
	font-size:10.0pt;
	font-family:"Times New Roman","serif";
	mso-fareast-font-family:"MS Mincho";}
</style>
<![endif]--><!--[if gte mso 9]><xml>
 <o:shapedefaults v:ext="edit" spidmax="2050"/>
</xml><![endif]--><!--[if gte mso 9]><xml>
 <o:shapelayout v:ext="edit">
  <o:idmap v:ext="edit" data="1"/>
 </o:shapelayout></xml><![endif]-->     
                </head>

                    <body>

                        <table id="h2table">
                            <tr>
                                <td><img src="support/logo.png" width="160"></td>
                                <td><h2>SALAMI: Structural Segmentation Experiments</h2></td>
                            </tr>
                        </table>

                        <div id="tabs">
                            <ul>
                                <li><a href="index.jsp" title="summary"><span>Summary</span></a></li>
                                <li class="shown"><a href="comparison.jsp" title="comparison"><span>Comparison</span></a></li>
                                <li><a href="classical.jsp" title="classical"><span>Classical</span></a></li>
                                <li><a href="jazz.jsp" title="Jazz"><span>Jazz</span></a></li>
                                <li><a href="live.jsp" title="Live"><span>Live</span></a></li>
                                <li><a href="pop.jsp" title="Pop"><span>Popular</span></a></li>
                                <li><a href="world.jsp" title="World"><span>World</span></a></li>
                            </ul>
                        </div>

                        <br><a name="top"></a>
                            <div id="content">
                                <h3>Algorithm Evaluation</h3>
                                


<p class=MsoNormal><span style='font-size:12.0pt;line-height:115%'><o:p>&nbsp;</o:p></span></p>

<p class=First-LevelHeadings align=left style='text-align:left'><![if !supportLists]><span
style='font-size:12.0pt;line-height:107%;mso-fareast-font-family:"Times New Roman";
mso-bidi-font-family:"Times New Roman"'><span style='mso-list:Ignore'>1.<span
style='font:7.0pt "Times New Roman"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</span></span></span><![endif]><span style='font-size:12.0pt;line-height:107%'>DATASETS<o:p></o:p></span></p>

<table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 width=318
 style='width:238.5pt;margin-left:5.0pt;background:white;border-collapse:collapse;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <thead>
  <tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes;page-break-inside:avoid;
   height:11.05pt'>
   <td width=78 valign=top style='width:58.5pt;border:solid black 1.0pt;
   background:#B0B3B2;padding:5.0pt 5.0pt 5.0pt 5.0pt;height:11.05pt'>
   <p class=Body align=center style='text-align:center'><span style='font-size:
   10.0pt;font-family:"Times New Roman","serif"'>Style<o:p></o:p></span></p>
   </td>
   <td width=60 valign=top style='width:45.0pt;border:solid black 1.0pt;
   border-left:none;mso-border-left-alt:solid black 1.0pt;background:#B0B3B2;
   padding:5.0pt 5.0pt 5.0pt 5.0pt;height:11.05pt'>
   <p class=Body align=center style='text-align:center'><span style='font-size:
   10.0pt;font-family:"Times New Roman","serif"'>Double-keyed<o:p></o:p></span></p>
   </td>
   <td width=54 valign=top style='width:40.5pt;border:solid black 1.0pt;
   border-left:none;mso-border-left-alt:solid black 1.0pt;background:#B0B3B2;
   padding:5.0pt 5.0pt 5.0pt 5.0pt;height:11.05pt'>
   <p class=Body align=center style='text-align:center'><span style='font-size:
   10.0pt;font-family:"Times New Roman","serif"'>Single-keyed<o:p></o:p></span></p>
   </td>
   <td width=54 valign=top style='width:40.5pt;border:solid black 1.0pt;
   border-left:none;mso-border-left-alt:solid black 1.0pt;background:#B0B3B2;
   padding:5.0pt 5.0pt 5.0pt 5.0pt;height:11.05pt'>
   <p class=Body align=center style='text-align:center'><span style='font-size:
   10.0pt;font-family:"Times New Roman","serif"'>Total<o:p></o:p></span></p>
   </td>
   <td width=72 valign=top style='width:.75in;border:solid black 1.0pt;
   border-left:none;mso-border-left-alt:solid black 1.0pt;background:#B0B3B2;
   padding:5.0pt 5.0pt 5.0pt 5.0pt;height:11.05pt'>
   <p class=Body align=center style='text-align:center'><span style='font-size:
   10.0pt;font-family:"Times New Roman","serif"'>Percentage<o:p></o:p></span></p>
   </td>
  </tr>
 </thead>
 <tr style='mso-yfti-irow:1;page-break-inside:avoid;height:11.05pt'>
  <td width=78 style='width:58.5pt;border:solid black 1.0pt;border-top:none;
  mso-border-top-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>Classical<o:p></o:p></span></p>
  </td>
  <td width=60 style='width:45.0pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>159<o:p></o:p></span></p>
  </td>
  <td width=54 style='width:40.5pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>66<o:p></o:p></span></p>
  </td>
  <td width=54 style='width:40.5pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>225<o:p></o:p></span></p>
  </td>
  <td width=72 style='width:.75in;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>16%<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:2;page-break-inside:avoid;height:11.05pt'>
  <td width=78 style='width:58.5pt;border:solid black 1.0pt;border-top:none;
  mso-border-top-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>Jazz<o:p></o:p></span></p>
  </td>
  <td width=60 style='width:45.0pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>225<o:p></o:p></span></p>
  </td>
  <td width=54 style='width:40.5pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>12<o:p></o:p></span></p>
  </td>
  <td width=54 style='width:40.5pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>237<o:p></o:p></span></p>
  </td>
  <td width=72 style='width:.75in;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>17%<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:3;page-break-inside:avoid;height:11.05pt'>
  <td width=78 style='width:58.5pt;border:solid black 1.0pt;border-top:none;
  mso-border-top-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>Popular<o:p></o:p></span></p>
  </td>
  <td width=60 style='width:45.0pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>205<o:p></o:p></span></p>
  </td>
  <td width=54 style='width:40.5pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>117<o:p></o:p></span></p>
  </td>
  <td width=54 style='width:40.5pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>322<o:p></o:p></span></p>
  </td>
  <td width=72 style='width:.75in;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>23%<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:4;page-break-inside:avoid;height:11.05pt'>
  <td width=78 style='width:58.5pt;border:solid black 1.0pt;border-top:none;
  mso-border-top-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>World<o:p></o:p></span></p>
  </td>
  <td width=60 style='width:45.0pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>186<o:p></o:p></span></p>
  </td>
  <td width=54 style='width:40.5pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>31<o:p></o:p></span></p>
  </td>
  <td width=54 style='width:40.5pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>217<o:p></o:p></span></p>
  </td>
  <td width=72 style='width:.75in;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>16%<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:5;page-break-inside:avoid;height:11.05pt'>
  <td width=78 style='width:58.5pt;border:solid black 1.0pt;border-top:none;
  mso-border-top-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>Live<o:p></o:p></span></p>
  </td>
  <td width=60 style='width:45.0pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>273<o:p></o:p></span></p>
  </td>
  <td width=54 style='width:40.5pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>109<o:p></o:p></span></p>
  </td>
  <td width=54 style='width:40.5pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>382<o:p></o:p></span></p>
  </td>
  <td width=72 style='width:.75in;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>28%<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:6;mso-yfti-lastrow:yes;page-break-inside:avoid;
  height:11.05pt'>
  <td width=78 style='width:58.5pt;border:solid black 1.0pt;border-top:none;
  mso-border-top-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><b style='mso-bidi-font-weight:
  normal'><span style='font-size:10.0pt;font-family:"Times New Roman","serif"'>Total<o:p></o:p></span></b></p>
  </td>
  <td width=60 style='width:45.0pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><b style='mso-bidi-font-weight:
  normal'><span style='font-size:10.0pt;font-family:"Times New Roman","serif"'>1048<o:p></o:p></span></b></p>
  </td>
  <td width=54 style='width:40.5pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><b style='mso-bidi-font-weight:
  normal'><span style='font-size:10.0pt;font-family:"Times New Roman","serif"'>335<o:p></o:p></span></b></p>
  </td>
  <td width=54 style='width:40.5pt;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><b style='mso-bidi-font-weight:
  normal'><span style='font-size:10.0pt;font-family:"Times New Roman","serif"'>1383<o:p></o:p></span></b></p>
  </td>
  <td width=72 style='width:.75in;border-top:none;border-left:none;border-bottom:
  solid black 1.0pt;border-right:solid black 1.0pt;mso-border-top-alt:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 0in 0in 0in;height:11.05pt'>
  <p class=Body align=center style='text-align:center'><span style='font-size:
  10.0pt;font-family:"Times New Roman","serif"'>100%<o:p></o:p></span></p>
  </td>
 </tr>
</table>

<p class=MsoNormal><b style='mso-bidi-font-weight:normal'><span
style='font-family:"Times New Roman","serif"'>Table </span></b><!--[if supportFields]><b
style='mso-bidi-font-weight:normal'><span style='font-family:"Times New Roman","serif"'><span
style='mso-element:field-begin'></span><span
style='mso-spacerun:yes'> </span>SEQ Tabla \* ARABIC <span style='mso-element:
field-separator'></span></span></b><![endif]--><b style='mso-bidi-font-weight:
normal'><span style='font-family:"Times New Roman","serif"'><span
style='mso-no-proof:yes'>1</span></span></b><!--[if supportFields]><b
style='mso-bidi-font-weight:normal'><span style='font-family:"Times New Roman","serif"'><span
style='mso-element:field-end'></span></span></b><![endif]--><b
style='mso-bidi-font-weight:normal'><span style='font-family:"Times New Roman","serif"'>.</span></b><span
style='font-family:"Times New Roman","serif"'> Breakdown of the MRX10V2 structure
segmentation dataset by musical style<o:p></o:p></span></p>

<p class=MsoNormal><span style='font-size:10.0pt;mso-bidi-font-size:11.0pt;
line-height:115%'><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><b style='mso-bidi-font-weight:normal'><span
style='font-size:12.0pt;line-height:115%;font-family:"Times New Roman","serif"'>2.
<span style='mso-spacerun:yes'>   </span>ALGORITHMS<o:p></o:p></span></b></p>

<table class=MsoNormalTable border=1 cellspacing=0 cellpadding=0
 style='border-collapse:collapse;border:none;mso-border-alt:solid black 1.0pt;
 mso-yfti-tbllook:1696;mso-padding-alt:0in 5.4pt 0in 5.4pt;mso-border-insideh:
 1.0pt solid black;mso-border-insidev:1.0pt solid black'>
 <tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes;height:11.55pt'>
  <td width=111 valign=top style='width:83.6pt;border:solid black 1.0pt;
  border-bottom:solid black 2.25pt;padding:0in 5.4pt 0in 5.4pt;height:11.55pt'>
  <p class=MsoBodyText align=center style='text-align:center'><span
  style='mso-fareast-font-family:Cambria;mso-fareast-language:JA;mso-bidi-font-weight:
  bold'>Algorithms<o:p></o:p></span></p>
  </td>
  <td width=147 valign=top style='width:110.6pt;border-top:solid black 1.0pt;
  border-left:none;border-bottom:solid black 2.25pt;border-right:solid black 1.0pt;
  mso-border-left-alt:solid black 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:
  11.55pt'>
  <p class=MsoBodyText align=center style='text-align:center'><span
  style='mso-fareast-font-family:Cambria;mso-fareast-language:JA;mso-bidi-font-weight:
  bold'>Average processing time (min. / piece)<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:1;height:11.55pt'>
  <td width=111 valign=top style='width:83.6pt;border:solid black 1.0pt;
  border-top:none;mso-border-top-alt:solid black 1.0pt;background:silver;
  padding:0in 5.4pt 0in 5.4pt;height:11.55pt'>
  <p class=MsoBodyText><span style='mso-fareast-font-family:Cambria;mso-fareast-language:
  JA;mso-bidi-font-weight:bold'>WB1<o:p></o:p></span></p>
  </td>
  <td width=147 valign=top style='width:110.6pt;border-top:none;border-left:
  none;border-bottom:solid black 1.0pt;border-right:solid black 1.0pt;
  mso-border-top-alt:solid black 1.0pt;mso-border-left-alt:solid black 1.0pt;
  background:silver;padding:0in 5.4pt 0in 5.4pt;height:11.55pt'>
  <p class=MsoBodyText align=center style='text-align:center'><span
  style='mso-fareast-font-family:Cambria;mso-fareast-language:JA'>2.28<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:2;height:11.25pt'>
  <td width=111 valign=top style='width:83.6pt;border:solid black 1.0pt;
  border-top:none;mso-border-top-alt:solid black 1.0pt;padding:0in 5.4pt 0in 5.4pt;
  height:11.25pt'>
  <p class=MsoBodyText><span style='mso-fareast-font-family:Cambria;mso-fareast-language:
  JA;mso-bidi-font-weight:bold'>GP7 <o:p></o:p></span></p>
  </td>
  <td width=147 valign=top style='width:110.6pt;border-top:none;border-left:
  none;border-bottom:solid black 1.0pt;border-right:solid black 1.0pt;
  mso-border-top-alt:solid black 1.0pt;mso-border-left-alt:solid black 1.0pt;
  padding:0in 5.4pt 0in 5.4pt;height:11.25pt'>
  <p class=MsoBodyText align=center style='text-align:center'><span
  style='mso-fareast-font-family:Cambria;mso-fareast-language:JA'>2.64<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:3;height:11.25pt'>
  <td width=111 valign=top style='width:83.6pt;border:solid black 1.0pt;
  border-top:none;mso-border-top-alt:solid black 1.0pt;background:silver;
  padding:0in 5.4pt 0in 5.4pt;height:11.25pt'>
  <p class=MsoBodyText><span style='mso-fareast-font-family:Cambria;mso-fareast-language:
  JA;mso-bidi-font-weight:bold'>BV1 &amp; BV2 <o:p></o:p></span></p>
  </td>
  <td width=147 valign=top style='width:110.6pt;border-top:none;border-left:
  none;border-bottom:solid black 1.0pt;border-right:solid black 1.0pt;
  mso-border-top-alt:solid black 1.0pt;mso-border-left-alt:solid black 1.0pt;
  background:silver;padding:0in 5.4pt 0in 5.4pt;height:11.25pt'>
  <p class=MsoBodyText align=center style='text-align:center'><span
  style='mso-fareast-font-family:Cambria;mso-fareast-language:JA'>2.94<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:4;height:11.55pt'>
  <td width=111 valign=top style='width:83.6pt;border:solid black 1.0pt;
  border-top:none;mso-border-top-alt:solid black 1.0pt;padding:0in 5.4pt 0in 5.4pt;
  height:11.55pt'>
  <p class=MsoBodyText><span style='mso-fareast-font-family:Cambria;mso-fareast-language:
  JA;mso-bidi-font-weight:bold'>MND1<o:p></o:p></span></p>
  </td>
  <td width=147 valign=top style='width:110.6pt;border-top:none;border-left:
  none;border-bottom:solid black 1.0pt;border-right:solid black 1.0pt;
  mso-border-top-alt:solid black 1.0pt;mso-border-left-alt:solid black 1.0pt;
  padding:0in 5.4pt 0in 5.4pt;height:11.55pt'>
  <p class=MsoBodyText align=center style='text-align:center'><span
  style='mso-fareast-font-family:Cambria;mso-fareast-language:JA'>5.60<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:5;mso-yfti-lastrow:yes;height:11.55pt'>
  <td width=111 valign=top style='width:83.6pt;border:solid black 1.0pt;
  border-top:none;mso-border-top-alt:solid black 1.0pt;background:silver;
  padding:0in 5.4pt 0in 5.4pt;height:11.55pt'>
  <p class=MsoBodyText><span style='mso-fareast-font-family:Cambria;mso-fareast-language:
  JA;mso-bidi-font-weight:bold'>MHRAF2 <o:p></o:p></span></p>
  </td>
  <td width=147 valign=top style='width:110.6pt;border-top:none;border-left:
  none;border-bottom:solid black 1.0pt;border-right:solid black 1.0pt;
  mso-border-top-alt:solid black 1.0pt;mso-border-left-alt:solid black 1.0pt;
  background:silver;padding:0in 5.4pt 0in 5.4pt;height:11.55pt'>
  <p class=MsoBodyText align=center style='text-align:center'><span
  style='mso-fareast-font-family:Cambria;mso-fareast-language:JA'>6.38<o:p></o:p></span></p>
  </td>
 </tr>
</table>

<p class=MsoNormal><b style='mso-bidi-font-weight:normal'><span
style='font-family:"Times New Roman","serif"'>Table 2.</span></b><span
style='font-family:"Times New Roman","serif"'> Algorithm names, corresponding
references, and runtimes.<o:p></o:p></span></p>

<p class=MsoNormal align=left style='margin-bottom:10.0pt;text-align:left;
line-height:115%'><o:p>&nbsp;</o:p></p>

<p class=First-LevelHeadings align=left style='text-align:left;mso-list:l0 level1 lfo2'><![if !supportLists]><span
style='font-size:12.0pt;line-height:107%;mso-fareast-font-family:"Times New Roman";
mso-bidi-font-family:"Times New Roman"'><span style='mso-list:Ignore'>3.<span
style='font:7.0pt "Times New Roman"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</span></span></span><![endif]><span style='font-size:12.0pt;line-height:107%'>Evaluation<o:p></o:p></span></p>

<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0 width=669
 style='width:502.1pt;border-collapse:collapse;border:none;mso-border-alt:solid windowtext .5pt;
 mso-table-overlap:never;mso-yfti-tbllook:1184;mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes;height:15.0pt'>
  <td width=59 nowrap valign=top style='width:44.15pt;border:solid windowtext 1.0pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;
  height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>(a)<o:p></o:p></span></p>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>Algorithm<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>NCE-OSS<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>NCE-USS<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>FPC-F<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>FPC-P<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>FPC-R<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>RCI<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  mso-bidi-font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "Times New Roman"'>SBR-F@0.5s</span><span style='font-size:8.0pt;font-family:
  "Times New Roman","serif";mso-fareast-font-family:"Times New Roman"'><o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  mso-bidi-font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "Times New Roman"'>SBR-P@0.5s</span><span style='font-size:8.0pt;font-family:
  "Times New Roman","serif";mso-fareast-font-family:"Times New Roman"'><o:p></o:p></span></p>
  </td>
  <td width=49 nowrap valign=top style='width:36.65pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  mso-bidi-font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "Times New Roman"'>SBR-R@0.5s</span><span style='font-size:8.0pt;font-family:
  "Times New Roman","serif";mso-fareast-font-family:"Times New Roman"'><o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  mso-bidi-font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "Times New Roman"'>SBR-F@3s</span><span style='font-size:8.0pt;font-family:
  "Times New Roman","serif";mso-fareast-font-family:"Times New Roman"'><o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  mso-bidi-font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "Times New Roman"'>SBR-P@3s</span><span style='font-size:8.0pt;font-family:
  "Times New Roman","serif";mso-fareast-font-family:"Times New Roman"'><o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  mso-bidi-font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "Times New Roman"'>SBR-R@3s</span><span style='font-size:8.0pt;font-family:
  "Times New Roman","serif";mso-fareast-font-family:"Times New Roman"'><o:p></o:p></span></p>
  </td>
  <td width=44 nowrap valign=top style='width:32.8pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>AB-2-RB<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>RB-2-AB<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:1;height:15.0pt'>
  <td width=59 nowrap valign=top style='width:44.15pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>BV1<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.605<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.441<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.520<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.513<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.669<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.549<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.190<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.151<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap valign=top style='width:36.65pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.289<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.450<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.361<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.669<o:p></o:p></span></p>
  </td>
  <td width=44 nowrap valign=top style='width:32.8pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>1.797<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>7.554<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:2;height:15.0pt'>
  <td width=59 nowrap valign=top style='width:44.15pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>BV2<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.454<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.715<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.427<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.678<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.350<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.638<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.189<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.150<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap valign=top style='width:36.65pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.286<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.449<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.361<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.666<o:p></o:p></span></p>
  </td>
  <td width=44 nowrap valign=top style='width:32.8pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>1.812<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>7.552<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:3;height:15.0pt'>
  <td width=59 nowrap valign=top style='width:44.15pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>GP7<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.499<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.683<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.485<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.675<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.424<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.654<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.188<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.146<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap valign=top style='width:36.65pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.306<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.440<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.346<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.695<o:p></o:p></span></p>
  </td>
  <td width=44 nowrap valign=top style='width:32.8pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>2.073<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>6.634<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:4;height:15.0pt'>
  <td width=59 nowrap valign=top style='width:44.15pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>MHRAF2<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.546<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.591<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.559<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.617<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.583<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.659<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.195<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.218<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap valign=top style='width:36.65pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.197<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.435<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.485<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.440<o:p></o:p></span></p>
  </td>
  <td width=44 nowrap valign=top style='width:32.8pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>7.262<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>5.338<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:5;height:15.0pt'>
  <td width=59 nowrap valign=top style='width:44.15pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>MND1<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.624<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.625<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.556<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.649<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.586<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.662<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.291<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.302<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap valign=top style='width:36.65pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.326<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.470<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.479<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.534<o:p></o:p></span></p>
  </td>
  <td width=44 nowrap valign=top style='width:32.8pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>8.565<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>5.389<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:6;height:15.0pt'>
  <td width=59 nowrap valign=top style='width:44.15pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>WB1<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.609<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.540<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.546<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.583<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.608<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.630<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.237<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.240<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap valign=top style='width:36.65pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.272<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.393<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.395<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.446<o:p></o:p></span></p>
  </td>
  <td width=44 nowrap valign=top style='width:32.8pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>10.780<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>3.881<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:7;height:15.0pt'>
  <td width=59 nowrap valign=top style='width:44.15pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  background:#BFBFBF;mso-background-themecolor:background1;mso-background-themeshade:
  191;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>(b)<o:p></o:p></span></p>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>Algorithm<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;
  height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>NCE-OSS<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;
  height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>NCE-USS<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;
  height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>FPC-F<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;
  height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>FPC-P<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;
  height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>FPC-R<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;
  height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>RCI<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;
  height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  mso-bidi-font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "Times New Roman"'>SBR-F@0.5s</span><span style='font-size:8.0pt;font-family:
  "Times New Roman","serif";mso-fareast-font-family:"Times New Roman"'><o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;
  height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  mso-bidi-font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "Times New Roman"'>SBR-P@0.5s</span><span style='font-size:8.0pt;font-family:
  "Times New Roman","serif";mso-fareast-font-family:"Times New Roman"'><o:p></o:p></span></p>
  </td>
  <td width=49 nowrap valign=top style='width:36.65pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;
  height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  mso-bidi-font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "Times New Roman"'>SBR-R@0.5s</span><span style='font-size:8.0pt;font-family:
  "Times New Roman","serif";mso-fareast-font-family:"Times New Roman"'><o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;
  height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  mso-bidi-font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "Times New Roman"'>SBR-F@3s</span><span style='font-size:8.0pt;font-family:
  "Times New Roman","serif";mso-fareast-font-family:"Times New Roman"'><o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;
  height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  mso-bidi-font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "Times New Roman"'>SBR-P@3s</span><span style='font-size:8.0pt;font-family:
  "Times New Roman","serif";mso-fareast-font-family:"Times New Roman"'><o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;
  height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  mso-bidi-font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "Times New Roman"'>SBR-R@3s</span><span style='font-size:8.0pt;font-family:
  "Times New Roman","serif";mso-fareast-font-family:"Times New Roman"'><o:p></o:p></span></p>
  </td>
  <td width=44 nowrap valign=top style='width:32.8pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;
  height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>AB-2-RB<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;
  height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>RB-2-AB<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:8;height:15.0pt'>
  <td width=59 nowrap valign=top style='width:44.15pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>BV1<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.643<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.323<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.384<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.321<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.680<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.505<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.179<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.236<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap valign=top style='width:36.65pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.159<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.567<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.744<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.499<o:p></o:p></span></p>
  </td>
  <td width=44 nowrap valign=top style='width:32.8pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>2.905<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>2.007<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:9;height:15.0pt'>
  <td width=59 nowrap valign=top style='width:44.15pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>BV2<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.521<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.567<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.373<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.452<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.386<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.712<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.177<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.234<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap valign=top style='width:36.65pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.157<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.565<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.741<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.497<o:p></o:p></span></p>
  </td>
  <td width=44 nowrap valign=top style='width:32.8pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>2.937<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>1.980<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:10;height:15.0pt'>
  <td width=59 nowrap valign=top style='width:44.15pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>GP7<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.584<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.557<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.432<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.467<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.482<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.720<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.163<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.208<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap valign=top style='width:36.65pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.153<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.472<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.605<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.436<o:p></o:p></span></p>
  </td>
  <td width=44 nowrap valign=top style='width:32.8pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>4.946<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>2.300<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:11;height:15.0pt'>
  <td width=59 nowrap valign=top style='width:44.15pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>MHRAF2<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.599<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.442<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.440<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.395<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.615<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.655<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.124<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.276<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap valign=top style='width:36.65pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.087<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.356<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.776<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.253<o:p></o:p></span></p>
  </td>
  <td width=44 nowrap valign=top style='width:32.8pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>11.311<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>1.885<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:12;height:15.0pt'>
  <td width=59 nowrap valign=top style='width:44.15pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>MND1<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.666<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.478<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.435<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.426<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.609<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.635<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.200<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.376<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap valign=top style='width:36.65pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.150<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.415<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.749<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.314<o:p></o:p></span></p>
  </td>
  <td width=44 nowrap valign=top style='width:32.8pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>13.944<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>1.835<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:13;mso-yfti-lastrow:yes;height:15.0pt'>
  <td width=59 nowrap valign=top style='width:44.15pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>WB1<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.675<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.420<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.442<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.382<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.653<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.632<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.148<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap valign=top style='width:35.75pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.277<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap valign=top style='width:36.65pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.112<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.317<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.588<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.239<o:p></o:p></span></p>
  </td>
  <td width=44 nowrap valign=top style='width:32.8pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>16.031<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap valign=top style='width:31.7pt;border-top:none;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal'><span style='font-size:8.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>1.975<o:p></o:p></span></p>
  </td>
 </tr>
</table>

<p class=MsoNormal><b style='mso-bidi-font-weight:normal'><span
style='font-family:"Times New Roman","serif"'>Table 3.</span></b><span
style='font-family:"Times New Roman","serif"'> Evaluations against coarse (a)
and fine (b) ground truth annotations.<o:p></o:p></span></p>

<p class=First-LevelHeadings align=left style='margin-left:0in;text-align:left;
text-indent:0in;mso-list:none'><o:p>&nbsp;</o:p></p>

<p class=First-LevelHeadings align=left style='text-align:left'><![if !supportLists]><span
style='font-size:12.0pt;line-height:107%;mso-fareast-font-family:"Times New Roman";
mso-bidi-font-family:"Times New Roman"'><span style='mso-list:Ignore'>4.<span
style='font:7.0pt "Times New Roman"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</span></span></span><![endif]><span style='font-size:12.0pt;line-height:107%'>RESults<o:p></o:p></span></p>

<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0 align=left
 width=335 style='width:251.45pt;border-collapse:collapse;border:none;
 mso-border-alt:solid windowtext .5pt;mso-table-overlap:never;mso-yfti-tbllook:
 1184;mso-table-lspace:9.35pt;margin-left:7.1pt;mso-table-rspace:9.35pt;
 margin-right:7.1pt;mso-table-anchor-vertical:paragraph;mso-table-anchor-horizontal:
 margin;mso-table-left:left;mso-table-top:-4.0pt;mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes;height:15.15pt'>
  <td width=70 nowrap style='width:52.5pt;border:solid windowtext 1.0pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>Algorithm<o:p></o:p></span></p>
  </td>
  <td width=75 nowrap style='width:56.5pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>MIREX10<o:p></o:p></span></p>
  </td>
  <td width=73 nowrap style='width:54.8pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>MIREX09<o:p></o:p></span></p>
  </td>
  <td width=72 nowrap style='width:53.65pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 5.4pt 0in 5.4pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:9.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>MRX10V2<o:p></o:p></span></p>
  </td>
  <td width=45 style='width:34.0pt;border:solid windowtext 1.0pt;border-left:
  none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  background:#BFBFBF;mso-background-themecolor:background1;mso-background-themeshade:
  191;padding:0in 5.4pt 0in 5.4pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>Ave.<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:1;height:15.15pt'>
  <td width=70 nowrap style='width:52.5pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.75pt 0in 5.75pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>BV1<o:p></o:p></span></p>
  </td>
  <td width=75 nowrap style='width:56.5pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.520<o:p></o:p></span></p>
  </td>
  <td width=73 nowrap style='width:54.8pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.502<o:p></o:p></span></p>
  </td>
  <td width=72 nowrap style='width:53.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.520<o:p></o:p></span></p>
  </td>
  <td width=45 style='width:34.0pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.514<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:2;height:15.15pt'>
  <td width=70 nowrap style='width:52.5pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.75pt 0in 5.75pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>BV2<o:p></o:p></span></p>
  </td>
  <td width=75 nowrap style='width:56.5pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.531<o:p></o:p></span></p>
  </td>
  <td width=73 nowrap style='width:54.8pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.493<o:p></o:p></span></p>
  </td>
  <td width=72 nowrap style='width:53.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.427<o:p></o:p></span></p>
  </td>
  <td width=45 style='width:34.0pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.484<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:3;height:15.15pt'>
  <td width=70 nowrap style='width:52.5pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.75pt 0in 5.75pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>GP7<o:p></o:p></span></p>
  </td>
  <td width=75 nowrap style='width:56.5pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.592<o:p></o:p></span></p>
  </td>
  <td width=73 nowrap style='width:54.8pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.536<o:p></o:p></span></p>
  </td>
  <td width=72 nowrap style='width:53.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.485<o:p></o:p></span></p>
  </td>
  <td width=45 style='width:34.0pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.538<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:4;height:15.15pt'>
  <td width=70 nowrap style='width:52.5pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.75pt 0in 5.75pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>MHRAF2<o:p></o:p></span></p>
  </td>
  <td width=75 nowrap style='width:56.5pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.600<o:p></o:p></span></p>
  </td>
  <td width=73 nowrap style='width:54.8pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.555<o:p></o:p></span></p>
  </td>
  <td width=72 nowrap style='width:53.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.559<o:p></o:p></span></p>
  </td>
  <td width=45 style='width:34.0pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.571<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:5;height:15.15pt'>
  <td width=70 nowrap style='width:52.5pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.75pt 0in 5.75pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>MND1<o:p></o:p></span></p>
  </td>
  <td width=75 nowrap style='width:56.5pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.625<o:p></o:p></span></p>
  </td>
  <td width=73 nowrap style='width:54.8pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.613<o:p></o:p></span></p>
  </td>
  <td width=72 nowrap style='width:53.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.556<o:p></o:p></span></p>
  </td>
  <td width=45 style='width:34.0pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><b style='mso-bidi-font-weight:normal'><span style='font-size:10.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.598<o:p></o:p></span></b></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:6;height:15.15pt'>
  <td width=70 nowrap style='width:52.5pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.75pt 0in 5.75pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>WB1<o:p></o:p></span></p>
  </td>
  <td width=75 nowrap style='width:56.5pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.602<o:p></o:p></span></p>
  </td>
  <td width=73 nowrap style='width:54.8pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.544<o:p></o:p></span></p>
  </td>
  <td width=72 nowrap style='width:53.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.546<o:p></o:p></span></p>
  </td>
  <td width=45 style='width:34.0pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.564<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:7;mso-yfti-lastrow:yes;height:15.15pt'>
  <td width=70 nowrap style='width:52.5pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 5.75pt 0in 5.75pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>Ave.<o:p></o:p></span></p>
  </td>
  <td width=75 nowrap style='width:56.5pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><b style='mso-bidi-font-weight:normal'><span style='font-size:10.0pt;
  font-family:"Times New Roman","serif";mso-fareast-font-family:"MS Mincho";
  color:black'>0.578<o:p></o:p></span></b></p>
  </td>
  <td width=73 nowrap style='width:54.8pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.541<o:p></o:p></span></p>
  </td>
  <td width=72 nowrap style='width:53.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;
  height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.516<o:p></o:p></span></p>
  </td>
  <td width=45 style='width:34.0pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;padding:0in 5.75pt 0in 5.75pt;height:15.15pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;page-break-after:avoid;mso-element:frame;
  mso-element-frame-hspace:9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:
  paragraph;mso-element-anchor-horizontal:margin;mso-element-top:-4.0pt;
  mso-height-rule:exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.545<o:p></o:p></span></p>
  </td>
 </tr>
</table>

<p class=First-LevelHeadings align=left style='text-align:left;mso-list:none'><o:p>&nbsp;</o:p></p>

<p class=MsoNormal><o:p>&nbsp;</o:p></p>

<p class=MsoNormal><o:p>&nbsp;</o:p></p>

<p class=MsoNormal><o:p>&nbsp;</o:p></p>

<p class=indent style='margin-top:6.0pt;text-indent:0in;line-height:normal'><span
style='font-size:11.0pt;font-family:"Calibri","sans-serif";mso-ascii-theme-font:
minor-latin;mso-fareast-font-family:Calibri;mso-fareast-theme-font:minor-latin;
mso-hansi-theme-font:minor-latin;mso-bidi-font-family:"Times New Roman";
mso-bidi-theme-font:minor-bidi'><o:p>&nbsp;</o:p></span></p>

<p class=indent style='margin-top:6.0pt;text-indent:0in;line-height:normal'><span
style='font-size:11.0pt;font-family:"Calibri","sans-serif";mso-ascii-theme-font:
minor-latin;mso-fareast-font-family:Calibri;mso-fareast-theme-font:minor-latin;
mso-hansi-theme-font:minor-latin;mso-bidi-font-family:"Times New Roman";
mso-bidi-theme-font:minor-bidi'><o:p>&nbsp;</o:p></span></p>

<p class=indent style='margin-top:6.0pt;text-indent:0in;line-height:normal'><b
style='mso-bidi-font-weight:normal'><span style='font-size:11.0pt'>Table
4a.<span style='mso-spacerun:yes'>  </span></span></b><span style='font-size:
11.0pt'>Comparison of algorithms over datasets<b style='mso-bidi-font-weight:
normal'><o:p></o:p></b></span></p>

<p class=indent style='margin-top:6.0pt;text-indent:0in'><o:p>&nbsp;</o:p></p>

<p class=indent style='margin-top:6.0pt;text-indent:0in'><o:p>&nbsp;</o:p></p>

<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0 align=left
 width=318 style='width:238.75pt;border-collapse:collapse;border:none;
 mso-border-alt:solid windowtext .5pt;mso-table-overlap:never;mso-yfti-tbllook:
 1184;mso-table-lspace:9.35pt;margin-left:7.1pt;mso-table-rspace:9.35pt;
 margin-right:7.1pt;mso-table-anchor-vertical:paragraph;mso-table-anchor-horizontal:
 margin;mso-table-left:left;mso-table-top:-2.2pt;mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes;height:15.0pt'>
  <td width=63 nowrap style='width:47.25pt;border:solid windowtext 1.0pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 0in 0in 0in;height:
  15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>Algorithm<o:p></o:p></span></p>
  </td>
  <td width=41 nowrap style='width:30.5pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>Live<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap style='width:36.65pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>Classical<o:p></o:p></span></p>
  </td>
  <td width=43 nowrap style='width:32.1pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>Jazz<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap style='width:31.65pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>Popular<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap style='width:.5in;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>World<o:p></o:p></span></p>
  </td>
  <td width=33 style='width:24.6pt;border:solid windowtext 1.0pt;border-left:
  none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  background:#BFBFBF;mso-background-themecolor:background1;mso-background-themeshade:
  191;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>Ave.<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:1;height:15.0pt'>
  <td width=63 nowrap style='width:47.25pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>BV1<o:p></o:p></span></p>
  </td>
  <td width=41 nowrap style='width:30.5pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><a name="RANGE!B2:F7"><span style='font-size:10.0pt;font-family:
  "Times New Roman","serif";mso-fareast-font-family:"Times New Roman";
  color:black'>0.504</span></a><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'><o:p></o:p></span></p>
  </td>
  <td width=49 nowrap style='width:36.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.513<o:p></o:p></span></p>
  </td>
  <td width=43 nowrap style='width:32.1pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.544<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap style='width:31.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.519<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap style='width:.5in;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.521<o:p></o:p></span></p>
  </td>
  <td width=33 style='width:24.6pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.520<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:2;height:15.0pt'>
  <td width=63 nowrap style='width:47.25pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>BV2<o:p></o:p></span></p>
  </td>
  <td width=41 nowrap style='width:30.5pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.432<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap style='width:36.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.426<o:p></o:p></span></p>
  </td>
  <td width=43 nowrap style='width:32.1pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.398<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap style='width:31.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.451<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap style='width:.5in;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.439<o:p></o:p></span></p>
  </td>
  <td width=33 style='width:24.6pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.429<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:3;height:15.0pt'>
  <td width=63 nowrap style='width:47.25pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>GP7<o:p></o:p></span></p>
  </td>
  <td width=41 nowrap style='width:30.5pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.510<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap style='width:36.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.427<o:p></o:p></span></p>
  </td>
  <td width=43 nowrap style='width:32.1pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.475<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap style='width:31.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.513<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap style='width:.5in;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.484<o:p></o:p></span></p>
  </td>
  <td width=33 style='width:24.6pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.482<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:4;height:15.0pt'>
  <td width=63 nowrap style='width:47.25pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>MND1<o:p></o:p></span></p>
  </td>
  <td width=41 nowrap style='width:30.5pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.532<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap style='width:36.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.564<o:p></o:p></span></p>
  </td>
  <td width=43 nowrap style='width:32.1pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.574<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap style='width:31.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.574<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap style='width:.5in;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.545<o:p></o:p></span></p>
  </td>
  <td width=33 style='width:24.6pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.558<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:5;height:15.0pt'>
  <td width=63 nowrap style='width:47.25pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>MHRAF2<o:p></o:p></span></p>
  </td>
  <td width=41 nowrap style='width:30.5pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.557<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap style='width:36.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.590<o:p></o:p></span></p>
  </td>
  <td width=43 nowrap style='width:32.1pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.556<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap style='width:31.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.543<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap style='width:.5in;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.555<o:p></o:p></span></p>
  </td>
  <td width=33 style='width:24.6pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.560<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:6;height:15.0pt'>
  <td width=63 nowrap style='width:47.25pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>WB1<o:p></o:p></span></p>
  </td>
  <td width=41 nowrap style='width:30.5pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.560<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap style='width:36.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.524<o:p></o:p></span></p>
  </td>
  <td width=43 nowrap style='width:32.1pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.547<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap style='width:31.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.548<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap style='width:.5in;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>0.537<o:p></o:p></span></p>
  </td>
  <td width=33 style='width:24.6pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.543<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:7;mso-yfti-lastrow:yes;height:15.0pt'>
  <td width=63 nowrap style='width:47.25pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"Times New Roman";color:black'>Ave.<o:p></o:p></span></p>
  </td>
  <td width=41 nowrap style='width:30.5pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.516<o:p></o:p></span></p>
  </td>
  <td width=49 nowrap style='width:36.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.507<o:p></o:p></span></p>
  </td>
  <td width=43 nowrap style='width:32.1pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.516<o:p></o:p></span></p>
  </td>
  <td width=42 nowrap style='width:31.65pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.525<o:p></o:p></span></p>
  </td>
  <td width=48 nowrap style='width:.5in;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.514<o:p></o:p></span></p>
  </td>
  <td width=33 style='width:24.6pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-top-alt:
  solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:paragraph;
  mso-element-anchor-horizontal:margin;mso-element-top:-2.2pt;mso-height-rule:
  exactly'><span style='font-size:10.0pt;font-family:"Times New Roman","serif";
  mso-fareast-font-family:"MS Mincho";color:black'>0.515<o:p></o:p></span></p>
  </td>
 </tr>
</table>

<p class=MsoNormal><o:p>&nbsp;</o:p></p>

<p class=MsoNormal><o:p>&nbsp;</o:p></p>

<p class=MsoNormal><o:p>&nbsp;</o:p></p>

<p class=MsoNormal><o:p>&nbsp;</o:p></p>

<p class=MsoNormal><o:p>&nbsp;</o:p></p>

<p class=MsoNormal><b style='mso-bidi-font-weight:normal'><span
style='font-family:"Times New Roman","serif";mso-fareast-language:JA;
mso-no-proof:yes'><o:p>&nbsp;</o:p></span></b></p>

<p class=MsoNormal><b style='mso-bidi-font-weight:normal'><span
style='font-family:"Times New Roman","serif";mso-fareast-language:JA;
mso-no-proof:yes'>Table 4b</span></b><span style='font-family:"Times New Roman","serif";
mso-fareast-language:JA;mso-no-proof:yes'>. Results by musical style<o:p></o:p></span></p>

<p class=MsoNormal><span style='font-family:"Times New Roman","serif";
mso-fareast-language:JA;mso-no-proof:yes'><o:p>&nbsp;</o:p></span></p>

<p class=MsoNormal><span style='mso-fareast-language:JA;mso-no-proof:yes'><o:p>&nbsp;</o:p></span></p>

<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0 align=left
 width=307 style='width:3.2in;border-collapse:collapse;border:none;mso-border-alt:
 solid windowtext .5pt;mso-table-overlap:never;mso-yfti-tbllook:1184;
 mso-table-lspace:9.35pt;margin-left:7.1pt;mso-table-rspace:9.35pt;margin-right:
 7.1pt;mso-table-anchor-vertical:page;mso-table-anchor-horizontal:margin;
 mso-table-left:left;mso-table-top:156.05pt;mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes;height:15.0pt'>
  <td width=70 nowrap style='width:52.5pt;border:solid windowtext 1.0pt;
  mso-border-alt:solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:
  background1;mso-background-themeshade:191;padding:0in 0in 0in 0in;height:
  15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "Times New Roman";color:black'>Algorithm<o:p></o:p></span></p>
  </td>
  <td width=117 nowrap style='width:87.9pt;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "Times New Roman";color:black'>Fine<o:p></o:p></span></p>
  </td>
  <td width=120 nowrap style='width:1.25in;border:solid windowtext 1.0pt;
  border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:
  solid windowtext .5pt;background:#BFBFBF;mso-background-themecolor:background1;
  mso-background-themeshade:191;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "Times New Roman";color:black'>Coarse<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:1;height:15.0pt'>
  <td width=70 nowrap style='width:52.5pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>BV1<o:p></o:p></span></p>
  </td>
  <td width=117 nowrap style='width:87.9pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>0.392<o:p></o:p></span></p>
  </td>
  <td width=120 nowrap style='width:1.25in;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>0.5248<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:2;height:15.0pt'>
  <td width=70 nowrap style='width:52.5pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>BV2<o:p></o:p></span></p>
  </td>
  <td width=117 nowrap style='width:87.9pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>0.371<o:p></o:p></span></p>
  </td>
  <td width=120 nowrap style='width:1.25in;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>0.4338<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:3;height:15.0pt'>
  <td width=70 nowrap style='width:52.5pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>GP7<o:p></o:p></span></p>
  </td>
  <td width=117 nowrap style='width:87.9pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>0.4332<o:p></o:p></span></p>
  </td>
  <td width=120 nowrap style='width:1.25in;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>0.4848<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:4;height:15.0pt'>
  <td width=70 nowrap style='width:52.5pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>MHRAF2<o:p></o:p></span></p>
  </td>
  <td width=117 nowrap style='width:87.9pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>0.4477<o:p></o:p></span></p>
  </td>
  <td width=120 nowrap style='width:1.25in;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>0.5647<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:5;height:15.0pt'>
  <td width=70 nowrap style='width:52.5pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>MND1<o:p></o:p></span></p>
  </td>
  <td width=117 nowrap style='width:87.9pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>0.4423<o:p></o:p></span></p>
  </td>
  <td width=120 nowrap style='width:1.25in;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>0.559<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:6;height:15.0pt'>
  <td width=70 nowrap style='width:52.5pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>WB1<o:p></o:p></span></p>
  </td>
  <td width=117 nowrap style='width:87.9pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>0.4491<o:p></o:p></span></p>
  </td>
  <td width=120 nowrap style='width:1.25in;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>0.5522<o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:7;mso-yfti-lastrow:yes;height:15.0pt'>
  <td width=70 nowrap style='width:52.5pt;border:solid windowtext 1.0pt;
  border-top:none;mso-border-top-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;
  padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>Human<o:p></o:p></span></p>
  </td>
  <td width=117 nowrap style='width:87.9pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>0.6289<o:p></o:p></span></p>
  </td>
  <td width=120 nowrap style='width:1.25in;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  mso-border-top-alt:solid windowtext .5pt;mso-border-left-alt:solid windowtext .5pt;
  mso-border-alt:solid windowtext .5pt;padding:0in 0in 0in 0in;height:15.0pt'>
  <p class=MsoNormal align=center style='margin-bottom:0in;margin-bottom:.0001pt;
  text-align:center;line-height:normal;mso-element:frame;mso-element-frame-hspace:
  9.35pt;mso-element-wrap:around;mso-element-anchor-vertical:page;mso-element-anchor-horizontal:
  margin;mso-element-top:156.05pt;mso-height-rule:exactly'><span
  style='font-size:10.0pt;font-family:"Times New Roman","serif";mso-fareast-font-family:
  "MS Mincho";color:black'>0.7211<o:p></o:p></span></p>
  </td>
 </tr>
</table>




<p class=MsoNormal><o:p>&nbsp;</o:p></p>

<p class=MsoNormal><o:p>&nbsp;</o:p></p>

<p class=MsoNormal><o:p>&nbsp;</o:p></p>

<p class=MsoNormal><o:p>&nbsp;</o:p></p>

<p class=MsoNormal><o:p>&nbsp;</o:p></p>

<p class=MsoNormal><b style='mso-bidi-font-weight:normal'><span
style='font-family:"Times New Roman","serif";mso-fareast-language:JA;
mso-no-proof:yes'><o:p>&nbsp;</o:p></span></b></p>

<p class=MsoNormal><b style='mso-bidi-font-weight:normal'><span
style='font-family:"Times New Roman","serif";mso-fareast-language:JA;
mso-no-proof:yes'>Table 4c</span></b><span style='font-family:"Times New Roman","serif";
mso-fareast-language:JA;mso-no-proof:yes'>. Fined-grained vs. coarse-grained FPC-F results<o:p></o:p></span></p>
<p class=MsoNormal><o:p>&nbsp;</o:p></p>

<p class=MsoNormal><span style='mso-no-proof:yes'><!--[if gte vml 1]><v:shapetype
 id="_x0000_t75" coordsize="21600,21600" o:spt="75" o:preferrelative="t"
 path="m@4@5l@4@11@9@11@9@5xe" filled="f" stroked="f">
 <v:stroke joinstyle="miter"/>
 <v:formulas>
  <v:f eqn="if lineDrawn pixelLineWidth 0"/>
  <v:f eqn="sum @0 1 0"/>
  <v:f eqn="sum 0 0 @1"/>
  <v:f eqn="prod @2 1 2"/>
  <v:f eqn="prod @3 21600 pixelWidth"/>
  <v:f eqn="prod @3 21600 pixelHeight"/>
  <v:f eqn="sum @0 0 1"/>
  <v:f eqn="prod @6 1 2"/>
  <v:f eqn="prod @7 21600 pixelWidth"/>
  <v:f eqn="sum @8 21600 0"/>
  <v:f eqn="prod @7 21600 pixelHeight"/>
  <v:f eqn="sum @10 21600 0"/>
 </v:formulas>
 <v:path o:extrusionok="f" gradientshapeok="t" o:connecttype="rect"/>
 <o:lock v:ext="edit" aspectratio="t"/>
</v:shapetype><v:shape id="Picture_x0020_20" o:spid="_x0000_i1025" type="#_x0000_t75"
 style='width:447.75pt;height:4in;visibility:visible;mso-wrap-style:square'>
 <v:imagedata src="ismir_tables_files/image001.emz" o:title=""/>
</v:shape><![endif]--><![if !vml]><img width=597 height=384
src="image003.gif" v:shapes="Picture_x0020_20"><![endif]></span></p>

<p class=MsoCaption><b style='mso-bidi-font-weight:normal'><span
style='font-size:11.0pt'>Figure 4a</span></b><span style='font-size:11.0pt'>.
Tukey-Kramer HSD comparison plots of the human and algorithm mean performance
ranks across 794 double-keyed tracks</span><span style='font-size:11.0pt;
mso-fareast-language:JA;mso-no-proof:yes'><o:p></o:p></span></p>

<p class=MsoNormal><o:p>&nbsp;</o:p></p>

                                                </div>
                                                </body>
                                                </html>
