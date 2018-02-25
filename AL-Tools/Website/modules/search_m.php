<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
// Eddit Lark (x714 serveraion.ru)

if(isset($_POST['submit'])) {
	
	if(!empty($_POST['name'])) 
    {
		$input_name = substr($_POST["name"], 0, 20);
		$input_name = trim(mysql_real_escape_string($input_name));
				
        $bef = "SELECT `id`, `name` FROM `players` WHERE `name` = '$input_name'";
        $res = $sql_gs->query($bef);
        
        if ($sql_gs->num_rows($res) > 0)
        {
			while ($row = $sql_gs->fetch_assoc($res)) 
            {
				$name = $row['name'];
                
				if ($name) 
					echo '<script language="JavaScript">window.location.href = "info.php?action=info&id='.$row['id'].'"; </script>';
			}
		}
		else {
			$error = 'Player "'.$input_name.'" Not found';
		}
	}
	else 
    {
		$error = "Enter the Name";
	}
}