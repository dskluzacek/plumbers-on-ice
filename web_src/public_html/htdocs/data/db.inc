<?php
$servername = 'db4free.net';
//$port = '3306';
$username = 'ics499';
$password = 'test1234';
$dbname = 'icsics';
$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

?>