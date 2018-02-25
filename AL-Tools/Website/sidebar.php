<!-- ServerAion Web by Pr00f & Sky (serveraion.ru) -->

<div class="sidebar">

    <div class="container">
        <div id="login-form">
            <h3><?php echo $lang['login']; ?></h3>
            <fieldset>  
                <footer>
                <?php require "login_form.php"; ?> 
                </footer>
            </fieldset> 
        </div>
    </div>
    
    <div class="container">
        <div id="login-form">
            <h3><?php echo $lang['statistic']; ?></h3>
            <fieldset>
                <footer class="clearfix">
                    <table cellspacing="0">
                       <colgroup>
                          <col style="width:65px;">
                          <col style="width:10px;">
                          <col style="width:40px;">
                       </colgroup>
                       <tr>
                          <td class="toptext"><?php echo $lang['accounts']; ?></td>
                          <td class="toptext"> : </td>
                          <td class="toptextright"><?php echo $accounts_amt; ?></td>
                       </tr>
                       <tr>
                          <td class="toptext"><?php echo $lang['characters']; ?></td>
                          <td class="toptext"> : </td>
                          <td class="toptextright"><?php echo $players_amt; ?></td>
                       </tr>
                       <tr>
                          <td class="toptext"><?php echo $lang['gms']; ?></td>
                          <td class="toptext"> : </td>
                          <td class="toptextright"><?php echo $gm_amt; ?></td>
                       </tr>
                       <tr>
                          <td class="toptext"><?php echo $lang['legions']; ?></td>
                          <td class="toptext"> : </td>
                          <td class="toptextright"><?php echo $legions_amt; ?></td>
                       </tr>
                    </table>
                </footer>
            </fieldset> 
        </div>
    </div>

    <div class="container">
        <div id="login-form">
            <h3><?php echo $lang['gmsOnline']; ?></h3>
            <fieldset>
                <footer class="clearfix">
                    <?php echo $gmonline; ?>
                </footer>
            </fieldset>
        </div>
	</div>
    
    <div class="container">
        <div id="login-form">
            <h3><?php echo $lang['vote']; ?></h3>
            <fieldset>
                <center><a href="http://falke34.bplaced.net/" target="_blank"><img src="img/banner_aion_german.png"></a> <a href="http://falke34.bplaced.net/" target="_blank"><img src="img/banner_aion_german.png"></a></center>
            </fieldset>
        </div>
	</div>
	
    <div class="container">
        <div id="login-form">
            <h3><?php echo $lang['counters']; ?></h3>
            <fieldset>
                <center><img src="img/count_test.png"> <img src="img/count_test.png"></center>
            </fieldset>
        </div>
	</div>
</div>