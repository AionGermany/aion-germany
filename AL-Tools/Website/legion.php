<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
// Eddit by Lark (x714 serveraion.ru)

require "header.php";
?>

<div class="case">
        <div class="content">
                <div class="list">
                        <div class="news-title"><center><?php echo $lang['topLegions']; ?></center></div>
                        <table cellspacing="0" width="100%">
                                <tr align="center">
                                        <td class="tophead"><?php echo $lang['place']; ?></td>
                                        <td class="tophead"><?php echo $lang['race']; ?></td>
                                        <td class="tophead"><?php echo $lang['name']; ?></td>
                                        <td class="tophead"><?php echo $lang['legate']; ?></td>
                                        <td class="tophead"><?php echo $lang['member']; ?></td>
                                        <td class="tophead"><?php echo $lang['level']; ?></td>
                                        <td class="tophead right"><?php echo $lang['abysspoints']; ?></td>
                                </tr>
                                <?php require "modules/legion_m.php";?>
                        </table>
                </div>
        </div>
    
<?php require "sidebar.php"; ?>
</div>

<?php require "footer.php"; ?>