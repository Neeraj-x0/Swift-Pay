<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
</head>
<body>
    <h2>Login</h2>
    <form action="user" method="post">
        <input type="hidden" name="action" value="login">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required><br><br>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br><br>

        <button type="submit">Login</button>
    </form>
    <p><a href="user?action=register">Don't have an account? Register here</a></p>

    <c:if test="${not empty error}">
        <p style="color:red">${error}</p>
    </c:if>
</body>
</html>
