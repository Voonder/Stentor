package fr.exia.stentor.model.ui;

import java.util.List;

public class LicenseItem {

	private String name;
	private List<String> libraryList;

	public LicenseItem(String name, List<String> libraryList) {
		this.name = name;
		this.libraryList = libraryList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getLibraryList() {
		return libraryList;
	}

	public void setLibraryList(List<String> libraryList) {
		this.libraryList = libraryList;
	}
}
