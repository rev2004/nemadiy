package org.imirsel.nema.webapp.webflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.Constants;
import org.imirsel.nema.dao.GenericDao;
import org.imirsel.nema.dao.MirexSubmissionDao;
import org.imirsel.nema.model.Contributor;
import org.imirsel.nema.model.MirexNote;
import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.MirexTask;
import org.imirsel.nema.model.Role;
import org.imirsel.nema.model.User;
import org.imirsel.nema.model.MirexNote.NoteType;
import org.imirsel.nema.model.MirexSubmission.SubmissionStatus;
import org.imirsel.nema.service.UserManager;
import org.imirsel.nema.util.StringUtil;
import org.imirsel.nema.webapp.service.MirexContributorDictionary;
import org.imirsel.nema.webapp.service.MirexTaskDictionary;
import org.springframework.webflow.core.collection.ParameterMap;



public class MirexSubmissionServiceImpl {

	static private Log logger = LogFactory
			.getLog(MirexSubmissionServiceImpl.class);

	static public List<SubmissionStatus> statusList() {
		List<SubmissionStatus> list = Arrays.asList(SubmissionStatus.values());
		logger.trace("set status list" + list);
		return list;
	}

	private MirexTaskDictionary mirexTaskDictionary;
	private MirexContributorDictionary mirexContributorDictionary;
	private MirexSubmissionDao mirexSubmissionDao;
	private UserManager userManager;
	private GenericDao<MirexNote,Long> mirexNoteDao;
	
	public void setMirexTaskDictionary(MirexTaskDictionary mirexTaskDictionary) {
		this.mirexTaskDictionary = mirexTaskDictionary;
	}

	public void setMirexContributorDictionary(
			MirexContributorDictionary mirexContributorDictionary) {
		this.mirexContributorDictionary = mirexContributorDictionary;
	}

	public void setMirexSubmissionDao(MirexSubmissionDao mirexSubmissionDao) {
		this.mirexSubmissionDao = mirexSubmissionDao;
	}
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setMirexNoteDao(GenericDao<MirexNote, Long> mirexNoteDao) {
		this.mirexNoteDao = mirexNoteDao;
	}

	public List<MirexTask> mirexTaskSet() {
		return mirexTaskDictionary.findAllActive();
	}
	
	public SubmissionStatus[] getSubmissionStatusSet(){
		return SubmissionStatus.values();
	}
	
	public Collection<NoteType> noteTypeSet() {
		Collection<NoteType> typeSet=new HashSet<NoteType>();
		if (isSuperUser(userManager.getCurrentUser())){
			typeSet.add(NoteType.PUBLIC);
			typeSet.add(NoteType.PRIVATE);
		}else{
			typeSet.add(NoteType.USER);
		}
		return typeSet;
	}
	


	public void updateSubmission(MirexSubmission submission, ParameterMap params) {
		Long[] contributorIds = (Long[]) params.getArray("contributor",
				Long.class);
		Long taskId = params.getLong("mirexTask");
		submission.setMirexTask(mirexTaskDictionary.find(taskId));
		if (contributorIds != null) {
			List<Contributor> list=new ArrayList<Contributor>();
			for (Long id : contributorIds) {
				if (id!=null) {list.add(mirexContributorDictionary.find(id));}
			}
			submission.setContributors(list);
		}
	}
	
	public MirexSubmission saveForFirstTime(MirexSubmission submission){
		
		User user=userManager.getCurrentUser();
		
		
		submission.setHashcode(hashcodeGenerate(submission.getContributors()));
		submission.setStatus(SubmissionStatus.READY_FOR_RUN);
		submission.setUser(user);
		return mirexSubmissionDao.save(submission);
	}
	
	
	public MirexSubmission saveForRunner(MirexSubmission submission,ParameterMap params){
		String noteStr=params.get("mirexNote");
		User user=userManager.getCurrentUser();
		if (noteStr!=null){
			MirexNote note=new MirexNote();
			note.setAuthor(user);
			note.setCreateTime(new Date());
			note.setSubmission(submission);
			note.setContent(params.get("mirexNote"));
			if (isSuperUser(user)){
				String typeStr=params.get("noteType");
			NoteType noteType=NoteType.valueOf(typeStr);
			if (noteType!=null) {
				note.setType(noteType);
			}
			}else {note.setType(NoteType.USER);}
			
			//note=mirexNoteDao.save(note);
			submission.addNote(note);
		}
		
		
		return mirexSubmissionDao.save(submission);
	}
	public MirexSubmission loadSubmission(Integer id){
		if (id==null){
			return new MirexSubmission();
		}else{
			return mirexSubmissionDao.get(new Long(id));
		}
	}
	
  public  boolean isSuperUser(){
		User user=userManager.getCurrentUser();
		Set<Role>  roles=user.getRoles();
		for (Role role:roles){
			if ((Constants.MIREX_RUNNER_ROLE.equals(role.getName()))||
					(Constants.ADMIN_ROLE.equals(role.getName()))){
				return true;
			}
		}
		return false;
	}
	private String hashcodeGenerate(List<Contributor> contributors){
		StringBuilder code=new StringBuilder();
		for (Contributor contributor:contributors){
			String lastname=contributor.getLastname();
			if (!StringUtil.isEmpty(lastname)) code.append(lastname.charAt(0));
		}
		List<MirexSubmission> list=mirexSubmissionDao.findByHashcodeBeginning(code.toString());
		int num=0;
		for (MirexSubmission submission:list){
			String hashcode=submission.getHashcode();
			hashcode.substring(code.length());
			Integer seq=parseInt(hashcode.substring(code.length()));
			if ((seq!=null)&&(seq>num)){num=seq;}
		}
		code.append(num+1);
		return code.toString();
	}
	
	private Integer parseInt(String str){
		try{
			Integer a=Integer.valueOf(str);
		    return a;
		}catch(NumberFormatException e){
			return null;
		}
	}
	
	private boolean isSuperUser(User user){
		
		Set<Role>  roles=user.getRoles();
		for (Role role:roles){
			if ((Constants.MIREX_RUNNER_ROLE.equals(role.getName()))||
					(Constants.ADMIN_ROLE.equals(role.getName()))){
				return true;
			}
		}
		return false;
	}
}
