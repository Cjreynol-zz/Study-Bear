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
			$sql = "INSERT INTO USER VALUES('$uname', '$fname', '$lname', '$pw_hash', '$email', '', '', '');";
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
		{
			$result = $stm->fetch(PDO::FETCH_ASSOC);
			#Classes brings back multiple records from the database, so to avoid weird json_encoding, I just 
			#wrote a second query and appended the key/value pair to the end of the first array by class number.
			$class_sql = 
			"SELECT CONCAT(C.classId,'-', C.className,', ', D.professorLname,', ',D.professorFname)
			FROM USER_ENROLLMENT A 
				inner join TEACHING B ON A.teachingId = B.teachingId
				inner join CLASS C ON B.classId = C.classId
				inner join PROFESSOR D ON B.professorId = D.professorId
			WHERE A.userName = '$uname';";
			
			$stm2 = $this->conn->prepare($class_sql);
				if($stm2->execute())
				{
					$class_result = $stm2->fetch();
					$i = 1;
					
					$class_array;
					if($class_result == false)
						$class_array = array("No Classes");
					else
					{			
						while($class_result[0] != null)
						{
							$class_array[] = $class_result[0];
							$i++;
							$class_result = $stm2->fetch();
						}
						$result["CLASSES"] = $class_array;
					}
				}		
			return json_encode($result);
		}
		else	
			return "error";
	}
	
	#Make sure client populates whats already saved in database first and then call this function
	function editProfile($fname, $lname, $biography, $university, $uname){
		$sql = "UPDATE USER 
		SET firstName = '$fname', lastName = '$lname', biography = '$biography', universityname = '$university'
		where userName = '$uname';";
		
		$stm = $this->conn->prepare($sql);
		if($stm->execute())
			echo "success";
	}
}
?>


