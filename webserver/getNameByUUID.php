<?php
    function setUUID()
    {
        include 'config.php';
        $UID = intval(htmlspecialchars($_POST["UID"]));
        $UUID = htmlspecialchars($_POST["UUID"]);
        
        $sql = "SELECT name, first_name
                FROM user_data
                WHERE UUID LIKE '$UUID'";
        
        $result = mysqli_query($con, $sql);
        
        $data = array();
        while($row = $result->fetch_assoc())
        {
            echo $row['first_name'] . " " . $row['name'];
        }        
        
    }

    setUUID();
?>