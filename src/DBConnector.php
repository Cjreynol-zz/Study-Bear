<?php
#Need to implement try catch for database queries;

class DBConnector
{	
	private $conn;
	#public $test = new PHPMailer();
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
		$sql = "SELECT password FROM USER WHERE userName = '$uname';";
	    $stm = $this->conn->prepare($sql);
		if($stm->execute())
		{
			$result = $stm->fetch();
			if(password_verify($pword, $result[0]))
				return "success";
			else
				return "incorrect username/password.";
		}
		else
			return "database error";
	}

	function Register($uname, $fname, $lname, $email, $pword)
	{
		#Email funtion to be implemented
		#require "PHPMailerAutoLoader.php"

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
			$sql = "INSERT INTO USER VALUES('$uname', '$fname', '$lname', '$pw_hash', '$email', '', ' ', '');";
			$stm = $this->conn->prepare($sql);
			
			if($stm->execute())
				return "success";
			else 
				return "this error";
			#Email functionality to be implemented 
		}
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
					$classes_array = array("No Classes");
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
	
	#Make sure client populates whats already saved in database first and then call this function
	function editProfile($fname, $lname, $biography, $university, $uname, $classList){
		
		$sql = "UPDATE USER 
		SET firstName = '$fname', lastName = '$lname', biography = '$biography', universityname = '$university' 
		WHERE userName = '$uname';";
		
		$stm = $this->conn->prepare($sql);
		
		$deleteList = json_decode($classList);
		
		if($deleteList != null)
		{		
			for($i = 0; $i < Count($deleteList); $i++)
			{
				$array = explode(", ", $deleteList[$i]);
				$classid = $array[0];
				$pfname = $array[3];
				$plname = $array[2];
		
				$sql2 = 
				"DELETE FROM USER_ENROLLMENT
				WHERE userName = '$uname' AND 
				professorId = (SELECT professorId FROM PROFESSOR WHERE professorFname = '$pfname' AND professorLname = '$plname') AND
				classId = '$classid';";
			
				$stm2 = $this->conn->prepare($sql2);
				$stm2->execute();			
			}
		}
	
	if($stm->execute())
			return "success" . $sql2;
		else
			return "error";
	}
	
	function editClasses($username){
		$sql_university_list = "SELECT * FROM UNIVERSITY;";
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
		WHERE B.universityName = '$university';";
		$result;
		$stm2 = $this->conn->prepare($classes_sql);
			if($stm2->execute())
			{
				$class = $stm2->fetch();

				$classes_array;
				if($class == false)
					$classes_array = array("No Classes");
				else
				{			
					while($class[0] != null)
					{
						$classes_array[] = $class;
						$class = $stm2->fetch();
					}
					$result["classList"] = $classes_array;	
					return $result;					
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

	function getConvo($buddy){
		$sql  = "SELECT *, DATE_FORMAT(dateTime, '%m/%d/%y %H:%i') AS niceDate from messages where sendingUser = '$buddy' or receivingUser = '$buddy' order by dateTime ASC;";

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
		$sql = "INSERT INTO messages (sendingUser, receivingUser, body, subject, dateTime) VALUES ('$uName', '$mTo', '$mBody', 'hi', now());";

		$stm = $this->conn->prepare($sql);
		if($stm->execute())
			echo "success";
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

	function checkTo($mTo){
		$sql = "SELECT EXISTS(SELECT * FROM user WHERE userName = '$mTo');";

		$stm = $this->conn->prepare($sql);
		$stm->execute();
		$result = $stm->fetch();
		
		if($result[0] == 0)
			return "error";
		else
			return "succes";
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
}
?>


