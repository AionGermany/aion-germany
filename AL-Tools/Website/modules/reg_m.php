<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
// Eddit Lark (x714 serveraion.ru)

if(isset($_POST['submit'])) 
{	
	if(!empty($_POST['name']) && !empty($_POST['pass1']) && !empty($_POST['pass2'])& !empty($_POST['mail']) && !empty($_POST['code'])) 
    {
		$code = strtolower($_POST['code']);
        
 		if($code == $_SESSION['rand_code']) 
        {
			if(!empty($_POST))
			{
				$name = $_POST["name"];
				$pass1 = $_POST["pass1"];
				$pass2 = $_POST["pass2"];
				$mail = trim($_POST["mail"]);
					
				if($pass1 != $pass2) 
                {
					$reg_error = "Passwords do not match";
				} 
                else
                {                
                    if ( !preg_match("/^([a-z0-9]([\-\_\.]*[a-z0-9])*)+@([a-z0-9]([\-]*[a-z0-9])*\.)+[a-z]{2,6}$/i", $mail) OR empty( $mail ) OR strlen( $mail ) > 50) 
                    {
                         $reg_error .= "Not correctly filled field E-Mail";
                    } 
                    else
                    {                         
                        $name  = trim(mysql_real_escape_string($_POST["name"]));
                        $pass1 = trim(mysql_real_escape_string($_POST["pass1"]));
                        $pass2 = trim(mysql_real_escape_string($_POST["pass2"]));
                        
                        $cnt = $sql_ls->result($sql_ls->query("SELECT count(*) FROM `account_data` WHERE `name`='$name'"),0);
                        
                        if($cnt > 0)
                        {
                            $reg_error = "Name busy";
                        }
                        else
                        {
                            $password = trim($pass1);
                            $password = base64_encode(sha1($password, true));
                            
                            $sql_ls->query("INSERT INTO `account_data` (`name`,`password`,`email`) VALUES ('$name','$password','$mail')");
                            
                            $reg_success = "Welcome, ".$name;
                        }
                    }
                }
            }
        }
        else 
        {	
            $reg_error = "Verification code does not match";
        }		
    }
    else 
    {
        $reg_error = "Fill in all fields";
    }
}