<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
// Eddit Lark (x714 serveraion.ru)

if(isset($_POST['submit'])) {
	$Name = trim(mysql_escape_string($_POST["name"])); 
	$mail = trim($_POST['mail']); 
    
	if ($Name == NULL || $mail == NULL) {
		$error = $lang['fillallfields'];
	}
	else 
    {
        $cnt = $sql_ls->result($sql_ls->query("SELECT COUNT(*) FROM account_data WHERE name = '$Name' AND email = '$mail'"),0);
        
        if ($cnt == 1)
        {
            $newpassword = uniqid("");
            $mail_text = $lang['lostpassword'];
            // replacing the placeholders for Name = ~name~  and  new Password = ~newpassword~
            $mail_text = str_replace("~name~",$Name,$mail_text);
            $mail_text = str_replace("~newpassword~",$newpassword,$mail_text);
            
            mail("$mail", $lang['pwresetmail'], "$mail_text"); 
            
            $newpassword = base64_encode(sha1($newpassword, true));
            $sql_ls->query("UPDATE `account_data` SET `password` = '$newpassword' WHERE name = '$Name'");
            $success = $lang['pwsendok'];
        }
        else 
        {
            $error = $lang['pwnomatch'];
        }
	}
}