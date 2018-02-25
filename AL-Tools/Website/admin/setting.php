<?php if (!defined('SAWEB')) {
    die('hacking attempt!');
}
if (isset($_POST['submit'])) {
    if (isset($_POST['dbhost']) && isset($_POST['dbuser']) && isset($_POST['dbpassword']) && isset($_POST['gs']) && isset($_POST['ls']) && isset($_POST['server']) && isset($_POST['portgame']) && isset($_POST['portlogin']) && isset($_POST['timeout']) && isset($_POST['address']) && isset($_POST['dbnews']) && isset($_POST['news_amt']) && isset($_POST['access_level']) && isset($_POST['rate_exp']) && isset($_POST['rate_kinah']) && isset($_POST['rate_drop']) && isset($_POST['rate_quest'])) {
        $config = array('dbhost' => $_POST['dbhost'], 'dbuser' => $_POST['dbuser'], 'dbpassword' => $_POST['dbpassword'], 'gs' => $_POST['gs'], 'ls' => $_POST['ls'], 'server' => $_POST['server'], 'portgame' => $_POST['portgame'], 'portlogin' => $_POST['portlogin'], 'timeout' => $_POST['timeout'], 'address' => $_POST['address'], 'dbnews' => $_POST['dbnews'], 'news_amt' => $_POST['news_amt'], 'access_level' => $_POST['access_level'], 'member_ship_premium' => $_POST['member_ship_premium'], 'member_ship_vip' => $_POST['member_ship_vip'], 'day' => $_POST['day'], 'rate_exp' => $_POST['rate_exp'], 'rate_kinah' => $_POST['rate_kinah'], 'rate_drop' => $_POST['rate_drop'], 'rate_quest' => $_POST['rate_quest']);
        $install = fopen('config.php', 'w') or die('Unable to open / create file config.php');
        $data = '<?php
$dbhost = "' . $config['dbhost'] . '";
$dbuser = "' . $config['dbuser'] . '";
$dbpassword = "' . $config['dbpassword'] . '";

$gs = "' . $config['gs'] . '";
$ls = "' . $config['ls'] . '";
$server = "' . $config['server'] . '";
$portgame = "' . $config['portgame'] . '";
$portlogin = "' . $config['portlogin'] . '";
$timeout = "' . $config['timeout'] . '";
$address = "' . $config['address'] . '";

$dbnews = "' . $config['dbnews'] . '";
$news_amt = "' . $config['news_amt'] . '";
$access_level = "' . $config['access_level'] . '";

//Script Revision
$rev_src = "' . $rev_src . '";
$rev_ver = "' . $rev_ver . '";

//Privileges Prices
$member_ship_premium = "' . $config['member_ship_premium'] . '";
$member_ship_vip = "' . $config['member_ship_vip'] . '";
$day = "' . $config['day'] . '";

$rate_exp = "' . $config['rate_exp'] . '";
$rate_kinah = "' . $config['rate_kinah'] . '";
$rate_drop = "' . $config['rate_drop'] . '";
$rate_quest = "' . $config['rate_quest'] . '";';
        $write = fwrite($install, $data);
        if ($write) {
            echo "<script language='JavaScript'>alert('This changed successfully'); window.location.href = 'admin.php?mod=setting'; </script>";
        }
    }
}
echo '
<form method="POST" action="admin.php?mod=setting">
	<div class="menu-button border shadow">
	<table align="center">
		<tr>
			<td colspan="2" align="center"><b>Connecting to MySQL</b><div class="hr"></div></td>
		</tr>
		<tr>
			<td><input type="text" name="dbhost" value="' . $dbhost . '"/></td>
			<td>MySQL Server Address</td>
		</tr>
		<tr>
			<td><input type="text" name="dbuser" value="' . $dbuser . '"/></td>
			<td>MySQL Administrator Name</td>
		</tr>
		<tr>
			<td><input type="text" name="dbpassword" value="' . $dbpassword . '"/></td>
			<td>MySQL Administrator Password</td>
		</div></tr>
		
		<tr>
			<td colspan="2" align="center"><br/></td>
		</tr>
		
		<tr>
			<td colspan="2" align="center"><b>Connection to the Game Server</b><div class="hr"></div></td>
		</tr>
		<tr>
			<td><input type="text" name="gs" value="' . $gs . '" /></td>
			<td>Game Server Databese</td>
		</tr>
		<tr>
			<td><input type="text" name="ls" value="' . $ls . '" /></td>
			<td>Login Server Database</td>
		</tr>
		<tr>
			<td><input type="text" name="server" value="' . $server . '" /></td>
			<td>Address of the Server to connect</td>
		</tr>
		<tr>
			<td><input type="text" name="portgame" value="' . $portgame . '" /></td>
			<td>Game Server Port. by default 7777</td>
		</tr>
		<tr>
			<td><input type="text" name="portlogin" value="' . $portlogin . '" /></td>
			<td>Login Server Port. by default 2106</td>
		</tr>
		<tr>
			<td><input type="text" name="timeout" value="' . $timeout . '" /></td>
			<td>Connection Timeout</td>
		</tr>
		<tr>
			<td><input type="text" name="address" value="' . $address . '" /></td>
			<td>Server Name</td>
		</tr>
		
		<tr>
			<td colspan="2" align="center"><br/></td>
		</tr>
		
		<tr>
			<td colspan="2" align="center"><b>News</b><div class="hr"></div></td>
		</tr>
		<tr>
			<td><input type="text" name="dbnews" value="' . $dbnews . '" /></td>
			<td>Database for News</td>
		</tr>
		<tr>
			<td><input type="text" name="news_amt" value="' . $news_amt . '" /></td>
			<td>Koliestvo of News on the Page</td>
		</tr>
		<tr>
			<td><input type="text" name="access_level" value="' . $access_level . '" /></td>
			<td>Priority Level for access to the Administrator and to addition of News</td>
		</tr>
				
		<tr>
			<td colspan="2" align="center"><br/></td>
		</tr>
		
		<tr>
			<td colspan="2" align="center"><b>Information about the Rates</b><div class="hr"></div></td>
		</tr>
		<tr>
			<td><input type="text" name="rate_exp" value="' . $rate_exp . '" /></td>
			<td>EXP Rate</td>
		</tr>
		<tr>
			<td><input type="text" name="rate_kinah" value="' . $rate_kinah . '" /></td>
			<td>Kinah Rate</td>
		</tr>
		<tr>
			<td><input type="text" name="rate_drop" value="' . $rate_drop . '" /></td>
			<td>Drop Rate</td>
		</tr>
		<tr>
			<td><input type="text" name="rate_quest" value="' . $rate_quest . '" /></td>
			<td>Quests EXP Rate</td>
		</tr>
		
		<tr>
			<td colspan="2" align="center"><br/></td>
		</tr>
		
		<tr>
			<td colspan="2" align="center"><b>Control of the Private Office</b><div class="hr"></div></td>
		</tr>
		<tr>
			<td><input type="text" name="member_ship_premium" value="' . $member_ship_premium . '" /></td>
			<td>Price Account Premium</td>
		</tr>
		<tr>
			<td><input type="text" name="member_ship_vip" value="' . $member_ship_vip . '" /></td>
			<td>Price Account VIP</td>
		</tr>
		<tr>
			<td><input type="text" name="day" value="' . $day . '" /></td>
			<td>On how many days to issue privileges</td>
		</tr>
		
		<tr>
			<td colspan="2" align="center"><input type="submit" class="button-submit dark-shadow" value="Submit" name="submit" ></td>
		</tr>
	</table>
	</div>
</form>';;