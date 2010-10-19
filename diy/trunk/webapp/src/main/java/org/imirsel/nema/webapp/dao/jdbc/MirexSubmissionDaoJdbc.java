package org.imirsel.nema.webapp.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.imirsel.nema.dao.ExternalMirexSubmissionDao;
import org.imirsel.nema.model.MirexSubmission;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * Implementation of Spring JDBC template for Mirex 2010 submission record DAO
 * @author gzhu1
 *
 */
public class MirexSubmissionDaoJdbc implements ExternalMirexSubmissionDao{

	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	
	public void setDataSource(DataSource dataSource){
		this.simpleJdbcTemplate=new SimpleJdbcTemplate(dataSource);
	}
	
	
	
	public List<MirexSubmission> getAllSubmissions() {
		final String sqlGetAll="select sub_ID,sub_Hashcode,sub_Name from mirex_Submissions" ;
		RowMapper<MirexSubmission> mapper = new RowMapper<MirexSubmission>() {
			public MirexSubmission mapRow(ResultSet rs, int rowNum) throws SQLException {
			MirexSubmission mirexSubmission=new MirexSubmission();
			mirexSubmission.setId(rs.getLong("sub_ID"));
			mirexSubmission.setHashcode(rs.getString("sub_Hashcode"));
			mirexSubmission.setName(rs.getString("sub_Name"));
			return mirexSubmission;
			}
			};
			
			return this.simpleJdbcTemplate.query(sqlGetAll, mapper);


	}

	/**
	 * Not implemented, return null
	 */
	public MirexSubmission getSubmissionByHashcode(String hashcode) {
		throw new java.lang.UnsupportedOperationException("Method not supported: getSubmissionHashcode");
	}

	/**
	 * Not implemented, return null
	 */
	public MirexSubmission getSubmissionById(long id) {
		throw new java.lang.UnsupportedOperationException("Method not supported: getSubmissionById");
	}
	
	

}
