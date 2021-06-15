/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
/*
 * Informations of the project
 */
package net.sourceforge.ganttproject;

/**
 * @author athomas Class to store the project informations
 */
public class PrjInfos {
    /** The name of the project */
    public String _sProjectName = new String();

    /** A short description of it */
    public String _sDescription = new String();

    /** The name of the organisation */
    public String _sOrganization = new String();

    /** Web link for the project or for the company */
    public String _sWebLink = new String();

    /** Default constructor with no parameters. */
    public PrjInfos() {
        this._sProjectName = "Untitled Gantt Project";
        this._sDescription = "";
        this._sOrganization = "";
        this._sWebLink = "http://";
    }

    /** Constructor. */
    public PrjInfos(String sProjectName, String sDescription,
            String sOrganization, String sWebLink) {
        this._sProjectName = sProjectName;
        this._sDescription = sDescription;
        this._sOrganization = sOrganization;
        this._sWebLink = sWebLink;
    }

    /** @return the name of the project. */
    public String getName() {
        return _sProjectName;
    }

    /** @return the description of the project. */
    public String getDescription() {
        return _sDescription;
    }

    /** @return the organization of the project. */
    public String getOrganization() {
        return _sOrganization;
    }

    /** @return the web link for the project or for the company. */
    public String getWebLink() {
        return _sWebLink;
    }
}
