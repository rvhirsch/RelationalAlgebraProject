import java.util.Arrays;

public class Police {

    public static Boolean spellChecker(String str, DBInfo dbi) {
        //check table names
        for (int x = 0; x < dbi.getTBNames().size(); x++) {
            if (str.equalsIgnoreCase(dbi.getTBNames().get(x))) {
                return true;
            }
        }
        //check column names
        for (int y = 0; y < dbi.getColNames().size(); y++) {
            if (str.equalsIgnoreCase(dbi.getTBNames().get(y))) {
                return true;
            }
        }
        return false;
    }

    public static Boolean isTextOnlyNoSpace(String str) {
        return str.matches("[a-zA-Z]+");
    }

    public static Boolean isTextOnly(String str) {
        return str.matches("^[ A-Za-z]+$");
    }

    public static Boolean isAlphanumeric(String str) {
        return str.matches("[A-Za-z0-9]+");
    }

    public static Boolean isNumOnly(String str) {
        return str.matches("[-+]?\\d*\\.?\\d+");
    }

    public static Boolean isTableNameDup(String tbName, DBInfo dbi) {
        for (int x = 0; x < dbi.getTBNames().size(); x++) {
            if (tbName.equalsIgnoreCase(dbi.getTBNames().get(x))) {
                return true;
            }
        }
        return false;
    }

    public static Boolean isColNameDup(String[] cNames) {
        for (int x = 0; x < cNames.length-1; x++) {
            for (int y = x+1; y < cNames.length; y++) {
//                System.out.println("X" + x + ": " + cNames[x] + ", Y" + y + ": " + cNames[y]);
                if (cNames[x].equalsIgnoreCase(cNames[y])) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Boolean checkRowColTypes(String[] stuff, String[] colTypes) {
        for (int x = 0; x < stuff.length; x++) {
            switch(colTypes[x].toLowerCase()) {
                case "varchar":
                    if (isTextOnly(stuff[x])) {
                        break;
                    } else {
                        return false;
                    }
                case "integer":
                    if (isNumOnly(stuff[x])) {
                        break;
                    } else {
                        return false;
                    }
                case "boolean":
                    if (stuff[x].equalsIgnoreCase("true") || stuff[x].equalsIgnoreCase("false")) {
                        break;
                    } else {
                        return false;
                    }
                default:
                    return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
//        System.out.println(isTextOnly("Th&*is is a test"));
//        System.out.println(isTextOnlyNoSpace("fiftyfive"));

        String[] test = {"one","two","three","four","five","six","seven","seven"};
        System.out.println(isColNameDup(test));


    }
}
