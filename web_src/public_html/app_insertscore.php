<?php

include("data/db.inc");
$username = $_POST['usernamelogin'];
$levelnumber = $_POST['levelNumber'];
$score = $_POST['score'];
$username = mysql_real_escape_string($username);
$levelnumber = mysql_real_escape_string($levelnumber);
$score = mysql_real_escape_string($score);

$sql = "INSERT INTO 
	highscores (levelNumber, username, score) 
	VALUES ('".$levelnumber."','".$username."','".$score."')";

if ($conn->query($sql) === TRUE) {
    
} else {
    echo "Error connecting to the database";
}

?>
