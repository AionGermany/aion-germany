<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)
// Edit by Lark (x714 serveraion.ru)
// Edit by Falke_34

// Name
$name = $row["name"];

// XP for Client 4.9, Aion EU
if (isset($row["exp"]))
{
    if     ($row["exp"] <        400) $exp =  1;
    elseif ($row["exp"] <       1433) $exp =  2;
    elseif ($row["exp"] <       3820) $exp =  3;
    elseif ($row["exp"] <       9054) $exp =  4;
    elseif ($row["exp"] <      17655) $exp =  5;
    elseif ($row["exp"] <      30978) $exp =  6;
    elseif ($row["exp"] <      52010) $exp =  7;
    elseif ($row["exp"] <      82982) $exp =  8;
    elseif ($row["exp"] <     126069) $exp =  9;
    elseif ($row["exp"] <     182252) $exp = 10;
    elseif ($row["exp"] <     260622) $exp = 11;
    elseif ($row["exp"] <     360825) $exp = 12;
    elseif ($row["exp"] <     490331) $exp = 13;
    elseif ($row["exp"] <     649169) $exp = 14;
    elseif ($row["exp"] <     844378) $exp = 15;
    elseif ($row["exp"] <    1080479) $exp = 16;
    elseif ($row["exp"] <    1393133) $exp = 17;
    elseif ($row["exp"] <    1793977) $exp = 18;
    elseif ($row["exp"] <    2282186) $exp = 19;
    elseif ($row["exp"] <    2881347) $exp = 20;
    elseif ($row["exp"] <    3659516) $exp = 21;
    elseif ($row["exp"] <    4622407) $exp = 22;
    elseif ($row["exp"] <    5821524) $exp = 23;
    elseif ($row["exp"] <    7227983) $exp = 24;
    elseif ($row["exp"] <    8835056) $exp = 25;
    elseif ($row["exp"] <   10699436) $exp = 26;
    elseif ($row["exp"] <   12853998) $exp = 27;
    elseif ($row["exp"] <   15255815) $exp = 28;
    elseif ($row["exp"] <   18061172) $exp = 29;
    elseif ($row["exp"] <   21551945) $exp = 30;
    elseif ($row["exp"] <   25635643) $exp = 31;
    elseif ($row["exp"] <   30490364) $exp = 32;
    elseif ($row["exp"] <   36299780) $exp = 33;
    elseif ($row["exp"] <   43890745) $exp = 34;
    elseif ($row["exp"] <   53559061) $exp = 35;
    elseif ($row["exp"] <   65392986) $exp = 36;
    elseif ($row["exp"] <   81005799) $exp = 37;
    elseif ($row["exp"] <   98965887) $exp = 38;
    elseif ($row["exp"] <  120006279) $exp = 39;
    elseif ($row["exp"] <  145305546) $exp = 40;
    elseif ($row["exp"] <  174901906) $exp = 41;
    elseif ($row["exp"] <  209678951) $exp = 42;
    elseif ($row["exp"] <  246923912) $exp = 43;
    elseif ($row["exp"] <  286491872) $exp = 44;
    elseif ($row["exp"] <  328812035) $exp = 45;
    elseif ($row["exp"] <  374157438) $exp = 46;
    elseif ($row["exp"] <  422165990) $exp = 47;
    elseif ($row["exp"] <  473102570) $exp = 48;
    elseif ($row["exp"] <  527287631) $exp = 49;
    elseif ($row["exp"] <  584861315) $exp = 50;
    elseif ($row["exp"] <  649149135) $exp = 51;
    elseif ($row["exp"] <  718967268) $exp = 52;
    elseif ($row["exp"] <  793470302) $exp = 53;
    elseif ($row["exp"] <  871508583) $exp = 54;
    elseif ($row["exp"] <  953180528) $exp = 55;
    elseif ($row["exp"] < 1039463797) $exp = 56;
    elseif ($row["exp"] < 1130615342) $exp = 57;
    elseif ($row["exp"] < 1226906283) $exp = 58;
    elseif ($row["exp"] < 1328622680) $exp = 59;
    elseif ($row["exp"] < 1434562107) $exp = 60;
    elseif ($row["exp"] < 1548141590) $exp = 61;
    elseif ($row["exp"] < 1667422949) $exp = 62;
    elseif ($row["exp"] < 1793319043) $exp = 63;
    elseif ($row["exp"] < 1926765410) $exp = 64;
    else                              $exp = 65;
}

// Race
if (isset ($row["race"])) {
	if ($row["race"] == 'ELYOS') {$race = "<img src='img_top/ely.png' title='".$lang['elyos']."' />";}
	elseif ($row["race"] == 'ASMODIANS') {$race = "<img src='img_top/asmo.png' title='".$lang['asmodian']."' />";}
}

// Legions Rank
if (isset ($row["rank"])) {
	if ($row["rank"] == 'BRIGADE_GENERAL') {$rank_leg = $lang['brigadegeneral'];}
	elseif ($row["rank"] == 'CENTURION') {$rank_leg = $lang['centurion'];}
	elseif($row["rank"] == 'LEGIONARY') {$rank_leg = $lang['legionary'];}
	else {$rank_leg = $lang['recruit'];}
}


