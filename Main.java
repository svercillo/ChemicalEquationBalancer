
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Logger;


/*
ASSUMPTIONS MADE
Input must be formatted like this C2H6 + O2 --> CO2 + H2O or like this
BaCl2 + Al2(SO4)3 --> Ba(SO4)3 + AlCl3
There may only be one polyatomic ion per molecule
There are no charges to balance
Polyatomic elements must be attached to an element such that the charges balance
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    private static boolean isInvalid;
    
    private static List<Character> barChars = Arrays.asList('!','@','#','%','*','=', '?', '\'', '<', '{', '}');

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

    //    Import this list from python script
    public static List<String> polyatomicList = Arrays.asList("(NH4)", "(NO2)", "(NO3)", "(NO4)", "(SO3)","(SO4)", "(SO2)", "(OH)");


    public static Integer whereIsPolyatomic (String string, List<String> polyatomicElements ) {
        for(String str: polyatomicElements){
            if (string.contains(str)){
                return string.indexOf(str);
            }
        }
        return -1;
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
        if (capitalsIndex.isEmpty())
            isInvalid =  true;
        return holder;
    }

    /*
    Consideration: this method as the polyatomic ion to te beginning of the list instead of at
    the end. This should be correct when the sort occurs.
    */
//    BaCl2 + Al2(SO4)3 --> Ba(SO4)3 + AlCl3
//    Cu + Ag(NO3) --> Ag + Cu(NO3)2

//    THIS FUNCTION IS FUCKED

    public static List<String> elementwNumParser(String string, List<Integer> capitalsIndex) {
        int ind = whereIsPolyatomic(string, polyatomicList);
        List<String> elementwNumList = new ArrayList<>();

        if (ind != -1){
            elementwNumList.add(string.substring(ind));
            string = string.substring(0, ind);
            for (int i=capitalsIndex.size()-1; i>-1; i--){
                if (ind < capitalsIndex.get(i)){
                    capitalsIndex.remove(i);
                }
            }
        }
        if (capitalsIndex.size() ==1){
            if (capitalsIndex.get(0) == 0){
                elementwNumList.add(string);
            } else {
                isInvalid = true;
                LOG.info("Issue processing and parsing string");
            }
        } else {

            for (int i = 0; i < capitalsIndex.size(); i++) {
                if (i == capitalsIndex.size() - 1) {
                    elementwNumList.add(string.substring(capitalsIndex.get(i)));
                } else {
                    elementwNumList.add(string.substring(capitalsIndex.get(i), capitalsIndex.get(i + 1)));
                }
            }
        }
        return elementwNumList;

    }

    //    BaCl2 + Al2(SO4)3 --> Ba(SO4)3 + AlCl3
//    BROKENN!!!!
    public static List<String> uniqueElement(List<String> moleStrList, String[] rxnArr) {

        List<String> uniqueElements = new ArrayList<>();
        for (int j = 0; j < rxnArr.length; j++) {

            String string = moleStrList.get(j);
            List<String> elementwNumList = elementwNumParser(string, indexProducer(string).capitalsIndex);

            for (String elementWnum : elementwNumList) {
                String element = elementInfoOneMole(elementWnum).element;
                if (!uniqueElements.contains(element)) {
                    uniqueElements.add(element);
                }
            }
        }
        if (uniqueElements.isEmpty()){
            isInvalid = true;
        }

        return (uniqueElements);
    }

    //    BaCl2 + Al2(SO4)3 --> Ba(SO4)3 + AlCl3
    public static StringAndInteger elementInfoOneMole(String elementwNum) {
//        This passes a single element wihtin a molecule string, for example it would pass O2 or Ca for example
        String string = elementwNum;
        if (!string.contains("(")) {
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
        } else {
            int elementNum = 1;
            if (!string.substring(string.lastIndexOf(")")).equals(")")) {
                elementNum = Integer.parseInt(string.substring(string.lastIndexOf(')') + 1));
            }
            String polyatomic = string.substring(string.indexOf('(')+1, string.lastIndexOf(')'));
            StringAndInteger enn = new StringAndInteger(polyatomic, elementNum);
            return enn;
        }
    }
