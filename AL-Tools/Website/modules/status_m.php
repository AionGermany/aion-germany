<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)

$game = @fsockopen($server, $portgame, $errno, $errstr, $timeout);
$login = @fsockopen($server, $portlogin, $errno, $errstr, $timeout);

// new Mysql-Class initialization for each database
require "classes/classmysql.php"; 

$sql_ls = new ClassMysql();
$sql_gs = new ClassMysql();

if (!$sql_gs->connect($dbhost,$dbuser,$dbpassword,$gs)) echo $sql_gs->error(); 
if (!$sql_ls->connect($dbhost,$dbuser,$dbpassword,$ls)) echo $sql_ls->error(); 

$rscount = $sql_gs->result($sql_gs->query("SELECT count(*) FROM `players` WHERE `online`=1"),0);

if ($rscount >=   0) {$playersonline = "<font color=white>" .$rscount. "</font>";}
if ($rscount >= 100) {$playersonline = "<font color=yellow>" .$rscount. "</font>";}
if ($rscount >= 200) {$playersonline = "<font color=orange>" .$rscount. "</font>";}
if ($rscount >= 300) {$playersonline = "<font color=red>" .$rscount. "</font>";}
if ($rscount >= 400) {$playersonline = "<font color=magenta>" .$rscount. "</font>";}

$gmonline  = "";
$query_gm  = "SELECT players.name FROM players INNER JOIN $ls.account_data ON players.account_id = $ls.account_data.id WHERE $ls.account_data.access_level >  '0' AND players.online = '1'";
$result_gm = $sql_gs->query($query_gm);

if ($sql_gs->num_rows($result_gm) > 0)
{
    while ($count = $sql_gs->fetch_array($result_gm))
    {
        $gmonline .= $count['name'];
    }
}
else
    $gmonline .= $lang['nogmonline'];
$sql_gs->free_result($result_gm);

$accounts_amt = $sql_ls->result($sql_ls->query("SELECT count(*) FROM $ls.account_data"),0); 
$players_amt  = $sql_gs->result($sql_gs->query("SELECT count(*) FROM players"),0);
$legions_amt  = $sql_gs->result($sql_gs->query("SELECT count(*) FROM legions"),0);
$gm_amt       = $sql_ls->result($sql_ls->query("SELECT count(*) FROM $ls.account_data WHERE access_level > 0"),0);