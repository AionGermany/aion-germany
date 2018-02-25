<!-- ServerAion Web by Pr00f & Sky (serveraion.ru) -->

<div class="news-title"><?php echo $lang['top10players']; ?></div>
<table cellspacing="0" width="100%">
	<tr align="center">
		<td class="tophead"><?php echo $lang['place']; ?></td>
		<td class="tophead"><?php echo $lang['name']; ?></td>
		<td class="tophead"><?php echo $lang['level']; ?></td>   
		<td class="tophead">Ap</td>   		
		<td class="tophead"><?php echo $lang['race']; ?></td>
		<td class="tophead"><?php echo $lang['class']; ?></td>
		<td class="tophead"><?php echo $lang['sex']; ?></td>
		<td class="tophead"><?php echo $lang['status']; ?></td>
	</tr>
	<?php require "modules/top10_m.php"; ?>
</table>