//    Pb(SO4) > 10PBSO3 + 3O2
//    BaCl2 + Al2(SO4)3 --> Ba(SO4)3 + AlCl3
//    Note: Polyatomic ions are always listed first in the rankLIst
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

    //    BaCl2 + Al2(SO4)3 --> Ba(SO4)3 + AlCl3
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

    private static List<double[][]> rrefList = new ArrayList<>();

    private static boolean isUnique (double[][] rref){
        Set<double[][]> rrefSet = new HashSet<>(rrefList);
        return rrefSet.size() == rrefList.size();
    }
    //        C2H6 + O2 --> CO2 + H2O
    private static double[][] rref(double[][] matrix) {
        double[][] rref = matrix;
        Set<double[][]> rrefSet = new HashSet<>(rrefList);
        while (isUnique(rref)){
            rref = rowReducer(rref);
        }
        return rref;
    }

    private static Map<int[], Double> pivotMapper(double[][] matrix) {
        double[][] rref = matrix;
        List<Integer> pivotRows = new ArrayList<>();

//        key for hashmap will be the column # and the value will be the pivot value
        Map<int[], Double> pivotMap = new HashMap<>();
        for (int t = 0; t < rref[0].length; t++) {
            double pivot = -1;
            int pivotRow = 0;
            for (int s = 0; s < rref.length; s++) {
                double st = rref[s][t];
//                there can only be one pivot per column
                if (st != 0 && pivot == -1 && !pivotRows.contains(s)) {
                    pivot = rref[s][t];
                    pivotRows.add(s);
                    int[] array = new int[]{s, t};
                    pivotMap.put(array, pivot);
                }
            }
        }
        pivotMap = mapSorter(pivotMap);
        return pivotMap;

    }

    private static double[][] rowReducer(double[][] matrix) {
        double[][] rref = matrix;
        Map<int[], Double> pivotMap = (pivotMapper(rref));

        for (Map.Entry<int[], Double> entry : pivotMap.entrySet()) {
            boolean isChanged = false;
            double pivot = entry.getValue();
            int i = entry.getKey()[0];
            int j = entry.getKey()[1];
            double[] pivotRow = rref[i];

            for (int s = 0; s < rref.length; s++) {
                double mul = -(rref[s][j]/ pivot);
                if (rref[s][j] != 0 && s != i){
                    for (int t=0; t<rref[0].length; t++){
                        double d = rref[s][j];
                        rref[s][t] = rref[s][t] + mul * rref[i][t];
                        isChanged =true;
                    }
                }
            }
            if (isChanged)
                break;

        }
        rrefList.add(rref);
        return rref;
    }

    private static Map<int[], Double> mapSorter(Map<int[], Double> pivotMap) {
        Map<int[], Double> hashMap = new LinkedHashMap<>();
        List<Map.Entry<int[], Double>> entryList = new ArrayList<>(pivotMap.entrySet());

        Collections.sort(entryList, new Comparator<Map.Entry<int[], Double>>() {
            @Override
            public int compare(Map.Entry<int[], Double> o1, Map.Entry<int[], Double> o2) {
                return o1.getKey()[1] - o2.getKey()[1];
            }
        });

        for (Map.Entry<int[], Double> e : entryList){
            hashMap.put(e.getKey(), e.getValue());
        }
        return hashMap;
    }

    public static List<Integer> balancedNumbers (double[][] rref, List<String> uniqueElements, String[] rxnStrings, String[] prodStrings) {
        DecimalFormat df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.HALF_UP);
        List<Double> rawCoefficientList = new ArrayList<>();
//        rref.length gives the number of ROWS
        for (int i=0; i<rref.length; i++){
//            rref[j].length gives the number of COLUMNS
            rawCoefficientList.add(rref[i][rref[i].length-1]);
        }
        int lowestPossibleCount =0;
        for (int count=1; count<33; count++) {
            List<Boolean> isIntegerList = new ArrayList<>();
            List<Double> doubleList = new ArrayList<>();
            for (Double d : rawCoefficientList) {
                double doub = Double.parseDouble(df.format(d * count));
                if (doub % 1 == 0) {
                    isIntegerList.add(true);
                }
            }
            if (isIntegerList.size()==rawCoefficientList.size()){
                lowestPossibleCount = count;
                break;
            }
        }
        for (int i=rawCoefficientList.size()-1; i>-1; i--){
            if (rawCoefficientList.get(i) == 0){
                rawCoefficientList.remove(i);
            }

        }
