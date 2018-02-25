<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
// Mariella, 01/2016, added Language Support
if (!file_exists("config.php")) {
	header("Location: install.php");
}
session_start();
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Aion German Emu Web</title>
<LINK href="img/favicon.png" rel="shortcut icon" />
<LINK href="style/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="style/tooltip.js"></script>
<!-- Page Navigation -->
<LINK rel="stylesheet" type="text/css" href="style/page.css" />
<script type="text/javascript" src="style/page.js"></script>
<script type="text/javascript" src="style/page_admin.js"></script>
<!-- BB Code -->
<LINK rel="stylesheet" type="text/css" href="style/bbcodes.css" />
<script type="text/javascript" src="style/jquery.min.js"></script>
<script type="text/javascript" src="style/jquery.cleditor.min.js"></script>
<!-- Knowledge Base -->
<script type="text/javascript" src="style/aiondatabase.js"></script>
<script type="text/javascript" src="style/aionyg.js"></script>
<!-- Language Support -->
<script type="text/javascript" src="language/language.js"></script>
<!-- MENUE -->
<LINK rel="stylesheet" type="text/css" href="style/menu.css" />
<script type="text/javascript" src="style/menu.js"></script>
</head>
<body>

<?php require "config.php"; ?>
<?php require "language/language.php"; ?>
<?php require "modules/status_m.php"; ?>

<!-- Top Right Navigation -->
<div id="navigation">
        <div style="text-align: right; float: right;">
            <a target="_blank" href="http://twitter.com/"><img class="b" src="img/twitter.png" tooltip="Twitter"/></a>
            <a target="_blank" href="http://facebook.com/groups/"><img class="b" src="img/facebook.png" tooltip="Facebook"/></a>
        </div>
</div>

<!-- Top Status -->
	<div class="width" align="center">
	<div class="head-status border shadow"><?php echo "Login: "; echo $login ? "<font color='#00CC00'><b>Online</b></font>" . "&nbsp&nbsp&nbsp" : "<font color='red'><b>Offline</b></font>" . "&nbsp&nbsp&nbsp"; ?>
	<?php echo "&nbsp&nbspGame: "; echo $game ? "<font color='#00CC00'><b>Online</b></font>" . "&nbsp&nbsp&nbsp" : "<font color='red'><b>Offline</b></font>" . "&nbsp&nbsp&nbsp"; ?>
	<?php echo "&nbspOnline:  <b>$playersonline</b>" . "&nbsp&nbsp&nbsp"; ?>
	<?php echo "&nbsp&nbsp"; echo $address ?>
	<?php echo "&nbsp&nbsp&nbsp"; echo getLangMenu(__file__); ?>
	</div>
    
    <!-- Aion Logo -->
    <div class="aionlogo"><img src="img/logo_blue.png"></div>
	
	<!-- Top Menue -->
	<div class="head-menu border-top shadow">
        <div>
            <ul id="menu">
                <li><a href="./"><?php echo $lang['home']; ?></a></li>
                <li><a href="#"><?php echo $lang['statistics']; ?></a>
                <ul>
                    <li><a href="online.php"><?php echo $lang['onlinePlayers']; ?></a></li>
                    <li><a href="top100.php"><?php echo $lang['top100']; ?></a></li>
                    <li><a href="legion.php"><?php echo $lang['topLegions']; ?></a></li>
                    <li><a href="rich.php"><?php echo $lang['topRich']; ?></a></li>
                    <li><a href="abyss.php"><?php echo $lang['topAbyss']; ?></a></li>
                </ul>
                <li><a href="search.php"><?php echo $lang['searchPlayers']; ?></a></li>
                <li><a href="download.php"><?php echo $lang['downloads']; ?></a></li>
                <li><a href="forum.php"><?php echo $lang['forum']; ?></a></li>
            </ul>
        </div>
	</div>
</div>