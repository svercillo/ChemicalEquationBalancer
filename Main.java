
import java.util.*;

public class Main {

    public static class StringAndInteger {
        public String element;
        public Integer elementNum;

        public StringAndInteger(String elements,
                                Integer elementNum) {
            this.element = elements;
            this.elementNum = elementNum;
        }
    }

    public static class IntLists2 {
        public List<Integer> capitalsIndex;
        public List<Integer> coefficientIndex;

        public IntLists2(List<Integer> capitalsIndex,
                         List<Integer> coefficientIndex) {
            this.capitalsIndex = capitalsIndex;
            this.coefficientIndex = coefficientIndex;
        }
    }

    //    C2H6 + O2 --> CO2 + H2O
    public static List<String> uniqueElement(List<String> elementsList, String[] rxnArr) {
        List<String> uniqueElements = new ArrayList<>();
        for (int j = 0; j < rxnArr.length; j++) {
            String string = elementsList.get(j);
            List<String> elementwNumList = elementwNumParser(string, indexProducer(string).capitalsIndex);
            for (int i = 0; i < elementwNumList.size(); i++) {
                String element = elementInfoOneMole(elementwNumList.get(i)).element;
                if (!uniqueElements.contains(element)) {
                    uniqueElements.add(element);
                }
            }
        }
        Collections.sort(uniqueElements);
        return (uniqueElements);
    }

    public static List<String> elementStrings(String[] rxnStrings, String[] prodStrings) {

        List<String> strings = new ArrayList<>();
        for (int i = 0; i < rxnStrings.length; i++) {
            if (rxnStrings[i] != null)
                strings.add(rxnStrings[i]);
        }
        for (int i = 0; i < prodStrings.length; i++) {
            if (rxnStrings[i] != null)

                strings.add(prodStrings[i]);
        }
        return strings;
    }


    public static IntLists2 indexProducer(String oneElementString) {
        String string = oneElementString;
        List<Integer> capitalsIndex = new ArrayList<>();
        List<Integer> coefficientIndex = new ArrayList<>();
        for (int i = 0; i < oneElementString.length(); i++) {
            if (Character.isUpperCase(string.charAt(i)))
                capitalsIndex.add(i);
            if (Character.isDigit(string.charAt(i)))
                coefficientIndex.add(i);
        }
        IntLists2 holder = new IntLists2(capitalsIndex, coefficientIndex);
        return holder;
    }

    public static List<String> elementwNumParser(String string, List<Integer> capitalsIndex) {
        List<String> elementwNumList = new ArrayList<>();

        for (int i = 0; i < capitalsIndex.size(); i++) {
            if (i == capitalsIndex.size() - 1) {
                elementwNumList.add(string.substring(capitalsIndex.get(i)));
            } else {
                elementwNumList.add(string.substring(capitalsIndex.get(i), capitalsIndex.get(i + 1)));
            }
        }
        return elementwNumList;
    }

    public static StringAndInteger elementInfoOneMole(String elementwNum) {
        String string = elementwNum;
        List<Integer> digitIndex = new ArrayList<>();
        for (int i = 0; i < string.length(); i++) {
            if (Character.isDigit(string.charAt(i))) {
                digitIndex.add(i);
                break;
            }
        }
        if (digitIndex.size() != 0) {
            String element = string.substring(0, digitIndex.get(0));
            int elementNum = Integer.valueOf(string.substring(digitIndex.get(0)));
            StringAndInteger enn = new StringAndInteger(element, elementNum);
            return enn;
        } else {
            String element = string;
            int elementNum = 1;
            StringAndInteger enn = new StringAndInteger(element, elementNum);
            return enn;
        }
    }

    public static List<String> concatenatedList(List<List<String>> singleRxnElewNumsList) {
        List<String> concatinatedList = new ArrayList<>();
        for (int i = 0; i < singleRxnElewNumsList.size(); i++) {
            for (int j = 0; j < singleRxnElewNumsList.get(i).size(); j++) {
                concatinatedList.add(singleRxnElewNumsList.get(i).get(j));
            }
        }
        return concatinatedList;
    }
    public static Boolean isUniqueElement (List<StringAndInteger> rankList, StringAndInteger elementInfo) {
        List<Integer> intList = new ArrayList<>();
        for (StringAndInteger s : rankList){
            if(elementInfo.element.equals(s.element)){
                intList.add(0);
            }
        }
        if (intList.size() == 0){
            return true;
        }
        return false;
    }

    public static List<List<StringAndInteger>> rankListEachMole(List<String> elementStrings) {
        List<List<StringAndInteger>> rankListEachMole = new ArrayList<>();
//        String == "C2H6"
        for (String string : elementStrings) {
            List<Integer> capitalIndex = indexProducer(string).capitalsIndex;
//            singleRxnElewNums.get(O) == C2
            List<String> singleRxnElewNums = elementwNumParser(string, capitalIndex);
            Collections.sort(singleRxnElewNums);
            List <StringAndInteger> elementInfoList = new ArrayList<>();
            for (String s : singleRxnElewNums){
                StringAndInteger elementInfo = elementInfoOneMole(s);
                elementInfoList.add(elementInfo);
            }
            rankListEachMole.add(elementInfoList);
        }
        return rankListEachMole;
    }

