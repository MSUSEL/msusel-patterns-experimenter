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
package org.hibernate.build.gradle.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.util.FileUtils;

/**
 * Models path information for a particular JDK install.
 * <p/>
 * Copied largely from {@link org.gradle.util.Jvm} and {@link org.apache.tools.ant.util.JavaEnvUtils}.  The main
 * difference is that those classes are static, based solely on the reported "java.home" sys prop.  Also, Ant's
 * JavaEnvUtils allows for use of either a JRE or JDK; we do not care about allowing for a JRE-only set up here.
 *
 *
 * @author Steve Ebersole
 */
public class Jdk {
	private static final boolean IS_DOS = Os.isFamily( "dos" );
	private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

	private final File jdkHome;
	private final JavaVersion version;
    public Jdk(){
        this(System.getenv( "JAVA_HOME" ));
    }

	public Jdk(File jdkHome) {
		this.jdkHome = jdkHome;
		if ( !jdkHome.exists() ) {
			throw new IllegalArgumentException( "Invalid path specified for JDK home; " + jdkHome.getAbsolutePath() + " did not exist" );
		}
		this.version = determineJdkVersion();
	}

	public Jdk(String jdkHomePath) {
		this( new File( jdkHomePath ) );
	}

    public File getJavaExecutable() {
        return new File( getJdkExecutable( "java" ) );
    }

	public File getJavacExecutable() {
		return new File( getJdkExecutable( "javac" ) );
	}

    public File getJavadocExecutable() {
        return new File( getJdkExecutable( "javadoc" ) );
    }

	public JavaVersion getVersion() {
		return version;
	}

	protected String getJdkExecutable(String command) {
		File executable = findInDir( jdkHome + "/bin", command );

		if ( executable == null ) {
			executable = findInDir( jdkHome + "/../bin", command );
		}

		if ( executable != null ) {
			return executable.getAbsolutePath();
		}
		else {
			// Unfortunately on Windows java.home doesn't always refer
			// to the correct location, so we need to fall back to
			// assuming java is somewhere on the PATH.
			return addExtension( command );
		}
	}

	private static File findInDir(String dirName, String commandName) {
		File dir = FILE_UTILS.normalize(dirName);
		File executable = null;
		if (dir.exists()) {
			executable = new File(dir, addExtension(commandName));
			if (!executable.exists()) {
				executable = null;
			}
		}
		return executable;
	}

	private static String addExtension(String command) {
		// This is the most common extension case - exe for windows and OS/2,
		// nothing for *nix.
		return command + (IS_DOS ? ".exe" : "");
	}

	private JavaVersion determineJdkVersion() {
		String javaVersionString = extractFromSunJdk();
		if ( javaVersionString == null ) {
			javaVersionString = "1.6";//make 1.6 as default
		}
		return new JavaVersion( javaVersionString );
	}

	private String extractFromSunJdk() {
		String version = null;

		try {
			final File javaCommand = getJavaExecutable();
			Process javaProcess = Runtime.getRuntime().exec( javaCommand.getAbsolutePath() + " -version" );

			try {
				version = extractVersion( new BufferedReader( new InputStreamReader( javaProcess.getErrorStream() ) ) );
				if( version == null || version.equals( "" )){
					version = extractVersion( new BufferedReader( new InputStreamReader( javaProcess.getInputStream() ) ) );
				}
			}
			finally {
				javaProcess.destroy();
			}
		}
		catch ( IOException e ) {
			throw new RuntimeException( "Unable to determine Java version", e );
		}
		return version;
	}
	
	private String extractVersion(BufferedReader br) throws IOException{
		final String key = "version \"";
		String line = null;
		String version = null;
		while ( (line = br.readLine()) != null) {
			if ( version == null && line.contains( key ) ) {
				version = line.substring( line.indexOf( key ) + key.length(), line.length() - 1 );
			}
		}
		br.close();
		return version;
	}
}
