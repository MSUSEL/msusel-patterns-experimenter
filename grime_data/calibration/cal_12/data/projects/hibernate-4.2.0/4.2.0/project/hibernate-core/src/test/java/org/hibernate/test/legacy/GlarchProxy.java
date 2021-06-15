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
package org.hibernate.test.legacy;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GlarchProxy {
	
	public int getVersion();
	public int getDerivedVersion();
	public void setVersion(int version);
	
	public String getName();
	public void setName(String name);
	
	public GlarchProxy getNext();
	public void setNext(GlarchProxy next);
	
	public short getOrder();
	public void setOrder(short order);
	
	public List getStrings();
	public void setStrings(List strings);
	
	public Map getDynaBean();
	public void setDynaBean(Map bean);
	
	public Map getStringSets();
	public void setStringSets(Map stringSets);
	
	public List getFooComponents();
	public void setFooComponents(List fooComponents);
	
	public GlarchProxy[] getProxyArray();
	public void setProxyArray(GlarchProxy[] proxyArray);
	
	public Set getProxySet();
	public void setProxySet(Set proxySet);
	
	public Multiplicity getMultiple();
	public void setMultiple(Multiplicity m);
	
	public Object getAny();
	public void setAny(Object any);
}







