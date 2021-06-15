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
if(!dojo._hasResource["dojo.data.api.Request"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojo.data.api.Request"] = true;
dojo.provide("dojo.data.api.Request");

dojo.declare("dojo.data.api.Request", null, {
	//	summary:
	//		This class defines out the semantics of what a 'Request' object looks like
	//		when returned from a fetch() method.  In general, a request object is
	//		nothing more than the original keywordArgs from fetch with an abort function 
	//		attached to it to allow users to abort a particular request if they so choose. 
	//		No other functions are required on a general Request object return.  That does not
	//		inhibit other store implementations from adding extentions to it, of course.
	//
	//		This is an abstract API that data provider implementations conform to.  
	//		This file defines methods signatures and intentionally leaves all the
	//		methods unimplemented.
	//
	//		For more details on fetch, see dojo.data.api.Read.fetch().

	abort: function(){
		//	summary:
		//		This function is a hook point for stores to provide as a way for 
		//		a fetch to be halted mid-processing.
		//	description:
		//		This function is a hook point for stores to provide as a way for 
		//		a fetch to be halted mid-processing.  For more details on the fetch() api,
		//		please see dojo.data.api.Read.fetch().
		throw new Error('Unimplemented API: dojo.data.api.Request.abort');
	}
});

}
