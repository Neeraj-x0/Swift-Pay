<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile</title>
</head>
<body>
    <h2>User Profile</h2>
    <p>Username: ${user.username}</p>
    <p>Email: ${user.email}</p>
    <form action="user" method="post">
        <input type="hidden" name="action" value="logout">
        <button type="submit">Logout</button>
    </form>
</body>
</html>
