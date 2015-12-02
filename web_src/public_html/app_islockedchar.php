<?php

include("data/db.inc");
$username = $_POST['usernamelogin'];
$asset = $_POST['asset'];
$username = mysql_real_escape_string($username);
$asset = mysql_real_escape_string($asset);

$sql1 = "SELECT orderid FROM orders WHERE username='".$username."' AND name='".$asset."'";
					$result = $conn->query($sql1);
					if ($result->num_rows > 0) {
    						while($row = $result->fetch_assoc()) {
      							$ordernum = $row["orderid"];
      							if($ordernum > 0) {
      								echo '1';
      							} else {
      								echo '0';
      							}
   						}
					} else {
    						echo "0 results";
				}

?>
