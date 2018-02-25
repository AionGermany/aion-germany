<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
// Eddit Lark (x714 serveraion.ru)

if (!isset($_SESSION['id'])) {
	echo 
	"<form method='POST' action=\"".$_SERVER['PHP_SELF']."\">
        <table>
            <colgroup>
                <col style='width:100%;'>
            </colgroup>
            <tr>
                <td><input class='logintext' type='text' name='name' value='".$lang['loginName']."' size='40' onBlur='if(this.value==\"\")this.value=\"".$lang['loginName']."\"' onFocus='if(this.value==\"".$lang['loginName']."\")this.value=\"\"' ></td>
            </tr>
            <tr>
                <td><input class='logintext' type='text' name='password' value='".$lang['loginPassword']."' size='40' onBlur='if(this.value==\"\"){this.value=\"".$lang['loginPassword']."\";this.type=\"text\"}' onFocus='if(this.value==\"".$lang['loginPassword']."\"){this.value=\"\";this.type=\"password\"}' ></td>
            </tr>
        </table>
        <table>
            <colgroup>
                <col style='width:195px;'>
                <col style='width:*;'>
            </colgroup>
            <tr><td colspan=2>&nbsp;</td></tr>
            <tr>
                <td>              
                        <span class='info'>?</span><a href='lostpassword.php'>".$lang['forgotPassword']."</a><br> 
                </td>
                <td align=center rowspan=2>
                    <input type='submit' name='enter' class='button-submit dark-shadow' value='".$lang['login']."'>
                </td>
            </tr>
            <tr>
              <td>
                        <span class='info'>?</span><a href='reg.php'>".$lang['createAccount']."</a>   
              </td>
            </tr>
        </table>
	</form>";
	if (isset($_POST['enter']))
    {
		$name = trim($_POST["name"]);
		$password = trim($_POST["password"]);
		$password = base64_encode(sha1($password, true));
		
		if (!empty($_POST['name']) && !empty($_POST['password']))
        {
			$res = $sql_ls->query("SELECT id,name,password,access_level from account_data where name='$name' and password='$password'");
            
			if($sql_ls->num_rows($res) == 1) {
				$user = array() ;
				$user = $sql_ls->fetch_row($res) ;
				$_SESSION['id'] = session_id();
				$_SESSION['id_account'] = $user[0];
				$_SESSION['login_name'] = $user[1];
				$_SESSION['login_password'] = $user[2];
				$_SESSION['access_level'] = $user[3];
                
				echo "<script language='JavaScript'>window.location.href = 'index.php'; </script>";
			}
			if ($result['password'] !== $password) {
				$error_login = "Incorrect Username or Password";
			}
		}
		else {
			$error_login = "Fill in all fields";
		}
		echo '<div class="error" align="center">'.$error_login.'</div>';
	}
}

if (isset($_SESSION['id'])) {
	echo '<div class="dot">'.$lang['loggedinas'].' <b>'.$_SESSION['login_name'].'</b></div>';
	if (isset($_SESSION['id']) AND $_SESSION['access_level'] >= "$access_level") {
		echo '<div class="dot"><img src="img/admin.png" class="ico"><a href="admin.php"> &nbsp;'.$lang['addministratorAccount'].'</a></div>';
		echo '<div class="dot"><img src="img/drop.png" class="ico"><a href="drop.php"> &nbsp;'.$lang['addDrops'].'</a></div>';
		echo '<div class="dot"><img src="img/add.png" class="ico"><a href="index.php?action=addnews"> &nbsp;'.$lang['addnews'].'</a></div>';
	}
	echo '<div class="dot"><img src="img/user.png" class="ico"><a href="account.php"> &nbsp;Account</a></div>';
	echo '<div class="dot"><img src="img/pass.png" class="ico"><a href="change.php"> &nbsp;'.$lang['changepassword'].'</a></div>';
	echo '<div class="dot"><img src="img/e-mail.png" class="ico"><a href="change_mail.php"> &nbsp;'.$lang['changeemail'].'</a></div>';
	echo '<div class="dot"><img src="img/exit.png" class="ico"><a href="logout.php"> &nbsp;'.$lang['exit'].'</a></div>';
}