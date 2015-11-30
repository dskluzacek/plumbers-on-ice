	


<html>
	<head>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
		<link rel="stylesheet" type="text/css" href="css/style.css">
		<script src="js/scripts.js"></script>
		<script src="js/jquery.validate.min.js"></script>

		<title>
			Falcon Foods - Where those big chain prices are For the Birds!
		</title>

	</head>
	<body>
		<div id="splashbg">
			<header>
				<?php include('data/header.inc'); ?>
			</header>

			<div id="top-logo" class="centered-h">
				<img src="img/falcfoodsplash.png" title="Falcon Foods" alt="Falcon Foods Logo" class="centered-h centered-v">	
			</div>

			<div id="navi" class="centered-h">
				<?php include('data/menu.inc'); ?>
			</div>	
			
			<div id="content-pane" class="centered-h">
				 
			

			        <th>
			            <h1>Change user details</h1>
			            <form action="editusercomplete.php" method="POST" name="signup" id="signup">
			                <div>
			                    <label for="username" class="label">User Name * </label>
			                    <input name="username" type="text" class="required" id="username" title="Please type your username." placeholder="Enter Your Username">
			                    
			                </div>
			                <div>
			                    <label for="password" class="label">Password * </label>
			                    <input name="password" type="password" id="password" placeholder="Enter Your Password.">
			                    
			                </div>
			                <div>
			                    <label for="confirm_password" class="label">Confirm Password * </label>
			                    <input name="confirm_password" type="password" id="confirm_password" placeholder="Confirm Your Password">
			                </div>
			                <div>
			                    <label for="firstname" class="label">First Name * </label>
			                    <input name="firstname" type="text" class="required" id="firstname" title="Please type your first name." placeholder="Enter Your Name">
			                    
			                </div>
			                <div>
			                    <label for="lastname" class="label">Last Name * </label>
			                    <input name="lastname" type="text" class="required" id="lastname" title="Please type your last name." placeholder="Enter Your Name">
			                    
			                </div>
			                <div>
			                    <label for="address" class="label">Street Address * </label>
			                    <input name="address" type="text" class="required" id="address" title="Please type your address." placeholder="Enter Your Address">
			                    
			                </div>
			                <div>
			                    <label for="city" class="label">City * </label>
			                    <input name="city" type="text" class="required" id="city" title="Please type your city." placeholder="Enter Your City">
			                    
			                </div>
			                <div>
			                    <label for="state" class="label">State * </label>
			                    <input name="state" type="text" class="required" id="state" title="Please type your state." placeholder="Enter Your State">
			                    
			                </div>
			                <div>
			                    <label for="email" class="label">E-mail Address * </label>
			                    <input name="email" type="text" id="email" placeholder="Enter your email address">
			                    
			                </div>
			                <div><span class="label">Gender * </span>
			                    <input name="gender" type="radio" id="male" value="male" class="required" title="Please choose a gender">
			                    <label for="male">Male</label>
			                    <input name="gender" type="radio" id="female" value="">
			                    <label for="female">Female</label>
			                    
			                </div>
			                <div>
			                    <label for="bird" class="label">Favorite Bird * </label>
			                    <select name="bird" id="bird" class="required" title="Please choose a bird.">
			                        <option value="">--Please select one--</option>
			                        <option value="falcon">Falcon</option>
			                        <option value="eagle">Eagle</option>
			                        <option value="blue jay">Blue Jay</option>
			                        <option value="robin">Robin</option>
			                    </select>
			                </div>
			                <div>
			                    <label for="comments" class="label">Comments</label>
			                    <textarea name="comments" cols="15" rows="5" id="comments"></textarea>
			                </div>
			                <div class="labelBlock">Would you like to receive coupouns and deals via email? * </div>
			                <div class="indent">
			                    <input type="checkbox" name="spam" id="yes" value="yes" class="required" title="Please select an option">
			                    <label for="yes">Yes</label>

			                </div>
			                <div>
			                    <input type="submit" name="submit" id="submit" value="Submit">
			                </div>
			            </form>
			            

			        </th>
			    </div>
			 
			 <footer class="centered-h">
				<?php include('data/footer.inc'); ?>
			</footer>
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
    		header("Location: signin.php?denied=yes");
	}	
?>	
