<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
// Eddit Lark (x714 serveraion.ru)
require "header.php";
define('SAWEB', '');
?>

<div class="case">
	<div class="content">
		<div class="list">
<?php if (isset($_SESSION['id']) AND $_SESSION['access_level'] >= "$access_level") { ?>
			<center>
			<div class="news-title">Administrator Account</div>
			<div class="mb10">
					<a class="tcenter admin-block border shadow" href="admin.php">Home</a>
					<a class="tcenter admin-block border shadow" href="admin.php?mod=setting">Settings</a>
					<a class="tcenter admin-block border shadow" href="admin.php?mod=accounts">Accounts</a>
					<a class="tcenter admin-block border shadow" href="admin.php?mod=legion">Legions</a>
					<!--<a class="tcenter admin-block border shadow" href="admin.php?mod=admins">Administrators</a>-->
			</div>
			</center>

			<?php 
            if (isset($_GET['mod']))
            {
                if ($_GET['mod'] == "setting") 
                    {require 'admin/setting.php';}
                elseif ($_GET['mod'] == "accounts") 
                    {require 'admin/accounts.php';}
                elseif ($_GET['mod'] == "info") 
                    {require 'admin/infos.php';}
                elseif ($_GET['mod'] == "legion") 
                    {require 'admin/legion.php';}
                elseif ($_GET['mod'] == "infoleg") 
                    {require 'admin/infoleg.php';}
                //elseif ($_GET['mod'] == "admins") 
                //	{require 'admin/admins.php';}
                else {require 'admin/info.php';}
            }
            else
                require('admin/info.php');
			?>
			
<?php		
}
else { ?>
			<div class="news-title"><center>ACCESS DENIED!<br/>You have no rights.<center></div>
<?php
}
?>
<div class="clear"></div>
		</div>
	</div>
	
<?php require "sidebar.php"; ?>
</div>
<?php require "footer.php"; ?>
