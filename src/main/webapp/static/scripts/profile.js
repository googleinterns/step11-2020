const deleteProfile = async () => {
let response = await fetch('/profile', {method:'DELETE'});
let json = await response.json()
  if(json.success) {
    location.href = '/landing';
  } else {
    alert("There was an error deleting your account.");
  }
};

function confirmDelete() {
  if (confirm("Are you sure you want to delete your account?")) {
    deleteProfile();
  }
}
