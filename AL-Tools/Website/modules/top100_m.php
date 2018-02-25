<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
if (isset($_GET['class']) && $_GET['class'] != "No" && $_GET['class'] != "All") 
    $bef = "SELECT `id`,`name`,`exp`,`race`,`gender`,`player_class`,`online` FROM `players` WHERE `player_class` = '".strtoupper($_GET['class'])."' ORDER BY `exp` DESC LIMIT 100";
else
    $bef = "SELECT `id`,`name`,`exp`,`race`,`gender`,`player_class`,`online` FROM `players` WHERE `exp`>0 ORDER BY `exp` DESC LIMIT 100";

$res = $sql_gs->query($bef);
$i   = 1;

while ($row = $sql_gs->fetch_array($res))
{
	require 'function.php';
	
	echo '<tr align="center">';
	echo '<td class="toptext">'.$i.'</td>';
	echo '<td class="toptext"><a href="info.php?action=info&id='.$row['id'].'">'.$name.'</a></td>';
	echo '<td class="toptext">'.$exp.'</td>';
	echo '<td class="toptext">'.$race.'</td>';
	echo '<td class="toptext">'.$player_class.'</td>';
	echo '<td class="toptext">'.$gender.'</td>';
	echo '<td class="toptext">'.$online.'</td>';
	echo '</tr>';
	$i++;
}