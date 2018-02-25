<?php
session_start();
session_destroy();
Header("Location: index.php");
?>