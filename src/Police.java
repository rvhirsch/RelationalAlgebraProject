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

    public static void main(String[] args) {
        System.out.println(isTextOnly("Th&*is is a test"));
        System.out.println(isTextOnlyNoSpace("fiftyfive"));
    }
}
