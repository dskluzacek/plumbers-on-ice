<?php

include("data/db.inc");

if($_GET["level"] === "grassy") {
    $levelName = "Grassy Level";
	$query = "select score, username from highscores where levelNumber=1 order by score desc limit 10";
    
}

if($_GET["level"] === "tropical") {
	$levelName = "Tropical Level";
    $query = "select score, username from highscores where levelNumber=2 order by score desc limit 10";
}

if($_GET["level"] === "fall") {
	$levelName = "Fall Level";
    $query = "select score, username from highscores where levelNumber=3 order by score desc limit 10";
}

if($_GET["level"] === "icy") {
	$levelName = "Icy Level";
    $query = "select score, username from highscores where levelNumber=4 order by score desc limit 10";
}

if($_GET["level"] === "castle") {
	$levelName = "Castle Level";
    $query = "select score, username from highscores where levelNumber=5 order by score desc limit 10";
}


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
            <div id="welcome-logo" class="centered-h">

            </div>

            <div id="top-logo" class="centered-h">
                <?php include('data/menu.inc'); ?>
            </div>

            <div id="content-pane" class="centered-h">
                <div id="navi" class="centered-h">


                    <h3><?php echo $levelName ?> Top 10 Scores</h3>
                    <?php 
    
    
    $result = $conn->query($query);
    $num_results = $result->num_rows;
    for ($i=0; $i < $num_results; $i++) {
        $row = $result->fetch_assoc();
        echo stripcslashes($row['username']);
        echo ": ";
        echo stripcslashes($row['score']);
        echo "<br />";
    }
    $result->free();
    $conn->close();
    
?>

                </div>

                <footer class="centered-h">
                    <?php include('data/footer.inc'); ?>
                </footer>

            </div>
        </div>
    </body>

    </html>