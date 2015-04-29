<?php
#Need to implement try catch for database queries;

class DBConnector
{	
	private $conn;
	function __construct()
	{
		require_once 'DBConstants.php';
		#echo "Constructing DBConnector </br>";
		#require_once 'DBConfiguration.php';		
		try
		{
			$this->conn = new PDO("mysql:host=".HOST.";dbname=".DATABASE, USER, PASSWORD);
			// set the PDO error mode to exception
			$this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
			#echo "Connected successfully </br>"; 
		}
		catch(PDOException $e)
		{
			echo "Connection failed: </br>" . $e->getMessage();
		}
	}
	
	function Login($uname, $pword)
	{
		#Getting user password hash from database and comparing against rehashed user provided password
		#If matched, return true and log user in, if false, return error string
		$sql = "SELECT password, accountStatus FROM USER WHERE userName = '$uname';";
	    $stm = $this->conn->prepare($sql);
		if($stm->execute())
		{
			$result = $stm->fetch();
			if(password_verify($pword, $result[0]) && $result[1] == 'A')
				return "success";
			else if (password_verify($pword, $result[0]) && $result[1] == 'I')
				return "inactive";
			else
				return "incorrect username/password.";
		}
		else
			return "error";
	}

		#require "PHPMailerAutoLoader.php"
	function Register($uname, $fname, $lname, $email, $pword, $university)
	{
		#Email funtion to be implemented

		$password = $pword;
		#Creating password hash using BLOWFISH encryption with randomized SALT to add complexity
		$pw_hash = password_hash($password, PASSWORD_BCRYPT);
		
		#Checking for existing user name and returning true if exists, false otherwise
		$sql_checkuser = "SELECT userName FROM USER WHERE userName = '$uname';";
		$stm1 = $this->conn->prepare($sql_checkuser);
		$stm1->execute();
		$result = $stm1->fetch();

		if($result[0] == $uname)
			return "uname_error";
		else
		{
			#Inserting new users into database and returning success message if sql passes, error otherwise
			$sql = "INSERT INTO USER VALUES('$uname', '$fname', '$lname', '$pw_hash', '$email', '', '$university', 'I');";
			$stm = $this->conn->prepare($sql);
			$stm->execute();

			$activationId = uniqid('', true);
			$sql2 = "INSERT INTO USER_ACTIVATION VALUES('$uname', '$activationId');";
			$stm2 = $this->conn->prepare($sql2);
			if($stm2->execute()){
				$this->accountActivation($uname, $activationId, $email);
				return "success";
			}
			
			else
				echo "Message no Sent";
			}			
			#Email functionality to be implemented 
		}
	
	function getProfile($uname)
	{
		$sql = "SELECT firstName,lastName, biography, universityName FROM USER WHERE userName = '$uname';";
		$stm = $this->conn->prepare($sql);
		if($stm->execute())		
			$result = $stm->fetch(PDO::FETCH_ASSOC);	
		else
			return "error";
		
		#Classes brings back multiple records from the database, so to avoid weird json_encoding, I just 
		#wrote a second query and appended the key/value pair to the end of the first array by class number.
		$classes_sql = 
		"SELECT C.classId, C.className, D.professorLname,D.professorFname
		FROM USER_ENROLLMENT A 
			inner join TEACHING B ON A.professorId = B.professorId
			and A.classId = B.classId
			inner join CLASS C ON B.classId = C.classId
			inner join PROFESSOR D ON B.professorId = D.professorId
		WHERE A.userName = '$uname';";
			
		$stm2 = $this->conn->prepare($classes_sql);
			if($stm2->execute())
			{
				$class = $stm2->fetch();

				$classes_array;
				if($class == false)				
					$result["classList"] = null;				
				else
				{			
					while($class[0] != null)
					{
						$classes_array[] = $class;
						$class = $stm2->fetch();
					}
					$result["classList"] = $classes_array;
				}
			}
		
		$university_sql = "SELECT * FROM UNIVERSITY;";
		$stm3 = $this->conn->prepare($university_sql);

		if($stm3->execute())
		{
			
			$university = $stm3->fetch();
			$universityArray;
			while ($university[0] != null)
			{
				$universityArray[] = $university;
				$university = $stm3->fetch();
			}
				$result["universityList"] = $universityArray;			
		}
		return json_encode($result);
		
		return "error";
	}
	
