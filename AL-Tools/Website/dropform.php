<?php
// Aion EMU EU Website - Mariella 01/2016

// ToDo:  - edit npc drops (update, add, delete)
// 
require "header.php"; 

// get the $_GET-variables
$action = isset($_GET['action']) ? $_GET['action'] : "";
$npcid  = isset($_GET['id'])     ? $_GET['id'] : 0;

?> 
<div class="case"> 
    <div class="content">
        <div class="news">
            <div class="news-title"><center>Drop List for Npc <?php echo $npcid; ?></center></div>
            <div>		
<?php
if ($action != "")
{    
    if ($npcid > 0)
    {
        switch ($action)
        {
            case "info":
                    showNpcDrops($npcid);
                    break;
            default:
                    break;
        }
    }
    else
        $error = "Missing NpcId for action ".$_GET['action'];
}

// get boolean value
function getBool($value)
{
    if ($value)
        return "true";
    else
        return "false";
}
// show drops from npc
function showNpcDrops($npcid)
{
    global $sql_gs;
?>  
                <table>
                    <tr>
                        <th>Mark</th>
                        <th>NpcId</th>
                        <th>ItemId</th>
                        <th>MinAmount</th>
                        <th>MaxAmount</th>
                        <th>Chance</th>
                        <th>noReduce</th>
                        <th>EachMember</th>
                    </tr>
<?php       
    $tdc = " style='text-align:center;'";
    $tdr = " style='text-align:right;'";
    $res = $sql_gs->query("SELECT * FROM droplist WHERE npcid='$npcid'"); 
    
    while($row = $sql_gs->fetch_assoc($res))
    {
        $chk = $row['npcid'].";".$row['itemid'];
        
        echo "<tr><td><input type='checkbox' value='".$chk."'><td>".$row['npcid']."</td><td>".$row['itemid']."</td><td $tdc>".$row['minamount']."</td><td $tdc>".$row['maxamount'].
             "</td><td $tdr>".$row['chance']."</td><td $tdc>".getBool($row['no_reduce'])."</td><td $tdc>".getBool($row['eachmember'])."</td></tr>";
    }
}
?>    
                </table>
                <br><br>
                <center>
                  <a href="drop.php" target="_self"><input type="button" class="button-submit dark-shadow" value="back to search" onclick="submit()"></a>
                </center>   
            </div>
        </div>
    </div>

<?php require "sidebar.php"; ?>
</div>

<?php require "footer.php"; ?>