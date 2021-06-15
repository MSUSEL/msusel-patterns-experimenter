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
mysql_pconnect("localhost", "root", "") or die("Could not connect");
mysql_select_db("demo") or die("Could not select database");

$start = ($_REQUEST["start"] == null)? 0 : $_REQUEST["start"];
$count = ($_REQUEST["limit"] == null)? 20 : $_REQUEST["limit"];
$sort = ($_REQUEST["sort"] == null)? "" : $_REQUEST["sort"];
$dir = ($_REQUEST["dir"] == "desc")? "DESC" : "";
$filter = $_REQUEST["filter"];

$where = " 0 = 0 ";
if (is_array($filter)) {
	for ($i=0;$i<count($filter);$i++){
		switch($filter[$i]['data']['type']){
			case 'string' : $qs .= " AND ".$filter[$i]['field']." LIKE '%".$filter[$i]['data']['value']."%'"; Break;
			case 'list' : 
				if (strstr($filter[$i]['data']['value'],',')){
					$fi = explode(',',$filter[$i]['data']['value']);
					for ($q=0;$q<count($fi);$q++){
						$fi[$q] = "'".$fi[$q]."'";
					}
					$filter[$i]['data']['value'] = implode(',',$fi);
					$qs .= " AND ".$filter[$i]['field']." IN (".$filter[$i]['data']['value'].")"; 
				}else{
					$qs .= " AND ".$filter[$i]['field']." = '".$filter[$i]['data']['value']."'"; 
				}
			Break;
			case 'boolean' : $qs .= " AND ".$filter[$i]['field']." = ".($filter[$i]['data']['value']); Break;
			case 'numeric' : 
				switch ($filter[$i]['data']['comparison']) {
					case 'eq' : $qs .= " AND ".$filter[$i]['field']." = ".$filter[$i]['data']['value']; Break;
					case 'lt' : $qs .= " AND ".$filter[$i]['field']." < ".$filter[$i]['data']['value']; Break;
					case 'gt' : $qs .= " AND ".$filter[$i]['field']." > ".$filter[$i]['data']['value']; Break;
				}
			Break;
			case 'date' : 
				switch ($filter[$i]['data']['comparison']) {
					case 'eq' : $qs .= " AND ".$filter[$i]['field']." = '".date('Y-m-d',strtotime($filter[$i]['data']['value']))."'"; Break;
					case 'lt' : $qs .= " AND ".$filter[$i]['field']." < '".date('Y-m-d',strtotime($filter[$i]['data']['value']))."'"; Break;
					case 'gt' : $qs .= " AND ".$filter[$i]['field']." > '".date('Y-m-d',strtotime($filter[$i]['data']['value']))."'"; Break;
				}
			Break;
		}
	}	
	$where .= $qs;
}

$query = "SELECT * FROM demo WHERE ".$where;
if ($sort != "") {
	$query .= " ORDER BY ".$sort." ".$dir;
}
$query .= " LIMIT ".$start.",".$count;

$rs = mysql_query($query);
$total = mysql_query("SELECT COUNT(id) FROM demo WHERE ".$where);
$total = mysql_result($total, 0, 0);

$arr = array();
while($obj = mysql_fetch_object($rs)) {
	$arr[] = $obj;
}

echo '{"total":"'.$total.'","data":'.json_encode($arr).'}';
?>
