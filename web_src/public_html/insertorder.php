<?php
/** GROUP F - Falcons
@author: James Hoffoss, Jamil Sabir, Maxine Vang, Nathan Olson, Tsega Terefe
@desc: this company might offer a home delivery service, this would have informationfor those interested.

*/	
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
			 
			<?php
				include("data/db.inc");
				if(!isset($_SESSION))
				{
					session_start();
				}
				
				

				$date = date("Y, j, n");
				$i = 0;
				$qtyy = $_SESSION['user'];
				
				while($i < count($_SESSION['cart'])) {
					$itm = $_SESSION['cart'][$i];
					$sql = "INSERT INTO orders ( name, username, date) VALUES ('".$itm."' , '".$qtyy."' , '".$date."')";
					if ($conn->query($sql) === TRUE) {
  						echo "Changes successfully made";
					} else {
						echo "Error: " . $sql . "<br>" . $conn->error;
					}
				$i = $i + 1;
				}
				header("Location: receipt.php");
			?>
			 
			 
			</div>

			<footer class="centered-h">
				<?php include('data/footer.inc'); ?>
			</footer>
		</div>

	</body>
</html>
<?php
	
	if(!isset($_SESSION))
	{
		session_start();
	}

	if($_SESSION["admin"] != '1' && $_SESSION["admin"] != '0')
	{
    		header("Location: signin.php?denied=yes");
	}	
?>	