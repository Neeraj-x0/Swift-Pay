<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - SwiftPay</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Oops! Something went wrong.</h1>
        </header>
        <main>
            <div class="error-message">
                <h2>Error Code: ${errorCode}</h2>
                <p>${errorMessage}</p>
            </div>
            <div class="btn-container">
                <a href="/" class="btn">Back to Home</a>
            </div>
        </main>
    </div>
</body>
</html>
