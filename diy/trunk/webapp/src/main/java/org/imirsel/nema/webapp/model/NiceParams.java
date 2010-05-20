package org.imirsel.nema.webapp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.imirsel.nema.model.Param;

public class NiceParams {
	private List<Param> params;

	public NiceParams(List<Param> params) {
		this.setParams(params);
	}

	public void setParams(List<Param> params) {
		this.params = params;
	}

	public List<String> getType(Param.ParamType type) {
		List<Param> list = new ArrayList<Param>();
		for (Param param : params) {
			if (param.getTypeCode() == type.getCode())
				list.add(param);
		}
		Collections.sort(list, comparator);

		List<String> listString = new ArrayList<String>();
		for (Param param : list) {
			listString.add(param.getValue());
		}

		return listString;
	}

	public List<String> getInputs() {
		return getType(Param.ParamType.INPUT);
	}

	public List<String> getOutputs() {
		return getType(Param.ParamType.OUTPUT);
	}

	public List<String> getOthers() {
		return getType(Param.ParamType.OTHER);
	}

	class ParamComparator implements Comparator<Param> {
		public int compare(Param p1, Param p2) {
			return Integer.signum(p1.getSequence() - p2.getSequence());
		}
		boolean equals(Param p) {
			return false;
		}
	}

	static ParamComparator comparator;

}
