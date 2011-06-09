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
                                <li ><a href="classical.jsp" title="classical"><span>Classical</span></a></li>
                                <li><a href="jazz.jsp" title="Jazz"><span>Jazz</span></a></li>
                                <li><a href="live.jsp" title="Live"><span>Live</span></a></li>
                                <li><a href="pop.jsp" title="Pop"><span>Popular</span></a></li>
                                <li class="shown"><a href="world.jsp" title="World"><span>World</span></a></li>
                            </ul>
                        </div>
                    <br><a name="top"></a>
                        <div id="content">

                            <ul><li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000005')">Plot</button> salami000005 (Ensemble Ro mulo Larrea Vero nica Larc, Tango Diablo)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000010')">Plot</button> salami000010 (Yo Yo Ma, Cafe 1930)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000015')">Plot</button> salami000015 (Compilations, Baladele Revoluteii)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000034')">Plot</button> salami000034 (Compilations, Ovdoviala Lissitchkata)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000036')">Plot</button> salami000036 (Compilations, Mayeya No Juegues con los )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000060')">Plot</button> salami000060 (Compilations, Hava Nagila)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000081')">Plot</button> salami000081 (Jacques Brel, Les Timides)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000099')">Plot</button> salami000099 (Compilations, Koku Reibo A Bell Ringing in the Em)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000105')">Plot</button> salami000105 (Ensemble Ro mulo Larrea Vero nica Larc, Contrabajisimo)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000106')">Plot</button> salami000106 (Giora Feidman, Ki Mizion)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000108')">Plot</button> salami000108 (Compilations, U selo kavga golema)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000145')">Plot</button> salami000145 (Compilations, Ramona)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000146')">Plot</button> salami000146 (Compilations, Oh Kesario Hazari Gul Ro Phool)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000153')">Plot</button> salami000153 (Compilations, Candela)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000171')">Plot</button> salami000171 (Compilations, Rokudan No Shirabe)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000177')">Plot</button> salami000177 (Compilations, Sunrise Song)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000192')">Plot</button> salami000192 (Te tes Raides, Viens )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000197')">Plot</button> salami000197 (Compilations, The Flower Of Carnage)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000204')">Plot</button> salami000204 (Jacques Brel, La valse A Mille Temps)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000218')">Plot</button> salami000218 (Compilations, On the Sunny Meadow)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000246')">Plot</button> salami000246 (Compilations, Enrique Melchor De Mi Fuente)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000249')">Plot</button> salami000249 (Compilations, Anoku Gonda)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000258')">Plot</button> salami000258 (Compilations, Basarabye)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000261')">Plot</button> salami000261 (Babatunde Olatunji, Oba Igbo)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000262')">Plot</button> salami000262 (Compilations, Krakowiak Z Mucharza)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000264')">Plot</button> salami000264 (Compilations, La Ultima Noche)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000270')">Plot</button> salami000270 (Tito Puente, Mambo Gallego)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000274')">Plot</button> salami000274 (Compilations, Devil Woman)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000277')">Plot</button> salami000277 (Gianmaria Testa, Na stella)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000296')">Plot</button> salami000296 (Compilations, Gambangan Gamelan Semar Pegulingan)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000308')">Plot</button> salami000308 (Compilations, One Dime Blues)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000313')">Plot</button> salami000313 (Compilations, Ako)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000320')">Plot</button> salami000320 (Compilations, Track 08)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000325')">Plot</button> salami000325 (Compilations, Going Back To Old Virginia)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000334')">Plot</button> salami000334 (Compilations, Me Pongo a Pregonar)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000343')">Plot</button> salami000343 (The Derek Trucks Band, Sierra Leone)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000350')">Plot</button> salami000350 (Compilations, The Robe Of Feathers Hagoromo)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000354')">Plot</button> salami000354 (Chebi i Sabbah, Maheshvara Yogi)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000358')">Plot</button> salami000358 (Compilations, Djelem Djelem)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000365')">Plot</button> salami000365 (Besh O Drom, Engem anya m mega tkozott My mother c)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000368')">Plot</button> salami000368 (Compilations, Nuevo Sol)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000381')">Plot</button> salami000381 (Compilations, Kaval Sviri)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000392')">Plot</button> salami000392 (Rachid Taha, Ida)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000393')">Plot</button> salami000393 (Compilations, Chaiyya Chaiyya Bollywood Joint)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000399')">Plot</button> salami000399 (Paolo Conte, Gong oh)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000401')">Plot</button> salami000401 (Hakim, Eh darun)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000423')">Plot</button> salami000423 (Compay Segundo, Cuba Y Espan a Cuba And Spain )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000432')">Plot</button> salami000432 (Compilations, Granadina)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000450')">Plot</button> salami000450 (Compilations, Sat Bhayan Ki Ek Behanadly II)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000455')">Plot</button> salami000455 (Compilations, Burundanga)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000471')">Plot</button> salami000471 (Compilations, Moulay Tahar)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000477')">Plot</button> salami000477 (Compilations, Grass Dance Song)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000497')">Plot</button> salami000497 (Issa Bagayogo, Timbuktu)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000521')">Plot</button> salami000521 (Compilations, Karnatek Kriti Marakata Manivarna)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000533')">Plot</button> salami000533 (Susana Baca, Se Me Van Los Pies)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000560')">Plot</button> salami000560 (Compilations, Tango Apasionado)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000582')">Plot</button> salami000582 (Gidon Kremer, Cafe 1930)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000592')">Plot</button> salami000592 (Compilations, Alf Leila)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000593')">Plot</button> salami000593 (Compilations, Kali Sara)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000597')">Plot</button> salami000597 (Compilations, Der Heyser Bulgar)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000624')">Plot</button> salami000624 (Compilations, Cigany Himnusz)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000630')">Plot</button> salami000630 (Compilations, Thumri in Raga Khamaj)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000643')">Plot</button> salami000643 (Jesse Cook, That s Right)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000644')">Plot</button> salami000644 (Compilations, Track 03)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000652')">Plot</button> salami000652 (Compilations, Raga Tilak Kamod Madhyalaya Teentaa)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000656')">Plot</button> salami000656 (Compilations, Paznja Paznja )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000663')">Plot</button> salami000663 (Compilations, Taboehgan Gamelan Semar Pegulingan)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000670')">Plot</button> salami000670 (Compilations, Banana Boat Day O )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000677')">Plot</button> salami000677 (Compilations, First Chelkona Dance)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000686')">Plot</button> salami000686 (Natalie MacMaster, Silverwells)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000704')">Plot</button> salami000704 (Compilations, Zombie Jamboree)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000707')">Plot</button> salami000707 (Compilations, Kilfenora Reels)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000714')">Plot</button> salami000714 (Compilations, Gillie Calum Sword Dance )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000722')">Plot</button> salami000722 (Jacques Brel, Sur La Place)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000725')">Plot</button> salami000725 (Chago Rodrigo, Garrotin)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000732')">Plot</button> salami000732 (Te tes Raides, La chanson du trepasse)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000734')">Plot</button> salami000734 (Alabi na, Linda)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000758')">Plot</button> salami000758 (Compilations, Reels Trip To Durrow Limerick Lass)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000764')">Plot</button> salami000764 (Compilations, Nadigravanje)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000766')">Plot</button> salami000766 (Compilations, She Did You A Favor)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000769')">Plot</button> salami000769 (Compilations, Djeli Mara)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000770')">Plot</button> salami000770 (Compilations, Jump in The Line)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000772')">Plot</button> salami000772 (Compilations, A Toda Cuba le Gusta)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000789')">Plot</button> salami000789 (Compilations, The Drunken Fisherman)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000793')">Plot</button> salami000793 (Compilations, Molimo Song of Devotion to the Fores)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000798')">Plot</button> salami000798 (Compilations, Pedat Tongtong)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000831')">Plot</button> salami000831 (Compilations, Vengo de mi Extremadura)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000840')">Plot</button> salami000840 (Boom Pam, The Souvlak)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000843')">Plot</button> salami000843 (Silly Wizard, Miss Shepherd Sweeney s Buttermilk M)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000846')">Plot</button> salami000846 (Gidon Kremer, Soledad)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000862')">Plot</button> salami000862 (Compilations, Iz Oblaka Rosa Pada)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000868')">Plot</button> salami000868 (Compilations, Al Shammasha)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000877')">Plot</button> salami000877 (Compilations, Hicaz Dolap)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000887')">Plot</button> salami000887 (Georges Brassens, Tonton Nestor la noce de Jean)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000897')">Plot</button> salami000897 (Compilations, Down That Lonely Road)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000907')">Plot</button> salami000907 (Ruben Gonzalez, Cumbanchero)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000908')">Plot</button> salami000908 (Compilations, El Son de Cuba Track 9)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000912')">Plot</button> salami000912 (Les Negresses Vertes, Marcelle Ratafia)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000922')">Plot</button> salami000922 (Carlos Paredes, Canto De Embalar)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000923')">Plot</button> salami000923 (Compilations, Son de Cuba a Puerto Rico)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000924')">Plot</button> salami000924 (Compilations, Flora Perdida)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000960')">Plot</button> salami000960 (Compilations, Al Mouaad)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000965')">Plot</button> salami000965 (Ravi Shankar, Kafi Holi Spring Festival Of Colors)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000968')">Plot</button> salami000968 (Dead Can Dance, As the Bell Rings the Maypole Spins)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000970')">Plot</button> salami000970 (Budapester Klezmer Band, Havah Nagilah)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000985')">Plot</button> salami000985 (Compilations, Manuelita)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001003')">Plot</button> salami001003 (Compilations, Balkany Flowers Original Mix )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001014')">Plot</button> salami001014 (Outback, Dance the Devil Away)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001019')">Plot</button> salami001019 (Compilations, The Real Moustache Vono Box Djs Rem)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001026')">Plot</button> salami001026 (Compilations, Afrah Baladi)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001030')">Plot</button> salami001030 (Compilations, Edurado Rodriguez Cantarela)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001039')">Plot</button> salami001039 (Compilations, Danny Boy)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001055')">Plot</button> salami001055 (Compilations, K adnikini ya)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001058')">Plot</button> salami001058 (Buena Vista Social Club, El Cuarto de Tula)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001061')">Plot</button> salami001061 (Compilations, Mamaita No Quiere)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001072')">Plot</button> salami001072 (Compilations, Drifting Too Far From The Shore)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001087')">Plot</button> salami001087 (Georges Brassens, Rien a jeter)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001092')">Plot</button> salami001092 (Compilations, Give Me the Food)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001097')">Plot</button> salami001097 (Ry Cooder Ali Farka Toure, Bonde)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001109')">Plot</button> salami001109 (Compilations, Opa Cupa)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001117')">Plot</button> salami001117 (Yann Tiersen, La dispute)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001136')">Plot</button> salami001136 (Septeto Haban ero, Un Diciembre Feliz)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001138')">Plot</button> salami001138 (Paco De Lucia, Mi Nin o Curro Rondena )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001141')">Plot</button> salami001141 (Yo Yo Ma, Sur Regresso Al Amor)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001155')">Plot</button> salami001155 (Compilations, We Workers Have Strength)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001179')">Plot</button> salami001179 (Compilations, Papa Montero)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001187')">Plot</button> salami001187 (Compilations, A Las Seis)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001195')">Plot</button> salami001195 (The Derek Trucks Band, Sahib Teri Bandi Maki Mandi)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001196')">Plot</button> salami001196 (Compilations, Lo Loma de Belen)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001198')">Plot</button> salami001198 (Paris Combo, Si J Avais E te )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001208')">Plot</button> salami001208 (Gianmaria Testa, Il meglio di te)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001213')">Plot</button> salami001213 (Compilations, Ya Dorah Shami)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001214')">Plot</button> salami001214 (Sine ad O Connor, O ro Se Do Bheatha Bhaile)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001215')">Plot</button> salami001215 (The Pogues, Boat Train)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001229')">Plot</button> salami001229 (Ashley MacIsaac, Rusty D con STRUCK tion)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001231')">Plot</button> salami001231 (Abyssinia Infinite, Gela)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001234')">Plot</button> salami001234 (Chava Alberstein, Hul iet Hul iet Kinderlech Have A G)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001241')">Plot</button> salami001241 (Celia Cruz, La Bayamesa)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001242')">Plot</button> salami001242 (Compilations, An Buachaill Caol Dubh)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001250')">Plot</button> salami001250 (Burhan Ocal Jamaaladeen Tacuma, Gene Gel)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001254')">Plot</button> salami001254 (Compilations, Kaman Garo Kanhaji)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001255')">Plot</button> salami001255 (Edith Piaf, Le droit d aimer)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001257')">Plot</button> salami001257 (Compilations, The Prince Who Changed into a Cat)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001259')">Plot</button> salami001259 (Compilations, Izpoved)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001260')">Plot</button> salami001260 (Kodo, Kashira)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001264')">Plot</button> salami001264 (Compilations, Mapale)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001267')">Plot</button> salami001267 (Gidon Kremer, Concierto para quinteto)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001269')">Plot</button> salami001269 (Georges Brassens, Quatre vingt quinze pour cent)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001294')">Plot</button> salami001294 (Compilations, Sevilla)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001297')">Plot</button> salami001297 (Compilations, Bat Karane Mujhe Mushkil)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001312')">Plot</button> salami001312 (Compilations, Solo Tu)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001313')">Plot</button> salami001313 (Kronos Quartet, White Man Sleeps I)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001330')">Plot</button> salami001330 (Ibrahim Ferrer, Boqin en e)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001350')">Plot</button> salami001350 (The Klezmorim, Sherele)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001352')">Plot</button> salami001352 (Compilations, Kaman Song Gula Sta de Kilie)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001358')">Plot</button> salami001358 (Manu Chao, Te tromper)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001360')">Plot</button> salami001360 (Yo Yo Ma, Le Grand Tango)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001367')">Plot</button> salami001367 (Lhasa De Sela, De Cara a la Pared)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001369')">Plot</button> salami001369 (Paris Combo, On N a Pas Besoin)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001389')">Plot</button> salami001389 (Compilations, Warsaw is Khelm)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001397')">Plot</button> salami001397 (Afro Cuban All Stars, Al Vaive n De Mi Carreta)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001400')">Plot</button> salami001400 (Compilations, Ei Mori Roujke)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001407')">Plot</button> salami001407 (Compilations, Hicaz Dolap ROM)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001411')">Plot</button> salami001411 (Compilations, Farruca El Albacin )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001418')">Plot</button> salami001418 (Compilations, Bopong I Lotring)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001433')">Plot</button> salami001433 (Edith Piaf, Adieu Mon Coeur)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001437')">Plot</button> salami001437 (Compilations, Hawai ian Chant Mele Pule)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001443')">Plot</button> salami001443 (Diouf, Lii)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001470')">Plot</button> salami001470 (Compilations, Coconut Milk)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001471')">Plot</button> salami001471 (Emad Sayyah, Habibi Lahibi My Darling My Fire )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001472')">Plot</button> salami001472 (Compilations, You re That Certain Someone)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001482')">Plot</button> salami001482 (Compilations, We Believe In Happy Endings)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001483')">Plot</button> salami001483 (Compilations, Paddy Works On The Railroad)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001485')">Plot</button> salami001485 (Compilations, Sekarinotan Gamelan Semar Pegulingan)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001498')">Plot</button> salami001498 (Compilations, C est Pas La Peine Brailler There s)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001499')">Plot</button> salami001499 (Compilations, Zorba the Greek)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001511')">Plot</button> salami001511 (Compilations, Morabaat Saiidi)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001512')">Plot</button> salami001512 (Budapester Klezmer Band, VI Azoy Lebt De Keyzer)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001514')">Plot</button> salami001514 (Compilations, Auschwitz)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001521')">Plot</button> salami001521 (Compilations, Vete a la calle)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001541')">Plot</button> salami001541 (Compilations, 23)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001543')">Plot</button> salami001543 (Compilations, Plamina Stara Planina)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001544')">Plot</button> salami001544 (Astor Piazzolla, Adio s Nonino)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001552')">Plot</button> salami001552 (Compilations, Genderan Gamelan Gong)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001585')">Plot</button> salami001585 (Buster Poindexter, Hot Hot Hot)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001590')">Plot</button> salami001590 (Compilations, Ninnade)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001618')">Plot</button> salami001618 (Compilations, Track 02)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001627')">Plot</button> salami001627 (Compilations, Lady Louden Sandy Cameron Bluebe)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001628')">Plot</button> salami001628 (Compilations, C NW Railroad Blues)</li>
                            </ul>

                        </div>
                </body>
                </html>
