<?php
#Client Application Request Handler Script
#Get Request will need to change to Post request in final app;
#echo "Script started </br>";
require_once "DBConnector.php";
$dbconn = new DBConnector();

switch($_GET["rtype"])
{
	case 'login': 
		if(isset($_POST["username"], $_POST["password"]))
		{
			if(strpos($_POST["username"], ' ') === false && strlen($_POST["password"]) >= 8)
			{
					echo $dbconn->Login($_POST["username"], $_POST["password"]);
						break;		
			}
			else
				echo "error";
		}
		else
			echo "error";
		break;
				
	case 'register':
		if(isset($_POST["fname"], $_POST["lname"], $_POST["uname"], $_POST["email"], $_POST["pword"], $_POST["pconfirm"]))
		{
			/*if(strpos($_POST["fname"], ' ') === false && strpos($_POST["lname"], ' ') === false 
				&& strpos($_POST["uname"], ' ') === false && strpos($_POST["email"], ' ') === false 
					&& strpos($_POST["pword"], ' ') === false && strlen($_POST["pword"]) >= 8
						&& $_POST["pword"] === $_POST["pconfirm"])
						{*/
				echo $dbconn->Register($_POST["uname"], $_POST["fname"], $_POST["lname"], $_POST["email"], $_POST["pword"]);
					break;
			#}			
			#else
				#echo "error";
		}
		else
			echo "index error";
		break;
		
	case 'getProfile':
		if(isset($_GET["username"]) )
			echo $dbconn->getProfile($_GET["username"]);
		else
			echo "error";
		break;
			
	case 'editProfile':
		echo $dbconn->editProfile($_POST["fname"], $_POST["lname"], $_POST["biography"], $_POST["university"], $_POST["uname"]);
		break;

	case 'getMessages':
		echo $dbconn->getMessages($_GET["username"]);
		break;
		
	case 'getConvo':
		echo $dbconn->getMessages($_GET["buddy"]);
		break;
		
	case 'newMessage':
		echo $dbconn->newMessage($_POST["mTo"], $_POST["mBody"], $_POST["uName"]);
		break;
	
	case 'getMatches':
		if (isset($_GET["username"]))
			echo $dbconn->getMatches($_GET["username"]);
		else
			echo $dbconn->getMatches("");
		break;
	
	
}
#To be implemented
#function DeleteMessage(){}
#unction SendMessage(){}

?>