package fr.exia.stentor.model;

import java.io.Serializable;
import java.util.List;

public class Operation implements Serializable {

	private String name;
	private String description;
	private Time duration;
	private List<String> steps;

	public Operation() {
	}

	public Operation(String name, String description, Time duration, List<String> steps) {
		this.name = name;
		this.description = description;
		this.duration = duration;
		this.steps = steps;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Time getDuration() {
		return duration;
	}

	public void setDuration(Time duration) {
		this.duration = duration;
	}

	public List<String> getSteps() {
		return steps;
	}

	public void setSteps(List<String> steps) {
		this.steps = steps;
	}
}
