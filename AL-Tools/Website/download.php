<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
require "header.php";
?>

<div class="case">
	<div class="content">
    	<div class="news">
			<div class="news-title"><center><?php echo $lang['downloadFiles']; ?></center></div>
			<div id="nodecor" align="center" class="mb10">
				<a href="#client"><div class="menu-block border shadow"><img src="img/game.png" /><br /><b><?php echo $lang['client']; ?></b></div></a>
				<a href="#launcher"><div class="menu-block border shadow"><img src="img/launcher.png" /><br /><b><?php echo $lang['launcher']; ?></b></div></a>
				<a href="#setup"><div class="menu-block border shadow"><img src="img/patch.png" /><br /><b><?php echo $lang['setup']; ?></b></div></a>
			</div>
			<div class="hr"></div>
			<div class="news-title" id="client"><center>Gameforge Client v4.8</center></div>
			<div id="nodecor" align="center" class="mb10">
				<a href="https://en.aion.gameforge.com/website/download/" target="_blank"><div class="tcenter menu-block border shadow"><img src="img/link.png" /><br /><b>Gameforge Client</b></div></a>
			</div>
			<div class="hr"></div>
			<div class="news-title" id="launcher"><center><?php echo $lang['launcher']; ?></center></div>
			<div id="nodecor" align="center" class="mb10">
				<a href="files/Aion Launcher.rar"><div class="tcenter menu-block border shadow"><img src="img/link.png" /><br /><b>Aion Launcher</b></div></a>
			</div>
			<div class="hr"></div>
			<div class="news-title" id="setup"><center><?php echo $lang['setup']; ?></center></div>
			<div id="nodecor" align="center" class="mb10">
				1. Download Gameforge Client<br>
				2. Download Aion Launcher.rar and extract to Aion Folder<br>
				3. Start aion_start_online.bat<br>
				4. Create Account<br>
			</div>
			<div id="clear"></div>
		</div>
    </div>
    
<?php require "sidebar.php"; ?>
</div>

<?php require "footer.php"; ?>