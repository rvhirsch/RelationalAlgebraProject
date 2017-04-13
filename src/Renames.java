import java.util.ArrayList;
import java.util.Arrays;

public class Renames {
    public class ReTup {
        private String name;
        private String latex;

        public ReTup() {
            name = "";
            latex = "";
        }

        public ReTup(String name, String latex) {
            this.name = name;
            this.latex = latex;
        }

        public String getName() {
            return this.name;
        }

        public String getLatex() {
            return this.latex;
        }

        public String toString() {
            String tr = "[" + name + ", " + latex + "]";
            return tr;
        }
    }

    private ArrayList<ReTup> associations;
    private String key = "/pi";
    private int keyLength = 3;

    public Renames() {
        this.associations = new ArrayList<>();
    }

    public void addAssoc(String name, String latex) {
        boolean goodToAdd = true;
        for (int x = 0; x < this.associations.size(); x++) {
            if (this.associations.get(x).getName().equalsIgnoreCase(name)) {
                System.out.println("Assoc already Exists!");
                goodToAdd = false;
            }
        }
        if (goodToAdd) {
            this.associations.add(new ReTup(name, latex));
        }
    }

    public String getAssoc(String name) {
        for (int x = 0; x < this.associations.size(); x++) {
            if (this.associations.get(x).getName().equalsIgnoreCase(name)) {
                return this.associations.get(x).getLatex();
            }
        }
        return null;
    }

    public void removeAssoc(String name) {
        for (int x = 0; x < this.associations.size(); x++) {
            if (this.associations.get(x).getName().equalsIgnoreCase(name)) {
                this.associations.remove(x);
            }
        }
    }

    public void clearAllAssoc() {
        this.associations.clear();
    }

    private String formatLatex(String str) {




        return str;
    }

    private Boolean containsNames(String str) {
        for (int x = 0; x < this.associations.size(); x++) {
            if (str.contains(this.associations.get(x).getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an an array list containing the char pos of the words if it exists, null if not
     */
    private ArrayList<Integer> getLocs(String str, String word) {
        if (!str.contains(word)) {
            return null;
        } else {
            ArrayList<Integer> list = new ArrayList<>();
            int index = 0;

            while (index >= 0) {
                index = str.indexOf(word, index+1);
                list.add(index);
            }
            list.remove(list.size()-1);
            return list;
        }
    }

    public String toString() {
        String tr = "{";
        for (int x = 0; x < this.associations.size(); x++) {
            tr += "[" + this.associations.get(x).getName() + ", " + this.associations.get(x).getLatex() + "]";

            if (x < this.associations.size()-1) {
                tr += ", ";
            }
        }
        tr += "}";
        return tr;
    }

    public Boolean scrub(String str) {
        if (str.contains(key)) {
            scrubIn(str);
            return true;
        } else if (containsNames(str)) {
            scrubOut(str);
            return false;
        }
        return false;
    }

    /**
     * Helper method for scrub, for inputting an assoc
     * @param str to parse for assoc
     */
    private void scrubIn(String str) {
        char[] tmp = str.toCharArray();
        int nameEndIndex = 0;
        String name = "";
        String latex = "";

        //get location of the end of the name
        nameEndIndex = str.indexOf(key);

        //get the name
        for (int y = 0; y < nameEndIndex; y++) {
            name += tmp[y];
        }

        //get the rest of the string
        for (int z = nameEndIndex+keyLength; z < tmp.length; z++) {
            latex += tmp[z];
        }

        this.addAssoc(name, latex);
    }

    /**
     * Helper method for scrub, places the LaTeX assoc into the latex string
     * @param temp to parse to add another string to
     */
    private String scrubOut(String temp) {
        String str = temp;
        System.out.println("Num of assocs: " + this.associations.size());
        for (int x = 0; x < this.associations.size(); x++) {
            str = str.replace(associations.get(x).getName(), associations.get(x).getLatex());
        }
        System.out.println();
        return str;
    }

    public static void main(String[] args) throws Exception {
        Renames re = new Renames();
        String test = "harbo/pidicksandtits";
        String test2 = "This is test a regular sentence with test some stuff mixed in test randomly!";

//        System.out.println(test2.replace("test","dick"));



        re.addAssoc("test", "testLaTeXString");

        System.out.println(re.scrubOut(test2));
        System.out.println(test2);

    }
}
