<?php
$servername = 'mysql.server303.com';
//$port = '3306';
$username = 'testuser';
$password = 'testdb';
$dbname = 'jhoffoss_test';
$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

?>