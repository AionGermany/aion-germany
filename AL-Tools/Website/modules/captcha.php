<?php
// ServerAion Web by Pr00f & Sky (serveraion.ru)

session_start();

$string = '';
	
	for ($i = 0; $i < 5; $i++) {// cycle responsible for the number of generated numbers.
		$string .= chr(rand(97, 122));
	}
	
	$_SESSION['rand_code'] = $string;

$dir = 'fonts/';

$image = imagecreatetruecolor(120, 30);
$black = imagecolorallocate($image, 0, 0, 0);
$color = imagecolorallocate($image, 122, 139, 0);
$white = imagecolorallocate($image, 255, 255, 255);

imagefilledrectangle($image,0,0,399,99,$white);
imagettftext ($image, 24, 0, 0, 25, $color, $dir."arial.ttf", $_SESSION['rand_code']);

header("Content-type: image/png");
imagepng($image);