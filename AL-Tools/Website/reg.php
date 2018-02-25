<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
// Eddit Lark (x714 serveraion.ru)

require "header.php";
require "modules/reg_m.php";
?>

<div class="case">
	<div class="content">
    	<div class="news">
			<div class="news-title"><center>Account Registration</center></div>
			<div>		
				<table align="center">
					<form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST">
					<tr>
					<td width="150px">Login:</td>
					<td><input type="text" class="field-reg" name="name" ></td>
					</tr>
					<tr>
					<td width="150px">Password:</td>
					<td><input type="password" class="field-reg" name="pass1" ></td>
					</tr>
					<tr>
					<td width="150px">Repeat Password:</td>
					<td><input type="password" class="field-reg" name="pass2"></td>
					</tr>
					<td width="150px">Your E-Mail:</td>
					<td><input type="text" class="field-reg" name="mail"></td>
					</tr>
					<tr>
					<td width="150px"><img src="modules/captcha.php"/></td>
					<td><input type="text" class="field-reg" name="code" /></td>
					</tr>
					<tr>
					<td colspan="2" align="center"><input type="submit" class="button-submit dark-shadow" value="Create" name="submit" ></td>
					</tr>
					</form>
				</table>

				<?php if(!empty($reg_error)) echo '<div class="error" align="center">'.$reg_error.'</div>'; ?>
				<?php if(!empty($reg_success)) echo '<div class="accept" align="center">'.$reg_success.'</div>'; ?>
			</div>
		</div>
	</div>

<?php require "sidebar.php"; ?>
</div>

<?php require "footer.php"; ?>