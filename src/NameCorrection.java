import java.util.*;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Hao Qu
 * Date: 12-9-12
 * Time: 下午10:04
 * Error detection is done using the collaborative filtering framework, each string's n-gram is retrieved first,
 * then the k-Nearest Neighbor (kNN) algorithm is used based on similarity measures of edit distance, candidates are then filtered on
 * maximum number of common characters, in order of to determine the most probable candidates, popularity of each name is used.
 */
@SuppressWarnings("unchecked")
public class NameCorrection {

    private List<PersonName> nameList = ErrorDetection.invalidNames;

    public List<String> correctNames() {

        for (PersonName name : nameList) {

            if (!name.isValidGivenName()) {
                // Process name if given name and surname are concatenated together
                if (name.getSurName().equals("")) {
                    int len = name.getGivenName().length();
                    String tempName = name.getGivenName();
                    //replace the given name pattern with empty string so what's left is of pattern of surname
                    tempName = tempName.replaceAll("^[A-Z][a-z]+", "");
                    name.setGivenName(name.getGivenName().substring(0, len - tempName.length()));
                    name.setSurName(tempName);
                }

                String invalidName = name.getGivenName().toLowerCase();

                // Do a simple common OCR mistake correction
                invalidName = simpleOCRCorrection(invalidName);

                //Find the most similar names based on its n-grams
                List<String> candidates = kNNSearch(invalidName, LangModel.nGrams(invalidName, LangModel.nGramMin, LangModel.nGramMax));

                //Filter based on maximum number of common characters
                candidates = commonCharsFilter(invalidName, candidates);

                //Sort possible candidates
                Collections.sort(candidates, new PopularityComparator());
                name.setGivenNamesRec(candidates);

            }

            if (!name.isValidSurName()) {

                String invalidName = name.getSurName().toLowerCase();
                // Do a simple common OCR mistake correction
                invalidName = simpleOCRCorrection(invalidName);

                //Find the most similar names based on its n-grams
                List<String> candidates = kNNSearch(invalidName, LangModel.nGrams(invalidName, LangModel.nGramMin, LangModel.nGramMax));

                //Filter based on maximum number of common characters
                candidates = commonCharsFilter(invalidName, candidates);

                //Sort possible candidates
                Collections.sort(candidates, new PopularityComparator());
                name.setSurnamesRec(candidates);

            }
        }

        return correctionList();
    }

    // Returns a list of Strings that are ready to print out for name correction task.
    public List<String> correctionList() {
        List<String> correctList = new LinkedList<String>();
        for (PersonName name : nameList) {
            for (PersonName pName : potentialNames(name)) {

                String line = "\"" + name.getOrigName() + "\"" + "\t";
                if (pName.getGivenName().equals("")) {
                    line += "\"" + pName.getSurName() + "\"";
                } else if (pName.getSurName().equals("")) {
                    line += "\"" + pName.getGivenName() + "\"";
                } else {
                    line += pName;
                }
                correctList.add(line);
            }
        }
        return correctList;
    }

    // Returns the final list of potential names for an erroneous full name
    public List<PersonName> potentialNames(PersonName name) {
        List<PersonName> potentialNames = new ArrayList<PersonName>();

        if (name.isValidGivenName() && name.isValidSurName()) {
            potentialNames.add(name);
        } else if (!name.isValidGivenName() && !name.isValidSurName()) {

            if (!name.getGivenNamesRec().isEmpty() && !name.getSurnamesRec().isEmpty()) {
                for (String given : name.getGivenNamesRec()) {
                    for (String last : name.getSurnamesRec()) {
                        potentialNames.add(new PersonName(firstCharUpper(given), last.toUpperCase()));
                    }
                }
            } else if (name.getGivenNamesRec().isEmpty() && !name.getSurnamesRec().isEmpty()) {
                for (String last : name.getSurnamesRec())
                    potentialNames.add(new PersonName("", last.toUpperCase()));
            } else if (name.getSurnamesRec().isEmpty() && !name.getGivenNamesRec().isEmpty()) {
                for (String given : name.getGivenNamesRec())
                    potentialNames.add(new PersonName(firstCharUpper(given), ""));
            } else {
                potentialNames.add(new PersonName("", ""));
            }

        } else if (name.isValidGivenName())

        {
            for (String last : name.getSurnamesRec()) {
                potentialNames.add(new PersonName(name.getGivenName(), last.toUpperCase()));
            }
            if (name.getSurnamesRec().isEmpty())
                potentialNames.add(new PersonName(name.getGivenName(), ""));
        } else

        {
            for (String given : name.getGivenNamesRec()) {
                potentialNames.add(new PersonName(firstCharUpper(given), name.getSurName()));
            }
            if (name.getGivenNamesRec().isEmpty())
                potentialNames.add(new PersonName("", name.getSurName()));
        }

        return potentialNames;
    }

    /* Do processing for simple potential ocr errors
        some rules referenced from the paper http://www.cs.unc.edu/cms/publications/honors-theses-1/lian09.pdf
    */
    public String simpleOCRCorrection(String invalidName) {

        invalidName = invalidName.replaceAll("[0]", "o");
        invalidName = invalidName.replaceAll("[1]", "i");
        invalidName = invalidName.replaceAll("[27]", "z");
        invalidName = invalidName.replaceAll("[4]", "h");
        invalidName = invalidName.replaceAll("[5]", "s");
        invalidName = invalidName.replaceAll("[8]", "b");
        invalidName = invalidName.replaceAll("[69]", "g");

        return invalidName;
    }


    // Returns a list of candidate with edit distance smaller than 3
    public LinkedList<String> kNNSearch(String name, List<String> nGrams) {

        Set<String> candidateSet = new HashSet<String>();
        LinkedList<String> list = new LinkedList<String>();

        for (String nGram : nGrams) {
            if (LangModel.nGramDict.containsKey(nGram)) {
                candidateSet.addAll(LangModel.nGramDict.get(nGram));
            }
        }

        int minDist = minDistance(name, candidateSet);
        for (String str : candidateSet) {
            if (EditDistance.distance(name, str) == minDist) {
                list.add(str);
            }
        }
        return list;
    }

    // Returns the minimum edit distance among test name and candidates
    public int minDistance(String name, Set<String> candidateSet) {

        int curMinDist = 2, curDist;

        for (String str : candidateSet) {
            curDist = EditDistance.distance(name, str);
            if (curMinDist > curDist)
                curMinDist = curDist;
        }
        return curMinDist;
    }

    // Returns candidates with maximum common characters
    public List<String> commonCharsFilter(String name, List<String> candidates) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        List<String> list = new LinkedList<String>();
        int maxCommon = 0;
        for (String candidate : candidates) {
            int nCommon = nCommonChars(name, candidate);
            if (maxCommon < nCommon)
                maxCommon = nCommon;
            map.put(candidate, nCommon);
        }

        for (String candidate : map.keySet()) {
            if (map.get(candidate) == maxCommon) {
                list.add(candidate);
            }
        }
        return list;
    }

    // Returns the number of common characters with string s and t
    public int nCommonChars(String s, String t) {
        t = Pattern.quote(t);
        String commonChars = s.replaceAll("[^" + t + "]", "");
        return commonChars.length();
    }

    // Turn first letter of a string into uppercase
    public String firstCharUpper(String myString) {
        char[] stringArray = myString.toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        myString = new String(stringArray);
        return myString;
    }

}
