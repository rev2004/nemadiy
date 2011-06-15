

<%-- 
    Document   : help
    Created on : Jun 9, 2011, 2:42:57 PM
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
                <li ><a href="index.jsp" title="summary"><span>Summary</span></a></li>
                <li><a href="comparison.jsp" title="comparison"><span>Comparison</span></a></li>
                <li><a href="classical.jsp" title="classical"><span>Classical</span></a></li>
                <li><a href="jazz.jsp" title="Jazz"><span>Jazz</span></a></li>
                <li><a href="live.jsp" title="Live"><span>Live</span></a></li>
                <li><a href="pop.jsp" title="Pop"><span>Popular</span></a></li>
                <li><a href="world.jsp" title="World"><span>World</span></a></li>
                <li class="shown"><a href="help.jsp" title="help"><span>Help</span></a></li>
            </ul>
        </div>

        <br/><a name="top"></a>
        <div id="content">
            <h3>Help</h3>
            <h4 >Using the SALAMI Player</h4>
            Shown below is an image of the SALAMI player. In the bottom of the
            plot window is a display of the structural annotations of the entire
            piece of music. A highlighted region shows what portion of the piece
            is being displayed in the zoomed-view. By clicking and dragging the
            highlighted region, the zoomed-view can be scrolled along the piece's
            duration. Clicking in the lower portion outside of the highlighted
            region brings up a cross-hair cursor. Clicking and holding allows the
            highlighted region to be redrawn to any desired size to adjust the
            level of zooming in the zoomed view. In the zoomed-view, clicking on
            any labeled rectangle (section annotation) will play this portion of
            audio, and loop it.

            <img src="img/player_help.png" width="800px"/>
            <br/>
            <br/>
            <h4> Interpreting Self-Similarity Maps</h4>
            Displayed below is a self-similarity map of chroma features.
            Self-similarity maps measure the similarity of features from each
            point in time of the music audio to every other point of time. The
            features used here are chroma vectors, which measure the amount of
            energy present in each of the 12 western-scale musical notes,
            localized in a roughly one second time slice. Shades of blue represent
            little similarity where as shades of red represent significant
            similarity (to dark red which is full similarity).
            <div>
                <img src="img/cs_help.png" width="800px"/>
            </div>
            The main diagonal (starting from lower left going to upper right) is
            the similarity of each chroma time-slice with itself. Naturally, this
            represents perfect similarity, and as such is the most prominent
            (darkest red) part of the image. In essence, this main diagonal
            represents the progression of the piece through time.
            The key part of the self-similarity map to inspect are the off
            diagonal elements. Prominent blocks and off-diagonals parallel to the
            main diagonal indicate the strong possibility of repeating sections.
            In this example, the most prominent example is the "C" section. Off
            the main diagonal, we notice prominent parallel diagonals (highlighted
            in white). When we project these off-diagonals vertically and
            horizontally to the main diagonal, we can infer there is a section of
            the piece that repeats. In contrast, the "A" block has no strong
            similarities to other portions of the piece (dominated by blue above
            and to the right of the main diagonal). The "B" block shows some
            similarities off the diagonal, but they are not complete. It is
            therefore possible that the B section is slightly varied, and
            therefore the second occurrence is labeled "B'". The final structure
            of this piece is then inferred to be ABCDB'C.

        </div>
    </body>
</html>
