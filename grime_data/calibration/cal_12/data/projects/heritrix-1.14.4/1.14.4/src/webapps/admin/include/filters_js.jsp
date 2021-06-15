<%--

    The MIT License (MIT)

    MSUSEL Arc Framework
    Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
    Software Engineering Laboratory and Idaho State University, Informatics and
    Computer Science, Empirical Software Engineering Laboratory

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

--%>
<%--This page is included by filters.jsp and by url-canonicalization-rules.jsp
    at least.
 --%>
<%@ page import="org.archive.crawler.admin.CrawlJob" %>
<script type="text/javascript">
    function doSubmit(){
        document.frmFilters.submit();
    }
    
    function doGoto(where){
        document.frmFilters.action.value="goto";
        document.frmFilters.subaction.value = where;
        doSubmit();
    }
    
    function doMoveUp(filter,map){
        document.frmFilters.action.value = "filters";
        document.frmFilters.subaction.value = "moveup";
        document.frmFilters.map.value = map;
        document.frmFilters.filter.value = filter;
        doSubmit();
    }

    function doMoveDown(filter,map){
        document.frmFilters.action.value = "filters";
        document.frmFilters.subaction.value = "movedown";
        document.frmFilters.map.value = map;
        document.frmFilters.filter.value = filter;
        doSubmit();
    }

    function doRemove(filter,map){
        document.frmFilters.action.value = "filters";
        document.frmFilters.subaction.value = "remove";
        document.frmFilters.map.value = map;
        document.frmFilters.filter.value = filter;
        doSubmit();
    }

    function doAdd(map){
        if(document.getElementById(map+".name").value == ""){
            alert("Must enter a unique name for the subcomponent");
        } else {
            document.frmFilters.action.value = "filters";
            document.frmFilters.subaction.value = "add";
            document.frmFilters.map.value = map;
            doSubmit();
        }
    }
</script>
