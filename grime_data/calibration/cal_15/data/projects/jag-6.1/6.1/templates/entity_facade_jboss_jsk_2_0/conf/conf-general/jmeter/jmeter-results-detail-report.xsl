<!--

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

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html" indent="yes" encoding="US-ASCII" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN" />

<xsl:template match="testResults">
	<html>
		<head>
			<title>Load Test Results</title>
			<style type="text/css">
				body {
					font:normal 68% verdana,arial,helvetica;
					color:#000000;
				}
				table tr td, table tr th {
					font-size: 68%;
				}
				table.details tr th{
					font-weight: bold;
					text-align:left;
					background:#a6caf0;
					white-space: nowrap;
				}
				table.details tr td{
					background:#eeeee0;
					white-space: nowrap;
				}
				h1 {
					margin: 0px 0px 5px; font: 165% verdana,arial,helvetica
				}
				h2 {
					margin-top: 1em; margin-bottom: 0.5em; font: bold 125% verdana,arial,helvetica
				}
				h3 {
					margin-bottom: 0.5em; font: bold 115% verdana,arial,helvetica
				}
				.Failure {
					font-weight:bold; color:red;
				}
				
	
				img
				{
				  border-width: 0px;
				}
				
				.expand_link
				{
				   position=absolute;
				   right: 0px;
				   width: 27px;
				   top: 1px;
				   height: 27px;
				}
				
				.page_details
				{
				   position=relative;
				   top: 0px;
				   height: 0px;
				   background-color: "#BBBBB0";
				   visibility: visible;
				   overflow: hidden;
				}


			</style>
			<script language="JavaScript">
			   function expand(details_id)
			   {
			      
			      document.getElementById(details_id).style.overflow = "visible";
			   }
			   
			   function collapse(details_id)
			   {
			      
			      document.getElementById(details_id).style.overflow = "hidden";
			   }
			   
			   function change(details_id)
			   {
			      if(document.getElementById(details_id+"_image").src.match("expand"))
			      {
			         document.getElementById(details_id+"_image").src = "collapse.jpg";
			         expand(details_id);
			      }
			      else
			      {
			         document.getElementById(details_id+"_image").src = "expand.jpg";
			         collapse(details_id);
			      } 
                           }
			</script>
		</head>
		<body>
		
			<xsl:call-template name="pageHeader" />
			
			<xsl:call-template name="summary" />
			<hr size="1" width="95%" align="left" />
			
			<xsl:call-template name="pagelist" />
			<hr size="1" width="95%" align="left" />
			
			<xsl:call-template name="detail" />

		</body>
	</html>
</xsl:template>

<xsl:template name="pageHeader">
	<h1>Load Test Results</h1>
	<table width="100%">
		<tr>
			<td align="left"></td>
			<td align="right">Designed for use with <a href="http://jakarta.apache.org/jmeter">JMeter</a> and <a href="http://ant.apache.org">Ant</a>.</td>
		</tr>
	</table>
	<hr size="1" />
</xsl:template>

