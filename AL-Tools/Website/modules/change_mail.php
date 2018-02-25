<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
// Eddit Lark (x714 serveraion.ru)

if(isset($_POST['submit'])) {
	$UserName = trim(mysql_escape_string($_POST["name"])); 
	$old_mail = trim($_POST['old_mail']); 
	$New_mail = trim($_POST['new_mail']); 

	if ($old_mail == $New_mail) 
    {
		$change_error = $lang['emailoldnew'];
	}
	else if( !preg_match("/^([a-z0-9]([\-\_\.]*[a-z0-9])*)+@([a-z0-9]([\-]*[a-z0-9])*\.)+[a-z]{2,6}$/i", $New_mail) OR empty( $New_mail ) OR strlen( $New_mail ) > 50) {
		$change_error = $lang['emailformat'];
	}
	else 
    {
	    $cnt = $sql_ls->query = "SELECT count(*) FROM account_data WHERE name = '$UserName' AND email = '$old_mail'";
    
		if ($cnt == 1) 
        {
			$sql_ls->query("UPDATE `account_data` SET `email` = '$New_mail' WHERE name = '$UserName'");
			$change_success = "E-Mail successfully changed";
		}
		else 
        {
			$change_error = $lang['emailnotfound'];
		}
	}
}