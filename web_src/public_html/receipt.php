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
				
				if(!isset($_SESSION))
				{
					session_start();
				}
				$usr = $_SESSION['user'];
				$sql1 = "SELECT * FROM users WHERE username='".$usr."'";
					$result = $conn->query($sql1);
					if ($result->num_rows > 0) {
    						while($row = $result->fetch_assoc()) {
      							$firstn = $row["fname"];
							$lastn = $row["lname"];
							
   						}
					} else {
    						echo "0 results";
				}
				
			
				echo "Checkout Workflow: Receipt <br/>";
					
					echo
						"<br/>Customer Name: <br/> "
						. "$firstn" . 
						" "
						. "$lastn"
						;
					echo "<br/><table>
					<tr>
					<th>Description</th>
					<th>Quantity Ordered</th>
					<th>Unit Price</th>
					<th>Line item total</th>
					
					</tr>";
				$i = 0;
				$total = 0;
				$tax = 0;
				
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
			$grandtotal = round(($total + $tax), 2);
			echo "Pre Tax Total: $" 
			. "$total" .
			"<br>Tax: $"
			. "$tax" .
			"<br>Grand Total: $"
			. "$grandtotal";
			
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

	if($_SESSION["admin"] != '1' && $_SESSION["admin"] != '0')
	{
    		header("Location: signin.php?denied=yes");
	}	
?>	