// Class
if (isset ($row["player_class"])) {
	if($row["player_class"] == 'WARRIOR') {$player_class = "<img src='img_top/warrior.png' title='".$lang['warrior']."'>";}
	elseif($row["player_class"] == 'GLADIATOR') {$player_class = "<img src='img_top/gladiator.png' title='".$lang['gladiator']."'>";}
	elseif($row["player_class"] == 'TEMPLAR') {$player_class = "<img src='img_top/templar.png' title='".$lang['templar']."'>";}
	elseif($row["player_class"] == 'SCOUT') {$player_class = "<img src='img_top/scout.png' title='".$lang['scout']."'>";}
	elseif($row["player_class"] == 'ASSASSIN') {$player_class = "<img src='img_top/assassin.png' title='".$lang['assassin']."'>";}
	elseif($row["player_class"] == 'RANGER') {$player_class = "<img src='img_top/ranger.png' title='".$lang['ranger']."'>";}
	elseif($row["player_class"] == 'MAGE') {$player_class = "<img src='img_top/mage.png' title='".$lang['mage']."'>";}
	elseif($row["player_class"] == 'SORCERER') {$player_class = "<img src='img_top/sorcerer.png' title='".$lang['sorcerer']."'>";}
	elseif($row["player_class"] == 'SPIRIT_MASTER') {$player_class = "<img src='img_top/spiritmaster.png' title='".$lang['spiritmaster']."'>";}
	elseif($row["player_class"] == 'PRIEST') {$player_class = "<img src='img_top/priest.png' title='".$lang['priest']."'>";}
	elseif($row["player_class"] == 'CLERIC') {$player_class = "<img src='img_top/cleric.png' title='".$lang['cleric']."'>";}
	elseif($row["player_class"] == 'CHANTER') {$player_class = "<img src='img_top/chanter.png' title='".$lang['chanter']."'>";}
	elseif($row["player_class"] == 'ENGINEER') {$player_class = "<img src='img_top/engineer.png' title='".$lang['engineer']."'>";}
	elseif($row["player_class"] == 'GUNNER') {$player_class = "<img src='img_top/gunner.png' title='".$lang['gunner']."'>";}
	elseif($row["player_class"] == 'RIDER') {$player_class = "<img src='img_top/rider.png' title='".$lang['rider']."'>";}
	elseif($row["player_class"] == 'ARTIST') {$player_class = "<img src='img_top/artist.png' title='".$lang['artist']."'>";}
	elseif($row["player_class"] == 'BARD') {$player_class = "<img src='img_top/bard.png' title='".$lang['bard']."'>";}
}


// Sex
if (isset ($row["gender"])) {
	if ($row["gender"] == 'MALE') {$gender = "<img src='img_top/male.png' title='".$lang['male']."' />";}
	elseif ($row["gender"] == 'FEMALE') {$gender = "<img src='img_top/female.png' title='".$lang['female']."' />";}
}


// Online Status
if (isset ($row["online"])) {
	if($row["online"] == '0') {$online = "<img src='img_top/off.png' title='Offline' />";}
	elseif($row["online"] == '1') {$online = "<img src='img_top/on.png' title='Online' />";}
}


