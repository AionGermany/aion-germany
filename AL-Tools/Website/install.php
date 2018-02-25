<!-- // ServerAion Web by Pr00f & Sky (serveraion.ru) -->
<!-- // Eddit Lark (x714 serveraion.ru) -->

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ServerAion Web</title>
<LINK href="img/favicon.png" rel="shortcut icon" />
<LINK href="style/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="style/tooltip.js"></script>
<!-- Page Navigation -->
<LINK rel="stylesheet" type="text/css" href="style/page.css" />
<script type="text/javascript" src="style/page.js"></script>
<!-- BB code -->
<LINK rel="stylesheet" type="text/css" href="style/bbcodes.css" />
<script type="text/javascript" src="style/jquery.min.js"></script>
<script type="text/javascript" src="style/jquery.cleditor.min.js"></script>
<!-- Knowledge Base -->
<script type="text/javascript" src="style/aiondatabase.js"></script>
<script type="text/javascript" src="style/aionyg.js"></script>
</head>
<body>

<div class="case" style="margin-top:170px;">
	<div class="list">

<?php
if (file_exists("config.php")) {
	exit("<script lanuage='JavaScript'>alert('file config.php already exists. Remove file or change settings manually');</script>");
	Header("Location: index.php");
}
?>

<form method="POST" action="install.php">
<center><b>Before installing complete the following fields:</b></center>
<table align="center">
	<tr>
	<td colspan="2" align="center"><b>MySQL Connecting</b></td>
	</tr>
	<tr>
	<td><input type="text" name="dbhost" value="localhost" onClick="this.value=''" /></td>
	<td>MySQL Server Address</td>
	</tr>
	<tr>
	<td><input type="text" name="dbuser" value="root" onClick="this.value=''" /></td>
	<td>MySQL Administrator Name</td>
	</tr>
	<tr>
	<td><input type="text" name="dbpassword" value="" onClick="this.value=''" /></td>
	<td>MySQL Administrator Password</td>
	</tr>
	
	<tr>
	<td colspan="2" align="center"><b>Connection to the Game Server</b></td>
	</tr>
	<tr>
	<td><input type="text" name="gs" value="al_server_gs" onClick="this.value=''" /></td>
	<td>Game Database Server, by default "al_server_gs"</td>
	</tr>
	<tr>
	<td><input type="text" name="ls" value="al_server_ls" onClick="this.value=''" /></td>
	<td>Database Server Login, by default "al_server_ls"</td>
	</tr>
	<tr>
	<td><input type="text" name="server" value="127.0.0.1" onClick="this.value=''" /></td>
	<td>Address of the Server to connect</td>
	</tr>
	<tr>
	<td><input type="text" name="portgame" value="7777" onClick="this.value=''" /></td>
	<td>Game Server Port. by default 7777</td>
	</tr>
	<tr>
	<td><input type="text" name="portlogin" value="2106" onClick="this.value=''" /></td>
	<td>Login Server Port. by default 2106</td>
	</tr>
	<tr>
	<td><input type="text" name="timeout" value="10" onClick="this.value=''" /></td>
	<td>Connection Timeout</td>
	</tr>
	<tr>
	<td><input type="text" name="address" value="IP: 127.0.0.1" onClick="this.value=''" /></td>
	<td>Server Name</td>
	</tr>

	<tr>
	<td colspan="2" align="center"><b>News</b></td>
	</tr>
	<tr>
	<td><input type="text" name="dbnews" value="al_server_ws" onClick="this.value=''" /></td>
	<td>Database for News. by default "al_server_ws"</td>
	</tr>
	<tr>
	<td><input type="text" name="news_amt" value="5" onClick="this.value=''" /></td>
	<td>Connection with News Page</td>
	</tr>
	<tr>
	<td><input type="text" name="access_level" value="3" onClick="this.value=''" /></td>
	<td>Minimum Level of Access to add and edit News</td>
	</tr>
	
	<tr>
	<td colspan="2" align="center"><b>Server Rates</b></td>
	</tr>
	<tr>
	<td><input type="text" name="rate_exp" value="1" onClick="this.value=''" /></td>
	<td>EXP Rates</td>
	</tr>
	<tr>
	<td><input type="text" name="rate_kinah" value="1" onClick="this.value=''" /></td>
	<td>Kinah Rates</td>
	</tr>
	<tr>
	<td><input type="text" name="rate_drop" value="1" onClick="this.value=''" /></td>
	<td>Drop Rates</td>
	</tr>
	<tr>
	<td><input type="text" name="rate_quest" value="1" onClick="this.value=''" /></td>
	<td>Quest Rates</td>
	</tr>
	<tr>
	<td colspan="2" align="center"><input type="submit" class="button-submit dark-shadow" value="Submit" name="submit" ></td>
	</tr>
</table>
</form>

<?php
require 'classes/classmysql.php';

