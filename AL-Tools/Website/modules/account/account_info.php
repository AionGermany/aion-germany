<?php 
$id  = $_SESSION['id_account'];
$res = $sql_ls->query("SELECT name,email,access_level,activated,membership,expire,last_ip FROM account_data WHERE id='$id'");

while ($row = $sql_ls->fetch_assoc($res)) 
{
    $name = $row['name'];
    $email = $row['email'];
    $access_level = $row['access_level'];
    $activated = $row['activated'];
    $membership = $row['membership'];
    $expire = $row['expire'];
    $last_ip = $row['last_ip'];
};
echo '<div class="hr"></div>
<table width="100%">
		<tr height="32">
			<td class="toptext">Account:</td>
			<td class="toptext">';
echo $name;
echo '</td>
			<td class="toptext"></td>
		</tr>
		<tr height="32">
			<td class="toptext">E-Mail:</td>
			<td class="toptext">';
echo $email;
echo '</td>
			<td class="toptext"></td>
		</tr>
		<tr height="32">
			<td class="toptext">Access Level:</td>
			<td class="toptext">';
echo access_account($access_level);
echo '</td>
			<td class="toptext"></td>
		</tr>
		<tr height="32">
			<td class="toptext">Status:</td>
			<td class="toptext">';
echo activ_account($activated);
echo '</td>
			<td class="toptext"></td>
		</tr>
		<tr height="32">
			<td class="toptext">Account Privileges:</td>
			<td class="toptext">';
            echo ship_account($membership);
/*
if ($membership != 0) {
    echo ship_account($membership) . ' to <font style="color:green; font-weight:bold;">' . str_replace('-', '.', $expire) . '</font></td>';
} else {
    echo ship_account($membership) . '</td>
				<td class="toptext">
				<a href="account.php?buy=premium">Premium (' . $member_ship_premium . ' Add.)</a> to ' . $day . ' Days
				<br/>
				<a href="account.php?buy=vip">VIP (' . $member_ship_vip . ' Add.)</a> to ' . $day . ' Days';
};
echo '		</tr>
*/
echo '
		<tr height="32">
			<td class="toptext">Last IP:</td>
			<td class="toptext">';
echo $last_ip;;
echo '</td>
			<td class="toptext"></td>
		</tr>
      </table>';