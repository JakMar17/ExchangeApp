package si.fri.jakmar.backend.exchangeapp.api.courses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.backend.exchangeapp.database.entities.courses.CourseEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.CourseRepository;

@RestController
@RequestMapping("courses/")
public class CoursesApi {

    @Autowired
    CourseRepository courseRepository;

    @PostMapping("save")
    public ResponseEntity<CourseEntity> insertOrUpdateCourse(@RequestBody CourseEntity course) {
        CourseEntity updated = courseRepository.save(course);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("all")
    public ResponseEntity getCourses() {
        var data = courseRepository.findAll();
        return ResponseEntity.ok(data);
    }
}
