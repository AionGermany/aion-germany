<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
// Eddit Lark (x714 serveraion.ru)

require "header.php";
?>
<div class="case">
	<div class="content">
		<div class="list">
			<div class="news-title"><?php echo $lang['infoaboutLegion']; ?></div>
<?php
$id  = intval($_GET['id']);
$bef = "SELECT players.name AS nick, legions.id, legions.name, legions.level, players.race, contribution_points FROM legions ".
       "LEFT JOIN legion_members ON legions.id = legion_members.legion_id LEFT JOIN players ON legion_members.player_id = players.id ".
       "WHERE legion_members.rank='BRIGADE_GENERAL' AND legions.id = '$id'";
$res = $sql_gs->query($bef);

while ($row = $sql_gs->fetch_assoc($res))
{
    $name = $row['name'];
    $nick = $row['nick'];
    $level = $row['level'];
    $contribution_points = $row['contribution_points'];
	
	require 'modules/function.php';
}

?>
<table width="100%">
	<tr height="32">
		<td class="toptext"><?php echo $lang['name']; ?></td>
		<td class="toptext"><?php if (isset($name)) echo $name; ?></td>
	</tr>
	<tr height="32">
		<td class="toptext"><?php echo $lang['legate']; ?></td>
		<td class="toptext"><?php if (isset($nick)) echo $nick; ?></td>
	</tr>
	<tr height="32">
		<td class="toptext"><?php echo $lang['level']; ?></td>
		<td class="toptext"><?php if (isset($level)) echo $level; ?></td>
	</tr>
	<tr height="32">
		<td class="toptext"><?php echo $lang['abysspoints']; ?></td>
		<td class="toptext"><?php if (isset($contribution_points)) echo number_format($contribution_points,0, ' ','.'); ?></td>
	</tr>
	<tr height="32">
		<td><?php echo $lang['race']; ?></td>
		<td><?php if (isset($race)) echo $race; ?></td>
	</tr>
</table>
<br/>
				<!-- Block List -->
				<div class="menu-button border shadow" ><center><b><?php echo $lang['listLegion']; ?></b></center>
					<?php require 'modules/legion_info.php';?>
				</div>
<div class="clear"></div>
		</div>
	</div>
	
<?php require "sidebar.php"; ?>
</div>

<?php require "footer.php"; ?>