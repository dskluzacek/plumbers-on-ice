<?php
	session_start();
	$_SESSION = array();
	session_destroy();
	header("Location: index.php?logout=yes");
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
			</div>
			

			<footer class="centered-h">
				<?php include('data/footer.inc'); ?>
			</footer>
		</div>

	</body>
</html>
