<?php if (!defined('SAWEB')) {
    die('hacking attempt!');
}
$id  = intval($_GET['id']);
$bef = "SELECT players.name AS nick, legions.name, legions.level, players.race, contribution_points FROM legions LEFT JOIN legion_members ON legions.id = legion_members.legion_id LEFT JOIN players ON legion_members.player_id = players.id WHERE legions.id='$id' AND legion_members.rank='BRIGADE_GENERAL'";
$res = $sql_gs->query($bef);

while ($row = $sql_gs->fetch_assoc($res)) 
{
    $name = $row['name'];
    $nick = $row['nick'];
    $level = $row['level'];
    $race = $row['race'];
    $contribution_points = $row['contribution_points'];
}

if (isset($_POST['submit'])) 
{
    if (isset($_POST['name']) && isset($_POST['level'])) 
    {
        $name = trim($_POST['name']);
        $level = trim($_POST['level']);
        
        $sql_gs->query( "UPDATE legions SET name = '$name', level = '$level' WHERE id = '$id'" );
        
        echo "<script language='JavaScript'>alert('These have been modified');</script>";
    }
}
echo '
<form method="POST" action="admin.php?mod=infoleg&action=info&id=' . $id . '">
<table width="100%">
	<tr height="32">
		<td class="toptext">Name:</td>
		<td class="toptext"><input type="text" name="name" value="' . $name . '"/></td>
	</tr>
	<tr height="32">
		<td class="toptext">Race:</td>
		<td class="toptext">' . $race . '</td>
	</tr>
	<tr height="32">
		<td class="toptext">Legate:</td>
		<td class="toptext">' . $nick . '</td>
	</tr>
	<tr height="32">
		<td class="toptext">Level:</td>
		<td class="toptext"><input type="text" name="level" value="' . $level . '"/></td>
	</tr>
	
	<tr>
		<td class="toptext">Legions AP:</td>
		<td class="toptext">' . number_format($contribution_points, 0, ' ', '.') . '</td>
	</tr>
	<tr>
	<td colspan="2" align="center"><input type="submit" class="button-submit dark-shadow" value="Submit" name="submit" ></td>
	</tr>
</table>
</form>
<div class="clear"></div>';;
echo '	';;