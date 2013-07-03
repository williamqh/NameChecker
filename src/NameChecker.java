/**
 * Created with IntelliJ IDEA.
 * User: Hao Qu
 * Date: 12-9-11
 * Time: 下午10:00
 * The entry point of the program.
 */

public class NameChecker {


    public static void main(String[] args) {

        // Specifying test file and training file path
        String testFilePath = "./inputFiles/ImperialChallenge.txt";

        String trainNameFilePath = "./trainingFiles/names.txt";
        String trainErrorFilePath = "./trainingFiles/missp.dat";

        String outputFile1 = "./outputFiles/HaoQU_Output1.txt";
        String outputFile2 = "./outputFiles/HaoQU_Output2.txt";

        // Build features from training files
        System.out.println("Training Model...");
        LangModel.trainModel(trainNameFilePath, trainErrorFilePath);

        // Print those names that have errors
        System.out.println("Processing..");
        ErrorDetection ed = new ErrorDetection();
        IO.outputListToFile(ed.errorCheck(testFilePath), outputFile1);

        // Print correction list
        NameCorrection ecc = new NameCorrection();
        IO.outputListToFile(ecc.correctNames(), outputFile2);
        System.out.println("Done.");


    }


}
