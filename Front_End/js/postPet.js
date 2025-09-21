const petForm = document.getElementById('postPetForm');
const petImageInput = document.getElementById('petImage');
const imagePreview = document.getElementById('imagePreview');
const uploadedImage = document.getElementById('uploadedImage');

// --------- Live preview before submission ---------
petImageInput.addEventListener('change', (e) => {
    const file = e.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(event) {
            imagePreview.src = event.target.result;
            imagePreview.style.display = 'block';
        };
        reader.readAsDataURL(file);
    } else {
        imagePreview.src = '';
        imagePreview.style.display = 'none';
    }
});

// --------- Form submission ---------
petForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const formData = new FormData(petForm);

    try {
        const response = await fetch('http://localhost:8080/pets/post', {
            method: 'POST',
            body: formData,
        });

        if (!response.ok) throw new Error('Failed to post pet');

        const savedPet = await response.json();

        // Show success alert
        Swal.fire({
            icon: 'success',
            title: 'Pet Posted!',
            text: `Your pet "${savedPet.petName}" has been successfully posted.`,
            confirmButtonColor: '#facc15'
        });

        // Display uploaded image from backend
        uploadedImage.src = `http://localhost:8080${savedPet.imageUrl}`;
        uploadedImage.style.display = 'block';

        // Reset form & preview
        petForm.reset();
        imagePreview.src = '';
        imagePreview.style.display = 'none';

    } catch (error) {
        console.error(error);
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Failed to post pet.',
            confirmButtonColor: '#facc15'
        });
    }
});
