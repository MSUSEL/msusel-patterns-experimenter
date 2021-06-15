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
	$_SESSION['isAdmin'] = true;
?>
<html>
<head>
  <title>Remote Component Loading - Employee DB</title>
	<link rel="stylesheet" type="text/css" href="../../resources/css/ext-all.css" />
    
    <!-- GC -->
 	<!-- LIBS -->
 	<script type="text/javascript" src="../../adapter/ext/ext-base.js"></script>
 	<!-- ENDLIBS -->

    <script type="text/javascript" src="../../ext-all.js"></script>
    <script type="text/javascript" src="ComponentLoader.js"></script>
	<script type="text/javascript" src="EmployeeDetailsTab.js"></script>
	<script type="text/javascript" src="EmployeeDetails.js"></script>
	<script type="text/javascript" src="EmployeePropertyGrid.js"></script>
	<script type="text/javascript" src="EmployeeGrid.js"></script>
	<script type="text/javascript" src="EmployeeStore.js"></script>
    <script type="text/javascript" src="App.js"></script>	

</head>
<body>
	<textarea id="employeeDetailTpl" class="x-hidden">		
		<div>
			<table cellpadding="2" cellspacing="2">
				<tr>
					<td>First Name:</td>
					<td>{firstName}</td>
				</tr>
				<tr>
					<td>Last Name:</td>
					<td>{lastName}</td>
				</tr>
				<tr>
					<td>Title:</td>
					<td>{title}</td>
				</tr>
				<tr>
					<td>Telephone:</td>
					<td>{telephone}</td>
				</tr>							
		
	<?php
		if (true == $_SESSION['isAdmin']) {
	?>
				<tr>
					<td>Home Address</td>
					<td><tpl for="homeAddress">{street}<br/>{city}, {state}<br/></tpl></td>
				</tr>			
	
	<?php
		}
	?>
		</div>
	</textarea>
	
</body>
</html>
