import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Hao Qu
 * Date: 12-9-12
 * Time: 上午1:36
 * PersonName models a person name.
 */
public class PersonName {

    private String origName;
    private String givenName;
    private String surName;
    private List<String> givenNamesRec; // Given name correction list
    private List<String> surnamesRec;  // Surname correction list
    private boolean validGivenName;
    private boolean validSurName;
    private boolean error;  // A name has an error if its name string contains illegal symbols other than characters

    public PersonName(String givenName, String surName) {
        this.givenName = givenName;
        this.surName = surName;
        this.setError(true);
        this.setValidSurName(false);
        this.setValidGivenName(false);
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public boolean hasError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String toString() {
        return "\"" + givenName + "  " + surName + "\"";
    }

    public boolean isValidGivenName() {
        return validGivenName;
    }

    public void setValidGivenName(boolean validGivenName) {
        this.validGivenName = validGivenName;
    }

    public boolean isValidSurName() {
        return validSurName;
    }

    public void setValidSurName(boolean validSurName) {
        this.validSurName = validSurName;
    }

    public String getOrigName() {
        return origName;
    }

    public void setOrigName(String origName) {
        this.origName = origName;
    }

    public List<String> getGivenNamesRec() {
        return givenNamesRec;
    }

    public void setGivenNamesRec(List<String> givenNamesRec) {
        this.givenNamesRec = givenNamesRec;
    }

    public List<String> getSurnamesRec() {
        return surnamesRec;
    }

    public void setSurnamesRec(List<String> surnamesRec) {
        this.surnamesRec = surnamesRec;
    }

}
