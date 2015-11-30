

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
		$username = $_POST['username'];
		$password = $_POST['password'];
		$password = sha1($password);
		$fname = $_POST['firstname'];
		$lname = $_POST['lastname'];
		$email = $_POST['email'];
		$datestamp = date("Y-m-d");
		$state = $_POST['state'];
		$city = $_POST['city'];
		$address = $_POST['address'];

	$sql = "UPDATE users 
	SET password= '".$password."'
	WHERE username = '".$username."'";

if ($conn->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}
?>


			 
			</div>

			<footer class="centered-h">
				<?php include('data/footer.inc'); ?>
			</footer>
		</div>

	</body>
</html>