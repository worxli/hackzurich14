<?php
    function setProfile()
    {
        include 'config.php';
        $UID = intval(htmlspecialchars($_POST["UID"]));
        $name = htmlspecialchars($_POST["name"]);
        $first_name = htmlspecialchars($_POST["first_name"]);
        $dob = htmlspecialchars($_POST["dob"]);
        $address = htmlspecialchars($_POST["address"]);
        $postcode = htmlspecialchars($_POST["postcode"]);
        $city = htmlspecialchars($_POST["city"]);
        $land = htmlspecialchars($_POST["land"]);
        $email_address = htmlspecialchars($_POST["email_address"]);
        $phone_number = htmlspecialchars($_POST["phone_number"]);
        $facebook = htmlspecialchars($_POST["facebook"]);
        $twitter = htmlspecialchars($_POST["twitter"]);
        $linkedin = htmlspecialchars($_POST["linkedin"]);
        $xing = htmlspecialchars($_POST["xing"]);
        $UUID = htmlspecialchars($_POST["UUID"]);
        
        if($UID == "-1")
        {
            $sql = "INSERT INTO user_data (name, first_name, dob, address, postcode, city, land, email_address, phone_number, facebook, twitter, linkedin, xing, UUID)
                    VALUES ('$name', '$first_name', '$dob', '$address', '$postcode', '$city', '$land', '$email_address', '$phone_number', '$facebook', '$twitter', '$linkedin', '$xing', '$UUID');";
            if(!mysqli_query($con, $sql))
            {
                die('Error: ' . mysqli_error($con));
            }
            $id = mysqli_insert_id($con);
            echo "Data inputed with id = " . $id;
        }
        else
        {
            $sql = "UPDATE user_data
                    SET name = '$name',
                        first_name = '$first_name',
                        dob = '$dob',
                        address = '$address',
                        postcode = '$postcode',
                        city = '$city',
                        land = '$land',
                        email_address = '$email_address',
                        phone_number = '$phone_number',
                        facebook = '$facebook',
                        twitter = '$twitter',
                        linkedin = '$linkedin',
                        xing = '$xing',
                        UUID = '$UUID'
                    WHERE UID LIKE '$UID';";
            if(!mysqli_query($con, $sql))
            {
                echo('Error: ' . mysqli_error($con));
            }
            $id = $UID
            echo "Data updated";
            
        }
        
        return $id;
    }

    setProfile();
?>