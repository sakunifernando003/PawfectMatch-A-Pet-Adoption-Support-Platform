const signupForm = document.getElementById('signupForm');

signupForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const userData = {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value,
        roles: [document.getElementById('role').value] // must be array of strings
    };

    try {
        const response = await fetch('http://localhost:8080/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        if (response.ok) {
            Swal.fire({
                icon: 'success',
                title: 'Registered!',
                text: 'You have successfully signed up',
                timer: 2000,
                showConfirmButton: false
            }).then(() => {
                window.location.href = 'LogIn_Page.html';
            });
        } else {
            const errorText = await response.text();
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: errorText
            });
        }
    } catch (err) {
        console.error('Error:', err);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Cannot reach server'
        });
    }
});
