package fr.exia.stentor.model.ui;

public class SettingsItem {
	private TypeOfLanguage typeOfLanguage;
	private String name;
	private String description;
	private String checkInfo;

	public SettingsItem() {
	}

	public SettingsItem(TypeOfLanguage typeOfLanguage, String name, String description, String checkInfo) {
		this.typeOfLanguage = typeOfLanguage;
		this.name = name;
		this.description = description;
		this.checkInfo = checkInfo;
	}

	@Override
	public String toString() {
		return "SettingsItem{" +
				"typeOfLanguage=" + typeOfLanguage.toString() +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", checkInfo='" + checkInfo + '\'' +
				'}';
	}

	public enum TypeOfLanguage {
		APP("APP"),
		VOICE("VOICE");

		private String value;

		TypeOfLanguage(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	public TypeOfLanguage getTypeOfLanguage() {
		return typeOfLanguage;
	}

	public void setTypeOfLanguage(TypeOfLanguage typeOfLanguage) {
		this.typeOfLanguage = typeOfLanguage;
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

	public String getCheckInfo() {
		return checkInfo;
	}

	public void setCheckInfo(String checkInfo) {
		this.checkInfo = checkInfo;
	}
}
