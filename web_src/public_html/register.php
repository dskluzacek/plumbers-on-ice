<?php
			include("data/db.inc");
				function test_input($data){
					$data = trim($data);
					$data = stripslashes($data);
					$data = htmlspecialchars($data);
					return $data;
				}
				$name = $email = $password = $gender = $date = $bird = $comments = $userName = "";
				$nameError = $emailError = $passwordError = $genderError = $dateError = $birdError = $userNameError= "";
				
				if($_SERVER["REQUEST_METHOD"] == "POST"){
				
					if($_SERVER["REQUEST_METHOD"] =="POST"){
						if(empty($_POST["name"])){
							$nameError = "Please type your name.";
							} else {
								$name = test_input($_POST["name"]);
									if(!preg_match("/^[a-zA-Z ]*$/",$name)){
										$nameError = "Enter only letters and spaces";
									}	
							}	

						if(empty($_POST["userName"])){
							$userNameError = "";
							} else {
								$userName = test_input($_POST["userName"]);
									if(!preg_match("/^[a-zA-Z0-9]*$/",$name)){
										$userNameError = "Enter only letters and numbers";
									}	
							}
							
					if(empty($_POST["email"])){
						$emailError = "Please supply an e-mail address.";
							} else {
								$email = test_input($_POST["email"]);
							}	
							
					if(empty($_POST["password"])){
						$passwordError = "Please supply a password";
							} else {
								$password = test_input($_POST["password"]);
									if(!filter_var($email, FILTER_VALIDATE_EMAIL)){
										$emailError = "This is not a valid email";
									}	
							}	

					if(empty($_POST["gender"])){
						$genderError = "";
							} else {
								$gender = test_input($_POST["gender"]);
							}	

					
					if(empty($_POST["bird"])){
						$birdError = "Please choose your favorite bird.";
							} else {
								$name = test_input($_POST["bird"]);
							}	
				}
			}
		?>

    <html>

    <head>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
        <link rel="stylesheet" href="css/skeleton.css">


        <script src="js/scripts.js"></script>
        <script src="js/jquery.validate.min.js"></script>

        <title>
            Plumbers on ice
        </title>
        <script>
            $(document).ready(function () {
                $('#signup').validate({

                    rules: {
                        email: {
                            required: true,
                            email: true
                        },
                        password: {
                            required: true,
                            rangelength: [8, 16]
                        },
                        confirm_password: {
                            equalTo: '#password'
                        },
                        spam: "required"
                    }, //end rules
                    messages: {
                        email: {
                            required: "Please supply an e-mail address.",
                            email: "This is not a valid email address."
                        },
                        password: {
                            required: 'Please type a password',
                            rangelength: 'Password must be between 8 and 16 characters long.'
                        },
                        confirm_password: {
                            equalTo: 'The two passwords do not match.'
                        }
                    },
                    errorPlacement: function (error, element) {
                        if (element.is(":radio") || element.is(":checkbox")) {
                            error.appendTo(element.parent());
                        } else {
                            error.insertAfter(element);
                        }
                    }

                }); // end validate 
            }); // end ready
        </script>
    </head>

    <body>

        <header>
            <?php include('data/header.inc'); ?>
        </header>

        <div id="navi" class="centered-h">
        </div>

        <div id="content-pane" class="centered-h">



            <th>
                <h1>Signup</h1>

                <form action="complete.php" method="POST" name="signup" id="signup">
                    <div>
                        <label for="username" class="label">User Name * </label>
                        <input name="username" type="text" class="required" id="username" title="Please type your username." placeholder="Enter Your Username">
                        <?php echo $userNameError;?>
                    </div>
                    <div>
                        <label for="password" class="label">Password * </label>
                        <input name="password" type="password" id="password" placeholder="Enter Your Password.">
                        <?php echo $passwordError;?>
                    </div>
                    <div>
                        <label for="confirm_password" class="label">Confirm Password * </label>
                        <input name="confirm_password" type="password" id="confirm_password" placeholder="Confirm Your Password">
                    </div>
                    <div>
                        <label for="firstname" class="label">First Name * </label>
                        <input name="firstname" type="text" class="required" id="firstname" title="Please type your first name." placeholder="Enter Your Name">
                        <?php echo $nameError;?>
                    </div>
                    <div>
                        <label for="lastname" class="label">Last Name * </label>
                        <input name="lastname" type="text" class="required" id="lastname" title="Please type your last name." placeholder="Enter Your Name">
                        <?php echo $nameError;?>
                    </div>
                    <div>
                        <label for="address" class="label">Street Address * </label>
                        <input name="address" type="text" class="required" id="address" title="Please type your address." placeholder="Enter Your Address">
                        <?php echo $nameError;?>
                    </div>
                    <div>
                        <label for="city" class="label">City * </label>
                        <input name="city" type="text" class="required" id="city" title="Please type your city." placeholder="Enter Your City">
                        <?php echo $nameError;?>
                    </div>
                    <div>
                        <label for="state" class="label">State * </label>
                        <input name="state" type="text" class="required" id="state" title="Please type your state." placeholder="Enter Your State">
                        <?php echo $nameError;?>
                    </div>
                    <div>
                        <label for="email" class="label">E-mail Address * </label>
                        <input name="email" type="text" id="email" placeholder="Enter your email address">
                        <?php echo $emailError;?>
                    </div>
                    <div>
                        <input type="submit" name="submit" id="submit" value="Submit">



                    </div>
                </form>
                <form action="index.php" name="cancel" id="cancel">
                    <input type="submit" name="cancel" id="cancel" value="Cancel">
                </form>

            </th>
        </div>


        </div>


        </div>

    </body>

    </html>