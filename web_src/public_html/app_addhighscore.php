<?php

include("data/db.inc");
$username = $_POST['usernamelogin'];
$levelnumber = $_POST['levelNumber'];
$username = mysql_real_escape_string($username);
$levelnumber = mysql_real_escape_string($levelnumber);
$scorehold = 0;
$sql1 = "SELECT score FROM highscores WHERE username='".$username."' AND levelNumber='".$levelnumber."'";
					$result = $conn->query($sql1);
					if ($result->num_rows > 0) {
    						while($row = $result->fetch_assoc()) {
      							$score = $row["score"];
      							if($scorehold < $score) {
      								$scorehold = $score;
      							}
   						}
					} else {
    						echo "0 results";
				}

?>
