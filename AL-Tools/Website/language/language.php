<?PHP
/*
   Mariella, 01/2016, added Language-Support
*/  
$langwahl = isset($_COOKIE['langwahl']) ? $_COOKIE['langwahl'] : "en";

// Language-File einbinden (z.B. de.php")
$modul = "language/".$langwahl.".php";

if (!file_exists($modul)) $modul = "language/en.php";
require_once($modul);

// Sprachen-Flaggen als Menue-Leiste ausgeben
function getLangMenu($href)
{
    global $lang;
    
    $tabflags = array(
                  array("de","de.png"),
                  array("ru","ru.png"),
                  array("gb","gb.png"),
                  array("fr","fr.png"),
                  array("it","it.png")
                     );
                     
    $domax = count($tabflags);
    $menue = "";
    
    for ($f=0;$f<$domax;$f++)
        $menue .= '
            <a target="_self" href="#" onclick="setLang(\''.$tabflags[$f][0].'\')"><img class="flags" src="img/'.$tabflags[$f][1].'" tooltip="'.$lang[$tabflags[$f][0]].'"/></a>';
        
    return $menue;
}
?>