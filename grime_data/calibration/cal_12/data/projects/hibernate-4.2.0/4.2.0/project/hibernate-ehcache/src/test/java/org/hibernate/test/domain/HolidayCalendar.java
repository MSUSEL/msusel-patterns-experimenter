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
package org.hibernate.test.domain;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HolidayCalendar {


	private Long id;
	// Date -> String
	private Map holidays = new HashMap();

	public HolidayCalendar init() {
		DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
		try {
			holidays.clear();
			holidays.put(df.parse("2009.01.01"), "New Year's Day");
			holidays.put(df.parse("2009.02.14"), "Valentine's Day");
			holidays.put(df.parse("2009.11.11"), "Armistice Day");
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public Map getHolidays() {
		return holidays;
	}

	protected void setHolidays(Map holidays) {
		this.holidays = holidays;
	}

	public void addHoliday(Date d, String name) {
		holidays.put(d, name);
	}

	public String getHoliday(Date d) {
		return (String)holidays.get(d);
	}

	public boolean isHoliday(Date d) {
		return holidays.containsKey(d);
	}

	protected Long getId() {
		return id;
	}

	protected void setId(Long id) {
		this.id = id;
	}
}

