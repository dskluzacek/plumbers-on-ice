<?php

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

			<div id="top-logo" class="centered-h">
					
			</div>

			<div id="navi" class="centered-h">
				<?php include('data/menu.inc'); ?>
				
			</div>	
			
			<div id="content-pane" class="centered-h">
			 <form action="signin.php">
				<button type="submit">Back to sign in</button>



<?php
if(!isset($_SESSION))
	{
		session_start();
	}
	include("data/db.inc");
		$username = $_POST['usernamelogin'];
		$password1 = $_POST['passwordlogin'];
		$password = sha1($password1);

	$sql = "SELECT password FROM users WHERE username='" . $username . "'";
	$sql1 = "SELECT * FROM users WHERE username='" . $username . "'";
	

$result = $conn->query($sql);
$result1 = $conn->query($sql1);
$upass = "xvit0239jf23f098j4ikj3m0498435jrjf304";
if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        $upass = $row["password"];
    }
} else {
    
}
if ($result1->num_rows > 0) {
    // output data of each row
    while($row = $result1->fetch_assoc()) {
        $adminid = $row["admin"];
    }
} else {
    
}



if ($upass == $password) {
	
	$_SESSION['user'] = $username;
	$_SESSION['admin'] = $adminid;
	$_SESSION['cart'] = array();
	$_SESSION['id'] = $idd;
	
	if($_SESSION["admin"] == '1')
		{
    		header("Location: index.php?login=yes");
	}
	if($_SESSION["admin"] != '1')
		{
    		header("Location: index.php?login=yes");
	}
	
    
} else {
	echo "Username/Password Incorrect";
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

