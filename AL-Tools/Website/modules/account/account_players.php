<?php 
echo '<div class="hr"></div>
<table cellspacing="0" width="100%">
	<tr align="center"> 
			<td class="tophead"><center>Race</center></td>
			<td class="tophead"><center>Nick</center></td>
			<td class="tophead"><center>Class</center></td>
			<td class="tophead"><center>Sex</center></td>
			<td class="tophead"><center>Level</center></td>
			<td class="tophead"><center>Title</center></td>
			<td class="tophead"><center>Location</center></td>
	</tr>
';
$res = $sql_gs->query("SELECT id, name, exp, gender, race, player_class, world_id, creation_date, last_online, title_id FROM players WHERE account_id ='$id'");

while ($row = $sql_gs->fetch_assoc($res)) 
{
    require 'modules/function.php';
    
    echo '<tr align="center" height="32">';
    echo '<td class="toptext">' . $race . '</td>';
    echo '<td class="toptext"><a href="info.php?action=info&id=' . $row['id'] . '">' . $name . '</a></td>';
    echo '<td class="toptext">' . $player_class . '</td>';
    echo '<td class="toptext">' . $gender . '</td>';
    echo '<td class="toptext">' . $exp . '</td>';
    echo '<td class="toptext">' . $title_id . '</td>';
    echo '<td class="toptext">' . $world_id . '</td>';
    echo '</tr>';
};
echo '</table>';;