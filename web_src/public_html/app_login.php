<?php
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
}

if ($upass == $password) {
    echo '1';
} else {
    echo '0';
}

?>