// Location
if (isset ($row["world_id"])) {
	if($row["world_id"] == 110010000) {$world_id = "Sanctum";}
	else if($row["world_id"] == 110070000) {$world_id = "Kaisinel Academy";}
	else if($row["world_id"] == 120010000) {$world_id = "Pandaemonium";}
	else if($row["world_id"] == 120020000) {$world_id = "Marchutan Temple";}
	else if($row["world_id"] == 120080000) {$world_id = "Marchutan Priory";}
	else if($row["world_id"] == 210010000) {$world_id = "Poeta";}
	else if($row["world_id"] == 210020000) {$world_id = "Eltnen";}
	else if($row["world_id"] == 210030000) {$world_id = "Verteron";}
	else if($row["world_id"] == 210040000) {$world_id = "Heiron";}
	else if($row["world_id"] == 210050000) {$world_id = "Inggison";}
	else if($row["world_id"] == 210060000) {$world_id = "Theobomos";}
	else if($row["world_id"] == 210070000) {$world_id = "Cygnea";}
	else if($row["world_id"] == 220010000) {$world_id = "Ishalgen";}
	else if($row["world_id"] == 220020000) {$world_id = "Morheim";}
	else if($row["world_id"] == 220030000) {$world_id = "Altgard";}
	else if($row["world_id"] == 220040000) {$world_id = "Beluslan";}
	else if($row["world_id"] == 220050000) {$world_id = "Brusthonin";}
	else if($row["world_id"] == 220070000) {$world_id = "Gelkmaros";}
	else if($row["world_id"] == 220080000) {$world_id = "Enshar";}
	else if($row["world_id"] == 300030000) {$world_id = "Nochsana Training Camp";}
	else if($row["world_id"] == 300040000) {$world_id = "Dark Poeta";}
	else if($row["world_id"] == 300050000) {$world_id = "Asteria Chamber";}
	else if($row["world_id"] == 300060000) {$world_id = "Sulfur Tree Nest";}
	else if($row["world_id"] == 300070000) {$world_id = "Chamber of Roah";}
	else if($row["world_id"] == 300080000) {$world_id = "Left Wing Chamber";}
	else if($row["world_id"] == 300090000) {$world_id = "Right Wing Chamber";}
	else if($row["world_id"] == 300100000) {$world_id = "Steel Rake" ;}
	else if($row["world_id"] == 300110000) {$world_id = "Dredgion";}
	else if($row["world_id"] == 300120000) {$world_id = "Kysis Chamber";}
	else if($row["world_id"] == 300130000) {$world_id = "Miren Chamber";}
	else if($row["world_id"] == 300140000) {$world_id = "Krotan Chamber";}
	else if($row["world_id"] == 300150000) {$world_id = "Udas Temple";}
	else if($row["world_id"] == 300160000) {$world_id = "Lower Udas Temple";}
	else if($row["world_id"] == 300170000) {$world_id = "Beshmundir Temple";}
	else if($row["world_id"] == 300190000) {$world_id = "Taloc's Hollow";}
	else if($row["world_id"] == 300200000) {$world_id = "Haramel";}
	else if($row["world_id"] == 300210000) {$world_id = "Chantra Dredgion";}
	else if($row["world_id"] == 300220000) {$world_id = "Abyssal_Splinter";}
	else if($row["world_id"] == 300230000) {$world_id = "Kromede's Trial" ;}
	else if($row["world_id"] == 300240000) {$world_id = "Aturam Sky Fortress" ;}
	else if($row["world_id"] == 300250000) {$world_id = "Esoterrace" ;}
	else if($row["world_id"] == 300280000) {$world_id = "Rentus Base" ;}
	else if($row["world_id"] == 300330000) {$world_id = "Protector's Realm" ;}
	else if($row["world_id"] == 300350000) {$world_id = "Arena of Chaos" ;}
	else if($row["world_id"] == 300360000) {$world_id = "Arena_of Discipline" ;}
	else if($row["world_id"] == 300380000) {$world_id = "Muada Trencher" ;}
	else if($row["world_id"] == 300390000) {$world_id = "Israphel's Tract" ;}
	else if($row["world_id"] == 300400000) {$world_id = "Tiamaranta's Eye" ;}
	else if($row["world_id"] == 300410000) {$world_id = "Sarpan Sky" ;}
	else if($row["world_id"] == 300420000) {$world_id = "Chaos Training Grounds" ;}
	else if($row["world_id"] == 300430000) {$world_id = "Discipline Training Grounds" ;}
	else if($row["world_id"] == 300440000) {$world_id = "Terath_Dredgion" ;}
	else if($row["world_id"] == 300450000) {$world_id = "Arena of Harmony" ;}
	else if($row["world_id"] == 300460000) {$world_id = "Steel Rake Cabin" ;}
	else if($row["world_id"] == 300470000) {$world_id = "Satra Treasure Hoard" ;}
	else if($row["world_id"] == 300480000) {$world_id = "Danuar Mysticarium Solo" ;}
	else if($row["world_id"] == 300490000) {$world_id = "Tiamat Solo" ;}
	else if($row["world_id"] == 300500000) {$world_id = "Tiamat Israphel" ;}
	else if($row["world_id"] == 300510000) {$world_id = "Tiamat Stronghold" ;}
	else if($row["world_id"] == 300520000) {$world_id = "Dragon Lords Refuge" ;}
	else if($row["world_id"] == 300540000) {$world_id = "The Eternal Bastion" ;}
	else if($row["world_id"] == 300550000) {$world_id = "Arena_Of Glory" ;}
	else if($row["world_id"] == 300570000) {$world_id = "Arena Team" ;}
	else if($row["world_id"] == 300580000) {$world_id = "Void Cube" ;}
	else if($row["world_id"] == 300590000) {$world_id = "Ophidan Bridge" ;}
	else if($row["world_id"] == 300600000) {$world_id = "Unstable Abyssal Splinter" ;}
	else if($row["world_id"] == 300610000) {$world_id = "Raksang Ruin (Solo)" ;}
	else if($row["world_id"] == 300620000) {$world_id = "Occupied Rentus Base" ;}
	else if($row["world_id"] == 300630000) {$world_id = "Anguished Dragon Lord's Refuge" ;}
	else if($row["world_id"] == 300700000) {$world_id = "The Hexway" ;}
	else if($row["world_id"] == 300800000) {$world_id = "Infinity Shard" ;}
	else if($row["world_id"] == 300900000) {$world_id = "Danuar Infinity Shard" ;}	
	else if($row["world_id"] == 301000000) {$world_id = "Experimentiergefangnis" ;}
	else if($row["world_id"] == 301100000) {$world_id = "Unity Training Grounds" ;}
	else if($row["world_id"] == 301110000) {$world_id = "Danuar Reliquary" ;}
	else if($row["world_id"] == 301120000) {$world_id = "Kamar Battlefield" ;}
	else if($row["world_id"] == 301130000) {$world_id = "Sauro Supply Base" ;}
	else if($row["world_id"] == 301140000) {$world_id = "Seized Danuar Sanctuary" ;}
	else if($row["world_id"] == 301160000) {$world_id = "Rukibuki Circus" ;}
	else if($row["world_id"] == 301200000) {$world_id = "Rukibuki Big Tent Extravaganza" ;}
	else if($row["world_id"] == 301210000) {$world_id = "Ophidan Bridge War" ;}
	else if($row["world_id"] == 301220000) {$world_id = "Iron Wall Warfront" ;}	
	else if($row["world_id"] == 310010000) {$world_id = "Karamatis";}
	else if($row["world_id"] == 310020000) {$world_id = "Karamatis";}
	else if($row["world_id"] == 310030000) {$world_id = "Aerdina";}
	else if($row["world_id"] == 310040000) {$world_id = "Geranaia";}
	else if($row["world_id"] == 310050000) {$world_id = "Aetherogenetics Lab";}
	else if($row["world_id"] == 310060000) {$world_id = "Sliver of Darkness" ;}
	else if($row["world_id"] == 310070000) {$world_id = "Sliver of Darkness" ;}
	else if($row["world_id"] == 310080000) {$world_id = "Sanctum Underground Arena";}
	else if($row["world_id"] == 310090000) {$world_id = "Indratu Fortress";}
	else if($row["world_id"] == 310100000) {$world_id = "Azoturan Fortress";}
	else if($row["world_id"] == 310110000) {$world_id = "Theobomos Lab's";}
	else if($row["world_id"] == 320010000) {$world_id = "Ataxiar";}
	else if($row["world_id"] == 320020000) {$world_id = "Ataxiar";}
	else if($row["world_id"] == 320030000) {$world_id = "Bregirun";}
	else if($row["world_id"] == 320040000) {$world_id = "Nidalber";}
	else if($row["world_id"] == 320050000) {$world_id = "Sky Temple Interior";}
	else if($row["world_id"] == 320060000) {$world_id = "Abode of Oblivion";}
	else if($row["world_id"] == 320070000) {$world_id = "Space of Destiny";}
	else if($row["world_id"] == 320080000) {$world_id = "Draupnir Cave";}
	else if($row["world_id"] == 320090000) {$world_id = "Triniel Underground Arena";}
	else if($row["world_id"] == 320100000) {$world_id = "Fire Temple" ;}
	else if($row["world_id"] == 320110000) {$world_id = "Alquimia Research Center";}
	else if($row["world_id"] == 320120000) {$world_id = "Shadow Court Dungeon" ;}
	else if($row["world_id"] == 320130000) {$world_id = "Adma Stronghold";}
	else if($row["world_id"] == 320140000) {$world_id = "Ataxiar";}
	else if($row["world_id"] == 320150000) {$world_id = "Padmarashka's Cave";}
	else if($row["world_id"] == 400010000) {$world_id = "Reshanta";}
	else if($row["world_id"] == 510010000) {$world_id = "Jail";}
	else if($row["world_id"] == 520010000) {$world_id = "Jail";}
	else if($row["world_id"] == 600010000) {$world_id = "Silentera Canyon";}
	else if($row["world_id"] == 600090000) {$world_id = "Kaldor";}
	else if($row["world_id"] == 600100000) {$world_id = "Levinshor";}
	else if($row["world_id"] == 600080000) {$world_id = "Live Party Concert Hall";}
	else if($row["world_id"] == 700010000) {$world_id = "Oriel";}
	else if($row["world_id"] == 710010000) {$world_id = "Pernon";}
	else if($row["world_id"] == 720010000) {$world_id = "Studio";}
	else if($row["world_id"] == 730010000) {$world_id = "Studio";}	
}

