<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
require "header.php";
?>

<div class="case">
	<div class="content">
		<div class="list">
			<div class="news-title"><center><?php echo $lang['onlineplayers']; ?></center></div>
			<table cellspacing="0" width="100%">
				<tr align="center">
					<td class="tophead"><?php echo $lang['name']; ?></td>
					<td class="tophead"><?php echo $lang['location']; ?></td>
					<td class="tophead"><?php echo $lang['level']; ?></td>
					<td class="tophead"><?php echo $lang['race']; ?></td>
					<td class="tophead"><?php echo $lang['class']; ?></td>
					<td class="tophead"><?php echo $lang['sex']; ?></td>
				</tr>
				<?php require "modules/online_m.php";?>
			</table>
		</div>
	</div>
    
<?php require "sidebar.php"; ?>
</div>

<?php require "footer.php"; ?>