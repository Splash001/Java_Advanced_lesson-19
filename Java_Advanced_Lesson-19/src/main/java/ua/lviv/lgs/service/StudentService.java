package ua.lviv.lgs.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import ua.lviv.lgs.domain.Student;
import ua.lviv.lgs.repository.StudentRepository;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;

	public Student storeStudent(String firstName, String secondName, Integer age, MultipartFile file) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Student student = null;
		if (!fileName.contains("..")) {
			student = new Student(firstName, secondName, age, fileName, file.getContentType(), file.getBytes());
		}
		return studentRepository.save(student);
	}

	public Student getStudent(String fileId) throws FileNotFoundException {
		return studentRepository.findById(fileId)
				.orElseThrow(() -> new FileNotFoundException("File not found with id = " + fileId));
	}

}