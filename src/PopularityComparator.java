import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: Hao Qu
 * Date: 12-9-18
 * Time: 下午2:06
 * Used for sort list of names based on popularity
 */
public class PopularityComparator implements Comparator {

    public int compare(Object o1, Object o2) {

        StringFeature feat1 = LangModel.nameDict.get(o1);
        StringFeature feat2 = LangModel.nameDict.get(o2);

        if (feat1.getPopularity() < feat2.getPopularity())
            return 1;
        else
            return 0;
    }
}
