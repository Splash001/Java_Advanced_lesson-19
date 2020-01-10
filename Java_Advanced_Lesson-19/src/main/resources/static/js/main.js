var registrationForm = document.querySelector('#registrationForm');
var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
var singleFileUploadError = document.querySelector('#singleFileUploadError');
var singleFileUploadSuccess = document
		.querySelector('#singleFileUploadSuccess');

function uploadSingleFile(file) {
	var firstName = document.getElementById("firstName").value;
	var secondName = document.getElementById("secondName").value;
	var age = document.getElementById("age").value;

	var formData = new FormData();
	formData.append("file", file);
	formData.append("firstName", firstName);
	formData.append("secondName", secondName);
	formData.append("age", age);

	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/uploadStudent");

	xhr.onload = function() {
		console.log(xhr.responseText);
		var response = JSON.parse(xhr.responseText);
		if (xhr.status == 200) {
			singleFileUploadError.style.display = "none";
			singleFileUploadSuccess.innerHTML = "<p>Registration complete Successfully. <br>"
					+ "First Name : "
					+ response.firstName
					+ "<br>Second Name : "
					+ response.secondName
					+ " <br> Age: "
					+ response.age
					+ "<br> Photo: <br> <img src="
					+ response.fileDownloadUri
					+ " alt='Photo' height='300' width='300'></p>";
			singleFileUploadSuccess.style.display = "block";
		} else {
			singleFileUploadSuccess.style.display = "none";
			singleFileUploadError.innerHTML = (response && response.message)
					|| "Some Error Occurred";
		}
	}

	xhr.send(formData);
}

registrationForm.addEventListener('submit', function(event) {
	var files = singleFileUploadInput.files;
	if (files.length === 0) {
		singleFileUploadError.innerHTML = "Please select a file";
		singleFileUploadError.style.display = "block";
	}
	uploadSingleFile(files[0]);
	event.preventDefault();
}, true);