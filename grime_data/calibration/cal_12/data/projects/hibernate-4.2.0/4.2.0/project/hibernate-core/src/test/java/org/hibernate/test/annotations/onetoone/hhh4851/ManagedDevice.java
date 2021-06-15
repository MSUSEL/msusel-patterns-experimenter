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
package org.hibernate.test.annotations.onetoone.hhh4851;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * this class represents a logical representation of a terminal it could be linked to a terminal or not it contains the
 * alias of the terminal and is virtualizable
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
public class ManagedDevice extends BaseEntity {

	private String name;
	private Device device;
	private DeviceGroupConfig deviceGroupConfig = null;

	public ManagedDevice() {
	}

	public ManagedDevice(String alias) {
		this.name = alias;
	}

	public String getName() {
		return name;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "terminal_id")
	public Device getDevice() {
		return device;
	}

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinTable(name = "ManDev_DevGroupConf",
			joinColumns = { @JoinColumn(name = "MavDev_id", unique = true) },
			inverseJoinColumns = { @JoinColumn(name = "DevGroupConf_id") })
	public DeviceGroupConfig getDeviceGroupConfig() {
		return deviceGroupConfig;
	}

	public void setName(String alias) {
		this.name = alias;
	}

	public void setDevice(Device terminal) {
		this.device = terminal;
	}

	public void setDeviceGroupConfig(DeviceGroupConfig terminalGroup) {
		this.deviceGroupConfig = terminalGroup;
	}

}