if (isset($_POST['submit'])) {
	if (isset($_POST['dbhost']) && isset($_POST['dbuser']) && isset($_POST['dbpassword'])
		&& isset($_POST['gs']) && isset($_POST['ls']) && isset($_POST['server']) && isset($_POST['portgame']) && isset($_POST['portlogin']) && isset($_POST['timeout']) && isset($_POST['address'])
		&& isset($_POST['dbnews']) && isset($_POST['news_amt']) && isset($_POST['access_level'])
		&& isset($_POST['rate_exp']) && isset($_POST['rate_kinah']) && isset($_POST['rate_drop']) && isset($_POST['rate_quest'])) {
		$config = array(
			'dbhost' => $_POST['dbhost'],
			'dbuser' => $_POST['dbuser'],
			'dbpassword' => $_POST['dbpassword'],
			
			'gs' => $_POST['gs'],
			'ls' => $_POST['ls'],
			'server' => $_POST['server'],
			'portgame' => $_POST['portgame'],
			'portlogin' => $_POST['portlogin'],
			'timeout' => $_POST['timeout'],
			'address' => $_POST['address'],
				
			'dbnews' => $_POST['dbnews'],
			'news_amt' => $_POST['news_amt'],
			'access_level' => $_POST['access_level'],
				
			'rate_exp' => $_POST['rate_exp'],
			'rate_kinah' => $_POST['rate_kinah'],
			'rate_drop' => $_POST['rate_drop'],
			'rate_quest' => $_POST['rate_quest'],
		);
        // check, if news db exists
        $sql = new ClassMysql();
        $sql->connect($config['dbhost'],$config['dbuser'],$config['dbpassword']);
		
        if ($sql)
        {            
            if (!$sql->existsDatabase($config['dbnews']))
            {
                exit("<script language='JavaScript'>alert('Please first create your News-Database and grant permissions to the user: ".$config['dbnews']."'); </script>");
            }            
        }
        $sql->close();
        
		$install = fopen("config.php", "w") or die("Unable to open / create file config.php");
		$data = '<?php
//MySQL Database		
$dbhost = "' . $config['dbhost'] . '";
$dbuser = "' . $config['dbuser'] . '";
$dbpassword = "' . $config['dbpassword'] . '";

//Game & Login Server
$gs = "' . $config['gs'] . '";
$ls = "' . $config['ls'] . '";
$server = "' . $config['server'] . '";
$portgame = "' . $config['portgame'] . '";
$portlogin = "' . $config['portlogin'] . '";
$timeout = "' . $config['timeout'] . '";
$address = "' . $config['address'] . '";

//News Server
$dbnews = "' . $config['dbnews'] . '";
$news_amt = "' . $config['news_amt'] . '";
$access_level = "' . $config['access_level'] . '";

//Rates
$rate_exp = "' . $config['rate_exp'] . '";
$rate_kinah = "' . $config['rate_kinah'] . '";
$rate_drop = "' . $config['rate_drop'] . '";
$rate_quest = "' . $config['rate_quest'] . '";';

		$write = fwrite($install, $data);
        
		if ($write) 
        {
			echo "<script language='JavaScript'>alert('Installation is complete. Delete the file install.php'); window.location.href = 'index.php'; </script>";
			unset($config);
					
            require 'config.php';
            
            $sql_ws = new ClassMysql();
            $sql_ws->connect($dbhost,$dbuser,$dbpassword,$dbnews);
            
            if (!$sql_ws->existsTable("news",$dbnews))
            {
                $sql_ws->query( "CREATE TABLE `news` (
                  `id_news` int(11) NOT NULL AUTO_INCREMENT,
                  `title` text,
                  `date` text,
                  `content` text,
                  PRIMARY KEY (`id_news`)
                ) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8; " );
                
                $sql_ws->query( "INSERT INTO news (title, content) VALUES ('Web for Aion Server', '<div style=\"text-align: center;\"><img src=\"img/news/news_1post.png\"><br><div style=\"text-align: left;\">The Web binding is based on Server Aion Web 1.1. The main work has focused on mutations both design and that was done. Some added features. Do not remove the logo SAW<br><br><span style=\"font-weight: bold;\">Key features:</span><br><span style=\"font-weight: bold;\">Functions:</span><br>• Registration page protected bots<br>• Liny Account<br>• Admin panel with server settings and other...<br>• Change Account Password<br>• Change E-Mail<br>• News management System <br>• Integration with aiondatabase.com and aion.yg.com<br>• Drop list server<br>• Automatic Plant<br><br><span style=\"font-weight: bold;\">Information:</span><br>• Displays information about the status of the server and the number of players online<br>• Statistics with the number of accounts, characters, GM-s, legions on the server<br>• List of GMs in Game<br>• Displays detailed information on individual player<br>• Page with Players in Game<br>• Page with the 100 best players in the Server<br>• Page rating legions<br>• Page from the richest players server<br>• Page rating abyss<br>• Page for downloading files<br></div></div>'); " );               
            }             
            $sql_ws->close();
            
            // switch to Database LS
            /* Field "email" should already be present
            $sql_ls->query("ALTER TABLE `account_data` ADD  `email` text NOT NULL;");
            */
		} else {
			exit("<script language='JavaScript'>alert('Error during Installation. Maybe you can not create file'); </script>");
		}
	} else {
		exit("Not all the data entered");
	}
}
?>

</div>
    
</div>

<?php require "footer.php"; ?>