<?php
/** GROUP F - Falcons
@author: James Hoffoss, Jamil Sabir, Maxine Vang, Nathan Olson, Tsega Terefe
@desc: this is the heart of the app. showcasing the many departments of food available dynamically via PHP and MySQL.

*/

if($_GET["dept"] === "Meat") {
	$img1 = "<img src='img/steak.png'>";
	$img2 = "<img src='img/placeholder.png'>";
	$item1 = "<a href='#'>Steak</a><br>";
	$item2 = "<a href='#'>Bacon</a><br>" . "$8.69/lb";
	$desc1 = "<a>$8.69/lb</a><br>";
}

if($_GET["dept"] === "Fruit") {
	$img1 = "<img src='img/placeholder.png'>";
	$img2 = "<img src='img/placeholder.png'>";
	$item1 = "<a href='#'>Apple, Green</a><br>" . "$1.99/lb";
	$item2 = "<a href='#'>Apple, Red</a><br>" . "$1.69/lb";
}

if($_GET["dept"] ===  "bakery") {
	$img1 = "<img src='img/placeholder.png>";
	$img2 = "<img src='img/placeholder.png'>";
	$item1 = "<a href='#'>Cheesecake</a><br>" . "$2.99 ea";
	$item2 = "<a href='#'>French Silk Pie</a><br>" . "$2.99 ea";
}

if($_GET["dept"] === "dairy") {
	$img1 = "<img src='img/placeholder.png'>";
	$img2 = "<img src='img/placeholder.png'>";
	$item1 = "<a href='#'>Whole Milk</a><br>" . "$2.39 per gal";
	$item2 = "<a href='#'>Cheeeeeeseee!</a><br>" . "$1.11/lb";
}


?>

<html>
	<head>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
		<link rel="stylesheet" type="text/css" href="css/style.css">
		<script src="js/scripts.js"></script>

		<title>
			Falcon Foods - Where those big chain prices are For the Birds!
		</title>
	</head>
	<body>
		<div id="splashbg">
			<header>
				<?php include('data/header.inc'); ?>
			</header>

			<div id="top-logo" class="centered-h">
				<img src="img/falcfoodsplash.png" title="Falcon Foods" alt="Falcon Foods Logo" class="centered-h centered-v">	
			</div>

			<div id="navi" class="centered-h">
				<?php include('data/menu.inc'); ?>
			</div>	

			<div id="content-pane" class="centered-h">
			 <h3>Products</h3>


<?php
	include("data/db.inc");
$dept = $_GET['dept'];
	$sql = "SELECT * FROM product WHERE CATEGORYNAME = '".$dept."'" ;

$result = $conn->query($sql);

if ($result->num_rows > 0) {
 
    while($row = $result->fetch_assoc()) {
	
        echo "<div class=item-box><img src='"
			
			 . "$row[imagepath]" . "' height='100'><br>"
			 ."$row[name]" . "<br>"
			 ."$row[price]" . "<br>"
			 ."<form method=post action=departments.php?dept=" . "$dept" . "&action=" . "$row[name]" . "><input type=text name=quantity value=1 size=2 /><input type=submit value=Add class=btnAddAction /></form>" .
			 "</div>" ;
    }
    
} else {
    echo "0 results";
}

if (isset($_GET['action'])) {

	$sql1 = "SELECT qty FROM product WHERE name='".$_GET['action']."'";

	$result = $conn->query($sql1);

	if ($result->num_rows > 0) {
    	while($row = $result->fetch_assoc()) {
      		$quant = $row["qty"];
   	}
	} else {
    		echo "0 results";
	}

	for ($i = 0; $i < count($_SESSION['cart']); $i++) {
		if ( $_SESSION['cart'][$i] == $_GET['action'] ) {
			if ( ($_SESSION['cart'][$i+1] + $_POST['quantity']) < $quant) {
				$_SESSION['cart'][$i+1] = $_SESSION['cart'][$i+1] + $_POST['quantity'];
			} else {
				$_SESSION['cart'][$i+1] = $quant;
				echo "The max inventory quantity of the selected item is in your shopping cart";
			}
		}
	}
	if( !in_array($_GET['action'], $_SESSION['cart'])) {
		array_push($_SESSION['cart'], $_GET['action'], $_POST['quantity']);
	}
	echo "You have successfully added an item to your cart";
}
$conn->close();

?>

			
			 
			 
			 
			</div>

			<footer class="centered-h">
				<?php include('data/footer.inc'); ?>
			</footer>
		</div>

	</body>
</html>