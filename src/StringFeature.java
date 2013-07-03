import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Hao Qu
 * Date: 12-9-13
 * Time: 下午10:05
 * Models a string using some statistical measures.
 */
@SuppressWarnings("unchecked")
public class StringFeature {

    /*
     1. popularity (ranking, count)
     2. n_gram score
     3. number of valid n_grams
     4. has an entry in the dictionary
     5. length
     6. number of non characters in string
     */

    private String str;
    private double popularity;
    private double nGramScore;  // defined as the proportion of nGrams that are contained in the correct nGram dictionary
    private int nValidGrams;    // number of nGrams that are contained in the correct str nGram Dictionary
    private int hasDictEntry;
    private int length;
    private int nNonChar;

    public StringFeature(String str, double popularity) {
        this.str = str;
        this.popularity = popularity;
    }

    public void setUpRestFeatures(int isTrainingCorrectNames) {
        this.setnValidGrams(isTrainingCorrectNames);
        this.setLength();
        this.setnNonChar();
        this.setHasDictEntry(isTrainingCorrectNames);
        this.setnGramScore();
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getnGramScore() {
        return nGramScore;
    }

    public void setnGramScore() {
        int validNameNumber = 0, allNameNumber = 0;
        List<String> nGrams = LangModel.nGrams(str, LangModel.nGramMin, LangModel.nGramMax);

        for (String str : nGrams) {

            if (LangModel.nGramDict.containsKey(str)) {
                validNameNumber++;
                allNameNumber++;
            } else {
                allNameNumber++;
            }
        }

        if (nGrams.size() != 0) {
            this.nGramScore = (double) validNameNumber / allNameNumber;
        } else {
            this.nGramScore = 0;
        }


    }

    public int getnValidGrams() {
        return nValidGrams;
    }

    public void setnValidGrams(int isTrainingCorrectNames) {
        List<String> nGrams = LangModel.nGrams(str, LangModel.nGramMin, LangModel.nGramMax);
        if (isTrainingCorrectNames == 1) {
            this.nValidGrams = nGrams.size();
        } else {
            this.nValidGrams = 0;
            for (String tempStr : nGrams) {
                if (LangModel.nGramDict.containsKey(tempStr))
                    this.nValidGrams++;
            }

        }
    }

    public int getHasDictEntry() {
        return hasDictEntry;
    }

    public void setHasDictEntry(int isTrainingCorrectNames) {
        this.hasDictEntry = isTrainingCorrectNames;
    }

    public int getLength() {
        return length;
    }

    public void setLength() {
        this.length = str.length();
    }

    public int getnNonChar() {
        return nNonChar;
    }

    public void setnNonChar() {
        String nonChar = str.replaceAll("[a-zA-Z]+", "");
        this.nNonChar = nonChar.length();
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }


}
