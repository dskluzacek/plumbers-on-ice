<?php

?>

<html>
	<head>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
		<link rel="stylesheet" type="text/css" href="css/style.css">
		<script src="js/scripts.js"></script>

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
			 





	<form method=get action=processpayment.php >
		<p>Checkout Workflow: Payment</p>
		<p>Enter your credit card details:</p>
		<br/>
		<input type=text name=name placeholder='Enter full name on card' size=20 /><br/>
		<input type=text name=creditno placeholder='Enter credit card number' size=20 /><br/>
		<input type=text name=exp placeholder='Expiration date' size=10 /><br/>
		<input type=text name=cvc placeholder='CVC number' size=10 /><br/>
		<input type=submit value='Submit Payment' class=btnAddAction />
		
		
	</form>       



			 
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
