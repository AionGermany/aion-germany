<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
// Eddit Lark (x714 serveraion.ru)

require "header.php";
?>

<div class="case">
	<div class="content">
		<div class="list">
			<div class="news-title"><?php echo $lang['infoaboutPlayer']; ?></div>
<?php
$id  = intval($_GET['id']);
$res = $sql_gs->query("SELECT ap, gp, all_kill, rank, id, name, exp, world_id, gender, race, player_class, creation_date, last_online, title_id, online FROM abyss_rank LEFT JOIN players ON player_id = id WHERE id = '$id'");

while ($row = $sql_gs->fetch_assoc($res)) 
{
	$exps = $row['exp'];
	$creation_date = $row['creation_date'];
	$last_online = $row['last_online'];
	$ap = $row['ap'];
	$gp = $row['gp'];
	$rank = $row['rank'];
	$all_kill = $row['all_kill'];
	
	require 'modules/function.php';
}
?>
<table width="100%">
	<tr height="32">
		<td class="toptext"><?php echo $lang['name']; ?></td>
		<td class="toptext"><?php if (isset($name)) echo $name; ?></td>
	</tr>
	<tr height="32">
		<td class="toptext"><?php echo $lang['level']; ?></td>
		<td class="toptext"><?php if (isset($exp)) echo $exp; ?></td>
	</tr>
	<tr height="32">
		<td class="toptext"><?php echo $lang['experience']; ?></td>
		<td class="toptext"><?php if (isset($exps)) echo number_format($exps,0, ' ',' '); ?></td>
	</tr>
	<tr height="32">
		<td class="toptext"><?php echo $lang['abysspoints']; ?></td>
		<td class="toptext"><?php if (isset($ap)) echo number_format($ap,0, ' ',' '); ?></td>
	</tr>
	<tr height="32">
		<td class="toptext"><?php echo $lang['glorypoints']; ?></td>
		<td class="toptext"><?php if (isset($gp)) echo number_format($gp,0, ' ',' '); ?></td>
	</tr>	
	<tr height="32">
		<td class="toptext"><?php echo $lang['rank']; ?></td>
		<td class="toptext"><?php if (isset($rank)) echo $rank ?></td>
	</tr>
	<tr height="32">
		<td class="toptext"><?php echo $lang['kills']; ?></td>
		<td class="toptext"><?php if (isset($all_kill)) echo number_format($all_kill,0, ' ',' '); ?></td>
	</tr>
	<tr height="32">
		<td class="toptext"><?php echo $lang['race']; ?></td>
		<td class="toptext"><?php if (isset($race)) echo $race; ?></td>
	</tr>
	<tr height="32">
		<td class="toptext"><?php echo $lang['class']; ?></td>
		<td class="toptext"><?php if (isset($player_class)) echo $player_class; ?></td>
	</tr>
	<tr height="32">
		<td class="toptext"><?php echo $lang['sex']; ?></td>
		<td class="toptext"><?php if (isset($gender)) echo $gender; ?></td>
	</tr>
	<tr height="32">
		<td class="toptext"><?php echo $lang['status']; ?></td>
		<td class="toptext"><?php if (isset($online)) echo $online; ?></td>
	</tr>
	<tr height="32">
		<td class="toptext"><?php echo $lang['location']; ?></td>
		<td class="toptext"><?php if (isset($world_id)) echo $world_id; ?></td>
	</tr>
	<tr height="32">
		<td class="toptext"><?php echo $lang['title']; ?></td>
		<td class="toptext"><?php if (isset($title_id)) echo $title_id; ?></td>
	</tr>
	<tr height="32">
		<td class="toptext"><?php echo $lang['created']; ?></td>
		<td class="toptext"><?php if (isset($creation_date)) echo $creation_date; ?></td>
	</tr>
	<tr height="32">
		<td class="toptext"><?php echo $lang['lastVisit']; ?></td>
		<td class="toptext"><?php if (isset($last_online)) echo $last_online; ?></td>
	</tr>
</table>

<div class="clear"></div>
		</div>
	</div>
	
<?php require "sidebar.php"; ?>
</div>

<?php require "footer.php"; ?>