// Titles
if (isset ($row["title_id"])) {
		if($row["title_id"] == '-1') {$title_id = "No Title";}
		elseif($row["title_id"] == '1') {$title_id = "Poeta's Protector";}
        elseif($row["title_id"] == '2') {$title_id = "Verteron's Warrior";}
        elseif($row["title_id"] == '3') {$title_id = "Bottled Lightning";}
        elseif($row["title_id"] == '4') {$title_id = "Tree Hugger";}
        elseif($row["title_id"] == '5') {$title_id = "Krall Hunter";}
        elseif($row["title_id"] == '6') {$title_id = "Straw for Brains";}
        elseif($row["title_id"] == '7') {$title_id = "Animal Lover";}
        elseif($row["title_id"] == '8') {$title_id = "Krall Whisperer";}
        elseif($row["title_id"] == '9') {$title_id = "Patient";}
        elseif($row["title_id"] == '10') {$title_id = "Mabangtah's Envoy";}        
        elseif($row["title_id"] == '11') {$title_id = "Demolitions Expert";}
        elseif($row["title_id"] == '12') {$title_id = "Eltnen's Hero";}
        elseif($row["title_id"] == '13') {$title_id = "Klaw Hunter";}
        elseif($row["title_id"] == '14') {$title_id = "Aerialist";}
        elseif($row["title_id"] == '15') {$title_id = "Kobold Chef";}
        elseif($row["title_id"] == '16') {$title_id = "Isson's Apologist";}
        elseif($row["title_id"] == '17') {$title_id = "Eulogist";}
        elseif($row["title_id"] == '18') {$title_id = "Bloodsworn";}
        elseif($row["title_id"] == '19') {$title_id = "Veritas Agent";}
        elseif($row["title_id"] == '20') {$title_id = "Savior of Eiron Forest";}       
        elseif($row["title_id"] == '21') {$title_id = "Gestanerk's Avenger";}
        elseif($row["title_id"] == '22') {$title_id = "Bounty Hunter";}
        elseif($row["title_id"] == '23') {$title_id = "Arbolu's Anointed";}
        elseif($row["title_id"] == '24') {$title_id = "Chief Investigator";}
        elseif($row["title_id"] == '25') {$title_id = "Indratu Bane";}
        elseif($row["title_id"] == '26') {$title_id = "Big Damn Hero";}
        elseif($row["title_id"] == '27') {$title_id = "Not-Quite-Master of Disguise";}
        elseif($row["title_id"] == '28') {$title_id = "Master Angler";}
        elseif($row["title_id"] == '29') {$title_id = "Spymaster";}
        elseif($row["title_id"] == '30') {$title_id = "Balaur Whisperer";} 
        elseif($row["title_id"] == '31') {$title_id = "Tough";}
        elseif($row["title_id"] == '32') {$title_id = "Battle-Hardened";}
        elseif($row["title_id"] == '33') {$title_id = "Invincible";}
        elseif($row["title_id"] == '34') {$title_id = "Heroic";}
        elseif($row["title_id"] == '35') {$title_id = "Dread Pirate";}
        elseif($row["title_id"] == '36') {$title_id = "Top Expert";}
        elseif($row["title_id"] == '37') {$title_id = "Miragent Holy Templar";}
        elseif($row["title_id"] == '38') {$title_id = "Aetheric Master";}
        elseif($row["title_id"] == '39') {$title_id = "Daeva of Mercy";}
        elseif($row["title_id"] == '40') {$title_id = "Dragon Sword Master";}       
        elseif($row["title_id"] == '41') {$title_id = "Honorary Black Cloud";}
        elseif($row["title_id"] == '42') {$title_id = "Krall Stalker";}
        elseif($row["title_id"] == '43') {$title_id = "Battering Ram";}
        elseif($row["title_id"] == '44') {$title_id = "Tenacious Pursuer";}
        elseif($row["title_id"] == '45') {$title_id = "Gullible";}
        elseif($row["title_id"] == '46') {$title_id = "Traitor's Bane";}
        elseif($row["title_id"] == '47') {$title_id = "Drakanhammer";}
        elseif($row["title_id"] == '48') {$title_id = "Knight Errant";}
        elseif($row["title_id"] == '49') {$title_id = "Seraphic Vindicator";}
        elseif($row["title_id"] == '50') {$title_id = "Dark Sovereign";}        
        elseif($row["title_id"] == '51') {$title_id = "Raider Hero";}
        elseif($row["title_id"] == '52') {$title_id = "Treasure Hunter";}
        elseif($row["title_id"] == '53') {$title_id = "Mosbear Slayer";}
        elseif($row["title_id"] == '54') {$title_id = "Mau Whisperer";}
        elseif($row["title_id"] == '55') {$title_id = "Kind";}
        elseif($row["title_id"] == '56') {$title_id = "Legendary Hunter";}
        elseif($row["title_id"] == '57') {$title_id = "Protector of Altgard";}
        elseif($row["title_id"] == '58') {$title_id = "Tayga Slayer";}
        elseif($row["title_id"] == '59') {$title_id = "Curse Breaker";}
        elseif($row["title_id"] == '60') {$title_id = "Protector of Morheim";} 
        elseif($row["title_id"] == '61') {$title_id = "Shugo Chef";}
        elseif($row["title_id"] == '62') {$title_id = "Ginseng-Infused";}
        elseif($row["title_id"] == '63') {$title_id = "Honorary Kidorun";}
        elseif($row["title_id"] == '64') {$title_id = "Shedim Altruist";}
        elseif($row["title_id"] == '65') {$title_id = "Mosbear Nemesis";}
        elseif($row["title_id"] == '66') {$title_id = "Silver Mane Ally";}
        elseif($row["title_id"] == '67') {$title_id = "Postal";}
        elseif($row["title_id"] == '68') {$title_id = "Provocateur";}
        elseif($row["title_id"] == '69') {$title_id = "Tenacious";}
        elseif($row["title_id"] == '70') {$title_id = "The Cat's Meow";}        
        elseif($row["title_id"] == '71') {$title_id = "Unyielding Pioneer";}
        elseif($row["title_id"] == '72') {$title_id = "Protector of Brusthonin";}
        elseif($row["title_id"] == '73') {$title_id = "Easy Mark";}
        elseif($row["title_id"] == '74') {$title_id = "Beluslan's Hero";}
        elseif($row["title_id"] == '75') {$title_id = "Snowfield Predator";}
        elseif($row["title_id"] == '76') {$title_id = "Besfer's Shield";}
        elseif($row["title_id"] == '77') {$title_id = "Scourge of Mt. Musphel";}
        elseif($row["title_id"] == '78') {$title_id = "Loremaster";}
        elseif($row["title_id"] == '79') {$title_id = "Emissary";}
        elseif($row["title_id"] == '80') {$title_id = "Balaur Whisperer";}        
        elseif($row["title_id"] == '81') {$title_id = "Tough";}
        elseif($row["title_id"] == '82') {$title_id = "Battle-Hardened";}
        elseif($row["title_id"] == '83') {$title_id = "Invincible";}
        elseif($row["title_id"] == '84') {$title_id = "Heroic";}
        elseif($row["title_id"] == '85') {$title_id = "Steel Rake Headhunter";}
        elseif($row["title_id"] == '86') {$title_id = "Top Expert";}
        elseif($row["title_id"] == '87') {$title_id = "Fenris's Fang";}
        elseif($row["title_id"] == '88') {$title_id = "Aetheric Master";}
        elseif($row["title_id"] == '89') {$title_id = "Azphel's Aegis";}
        elseif($row["title_id"] == '90') {$title_id = "Master of Agrif's Rage";}        
        elseif($row["title_id"] == '91') {$title_id = "Wheeler-Dealer";}
        elseif($row["title_id"] == '92') {$title_id = "Factotum";}
        elseif($row["title_id"] == '93') {$title_id = "Valiant";}
        elseif($row["title_id"] == '94') {$title_id = "Silver Mane Champion";}
        elseif($row["title_id"] == '95') {$title_id = "Born Merchant";}
        elseif($row["title_id"] == '96') {$title_id = "Shadow Marked";}
        elseif($row["title_id"] == '97') {$title_id = "Spiritspeaker";}
        elseif($row["title_id"] == '98') {$title_id = "Pirate of the Carobian";}
        elseif($row["title_id"] == '99') {$title_id = "Shedim Conquerer";}
        elseif($row["title_id"] == '100') {$title_id = "Dark Vindicator";}        
        elseif($row["title_id"] == '101') {$title_id = "Settler of Aion";}
        elseif($row["title_id"] == '102') {$title_id = "Destiny Ascendant";}
        elseif($row["title_id"] == '103') {$title_id = "Adept of Aion";}
        elseif($row["title_id"] == '104') {$title_id = "Shining Intellectual";}
        elseif($row["title_id"] == '105') {$title_id = "Sage of Aion";}
		elseif($row["title_id"] == '106') {$title_id = "Munificent";}
		elseif($row["title_id"] == '107') {$title_id = "Iron Stomached";}
        elseif($row["title_id"] == '108') {$title_id = "Shulack Friend";}
        elseif($row["title_id"] == '109') {$title_id = "Guardian Ally";}
        elseif($row["title_id"] == '110') {$title_id = "Kaisinel's Assassin";}        
        elseif($row["title_id"] == '111') {$title_id = "Guardian Protector";}
        elseif($row["title_id"] == '112') {$title_id = "Balaur Eradicator";}
		elseif($row["title_id"] == '113') {$title_id = "Discoverer of the Secret";}
		elseif($row["title_id"] == '114') {$title_id = "Vanquisher of Mastarius";}
        elseif($row["title_id"] == '115') {$title_id = "Alabaster Wing";}
        elseif($row["title_id"] == '116') {$title_id = "Alabaster Eye";}
		elseif($row["title_id"] == '117') {$title_id = "Alabaster Hand";}
		elseif($row["title_id"] == '118') {$title_id = "Radiant Fist";}
        elseif($row["title_id"] == '119') {$title_id = "Radiant Shield";}
        elseif($row["title_id"] == '120') {$title_id = "Radiant Blade";}
		elseif($row["title_id"] == '121') {$title_id = "Radiant Shroud";}
		elseif($row["title_id"] == '122') {$title_id = "Seeker";}
        elseif($row["title_id"] == '123') {$title_id = "Tracker";}
        elseif($row["title_id"] == '124') {$title_id = "Hunter";}
		elseif($row["title_id"] == '125') {$title_id = "Fortuneer";}		
		elseif($row["title_id"] == '126') {$title_id = "Lesson Giver";}
        elseif($row["title_id"] == '127') {$title_id = "Incarnation of Vengeance";}
        elseif($row["title_id"] == '128') {$title_id = "Apostle of Justice";}
		elseif($row["title_id"] == '129') {$title_id = "Veille's Adversary";}
		elseif($row["title_id"] == '130') {$title_id = "Hand of Support";}
        elseif($row["title_id"] == '131') {$title_id = "Balaur Voltaire";}
        elseif($row["title_id"] == '132') {$title_id = "Delver of Mysteries";}
		elseif($row["title_id"] == '133') {$title_id = "Vanquisher of Veille";}
		elseif($row["title_id"] == '134') {$title_id = "Field Warden";}
        elseif($row["title_id"] == '135') {$title_id = "Field Agent";}
        elseif($row["title_id"] == '136') {$title_id = "Field Director";}
		elseif($row["title_id"] == '137') {$title_id = "Blood Champion";}
		elseif($row["title_id"] == '138') {$title_id = "Blood Sentinel";}
        elseif($row["title_id"] == '139') {$title_id = "Blood Conqueror";}
        elseif($row["title_id"] == '140') {$title_id = "Blood Phalanx";}
		elseif($row["title_id"] == '141') {$title_id = "Fixer";}
		elseif($row["title_id"] == '142') {$title_id = "Operator";}
        elseif($row["title_id"] == '143') {$title_id = "Handler";}
        elseif($row["title_id"] == '144') {$title_id = "Daemon";}
		elseif($row["title_id"] == '145') {$title_id = "Taegeuk Hero";}
		elseif($row["title_id"] == '146') {$title_id = "Daddy Long Legs";}
        elseif($row["title_id"] == '147') {$title_id = "Akarios' Friend";}
        elseif($row["title_id"] == '148') {$title_id = "Arekedil's Hope";}
		elseif($row["title_id"] == '149') {$title_id = "Tiamat Stalker";}
		elseif($row["title_id"] == '150') {$title_id = "Obstinate Herdsman";}		
        elseif($row["title_id"] == '151') {$title_id = "Master of Many Pets";}
        elseif($row["title_id"] == '152') {$title_id = "Charmer";}
		elseif($row["title_id"] == '153') {$title_id = "Sucker";}
		elseif($row["title_id"] == '154') {$title_id = "Circumspect Advisor";}
        elseif($row["title_id"] == '155') {$title_id = "Kind Counsel";}
        elseif($row["title_id"] == '156') {$title_id = "Key Master";}
		elseif($row["title_id"] == '157') {$title_id = "Shrewd Advisor";}
		elseif($row["title_id"] == '158') {$title_id = "Outstanding Advisor";}
        elseif($row["title_id"] == '159') {$title_id = "The Circle Leader";}
        elseif($row["title_id"] == '160') {$title_id = "Gufrinerk's Patron";}
		elseif($row["title_id"] == '161') {$title_id = "Jielinlinerk's Patron";}		
		elseif($row["title_id"] == '162') {$title_id = "Hot Shot";}
        elseif($row["title_id"] == '163') {$title_id = "Jielinlinerk's Faithful Patron";}
        elseif($row["title_id"] == '164') {$title_id = "Empyrean Challenger";}
		elseif($row["title_id"] == '165') {$title_id = "Up-and-Comer";}		
		elseif($row["title_id"] == '166') {$title_id = "Master of Preceptors";}
        elseif($row["title_id"] == '167') {$title_id = "Mighty Combatant";}
        elseif($row["title_id"] == '168') {$title_id = "Crucible Champion";}
		elseif($row["title_id"] == '169') {$title_id = "Empyrean Challenger";}
		elseif($row["title_id"] == '170') {$title_id = "Up-and-Comer";}
        elseif($row["title_id"] == '171') {$title_id = "Master of Preceptors";}       
        elseif($row["title_id"] == '172') {$title_id = "Mighty Combatant";}
		elseif($row["title_id"] == '173') {$title_id = "Crucible Champion";}
		elseif($row["title_id"] == '174') {$title_id = "Memory Journaler";}
        elseif($row["title_id"] == '175') {$title_id = "Dream Journaler";}
        elseif($row["title_id"] == '176') {$title_id = "Festival Crab";}
		elseif($row["title_id"] == '177') {$title_id = "[Event] Memorialist";}
		elseif($row["title_id"] == '178') {$title_id = "Indomitable";}
        elseif($row["title_id"] == '179') {$title_id = "New Year";}
        elseif($row["title_id"] == '180') {$title_id = "Daevic Defender";}
		elseif($row["title_id"] == '181') {$title_id = "Self-pitying";}
		elseif($row["title_id"] == '182') {$title_id = "Legendary";}
        elseif($row["title_id"] == '183') {$title_id = "Crucible Challenger";}
        elseif($row["title_id"] == '184') {$title_id = "Crucible Challenger";}
		elseif($row["title_id"] == '185') {$title_id = "Majordomo of Discipline";}
		elseif($row["title_id"] == '186') {$title_id = "Brawny";}
        elseif($row["title_id"] == '187') {$title_id = "Sea-Like";}
        elseif($row["title_id"] == '188') {$title_id = "Novice";}
		elseif($row["title_id"] == '189') {$title_id = "Returnee";}
		elseif($row["title_id"] == '190') {$title_id = "Universally Friendly";}
        elseif($row["title_id"] == '191') {$title_id = "Universally Earnest";}
        elseif($row["title_id"] == '192') {$title_id = "Trailblazer";}
		elseif($row["title_id"] == '193') {$title_id = "Friendly Sponsor";}
		elseif($row["title_id"] == '194') {$title_id = "Earnest Sponsor";}
        elseif($row["title_id"] == '195') {$title_id = "Diligent Sponsor";}
        elseif($row["title_id"] == '196') {$title_id = "Big Hitter";}
		elseif($row["title_id"] == '197') {$title_id = "Eager Hitter";}
		elseif($row["title_id"] == '198') {$title_id = "Hard Hitter";}
        elseif($row["title_id"] == '199') {$title_id = "Heavy Hitter";}
        elseif($row["title_id"] == '200') {$title_id = "Dreamer";}        
		elseif($row["title_id"] == '201') {$title_id = "Love's Bane";}
		elseif($row["title_id"] == '202') {$title_id = "Empowered";}
        elseif($row["title_id"] == '203') {$title_id = "Breaking up the Asmodian Scout Band";}
        elseif($row["title_id"] == '204') {$title_id = "Tiamaranta's Eye Visitor";}
		elseif($row["title_id"] == '205') {$title_id = "Shining Spear";}
		elseif($row["title_id"] == '206') {$title_id = "Proud Homeowner";}
        elseif($row["title_id"] == '207') {$title_id = "Expert Infiltrator";}
        elseif($row["title_id"] == '208') {$title_id = "Raksang Sealbreaker";}
		elseif($row["title_id"] == '209') {$title_id = "Dream Chaser";}
		elseif($row["title_id"] == '210') {$title_id = "Davlin Rescuer";}
        elseif($row["title_id"] == '211') {$title_id = "Reian Savior";}
        elseif($row["title_id"] == '212') {$title_id = "Malodorous";}
		elseif($row["title_id"] == '213') {$title_id = "Sealing Force";}
		elseif($row["title_id"] == '214') {$title_id = "Breaking up the Elyos Scout Band";}
        elseif($row["title_id"] == '215') {$title_id = "Tiamat's Defier";}
        elseif($row["title_id"] == '216') {$title_id = "Shadow Spear";}
		elseif($row["title_id"] == '217') {$title_id = "House Owner";}
		elseif($row["title_id"] == '218') {$title_id = "Expert Undercover";}
        elseif($row["title_id"] == '219') {$title_id = "Raksha Confronter";}
        elseif($row["title_id"] == '220') {$title_id = "Friend of True Love";}
		elseif($row["title_id"] == '221') {$title_id = "Honored Guest";}
		elseif($row["title_id"] == '222') {$title_id = "Rentus's Savior";}
        elseif($row["title_id"] == '223') {$title_id = "Free Spirit";}
        elseif($row["title_id"] == '224') {$title_id = "Prima-Daeva";}
		elseif($row["title_id"] == '225') {$title_id = "Tasteful Daeva";}
		elseif($row["title_id"] == '226') {$title_id = "Student Leader";}
        elseif($row["title_id"] == '227') {$title_id = "Stage Setting No.1";}
        elseif($row["title_id"] == '228') {$title_id = "Supersonic Daeva";}
		elseif($row["title_id"] == '229') {$title_id = "Lightspeed Daeva";}
		elseif($row["title_id"] == '230') {$title_id = "Daeva in a Predicament";}
        elseif($row["title_id"] == '231') {$title_id = "A Wracked Daeva";}        
        elseif($row["title_id"] == '232') {$title_id = "A Chaotic Daeva";}
		elseif($row["title_id"] == '233') {$title_id = "A Lone Daeva";}
		elseif($row["title_id"] == '234') {$title_id = "Birthday Daeva";}
        elseif($row["title_id"] == '235') {$title_id = "Drunken Master";}
        elseif($row["title_id"] == '236') {$title_id = "Crafting Association Completion Certificate";}
		elseif($row["title_id"] == '237') {$title_id = "Siel's Warrior";}
		elseif($row["title_id"] == '238') {$title_id = "Siel's Fighter";}
        elseif($row["title_id"] == '239') {$title_id = "Ten-time Champion";}
        elseif($row["title_id"] == '240') {$title_id = "Twenty-time Champion";}
		elseif($row["title_id"] == '241') {$title_id = "Thirty-time Champion";}
		elseif($row["title_id"] == '242') {$title_id = "Forty-time Champion";}
		elseif($row["title_id"] == '243') {$title_id = "Academy Legend";}
		elseif($row["title_id"] == '244') {$title_id = "Ten-time Champion";}
        elseif($row["title_id"] == '245') {$title_id = "Twenty-time Champion";}
        elseif($row["title_id"] == '246') {$title_id = "Thirty-time Champion";}
		elseif($row["title_id"] == '247') {$title_id = "Forty-time Champion";}
		elseif($row["title_id"] == '248') {$title_id = "Priory Legend";}
        elseif($row["title_id"] == '249') {$title_id = "Tiamat's Challenger";}
        elseif($row["title_id"] == '250') {$title_id = "Tiamat's Challenger";}
		elseif($row["title_id"] == '251') {$title_id = "No stats";}
		elseif($row["title_id"] == '252') {$title_id = "Dragonbane";}
        elseif($row["title_id"] == '253') {$title_id = "Dragon Slayer";}
        elseif($row["title_id"] == '254') {$title_id = "The Pinnacle Of Beauty";}
		elseif($row["title_id"] == '255') {$title_id = "Top Gun";}		
		elseif($row["title_id"] == '256') {$title_id = "Katalam's Hope";}
        elseif($row["title_id"] == '257') {$title_id = "Katalam's Light";}
        elseif($row["title_id"] == '258') {$title_id = "Watcher of the Forest";}
		elseif($row["title_id"] == '259') {$title_id = "Nature's Friend";}
		elseif($row["title_id"] == '260') {$title_id = "Victor in All Realms";}
        elseif($row["title_id"] == '261') {$title_id = "Elemental Sage";}
        elseif($row["title_id"] == '262') {$title_id = "Stalwart";}
		elseif($row["title_id"] == '263') {$title_id = "Guardian Detachment's Hero";}
		elseif($row["title_id"] == '264') {$title_id = "Archon Battlegroup's Hero";}
        elseif($row["title_id"] == '265') {$title_id = "Quick Feet";}
        elseif($row["title_id"] == '266') {$title_id = "Dexterous Agility";}
		elseif($row["title_id"] == '267') {$title_id = "Head and Shoulders Above the Rest";}
		elseif($row["title_id"] == '268') {$title_id = "Aion's Chosen";}
        elseif($row["title_id"] == '269') {$title_id = "Uncontrollably Happy";}
        elseif($row["title_id"] == '270') {$title_id = "Lots to Talk About";}
		elseif($row["title_id"] == '271') {$title_id = "Held-up Words Until The End";}
		elseif($row["title_id"] == '272') {$title_id = "Dreamstrider";}
		elseif($row["title_id"] == '273') {$title_id = "Ultimate Duo";}
		elseif($row["title_id"] == '274') {$title_id = "Glorious Number One";}
}

