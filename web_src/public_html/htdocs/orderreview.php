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
			Plumbers on Ice
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
				<?php include('data/menu.inc'); ?>
			</div>	
			
			<div id="content-pane" class="centered-h">
				 
			<?php 
			include("data/db.inc");
			if($_POST['shipaddress'] == '') {
				header("Location: shopcart.php?noship=yes");
			}
			$_SESSION['shipaddress'] = $_POST['shipaddress'];
			
				echo "Checkout Workflow: Order Review <br><br>";

					echo "<table>
					<tr>
					<th>Description</th>
					<th>Quantity Ordered</th>
					<th>Unit Price</th>
					<th>Line item total</th>
					
					</tr>";
				$i = 0;
				$total = 0;
				$tax = 0;
				$shipcost = 5;
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
					."".
					"</th><th>" 
					."$price".
					"</th><th>$" 
					."$linetotal".
					 "</th><tr>";
					$i = $i+1;
				}
    				
  					echo "</table>";
			$tax = $total * 0.06875;
			$grandtotal = round(($total + $tax + $shipcost), 2);
			echo "Pre Tax Total: $" 
			. "$total" .
			"<br>Tax: $"
			. "$tax" .
			"<br>Shipping (flat rate): $"
			. "$shipcost" .
			"<br>Grand Total: $"
			. "$grandtotal";
			
			?>
			<form method=post action=payment.php>
			<input type=submit value='Click here to pay!' class=btnAddAction />
			</form>        
			        
			 
			 <footer class="centered-h">
				<?php include('data/footer.inc'); ?>
			</footer>
			</div>

			
		</div>

	</body>
</html>

