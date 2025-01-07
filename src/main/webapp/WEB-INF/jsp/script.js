// Function to handle form submission
function handleFormSubmit(event) {
    event.preventDefault(); // Prevent the default form submission

    // Get form values
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    // Validate form data
    if (username === "" || password === "") {
        alert("All fields are required!");
        return;
    }

    // Send data to the server using AJAX (example with fetch)
    fetch("/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert("Registration successful!");
            window.location.href = "/profile";
        } else {
            alert("Registration failed: " + data.error);
        }
    })
    .catch(error => {
        console.error("Error during registration:", error);
        alert("An error occurred, please try again later.");
    });
}

// Event listener for form submission
const form = document.getElementById("registrationForm");
if (form) {
    form.addEventListener("submit", handleFormSubmit);
}

// Function to handle profile update form
function handleProfileUpdate(event) {
    event.preventDefault();

    // Get form values
    const email = document.getElementById("email").value;
    const phone = document.getElementById("phone").value;

    // Validate the data
    if (email === "" || phone === "") {
        alert("Please fill in all fields!");
        return;
    }

    // Make an AJAX request to update profile data
    fetch("/updateProfile", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, phone }),
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert("Profile updated successfully!");
            window.location.reload();  // Refresh the profile page
        } else {
            alert("Profile update failed: " + data.error);
        }
    })
    .catch(error => {
        console.error("Error during profile update:", error);
        alert("An error occurred, please try again later.");
    });
}

// Event listener for profile update form
const profileForm = document.getElementById("profileForm");
if (profileForm) {
    profileForm.addEventListener("submit", handleProfileUpdate);
}

// Function to display user profile data dynamically (example)
function displayUserProfile(data) {
    document.getElementById("usernameDisplay").textContent = data.username;
    document.getElementById("emailDisplay").textContent = data.email;
    document.getElementById("phoneDisplay").textContent = data.phone;
}

// Fetch user profile data from server (example)
function fetchUserProfile() {
    fetch("/profileData")
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            displayUserProfile(data.user);
        } else {
            alert("Failed to load profile data.");
        }
    })
    .catch(error => {
        console.error("Error fetching user profile:", error);
        alert("An error occurred, please try again later.");
    });
}

// Call fetchUserProfile on page load
document.addEventListener("DOMContentLoaded", fetchUserProfile);
