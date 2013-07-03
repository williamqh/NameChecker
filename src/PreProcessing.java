/**
 * Created with IntelliJ IDEA.
 * User: Hao Qu
 * Date: 12-9-11
 * Time: 下午11:01
 * Contains functions for preprocessing strings.
 */
public class PreProcessing {

    // Process an Input file's line, returns first column
    public String[] extractNameField(String row) {

        String[] nameField;

        row = row.replaceAll("\"", "").replaceAll(",", "\t").trim();

        nameField = row.split("\t");

        return nameField;
    }

}
