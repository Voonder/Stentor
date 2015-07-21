/**************************************************************************************************
 * The MIT License (MIT)                                                                          *
 *                                                                                                *
 * Copyright (c) 2015 - Julien Normand                                                            *
 *                                                                                                *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software  *
 * and associated documentation files (the "Software"),  to deal in the Software without          *
 * restriction, including without limitation  the  rights to use, copy, modify, merge, publish,   *
 * distribute, sublicense, and/or  sell copies of the Software, and to permit persons to whom the *
 * Software is furnished to do so, subject to the following conditions:                           *
 *                                                                                                *
 * The above copyright notice and this permission notice shall be included in all copies or       *
 * substantial portions of the Software.                                                          *
 *                                                                                                *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING  *
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND     *
 * NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,  *
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING       *
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.  *
 **************************************************************************************************/

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
}
