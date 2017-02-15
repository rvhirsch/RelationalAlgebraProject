/**
 * Created by Josh on 2/14/2017.
 */
public class Parser {


    public Parser() {

    }

    public String parseLatexToString (String input) {
        String statement = "";
        String prevWord = "";
        String curWord = "";
        String nextWord = "";

        for (int x = 0; x < input.length(); x++) {
            curWord += input.charAt(x);

            switch(curWord) {
                case("\\pi"):
                case("\\sigma"):
//                    statement += "SELECT " + nextWord;



                    break;
                case("\\bigtimes"):
            }


        }






        return statement;
    }

    public static void main(String[] args) throws Exception {
        Parser p = new Parser();
        String sampleInput1 = "\\pi_{name}(Person)";
        String sampleInput2 = "\\pi_{name}(\\sigma_{pizza}(Person \\bigtimes Eats))";


        System.out.println(p.parseLatexToString(sampleInput1));
    }

}
