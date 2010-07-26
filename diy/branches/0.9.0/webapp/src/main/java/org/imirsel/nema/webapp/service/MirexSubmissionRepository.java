package org.imirsel.nema.webapp.service;

import org.springframework.web.multipart.MultipartFile;

public interface MirexSubmissionRepository {
	String save(MultipartFile file);
	String exist(String path);
	String remove(String path);
}
