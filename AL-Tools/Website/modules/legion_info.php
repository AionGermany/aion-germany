<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
// Eddit by Lark (x714 serveraion.ru)
?>
<div class="hr"></div>
<table cellspacing="0" width="100%">
	<tr align="center"> 
			<td class="tophead"><center><?php echo $lang['place']; ?></center></td>
			<td class="tophead"><center><?php echo $lang['name']; ?></center></td>
			<td class="tophead"><center><?php echo $lang['class']; ?></center></td>
			<td class="tophead"><center><?php echo $lang['sex']; ?></center></td>
			<td class="tophead"><center><?php echo $lang['level']; ?></center></td>
			<td class="tophead"><center><?php echo $lang['title']; ?></center></td>
			<td class="tophead"><center><?php echo $lang['location']; ?></center></td>
			<td class="tophead"><center><?php echo $lang['rank']; ?></center></td>
			<td class="tophead"><center><?php echo $lang['status']; ?></center></td>
	</tr>
<?php
$bef ="SELECT players.name,players.id, players.exp, players.gender, players.player_class, players.world_id, players.title_id, players.online,legion_members.rank ".
      "FROM legions LEFT JOIN legion_members ON legions.id = legion_members.legion_id LEFT JOIN players ON legion_members.player_id = players.id WHERE legions.id = '$id' ORDER BY exp DESC ";
$res = $sql_gs->query($bef);
$i   = 1;

while ($row = $sql_gs->fetch_assoc($res)) {
	require 'modules/function.php';
	echo '<tr align="center" height="32">';
	echo '<td class="toptext">'.$i++.'</td>';
	echo '<td class="toptext"><a href="info.php?action=info&id='.$row['id'].'">'.$name.'</a></td>';
	echo '<td class="toptext">'.$player_class.'</td>';
	echo '<td class="toptext">'.$gender.'</td>';
	echo '<td class="toptext">'.$exp.'</td>';
	echo '<td class="toptext">'.$title_id.'</td>';
	echo '<td class="toptext">'.$world_id.'</td>';
	echo '<td class="toptext">'.$rank_leg.'</td>';
	echo '<td class="toptext">'.$online.'</td>';
	echo '</tr>';
}
?>
</table>