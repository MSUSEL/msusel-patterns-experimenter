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
package org.hibernate.ejb.packaging;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Set;

/**
 * @author Emmanuel Bernard
 */
public interface Scanner {
	/**
	 * return all packages in the jar matching one of these annotations
	 * if annotationsToLookFor is empty, return all packages
	 */
	Set<Package> getPackagesInJar(URL jartoScan, Set<Class<? extends Annotation>> annotationsToLookFor);

	/**
	 * return all classes in the jar matching one of these annotations
	 * if annotationsToLookFor is empty, return all classes
	 */
	Set<Class<?>> getClassesInJar(URL jartoScan, Set<Class<? extends Annotation>> annotationsToLookFor);

	/**
	 * return all files in the jar matching one of these file names
	 * if filePatterns is empty, return all files
	 * eg **\/*.hbm.xml, META-INF/orm.xml
	 */
	Set<NamedInputStream> getFilesInJar(URL jartoScan, Set<String> filePatterns);


	/**
	 * Return all files in the classpath (ie PU visibility) matching one of these file names
	 * if filePatterns is empty, return all files
	 * the use case is really exact file name.
	 *
	 * NOT USED by HEM at the moment. We use exact file search via getResourceAsStream for now.
	 */
	Set<NamedInputStream> getFilesInClasspath(Set<String> filePatterns);

	/**
	 * return the unqualified JAR name ie customer-model.jar or store.war
	 */
	String getUnqualifiedJarName(URL jarUrl);

}
