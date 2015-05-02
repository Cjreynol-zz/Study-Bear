<?php
#Client Application Request Handler Script
#Get Request will need to change to Post request in final app;
#echo "Script started </br>";
require_once "DBConnector.php";
$dbconn = new DBConnector();
//require_once "EmailServer.php";

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
		if(isset($_POST["fname"], $_POST["lname"], $_POST["uname"], $_POST["email"], $_POST["pword"], $_POST["pconfirm"], $_POST["university"]))
			echo $dbconn->Register($_POST["uname"], $_POST["fname"], $_POST["lname"], $_POST["email"], $_POST["pword"], $_POST["university"]);
				break;
				
	case 'getProfile':
		if(isset($_GET["username"]) )
			echo $dbconn->getProfile($_GET["username"]);
		else
			echo "error0";
				break;
			
	case 'editProfile':
		if($_POST["fname"] != null &&  $_POST["lname"] != null)
			echo $dbconn->editProfile($_POST["fname"], $_POST["lname"], $_POST["biography"], $_POST["university"], $_POST["uname"]);
		else
			echo "error1";
		break;
			
	case 'getUniversity':
		echo $dbconn->getUniversity($_GET["username"]);
		break;
		
	case 'getUniversityList':
		echo $dbconn->getUniversityList();
		break;
		
	case 'getMajor':
		echo $dbconn->getMajor($_GET["university"]);
		break;
	
	case 'getUserClasses':
		echo $dbconn->getUserClasses($_GET["username"]);
		break;
			
	case 'getClasses':
		echo $dbconn->getClasses($_GET["username"], $_GET["university"], $_GET["major"]);
		break;
			
	case 'getMessages':
		echo $dbconn->getMessages($_GET["username"]);
		break;
		
	case 'getConvo':
		echo $dbconn->getConvo($_GET["buddy"], $_GET["username"]);
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
	
	case 'saveClasses':
		if (isset($_POST["username"]))
			echo $dbconn->saveClasses($_POST["username"], $_POST["removeList"], $_POST["insertList"]);
		break;

	case 'checkEmail':
		echo $dbconn->checkEmail($_POST["email"]);
		break;	
		
	case 'accountConfirm':
		$dbconn->accountConfirm($_GET["actId"], $_GET["username"]);
		echo "Thanks for activiating your account. You can now use StudyBear.";
		break;

	case 'saveBio':
		echo $dbconn->saveBio($_POST["biography"], $_POST["username"]);
		break;
}

?>