<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
// Eddit by Lark (x714 serveraion.ru)

$bef = "SELECT players.name AS nick, legions.id, legions.name, legions.level, players.race, contribution_points FROM legions ".
       "LEFT JOIN legion_members ON legions.id = legion_members.legion_id LEFT JOIN players ON legion_members.player_id = players.id ".
       "WHERE legion_members.rank='BRIGADE_GENERAL' ORDER BY  contribution_points DESC";
$leg = "SELECT count(*) FROM legions LEFT JOIN legion_members ON legions.id = legion_members.legion_id LEFT JOIN players ON legion_members.player_id = players.id WHERE legion_members.legion_id='";
$res = $sql_gs->query($bef);
$i   = 1;

while ($row = $sql_gs->fetch_assoc($res))
{
    require 'function.php';
    $name = $row['name'];
    $nick = $row['nick'];
    $level = $row['level'];
    $contribution_points = $row['contribution_points'];

    $rs = $sql_gs->result($sql_gs->query($leg.$row['id']."'"),0);
    echo '<tr align="center" height="32">';
    echo '<td class="toptext">'.$i++.'</td>';
    echo '<td class="toptext">'.$race.'</td>';
    echo '<td class="toptext"><a href="info_leg.php?action=info&id='.$row['id'].'">'.$name.'</a></td>';
    echo '<td class="toptext">'.$nick.'</td>';      
    echo '<td class="toptext">'.$rs[0].'</td>';
    echo '<td class="toptext">'.$level.'</td>';
    echo '<td class="toptext right">'.number_format($contribution_points,0, ' ','.').'&nbsp;</td>';
    echo '</tr>';
}