<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Domain Default page</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-cache" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
<link rel="icon" href="favicon.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<body>
    <?php
        include 'config.php';
    ?>
    
    <?php
// define variables and set to empty values
$nameErr = $first_nameErr = $emailErr = $bleErr = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
   if (empty($_POST["name"])) {
     $nameErr = "Name is required";
   } else {
     $name = test_input($_POST["name"]);
     // check if name only contains letters and whitespace
     if (!preg_match("/^[a-zA-Z ]*$/",$name)) {
       $nameErr = "Only letters and white space allowed"; 
     }
   }
    
    if (empty($_POST["first_name"])) {
     $nameErr = "First Name is required";
   } else {
     $name = test_input($_POST["first_name"]);
     // check if name only contains letters and whitespace
     if (!preg_match("/^[a-zA-Z ]*$/",$first_name)) {
       $first_nameErr = "Only letters and white space allowed"; 
     }
   }
    
    if (!empty($_POST["dob"])) {
        $dob = test_input($_POST["dob"]);
    }
    
    if (!empty($_POST["address"])) {
       $address = test_input($_POST["address"]);
    }
    
    if (!empty($_POST["city"])) {
        $city = test_input($_POST["city"]);
    }
    
    if (!empty($_POST["land"])) {
        $land = test_input($_POST["land"]);
    }
   
    if (!empty($_POST["email"])) {
        $email = test_input($_POST["email"]);
        // check if e-mail address is well-formed
        if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            $emailErr = "Invalid email format"; 
        }    
    }
    
    if (!empty($_POST["phone_number"])) {
        $phone_number = test_input($_POST["phone_number"]);
    }
    
    if (!empty($_POST["facebook"])) {
        $facebook = text_input($_POST["facebook"]);
    }
    
    if (!empty($_POST["twitter"])) {
        $twitter = text_input($_POST["twitter"]);
    }
    
    if (!empty($_POST["linkedin"])) {
        $linkedin = text_input($_POST["linkedin"]);
    }
    
    if (!empty($_POST["xing"])) {
        $xing = text_input($_POST["xing"]);
    }
    
    if (empty($_POST["uuid"])) {
        $bleErr = "BLE Address missing";
    } else {
        $uuid = text_input($_POST["uuid"]);
    }
    
    $UID = "-1";
}

function test_input($data) {
   $data = trim($data);
   $data = stripslashes($data);
   $data = htmlspecialchars($data);
   return $data;
}
?>

<h2>Input your data to Server</h2>
<p><span class="error">* required field.</span></p>
<form method="post" action="setProfile.php"> 
    Name: <input type="text" name="name" value="<?php echo $name;?>">
    <span class="error">* <?php echo $nameErr;?></span>
    <br><br>
    First Name: <input type="text" name="first_name" value="<?php echo $first_name;?>">
    <span class="error">* <?php echo $first_nameErr;?></span>
    <br><br>
    Date of Birth (yyyy-mm-dd): <input type="text" name="dob" value="<?php echo $dob;?>">
    <br><br>
    Address: <input type="text" name="address" value="<?php echo $address;?>">
    <br><br>
    Postcode: <input type="text" name="postcode" value="<?php echo $postcode;?>">
    <br><br>
    City: <input type="text" name="city" value="<?php echo $city;?>">
    <br><br>
    Land: <input type="text" name="land" value="<?php echo $land;?>">
    <br><br>
    E-mail: <input type="text" name="email" value="<?php echo $email;?>">
    <br><br>
    Phone Number: <input type="text" name="phone_number" value="<?php echo $phone_number;?>">
    <br><br>
    Facebook: <input type="text" name="facebook" value="<?php echo $facebook;?>">
    <br><br>
    Twitter: <input type="text" name="twitter" value="<?php echo $twitter;?>">
    <br><br>
    LinkedIn: <input type="text" name="linkedin" value="<?php echo $linkedin;?>">
    <br><br>
    Xing: <input type="text" name="xing" value="<?php echo $xing;?>">
    <br><br>
    BLE Address: <input type="text" name="uuid" value="<?php echo $uuid;?>">
    <span class="error">* <?php echo $bleErr;?></span>
    <br><br>
    <input type="hidden" name="UID" value="-1">
    
    <input type="submit" name="submit" value="Submit"> 
</form>
 
</body>
</html>
