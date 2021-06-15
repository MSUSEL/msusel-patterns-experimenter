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
package org.apache.hadoop.security;

import java.io.IOException;
import java.security.Principal;

import javax.security.auth.login.LoginContext;

import org.apache.hadoop.security.UserGroupInformation.AuthenticationMethod;

/**
 * Save the full and short name of the user as a principal. This allows us to
 * have a single type that we always look for when picking up user names.
 */
class User implements Principal {
  private final String fullName;
  private final String shortName;
  private AuthenticationMethod authMethod = null;
  private LoginContext login = null;
  private long lastLogin = 0;

  public User(String name) {
    this(name, null, null);
  }
  
  public User(String name, AuthenticationMethod authMethod, LoginContext login) {
    try {
      shortName = new KerberosName(name).getShortName();
    } catch (IOException ioe) {
      throw new IllegalArgumentException("Illegal principal name " + name, ioe);
    }
    fullName = name;
    this.authMethod = authMethod;
    this.login = login;
  }

  /**
   * Get the full name of the user.
   */
  @Override
  public String getName() {
    return fullName;
  }
  
  /**
   * Get the user name up to the first '/' or '@'
   * @return the leading part of the user name
   */
  public String getShortName() {
    return shortName;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    } else {
      return ((fullName.equals(((User) o).fullName)) && (authMethod == ((User) o).authMethod));
    }
  }
  
  @Override
  public int hashCode() {
    return fullName.hashCode();
  }
  
  @Override
  public String toString() {
    return fullName;
  }

  public void setAuthenticationMethod(AuthenticationMethod authMethod) {
    this.authMethod = authMethod;
  }

  public AuthenticationMethod getAuthenticationMethod() {
    return authMethod;
  }
  
  /**
   * Returns login object
   * @return login
   */
  public LoginContext getLogin() {
    return login;
  }
  
  /**
   * Set the login object
   * @param login
   */
  public void setLogin(LoginContext login) {
    this.login = login;
  }
  
  /**
   * Set the last login time.
   * @param time the number of milliseconds since the beginning of time
   */
  public void setLastLogin(long time) {
    lastLogin = time;
  }
  
  /**
   * Get the time of the last login.
   * @return the number of milliseconds since the beginning of time.
   */
  public long getLastLogin() {
    return lastLogin;
  }
}
