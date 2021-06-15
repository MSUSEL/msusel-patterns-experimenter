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
	require_once("./JSON.php");
	
	// FIXME: doesn't look like we really need Pear at all
	// which decreases the testing burden. 
	// Commenting out.the require and the new File() call.

	// NOTE: File.php is installed via Pear using:
	//	%> sudo pear install File
	// Your server will also need the Pear library directory included in PHP's
	// include_path configuration directive
	// require_once('File.php');

	// ensure that we don't try to send "html" down to the client
	header("Content-Type: text/plain");

	$json = new Services_JSON;
	//$fp = new File();

	$results = array();
	$results['error'] = null;

	$jsonRequest = file_get_contents('php://input');
	//$jsonRequest = '{"params":["Blah"],"method":"myecho","id":86}';

	$req = $json->decode($jsonRequest);

	include("./testClass.php");
	$testObject = new testClass();

	$method = $req->method;
	if ($method != "triggerRpcError") {
		$ret = call_user_func_array(array($testObject,$method),$req->params);
		$results['result'] = $ret;
	} else {
		$results['error'] = "Triggered RPC Error test";
	}
	$results['id'] = $req->id;

	$encoded = $json->encode($results);

	print $encoded;
?>
