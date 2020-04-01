package Lab2;

import Lab2.domain.Nota;
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
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */

    Service service;
    StudentXMLRepository fileRepository1;
    TemaXMLRepository fileRepository2;
    NotaXMLRepository fileRepository3;

    @Before
    public void initData(){
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();
        Validator<Student> studentValidator = new StudentValidator();

        fileRepository1 = new StudentXMLRepository(studentValidator, "studenti.xml");
        fileRepository2 = new TemaXMLRepository(temaValidator, "teme.xml");
        fileRepository3 = new NotaXMLRepository(notaValidator, "note.xml");
        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testAddStudentDuplicateId() {

        assertTrue(service.saveStudent("10", "Andrei", 932) == 0);
        assertFalse(service.saveStudent("10", "Ale", 932) == 0);
    }

    @Test
    public void testAddStudentToRepository(){

        assertTrue(service.saveStudent("11", "Andrei", 932) == 0);
        assertEquals(fileRepository1.findOne("11").getNume(), "Andrei");

    }

    @Test
    public void testAddTemaToRepository(){

        assertTrue(service.saveTema("11", "Tema1", 2, 1) == 0);
        assertEquals(fileRepository2.findOne("11").getDescriere(), "Tema1");

    }

    @Test
    public void testAddTemaDuplicateId() {

        assertTrue(service.saveTema("11", "Tema1", 2, 1) == 0);
        assertFalse(service.saveTema("11", "Tema2", 4, 2) == 0);
    }


}
