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
 * Copyright (C) 2005 Leon Nicholls
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * 
 * See the file "COPYING" for more details.
 */

package org.lnicholls.galleon.util;

import sun.security.util.SecurityConstants;

import java.lang.reflect.Member;

public class CustomSecurityManager extends SecurityManager {

	public CustomSecurityManager()
	{
		
	}
	
	public void checkMemberAccess(Class clazz, int which) {
		if (clazz == null) {
		    throw new NullPointerException("class can't be null");
		}
		System.out.println(clazz.getName()+"="+which);
		if (which != Member.PUBLIC) {
		    Class stack[] = getClassContext();
		    /*
		     * stack depth of 4 should be the caller of one of the
		     * methods in java.lang.Class that invoke checkMember
		     * access. The stack should look like:
		     * 
		     * someCaller                         [3]
		     * java.lang.Class.someReflectionAPI  [2]
		     * java.lang.Class.checkMemeberAccess [1]
		     * SecurityManager.checkMemeberAccess [0]
		     *
		     */
		    if ((stack.length<4) || 
			(stack[3].getClassLoader() != clazz.getClassLoader())) {
			checkPermission(SecurityConstants.CHECK_MEMBER_ACCESS_PERMISSION);
		    }
		}
    }
}