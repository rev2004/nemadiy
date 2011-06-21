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
                                <li class="shown"><a href="pop.jsp" title="Pop"><span>Popular</span></a></li>
                                <li><a href="world.jsp" title="World"><span>World</span></a></li>
                                <li><a href="help.jsp" title="help"><span>Help</span></a></li>
                            </ul>
                        </div>
                    <br><a name="top"></a>
                        <div id="content">

                            <ul><li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000008')">Plot</button> salami000008 (RWC MDB P 2001 M03, 7)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000014')">Plot</button> salami000014 (White Zombie, More Human Than Human)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000017')">Plot</button> salami000017 (Red Hot Chili Peppers, Sex Rap)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000019')">Plot</button> salami000019 (Beatles, CD2 - 10 - Savoy Truffle)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000025')">Plot</button> salami000025 (Jewel, Love Me Just Leave Me Alone)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000028')">Plot</button> salami000028 (Sine ad O Connor, Jah Nuh Dead)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000029')">Plot</button> salami000029 (Tori Amos, Honey Live )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000033')">Plot</button> salami000033 (Compilations, I m Sticking With You)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000035')">Plot</button> salami000035 (Kaki King, The Footsteps Die Out Forever)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000038')">Plot</button> salami000038 (Monty Python, Never Be Rude To An Arab)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000056')">Plot</button> salami000056 (Sarah Harmer, You Were Here)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000068')">Plot</button> salami000068 (Gnarls Barkley, Neighbors)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000083')">Plot</button> salami000083 (Ladyhawk, My Old Jacknife)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000086')">Plot</button> salami000086 (Matisyahu, Refuge)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000090')">Plot</button> salami000090 (Kid Koala, Annie s Parlor)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000091')">Plot</button> salami000091 (Flamin Groovies, Shakin All Over)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000094')">Plot</button> salami000094 (Rickie Lee Jones, Rodeo Girl)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000127')">Plot</button> salami000127 (James Galway Henry Mancini, Pink Panther)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000144')">Plot</button> salami000144 (The Dillinger Escape Plan, Mouth Of Ghosts)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000148')">Plot</button> salami000148 (Henry Mancini, Dreamsville)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000166')">Plot</button> salami000166 (Yeah Yeah Yeahs, No No No)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000173')">Plot</button> salami000173 (RWC MDB P 2001 M03, 12)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000182')">Plot</button> salami000182 (RWC MDB P 2001 M06, 8)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000186')">Plot</button> salami000186 (Sixtoo, Chainsaw Breakfast)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000203')">Plot</button> salami000203 (Compilations, Piazza New York Catcher)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000208')">Plot</button> salami000208 (Bonnie Raitt, I Can t Make You Love Me)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000221')">Plot</button> salami000221 (Leonard Cohen, Hey That s No Way To Say Goodbye)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000233')">Plot</button> salami000233 (Beatles, 11 - Mean Mr Mustard)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000255')">Plot</button> salami000255 (RWC MDB P 2001 M02, 10)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000263')">Plot</button> salami000263 (Compilations, The Assuming Song)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000269')">Plot</button> salami000269 (Queen, ImGoingSlightly)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000299')">Plot</button> salami000299 (The Arrogant Worms, A Night on Dildo)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000301')">Plot</button> salami000301 (Compilations, Crazy Train)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000310')">Plot</button> salami000310 (Johnny Cash, It s Just About Time 1)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000331')">Plot</button> salami000331 (Beatles, CD1 - 07 - While My Guitar Gently Weeps)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000332')">Plot</button> salami000332 (Compilations, Bring)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000341')">Plot</button> salami000341 (Apocalyptica, Harvester of Sorrow)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000346')">Plot</button> salami000346 (Lynyrd Skynyrd, That Smell)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000355')">Plot</button> salami000355 (Bob Dylan, Country Pie)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000370')">Plot</button> salami000370 (Counting Crows, On A Tuesday In Amsterdam Long Ago)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000380')">Plot</button> salami000380 (The Arrogant Worms, Horizon)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000395')">Plot</button> salami000395 (Pantera, Use My Third Arm)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000402')">Plot</button> salami000402 (RWC MDB P 2001 M06, 7)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000406')">Plot</button> salami000406 (Johnny Winter, Prodigal Man)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000412')">Plot</button> salami000412 (Queen, WeAreTheChampions)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000424')">Plot</button> salami000424 (RWC MDB P 2001 M04, 5)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000437')">Plot</button> salami000437 (Beatles, 09 - Words of Love)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000440')">Plot</button> salami000440 (RWC MDB P 2001 M01, 13)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000441')">Plot</button> salami000441 (Compilations, The Bigger The Cushion)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000472')">Plot</button> salami000472 (RWC MDB P 2001 M07, 7)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000487')">Plot</button> salami000487 (A Tribe Called Quest, Vivrant Thing)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000493')">Plot</button> salami000493 (Compilations, Bodies)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000496')">Plot</button> salami000496 (Compilations, Business Time)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000500')">Plot</button> salami000500 (RWC MDB P 2001 M01, 14)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000507')">Plot</button> salami000507 (Compilations, Modern Love)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000513')">Plot</button> salami000513 (Compilations, Sweet Poison)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000523')">Plot</button> salami000523 (RWC MDB P 2001 M01, 10)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000531')">Plot</button> salami000531 (Gnarls Barkley, The Boogie Monster)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000536')">Plot</button> salami000536 (Metallica, Ride The Lightning)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000544')">Plot</button> salami000544 (J J Cale, Sensitive Kind)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000549')">Plot</button> salami000549 (Cream, Take It Back)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000556')">Plot</button> salami000556 (Sine ad O Connor, Scarlett Ribbons)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000559')">Plot</button> salami000559 (Compilations, Sally And Jack)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000570')">Plot</button> salami000570 (K naan, The Dusty Foot Philosopher)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000577')">Plot</button> salami000577 (Nellie McKay, Suitcase Song)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000579')">Plot</button> salami000579 (Beatles, 07 - Michelle)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000600')">Plot</button> salami000600 (Monty Python, I m So Worried)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000607')">Plot</button> salami000607 (Santana, Victory Is Won)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000622')">Plot</button> salami000622 (Priya Thomas, Stillborn)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000627')">Plot</button> salami000627 (Emmylou Harris, Calling My Children Home)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000628')">Plot</button> salami000628 (Compilations, Johnny Too Bad)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000632')">Plot</button> salami000632 (Herb Alpert The Tijuana Brass, South of the Border)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000633')">Plot</button> salami000633 (Jessy Moss, Chapters)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000639')">Plot</button> salami000639 (Compilations, Walk This Land)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000646')">Plot</button> salami000646 (Compilations, If The Milk Turns Sour)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000664')">Plot</button> salami000664 (Monty Python, Bruces Philosophers Song)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000671')">Plot</button> salami000671 (Marilyn Manson, Obsequey The Death Of Art )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000676')">Plot</button> salami000676 (Compilations, Crane White Lightning)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000683')">Plot</button> salami000683 (RWC MDB P 2001 M07, 1)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000684')">Plot</button> salami000684 (Les Vulgaires Machins, Dieu se pique)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000687')">Plot</button> salami000687 (Compilations, Go Slow)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000688')">Plot</button> salami000688 (Madonna, 02 madonna 4 minutes)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000691')">Plot</button> salami000691 (Cowboy Junkies, My Only Guarantee)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000697')">Plot</button> salami000697 (Amy Millan, Ruby II)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000698')">Plot</button> salami000698 (Compilations, GET OUTTA MY DREAMS GET INTO MY CAR)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000701')">Plot</button> salami000701 (Martha Wainwright, When the Day Is Short)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000726')">Plot</button> salami000726 (Neil Young, The Loner)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000729')">Plot</button> salami000729 (Monty Python, Henry Kissinger)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000738')">Plot</button> salami000738 (Gillian Welch, Only One And Only)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000739')">Plot</button> salami000739 (Compilations, Synergy)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000740')">Plot</button> salami000740 (Chicago, Listen)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000744')">Plot</button> salami000744 (Frank Zappa, Little Umbrellas)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000746')">Plot</button> salami000746 (RWC MDB P 2001 M01, 3)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000747')">Plot</button> salami000747 (Cypress Hill, Throw Your Set In The Air)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000754')">Plot</button> salami000754 (RWC MDB P 2001 M02, 12)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000778')">Plot</button> salami000778 (Radio Free Vestibule, I Don t Want To Go To Toronto)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000786')">Plot</button> salami000786 (Compilations, Tear in my Beer)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000794')">Plot</button> salami000794 (Natalie Merchant, The Living)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000795')">Plot</button> salami000795 (Beatles, 08 - Within You Without You)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000796')">Plot</button> salami000796 (Queen, NowImHere)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000797')">Plot</button> salami000797 (Compilations, Big Town)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000799')">Plot</button> salami000799 (RWC MDB P 2001 M04, 12)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000813')">Plot</button> salami000813 (Compilations, Japanese Single)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000827')">Plot</button> salami000827 (Tiken Jah Fakoly, Nationalite )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000828')">Plot</button> salami000828 (Compilations, James Bond Theme)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000836')">Plot</button> salami000836 (Ani DiFranco, Providence)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000838')">Plot</button> salami000838 (Tom Waits, Gospel Train Orchestral )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000844')">Plot</button> salami000844 (Compilations, Fort Augustus)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000853')">Plot</button> salami000853 (Bob Marley, Ride Natty Ride)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000863')">Plot</button> salami000863 (Compilations, Young Gifted and Black)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000873')">Plot</button> salami000873 (Compilations, Too Much Fun)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000879')">Plot</button> salami000879 (Burning Spear, Should I)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000892')">Plot</button> salami000892 (Monty Python, All Things Dull And Ugly)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000895')">Plot</button> salami000895 (Eminem, It s Ok)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000901')">Plot</button> salami000901 (Compilations, I Wanna Rock)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000910')">Plot</button> salami000910 (Peter Tosh, Johnny B)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000915')">Plot</button> salami000915 (The Zoobombs, o )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000925')">Plot</button> salami000925 (The Black Crowes, Sometimes Salvation)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000929')">Plot</button> salami000929 (RWC MDB P 2001 M03, 4)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000934')">Plot</button> salami000934 (Compilations, Sleep)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000941')">Plot</button> salami000941 (The Von Bondies, C mon C mon)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000942')">Plot</button> salami000942 (RWC MDB P 2001 M06, 4)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000948')">Plot</button> salami000948 (RWC MDB P 2001 M01, 6)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000949')">Plot</button> salami000949 (Compilations, Run Rabbit Run)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000955')">Plot</button> salami000955 (Hole, Berry)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000957')">Plot</button> salami000957 (Santana, Eternal Caravan Of Reincarnation)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000961')">Plot</button> salami000961 (Sugarcubes, Coldsweat Remix )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000974')">Plot</button> salami000974 (John McLaughlin Paco de Lucia Al Di Me, Frevo Rasgado)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000986')">Plot</button> salami000986 (Shakira, Ready For The Good Times)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000996')">Plot</button> salami000996 (Natasha Bedingfield, How Do You Do)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000997')">Plot</button> salami000997 (RWC MDB P 2001 M03, 1)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami000998')">Plot</button> salami000998 (Compilations, My Conversation)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001000')">Plot</button> salami001000 (The Pixies, Bailey s Walk)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001006')">Plot</button> salami001006 (Derek and the Dominos, Anyday)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001011')">Plot</button> salami001011 (Compilations, Two Sevens Clash)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001017')">Plot</button> salami001017 (Joan Osborne, One Of Us)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001022')">Plot</button> salami001022 (RWC MDB P 2001 M07, 10)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001028')">Plot</button> salami001028 (Compilations, Since She Started To Ride)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001033')">Plot</button> salami001033 (Compilations, Strings Outro)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001034')">Plot</button> salami001034 (Les Cowboys Fringants, Les e toiles filantes)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001036')">Plot</button> salami001036 (Pearl Jam, Smile)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001045')">Plot</button> salami001045 (Christina Aguilera, Stripped Part 2)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001046')">Plot</button> salami001046 (The Rolling Stones, Factory Girl)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001065')">Plot</button> salami001065 (Compilations, Hidden Track )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001075')">Plot</button> salami001075 (Compilations, She Said No)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001094')">Plot</button> salami001094 (Justin Timberlake, 13 justin timberlake pose feat snoo)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001098')">Plot</button> salami001098 (Janis Joplin, Hesitation Blues)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001105')">Plot</button> salami001105 (Crosby Stills Nash Young, Pre Road Downs)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001108')">Plot</button> salami001108 (The Fugees, Fu Gee La)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001118')">Plot</button> salami001118 (JEAN LECLERC, COWBOY GROOVE)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001137')">Plot</button> salami001137 (Compilations, The Wind)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001140')">Plot</button> salami001140 (Lisa Germano, Singing To The Birds)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001148')">Plot</button> salami001148 (R E M, Imitation Of Life)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001150')">Plot</button> salami001150 (Nirvana, Plateau)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001151')">Plot</button> salami001151 (Monty Python, Always Look On The Bright Side Of Li)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001152')">Plot</button> salami001152 (RWC MDB P 2001 M05, 1)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001167')">Plot</button> salami001167 (Paris Hilton, Stars Are Blind)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001172')">Plot</button> salami001172 (Bonobo, Otter s Pool Super Numeri)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001175')">Plot</button> salami001175 (RWC MDB P 2001 M05, 6)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001177')">Plot</button> salami001177 (The Chemical Brothers, Piku)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001180')">Plot</button> salami001180 (Guns N Roses, Estranged)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001186')">Plot</button> salami001186 (Beatles, 10 - You Really Got A Hold On Me)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001192')">Plot</button> salami001192 (The Arrogant Worms, I Ran Away)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001193')">Plot</button> salami001193 (Natalie Imbruglia, Goodbye)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001197')">Plot</button> salami001197 (Compilations, Hands of Death Burn Baby Burn )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001203')">Plot</button> salami001203 (Compilations, Lips Stained Red with Wine)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001232')">Plot</button> salami001232 (Bon Jovi, I d Die For You)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001235')">Plot</button> salami001235 (The Beatles, Hold Me Tight)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001243')">Plot</button> salami001243 (Compilations, Girl in a Box)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001247')">Plot</button> salami001247 (Compilations, Hol a Fresh)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001262')">Plot</button> salami001262 (Lords of Acid, Kiss Eternal)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001275')">Plot</button> salami001275 (Michael Jackson, who is it)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001298')">Plot</button> salami001298 (Carole King, You ve Got A Friend)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001306')">Plot</button> salami001306 (RWC MDB P 2001 M04, 3)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001314')">Plot</button> salami001314 (Compilations, NASTY)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001317')">Plot</button> salami001317 (Michael Jackson, she s out of my life)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001319')">Plot</button> salami001319 (Rage Against The Machine, New Millenium Homes)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001323')">Plot</button> salami001323 (Evanescence, My Immortal)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001324')">Plot</button> salami001324 (The Arrogant Worms, Me Like Hockey)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001332')">Plot</button> salami001332 (The Arrogant Worms, Boy Band)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001333')">Plot</button> salami001333 (Radiohead, Blow Out)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001334')">Plot</button> salami001334 (Kristin Hersh, Cartoons)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001336')">Plot</button> salami001336 (Compilations, BEAT IT)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001339')">Plot</button> salami001339 (Compilations, Go Go Not Cry Cry)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001347')">Plot</button> salami001347 (Pink, Tonight s The Night)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001363')">Plot</button> salami001363 (Compilations, Passenger 24)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001364')">Plot</button> salami001364 (Neil Young, The Believer)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001373')">Plot</button> salami001373 (The Cure, How Beautiful You Are)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001394')">Plot</button> salami001394 (RWC MDB P 2001 M02, 4)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001396')">Plot</button> salami001396 (Neil Young, The Needle And The Damage Done)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001410')">Plot</button> salami001410 (Timbaland, I Got Luv 4 Ya)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001419')">Plot</button> salami001419 (Jewel, Stand)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001432')">Plot</button> salami001432 (Neil Young, Safeway Cart)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001434')">Plot</button> salami001434 (Kinnie Starr, Alright)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001438')">Plot</button> salami001438 (GZA, I Gotcha Back)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001449')">Plot</button> salami001449 (Michael Jackson, beat it)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001467')">Plot</button> salami001467 (Norah Jones, Broken)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001469')">Plot</button> salami001469 (Great Lake Swimmers, Passenger Song)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001476')">Plot</button> salami001476 (Foo Fighters, Virginia Moon)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001494')">Plot</button> salami001494 (RWC MDB P 2001 M04, 11)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001497')">Plot</button> salami001497 (RWC MDB P 2001 M05, 10)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001502')">Plot</button> salami001502 (Pink Floyd, Free Four)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001518')">Plot</button> salami001518 (Compilations, Bogle)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001524')">Plot</button> salami001524 (Bunny Rabbit, St Guillen)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001533')">Plot</button> salami001533 (Blue Rodeo, You re Everywhere)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001534')">Plot</button> salami001534 (Lesbians On Ecstasy, Superdyke Live )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001538')">Plot</button> salami001538 (Los Lobos, Saint Behind The Glass)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001542')">Plot</button> salami001542 (Monty Python, I ve Got Two Legs)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001559')">Plot</button> salami001559 (Don Ross, Loaded Leather Moonroof)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001571')">Plot</button> salami001571 (Tricky, Feed Me)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001573')">Plot</button> salami001573 (Van Morrison, It s All Over Now Baby Blue)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001587')">Plot</button> salami001587 (Jessy Moss, Alarm Remix )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001588')">Plot</button> salami001588 (RWC MDB P 2001 M06, 9)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001607')">Plot</button> salami001607 (Ten Years After, Summertime Into Shantung Cabbage )</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001611')">Plot</button> salami001611 (RWC MDB P 2001 M04, 4)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001614')">Plot</button> salami001614 (Compilations, Mama s Last Stand)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001619')">Plot</button> salami001619 (RWC MDB P 2001 M05, 8)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001620')">Plot</button> salami001620 (Nadja, Now I Am Become Death the Destroyer)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001621')">Plot</button> salami001621 (Jimi Hendrix, Moon Turn The Tides)</li>
<li><button onclick="window.open('DemoJune/plot.jsp?plot=salami001623')">Plot</button> salami001623 (Compilations, Glow Worm Cha Cha Cha)</li>                            </ul>

                        </div>
                </body>
                </html>
