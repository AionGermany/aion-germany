<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)

if(isset($_POST['submit'])) {
	$UserName = trim(mysql_escape_string($_POST["name"])); 
	$PassAc = trim(mysql_escape_string($_POST['old_pass'])); 
	$NewPassAc = trim(mysql_escape_string($_POST['new_pass1'])); 
	$NewPassAc1 = trim(mysql_escape_string($_POST['new_pass2'])); 
	$OldPassAc = base64_encode(sha1($PassAc, true));
	$NewPass = base64_encode(sha1($NewPassAc, true));

	if ($NewPassAc != $NewPassAc1) 
    {
		$change_error = $lang['pwchangenomatch'];
	}
	else 
    {
        $res = $sql_ls->query("SELECT name, password FROM account_data WHERE name = '$UserName' AND password = '$OldPassAc'");

        if ($sql_ls->num_rows($res) > 0) 
        {
            $sql_ls->query("UPDATE `account_data` SET `password` = '$NewPass' WHERE name = '$UserName'");
            $change_success = $lang['pwchangeok'];
            
            if ($_SESSION['login_name'] == $UserName) 
            {
                //header("Location: logout.php");
                echo "<script language='JavaScript'>alert('".$lang['pwchangelogin']."'); window.location.href = 'logout.php'; </script>";
            }
        }
        else 
        {
            $change_error = $lang['pwnomatch'];
        }
	}
}