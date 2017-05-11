import junit.framework.TestCase;

public class ParserTest3 extends TestCase {
    Parser3 p;
    DBInfo d;

    public void testCase1() throws Exception {
        String sampleInput1 = "\\Pi_{name}(Person)";
        p = new Parser3(sampleInput1, d);

        String test = p.runTest(sampleInput1, 1, p);
        String answer = "SELECT name FROM Person";

        assertEquals(test, answer);
    }

    public void testCase2() throws Exception {
        String sampleInput2 = "\\Pi_{name, age}(\\sigma_{age > 16}(Person \\bowtie Eats))";
        p = new Parser3(sampleInput2, d);

        String test = p.runTest(sampleInput2, 2, p);
        String answer = "SELECT name, age FROM (SELECT * FROM Person NATURAL JOIN Eats WHERE age > 16)";

        assertEquals(test, answer);
    }

    public void testCase3() throws Exception {
        String sampleInput3 = "\\sigma_{age > 10 || name == 'sally'}(Person \\bowtie Eats)";
        p = new Parser3(sampleInput3, d);

        String test = p.runTest(sampleInput3, 3, p);
        String answer = "SELECT * FROM Person NATURAL JOIN Eats WHERE age > 10 OR name == 'sally'";

        assertEquals(test, answer);
    }

    public void testCase4() throws Exception {
        String sampleInput4 = "_{age, name}\\gamma_{max(age), name} (Person \\bowtie Eats)";
        p = new Parser3(sampleInput4, d);

        String test = p.runTest(sampleInput4, 4, p);
        String answer = "SELECT max(age), name FROM (Person NATURAL JOIN Eats) GROUP BY age, name";

        assertEquals(test, answer);
    }

    public void testCase5() throws Exception {
        String sampleInput5 = "\\Pi_{name, age}(\\sigma_{age > 10 || name == 'sally'}(Eats \\bowtie (Person \\cap Pizzeria)))";
        p = new Parser3(sampleInput5, d);

        String test = p.runTest(sampleInput5, 5, p);
        String answer = "SELECT name, age FROM (SELECT * FROM Eats NATURAL JOIN (Person INTERSECT Pizzeria) WHERE age > 10 OR name == 'sally')";

        assertEquals(test, answer);
    }

    public void testCase6() throws Exception {
        String sampleInput6 = "\\sigma(Person)";
        p = new Parser3(sampleInput6, d);

        String test = p.runTest(sampleInput6, 6, p);
        String answer = "SELECT * FROM Person";

        assertEquals(test, answer);
    }

    public void testCase7() throws Exception {
        String sampleInput7 = "_{age}\\Pi_{name}(Person)";
        p = new Parser3(sampleInput7, d);

        String test = p.runTest(sampleInput7, 7, p);
        String answer = "SELECT name FROM Person GROUP BY age";

        assertEquals(test, answer);
    }

    public void testCase8() throws Exception {
        String sampleInput8 = "_{age}\\Pi_{name}(Person)";
        p = new Parser3(sampleInput8, d);

        String test = p.runTest(sampleInput8, 8, p);
        String answer = "SELECT name FROM Person GROUP BY age";

        assertEquals(test, answer);
    }

    public void testCase9() throws Exception {
        String sampleInput9 = "\\sigma(Person \\foj Eats)";
        p = new Parser3(sampleInput9, d);

        String test = p.runTest(sampleInput9, 9, p);
        String answer = "SELECT * FROM Person LEFT JOIN Eats UNION SELECT * FROM Person RIGHT JOIN Eats";

        assertEquals(test, answer);
    }

}