

<?php
session_start();
     if(!empty($_GET['login'])){
          echo '<div align=right>You have successfully signed in '.$_SESSION['user'].'</div>';
   }
      if(!empty($_GET['logout'])){
          echo '<div align=right>You have successfully logged out</div>';
   }
   if(!empty($_SESSION['user'])){
          echo '<div align=right>You are currently signed in as signed in '.$_SESSION['user'].'</div>';
   }
   if(!empty($_GET['denied'])){
          echo '<div align=right>Please sign in as an administrator to view that page</div>';
   }
 
?>
<link href='https://fonts.googleapis.com/css?family=Lora:400,700' rel='stylesheet' type='text/css'>
