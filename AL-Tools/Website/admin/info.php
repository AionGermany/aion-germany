<?php 
$accounts_ban = $sql_ls->result($sql_ls->query("SELECT count(*) FROM account_data WHERE account_data.activated =  '0'"),0);

$sql_ws = new ClassMysql();
$sql_ws->connect($dbhost,$dbuser,$dbpassword,$dbnews);
$news = $sql_ws->result($sql_ws->query('SELECT count(*) FROM news'),0);
$sql_ws->close();

echo '<div class="menu-button border shadow" ><center><b>General information</b></center>
<div class="hr"></div>
<table width="100%">
		<tr height="32">
			<td class="toptext"><b>Script Version:</b></td>
			<td class="toptext">';
// echo $rev_ver;
echo '</td>
		</tr>
		<tr height="32">
			<td class="toptext"><b>Script Revision:</b></td>
			<td class="toptext">';
// echo $rev_src;
echo '</td>
		</tr>
		<tr height="32">
			<td class="toptext"><b>PHP Version:</b></td>
			<td class="toptext">';
echo PHP_VERSION;
echo '</td>
		</tr>
		<tr height="32">
			<td class="toptext"><b>MySql Version:</b></td>
			<td class="toptext">';
echo mysql_get_server_info();
echo '</td>
		</tr>
		<tr height="32">
			<td class="toptext"><b>News:</b></td>
			<td class="toptext">';
echo $news;
echo '</td>
		</tr>
		<tr height="32"><td Colspan=2><div class="hr"></div></td></tr>
		<tr height="32">
			<td class="toptext"><b>EXP Rate:</b></td>
			<td class="toptext">';
echo $rate_exp;
echo '</td>
		</tr>
		<tr height="32">
			<td class="toptext"><b>Kinah Rate:</b></td>
			<td class="toptext">';
echo $rate_kinah;
echo '</td>
		</tr>
		<tr height="32">
			<td class="toptext"><b>Drop Rate:</b></td>
			<td class="toptext">';
echo $rate_drop;
echo '</td>
		</tr>
		<tr height="32">
			<td class="toptext"><b>Quest EXP Rate:</b></td>
			<td class="toptext">';
echo $rate_quest;
echo '</td>
		</tr>
		<tr height="32"><td Colspan=2><div class="hr"></div></td></tr>
		<tr height="32">
			<td class="toptext"><b>Accounts:</b></td>
			<td class="toptext">';
echo $accounts_amt;
echo '</td>
		</tr>
		<tr height="32">
			<td class="toptext"><b>Blocked:</b></td>
			<td class="toptext">';
echo $accounts_ban;
echo '</td>
		</tr>
		<tr height="32">
			<td class="toptext"><b>Characters:</b></td>
			<td class="toptext">';
echo $players_amt;
echo '</td>
		</tr>
		<tr height="32">
			<td class="toptext"><b>GMs:</b></td>
			<td class="toptext">';
echo $gm_amt;
echo '</td>
		</tr>
		<tr height="32">
			<td class="toptext"><b>Legions:</b></td>
			<td class="toptext">';
echo $legions_amt;
echo '</td>
		</tr>
</table>
</div>';;