<xsl:template name="summary">
	<h2>Summary</h2>
	<table class="details" border="0" cellpadding="5" cellspacing="2" width="95%">
		<tr valign="top">
			<th>Tests</th>
			<th>Failures</th>
			<th>Success Rate</th>
			<th>Average Time</th>
			<th>Min Time</th>
			<th>Max Time</th>
		</tr>
		<tr valign="top">
			<xsl:variable name="allCount" select="count(/testResults/sampleResult)" />
			<xsl:variable name="allFailureCount" select="count(/testResults/sampleResult[attribute::success='false'])" />
			<xsl:variable name="allSuccessCount" select="count(/testResults/sampleResult[attribute::success='true'])" />
			<xsl:variable name="allSuccessPercent" select="$allSuccessCount div $allCount" />
			<xsl:variable name="allTotalTime" select="sum(/testResults/sampleResult/@time)" />
			<xsl:variable name="allAverageTime" select="$allTotalTime div $allCount" />
			<xsl:variable name="allMinTime">
				<xsl:call-template name="min">
					<xsl:with-param name="nodes" select="/testResults/sampleResult/@time" />
				</xsl:call-template>
			</xsl:variable>
			<xsl:variable name="allMaxTime">
				<xsl:call-template name="max">
					<xsl:with-param name="nodes" select="/testResults/sampleResult/@time" />
				</xsl:call-template>
			</xsl:variable>
			<xsl:attribute name="class">
				<xsl:choose>
					<xsl:when test="$allFailureCount &gt; 0">Failure</xsl:when>
				</xsl:choose>
			</xsl:attribute>
			<td>
				<xsl:value-of select="$allCount" />
			</td>
			<td>
				<xsl:value-of select="$allFailureCount" />
			</td>
			<td>
				<xsl:call-template name="display-percent">
					<xsl:with-param name="value" select="$allSuccessPercent" />
				</xsl:call-template>
			</td>
			<td>
				<xsl:call-template name="display-time">
					<xsl:with-param name="value" select="$allAverageTime" />
				</xsl:call-template>
			</td>
			<td>
				<xsl:call-template name="display-time">
					<xsl:with-param name="value" select="$allMinTime" />
				</xsl:call-template>
			</td>
			<td>
				<xsl:call-template name="display-time">
					<xsl:with-param name="value" select="$allMaxTime" />
				</xsl:call-template>
			</td>
		</tr>
	</table>
</xsl:template>

<xsl:template name="pagelist">
	<h2>Pages</h2>
	<table class="details" border="1" bordercolor="#FFFFFF" cellpadding="0" cellspacing="0" width="95%">
		<tr valign="top">
			<th>URL</th>
			<th>Tests</th>
			<th>Failures</th>
			<th>Success Rate</th>
			<th>Average Time</th>
			<th>Min Time</th>
			<th>Max Time</th>
			<th></th>
		</tr>
		<xsl:for-each select="/testResults/sampleResult[not(@label = preceding::*/@label)]">
			<xsl:variable name="label" select="@label" />
			<xsl:variable name="count" select="count(../sampleResult[@label = current()/@label])" />
			<xsl:variable name="failureCount" select="count(../sampleResult[@label = current()/@label][attribute::success='false'])" />
			<xsl:variable name="successCount" select="count(../sampleResult[@label = current()/@label][attribute::success='true'])" />
			<xsl:variable name="successPercent" select="$successCount div $count" />
			<xsl:variable name="totalTime" select="sum(../sampleResult[@label = current()/@label]/@time)" />
			<xsl:variable name="averageTime" select="$totalTime div $count" />
			<xsl:variable name="minTime">
				<xsl:call-template name="min">
					<xsl:with-param name="nodes" select="../sampleResult[@label = current()/@label]/@time" />
				</xsl:call-template>
			</xsl:variable>
			<xsl:variable name="maxTime">
				<xsl:call-template name="max">
					<xsl:with-param name="nodes" select="../sampleResult[@label = current()/@label]/@time" />
				</xsl:call-template>
			</xsl:variable>
			<tr valign="top">
				<xsl:attribute name="class">
					<xsl:choose>
						<xsl:when test="$failureCount &gt; 0">Failure</xsl:when>
					</xsl:choose>
				</xsl:attribute>
				<td>
					<xsl:value-of select="$label" />
				</td>
				<td>
					<xsl:value-of select="$count" />
				</td>
				<td>
					<xsl:value-of select="$failureCount" />
				</td>
				<td>
					<xsl:call-template name="display-percent">
						<xsl:with-param name="value" select="$successPercent" />
					</xsl:call-template>
				</td>
				<td>
					<xsl:call-template name="display-time">
						<xsl:with-param name="value" select="$averageTime" />
					</xsl:call-template>
				</td>
				<td>
					<xsl:call-template name="display-time">
						<xsl:with-param name="value" select="$minTime" />
					</xsl:call-template>
				</td>
				<td>
					<xsl:call-template name="display-time">
						<xsl:with-param name="value" select="$maxTime" />
					</xsl:call-template>
				</td>
				<td align="center">
				   <a href="">
				      <xsl:attribute name="href"><xsl:text/>javascript:change('page_details_<xsl:value-of select="position()" />')</xsl:attribute>
				      <img src="expand.jpg" alt="expand/collapse"><xsl:attribute name="id"><xsl:text/>page_details_<xsl:value-of select="position()" />_image</xsl:attribute></img>				      
				   </a>
				</td>
			</tr>
			
                        <tr>
                           <td colspan="8">
                              <xsl:attribute name="bgcolor"><xsl:text/>"#FF0000"</xsl:attribute>
                              <div id="page_details_1" class="page_details" align="center">
				 <xsl:attribute name="id"><xsl:text/>page_details_<xsl:value-of select="position()" /></xsl:attribute>
			         <b>Details for Page "<xsl:value-of select="$label" />"</b>
			         <table bordercolor="#000000" border="1"  cellpadding="0" cellspacing="0" width="95%">
			         <tr>
			            <th>Thread</th>
			            <th>Iteration</th>
			            <th>Time</th>
			            <th>Success</th>
			            <!--
			            itterations = position() - thread stuff
			            time="312"
			            timeStamp="1053622047640"
			            responseMessage="OK"
			            threadName="Thread Group-1"
			            dataType="text"
			            label="/CansysAandA/processAuthentication.do"
			            success="true"
			            -->
			         </tr>
			         		         
			         <xsl:for-each select="../sampleResult[@label = $label and @threadName != $label]">			         			            
			            <tr>
			               <td><xsl:value-of select="@threadName" /></td>
			               <td><xsl:value-of select="position()" /></td>
			               <td><xsl:value-of select="@time" />ms</td>
			               <td><xsl:value-of select="@success" /></td>
			            </tr>
			         </xsl:for-each>
			         
			         </table>
			      </div>
                           </td>
                        </tr>
			
		</xsl:for-each>
	</table>
