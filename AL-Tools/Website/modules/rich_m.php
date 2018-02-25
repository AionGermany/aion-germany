<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
echo '<table cellspacing="0" width="100%">';
echo '<tr align="center">';
echo '<td class="tophead"><b>'.$lang['place'].'</b></td> <td class="tophead"><b>'.$lang['name'].'</b></td> <td class="tophead"><b>'.
     $lang['kinah'].'</b></td> <td class="tophead"><b>'.$lang['level'].'</b></td> <td class="tophead"><b>'.$lang['race'].
     '</b></td> <td class="tophead"><b>'.$lang['sex'].'</b></td> <td class="tophead"><b>'.$lang['class'].'</b></td> <td class="tophead"><b>'.$lang['status'].'</b></td>';
echo '</tr>';

$bef = "SELECT inventory.item_id, inventory.item_count, inventory.item_owner, players.id, players.name, players.exp, players.gender, players.race, players.player_class, ".
       "players.world_id, players.online FROM inventory LEFT JOIN players ON item_owner = players.id WHERE inventory.item_id = 182400001 AND NOT players.name = '' ".
       "ORDER by item_count DESC LIMIT 100";
$res = $sql_gs->query($bef);
$i   = 1;

while ($row = $sql_gs->fetch_array($res))
{
	$money = $row['item_count'];
	require 'function.php';
	echo "
	    <tr align='center'>
			<td class='toptext'><font size='2'>".$i."</font></td>
	        <td class='toptext'><font size='2'><a href='info.php?action=info&id=".$row['id']."'>".$name."</a></font></td>
			<td class='toptext right'><font size='2'>".number_format($money,0, ' ','.')."</font></td>
			<td class='toptext'><font size='2'>".$exp."</font></td>
			<td class='toptext'><font size='2'>".$race."</font></td>
			<td class='toptext'><font size='2'>".$gender."</font></td>
			<td class='toptext'><font size='2'>".$player_class."</font></td>
			<td class='toptext'><font size='2'>".$online."</font></td>
	    </tr>";
	$i++;
}