// Abyss Rank
if (isset ($row["rank"])) {
	if    ($row["rank"] ==  '1') {$rank = $lang['soldier']." 9";}
	elseif($row["rank"] ==  '2') {$rank = $lang['soldier']." 8";}
	elseif($row["rank"] ==  '3') {$rank = $lang['soldier']." 7";}
	elseif($row["rank"] ==  '4') {$rank = $lang['soldier']." 6";}
	elseif($row["rank"] ==  '5') {$rank = $lang['soldier']." 5";}
	elseif($row["rank"] ==  '6') {$rank = $lang['soldier']." 4";}
	elseif($row["rank"] ==  '7') {$rank = $lang['soldier']." 3";}
	elseif($row["rank"] ==  '8') {$rank = $lang['soldier']." 2";}
	elseif($row["rank"] ==  '9') {$rank = $lang['soldier']." 1";}
	elseif($row["rank"] == '10') {$rank = $lang['officer1'];}
	elseif($row["rank"] == '11') {$rank = $lang['officer2'];}
	elseif($row["rank"] == '12') {$rank = $lang['officer3'];}
	elseif($row["rank"] == '13') {$rank = $lang['officer4'];}
	elseif($row["rank"] == '14') {$rank = $lang['officer5'];}
	elseif($row["rank"] == '15') {$rank = $lang['general'];}
	elseif($row["rank"] == '16') {$rank = $lang['greatgeneral'];}
	elseif($row["rank"] == '17') {$rank = $lang['commander'];}
	elseif($row["rank"] == '18') {$rank = $lang['governor'];}
}