</xsl:template>

<xsl:template name="detail">
	<xsl:variable name="allFailureCount" select="count(/testResults/sampleResult[attribute::success='false'])" />

	<xsl:if test="$allFailureCount > 0">
		<h2>Failure Detail</h2>

		<xsl:for-each select="/testResults/sampleResult[not(@label = preceding::*/@label)]">

			<xsl:variable name="failureCount" select="count(../sampleResult[@label = current()/@label][attribute::success='false'])" />

			<xsl:if test="$failureCount > 0">
				<h3><xsl:value-of select="@label" /></h3>

				<table class="details" border="0" cellpadding="5" cellspacing="2" width="95%">
				<tr valign="top">
					<th>Response</th>
					<th>Failure Message</th>
				</tr>
			
				<xsl:for-each select="/testResults/sampleResult[@label = current()/@label][attribute::success='false']">
					<tr>
						<td><xsl:value-of select="@responseCode" /> - <xsl:value-of select="@responseMessage" /></td>
						<td><xsl:value-of select="assertionResult/@failureMessage" /></td>
					</tr>
				</xsl:for-each>
				
				</table>
			</xsl:if>

		</xsl:for-each>
	</xsl:if>
</xsl:template>

<xsl:template name="min">
	<xsl:param name="nodes" select="/.." />
	<xsl:choose>
		<xsl:when test="not($nodes)">NaN</xsl:when>
		<xsl:otherwise>
			<xsl:for-each select="$nodes">
				<xsl:sort data-type="number" />
				<xsl:if test="position() = 1">
					<xsl:value-of select="number(.)" />
				</xsl:if>
			</xsl:for-each>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="max">
	<xsl:param name="nodes" select="/.." />
	<xsl:choose>
		<xsl:when test="not($nodes)">NaN</xsl:when>
		<xsl:otherwise>
			<xsl:for-each select="$nodes">
				<xsl:sort data-type="number" order="descending" />
				<xsl:if test="position() = 1">
					<xsl:value-of select="number(.)" />
				</xsl:if>
			</xsl:for-each>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="display-percent">
	<xsl:param name="value" />
	<xsl:value-of select="format-number($value,'0.00%')" />
</xsl:template>

<xsl:template name="display-time">
	<xsl:param name="value" />
	<xsl:value-of select="format-number($value,'0 ms')" />
</xsl:template>
	
</xsl:stylesheet>