//        if count != 0 || null;
        List<Integer> balancedNums = new ArrayList<>();
        for (Double d : rawCoefficientList){
            int balancedInt = Math.abs((int) Math.round(d*lowestPossibleCount));
            balancedNums.add(balancedInt);
        }
        balancedNums.add(lowestPossibleCount);

        return balancedNums;
    }

    //        Cu + Ag(NO3) --> Ag + Cu(NO3)2
    public static String balancedEquation(List<String> elementStrings, List<Integer> balancedNumbers){
        int reactantsIndex = 0;
        int count =-1;
        for (int i : balancedNumbers) {
            count++;
            if (i<0) {
                break;
            }
        }
        List<String> rxnElements = new ArrayList<>();
        List<String> prodElements = new ArrayList<>();
        for (int i=0; i<elementStrings.size(); i++ ){
            if ( i< count-1){
                rxnElements.add(elementStrings.get(i));
            } else{
                prodElements.add(elementStrings.get(i));
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        int commonInt =0;
        for (int i=0; i<rxnElements.size(); i++) {
            stringBuilder.append(balancedNumbers.get(i) + " " + rxnElements.get(i) + " + ");
            commonInt=i;
        }
        stringBuilder.delete(stringBuilder.lastIndexOf("+ "), stringBuilder.length());
        stringBuilder.append(" ---> ");
        for (int i=0; i<prodElements.size(); i++) {
            stringBuilder.append(balancedNumbers.get(i+commonInt+1) + " " + prodElements.get(i) + " + ");
        }
        stringBuilder.delete(stringBuilder.lastIndexOf("+ "), stringBuilder.length());
        String balancedEqn = stringBuilder.toString();
        return balancedEqn;
    }

//        C3H8 + O2 --> CO2 + H2O
//        C2H6 + O2 --> CO2 + H2O
//        CH6 + O2 --> CO2 + H2O
//        Cu + Ag(NO3) --> Ag + Cu(NO3)2
//        Al2O3 + Fe --> Fe3O4 + Al
//        P4O10 + H2O --> H3PO4
//        Al + H2(SO4) --> Al2(SO4)3 + H2

//WHY CANT IT SOLVE THIS ONE???
//        Al2(SO3)3 + Na(OH) --> Na2(SO3) + Al(OH)3


    /*    ==================================================
      HELPER FUNCTIONS
*/
    public static List<String> elementStrings(String[] rxnStrings, String[] prodStrings) {

        List<String> strings = new ArrayList<>();
        for (int i = 0; i < rxnStrings.length; i++) {
            if (rxnStrings[i] != null)
                strings.add(rxnStrings[i]);
        }
        for (int i = 0; i < prodStrings.length; i++) {
            if (prodStrings[i] != null){
                strings.add(prodStrings[i]);
            }


        }
        return strings;
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

    public static void main(String[] args) {
//        receiving and splitting data into elements
        Scanner scanner = new Scanner(System.in);
        String whole = scanner.nextLine().trim();
//        If the string doesn't start with a valid uppercase
        if (whole.charAt(0) != Character.toUpperCase(whole.charAt(0))) {
            isInvalid = true;
        }
//        if the string contains an invalid character 
        for (char c : whole.toCharArray()){
            if (barChars.contains(c)){
                isInvalid = true;
            }
        }
        if (isInvalid){
//            Do something bad here
        } else {


            String[] eqnSplitter = whole.split(" --> ");
            String[] rxnStrings = eqnSplitter[0].split(" \\+ ");
            String[] prodStrings = eqnSplitter[1].split(" \\+ ");
            List<String> elementStrings = elementStrings(rxnStrings, prodStrings);
            List<String> uniqueElements = uniqueElement(elementStrings, rxnStrings);
            List<List<StringAndInteger>> rankListEachMole = rankListEachMole(elementStrings);
            List<List<StringAndInteger>> rankList = new ArrayList<>();
            for (List<StringAndInteger> siList : rankListEachMole) {
                rankList.add(zeroPutter(siList, uniqueElements));
            }
            double[][] rref = rref(matrix(rankList));

            List<Integer> balancedEquation = balancedNumbers(rref, uniqueElements, rxnStrings, prodStrings);
            String s = balancedEquation(elementStrings, balancedEquation);
            System.out.println(s);
        }
    }
}