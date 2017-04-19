import junit.framework.TestCase;

public class ParserTest3 extends TestCase {
    Parser3 p;
    String sampleInput1 = "\\Pi_{name}(Person)";
    String sampleInput2 = "\\Pi_{name, age}(\\sigma_{age > 16}(Person \\bowtie Eats))";
    String sampleInput3 = "\\sigma_{age > 10 || name == 'sally'}(Person \\bowtie Eats)";
    String sampleInput4 = "_{age, name}\\G_{max(age)} (Person \\bowtie Eats)";
    String sampleInput5 = "\\Pi_{name, age}(\\sigma_{age > 10 || name == 'sally'}(Eats \\bowtie (Person \\cap Pizzeria)))";
    String sampleInput6 = "\\sigma(Person)";

    public void testCase1() throws Exception {
        p = new Parser3(sampleInput1);

        String test = p.runTest(sampleInput1, 1, p);
        String answer = "SELECT name FROM Person";

        assertEquals(test, answer);
    }

    public void testCase2() throws Exception {
        p = new Parser3(sampleInput2);

        String test = p.runTest(sampleInput2, 2, p);
        String answer = "SELECT name, age FROM (SELECT * FROM (Person INNER JOIN Eats) WHERE age > 16)";

        assertEquals(test, answer);
    }

    public void testCase3() throws Exception {
        p = new Parser3(sampleInput3);

        String test = p.runTest(sampleInput3, 3, p);
        String answer = "SELECT * FROM (Person INNER JOIN Eats) WHERE age > 10 OR name == 'sally'";

        assertEquals(test, answer);
    }

    public void testCase4() throws Exception {
        p = new Parser3(sampleInput4);

        String test = p.runTest(sampleInput4, 4, p);
        String answer = "SELECT max(age) FROM (person INNER JOIN eats) GROUP BY age, name";

        assertEquals(test, answer);
    }

    public void testCase5() throws Exception {
        p = new Parser3(sampleInput5);

        String test = p.runTest(sampleInput5, 5, p);
//        String answer = "SELECT name, age FROM (SELECT * FROM (eats INNER JOIN (person INTERSECT pizzeria))) WHERE age > 10 OR name == 'sally' ";
        String answer = "SELECT name, age FROM (SELECT * FROM (Eats INNER JOIN (Person INTERSECT Pizzeria)) WHERE age > 10 OR name == 'sally')";

        assertEquals(test, answer);
    }

    public void testCase6() throws Exception {
        p = new Parser3(sampleInput6);

        String test = p.runTest(sampleInput6, 6, p);
        String answer = "SELECT * FROM (person) ";

        assertEquals(test, answer);
    }

}