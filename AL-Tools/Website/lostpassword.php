<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
// Eddit Lark (x714 serveraion.ru)

require "header.php";

?>

<div class="case">
	<div class="content">
    	<div class="news">
			<div class="news-title">Password Reset</div>
			<div>		
				<table align="center">
					<form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST">
					<tr>
					<td width="150px">Login:</td>
					<td><input type="text" class="field-reg" name="name" ></td>
					</tr>
					<tr>
					<td width="150px">E-Mail:</td>
					<td><input type="text" class="field-reg" name="mail" ></td>
					</tr>
					<tr>
					<tr>
					<td colspan="2" align="center"><input type="submit" class="button-submit dark-shadow" value="Recover Password" name="submit" ></td>
					</tr>
					</form>
				</table>
				<?php require "modules/lostpassword_m.php"; ?>
				<?php if(!empty($error)) echo '<div class="error" align="center">'.$error.'</div>'; ?>
				<?php if(!empty($success)) echo '<div class="accept" align="center">'.$success.'</div>'; ?>
			</div>
		</div>
	</div>

<?php require "sidebar.php"; ?>
</div>

<?php require "footer.php"; ?>




    
