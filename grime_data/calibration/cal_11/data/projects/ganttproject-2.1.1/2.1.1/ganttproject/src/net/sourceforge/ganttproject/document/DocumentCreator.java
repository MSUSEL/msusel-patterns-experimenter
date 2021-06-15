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
 * Created on 20.08.2003
 *
 */
package net.sourceforge.ganttproject.document;

import java.io.File;
import java.io.IOException;

import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.gui.TaskTreeUIFacade;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.TableHeaderUIFacade;
import net.sourceforge.ganttproject.gui.options.model.DefaultStringOption;
import net.sourceforge.ganttproject.gui.options.model.GP1XOptionConverter;
import net.sourceforge.ganttproject.gui.options.model.GPOption;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.gui.options.model.StringOption;
import net.sourceforge.ganttproject.parser.ParserFactory;

/**
 * This is a helper class, to create new instances of Document easily. It
 * chooses the correct implementation based on the given path.
 * 
 * @author Michael Haeusler (michael at akatose.de)
 */
public class DocumentCreator implements DocumentManager {
    private IGanttProject myProject;

    private UIFacade myUIFacade;

    private ParserFactory myParserFactory;

    public DocumentCreator(IGanttProject project, UIFacade uiFacade,
            ParserFactory parserFactory) {
        myProject = project;
        myUIFacade = uiFacade;
        setParserFactory(parserFactory);
    }

    /**
     * Creates an HttpDocument if path starts with "http://" or "https://";
     * creates a FileDocument otherwise.
     * 
     * @param path
     *            path to the document
     * @return an implementation of the interface Document
     */
    private Document createDocument(String path) {
        return createDocument(path, null, null);
    }

    /**
     * Creates an HttpDocument if path starts with "http://" or "https://";
     * creates a FileDocument otherwise.
     * 
     * @param path
     *            path to the document
     * @param user
     *            username
     * @param pass
     *            password
     * @return an implementation of the interface Document
     */
    private Document createDocument(String path, String user, String pass) {
        assert path!=null;
        path = path.trim();
        if (path.toLowerCase().startsWith("http://")
                || path.toLowerCase().startsWith("https://")) {
            return new HttpDocument(path, user, pass);
        }
        else if (path.toLowerCase().startsWith("ftp:")) {
            return new FtpDocument(path, myFtpUserOption, myFtpPasswordOption);
        }
        else {
            return new FileDocument(new File(path));
        }
    }

    public Document getDocument(String path) {
        Document physicalDocument = createDocument(path);
        Document proxyDocument = new ProxyDocument(this, physicalDocument, myProject,
                myUIFacade, getVisibleFields(), getParserFactory());
        return proxyDocument;
    }
    
    public Document getDocument(String path, String userName, String password) {
        Document physicalDocument = createDocument(path, userName, password);
        Document proxyDocument = new ProxyDocument(this, physicalDocument, myProject, myUIFacade, getVisibleFields(), getParserFactory());
        return proxyDocument;
    }

    protected TableHeaderUIFacade getVisibleFields() {
		return null;
	}

	public void addToRecentDocuments(Document document) {
        // TODO Auto-generated method stub
        
    }

    protected void setParserFactory(ParserFactory myParserFactory) {
        this.myParserFactory = myParserFactory;
    }

    protected ParserFactory getParserFactory() {
        return myParserFactory;
    }

	String createTemporaryFile() throws IOException {
		File tempFile = File.createTempFile("project", ".gan", getWorkingDirectoryFile());
		return tempFile.getAbsolutePath();
	}

    public void changeWorkingDirectory(File directory) {
    	assert directory.isDirectory();
        myWorkingDirectory.lock();
        myWorkingDirectory.setValue(directory.getAbsolutePath());
        myWorkingDirectory.commit();
    }
	public String getWorkingDirectory() {
		return myWorkingDirectory.getValue();
	}
	public StringOption getLastWebDAVDocumentOption() {
		return myLastWebDAVDocument;
	}
    private File getWorkingDirectoryFile() {
    	return new File(getWorkingDirectory());
    }
    public GPOptionGroup getOptionGroup() {
        return myOptionGroup;
    }
    public FTPOptions getFTPOptions() {
    	return myFtpOptions;
    }
    public GPOptionGroup[] getNetworkOptionGroups() {
        return new GPOptionGroup[] {myFtpOptions};
    }
    
    
    private final StringOption myWorkingDirectory = new StringOptionImpl("working-dir", "working-dir", "dir");
    private final StringOption myLastWebDAVDocument = new StringOptionImpl("last-webdav-document", "", "");
    private final GPOptionGroup myOptionGroup = new GPOptionGroup("", new GPOption[] {myWorkingDirectory, myLastWebDAVDocument});
    
    private final StringOption myFtpUserOption = new StringOptionImpl("user-name", "ftp", "ftpuser");
    private final StringOption myFtpServerNameOption = new StringOptionImpl("server-name", "ftp", "ftpurl");
    private final StringOption myFtpDirectoryNameOption = new StringOptionImpl("directory-name", "ftp", "ftpdir");
    private final StringOption myFtpPasswordOption = new StringOptionImpl("password", "ftp", "ftppwd");
    private final FTPOptions myFtpOptions = new FTPOptions("ftp", new GPOption[] {myFtpUserOption, myFtpServerNameOption, myFtpDirectoryNameOption, myFtpPasswordOption}) {
		public StringOption getDirectoryName() {
			return myFtpDirectoryNameOption;
		}

		public StringOption getPassword() {
			return myFtpPasswordOption;
		}

		public StringOption getServerName() {
			return myFtpServerNameOption;
		}

		public StringOption getUserName() {
			return myFtpUserOption;
		}
    	
    };
    
    static final String USERNAME_OPTION_ID = "user-name";
    static final String SERVERNAME_OPTION_ID = "server-name";
    static final String DIRECTORYNAME_OPTION_ID = "directory-name";
    static final String PASSWORD_OPTION_ID = "password";
    
    private static class StringOptionImpl extends DefaultStringOption implements GP1XOptionConverter {
        private final String myLegacyTagName;
        private final String myLegacyAttrName;

        private StringOptionImpl(String optionName, String legacyTagName, String legacyAttrName) {
            super(optionName);
            myLegacyTagName = legacyTagName;
            myLegacyAttrName = legacyAttrName;
        }
        public String getTagName() {
            return myLegacyTagName;
        }

        public String getAttributeName() {
            return myLegacyAttrName;
        }

        public void loadValue(String legacyValue) {
            lock();
            loadPersistentValue(legacyValue);
            commit();            
        }
        
    }
        
}
