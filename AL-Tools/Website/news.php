<?php
$sql_ws = new ClassMysql();
$sql_ws->connect($dbhost,$dbuser,$dbpassword,$dbnews);
$res    = $sql_ws->query("SELECT `id_news`,`title`,`date`,`content` FROM `news`");

while ($row = $sql_ws->fetch_assoc($res)) 
{
	$id_news = $row['id_news'];
	$title = $row['title'];
	$date = $row['date'];
	$content = $row['content'];
}


if ( !isset( $_GET["action"] ) ) $_GET["action"] = "showlist";
 
switch ( $_GET["action"] ) {
	// Withdraw all the news
	case "showlist":
		show_list(); break;
	
    // Form for adding news
	case "addnews":
		if (isset($_SESSION['id']) AND $_SESSION['access_level'] >= "$access_level") {
			get_add_item_form(); break;
		}
		else {echo "Error";}
	
    // Add news
	case "add":
		if (isset($_SESSION['id']) AND $_SESSION['access_level'] >= "$access_level") {
			add_item(); break;
		}
		else return;
		
    // Output separate news
	case "get_news":
		get_news(); break;
	
    // Form for news editing
	case "editform":
		if (isset($_SESSION['id']) AND $_SESSION['access_level'] >= "$access_level") {
			get_edit_item_form();
		}
		else {echo "Error";}
	break;
	
    // Update news
	case "update":
		if (isset($_SESSION['id']) AND $_SESSION['access_level'] >= "$access_level") {
			update_item(); break;
		}
		else {echo "Error";}
	
    // Remove the news
	case "delete":
		if (isset($_SESSION['id']) AND $_SESSION['access_level'] >= "$access_level") {
			delete_item(); break;
		}
		else return;

    // By default, - the withdrawal of all news
	default:
		show_list();
}

$sql_ws->close();

// Lists all records in the database table
function show_list() 
{
    global $sql_ws;
	global $news_amt;
	global $access_level;
	
	if (isset($_GET['page'])) {
		$page = intval($_GET['page']);
	}
	else {
		$page = 1;
	}

    $elements = $sql_ws->result($sql_ws->query("SELECT COUNT(*) AS `counter` FROM `news`"),0);
	$pages = ceil($elements/$news_amt);

	if ($page < 1) {
		$page = 1;
	}
	elseif ($page > $pages) {
		$page = $pages;
	}
	$start = ($page-1)*$news_amt;
	if ($start < 0) $start = 0;

	$bef = "SELECT * FROM news ORDER BY id_news DESC LIMIT {$start}, {$news_amt}";
	$res = $sql_ws->query($bef);

	while ($item = $sql_ws->fetch_assoc($res)) 
    {
		$id_news = $item['id_news'];
		
		echo '<div class="news">';
		echo '<div class="news_title"><a href="'.$_SERVER['PHP_SELF'].'?action=get_news&id_news='.$item['id_news'].'">'.$item['title'].'</a></div>';
		echo '<div class="news-text"><hr style="color:#000">'.$item['content'].'</div>';
		echo '<div class="news-date"><hr style="color:#000">'.$item['date'].'</div>';
        
		if (isset($_SESSION['id']) AND $_SESSION['access_level'] >="$access_level") {
			echo '<a href="'.$_SERVER['PHP_SELF'].'?action=editform&id_news='.$item['id_news'].'"><div class="button-edit border shadow">Edit</div></a>';
		}
		echo '<div class="clear"></div>';
		echo '</div>';
	}
	
	echo '<div class="nav_news">';
	echo '<div class="nextprev" id="paginator3"></div>';
	echo '<script type="text/javascript"> pag3 = new Paginator(\'paginator3\', '.$pages.', 5, '.$page.', ""); </script>';
	echo '</div>';
}

// Conclusion news separately
function get_news() {
	global $sql_ws, $access_level;
	$id_news = intval($_GET['id_news']);
    
	$res = $sql_ws->query("SELECT * FROM `news` WHERE `id_news` = ".$id_news);
    
	while ($item = $sql_ws->fetch_assoc($res)) 
    {
		$title = $item['title'];
		$date = $item['date'];
		$content = $item['content'];
		
		echo '<div class="news">';
		echo '<div class="news-title">'.$title.'</div>';
		echo '<div class="news-date">'.$date.'</div>';
		echo '<div class="news-text">'.$content.'</div>';
        
		if (isset($_SESSION['id']) AND $_SESSION['access_level'] >= "$access_level") {
			echo '<a href="'.$_SERVER['PHP_SELF'].'?action=editform&id_news='.$item['id_news'].'"><div class="button-edit border shadow">Edit</div></a>';
		}
		echo '<div class="clear"></div>';
		echo '</div>';
	}
}

