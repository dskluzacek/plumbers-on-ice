		<?php
			
		?>

<html>
	<head>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
		<link rel="stylesheet" type="text/css" href="css/style.css">
		<script src="js/scripts.js"></script>
		<script src="js/jquery.validate.min.js"></script>

		<title>
			Plumbers on ice
		</title>

	</head>
	<body>
		<div id="splashbg">
			<header>
				<?php include('data/header.inc'); ?>
			</header>

			<div id="top-logo" class="centered-h">
				
			</div>

			<div id="navi" class="centered-h">
			
			</div>	
			
			<div id="content-pane" class="centered-h">
				 


			<?php
				include('data/menu.inc');
				include("data/db.inc");
				if(!isset($_SESSION))
				{
					session_start();
				}

				if (isset($_GET['remove'])) {
					$x = 0;
					for ( $x; $x < count($_SESSION['cart']); $x++) {
						if ( $_SESSION['cart'][$x] == $_GET['remove'] ) {
							unset($_SESSION['cart'][$x]);
							$_SESSION['cart'] = array_values($_SESSION['cart']);
						}
					}	
				}
					echo "Shopping Cart List";

					echo "<table>
					<tr>
					<th>Item Name</th>
					<th>Unit Price</th>
					
					
					</tr>";
				$i = 0;
				$total = 0;
				$price = 0;
    				while( $i < count($_SESSION['cart'])) {
					$food = $_SESSION['cart'][$i];
				

					$sql1 = "SELECT price FROM assets WHERE name='".$food."'";
					$result = $conn->query($sql1);
					if ($result->num_rows > 0) {
    						while($row = $result->fetch_assoc()) {
      							$price = $row["price"];
   						}
					} else {
    						echo "0 results";
				}
					$linetotal = $price; 
					$total = $total + $linetotal;

					echo "<tr><th>" 
					."$food". 

					"</th><th>" 
					."$price".
					"</th><th>" 
					."<form method=post action=shopcart.php?remove=" . "$food" . "><input type=submit value=Remove class=btnAddAction /></form>".
					 "</th><tr>";
					$i = $i+1;
				}
    				
  					echo "</table>";
			echo "Total: $" . "$total";	
			
			
			?>
			<form method=post action=orderreview.php>
			<input type=text name='shipaddress' placeholder='Enter a shipping address' size=20 />
			<input type=submit value='Click to checkout' class=btnAddAction />
			</form>        
			
			<?php
			if(isset($_GET['noship'])) {
				if($_GET['noship'] == 'yes') {
					echo "Please enter in a shipping address";
				}
			}
			if(isset($_GET['noship'])) {
				if($_GET['noship'] == 'noitem') {
					echo "You cannot proceed with an empty cart";
				}
			}
			?>

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
