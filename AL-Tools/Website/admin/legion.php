<?php 
if (!defined('SAWEB')) 
{
    die('hacking attempt!');
}
echo '<div class="menu-button border shadow">';
echo '<div class="news-title">Server Legions List	
			</div>
			
			<table cellspacing="0" width="100%">
				<tr align="center">
					<td class="tophead">ID</td>
					<td class="tophead">Name</td>
					<td class="tophead">Level</td>
					<td class="tophead">AP</td>
				</tr>';
if (isset($_GET['page'])) {
    $page = intval($_GET['page']);
} else {
    $page = 1;
}
$elements = $sql_gs->result($sql_gs->query('SELECT COUNT(*) AS `counter` FROM `legions`'),0);
$pages = ceil($elements / 50);

if ($page < 1) {
    $page = 1;
} elseif ($page > $pages) {
    $page = $pages;
}
$start = ($page - 1) * 50;
if ($start < 0) $start = 0;

$bef = "SELECT `id`,`name`,`level`, `contribution_points` FROM `legions`  ORDER BY `id` LIMIT $start, 50";
$res = $sql_gs->query($bef);
$i   = $start + 1;

while ($row = $sql_gs->fetch_array($res)) 
{
    echo '<tr align="center">';
    echo '<td class="toptext">' . $row['id'] . '</td>';
    echo '<td class="toptext"><a href="admin.php?mod=infoleg&action=info&id=' . $row['id'] . '">' . $row['name'] . '</a></td>';
    echo '<td class="toptext">' . $row['level'] . '</td>';
    echo '<td class="toptext">' . $row['contribution_points'] . '</td>';
    echo '</tr>';
}
echo '</table>
	</div>';
echo '<div class="pages">';
echo '<div class="paginator" id="paginator3"></div>';
echo '<script type="text/javascript"> pag3 = new Paginator_Legion(\'paginator3\', ' . $pages . ', 5, ' . $page . ', ""); </script>';
echo '</div>';;