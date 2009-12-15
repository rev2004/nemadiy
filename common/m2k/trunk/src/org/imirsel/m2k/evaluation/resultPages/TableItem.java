/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation.resultPages;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author kriswest
 */
public class TableItem extends PageItem{

    protected String[] colNames;
    protected List<String[]> dataRows;

    public TableItem(String name, String caption, String[] colNames,
                     List<String[]> dataRows){
        super(name,caption);
        this.colNames = colNames;
        this.dataRows = dataRows;
    }

    /**
     * @return the colNames
     */
    public String[] getColNames(){
        return colNames;
    }

    /**
     * @param colNames the colNames to set
     */
    public void setColNames(String[] colNames){
        this.colNames = colNames;
    }

    /**
     * @return the dataRows
     */
    public List<String[]> getDataRows(){
        return dataRows;
    }

    /**
     * @param dataRows the dataRows to set
     */
    public void setDataRows(List<String[]> dataRows){
        this.dataRows = dataRows;
    }

    @Override
    public String getHeadData(){
        String out = "<script>\n";
        out += "\t" + getName() + " = {\n";
        out += "\t\ttable: [\n";
        for (Iterator<String[]> it = dataRows.iterator(); it.hasNext();){
            String[] rowVals = it.next();
            out +="\t\t\t{ ";
            for (int c = 0; c < colNames.length; c++){
                if(c > 0){
                    out += ", ";
                }
                out += "\t\t{ \"" + colNames[c] + "\":\"" + rowVals[c] + "\"";
            }
            out += " }";
            if (it.hasNext()){
                out +=",";
            }
            out +="\n";
        }
        out += "\t]\n";
        out += "}\n\n";

        out += "YAHOO.util.Event.addListener(window, \"load\", function() {\n" +
                "\tYAHOO.example.Basic = function() {\n" +
                "\t\tvar myColumnDefs = [";

        for (int i = 0; i < colNames.length; i++){
            if(i>0){
                out += ",\n";
            }else{
                out += "\n";
            }
            out += "\t\t\t{key:\"" + colNames[i] + "\", sortable:true, resizeable:true }";
        }
        out += "\t\t];\n\n";
        out += "\t\tvar myDataSource = new YAHOO.util.DataSource(" + getName() + ".table);\n";
        out += "\t\tmyDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;\n";
        out += "\t\tmyDataSource.responseSchema = {\n";
        out += "\t\t\tfields: [";
        for (int i = 0; i < colNames.length; i++){
            if(i>0){
                out += ",";
            }
            out += "\"" + colNames[i] +"\"";
        }
        out += "]\n";
        out += "\t\t};\n\n";
        out += "\t\tvar myDataTable = new YAHOO.widget.DataTable(\"" + getName() + "\"," +
                "myColumnDefs, myDataSource, {caption:\"" + getCaption() + "\"});\n\n";
        out += "\t\treturn {\n" +
                "\t\t\toDS: myDataSource,\n" +
                "\t\t\toDT: myDataTable\n" +
                "\t\t};\n";
        out += "\t}();\n";
        out += "});\n";
        out +="</script>\n\n";
        return out;
    }

    @Override
    public String getBodyData(boolean topLink){
        String out = "<a name=\"" + getName() + "\"></a>\n" +
                "\t<div id=\"" + getName() + "\"></div>\n";
        if (topLink){
            out += "<span class=\"toplink\"><a href=\"#top\">[top]</a></span>\n";
        }
	out += "<br><br>\n";
        return out;
    }


}
