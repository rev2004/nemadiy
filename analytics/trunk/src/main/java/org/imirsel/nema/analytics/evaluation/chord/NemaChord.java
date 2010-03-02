package org.imirsel.nema.analytics.evaluation.chord;

import java.util.Arrays;

public class NemaChord {
	
	double onset;
	double offset;
	int[] notes;
	
	public NemaChord() {
		onset = -1;
		offset = -1;
		notes = null;
	}
	
	public NemaChord(double onset, double offset, final int[] notes){
		this.onset = onset;
		this.offset = offset;
		this.notes = notes;
	}
	
	/**
	 * Compute hashcode using only the notes (not onset/offest times).
	 * @return hashCode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(notes);
		return result;
	}

	@Override
	public String toString() {
		return onset + "\t" + offset + "\t" + ChordConversionUtil.convertNotenumbersToIntervals(notes);
	}

	/**
	 * Compare for equality using only the notes (not onset/offest times).
	 * @return flag inidcating equality
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NemaChord other = (NemaChord) obj;
		if (!Arrays.equals(notes, other.notes))
			return false;
		return true;
	}

	public double getOnset() {
		return onset;
	}

	public void setOnset(double onset) {
		this.onset = onset;
	}

	public double getOffset() {
		return offset;
	}

	public void setOffset(double offset) {
		this.offset = offset;
	}

	public int[] getNotes() {
		return notes;
	}

	public void setNotes(int[] notes) {
		this.notes = notes;
	}
}
