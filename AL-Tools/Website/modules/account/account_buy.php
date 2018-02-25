<?php $status_buy = $_GET['buy'];
$date = date('Y-m-d');
if ($status_buy == 'premium') {
    $connect = mysql_connect("$dbhost", "$dbuser", "$dbpassword");
    mysql_select_db("$ls");
    mysql_query("SET NAMES 'utf8'");
    $donat_m_ship = "SELECT donatemoney FROM account_data WHERE id='" . $_SESSION['id_account'] . "'";
    $result = mysql_query($donat_m_ship);
    $row = mysql_fetch_assoc($result);
    if ($row['donatemoney'] < $member_ship_premium) {
        echo "<script language='JavaScript'>alert('Not enough money to buy.');</script>";
    } else {
        $donate_money_minus_ok = "UPDATE account_data SET donatemoney=donatemoney-$member_ship_premium WHERE id='" . $_SESSION['id_account'] . "'";
        mysql_query($donate_money_minus_ok);
        if ($donate_money_minus_ok == true) {
            mysql_query("UPDATE account_data SET expire='" . $date . "' WHERE id='" . $_SESSION['id_account'] . "'");
            mysql_query("UPDATE account_data SET membership='1', expire=DATE_ADD(expire,INTERVAL $day DAY) WHERE id='" . $_SESSION['id_account'] . "'");
            echo "<script language='JavaScript'>alert('You were bought at Premium Account $day day (s).'); window.location.href = 'account.php';</script>";
        }
    }
} elseif ($status_buy == 'vip') {
    $connect = mysql_connect("$dbhost", "$dbuser", "$dbpassword");
    mysql_select_db("$ls");
    mysql_query("SET NAMES 'utf8'");
    $donat_m_ship = "SELECT donatemoney FROM account_data WHERE id='" . $_SESSION['id_account'] . "'";
    $result = mysql_query($donat_m_ship);
    $row = mysql_fetch_assoc($result);
    if ($row['donatemoney'] < $member_ship_vip) {
        echo "<script language='JavaScript'>alert('Not enough money to buy.');</script>";
    } else {
        $donate_money_minus_ok = "UPDATE account_data SET donatemoney=donatemoney-$member_ship_vip WHERE id='" . $_SESSION['id_account'] . "'";
        mysql_query($donate_money_minus_ok);
        if ($donate_money_minus_ok == true) {
            mysql_query("UPDATE account_data SET expire='" . $date . "' WHERE id='" . $_SESSION['id_account'] . "'");
            mysql_query("UPDATE account_data SET membership='2', expire=DATE_ADD(expire,INTERVAL $day DAY) WHERE id='" . $_SESSION['id_account'] . "'");
            echo "<script language='JavaScript'>alert('You were bought at Vip Account $day day (s).'); window.location.href = 'account.php'; </script>";
        }
    }
};