    //    Needs to be interated through rankList
    public static List<StringAndInteger> zeroPutter (List<StringAndInteger> rankList, List<String> uniqueElements){
        List<StringAndInteger> zeroPutter = new ArrayList<>();
        Map<Integer, StringAndInteger> hashMap = new HashMap<>();
        for (int i=0; i<uniqueElements.size(); i++){
            List<StringAndInteger> elementInfoList = new ArrayList<>();
            for (StringAndInteger si : rankList){
                Boolean s = isUniqueElement(rankList, si);
                if(si.element.equals(uniqueElements.get(i)) && isUniqueElement(elementInfoList, si)){
                    elementInfoList.add(si);
                    hashMap.put(i, si);
                }
            }
        }
        for (int i=0; i<uniqueElements.size(); i++){
            if (hashMap.containsKey(i)){
                zeroPutter.add(i, hashMap.get(i));
            }else{
                StringAndInteger empty = new StringAndInteger(uniqueElements.get(i), 0);
                zeroPutter.add(empty);
            }
        }
        return zeroPutter;
    }

    public static double[][] matrix(List<List<StringAndInteger>> rankList) {
//         3
        int m = rankList.get(0).size();
//         4
        int n = rankList.size();
        double[][] matrix = new double[m][n];
        List<List<StringAndInteger>> columnList = rankList;
        //        For each column
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                matrix[i][j] = rankList.get(j).get(i).elementNum;
            }
        }
        return matrix;
    }

    //        C2H6 + O2 --> CO2 + H2O
    private static double[][] rref(double[][] matrix) {
        double[][] rref = new double[matrix.length][];
        for (int i = 0; i < matrix.length; i++)
            rref[i] = Arrays.copyOf(matrix[i], matrix[i].length);

        int r = 0;
        for (int c = 0; c < rref[0].length && r < rref.length; c++) {
            int j = r;
            for (int i = r + 1; i < rref.length; i++)
                if (Math.abs(rref[i][c]) > Math.abs(rref[j][c]))
                    j = i;
            if (Math.abs(rref[j][c]) < 0.00001)
                continue;

            double[] temp = rref[j];
            rref[j] = rref[r];
            rref[r] = temp;

            double s = 1.0 / rref[r][c];
            for (j = 0; j < rref[0].length; j++)
                rref[r][j] *= s;
            for (int i = 0; i < rref.length; i++) {
                if (i != r) {
                    double t = rref[i][c];
                    for (j = 0; j < rref[0].length; j++)
                        rref[i][j] -= t * rref[r][j];
                }
            }
            r++;
        }

        return rref;
    }

    public static List<Double> balancedNumbers (double[][] rref, List<String> uniqueElements, String[] rxnStrings, String[] prodStrings) {
//        List<Double> rawCoefficientList = new ArrayList<>();
//        for (int j=0; j<rref.length; j++){
//            for (int i=0; i<rref[j].length; i++)
//                rawCoefficientList.add(rref[][]);
//        }





        int m = uniqueElements.size()-1;
        int n = rxnStrings.length + prodStrings.length - 1;
        List<Double> doubleList = new ArrayList<>();
        int muliplier =0;
        List<Double> balancedNums = new ArrayList<>();
        for (int i = 0; i < m; i++)
            doubleList.add(rref[i][n]);

        for (int w = 2; w < 11; w++) {
            List<Boolean> bool = new ArrayList<>();
            for (int i = 0; i < doubleList.size(); i++) {
                double num = doubleList.get(i);
                if (w * num % 1 == 0) bool.add(true);
            }
            if (bool.size() == uniqueElements.size()) {
                muliplier = w;
                break;
            }
        }
        for (int i = 0; i < doubleList.size(); i++) {
            double value = doubleList.get(i)*muliplier;
            balancedNums.add(value);
        }
        return balancedNums;
    }

//        C2H6 + O2 --> CO2 + H2O
//        O2H4 + C2H6 + O2 --> CO2 + H2O

    public static void main(String[] args) {
//        receiving and splitting data into elements
        Scanner scanner = new Scanner(System.in);
        String whole = scanner.nextLine();
        String[] eqnSplitter = whole.split(" --> ");
        String[] rxnStrings = eqnSplitter[0].split(" \\+ ");
        String[] prodStrings = eqnSplitter[1].split(" \\+ ");
        List<String> elementStrings = elementStrings(rxnStrings, prodStrings);
        List<String> uniqueElements = uniqueElement(elementStrings, rxnStrings);
        List<List<StringAndInteger>> rankListEachMole = rankListEachMole(elementStrings);
        List<List<StringAndInteger>> rankList = new ArrayList<>();
        for (List<StringAndInteger> siList : rankListEachMole){
            rankList.add(zeroPutter(siList, uniqueElements));
        }
        double[][] rref = rref(matrix(rankList));

        List<Double> balancedEquation = balancedNumbers(rref,uniqueElements,rxnStrings,prodStrings);
        List <Double> f = balancedEquation;

        int fs = 0;
    }

}

//            List<StringAndInteger> rankListHolder = new ArrayList<>();
//            Map<String, StringAndInteger> map = new HashMap<>();
//            for (int i=0; i<uniqueElements.size(); i++) {
//                for (String s : singleRxnElewNums) {
////                StringAndInteger elementInfo = elementInfoOneMole(s);
////                    if (elementInfo.element.equals(uniqueElements.get(i)) && isUniqueElement(rankListHolder, elementInfo)) {
////                        rankListHolder.add(elementInfo);
////                        map.put(elementInfo.element, elementInfo);
////                    }
//                }
//            }
//            List<StringAndInteger> rankList = new ArrayList<>();
//            for (int i=0; i<uniqueElements.size(); i++){
//                if(map.keySet().contains(uniqueElements.get(i))){
//                    StringAndInteger sAndI = map.get(i);
//                    rankList.add(i,sAndI);
//                }
//                StringAndInteger si = new StringAndInteger(uniqueElements.get(i), 0);
//                rankList.add(si);
//            }
//            rankListEachMole.add(rankListHolder);