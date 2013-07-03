import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Hao Qu
 * Date: 12-9-12
 * Time: 下午4:37
 * LangModel trains a list of global dictionaries (strings and n-grams) based on training files.
 */
@SuppressWarnings("unchecked")
public class LangModel {

    public static Map<String, StringFeature> nameDict;    // look up dictionary for error detection
    public static Map<String, StringFeature> errorDict;
    public static Map<String, List<String>> nGramDict;  // look up n-gram dictionary for error correction
    public static Map<String, List<String>> allNGramDict;

    public static final int nGramMin = 1, nGramMax = 5, toLower = 1;  // min and max limit of size of grams

    /* Read in the training file and turn characters into lower case.
        convert Person Names into unigrams, bigrams and trigrams etc...
     */
    public static void trainModel(String trainNameFilePath, String errorModelFilePath) {

        int isNameModel = 1;
        nameDict = trainDict(trainNameFilePath, isNameModel);
        errorDict = trainDict(errorModelFilePath, 0);

        setUpFeatures(nameDict, isNameModel);
        setUpFeatures(errorDict, 0);

    }

    // Trains n-gram dictionary as well as string dictionary
    private static Map<String, StringFeature> trainDict(String filePath, int isNameModel) {

        Map<String, StringFeature> dict = IO.readInputToMap(filePath, toLower);
        trainAllNGrams(dict);
        if (isNameModel == 1) {
            trainNGrams(dict);
        }
        return dict;
    }

    // When allNGramDict is complete, complete the features
    private static void setUpFeatures(Map<String, StringFeature> dict, int isNameModel) {

        for (String str : dict.keySet()) {
            dict.get(str).setUpRestFeatures(isNameModel);
        }
    }

    // Set up nGramDict which contains n-grams including name strings
    private static void trainNGrams(Map<String, StringFeature> dict) {

        // nGramDict hold all of ngrams from correct names
        nGramDict = new HashMap<String, List<String>>();

        for (String name : dict.keySet()) {
            List<String> nGrams = nGrams(name, nGramMin, nGramMax);

            for (String nGram : nGrams) {
                if (nGramDict.containsKey(nGram)) {
                    nGramDict.get(nGram).add(name);
                } else {
                    List<String> newNameList = new LinkedList<String>();
                    newNameList.add(name);
                    nGramDict.put(nGram, newNameList);
                }
            }
        }
    }

    // Set up allNGrams which contains all n-grams including error strings'
    private static void trainAllNGrams(Map<String, StringFeature> dict) {

        // nGramDict hold all of ngrams from correct names
        allNGramDict = new HashMap<String, List<String>>();

        for (String name : dict.keySet()) {

            List<String> nGrams = nGrams(name, nGramMin, nGramMax);

            for (String nGram : nGrams) {
                if (allNGramDict.containsKey(nGram)) {
                    allNGramDict.get(nGram).add(name);
                } else {
                    List<String> newNameList = new LinkedList<String>();
                    newNameList.add(name);
                    allNGramDict.put(nGram, newNameList);
                }
            }

        }

    }

    // Generating nGrams for String with minimum of min, maximum of max
    public static List nGrams(String name, int min, int max) {

        List<String> nGrams = new ArrayList<String>();
        int len = name.length();

        for (int i = 0; i < len; i++)
            for (int j = i + min; j < Math.min(len, i + max); j++) {
                nGrams.add(name.substring(i, j));
            }
        return nGrams;
    }

    // Used only when we want to output feature list
    private static void printFeatures(Map<String, StringFeature> dict, List<String> list) {

        for (String str : dict.keySet()) {
            StringFeature feat = dict.get(str);
            String newline = str + "," + feat.getPopularity() + "," + feat.getnValidGrams() + "," + feat.getnGramScore()
                    + "," + feat.getLength() + "," + feat.getnNonChar() + "," + feat.getHasDictEntry();

            list.add(newline);
        }
    }


}
