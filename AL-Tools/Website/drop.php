<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)

require "header.php";
require "modules/drop_m.php";

$npcid = isset($_GET['id']) ? $_GET['id'] : "";
?>

<div class="case">
	<div class="content">
    	<div class="news">
			<div class="news-title"><center>Modify Drops</center></div>
			<div>		
				<table align="center">
					<form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST">
					<tr>
					<td width="150px">NPC ID:</td>
					<td><input type="text" class="field-reg" name="npcId" value="<?php echo $npcid; ?>" ></td>
					</tr>
					<tr>
					<td colspan="2" align="center"><input type="submit" class="button-submit dark-shadow" value="Search" name="submit" ></td>
					</tr>
					</form>
				</table>

				<?php if(!empty($error))   echo '<div class="error" align="center">'.$error.'</div>'; ?>
				<?php if(!empty($success)) echo '<div>'.$success.'</div>'; ?>
                
                <br><br>
                <center>There are some testdata for Npc`s <font color=red>210758</font> and <font color=red>210760</font> !</center>
			</div>
		</div>
	</div>

<?php require "sidebar.php"; ?>
</div>

<?php require "footer.php"; ?>