<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Forms fotr testing</title>
</head>

<body id="main_body" >

		<h5>POST   /sessions/{userLogin} -- Get session number (authentication)</h5>
		<form method="post" action="http://opennote-opennote.rhcloud.com/api/sessions/riaval"><!-- ______________________ login -->
			<label>password</label><br>
			<input name="password" type="text" value="password"/>
			<input class="button_text" type="submit" name="submit" value="Submit" />
		</form>

		<h5>POST   /users/{userLogin} -- Create a new user</h5>
		<form method="post" action="http://localhost:8080/api/users/fullName3"><!-- ____________________________ login -->
			<label>full name</label><br>
			<input name="full_name" type="text" value="fullName"/><br>
			<label>password</label><br>
			<input name="password" type="text" value="password"/> 
			<input type="submit" name="submit" value="Submit" />
		</form>

		<h5>POST   /groups/{groupSlug} -- Create a new group </h5>
		<form method="post" action="http://localhost:8080/api/groups/newSimpleGroup"><!-- ____________________________ slug -->
			<label>name</label><br>
			<input name="name" type="text" value="Group name"/><br>
			<label>session_hash</label><br>
			<input name="session_hash" type="text" value="c7ae8f2a56d2627f1be46d3c69c232ad"/> 
			<input type="submit" name="submit" value="Submit" />
		</form>

		<h5>DELETE /groups/{groupSlug} -- Delete group</h5>
		<form method="post" action="http://localhost:8080/api/groups/fortified">
			<label>session_hash</label><br>
			<input name="session_hash" type="text" value="c7ae8f2a56d2627f1be46d3c69c232ad"/> 
			<input type="submit" name="submit" value="Submit" />
		</form>

		<h5>GET    /groups/ -- Get list of groups </h5>
		<form method="get" action="http://opennote-opennote.rhcloud.com/api/groups/">
			<label>session_hash</label><br>
			<input name="session_hash" type="text" value="c7ae8f2a56d2627f1be46d3c69c232ad"/> 
			<input type="submit" name="submit" value="Submit" />
		</form>

		<h5>POST   /groups/{groupSlug}/snote/ -- Create SimpleNote</h5>
		<form method="post" action="http://localhost:8080/api/groups/GTVscience/snote/">
			<label>title</label><br>
			<input name="title" type="text" value="title"/><br>
			<label>body</label><br>
			<input name="body" type="text" value="body"/><br>
			<label>session_hash</label><br>
			<input name="session_hash" type="text" value="c7ae8f2a56d2627f1be46d3c69c232ad"/> 
			<input type="submit" name="submit" value="Submit" />
		</form>

		<h5>GET    /groups/{groupSlug}/snote/ -- Get SimpleNotes</h5>
		<form method="get" action="http://localhost:8080/api/groups/GTVscience/snote/">
			<label>session_hash</label><br>
			<input name="session_hash" type="text" value="c7ae8f2a56d2627f1be46d3c69c232ad"/> 
			<input type="submit" name="submit" value="Submit" />
		</form>

		<h5>GET    /snote/ -- Get all SimpleNotes</h5>
		<form method="get" action="http://localhost:8080/api/snote/">
			<label>session_hash</label><br>
			<input name="session_hash" type="text" value="c7ae8f2a56d2627f1be46d3c69c232ad"/> 
			<input type="submit" name="submit" value="Submit" />
		</form>

		<h5>GET    /users/ -- Find users data</h5>
		<form method="get" action="http://localhost:8080/api/users/">
			<label>session_hash</label><br>
			<input name="session_hash" type="text" value="c7ae8f2a56d2627f1be46d3c69c232ad"/><br>
			<label>search</label><br>
			<input name="search" type="text" value=""/>
			<input type="submit" name="submit" value="Submit" />
		</form>

		<h5>POST   /invitations/users/{userLogin}/groups/{groupSlug} -- Create invitation</h5>
		<form method="post" action="http://localhost:8080/api/invitations/users/fullName3/groups/mygroup">
			<label>session_hash</label><br>
			<input name="session_hash" type="text" value="c7ae8f2a56d2627f1be46d3c69c232ad"/> 
			<input type="submit" name="submit" value="Submit" />
		</form>

		<h5>GET    /invitations/ -- get list of invitations</h5>
		<form method="get" action="http://localhost:8080/api/invitations">
			<label>session_hash</label><br>
			<input name="session_hash" type="text" value="c7ae8f2a56d2627f1be46d3c69c232ad"/> 
			<input type="submit" name="submit" value="Submit" />
		</form>

		<h5>DELETE    /invitations/{invitationID} -- delete invitation</h5>
		<form method="get" action="http://localhost:8080/api/invitations/18">
			<label>session_hash</label><br>
			<input name="session_hash" type="text" value="c7ae8f2a56d2627f1be46d3c69c232ad"/> 
<!-- 			<label>invitation_id</label><br>
			<input name="invitation_id" type="text" value="5"/>  -->
			<input type="submit" name="submit" value="Submit" />
		</form>

		<h5>POST   /groups/{groupSlug}/users/ -- Add user into group</h5>
		<form method="post" action="http://localhost:8080/api/groups/mygroup/users">
			<label>session_hash</label><br>
			<input name="session_hash" type="text" value="c7ae8f2a56d2627f1be46d3c69c232ad"/> 
			<input type="submit" name="submit" value="Submit" />
		</form>

		<h5>DELETE   /snote/ -- Delete SimpleNotes</h5>
		<form method="get" action="http://localhost:8080/api/snote">
			<label>session_hash</label><br>
			<input name="session_hash" type="text" value="c7ae8f2a56d2627f1be46d3c69c232ad"/><br>
			<input name="id" type="text" value="2"/><br>
			<input name="id" type="text" value="4"/><br>
			<input type="submit" name="submit" value="Submit" />
		</form>

</body>
</html>