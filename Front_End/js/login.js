// login.js

document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("loginForm");

    loginForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        // Get input values
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        // Prepare request body
        const loginData = {
            username: username,
            password: password
        };

        try {
            const response = await fetch("http://localhost:8080/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(loginData)
            });

            if (!response.ok) {
                // If status is not 200-299, show error
                const errorText = await response.text();
                Swal.fire({
                    icon: "error",
                    title: "Login Failed",
                    text: errorText,
                    confirmButtonColor: "#fbbf24"
                });
                return;
            }

            const token = await response.text();

            // Save JWT token to localStorage (optional)
            localStorage.setItem("token", token);

            // Show success alert and redirect
            Swal.fire({
                icon: "success",
                title: "Login Successful",
                text: "Redirecting to your dashboard...",
                showConfirmButton: false,
                timer: 2000,
                didClose: () => {
                    window.location.href = "Dash_board.html"; // change to your page
                }
            });

        } catch (error) {
            console.error("Error:", error);
            Swal.fire({
                icon: "error",
                title: "Login Error",
                text: "Something went wrong. Please try again!",
                confirmButtonColor: "#fbbf24"
            });
        }
    });
});
