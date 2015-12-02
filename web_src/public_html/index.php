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

        <div id="welcome-logo" class="centered-h">
            <h1>Welcome to the Plumbers on Ice webpage!!</h1>
        </div>

        <div id="welcome-logo" class="centered-h">


            <?php include('data/menu.inc'); ?>

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