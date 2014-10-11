<?php
// Create connection
$con=mysqli_connect("hackzurich14.worx.li","web17_hackaton","qhA7f6$0","web171a_hackaton");

// Check connection
if (mysqli_connect_errno()) {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
?>