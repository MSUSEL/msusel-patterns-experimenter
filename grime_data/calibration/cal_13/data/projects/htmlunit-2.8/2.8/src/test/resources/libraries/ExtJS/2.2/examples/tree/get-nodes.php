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
<?
// from php manual page
function formatBytes($val, $digits = 3, $mode = "SI", $bB = "B"){ //$mode == "SI"|"IEC", $bB == "b"|"B"
   $si = array("", "K", "M", "G", "T", "P", "E", "Z", "Y");
   $iec = array("", "Ki", "Mi", "Gi", "Ti", "Pi", "Ei", "Zi", "Yi");
   switch(strtoupper($mode)) {
       case "SI" : $factor = 1000; $symbols = $si; break;
       case "IEC" : $factor = 1024; $symbols = $iec; break;
       default : $factor = 1000; $symbols = $si; break;
   }
   switch($bB) {
       case "b" : $val *= 8; break;
       default : $bB = "B"; break;
   }
   for($i=0;$i<count($symbols)-1 && $val>=$factor;$i++)
       $val /= $factor;
   $p = strpos($val, ".");
   if($p !== false && $p > $digits) $val = round($val);
   elseif($p !== false) $val = round($val, $digits-$p);
   return round($val, $digits) . " " . $symbols[$i] . $bB;
}

$dir = isset($_REQUEST['lib'])&&$_REQUEST['lib'] == 'yui' ? '../../../' : '../../';
$node = isset($_REQUEST['node'])?$_REQUEST['node']:"";
if(strpos($node, '..') !== false){
    die('Nice try buddy.');
}
$nodes = array();
$d = dir($dir.$node);
while($f = $d->read()){
    if($f == '.' || $f == '..' || substr($f, 0, 1) == '.')continue;
    $lastmod = date('M j, Y, g:i a',filemtime($dir.$node.'/'.$f));
    if(is_dir($dir.$node.'/'.$f)){
        $qtip = 'Type: Folder<br />Last Modified: '.$lastmod;
        $nodes[] = array('text'=>$f, 'id'=>$node.'/'.$f/*, 'qtip'=>$qtip*/, 'cls'=>'folder');
    }else{
        $size = formatBytes(filesize($dir.$node.'/'.$f), 2);
        $qtip = 'Type: JavaScript File<br />Last Modified: '.$lastmod.'<br />Size: '.$size;
        $nodes[] = array('text'=>$f, 'id'=>$node.'/'.$f, 'leaf'=>true/*, 'qtip'=>$qtip, 'qtipTitle'=>$f */, 'cls'=>'file');
    }
}
$d->close();
echo json_encode($nodes);
?>
