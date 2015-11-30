

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

if(!isset($_SESSION))
	{
		session_start();
	}

	include("data/db.inc");
		$username = $_POST['usernamelogin'];
		$password1 = $_POST['passwordlogin'];
		$password = sha1($password1);

	$sql = "SELECT password FROM users WHERE username='" . $username . "'";

$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        $upass = $row["password"];
    }
} else {
    echo "0 results";
}

if ($upass == $password) {
	
	$_SESSION['user'] = $username;
	if($_SESSION["user"] == 'administrator')
		{
    		header("Location: viewallusers.php?login=yes");
	}
	if($_SESSION["user"] != 'administrator')
		{
    		header("Location: index.php?login=yes");
	}
	
    
} else {
	echo "Username/Password Incorrect. -- " . $upass . " ::: " . $password1;
}
$conn->close();

?>

			 
			</div>

	
		</div>

	</body>
</html>

