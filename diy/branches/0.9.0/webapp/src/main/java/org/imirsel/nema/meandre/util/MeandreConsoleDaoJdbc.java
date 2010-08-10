package org.imirsel.nema.meandre.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.model.Job;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class MeandreConsoleDaoJdbc implements MeandreConsoleDao {

	static protected Log logger = LogFactory.getLog(MeandreConsoleDao.class);
	private SimpleJdbcTemplate simpleJdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}

	@Override
	public void clearConsole(Job job) {
//		final String deleteSql="delete from meandre_job_console where job_ID= ?";
//		int count=this.simpleJdbcTemplate.update(deleteSql, job.getExecutionInstanceId());
//		logger.info("Console entries of job ("+job.getName()+") has been deleted. Total count:"+count);
	}

	@Override
	public List<ConsoleEntry> getEntry(final Job job) {
		final String sqlGetList = "select print,ts from meandre_job_console where job_ID = ?";
		RowMapper<ConsoleEntry> mapper = new RowMapper<ConsoleEntry>() {
			public ConsoleEntry mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				ConsoleEntry entry = new ConsoleEntry();
				entry.setLine(rs.getString("print"));
				entry.setTimeStamp(rs.getTimestamp("ts"));
				return entry;
			}
		};

		return this.simpleJdbcTemplate.query(sqlGetList, mapper,job.getExecutionInstanceId());
	}

	@Override
	public List<ConsoleEntry> getEntry(Job job, int maxCount) {
		List<ConsoleEntry> list = getEntry(job);
		if ((list != null) && (list.size() > maxCount)) {
			return list.subList(list.size() - maxCount, list.size() - 1);
		} else
			return list;
	}

}
