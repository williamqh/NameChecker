import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Hao Qu
 * Date: 12-9-11
 * Time: 下午10:43
 * Contains methods that are used for Input and Output.
 */
@SuppressWarnings("unchecked")
public class IO {

    private static PreProcessing preProUtil = new PreProcessing();

    /* Read in a file line by line, specified by file path and the option to turn String read to lower case */
    public static List readInputToList(String path, int toLower) {

        List list = new LinkedList();
        File file = new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String rowString = "";
            String[] nameField = {};

            while ((rowString = reader.readLine()) != null) {
                nameField = preProUtil.extractNameField(rowString);
                if (toLower == 0)
                    list.add(nameField[0]);
                else
                    list.add(nameField[0].toLowerCase());
            }
            reader.close();
            isr.close();
            fis.close();
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, StringFeature> readInputToMap(String path, int toLower) {

        Map map = new HashMap<String, StringFeature>();

        File file = new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String rowString = "";
            String[] nameField = {};

            while ((rowString = reader.readLine()) != null) {
                nameField = preProUtil.extractNameField(rowString);
                String str = nameField[0];

                Double popularity = Double.parseDouble(nameField[1]);
                if (toLower == 0) {
                    if (map.containsKey(str)) {
                        Double newScore;
                        StringFeature feat = (StringFeature) map.get(str);
                        newScore = popularity + feat.getPopularity();
                        feat.setPopularity(newScore);
                        feat.setStr(str);
                        map.put(str, feat);
                    } else {
                        StringFeature feat = new StringFeature(str, popularity);
                        map.put(str, feat);
                    }
                } else {
                    if (map.containsKey(str)) {
                        Double newScore;
                        StringFeature feat = (StringFeature) map.get(str);
                        newScore = popularity + feat.getPopularity();
                        feat.setPopularity(newScore);
                        map.put(str.toLowerCase(), feat);
                    } else {
                        StringFeature feat = new StringFeature(str.toLowerCase(), popularity);
                        map.put(str.toLowerCase(), feat);
                    }
                }

            }

            reader.close();
            isr.close();
            fis.close();
            map.remove("");
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* Print list of Strings in a list */
    public static void printList(List<String> list) {
        for (String row : list) {
            System.out.println(row);
        }
    }

    /* output the content of a list to file */
    public static void outputListToFile(List<String> list, String outfilepath) {

        try {
            // Create file
            FileWriter fstream = new FileWriter(outfilepath);
            BufferedWriter out = new BufferedWriter(fstream);

            for (String row : list) {
                out.write(row);
                out.write("\n");
            }

            //Close the output stream
            out.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }


    /* save object to file */
    public static void saveObject(Object obj, String outfilepath) {
        try {
            FileOutputStream fs = new FileOutputStream(outfilepath);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(obj);
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* Read in a file line by line, specified by file path and the option to turn String read to lower case
    * Used for debugging
    * */
    public static List readGiven(String path) {


        List list = new LinkedList();
        File file = new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String rowString = "";
            String[] nameField = {};

            while ((rowString = reader.readLine()) != null) {

                nameField = preProUtil.extractNameField(rowString);

                if (true) {


                    list.add(nameField[0].toLowerCase());

                }

            }
            reader.close();
            isr.close();
            fis.close();
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
