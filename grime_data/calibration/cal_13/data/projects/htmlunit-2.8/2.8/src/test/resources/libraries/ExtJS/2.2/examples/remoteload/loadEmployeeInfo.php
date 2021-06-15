<?php	
/*
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
	session_start();
	switch ($_REQUEST['employeeId']) {
		case '1':
			$employee = array(
				"employeeId"=>1,
				"firstName"=>"Joe",
				"lastName"=>"Smith",
				"department"=>"Management",
				"title"=>"CEO",
				"telephone"=>"240-555-1287"
			);
			if (true == $_SESSION['isAdmin']) {
				$employee['homeAddress'] = array(
					"street"=>"123 Bluebird Ct.",
					"city"=>"Columbia",
					"state"=>"MD"
				);
			}
			break;
		case '2':
			$employee = array(
				"employeeId"=>2,
				"firstName"=>"George",
				"lastName"=>"Loram",
				"department"=>"Sales",
				"title"=>"VP of Sales",
				"telephone"=>"240-555-1287"
			);
			if (true == $_SESSION['isAdmin']) {
				$employee['homeAddress'] = array(
					"street"=>"283 N. Market St.",
					"city"=>"Frederick",
					"state"=>"MD"
				);
			}
			break;
		case '3':
			$employee = array(
				"employeeId"=>3,
				"firstName"=>"Sally",
				"lastName"=>"Beogagi",
				"department"=>"Human Resources",
				"title"=>"Senior Hiring Agent",
				"telephone"=>"443-555-1220"
			);
			if (true == $_SESSION['isAdmin']) {
				$employee['homeAddress'] = array(
					"street"=>"8923 Redwood St.",
					"city"=>"Ellicott City",
					"state"=>"MD"
				);
			}
			break;
		case '4':
			$employee = array(
				"employeeId"=>4,
				"firstName"=>"Billy",
				"lastName"=>"Diarmaid",
				"department"=>"Human Resources",
				"title"=>"Hiring Agent",
				"telephone"=>"443-555-2890"
			);
			if (true == $_SESSION['isAdmin']) {
				$employee['homeAddress'] = array(
					"street"=>"893 Madison St.",
					"city"=>"Mt. Airy",
					"state"=>"MD"
				);
			}
			break;
		case '5':
			$employee = array(
				"employeeId"=>5,
				"firstName"=>"Bruno",
				"lastName"=>"Domingos",
				"department"=>"Technology",
				"title"=>"CTO",
				"telephone"=>"443-555-2890"
			);
			if (true == $_SESSION['isAdmin']) {
				$employee['homeAddress'] = array(
					"street"=>"123 5th St.",
					"city"=>"Washington",
					"state"=>"DC"
				);
			}
			break;		
	}
	echo json_encode($employee);
?>
