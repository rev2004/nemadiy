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
                            <li><a href="index.jsp" title="classical"><span>Summary</span></a></li>
                            <li><a href="comparison.jsp" title="comparison"><span>Comparison</span></a></li>
                            <li class="shown"><a href="classical.jsp" title="classical"><span>Classical</span></a></li>
                            <li><a href="jazz.jsp" title="Jazz"><span>Jazz</span></a></li>
                            <li><a href="live.jsp" title="Live"><span>Live</span></a></li>
                            <li><a href="pop.jsp" title="Pop"><span>Popular</span></a></li>
                            <li><a href="world.jsp" title="World"><span>World</span></a></li>
                            <li><a href="help.jsp" title="help"><span>Help</span></a></li>
                        </ul>
                    </div>
                    <br><a name="top"></a>
                        <div id="content">

                            <ul>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000000')">Plot</button> salami000000 (English Baroque Soloists John Eliot Ga, Symphony No)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000004')">Plot</button> salami000004 (Compilations, Orontea Act II Scene 17 Intorno A)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000006')">Plot</button> salami000006 (Stradivaria, Sonata Prima Opus 34 Presto)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000013')">Plot</button> salami000013 (Compilations, Historia Di Jephte Plorate Colles A)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000016')">Plot</button> salami000016 (Andre s Segovia, Studies In E op)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000023')">Plot</button> salami000023 (Montreal Symphony Orchestra Charles Du, Concerto in D Rondo)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000030')">Plot</button> salami000030 (Compilations, Pavana Lachrymae 2)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000049')">Plot</button> salami000049 (Jordi Saval, Pre lude pour Mr)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000062')">Plot</button> salami000062 (Compilations, La Griselda Act II Scene I Mi Riv 3)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000067')">Plot</button> salami000067 (Les Chambristes de Montreal, Rhapsodie a sept I)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000069')">Plot</button> salami000069 (Sequentia, Instrumental Piece 2)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000080')">Plot</button> salami000080 (Compilations, Cello Concerto in C Op)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000097')">Plot</button> salami000097 (City of Birmingham Symphony Orchestra, Petrushka 4e me tableau Danse des n)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000098')">Plot</button> salami000098 (Strada Ensemble Anonymus, Chanconetta Tedesca)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000101')">Plot</button> salami000101 (Compilations, Symphony In F Major No)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000124')">Plot</button> salami000124 (Gothic Voices Christopher Page, Je demande ma bienvenue)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000135')">Plot</button> salami000135 (Kronos Quartet, Black Angels God Music)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000140')">Plot</button> salami000140 (Benedictine Monks of Santo Domingo De Si, Christe Redemptor)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000162')">Plot</button> salami000162 (Compilations, Le Devin Du Village Scene 1 J ai)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000163')">Plot</button> salami000163 (Steve Reich Musicians, Octet 4 79 )</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000170')">Plot</button> salami000170 (Stradivaria, Sonate en quatuor)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000175')">Plot</button> salami000175 (The King s Consort Robert King, Overture in C Major Wassermusik H 5)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000179')">Plot</button> salami000179 (Compilations, Kemp s Jig)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000180')">Plot</button> salami000180 (Compilations, La Poste Lute version and harpsicho 1)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000188')">Plot</button> salami000188 (Zoltan Tokos Danubius String Quartet, Quintet in D Major G)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000198')">Plot</button> salami000198 (Compilations, Sinfonietta II)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000207')">Plot</button> salami000207 (Tatiana Nikolayeva, Prelude and Fugue No)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000216')">Plot</button> salami000216 (Compilations, Black Angels for Electric String Qua)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000217')">Plot</button> salami000217 (William Primrose, La Campanella)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000223')">Plot</button> salami000223 (Puirt A Baroque, Callam)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000234')">Plot</button> salami000234 (Dietrich Fischer Dieskau Gerald Moore, Schwanengesang D 957 08 Der Atlas)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000235')">Plot</button> salami000235 (Compilations, Concert Music for Strings and Brass )</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000239')">Plot</button> salami000239 (Koda ly Quartet, The Seven Last Words of Jesus Christ 1)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000242')">Plot</button> salami000242 (Puirt A Baroque, Kinloch s Fantasy Kinloche His Fan)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000245')">Plot</button> salami000245 (Kirov Chorus and Orchestra Valery Gerg, Ruslan and Lyudmila Oriental Dance)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000267')">Plot</button> salami000267 (Jordi Saval, Les Pleurs)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000271')">Plot</button> salami000271 (Compilations, Danseries A 4 Parties Basse Danse A 1)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000289')">Plot</button> salami000289 (Compilations, Fandango)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000292')">Plot</button> salami000292 (Sarah Chang, Chaconne)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000300')">Plot</button> salami000300 (Daniel Adni, Songs Without Words Op 30 No 4)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000327')">Plot</button> salami000327 (Compilations, Der Vogelfa nger bin ich ja)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000353')">Plot</button> salami000353 (RWC MDB C 2001 M06, 13)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000371')">Plot</button> salami000371 (RWC MDB C 2001 M03, 5)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000382')">Plot</button> salami000382 (Compilations, The Beggar s Opera Scenes 11 to 13 35)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000403')">Plot</button> salami000403 (Yuri Bashmet, Bruch Concerto III)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000445')">Plot</button> salami000445 (Puirt A Baroque, Bonnocks of Beer Meal)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000463')">Plot</button> salami000463 (Nobuko Imai Roland Po ntinen, Salut D amour Op)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000470')">Plot</button> salami000470 (Sofia Chamber Choir, Missa a cappella Kyrie)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000478')">Plot</button> salami000478 (Steven Dann Bruce Vogt, Sonata for Viola and Piano Impetuoso)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000480')">Plot</button> salami000480 (Compilations, Firebird Disappearance of the pala)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000482')">Plot</button> salami000482 (Andre s Segovia, Studies for the guitar I)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000490')">Plot</button> salami000490 (Maria Cristina Kiehr Concerto Soave, Nascente Maria 2)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000516')">Plot</button> salami000516 (Isaac Stern Co Liang Lin Jaime Laredo, String Quintet in E major Op)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000517')">Plot</button> salami000517 (The King s Consort Robert King, Water Music Suite in D G Major M 1)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000518')">Plot</button> salami000518 (Compilations, Deh Vieni alla finestra)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000519')">Plot</button> salami000519 (Stradivaria, Gigue)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000526')">Plot</button> salami000526 (Gothic Voices Christopher Page, Fortune Faulce Parverse Rondeau)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000527')">Plot</button> salami000527 (Compilations, Belle Bonne Sage)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000543')">Plot</button> salami000543 (Compilations, Mass for Christmas Day Kyrie)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000548')">Plot</button> salami000548 (Choir of Westminster Cathedral David H, Missa Brevis Credo)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000550')">Plot</button> salami000550 (Compilations, Gehimes Flu stern Hier Und Dort)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000552')">Plot</button> salami000552 (Nobuko Imai Roland Po ntinen, Sonata for Viola Piano Op)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000554')">Plot</button> salami000554 (RWC MDB C 2001 M03, 3)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000555')">Plot</button> salami000555 (Compilations, Cielo e mar)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000595')">Plot</button> salami000595 (Compilations, Violin Concerto 2 in D K)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000596')">Plot</button> salami000596 (Compilations, Se vuol ballare)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000610')">Plot</button> salami000610 (Compilations, Belicha)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000618')">Plot</button> salami000618 (Compilations, Suite In D Major Prince Of Denmark )</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000620')">Plot</button> salami000620 (RWC MDB C 2001 M01, 6)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000621')">Plot</button> salami000621 (Barbara Nissman, Piano Sonata No)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000640')">Plot</button> salami000640 (Puirt A Baroque, Black Jock Macklean Sonata I)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000667')">Plot</button> salami000667 (Gothic Voices Christopher Page, Je La Remire La Belle Rondeau)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000669')">Plot</button> salami000669 (Puirt A Baroque, Lilac Slips Languido Largo)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000674')">Plot</button> salami000674 (Compilations, D ung Aultre Amer)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000678')">Plot</button> salami000678 (Compilations, Deh vieni non tardar)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000710')">Plot</button> salami000710 (Compilations, Orfeo Ed Euridice Act II Scene I 3)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000712')">Plot</button> salami000712 (Maria Cristina Kiehr Concerto Soave, Tastegiata 1 )</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000724')">Plot</button> salami000724 (Compilations, Suite in F Minor Gigue)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000731')">Plot</button> salami000731 (Pepe Romero, Cantos Des Espana Op)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000742')">Plot</button> salami000742 (Raphael Wallfisch City Of London Sinf, Concerto in F major RV411 2)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000752')">Plot</button> salami000752 (Compilations, La ci darem la mano)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000767')">Plot</button> salami000767 (Pepe Romero, Tango Maria)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000773')">Plot</button> salami000773 (Compilations, La fleur que tu m avais jetee)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000782')">Plot</button> salami000782 (Nobuko Imai Roland Po ntinen, Fantasia Cromatica In D Minor BWV 9)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000790')">Plot</button> salami000790 (Maurizio Pollini, Piano Sonata in B minor S)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000804')">Plot</button> salami000804 (Glenn Gould, Sonata in A minor Wg)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000810')">Plot</button> salami000810 (RWC MDB C 2001 M06, 14)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000832')">Plot</button> salami000832 (Lorraine McAslan John Blakely, Sonata for Violon and Piano III)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000834')">Plot</button> salami000834 (Pepe Romero, Tres Preludios Aleluya)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000845')">Plot</button> salami000845 (Choir of Westminster Cathedral David H, Missa O quam gloriosum c1583 2 G)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000849')">Plot</button> salami000849 (Hesperus, Tant Que Je Vivrai)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000850')">Plot</button> salami000850 (Compilations, Suite De Symphonies 1 I Rondeau)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000855')">Plot</button> salami000855 (Glenn Gould, Piano Sonata in C major Hob)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000860')">Plot</button> salami000860 (Anner Bylsma, Cello Concerto In A Major Wq)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000867')">Plot</button> salami000867 (Daniel Barenboim, Don Giovanni Act 1 S3 Ah Fuggi)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000875')">Plot</button> salami000875 (Neues Leipziger String Quartet, Five Movements For String Quartet Op)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000881')">Plot</button> salami000881 (Compilations, Piano Concerto No)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000899')">Plot</button> salami000899 (Compilations, Gregorian Chant For Good Friday Re)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000900')">Plot</button> salami000900 (Benedictine Monks of Santo Domingo De Si, Nos Autem)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000905')">Plot</button> salami000905 (RWC MDB C 2001 M05, 3)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000936')">Plot</button> salami000936 (Compilations, Der Freischutz Act II Finale Wolf 5)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000952')">Plot</button> salami000952 (Borodin String Quartet, String Quartet No)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000964')">Plot</button> salami000964 (Hesperus, Gracieusette)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000969')">Plot</button> salami000969 (Matt Haimovitz Chicago Symphony Orches, Concerto for Violoncello and Orchest 3)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000982')">Plot</button> salami000982 (Liszt Ferenc Chamber Orchestra Janos R, Serenade)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000990')">Plot</button> salami000990 (Andre s Segovia, Studies In A minor)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000999')">Plot</button> salami000999 (Puirt A Baroque, Lilac Slips Brilliante Giga Bril)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001021')">Plot</button> salami001021 (Compilations, Contessa perdono)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001025')">Plot</button> salami001025 (Christopher Herrick, Six Part Ricercare ni C minor Musi)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001050')">Plot</button> salami001050 (Claude Lamothe, Blue fiddle)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001069')">Plot</button> salami001069 (Mstislav Rostropovich, Cello Concerto in D major 2 II)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001083')">Plot</button> salami001083 (Maurizio Pollini, Douze Etudes pour le piano Pour le 1)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001100')">Plot</button> salami001100 (Compilations, Te Deum In D Major Introduction)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001110')">Plot</button> salami001110 (The Academy Of Ancient Music Christoph, Dido Aeneas Act 2 In our deep vau)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001129')">Plot</button> salami001129 (Compilations, Tant Ai Ame C or Me Convient Hair)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001134')">Plot</button> salami001134 (Danielle Cummings, Five Bagatelles )</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001135')">Plot</button> salami001135 (Stradivaria, Sonata in Ecco)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001159')">Plot</button> salami001159 (Compilations, Pro et Contra III 1)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001165')">Plot</button> salami001165 (Compilations, Nach Bach Fantasy for harpsichord o 2)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001169')">Plot</button> salami001169 (Robert Shaw Atlanta Symphony Orchestra, Requiem K)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001178')">Plot</button> salami001178 (Compilations, Non Avra Ma Pieta Stanza)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001191')">Plot</button> salami001191 (Compilations, String Quartet Op)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001194')">Plot</button> salami001194 (Compilations, Intermezzo)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001216')">Plot</button> salami001216 (The Amadeus Ensemble, Riconosci in questo amplesso)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001219')">Plot</button> salami001219 (Laurence Cummings Reiko Ichise, Premier Ordre Book 1 Allemande l )</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001220')">Plot</button> salami001220 (Academy Of St Martin In The Fields, Aprite Un Po quegli Occhi Figaro )</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001226')">Plot</button> salami001226 (Compilations, Una voce poco fa)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001238')">Plot</button> salami001238 (Wouter Moller Linde Consort, Cello concerto 7 G 480)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001244')">Plot</button> salami001244 (Hesperus, Una Panthera)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001246')">Plot</button> salami001246 (Compilations, Nocturne In A Major No)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001253')">Plot</button> salami001253 (Gothic Voices Christopher Page, Mon Cuer Me Fait Tous Dis Penser R)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001287')">Plot</button> salami001287 (Compilations, Trio Sonata Op)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001291')">Plot</button> salami001291 (RWC MDB C 2001 M01, 5)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001300')">Plot</button> salami001300 (RWC MDB C 2001 M06, 2)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001302')">Plot</button> salami001302 (Quartetto Italiano, String Quartet in F III)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001326')">Plot</button> salami001326 (Compilations, Three Songs to Poems by Carl Sandbur)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001338')">Plot</button> salami001338 (Miklos Spanyi, Sonata in E minor H)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001359')">Plot</button> salami001359 (RWC MDB C 2001 M06, 9)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001368')">Plot</button> salami001368 (Compilations, Lamentation gaite sur la mort tres d 1)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001380')">Plot</button> salami001380 (RWC MDB C 2001 M06, 8)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001386')">Plot</button> salami001386 (Sviatoslav Richter, E tudes op)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001402')">Plot</button> salami001402 (Yuri Bashmet Mikhail Muntian, Sonata for Viola and Piano Op)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001408')">Plot</button> salami001408 (Czech Philharmonic, Requiem Agnus Dei)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001421')">Plot</button> salami001421 (Norbert Kraft Northern Chamber Orchest, Concerto for Guitar and Orchestra 2)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001424')">Plot</button> salami001424 (RWC MDB C 2001 M06, 1)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001442')">Plot</button> salami001442 (Compilations, Recondita Armonia)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001447')">Plot</button> salami001447 (The Moscow String Quartet, String Quartet No)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001460')">Plot</button> salami001460 (Benedictine Monks of Santo Domingo De Si, Spiritus Domini)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001461')">Plot</button> salami001461 (Benedictine Monks of Santo Domingo De Si, De Profundis)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001463')">Plot</button> salami001463 (Compilations, Orchestral Suite No)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001474')">Plot</button> salami001474 (Compilations, La Dousa Votz)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001479')">Plot</button> salami001479 (Vlado Perlemuter Concerts Colonne Orch, Menuet sur le nom de Haydn)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001480')">Plot</button> salami001480 (Maria Cristina Kiehr Concerto Soave, Cappriccio cromatico)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001501')">Plot</button> salami001501 (Nobuko Imai Roland Po ntinen, Sonata In G Minor Largo)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001504')">Plot</button> salami001504 (A Sei Voci, Missa de Beata Virgine Agnus Dei I)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001540')">Plot</button> salami001540 (Sequentia, Como O Nome Da Virgen)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001549')">Plot</button> salami001549 (Puirt A Baroque, Macklean Sonata I Allegro)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001568')">Plot</button> salami001568 (Compilations, Chi mi frena in tal momento)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001570')">Plot</button> salami001570 (RWC MDB C 2001 M05, 5)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001584')">Plot</button> salami001584 (Vladimir Horowitz, Sonata in F minor K466)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001602')">Plot</button> salami001602 (Nathan Milstein St Loius Symphony Orc, Faust Dance of the Trojan Maidens)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001604')">Plot</button> salami001604 (Walter Gieseking, Preludes Book II Hommage a S)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001608')">Plot</button> salami001608 (Compilations, Wozzeck Act III Scene 3 2)</li>
                                <li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001610')">Plot</button> salami001610 (Glenn Gould, Piano Sonata in B minor Op)</li>                            </ul>

                        </div>
                </body>
                </html>
