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
package org.hibernate.test.readonly;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Steve Ebersole, Gail Badner (adapted this from "proxy" tests version)
 */
public class Container implements Serializable {
	private Long id;
	private String name;
	private Owner noProxyOwner;
	private Owner proxyOwner;
	private Owner nonLazyOwner;
	private Info noProxyInfo;
	private Info proxyInfo;
	private Info nonLazyInfo;
	private Set lazyDataPoints = new HashSet();
	private Set nonLazyJoinDataPoints = new HashSet();
	private Set nonLazySelectDataPoints = new HashSet();

	public Container() {
	}

	public Container(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Owner getNoProxyOwner() {
		return noProxyOwner;
	}

	public void setNoProxyOwner(Owner noProxyOwner) {
		this.noProxyOwner = noProxyOwner;
	}

	public Owner getProxyOwner() {
		return proxyOwner;
	}

	public void setProxyOwner(Owner proxyOwner) {
		this.proxyOwner = proxyOwner;
	}

	public Owner getNonLazyOwner() {
		return nonLazyOwner;
	}

	public void setNonLazyOwner(Owner nonLazyOwner) {
		this.nonLazyOwner = nonLazyOwner;
	}

	public Info getNoProxyInfo() {
		return noProxyInfo;
	}

	public void setNoProxyInfo(Info noProxyInfo) {
		this.noProxyInfo = noProxyInfo;
	}

	public Info getProxyInfo() {
		return proxyInfo;
	}

	public void setProxyInfo(Info proxyInfo) {
		this.proxyInfo = proxyInfo;
	}

	public Info getNonLazyInfo() {
		return nonLazyInfo;
	}

	public void setNonLazyInfo(Info nonLazyInfo) {
		this.nonLazyInfo = nonLazyInfo;
	}

	public Set getLazyDataPoints() {
		return lazyDataPoints;
	}

	public void setLazyDataPoints(Set lazyDataPoints) {
		this.lazyDataPoints = lazyDataPoints;
	}

	public Set getNonLazyJoinDataPoints() {
		return nonLazyJoinDataPoints;
	}

	public void setNonLazyJoinDataPoints(Set nonLazyJoinDataPoints) {
		this.nonLazyJoinDataPoints = nonLazyJoinDataPoints;
	}

	public Set getNonLazySelectDataPoints() {
		return nonLazySelectDataPoints;
	}

	public void setNonLazySelectDataPoints(Set nonLazySelectDataPoints) {
		this.nonLazySelectDataPoints = nonLazySelectDataPoints;
	}
}