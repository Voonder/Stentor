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

package fr.exia.stentor.speech;

public class SpeechUtils {

    private static final String APP_PACKAGE = "fr.exia.stentor.";

    // everywhere
    public static final String BACK = APP_PACKAGE + "BACK";

    // Position 1
    public static final String CLOSE_APP = APP_PACKAGE + "CLOSE_APP";
    public static final String CLOSE_SERVICE = APP_PACKAGE + "CLOSE_SERVICE";

    // Position 2
    public static final String OPEN_HOME = APP_PACKAGE + "OPEN_HOME";
    public static final String OPEN_MAINTENANCE = APP_PACKAGE + "OPEN_MAINTENANCE";
    public static final String OPEN_SETTINGS = APP_PACKAGE + "OPEN_SETTINGS";
    public static final String OPEN_HELP_FEEDBACK = APP_PACKAGE + "OPEN_HELP_FEEDBACK";

    // Position 3
    public static final String OPERATION = APP_PACKAGE + "OPERATION";

    // Position 4
    public static final String OP_LAUNCH = APP_PACKAGE + "OP_LAUNCH";
    public static final String OP_REPEAT = APP_PACKAGE + "OP_REPEAT";
    public static final String OP_NEXT = APP_PACKAGE + "OP_NEXT";
    public static final String OP_PREVIOUS = APP_PACKAGE + "OP_PREVIOUS";
    public static final String OP_STOP = APP_PACKAGE + "OP_STOP";
}
