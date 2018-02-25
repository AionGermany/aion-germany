<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
require "header.php";
?>

<div class="case">
	<div class="content">
		<div class="list">
			<div class="news-title"><?php echo $lang['top100players']; ?>
				<div style="float:right; "><?php echo $lang['choice']; ?>
                  <SELECT onchange="document.location='?class='+this.value;">
                  <?php
                    // with this table, we can easy set the selected value
                    $tabvalues = array(
                                   //    selection value  language key
                                   array("No",           "noclass"),
                                   array("All",          "allclasses"),
                                   array("Warrior",      "warrior"),
                                   array("Gladiator",    "gladiator"),
                                   array("Templar",      "templar"),
                                   array("Scout",        "scout"),
                                   array("Assassin",     "assassin"),
                                   array("Ranger",       "ranger"),
                                   array("Mage",         "mage"),
                                   array("Sorcerer",     "sorcerer"),
                                   array("Spirit_Master","spiritmaster"),
                                   array("Priest",       "priest"),
                                   array("Cleric",       "cleric"),
                                   array("Chanter",      "chanter"),
                                   array("Artist",       "artist"),
                                   array("Bard",         "bard"),
                                   array("Engineer",     "engineer"),
                                   array("Gunner",       "gunner"),
                                   array("Rider",        "rider")
                                 );
                    $class = isset($_GET['class']) ? $_GET['class'] : "All";
                    
                    for ($i=0;$i<count($tabvalues);$i++)
                    {
                        if ($class == $tabvalues[$i][0])
                            $sel = " selected";
                        else
                            $sel = "";
                        echo '
            <OPTION VALUE="'.$tabvalues[$i][0].'"'.$sel.'>'.$lang[$tabvalues[$i][1]].'</OPTION>';
                    }
                         
                  /*
					<OPTION VALUE="No"><?php echo $lang['noclass']; ?></OPTION>
					<OPTION VALUE="All"><?php echo $lang['allclasses']; ?></OPTION>
					<OPTION VALUE="Warrior"><?php echo $lang['warrior']; ?></OPTION>
					<OPTION VALUE="Gladiator"><?php echo $lang['gladiator']; ?></OPTION>
					<OPTION VALUE="Templar"><?php echo $lang['templar']; ?></OPTION>
					<OPTION VALUE="Scout"><?php echo $lang['scout']; ?></OPTION>
					<OPTION VALUE="Assassin"><?php echo $lang['assassin']; ?></OPTION>
					<OPTION VALUE="Ranger"><?php echo $lang['ranger']; ?></OPTION>
					<OPTION VALUE="Mage"><?php echo $lang['mage']; ?></OPTION>
					<OPTION VALUE="Sorcerer"><?php echo $lang['sorcerer']; ?></OPTION>
					<OPTION VALUE="Spirit_Master"><?php echo $lang['spiritmaster']; ?></OPTION>
					<OPTION VALUE="Priest"><?php echo $lang['priest']; ?></OPTION>
					<OPTION VALUE="Cleric"><?php echo $lang['cleric']; ?></OPTION>
					<OPTION VALUE="Chanter"><?php echo $lang['chanter']; ?></OPTION>
					<OPTION VALUE="Artist"><?php echo $lang['artist']; ?></OPTION>
					<OPTION VALUE="Bard"><?php echo $lang['bard']; ?></OPTION>
					<OPTION VALUE="Engineer"><?php echo $lang['engineer']; ?></OPTION>
					<OPTION VALUE="Gunner"><?php echo $lang['gunner']; ?></OPTION>
					<OPTION VALUE="Rider"><?php echo $lang['rider']; ?></OPTION>
                  */          
                  ?>
				  </SELECT>
				</div>
			</div>
			
			<table cellspacing="0" width="100%">
				<tr align="center">
					<td class="tophead"><?php echo $lang['place']; ?></td>
					<td class="tophead"><?php echo $lang['name']; ?></td>
					<td class="tophead"><?php echo $lang['level']; ?></td>        
					<td class="tophead"><?php echo $lang['race']; ?></td>
					<td class="tophead"><?php echo $lang['class']; ?></td>
					<td class="tophead"><?php echo $lang['sex']; ?></td>
					<td class="tophead"><?php echo $lang['status']; ?></td>
				</tr>
				<?php require "modules/top100_m.php"; ?>
			</table>
		</div>
	</div>
	
<?php require "sidebar.php"; ?>
</div>

<?php require "footer.php"; ?>