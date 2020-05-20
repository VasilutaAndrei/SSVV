package Lab2;

import Lab2.domain.Nota;
import Lab2.domain.Pair;
import Lab2.domain.Student;
import Lab2.domain.Tema;
import Lab2.repository.NotaXMLRepository;
import Lab2.repository.StudentXMLRepository;
import Lab2.repository.TemaXMLRepository;
import Lab2.service.Service;
import Lab2.validation.NotaValidator;
import Lab2.validation.StudentValidator;
import Lab2.validation.TemaValidator;
import Lab2.validation.Validator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class IncrementalIntegration {
    /**
     * Rigorous Test :-)
     */

    Service service;
    StudentXMLRepository fileRepository1;
    TemaXMLRepository fileRepository2;
    NotaXMLRepository fileRepository3;

    @Before
    public void initData() {
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();
        Validator<Student> studentValidator = new StudentValidator();

        fileRepository1 = new StudentXMLRepository(studentValidator, "studenti.xml");
        fileRepository2 = new TemaXMLRepository(temaValidator, "teme.xml");
        fileRepository3 = new NotaXMLRepository(notaValidator, "note.xml");
        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }


    @Test
    public void testAddStudentToRepository() {
        service.deleteStudent("13");
        assertNull(fileRepository1.findOne("13"));
        assertTrue(service.saveStudent("13", "Andrei", 937) == 1);
        assertEquals(fileRepository1.findOne("13").getNume(), "Andrei");

    }

    @Test
    public void testAddAssignmentToRepository() {
        service.deleteStudent("13");
        assertNull(fileRepository1.findOne("13"));
        service.deleteTema("11");
        assertNull(fileRepository1.findOne("11"));

        assertTrue(service.saveStudent("13", "Andrei", 937) == 1);
        assertEquals(fileRepository1.findOne("13").getNume(), "Andrei");
        assertTrue(service.saveTema("11", "Tema1", 2, 1) == 1);
        assertEquals(fileRepository2.findOne("11").getDescriere(), "Tema1");

    }


    @Test
    public void testAddGrade() {
        service.deleteStudent("13");
        assertNull(fileRepository1.findOne("13"));
        service.deleteTema("11");
        assertNull(fileRepository1.findOne("11"));
        service.deleteNota("13", "11");
        assertNull(fileRepository3.findOne(new Pair<String, String>("13", "11")));

        assertTrue(service.saveStudent("13", "Andrei", 937) == 1);
        assertEquals(fileRepository1.findOne("13").getNume(), "Andrei");
        assertTrue(service.saveTema("11", "Tema1", 2, 1) == 1);
        assertEquals(fileRepository2.findOne("11").getDescriere(), "Tema1");
        assertTrue(service.saveNota("13", "11", 9.0, 2, "Excelent") == 1);
        assertTrue(fileRepository3.findOne(new Pair<String, String>("13", "11")).getNota() == 9.0);
    }

}