	function getUserClasses($username){
		$classes_sql = 
		"SELECT C.classId, C.className, D.professorLname,D.professorFname
		FROM USER_ENROLLMENT A 
			inner join TEACHING B ON A.professorId = B.professorId
			and A.classId = B.classId
			inner join CLASS C ON B.classId = C.classId
			inner join PROFESSOR D ON B.professorId = D.professorId
		WHERE A.userName = '$username';";
			
		$stm2 = $this->conn->prepare($classes_sql);
		$result;
			if($stm2->execute())
			{
				$class = $stm2->fetch();

				$classes_array;
				if($class == false)
				{
					$classes_array["classList"] = null;
					return json_encode($classes_array);
				}
				else
				{			
					while($class[0] != null)
					{
						$classes_array[] = $class;
						$class = $stm2->fetch();
					}
					$result["classList"] = $classes_array;
					return json_encode($result);
				}
			}
	}
	#Make sure client populates whats already saved in database first and then call this function
	function editProfile($fname, $lname, $biography, $university, $uname){
		
		$sql = "UPDATE USER 
		SET firstName = '$fname', lastName = '$lname', biography = '$biography', universityname = '$university' 
		WHERE userName = '$uname';";	
		$stm = $this->conn->prepare($sql);
	
	if($stm->execute())
			return "success";
		else
			return "error";
	}
	
	function getUniversity($username){
		$sql_university_list = "SELECT universityName FROM user WHERE username ='$username'
								UNION						
								SELECT * FROM UNIVERSITY WHERE universityName NOT IN (SELECT universityName FROM user WHERE username ='$username');";		
		$stm = $this->conn->prepare($sql_university_list);
		$stm->execute();	
		$universityList["List"] = $stm->fetchAll();
		
		return json_encode($universityList);
	}
	
	function getUniversityList(){
	$sql_university_list = "SELECT universityName FROM university ORDER BY universityName ASC;";
							
	$stm = $this->conn->prepare($sql_university_list);
	$stm->execute();	
	$universityList["List"] = $stm->fetchAll();
		
		return json_encode($universityList);
	}
	
	function getClasses($username, $university){
		$classes_sql = 
		"SELECT B.classId, B.className,C.professorLname, C.professorFname
		FROM TEACHING A 
			inner join CLASS B on A.classId = B.classId
			inner join PROFESSOR C on A.professorId = C.professorId
		WHERE B.universityName = '$university' order by 1 asc;";
		$result;
		$stm2 = $this->conn->prepare($classes_sql);
			if($stm2->execute())
			{
				$class = $stm2->fetch();

				$classes_array;
				if($class == false){
					$classes_array["classList"] = array(array("No Classes"));
					return json_encode($classes_array);
				}
				else
				{			
					while($class[0] != null)
					{
						$classes_array[] = $class;
						$class = $stm2->fetch();
					}
					$result["classList"] = $classes_array;	
					return json_encode($result);					
				}
			}
			
	}
	
	#messages
	function getMessages($userName){
		$sql  = "SELECT *, DATE_FORMAT(dateTime, '%m/%d/%y %H:%i') AS niceDate from messages where sendingUser = '$userName' or receivingUser = '$userName' order by dateTime DESC;";

		$stm = $this->conn->prepare($sql);
		if($stm->execute())

		$message = $stm->fetch();
		$messageArray;
		while ($message[0] != null){
			$messageArray[] = $message;
			$message = $stm->fetch();
		}

		$result["messageList"] = $messageArray;
		return json_encode($result);
	}

	function getConvo($buddy, $username){
		$sql  = "SELECT *, DATE_FORMAT(dateTime, '%m/%d/%y %H:%i') AS niceDate from messages where (sendingUser = '$buddy' and receivingUser = '$username' ) or (receivingUser = '$buddy' and sendingUser = '$username') order by dateTime ASC;";

		$stm = $this->conn->prepare($sql);
		if($stm->execute())

		$message = $stm->fetch();
		$messageArray;
		while ($message[0] != null){
			$messageArray[] = $message;
			$message = $stm->fetch();
		}

		$result["messageList"] = $messageArray;
		return json_encode($result);
	}

