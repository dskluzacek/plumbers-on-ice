<?php


if($_GET["shop"] === "chroma") {
	$img1 = "<img src='img/redgoomba.png'>";
	$item1 = "<a>Red Goomba</a><br>";
	$itemname = "RedGoomba";
	$desc1 = "<a>$8.69</a><br>";

	$img2 = "<img src='img/bluegoomba.png'>";
	$item2 = "<a>Green Goomba</a><br>";
	$itemname2 = "GreenGoomba";
	$desc2 = "<a>$2.44</a><br>";

	$img3 = "<img src='img/greenbot1.png'>";
	$item3 = "<a>Green Bot</a><br>";
	$itemname3 = "GreenBot";
	$desc3 = "<a>$10.22</a><br>";
	

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
"</div>";

echo "</br><div>" . 
	"$img2" .

"</div><div>" . 
	"$item2" .

"</div><div>" . 
	"$desc2" .
"</div><div><form method=post action=departments.php?shop=" . "$_GET[shop]" . "&action=" . "$itemname2" . "><input type=submit value=Purchase class=btnAddAction /></form>" .
"</div>";

echo "</br><div>" . 
	"$img3" .

"</div><div>" . 
	"$item3" .

"</div><div>" . 
	"$desc3" .
"</div><div><form method=post action=departments.php?shop=" . "$_GET[shop]" . "&action=" . "$itemname3" . "><input type=submit value=Purchase class=btnAddAction /></form>" .
"</div>";

if (isset($_GET['action'])) {


	if( !in_array($_GET['action'], $_SESSION['cart'])) {
		array_push($_SESSION['cart'], $_GET['action']);
	}
	echo "You have successfully added an item to your cart";
}


?>			
			
			 
			 
			
			</div>

			<footer class="centered-h">
				<?php include('data/footer.inc'); ?>
			</footer>
			
		</div>
	</div>
	</body>
</html>

<?php
	
	if(!isset($_SESSION))
	{
		session_start();
	}

	if(!isset($_SESSION["admin"]))
	{
    		header("Location: signin.php?denied=yes");
	}	
?>	
