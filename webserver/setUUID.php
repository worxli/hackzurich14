<?php
    function setUUID()
    {
        include 'config.php';
        $UID = intval(htmlspecialchars($_POST["UID"]));
        $UUID = htmlspecialchars($_POST["UUID"]);
        
        $sql = "UPDATE user_data
                SET UUID = '$UUID'
                WHERE UID = 'UID';";
        
        if(!mysqli_query($con, $sql))
        {
            echo('Error: ' . mysqli_error($con));
        }
    }

    setUUID();
?>