package Lab2;

import Lab2.domain.Nota;
import Lab2.domain.Student;
import Lab2.domain.Tema;
import Lab2.repository.NotaXMLRepository;
import Lab2.repository.StudentXMLRepository;
import Lab2.repository.TemaXMLRepository;
import Lab2.service.Service;
import Lab2.validation.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
        service.deleteStudent("9");
        assertNull(fileRepository1.findOne("9"));
        assertTrue(service.saveStudent("9", "Andrei", 937) == 1);
        assertTrue(service.saveStudent("9", "Ale", 937) == 0);
    }

    @Test
    public void testAddStudentToRepository(){
        service.deleteStudent("13");
        assertNull(fileRepository1.findOne("13"));
        assertTrue(service.saveStudent("13", "Andrei", 937) == 1);
        assertEquals(fileRepository1.findOne("13").getNume(), "Andrei");

    }

    @Test
    public void testAddStudentIdNull() {
        service.deleteStudent("11");
        assertNull(fileRepository1.findOne("11"));
        assertTrue(service.saveStudent(null, "Andrei", 937) == 1);
        assertNull(fileRepository1.findOne("11"));
    }

    @Test
    public void testAddStudentIdEmpty() {
        service.deleteStudent("11");
        assertNull(fileRepository1.findOne("11"));
        assertTrue(service.saveStudent("", "Andrei", 937) == 1);
        assertNull(fileRepository1.findOne("11"));
    }

    @Test
    public void testAddStudentNameNull() {
        service.deleteStudent("11");
        assertNull(fileRepository1.findOne("11"));
        assert(service.saveStudent("11", null, 937) == 1);
        assertNull(fileRepository1.findOne("11"));
    }

    @Test
    public void testAddStudentNameEmpty() {
        service.deleteStudent("11");
        assertNull(fileRepository1.findOne("11"));
        assert(service.saveStudent("11", "", 937) == 1);
        assertNull(fileRepository1.findOne("11"));
    }


    @Test
    public void testAddStudentGroupSmallInvalid() {
        service.deleteStudent("11");
        assertNull(fileRepository1.findOne("11"));
        assert(service.saveStudent("11", "", 110) == 1);
        assertNull(fileRepository1.findOne("11"));
    }

    @Test
    public void testAddStudentGroupLargeInvalid() {
        service.deleteStudent("11");
        assertNull(fileRepository1.findOne("11"));
        assert(service.saveStudent("11", "Andrei", 938) == 1);
        assertNull(fileRepository1.findOne("11"));
    }


    @Test
    public void testAddStudentGroupValid() {
        service.deleteStudent("11");
        assertNull(fileRepository1.findOne("11"));
        service.deleteStudent("12");
        assertNull(fileRepository1.findOne("12"));
        assertTrue(service.saveStudent("11", "Andrei", 111) == 1);
        assertTrue(service.saveStudent("12", "Andrei", 937) == 1);
        assertEquals(fileRepository1.findOne("11").getGrupa(), 111);
        assertEquals(fileRepository1.findOne("12").getGrupa(), 937);
    }

    @Test
    public void testAddTemaToRepository(){

        assertTrue(service.saveTema("11", "Tema1", 2, 1) == 0);
        assertEquals(fileRepository2.findOne("11").getDescriere(), "Tema1");

    }

    @Test
    public void testAddTemaDuplicateId() {

        assertTrue(service.saveTema("11", "Tema1", 2, 1) == 0);
        assert(service.saveTema("11", "Tema2", 4, 2) == 0);
    }

    @Test
    public void testAddAssignmentInvalid() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String id = null;
        String description = "description";
        int deadline = 1;
        int startline = 12;
        Tema assigment = new Tema(id, description, deadline, startline);

        try {
            fileRepository2.save(assigment);
        } catch (ValidationException ve) {
            assertEquals(ve.getMessage(), "ID invalid! ");
        }

        try {
            assertNull(fileRepository2.findOne(id));
            assert(false);
        } catch (IllegalArgumentException e){
            assertEquals("ID-ul nu poate fi null! \n", e.getMessage());
        }
    }

    @Test
    public void testAddAssignmentService() {

        String idAssignment = "100";
        String description = "description";
        int deadline = 2;
        int startline = 1;

        service.deleteTema(idAssignment);
        assertNull(fileRepository2.findOne(idAssignment));
        assertEquals(service.saveTema(idAssignment, description, deadline, startline), 1);
        assertEquals(fileRepository2.findOne(idAssignment).getDescriere(),description);
    }



    @Test
    public void testAddfileRepository2() {
        String idAssignment = "100";
        String description = "description";
        int deadline = 2;
        int startline = 1;

        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        fileRepository2.delete(idAssignment);
        assertNull(fileRepository2.findOne(idAssignment));
        assertNull(fileRepository2.save(assigment));
        assertEquals(fileRepository2.findOne(idAssignment).getDescriere(), description);
        assertEquals(fileRepository2.findOne(idAssignment).getStartline(), startline);
        assertEquals(fileRepository2.findOne(idAssignment).getDeadline(), deadline);
        assertEquals(fileRepository2.findOne(idAssignment).getID(), idAssignment);
    }

    @Test
    public void testAddfileRepository2Duplicate() {
        String idAssignment = "100";
        String description = "description";
        int deadline = 2;
        int startline = 1;

        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        fileRepository2.delete(idAssignment);
        assertNull(fileRepository2.findOne(idAssignment));

        long count = service.findAllTeme().spliterator().getExactSizeIfKnown();

        fileRepository2.save(assigment);

        try {
            fileRepository2.save(assigment);
        } catch (ValidationException ve){
            assertEquals(ve.getMessage(), "Duplicate ID! ");
        }

        long newCount = service.findAllTeme().spliterator().getExactSizeIfKnown();

        assertEquals(count + 1, newCount);

        assertEquals(fileRepository2.findOne(idAssignment).getDescriere(), description);
        assertEquals(fileRepository2.findOne(idAssignment).getStartline(), startline);
        assertEquals(fileRepository2.findOne(idAssignment).getDeadline(), deadline);
        assertEquals(fileRepository2.findOne(idAssignment).getID(), idAssignment);
    }

    @Test
    public void testAddAssignmentValidatorDeadline1() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String idAssignment = "100";
        String description = "description";
        int deadline = 15;
        int startline = 1;

        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        fileRepository2.delete(idAssignment);
        assertNull(fileRepository2.findOne(idAssignment));

        try {
            fileRepository2.save(assigment);
        } catch (ValidationException ve) {
            assertEquals("Deadline invalid! ", ve.getMessage());
        }
        assertNull(fileRepository2.findOne(idAssignment));
    }

    @Test
    public void testAddAssignmentValidatorDeadline2() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String idAssignment = "100";
        String description = "description";
        int deadline = 1;
        int startline = 3;

        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        fileRepository2.delete(idAssignment);
        assertNull(fileRepository2.findOne(idAssignment));

        try {
            fileRepository2.save(assigment);
        } catch (ValidationException ve) {
            assertEquals("Deadline invalid! ", ve.getMessage());
        }
        assertNull(fileRepository2.findOne(idAssignment));
    }


    @Test
    public void testAddAssignmentValidatorDeadline3() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String idAssignment = "100";
        String description = "description";
        int deadline = -1;
        int startline = 3;
        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        fileRepository2.delete(idAssignment);
        assertNull(fileRepository2.findOne(idAssignment));

        try {
            fileRepository2.save(assigment);
        } catch (ValidationException ve) {
            assertEquals("Deadline invalid! ", ve.getMessage());
        }
        assertNull(fileRepository2.findOne(idAssignment));
    }

    @Test
    public void testAddAssignmentValidatorStartLine() {

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String idAssignment = "100";
        String description = "description";
        int deadline = 2;
        int startline = -1;

        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        fileRepository2.delete(idAssignment);
        assertNull(fileRepository2.findOne(idAssignment));

        try {
            fileRepository2.save(assigment);
        } catch (ValidationException ve) {
            assertEquals("Data de primire invalida! \n", ve.getMessage());
        }
        assertNull(fileRepository2.findOne(idAssignment));

        outContent.reset();

        assertTrue(service.saveTema(idAssignment, description, deadline, startline) == 1);

        assertNull(fileRepository2.findOne(idAssignment));
        outContent.reset();

    }

    @Test
    public void testAddAssignmentValidatorDescriptionEmpty() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String idAssignment = "100";
        String description = "";
        int deadline = 2;
        int startline = 1;

        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        fileRepository2.delete(idAssignment);
        assertNull(fileRepository2.findOne(idAssignment));

        try {
            fileRepository2.save(assigment);
        } catch (ValidationException ve) {
            assertEquals("Descriere invalida! ", ve.getMessage());
        }
        assertNull(fileRepository2.findOne(idAssignment));

    }

    @Test
    public void testAddAssignmentValidatorDescriptionNull() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String idAssignment = "100";
        String description = "";
        int deadline = 2;
        int startline = 1;

        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        fileRepository2.delete(idAssignment);
        assertNull(fileRepository2.findOne(idAssignment));

        try {
            fileRepository2.save(assigment);
        } catch (ValidationException ve) {
            assertEquals("Descriere invalida! ", ve.getMessage());
        }
        assertNull(fileRepository2.findOne(idAssignment));

    }

    @Test
    public void testAddAssignmentValidatorDescriptionValid() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String idAssignment = "100";
        String description = "it should pass";
        int deadline = 2;
        int startline = 1;

        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        fileRepository2.delete(idAssignment);
        assertNull(fileRepository2.findOne(idAssignment));

        fileRepository2.save(assigment);

        assertNotNull(fileRepository2.findOne(idAssignment));

    }
}
