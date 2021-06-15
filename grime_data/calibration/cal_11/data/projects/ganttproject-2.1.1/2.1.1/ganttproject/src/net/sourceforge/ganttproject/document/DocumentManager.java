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
 * Created on 12.03.2005
 */
package net.sourceforge.ganttproject.document;

import java.io.File;

import net.sourceforge.ganttproject.gui.options.model.GPOption;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.gui.options.model.StringOption;

/**
 * @author bard
 */
public interface DocumentManager {
    Document getDocument(String path);

    void addToRecentDocuments(Document document);

    Document getDocument(String path, String userName, String password);

	void changeWorkingDirectory(File parentFile);

	String getWorkingDirectory();

	GPOptionGroup getOptionGroup();
	FTPOptions getFTPOptions();
    GPOptionGroup[] getNetworkOptionGroups();
    StringOption getLastWebDAVDocumentOption();
    abstract class FTPOptions extends GPOptionGroup {
		public FTPOptions(String id, GPOption[] options) {
			super(id, options);
		}
    	public abstract StringOption getServerName();
    	public abstract StringOption getUserName();
    	public abstract StringOption getDirectoryName();
    	public abstract StringOption getPassword();
    }
}
