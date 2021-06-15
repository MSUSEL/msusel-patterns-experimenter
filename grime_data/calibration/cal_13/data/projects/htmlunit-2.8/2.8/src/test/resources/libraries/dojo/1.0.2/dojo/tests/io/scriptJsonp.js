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
function getJsonpCallback(url){
	var result = null;
	var idMatch = url.match(/jsonp=(.*?)(&|$)/);
	if(idMatch){
		result = idMatch[1];
	}else{
		//jsonp didn't match, so maybe it is the jsonCallback thing.
		idMatch = url.match(/callback=(.*?)(&|$)/);
		if(idMatch){
			result = idMatch[1];
		}
	}
	
	if(result){
		result = decodeURIComponent(result);
	}
	return result;
}

function findJsonpDone(){
	var result = false;
	var scriptUrls = getScriptUrls();
	
	for(var i = 0; i < scriptUrls.length; i++){
		var jsonp = getJsonpCallback(scriptUrls[i]);
		if(jsonp){
			eval(jsonp + "({animalType: 'mammal'});");
			result = true;
			break;
		}
	}
	return result;
}

function getScriptUrls(){
	//Get the script tags in the page to figure what state we are in.
	var scripts = document.getElementsByTagName('script');
	var scriptUrls = new Array();
	for(var i = 0; scripts && i < scripts.length; i++){
		var scriptTag = scripts[i];
		if(scriptTag.id.indexOf("dojoIoScript") == 0){
			scriptUrls.push(scriptTag.src);
		}
	}

	return scriptUrls;
}

function doJsonpCallback(){
	if(!findJsonpDone()){
		 alert('ERROR: Could not jsonp callback!');
	}
}

//Set a timeout to do the callback check, since MSIE won't see the SCRIPT tag until
//we complete processing of this page.
setTimeout('doJsonpCallback()', 300);
