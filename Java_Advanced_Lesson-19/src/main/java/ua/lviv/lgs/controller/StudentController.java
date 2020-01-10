package ua.lviv.lgs.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ua.lviv.lgs.domain.Student;
import ua.lviv.lgs.dto.StudentDto;
import ua.lviv.lgs.service.StudentService;

@RestController
public class StudentController {

	@Autowired
	StudentService studentService;

	@PostMapping("/uploadStudent")
	public StudentDto uploadStudent(@RequestParam("firstName") String firstName,
			@RequestParam("secondName") String secondName, @RequestParam("age") String age,
			@RequestParam("file") MultipartFile file) throws NumberFormatException, IOException {
		Student student = studentService.storeStudent(firstName, secondName, Integer.parseInt(age), file);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadStudent/")
				.path(student.getId()).toUriString();
		return new StudentDto(student.getFirstName(), student.getSecondName(), student.getAge(), student.getPhotoName(),
				fileDownloadUri, file.getContentType(), file.getSize());
	}

	@GetMapping("/downloadStudent/{fileId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws FileNotFoundException {
		Student student = studentService.getStudent(fileId);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(student.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + student.getPhotoName() + "\"")
				.body(new ByteArrayResource(student.getData()));
	}
}