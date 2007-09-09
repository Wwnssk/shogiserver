<?php
	require 'config.php';

	if($_POST['submit']) {
		$global_dbh = mysql_connect($dbHost, $dbUser, $dbPass)
			or die("Could not connect to MySQL database.");
		mysql_select_db($dbName)
			or die("Could not connect to database: $dbName");

		$username = $_POST['username'];
		$password = $_POST['password'];
		$email = $_POST['email'];	

		$query_string = "INSERT INTO $dbShogiUserTable (user_name, user_password, user_email) VALUES('$username', '$password', '$email');";
		echo $query_string;
		mysql_query($query_string, $global_dbh)
			or die("Could not execute query");
		mysql_close($global_dbh);
		die("Data submitted.");
	}
?>

<HTML>
	<HEAD>
		<TITLE>ShogiServer - Register a New Account</TITLE>
	</HEAD>
	<BODY>
		<H1>Register</H1>
		<FORM ACTION="register.php" METHOD="POST">
			<TABLE>
				<TBODY>
					<TR>
						<TD>Username:</TD>
						<TD><INPUT type="text" name="username"></TD>
					</TR>
					<TR>
						<TD>Password:</TD>
						<TD><INPUT type="password" name="password"></TD>
					</TR>
					<TR>
						<TD>Email:</TD>
						<TD><INPUT type="text" name="email"></TD>
					</TR>
				</TBODY>
			</TABLE>
			<INPUT type="submit" name="submit">
		</FORM>
	</BODY>
</HTML>