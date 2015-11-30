<?php


if($_GET["shop"] === "chroma") {
	$img1 = "<img src='img/redgoomba.png'>";
	$img2 = "<img src='img/placeholder.png'>";
	$item1 = "<a href='#'>Red Goomba</a><br>";
	$itemname = "redg";
	$item2 = "<a href='#'>Blue Goomba</a><br>" . "$3.54";
	$desc1 = "<a>$8.69</a><br>";
}

if($_GET["shop"] === "character") {
	$img1 = "<img src='img/placeholder.png'>";
	$img2 = "<img src='img/placeholder.png'>";
	$item1 = "<a href='#'>Apple, Green</a><br>" . "$1.99/lb";
	$item2 = "<a href='#'>Apple, Red</a><br>" . "$1.69/lb";
}

if($_GET["shop"] ===  "level") {
	$img1 = "<img src='img/placeholder.png>";
	$img2 = "<img src='img/placeholder.png'>";
	$item1 = "<a href='#'>Cheesecake</a><br>" . "$2.99 ea";
	$item2 = "<a href='#'>French Silk Pie</a><br>" . "$2.99 ea";
}


?>

<html>
	<head>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
		<link rel="stylesheet" type="text/css" href="css/style.css">
		<script src="js/scripts.js"></script>

		<title>
			Plumbers on ice
		</title>
	</head>
	<body>
		<div id="splashbg">
		<header>
			<?php include('data/header.inc'); ?>
				
		</header>
			<div id="welcome-logo" class="centered-h">
			
			</div>

		<div id="top-logo" class="centered-h">
			<?php include('data/menu.inc'); ?>
		</div>

			
				
			

		<div id="content-pane" class="centered-h">
		<div id="navi" class="centered-h">
			 
		
<h3>Products</h3>
<?php
echo "<div>" . 
	"$img1" .

"</div><div>" . 
	"$item1" .

"</div><div>" . 
	"$desc1" .
"</div><div><form method=post action=departments.php?shop=" . "$_GET[shop]" . "&action=" . "$itemname" . "><input type=submit value=Purchase class=btnAddAction /></form>" .
"</div>"


?>			
			
			 
			 
			
			</div>

			<footer class="centered-h">
				<?php include('data/footer.inc'); ?>
			</footer>
			
		</div>
	</div>
	</body>
</html>