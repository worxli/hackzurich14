<?php
    function setProfile()
    {
        include 'config.php';
        $UID = htmlspecialchars($_POST["UID"]);
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

        
        if($UID == -1)
        {
            mysqli_query($con, "INSERT INTO user_data (name, first_name, dob, address, postcode, city, land, email_address, phone_number, facebook, twitter, linkedin, xing, UUID)
                                VALUES ('$name', '$first_name', '$dob', '$address', '$postcode', '$city', '$land', '$email_address', '$phone_number', '$facebook', '$twitter', '$linkedin', '$xing', '$UUID')");
            echo "Data inputed";
        }
        else
        {
            mysqli_query($con, "UPDATE user_data
                                SET name = '$name'
                                    first_name = '$first_name'
                                    dob = '$dob'
                                    address = '$address'
                                    postcode = '$postcode'
                                    city = '$city'
                                    land = '$land'
                                    email_address = '$email_address'
                                    phone_number = '$phone_number'
                                    facebook = '$facebook'
                                    twitter = '$twitter'
                                    linkedin = '$linkedin'
                                    xing = '$xing'
                                    UUID = '$UUID'
                                WHERE UID = '$UID'");
            echo "Data updated";
        }
    }

    setProfile();
?>