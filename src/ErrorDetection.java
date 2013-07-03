import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Hao Qu
 * Date: 12-9-12
 * Time: 下午12:57
 * Error Detection is done by extracting names and checking the name dictionary nameDict.
 */
@SuppressWarnings("unchecked")
public class ErrorDetection {

    public static List<PersonName> invalidNames = new LinkedList<PersonName>();

    private List<String> errorList = new LinkedList<String>();

    public List<String> errorCheck(String testFilePath) {

        // Read in test file
        List<String> nameLines = IO.readInputToList(testFilePath, 0);

        errorList = processInvalidNames(nameLines);

        return errorList;

    }

    //Returns a list of strings that are ready to print out for task 1.
    public List<String> processInvalidNames(List<String> list) {
        PersonName name;
        for (String nameField : list) {

            name = extractName(nameField);

            if (name.hasError()) {
                errorList.add("\"" + name.getOrigName() + "\"");
                invalidNames.add(name);
            }
        }
        return errorList;
    }

    // Process the first column (PersonName Field) of the input file
    public PersonName extractName(String nameField) {

        PersonName name;
        String[] combinedName;
        int givenIndex, surIndex;

        givenIndex = -1;
        surIndex = -1;

        combinedName = nameField.split(" ");

        for (int i = 0; i < combinedName.length; i++) {

            Pattern pattern = Pattern.compile("^([a-zA-Z])(.*)");
            combinedName[i] = combinedName[i].replaceAll("[^a-zA-Z0-9]", "");
            Matcher matcher = pattern.matcher(combinedName[i]);
            if (matcher.matches()) {
                // Assign proper index
                if (givenIndex == -1) givenIndex = i;
                else if (surIndex < givenIndex) surIndex = i;
                //System.out.print(i + " " + combinedName[i] + givenIndex+surIndex);
            }
        }

        if (givenIndex == -1) {
            name = new PersonName(combinedName[0], "");
        } else if (surIndex == -1)
            name = new PersonName(combinedName[givenIndex], "");
        else
            name = new PersonName(combinedName[givenIndex], combinedName[surIndex]);


        // only valid if they are contained in the name dictionary and are not empty
        name.setValidGivenName(LangModel.nameDict.keySet().contains(name.getGivenName().toLowerCase()));
        name.setValidSurName(LangModel.nameDict.keySet().contains(name.getSurName().toLowerCase()));

        /* A name has no error if it contains strings are valid and does not contain other non-words symbols
           if givenIndex is not 0, it has other symbols at the front */
        if (name.isValidGivenName() && name.isValidSurName() && (givenIndex == 0)) {
            name.setError(false);
        }

        name.setOrigName(nameField);

        return name;
    }

}