// Function generates a form to add a record to a database table
function get_add_item_form() 
{
	$date=date("d M Y, H:i");
	
	echo '<div class="news">';
	echo '<div class="news-title">Add News</div>';
	echo '<div>
		<form name="addnews" action="'.$_SERVER['PHP_SELF'].'?action=add" method="POST">';
    echo '<div class="mb10"><b>Header:</b> <input type="text" class="field" name="title"></div>';
    echo '<div class="mb10"><b>Date:</b> '.$date.'</div>';
    echo '<div><textarea type="text" id="input" name="content"></textarea></div>';
    echo '<div align="center"><input type="submit" class="button-submit dark-shadow" name="button" value="Add"/> <button type="button" class="button-submit dark-shadow" onClick="history.back();">Cancel</button></div>';
    echo '</form>';
	echo '</div>';
	if(!empty($add_error)) echo '<center><div class="error">'.$add_error.'</div>';
	if(!empty($add_success)) echo '<div class="accept">'.$add_success.'</center></div>';
	echo '</div>';
	echo '<div class="clear"></div>';
}

// This function adds a new record to the database table
function add_item() 
{
    global $sql_ws;
    
	$date=date("d M Y, H:i");
	$title = mysql_escape_string( $_POST['title'] );
	$content = mysql_escape_string( $_POST['content'] );
	$sql_ws->query("INSERT INTO news (title, date, content) VALUES ('".$title."', '".$date."', '".$content."');");
    
	echo "<script language='JavaScript'>window.location.href = 'index.php'; </script>";
	die();
}

// Function generates a form to edit records in the database table
function get_edit_item_form() 
{
    global $sql_ws;
    
	$date=date("d M Y, H:i");
	$res = $sql_ws->query( 'SELECT id_news, title, content FROM news WHERE id_news='.$_GET['id_news'] );
	$item = $sql_ws->fetch_array( $res );
	
	echo '<div class="news">';
	echo '<div class="news_lc">Edit News</div><br>';
	echo '<div>
		<form name="editform" action="'.$_SERVER['PHP_SELF'].'?action=update&id_news='.$_GET['id_news'].'" method="POST">';
    echo '<div class="mb10"><b>Header:</b> <input type="text" class="field" name="title" value="'.$item['title'].'"></div>';
    echo '<div class="mb10"><b>Date:</b> '.$date.'</div>';
    echo '<div><textarea type="text" id="input" name="content">'.$item['content'].'</textarea></div>';
    echo '<div align="center">
			<input type="submit" class="button_reg" name="button" value="Save"/>
			<button type="button" class="button_reg" onClick="history.back();">Cancel</button>
			<button type="button" class="button_reg" ONCLICK="stat()">Remove</button>
			<script>function stat(){location.href="'.$_SERVER['PHP_SELF'].'?action=delete&id_news='.$item['id_news'].'";}</script>
		</div>';
	echo '</form>';
	echo '</div>';
    
	if(!empty($add_error)) echo '<center><div class="error">'.$add_error.'</div>';
	if(!empty($add_success)) echo '<div class="accept">'.$add_success.'</center></div>';
    
	echo '</div>';
	echo '<div class="clear"></div>';
}

// Function updates the record in the database table
function update_item() 
{
    global $sql_ws;
    
	$title = mysql_escape_string( $_POST['title'] );
	$content = mysql_escape_string( $_POST['content'] );
	$sql_ws->query( "UPDATE news SET title='".$title."', content='".$content."' WHERE id_news=".$_GET['id_news']);
    
	echo "<script language='JavaScript'>window.location.href = 'index.php'; </script>";
	die();
}

// Function deletes a record in the database table
function delete_item() 
{
    global $sql_ws;
    
	$sql_ws->query( "DELETE FROM news WHERE id_news=".$_GET['id_news'] );
    
	echo "<script language='JavaScript'>window.location.href = 'index.php'; </script>";
	die();
}