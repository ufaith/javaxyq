package com.javaxyq.android.common.graph.tcp;

import java.util.ArrayList;
import java.util.List;

public class Section {
	
	private int start;
	
	private int end;
	
	private List<ColorationScheme> schemes;
	
	public Section(int start,int end) {
		this.start = start;
		this.end = end;
		schemes = new ArrayList<ColorationScheme>();
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public List<ColorationScheme> getSchemes() {
		return schemes;
	}

	public void setSchemes(List<ColorationScheme> schemes) {
		this.schemes = schemes;
	}
	
	public void addScheme(ColorationScheme scheme) {
		this.schemes.add(scheme);
	}
	
	public ColorationScheme getScheme(int index) {
		return this.schemes.get(index);
	}
	
	public int getSchemeCount() {
		return this.schemes.size();
	}
}
