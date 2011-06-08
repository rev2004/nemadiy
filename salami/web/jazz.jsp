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


                </head>

                <body>

                    <table id="h2table">
                        <tr>
                            <td><img src="support/logo.png" width="160"></td>
                            <td><h2>Segmentation Plot Results</h2></td>
                        </tr>
                    </table>

                     <div id="tabs">
                            <ul>
                                <li><a href="index.jsp" title="classical"><span>Summary</span></a></li>
                                <li ><a href="classical.jsp" title="classical"><span>Classical</span></a></li>
                                <li class="shown"><a href="jazz.jsp" title="Jazz"><span>Jazz</span></a></li>
                                <li><a href="live.jsp" title="Live"><span>Live</span></a></li>
                                <li><a href="pop.jsp" title="Pop"><span>Popular</span></a></li>
                                <li><a href="world.jsp" title="World"><span>World</span></a></li>
                            </ul>
                        </div>
                    <br><a name="top"></a>
                        <div id="content">

                            <ul>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000022')">Plot</button> salami000022 (Wynton Marsalis, Love And Broken Hearts)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000024')">Plot</button> salami000024 (Coleman Hawkins, I ll Never Be The Same)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000042')">Plot</button> salami000042 (Johnny Winter, Mojo Boogie)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000043')">Plot</button> salami000043 (Compilations, Tiger Rag)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000044')">Plot</button> salami000044 (Compilations, Samba Triste)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000063')">Plot</button> salami000063 (Pocketdwellers, 26th Moon)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000064')">Plot</button> salami000064 (Joy Denalane, Be Real)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000079')">Plot</button> salami000079 (Charlie Hunter, Someday We ll All Be Free)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000089')">Plot</button> salami000089 (The Derek Trucks Band, So Close So Far Away)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000096')">Plot</button> salami000096 (Wynton Marsalis, Little Birdie)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000100')">Plot</button> salami000100 (Compilations, Rock My Soul)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000122')">Plot</button> salami000122 (Liquid Soul, Cabbage Roll)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000131')">Plot</button> salami000131 (Stan Getz, Manha De Carnival Morning of the Ca)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000132')">Plot</button> salami000132 (Buddy Guy, Watch Yourself)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000139')">Plot</button> salami000139 (Charlie Hunter, Come As You Are)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000142')">Plot</button> salami000142 (Compilations, It s Easy To Remember)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000158')">Plot</button> salami000158 (Paul Butterfield Blues Band, Screamin )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000176')">Plot</button> salami000176 (Albert King Stevie Ray Vaughan, Call It Stormy Monday)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000183')">Plot</button> salami000183 (Soweto Kinch, Elision)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000193')">Plot</button> salami000193 (Compilations, Senga)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000199')">Plot</button> salami000199 (Heather Headley, Rain)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000200')">Plot</button> salami000200 (Compilations, Stop 3)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000209')">Plot</button> salami000209 (Stan Getz Astrud Gilberto, Eu E Voce)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000212')">Plot</button> salami000212 (Billie Holiday, Lover Man)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000213')">Plot</button> salami000213 (Aretha Franklin, Try Matty s)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000214')">Plot</button> salami000214 (Compilations, Soy Califa)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000220')">Plot</button> salami000220 (Compilations, Memories Of You)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000224')">Plot</button> salami000224 (Dizzy Gillespie Charlie Parker, Ooh Shoobee Doobee)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000226')">Plot</button> salami000226 (Compilations, Fools Gold)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000231')">Plot</button> salami000231 (Jamiroquai, Where Do We Go from Here)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000232')">Plot</button> salami000232 (Donald Byrd, Rock And Roll Again)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000237')">Plot</button> salami000237 (Charlie Hunter Leon Parker, The Last Time)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000244')">Plot</button> salami000244 (Louis Armstrong, West End Blues)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000247')">Plot</button> salami000247 (Compilations, Money Honey)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000248')">Plot</button> salami000248 (James Taylor, Lo And Behold)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000252')">Plot</button> salami000252 (Compilations, When The Lights Go Out)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000253')">Plot</button> salami000253 (Horace Silver, Sighin And Cryin )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000265')">Plot</button> salami000265 (Montreal Jubilation Gospel Choir, I Said I Wasn t Gonna Tell Nobody)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000272')">Plot</button> salami000272 (Wynton Marsalis, Find Me)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000281')">Plot</button> salami000281 (Wayne Shorter, Genesis)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000288')">Plot</button> salami000288 (Billie Holiday, God Bless The Child)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000297')">Plot</button> salami000297 (RWC MDB J 2001 M02, 5)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000302')">Plot</button> salami000302 (Kenny Dorham, K)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000306')">Plot</button> salami000306 (Wilson Pickett, You Can t Stand Alone)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000315')">Plot</button> salami000315 (Compilations, Revelations 1st Movement)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000318')">Plot</button> salami000318 (Soweto Kinch, Intro)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000329')">Plot</button> salami000329 (Art Blakey, Politely)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000333')">Plot</button> salami000333 (Compilations, Chicago Flyer)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000340')">Plot</button> salami000340 (Compilations, 8 4 Beat)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000347')">Plot</button> salami000347 (Hank Mobley, Three Coins In The Fountain)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000363')">Plot</button> salami000363 (Kelis, Attention featuring Raphael Saadiq )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000367')">Plot</button> salami000367 (Marsh Dondurma, Klachnikov)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000369')">Plot</button> salami000369 (Compilations, Moody s All Frantic)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000373')">Plot</button> salami000373 (Yehudi Menuhin Stephane Grappelli, These Foolish Things)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000374')">Plot</button> salami000374 (Dawn Tyler Blues Project, Latex)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000378')">Plot</button> salami000378 (Stan Getz J J Johnson, My Funny Valentine)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000386')">Plot</button> salami000386 (The Derek Trucks Band, Afro Blue)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000387')">Plot</button> salami000387 (Charlie Parker, Scrapple From The Apple)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000391')">Plot</button> salami000391 (Brian Barley Trio, Le Pingouin)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000396')">Plot</button> salami000396 (Wes Montgomery, Movin Wes Part II )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000411')">Plot</button> salami000411 (Louis Armstrong, Mahogany Hall Stomp)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000414')">Plot</button> salami000414 (Kelly Joe Phelps, Dock Boggs Country Blues)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000429')">Plot</button> salami000429 (Count Basie Sarah Vaughan, Little Man You ve Had A Busy Day )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000431')">Plot</button> salami000431 (Bessie Smith, St)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000433')">Plot</button> salami000433 (Elvis Presley, Don t ask me why)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000435')">Plot</button> salami000435 (Compilations, Stagger Lee)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000442')">Plot</button> salami000442 (Mahalia Jackson, To Me It s So Wonderful)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000444')">Plot</button> salami000444 (Joss Stone, Bad Habit)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000446')">Plot</button> salami000446 (The Staple Singers, Willl the Circle Be Unbroken)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000447')">Plot</button> salami000447 (Albert Collins, Chatterbox)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000449')">Plot</button> salami000449 (Compilations, Feel So Bad)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000452')">Plot</button> salami000452 (Stan Getz Luiz Bonfa, Um Abraco No Getz A Tribute To Getz)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000456')">Plot</button> salami000456 (Fantasia Barrino, Free Yourself)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000461')">Plot</button> salami000461 (Ornette Coleman, Mob Job)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000465')">Plot</button> salami000465 (Oliver Jones Clark Terry, In My Solitude)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000484')">Plot</button> salami000484 (Compilations, Swamp Road)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000491')">Plot</button> salami000491 (Charlie Hunter, Al Green)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000492')">Plot</button> salami000492 (Horace Silver, The Kicker)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000505')">Plot</button> salami000505 (Grant Green, Blues For Juanita)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000510')">Plot</button> salami000510 (Omar Sosa, Torbelegg)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000511')">Plot</button> salami000511 (Compilations, Congratulation Cigar)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000514')">Plot</button> salami000514 (Dizzy Gillespie Stan Getz Sonny Stitt, Wee Allen s Alley )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000515')">Plot</button> salami000515 (Compilations, Living Easy)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000524')">Plot</button> salami000524 (Son House, John The Revelator)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000528')">Plot</button> salami000528 (Ronnie Earl, I Cried My Eyes Out)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000535')">Plot</button> salami000535 (Charlie Hunter, Rebel Music)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000538')">Plot</button> salami000538 (Duke Ellington, So Far So Good)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000540')">Plot</button> salami000540 (Big Mama Thornton, Looking the World Over)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000557')">Plot</button> salami000557 (RWC MDB J 2001 M01, 7)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000563')">Plot</button> salami000563 (Robert Johnson, Ramblin On My Mind Alternate Take)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000568')">Plot</button> salami000568 (Bud Powell, So Sorry Please)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000571')">Plot</button> salami000571 (James Cotton, Knock On Wood)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000573')">Plot</button> salami000573 (Ella Fitzgerald Louis Armstrong, You Won t Be Satisfied Until You Br)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000601')">Plot</button> salami000601 (Otis Redding, Chain Gang)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000602')">Plot</button> salami000602 (Maceo Parker, Over The Rainbow)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000604')">Plot</button> salami000604 (Soweto Kinch, Intermission Split Decision)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000606')">Plot</button> salami000606 (S Word, Blood On That Rock)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000608')">Plot</button> salami000608 (Compilations, Spooky)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000611')">Plot</button> salami000611 (Art Tatum, Embraceable You)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000613')">Plot</button> salami000613 (Compilations, Jeepster)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000626')">Plot</button> salami000626 (Macy Gray, I ve Committed Murder)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000631')">Plot</button> salami000631 (Compilations, Siete Ocho)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000637')">Plot</button> salami000637 (Compilations, Lop Pow)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000642')">Plot</button> salami000642 (Art Blakey, Sam s Tune)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000649')">Plot</button> salami000649 (Cannonball Adderley, Azule Serape)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000653')">Plot</button> salami000653 (Ethio Jazz, Asmarina My Asmara)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000659')">Plot</button> salami000659 (James Brown, Smokin Drinkin )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000662')">Plot</button> salami000662 (The Staple Singers, Hammer and Nails)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000666')">Plot</button> salami000666 (Lyle Lovett, Since The Last Time)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000681')">Plot</button> salami000681 (Compilations, Bogalousa Strut)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000682')">Plot</button> salami000682 (Compilations, The Jackal)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000689')">Plot</button> salami000689 (Compilations, Cantaloop)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000694')">Plot</button> salami000694 (Compilations, Masterpiece)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000695')">Plot</button> salami000695 (Compilations, Gloomy Sunday)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000718')">Plot</button> salami000718 (Compilations, Five Point Blues)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000721')">Plot</button> salami000721 (Compilations, Panacea)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000723')">Plot</button> salami000723 (Compilations, The Ending to the First Side)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000735')">Plot</button> salami000735 (Dion, Runaround Sue)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000749')">Plot</button> salami000749 (Stan Getz Bill Evans, Night and Day 2)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000759')">Plot</button> salami000759 (Compilations, Way Down Yonder in New Orleans)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000760')">Plot</button> salami000760 (Cassie, What Do U Want)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000761')">Plot</button> salami000761 (Compilations, A gua De Beber)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000762')">Plot</button> salami000762 (Charlie Hunter, Lively Up Yourself)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000765')">Plot</button> salami000765 (Charles Mingus, I ll Remember April)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000771')">Plot</button> salami000771 (Erik Truffaz, Minaret)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000775')">Plot</button> salami000775 (Grant Green, Somewhere In The Night)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000779')">Plot</button> salami000779 (Oliver Jones, Round Midnight)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000780')">Plot</button> salami000780 (Joss Stone, I Had A Dream)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000787')">Plot</button> salami000787 (Oliver Jones, Dumpcake Blues)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000788')">Plot</button> salami000788 (Compilations, Morning Train)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000807')">Plot</button> salami000807 (Compilations, Lonesome Town)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000819')">Plot</button> salami000819 (Compilations, A Street Named Hell)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000820')">Plot</button> salami000820 (Compilations, Maple Rag Leaf)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000821')">Plot</button> salami000821 (Dizzy Gillespie, Cool Breeze)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000822')">Plot</button> salami000822 (Duke Ellington, The Sleeping Lady And The Giant Who)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000825')">Plot</button> salami000825 (Montreal Jubilation Gospel Choir, Glory Train)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000839')">Plot</button> salami000839 (Vaya Con Dios, Don t Cry For Louie)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000841')">Plot</button> salami000841 (Compilations, Back At The Chicken Shack)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000842')">Plot</button> salami000842 (Compilations, Woo Hoo)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000847')">Plot</button> salami000847 (Lyle Lovett, Church)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000852')">Plot</button> salami000852 (Alicia Keys, Someday We ll All Be Free)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000854')">Plot</button> salami000854 (Compilations, Stompin At The Savoy)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000870')">Plot</button> salami000870 (Brownie McGhee, The Last Mile)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000872')">Plot</button> salami000872 (Donald Byrd, Weasil)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000886')">Plot</button> salami000886 (Compilations, Ain t Treating Me Right)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000890')">Plot</button> salami000890 (The Staple Singers, Love Is Plentiful)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000891')">Plot</button> salami000891 (RWC MDB J 2001 M03, 6)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000898')">Plot</button> salami000898 (Compilations, Long Tall Sally)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000914')">Plot</button> salami000914 (Count Basie, Broadway)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000920')">Plot</button> salami000920 (Compilations, Misirlou)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000937')">Plot</button> salami000937 (Charles Mingus, Moods In Mambo)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000938')">Plot</button> salami000938 (RWC MDB J 2001 M01, 12)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000958')">Plot</button> salami000958 (Erik Truffaz, More)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000959')">Plot</button> salami000959 (Compilations, Monk In Wonderland)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000967')">Plot</button> salami000967 (Mary J Blige, Sweet Thing)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000976')">Plot</button> salami000976 (Montreal Jubilation Gospel Choir, Go Tell it on the Mountain)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000993')">Plot</button> salami000993 (Compilations, The Pearls)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001001')">Plot</button> salami001001 (Buddy Guy Junior Wells, Medley)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001005')">Plot</button> salami001005 (Johnny Hodges, Honeysuckle Rose)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001018')">Plot</button> salami001018 (The Staple Singers, Praying Time)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001023')">Plot</button> salami001023 (Marvin Gaye, Trouble Man)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001031')">Plot</button> salami001031 (Compilations, That s All)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001038')">Plot</button> salami001038 (Erykah Badu, Green Eyes)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001043')">Plot</button> salami001043 (John Coltrane, A Love Supreme Part One )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001077')">Plot</button> salami001077 (RWC MDB J 2001 M03, 3)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001085')">Plot</button> salami001085 (Sade, Flow)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001088')">Plot</button> salami001088 (Compilations, Summertime)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001091')">Plot</button> salami001091 (Miles Davis, Joshua)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001093')">Plot</button> salami001093 (Mahalia Jackson, It Don t Cost Very Much)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001106')">Plot</button> salami001106 (Howlin Wolf, Howlin For My Darlin )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001126')">Plot</button> salami001126 (Chucho Valde s, My Funny Valentine)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001131')">Plot</button> salami001131 (Koko Taylor, The Hunter)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001139')">Plot</button> salami001139 (Me Shell Ndege ocello, Interlude 6 Legged Griot Trio Wear)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001145')">Plot</button> salami001145 (The Derek Trucks Band, Baby You re Right feat)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001149')">Plot</button> salami001149 (Buddy Guy, Trouble Blues)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001158')">Plot</button> salami001158 (Oliver Jones Clark Terry, Last Night In Rio)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001160')">Plot</button> salami001160 (Al Green, Let s Stay Together)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001163')">Plot</button> salami001163 (RWC MDB J 2001 M04, 5)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001164')">Plot</button> salami001164 (Wayne Shorter, The Big Push)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001174')">Plot</button> salami001174 (RWC MDB J 2001 M03, 9)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001182')">Plot</button> salami001182 (Miles Davis, Boplicity)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001184')">Plot</button> salami001184 (Muddy Waters, Evil)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001189')">Plot</button> salami001189 (Herbie Hancock, A Tribute To Someone)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001202')">Plot</button> salami001202 (Compilations, Weep)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001204')">Plot</button> salami001204 (King Oliver, Snake Rag)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001205')">Plot</button> salami001205 (Compilations, She Keeps It Up All The Time)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001236')">Plot</button> salami001236 (Compilations, Groove Street)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001268')">Plot</button> salami001268 (Compilations, Church Bells Ring)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001272')">Plot</button> salami001272 (Compilations, Crazeology 4 )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001273')">Plot</button> salami001273 (Marc Ribot Los Cubanos Postizos, Los Teenagers Bailan Changui)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001288')">Plot</button> salami001288 (Compilations, Don t You Just Know It)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001303')">Plot</button> salami001303 (Grant Green, Born To Be Blue)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001316')">Plot</button> salami001316 (Compilations, San Fransisco Bay Blues)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001327')">Plot</button> salami001327 (Compilations, Low Society)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001344')">Plot</button> salami001344 (Miles Davis, All Of You)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001357')">Plot</button> salami001357 (Stevie Ray Vaughan, All Your Love I Miss Loving )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001371')">Plot</button> salami001371 (Erik Truffaz, Betty)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001375')">Plot</button> salami001375 (Mississippi Heat, Straight From the Heart)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001379')">Plot</button> salami001379 (Thelonious Monk, Monk s Mood)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001383')">Plot</button> salami001383 (George Benson, Low Down Dirty)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001395')">Plot</button> salami001395 (Louis Armstrong Duke Ellington, The Mooche)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001414')">Plot</button> salami001414 (Soulive, First Street)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001428')">Plot</button> salami001428 (The Meters, I Need More Time)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001445')">Plot</button> salami001445 (Compilations, Left Me With A Broken Heart)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001446')">Plot</button> salami001446 (Stan Getz Joa o Gilberto, Desafinado)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001452')">Plot</button> salami001452 (Compilations, Omega)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001453')">Plot</button> salami001453 (Funkadelic, Smokey)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001457')">Plot</button> salami001457 (Lester Young, I Can t Get Started)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001459')">Plot</button> salami001459 (Ronnie Earl, Szeren)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001462')">Plot</button> salami001462 (Brownie McGhee Sonny Terry, C mon If You re Comin )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001478')">Plot</button> salami001478 (Mahalia Jackson, Didn t It Rain)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001481')">Plot</button> salami001481 (Compilations, After Hours Blues)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001487')">Plot</button> salami001487 (Stan Getz Charlie Byrd, Samba Dees Days)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001488')">Plot</button> salami001488 (Stevie Wonder, You Haven t Done Nothin)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001522')">Plot</button> salami001522 (Jean Luc Ponty Stephane Grappelli, Swing Guitars)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001526')">Plot</button> salami001526 (Compilations, Comanche)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001537')">Plot</button> salami001537 (Compilations, Me and My Crazy Self)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001545')">Plot</button> salami001545 (Sonny Terry, So Tough With Me)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001557')">Plot</button> salami001557 (Oliver Jones, My Funny Valentine)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001574')">Plot</button> salami001574 (RWC MDB J 2001 M04, 4)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001575')">Plot</button> salami001575 (Compilations, Harlem Bound)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001578')">Plot</button> salami001578 (Eric Clapton, Hey Hey)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001579')">Plot</button> salami001579 (Charlie Hunter, Shango)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001592')">Plot</button> salami001592 (Compilations, The Sidewinder)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001594')">Plot</button> salami001594 (Compilations, Just Like A Bird Without A Feather)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001597')">Plot</button> salami001597 (Lightnin Hopkins, Honey Babe)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001622')">Plot</button> salami001622 (The Everly Brothers, Til I Kissed You)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001624')">Plot</button> salami001624 (Stray Cats, You Don t Believe Me)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001635')">Plot</button> salami001635 (Sonny Rollins, I Wish I Knew)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001636')">Plot</button> salami001636 (Joe Cocker, High Time We Went)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001641')">Plot</button> salami001641 (B B King, Dangerous Mood With Joe Cocker )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001642')">Plot</button> salami001642 (Donald Byrd, Mr)</li>
                          </ul>

                        </div>
                </body>
                </html>
