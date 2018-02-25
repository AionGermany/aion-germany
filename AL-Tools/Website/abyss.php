<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
require "header.php";
?>

<div class="case">
	<div class="content">
		<div class="list">
			<div class="rankabyss"><center><?php echo $lang['ratingAbyssGp']; ?></center></div>
			<table cellspacing="0" width="100%">
				<tr align="center">
					<td class="tophead"><?php echo $lang['place']; ?></td>
					<td class="tophead"><?php echo $lang['name']; ?></td>
					<td class="tophead"><?php echo $lang['level']; ?></td>        
					<td class="tophead"><?php echo $lang['race']; ?></td>
					<td class="tophead"><?php echo $lang['class']; ?></td>
					<td class="tophead"><?php echo $lang['sex']; ?></td>
					<td class="tophead"><?php echo $lang['glorypoints']; ?></td>
					<td class="tophead"><?php echo $lang['rank']; ?></td>
					<td class="tophead"><?php echo $lang['kills']; ?></td>
				</tr>
				<?php require "modules/abyssgp_m.php";?>
			</table>
		</div>
		<div class="list">
			<div class="rankabyss"><center><?php echo $lang['ratingAbyss']; ?></center></div>
			<table cellspacing="0" width="100%">
				<tr align="center">
					<td class="tophead"><?php echo $lang['place']; ?></td>
					<td class="tophead"><?php echo $lang['name']; ?></td>
					<td class="tophead"><?php echo $lang['level']; ?></td>        
					<td class="tophead"><?php echo $lang['race']; ?></td>
					<td class="tophead"><?php echo $lang['class']; ?></td>
					<td class="tophead"><?php echo $lang['sex']; ?></td>
					<td class="tophead"><?php echo $lang['abysspoints']; ?></td>
					<td class="tophead"><?php echo $lang['rank']; ?></td>
					<td class="tophead"><?php echo $lang['kills']; ?></td>
				</tr>
				<?php require "modules/abyssap_m.php";?>
			</table>
		</div>
	</div>
    
<?php require "sidebar.php"; ?>
</div>

<?php require "footer.php"; ?>