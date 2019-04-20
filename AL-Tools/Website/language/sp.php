<?PHP
/*
    Mariella, 01/2016, added Language-Support
*/
/*
	&ntilde; = ñ	&Ntilde; = Ñ
	&iacute; = í	&Iacute; = Í
	&oacute; = ó	&Oacute; = Ó
	&aacute; = á	&Aacute; = Á
	&eacute; = é	&Eacute; = É
	&uacute; = ú	&Uacute; = Ú
	&iexcl;  = ¡
	
*/
$lang = array(
        // für die Sprachauswahl
        "de" => "German",
		"gb" => "English",
        "ru" => "Russian",
        "fr" => "French",
        "it" => "Italian",
		"sp" => "Espa&ntilde;ol",

        // für die Seitentexte
        "login" => "Conectar",
        "game"  => "Juego",
        "home" => "Inicio",
        "statistics" => "Estad&iacute;sticas",
        "onlinePlayers" => "Jugadores en l&iacute;nea",
        "top100" => "Top 100",
        "topLegions" => "Top Legions",
        "topRich" => "Top Millonarios",
        "topAbyss" => "Top Abyss",
        "searchPlayers" => "Ver Jugador",
        "downloads" => "Descargas",
        "forum" => "Foro",
        "loginbox" => "Conectar",
        "createAccount" => "Crear Cuenta",
        "forgotPassword" => "Olvid&eacute; mi contrase&ntilde;a",
        "serverStatistics" => "Estad&iacute;sticas del Servidor",
        "accounts" => "Cuentas",
        "characters" => "Personajes",
        "gms" => "GMs",
        "legions" => "Legions",
        "statistic" => "Estad&iacute;sticas",
        "gmsOnline" => "GM Conectado",
        "vote" => "Votar",
        "counters" => "Counter",
        "loginName" => "Nombre de Cuenta",
        "loginPassword" => "Contrase&ntilde;a",
        "nogmonline" => "No hay GMs",
        
        // Login box
        "loggedinas" => "Est&aacute;s conectado como",
        "addministratorAccount" => "Cuenta de Administrador",
        "addDrops" => "A&ntilde;adir Drops",
        "addnews" => "A&ntilde;adir News",
        "changepassword" => "Cambiar Contrase&ntilde;a",
        "changeemail" => "Cambiar E-Mail",
        "exit" => "Desconectar",        

        // PlayerInfo / Abyss / Legion / Online List
        "elyos" => "Elyos",
        "asmodian" => "Asmodian",
        "place" => "Lugar",
        "race" => "Raza",
        "name" => "Nombre",
        "legate" => "Legado",
        "member" => "Miembro",
        "level" => "Nivel",
        "class" => "Clase",
        "location" => "Localizaci&oacute;n",
        "sex" => "Sexo",
        "male" => "Hombre",
        "female" => "Mujer",
        "experience" => "Experiencia",
        "abysspoints" => "Abyss Points",
        "glorypoints" => "Glory Points",
        "rank" => "Rango",
        "kills" => "Muertes",
        "status" => "Estado",
        "title" => "T&iacute;tulo",
        "created" => "Creado",
        "lastVisit" => "&Uacute;ltima Visita",
        "infoaboutPlayer" => "Informaci&oacute;n Sobre el Jugador",
        "infoaboutLegion" => "Informaci&oacute;n Sobre la Legion",
        "listLegion" => "Lista de Legion",
        "ratingAbyss" => "Rating Abyss (AP)",
        "ratingAbyssGp" => "Rating Abyss (GP)",
        "onlineplayers" => "Jugadores en L&iacute;nea",
        "top10players" => "Jugadores del Top 10",
        "top100players" => "Jugadores del Top 100",
        "choice" => "Elige una clase: ",
        "noclass" => "Por favor elige:",
        "allclasses" => "Todas las clases",
	    "brigadegeneral" => "Brigadier General",
	    "centurion" => "Centurion",
	    "legionary" => "Legionary",
	    "recruit" => "Recruit",
        "kinah" => "Kinah",
        
        // Classes
        "warrior" => "Warrior",
        "gladiator" => "Gladiator",
        "templar" => "Templar",
        "scout" => "Scout",
        "assassin" => "Assassin",
        "ranger" => "Ranger",
        "mage" => "Mage",
        "sorcerer" => "Sorcerer",
        "spiritmaster" => "Spiritmaster",
        "priest" => "Priest",
        "cleric" => "Cleric",
        "chanter" => "Chanter",
        "artist" => "Artist",
        "bard" => "Bard",
        "engineer" => "Engineer",
        "gunner" => "Gunner",
        "rider" => "Rider",       
        
        // Abyss-anks
        "soldier" => "Soldier, Rank",
        "officer1" => "Officer with 1 Star",
        "officer2" => "Officer with 2 Stars",
        "officer3" => "Officer with 3 Stars",
        "officer4" => "Officer with 4 Stars",
        "officer5" => "Officer with 5 Stars",
        "general" => "General",
        "greatgeneral" => "Great General",
        "commander" => "Commander",
        "governor"  => "Governor",
        
        // Download
        "downloadFiles" => "Descargar Archivos",
        "client" => "Cliente",
        "launcher" => "Launcher",
        "setup" => "Instalar",
        
        // Account
        "accountInformation" => "Informaci&oacute;n de la cuenta",
        "generalInformation" => "Informaci&oacute;n General",
        "charactersList" => "Lista de Personajes",
        
        // Lost Password
        "lostpassword" => "Hola ~name~\nAlguien (Alomejor t&uacute;) intent&oacute; cambiar la contrase&ntilde;a para conectar.\n\nTu nueva contrase&ntilde;a es: ~newpassword~\n\n &iexcl;Un saludo la Administraci&oacute;n!",
        "pwresetmail" => "Reestablecer contrase&ntilde;a",
        "fillallfields" => "Llenar en todos los campos",
        "pwsendok" => "Se ha enviado un correo con la nueva contrase&ntilde;a",
        "pwnomatch" => "Los datos introducidos no coinciden",
        // Change Password
        "pwchangenomatch" => "Las contrase&ntilde;as no coinciden",
        "pcchangeok" => "Contrase&ntilde;a cambiada con &eacute;xito.",
        "pschangelogin" => "Contrase&ntilde;a cambiada. Conecta de nuevo usando tu nueva contrase;ntilde;a",
        // Change eMail
        "emailoldnew" => "El nuevo e-mail coincide con uno antiguo.",
        "emailformat" => "El campo E-Mail no fue rellenado correctamente",
        "emailchangeok" => "E-Mail cambiado con &eacute;xito",
        "emailnotfound" => "No se encontr&oacute; un usuario con este e-mail."
        );
?>