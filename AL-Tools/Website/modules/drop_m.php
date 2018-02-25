<?php
// Aion EMU EU Website - Mariella 01/2016

// ToDo:  - edit npc drops (update, add, delete)
// 
// if table not exists, create this table
if (!$sql_gs->existsTable("droplist",$gs))
{
    $bef = "CREATE TABLE droplist (".
           "npcid      INT(11) NOT NULL,".
           "itemid     INT(11) NOT NULL,".
           "minamount  INT(5) NOT NULL,".
           "maxamount  INT(5) NOT NULL,".
           "chance     DEC(8,5) NOT NULL,".
           "no_reduce  BOOLEAN NOT NULL DEFAULT true,".
           "eachmember BOOLEAN NOT NULL DEFAULT false,".
           "PRIMARY KEY (npcid,itemid) ".
           ") ENGINE=InnoDB DEFAULT CHARSET=utf8; ";
    $sql_gs->query($bef);
}

// check for testing data
$cnt = $sql_gs->result($sql_gs->query("SELECT COUNT(*) FROM droplist"),0);
if ($cnt < 1)
{
    // we make a bulk insert for some test data
    $bef = "INSERT INTO droplist(npcid,itemid,minamount,maxamount,chance,no_reduce,eachmember) VALUES ".
           "(210758,111600525,1,1, 1.031,true,false),".
           "(210758,112600314,1,1, 1.031,true,false),".
           "(210758,112600507,1,1, 1.031,true,false),".
           "(210758,152010311,1,1,16.495,true,false),".
           "(210760,100600303,1,1, 1.408,true,false),".
           "(210760,160003553,1,1, 2.817,true,false),".
           "(210760,182400001,1,1,66.66 ,true,false);";
    $sql_gs->query($bef);
}

// if submit is pressed, we want to search an NpcId           
if (isset($_POST['submit'])) 
{	
	if (!empty($_POST['npcId'])) 
    {
		$npcId = substr($_POST["npcId"], 0, 20);
		$npcId = trim(mysql_real_escape_string($npcId));
				
        $cnt = $sql_gs->result($sql_gs->query("SELECT count(*) FROM droplist WHERE npcid = '$npcId'"),0);
        
		if ($cnt > 0) 
        {
			echo '<script language="JavaScript">window.location.href = "dropform.php?action=info&id='.$npcId.'"; </script>';
		}
		else 
        {
			$error = 'NPC ID "'.$npcId.'" not found';
		}
	}
	else 
    {
		$error = "Enter the NPC ID";
	}
}