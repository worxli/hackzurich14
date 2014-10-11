<?php
    function setUUID()
    {
        include 'config.php';
        $UID = intval(htmlspecialchars($_POST["UID"]));
        $UUID = htmlspecialchars($_POST["UUID"]);
        
        $sql = "SELECT *
                FROM user_data
                WHERE UID = '$UID'";
        
        $result = mysqli_query($con, $sql);
        
        json_encode($result);
        
    }

    setUUID();
?>