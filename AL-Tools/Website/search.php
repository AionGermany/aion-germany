<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)

require "header.php";
require "modules/search_m.php";
?>

<div class="case">
	<div class="content">
    	<div class="news">
			<div class="news-title"><center>Search Character</center></div>
			<div>		
				<table align="center">
					<form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST">
					<tr>
					<td width="150px">Character Name:</td>
					<td><input type="text" class="field-reg" name="name" ></td>
					</tr>
					<tr>
					<td colspan="2" align="center"><input type="submit" class="button-submit dark-shadow" value="Search" name="submit" ></td>
					</tr>
					</form>
				</table>

				<?php if(!empty($error)) echo '<div class="error" align="center">'.$error.'</div>'; ?>
				<?php if(!empty($success)) echo '<div>'.$success.'</div>'; ?>
			</div>
		</div>
	</div>

<?php require "sidebar.php"; ?>
</div>

<?php require "footer.php"; ?>