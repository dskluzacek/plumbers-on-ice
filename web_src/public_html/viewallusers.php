
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

	$sql = "SELECT * FROM users";

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	echo "<table>
			<tr>
			<th>ID</th>
			<th>Username</th>
			<th>Password</th>
			<th>First Name</th>
			<th>Last Name</th>
			<th>Email</th>
			<th>Address</th>
			<th>City</th>
			<th>State</th>
			<th>datestamp</th>
			</tr>";
    // output data of each row
    while($row = $result->fetch_assoc()) {
        echo "<tr><td> " . 
						$row["id"] . 
						"</td><td> " .
						$row["username"] . 
						"</td><td> " .
						$row["password"] . 
						"</td><td> " .
						$row["fname"] . 
						"</td><td> " .
						$row["lname"] . 
						"</td><td> " .
						$row["email"] . 
						"</td><td> " .
						$row["address"] . 
						"</td><td> " .
						$row["city"] . 
						"</td><td> " .
						$row["state"] . 
						"</td><td> " .
						$row["datestamp"] . 
						"</td>";
    }
    echo "</table>";
} else {
    echo "0 results";
}

$conn->close();

?>
			 
			 
			</div>

			
		</div>

	</body>
</html>
<?php
	
	if(!isset($_SESSION))
	{
		session_start();
	}

	if($_SESSION["user"] != 'administrator')
	{
    		header("Location: register.php?denied=yes");
	}	
?>	