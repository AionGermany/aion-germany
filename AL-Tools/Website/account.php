<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
// Eddit Lark (x714 serveraion.ru)

require "header.php";
require 'modules/function_account.php';
?>

<div class="case">
	<div class="content">
		<div class="list">
			<div class="news-title"><?php echo $lang['accountInformation']; ?></div>
				<!-- Block Overview -->
				<div class="menu-button border shadow" ><center><b><?php echo $lang['generalInformation']; ?></b></center>
					<?php require 'modules/account/account_info.php';?>
				</div>
				<!-- Block List -->
				<div class="menu-button border shadow" ><center><b><?php echo $lang['charactersList']; ?></b></center>
					<?php require 'modules/account/account_players.php';?>
				</div>
<div class="clear"></div>
		</div>
	</div>
	
<?php require "sidebar.php"; ?>
</div>

<?php require "footer.php"; ?>
<?php /* require 'modules/account/account_buy.php'; */ ?>