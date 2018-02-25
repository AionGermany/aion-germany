<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
require "header.php";

?>

<div class="case">
	<div class="content">
    	<div class="news">
			<div class="news-title">Change Password</div>
			<div>		
				<table align="center">
					<form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST">
					<tr>
					<td width="150px">Login:</td>
					<td><input type="text" class="field-reg" name="name" ></td>
					</tr>
					<tr>
					<td width="150px">Old Password:</td>
					<td><input type="password" class="field-reg" name="old_pass" ></td>
					</tr>
					<tr>
					<tr>
					<td width="150px">New Password:</td>
					<td><input type="password" class="field-reg" name="new_pass1" ></td>
					</tr>
					<tr>
					<td width="150px">Repeat Password:</td>
					<td><input type="password" class="field-reg" name="new_pass2"></td>
					</tr>
					<tr>
					<td colspan="2" align="center"><input type="submit" class="button-submit dark-shadow" value="Change" name="submit" ></td>
					</tr>
					</form>
				</table>
				<?php require "modules/change_m.php"; ?>
				<?php if(!empty($change_error)) echo '<div class="error" align="center">'.$change_error.'</div>'; ?>
				<?php if(!empty($change_success)) echo '<div class="accept" align="center">'.$change_success.'</div>'; ?>
			</div>
		</div>
	</div>

<?php require "sidebar.php"; ?>
</div>

<?php require "footer.php"; ?>