	function newMessage($mTo, $mBody, $uName){
		$sql = "SELECT EXISTS(SELECT * FROM user WHERE username = '$mTo');";

		$stm = $this->conn->prepare($sql);
		$stm->execute();
		$result = $stm->fetch();
		
		if($result[0] == 0)
			return "error";
		else{
		$sql = "INSERT INTO messages (sendingUser, receivingUser, body, subject, dateTime) VALUES ('$uName', '$mTo', '$mBody', 'hi', now());";

		$stm = $this->conn->prepare($sql);
		if($stm->execute())
			echo "success";
		}
	}

	function getMatches($userName) {
		$sql = "SELECT firstName, lastName, userName, biography FROM USER WHERE USER.userName <> '$userName' LIMIT 5;";
		
		$stm = $this->conn->prepare($sql);
		if ($stm->execute()) {
		
			$user = $stm->fetch();
			$userArray;
			while ($user[0] != null){
				$userArray[] = $user;
				$user = $stm->fetch();
			}
		
			$result["userList"] = $userArray;
			return json_encode($result);
		}
	}
	
	function saveClasses($username, $removeList, $insertList){
		
		$deleteDecode = json_decode($removeList);
		$insertDecode = json_decode($insertList);
		
		$classId;
		$professorFname;
		$professorLname;
		
		#return $removeList;
		for($i = 0; $i < count($insertDecode); $i++){
			$insertRow = explode(", ", $insertDecode[$i]);
			$classId = $insertRow[0];
			$professorFname = $insertRow[3];
			$professorLname = $insertRow[2];
			
			$sql = "INSERT into USER_ENROLLMENT 
				select '$username', C.professorId, B.classId, 'A'
				from TEACHING A 
			inner join CLASS B on A.classId = B.classId
			inner join PROFESSOR C on A.professorId = C.professorId
		WHERE B.classId = '$classId' and C.professorFname = '$professorFname' and C.professorLname = '$professorLname';";
		
		$stm = $this->conn->prepare($sql);
		$stm->execute();
		}
		
		for($i = 0; $i < count($deleteDecode); $i++){
			$deleteRow = explode(", ", $deleteDecode[$i]);
			$classId = $deleteRow[0];
			$professorFname = $deleteRow[3];
			$professorLname = $deleteRow[2];
			
			$sql = "DELETE FROM USER_ENROLLMENT
			WHERE professorId IN (SELECT professorId from PROFESSOR where professorFname = '$professorFname' and professorLname = '$professorLname')
			and classId = '$classId' and username = '$username';";
			$stm = $this->conn->prepare($sql);
			$stm->execute();
		}
					
	}

	function checkEmail($email){
		$sql = "SELECT EXISTS(SELECT * FROM user WHERE email = '$email');";

		$stm = $this->conn->prepare($sql);
		$stm->execute();
		$result = $stm->fetch();
		
		if($result[0] == 0)
			return "error";
		else
			return "succes";
	}

	function accountActivation($username, $activationId, $email){	
		include 'EmailServer.php';
		return sendEmail($username, $activationId, $email);
	
	}
	
	function accountConfirm($actId, $username){
		$sql = "SELECT actId FROM USER_ACTIVATION WHERE userName = '$username';";
		$stm = $this->conn->prepare($sql);
		$stm->execute();
		$confirmId = $stm->fetch();
		
		if($confirmId[0] == $actId)
		{
			$sql2 = "UPDATE USER SET accountStatus = 'A' WHERE userName = '$username';";
			$stm2 = $this->conn->prepare($sql2);
			if($stm2->execute())
				echo "success";
			else
				echo "fail";
		}
	}

	function saveBio($bio, $username){
	$sql = "UPDATE User SET biography = '$bio' WHERE username = '$username';";
		
	$stm = $this->conn->prepare($sql);
		if($stm->execute())
			echo $bio;
	}
}
?>


