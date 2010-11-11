/**
 * 
 */
package org.imirsel.nema.webapp.json;

import java.util.HashMap;
import java.util.Map;

import org.imirsel.nema.model.Profile;

/**
 * Converter for a  {@link Profile} to (key, value) pairs for Json rendering.
 * @author gzhu1
 * 
 */
public class ConverterToMapProfile implements ConverterToMap<Profile> {

	@Override
	public Map<String, String> convertToMap(Profile profile) {
		Map<String, String> map = new HashMap<String, String>();

		JsonHelper helper = new JsonHelper(map);

		helper.addCheckNull(profile.getId(), "id");
		
		helper.addCheckNull(profile.getFirstname(), "firstname");
		helper.addCheckNull(profile.getLastname(), "lastname");
		helper.addCheckNull(profile.getOrganization(), "orgnization");
                helper.addCheckNull(profile.getUnit(), "unit");
                helper.addCheckNull(profile.getDepartment(), "department");
                helper.addCheckNull(profile.getTitle(), "title");
		return map;
	}

}
