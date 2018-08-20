<?PHP
/*
    Mariella, 01/2016, added Language-Support
*/

/*
    &auml; = ä, 
    &Auml; = Ä
    &ouml; = ö; 
    &Ouml; = Ö
    &uuml; = ü, 
    &Uuml; = Ü
*/

$lang = array(
        // für die Sprachauswahl
        "de" => "German",
		"gb" => "English",
        "ru" => "Russian",
        "fr" => "French",
        "it" => "Italian",

        // für die Seitentexte (Header/Menue und Sidebar)  
        "login" => "Login",
        "game"  => "Spiel",
        "home" => "Home",
        "statistics" => "Statistiken",
        "onlinePlayers" => "Spieler Online",
        "top100" => "Top 100",
        "topLegions" => "Top Legionen",
        "topRich" => "Top Reiche",
        "topAbyss" => "Top Abyss",   
        "searchPlayers" => "Spieler suchen",
        "downloads" => "Downloads",
        "forum" => "Forum", 
        "loginbox" => "Anmelden",
        "createAccount" => "Account erstellen",  
        "forgotPassword" => "Passwort vergessen",
        "serverStatistics" => "Server-Statistik",
        "accounts" => "Accounts",
        "characters" => "Charakter",
        "gms" => "GMs",
        "legions" => "Legionen",
        "statistic" => "Statistik",
        "gmsOnline" => "GM Online",
        "vote" => "Vote",
        "counters" => "Z&auml;hler",
        "loginName" => "Account-Name",
        "loginPassword" => "Passwort",
        "nogmonline" => "Keine GMs Online",
        
        // Login-Box
        "loggedinas" => "Du bist eingeloggt mit",
        "addministratorAccount" => "Administrator Account",
        "addDrops" => "Drops hinzuf&uuml;gen",
        "addnews" => "News hinzuf&uuml;gen",
        "changepassword" => "Passwort &auml;ndern",
        "changeemail" => "Email &auml;ndern",
        "exit" => "Abmelden",        

        // PlayerInfo / Abyss / Legion / Online List
        "elyos" => "Elyos",
        "asmodian" => "Asmodier",
        "place" => "Platz",
        "race" => "Rasse",
        "name" => "Name",
        "legate" => "Leiter",
        "member" => "Mitglied",
        "level" => "Level",
        "class" => "Klasse",
        "location" => "Ort",
        "sex" => "Geschlecht",
        "male" => "m&auml;nnlich",
        "female" => "weiblich",
        "experience" => "Erfahrung",
        "abysspoints" => "Abyss-Punkte",
        "glorypoints" => "Glory-Punkte",
        "rank" => "Rang",
        "kills" => "Kills",
        "status" => "Status",
        "title" => "Titel",
        "created" => "Erstellt",
        "lastVisit" => "letzter Besuch",  
        "infoaboutPlayer" => "Informationen &uuml;ber den Spieler",
        "infoaboutLegion" => "Informationen &uuml;ber die Legion", 
        "listLegion" => "Legions Liste",
        "ratingAbyss" => "Rangliste Abyss (AP)",
        "ratingAbyssGp" => "Rangliste Abyss (GP)",
        "onlineplayers" => "Online Spieler",
        "top10players" => "Top 10 Spieler",
        "top100players" => "Top 100 Spieler",
        "choice" => "Klassenwahl: ",
        "noclass" => "bitte w&auml;hlen",
        "allclasses" => "alle Klassen",
	    "brigadegeneral" => "Brigade General",
	    "centurion" => "Zenturio",
	    "legionary" => "Legion&auml;r",
	    "recruit" => "Rekrut",
        "kinah" => "Kinah",
        
        // Classes
        "warrior" => "Krieger",
        "gladiator" => "Gladiator",
        "templar" => "Templer",
        "scout" => "Sp&auml;her",
        "assassin" => "Assassine",
        "ranger" => "J&auml;ger",
        "mage" => "Magier",
        "sorcerer" => "Zauberer",
        "spiritmaster" => "Beschw&ouml;rer",
        "priest" => "Priester",
        "cleric" => "Kleriker",
        "chanter" => "Kantor",
        "artist" => "K&uuml;nstler",
        "bard" => "Barde",
        "engineer" => "Ingenieur",
        "gunner" => "Sch&uuml;tze",
        "rider" => "&Auml;thertech",        
        
        // Abyss-Ränge
        "soldier" => "Soldat, Rang",
        "officer1" => "Offizier mit 1 Stern",
        "officer2" => "Offizier mit 2 Sternen",
        "officer3" => "Offizier mit 3 Sternen",
        "officer4" => "Offizier mit 4 Sternen",
        "officer5" => "Offizier mit 5 Sternen",
        "general" => "General",
        "greatgeneral" => "Grossgeneral",
        "commander" => "Oberbefehlshaber",
        "governor"  => "Statthalter",
        
        // Download
        "downloadFiles" => "Download Files",
        "client" => "Client",
        "launcher" => "Launcher",
        "setup" => "Setup",
        
        // Account
        "accountInformation" => "Account Informationen",
        "generalInformation" => "Allgemeine Informationen",
        "charactersList" => "Characters Liste",
        
        // Lost Password
        "lostpassword" => "Hallo ~name~\njemand (vielleicht du) hat versucht, dein Passwort zu Ändern.\n\nDein neues Passwort: ~newpassword~\n\nMit freundlichen Grüssen Projektverwaltung!",
        "pwresetmail" => "Passwort zur&uuml;cksetzen",
        "fillallfields" => "alle Felder ausf&uuml;llen",
        "pwsendok" => "Dein neues Passwort wurde dir per eMail geschickt",
        "pwnomatch" => "die Eingabedaten stimmen nicht &uuml;berein",
        // Change Password
        "pwchangenomatch" => "die neuen Passworte stimmen nicht &uuml;berein",
        "pcchangeok" => "Passwort wurde ge&auml;ndert",
        "pschangelogin" => "Passwort wurde ge&auml;ndert. Melde dich wieder mit dem neuen Passwort an.",
        // Change eMail
        "emailoldnew" => "Alte und neue eMail sind identisch",
        "emailformat" => "eMail-Angabe ist nicht korrekt",
        "emailchangeok" => "E-Mail wurde ge&auml;ndert",
        "emailnotfound" => "Benutzer mit dieser eMail-Adresse nicht gefunden"
        );
?>