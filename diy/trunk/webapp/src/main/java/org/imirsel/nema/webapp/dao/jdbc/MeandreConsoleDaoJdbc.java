package org.imirsel.nema.webapp.dao.jdbc;

import org.imirsel.nema.webapp.dao.MeandreConsoleDao;
import org.imirsel.nema.webapp.model.ConsoleEntry;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.model.Job;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * Implementation of {@link MeandreConsoleDao} with Spring JDBC template
 * @author gzhu1
 */
public class MeandreConsoleDaoJdbc implements MeandreConsoleDao {

    static protected Log logger = LogFactory.getLog(MeandreConsoleDao.class);
    private SimpleJdbcTemplate simpleJdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void clearConsole(Job job) {
//		final String deleteSql="delete from meandre_job_console where job_ID= ?";
//		int count=this.simpleJdbcTemplate.update(deleteSql, job.getExecutionInstanceId());
//		logger.info("Console entries of job ("+job.getName()+") has been deleted. Total count:"+count);
    }
    
    
     final String sqlGetList = "select print,ts from meandre_job_console where job_ID = ?";
    /**
     * {@inheritDoc }
     */
    @Override
    public List<ConsoleEntry> getEntry(final Job job) {
       
        RowMapper<ConsoleEntry> mapper = new RowMapper<ConsoleEntry>() {

            public ConsoleEntry mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                ConsoleEntry entry = new ConsoleEntry();
                entry.setLine(rs.getString("print"));
                entry.setTimeStamp(rs.getTimestamp("ts"));
                return entry;
            }
        };

        return this.simpleJdbcTemplate.query(sqlGetList, mapper, job.getExecutionInstanceId());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<ConsoleEntry> getEntry(Job job, int maxCount) {
        List<ConsoleEntry> list = getEntry(job);
        if ((list != null) && (list.size() > maxCount)) {
            return list.subList(list.size() - maxCount, list.size() - 1);
        } else {
            return list;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void handleEntry(Job job, RowCallbackHandler handler) {
        
        this.simpleJdbcTemplate.getJdbcOperations().query(
                sqlGetList, handler,job.getExecutionInstanceId());
       

    }
    
    
}
