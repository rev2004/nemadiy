/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.imirsel.nema.meandre.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

/**
 * Call Back implementation of {@link org.springframework.jdbc.core.JdbcTemplate} to write every row into a {@link BufferedWriter}
 * @since 0.9
 * @author gzhu1
 */
public class ConsoleRowHandler implements RowCallbackHandler {

    static protected Log logger = LogFactory.getLog(ConsoleRowHandler.class);
    private BufferedWriter writer;

    /**
     * @param writer the writer to set
     */
    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public void processRow(ResultSet rs) throws SQLException {
        try {
            writer.append("[").append(rs.getString("print")).append("]");
            writer.newLine();
            writer.append(rs.getString("ts"));
            writer.newLine();
        } catch (IOException ex) {
            logger.error(ex,ex);
        }

    }
}
