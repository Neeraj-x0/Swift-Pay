<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Enter Payment Amount</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Enter Payment Amount</h1>
        </header>
        <main>
            <form action="/process-payment" method="post">
                <label for="amount">Amount</label>
                <input type="text" id="amount" name="amount" placeholder="Enter amount" required>

                <div class="error-message">
                    <p style="color: red;">${amountError}</p>
                </div>

                <button type="submit" class="btn">Pay Now</button>
            </form>
            <div class="btn-container">
                <a href="/" class="btn">Back to Home</a>
            </div>
        </main>
    </div>
</body>
</html>
