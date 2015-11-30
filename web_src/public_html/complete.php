
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
		
			<header>
				<?php include('data/header.inc'); ?>
			</header>


			
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

	$sql = "INSERT INTO 
	users (username,password,fname,lname,email,datestamp,state,address,city) 
	VALUES ('".$username."','".$password."','".$fname."','".$lname."','".$email."','".$datestamp."','".$state."','".$address."','".$city."')";

if ($conn->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}
?>


			 
			</div>


		</div>

	</body>
</html>