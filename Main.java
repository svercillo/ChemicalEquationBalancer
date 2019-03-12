package com.company;

import java.util.*;


public class Main {
    public static void main(String[] args) {

    }
    public static class StringAndInteger {
        public String elements;
        public int elementNum;

        public StringAndInteger(String elements,
                                int elementNum) {
            this.elements = elements;
            this.elementNum = elementNum;
        }
    }
    public static class Ranker {
        public StringAndInteger stringAndInteger;
        public int moleculeNum;

        public Ranker(StringAndInteger stringAndInteger,
                      int moleculeNum) {
            this.stringAndInteger = stringAndInteger;
            this.moleculeNum = moleculeNum;
        }
    }

    public static class MatrixRow  {
        public String elements;
        public int elementNum;
        public  int moleNum;
        public MatrixRow (String elements,
                          int elementNum,
                          int moleNum) {
            this.elements = elements;
            this.elementNum = elementNum;
            this.moleNum = moleNum;
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

    public static Boolean isCapital(char character) {
        if (Character.isUpperCase(character)) {
            return true;
        }
        return false;
    }

    public static Boolean isDigit(char character) {
        if (Character.isDigit(character)) {
            return true;
        }
        return false;
    }

    //    premise, there will always be a number before the first element, all elements start with a capital,
//    and all od the elements
    public static IntLists2 indexProducer(String string) {
        List<Integer> capitalsIndex = new ArrayList<>();
        List<Integer> coefficientIndex = new ArrayList<>();
        for (int i = 0; i < string.length(); i++) {
            String iter = Character.toString(string.charAt(i));
            if (Character.isUpperCase(iter.charAt(i))) {
                capitalsIndex.add(i);
            }
            if (Character.isDigit(iter.charAt(i))) {
                coefficientIndex.add(i);
            }
            if (capitalsIndex.size() < 1) {
                System.out.println("ERROR INPPUT INVALID, input in proper format with capital letters");
            }
        }
        IntLists2 holder = new IntLists2(capitalsIndex, coefficientIndex);
        return holder;
    }

    public static List<String> elementwNumParser(String string, List<Integer> capitalsIndex) {
        List<String> elementwNum = new ArrayList<>();


        for (int i = 0; i < capitalsIndex.size(); i++) {
            if (i == capitalsIndex.size() - 1) {
                elementwNum.add(string.substring(capitalsIndex.get(i)));
            } else {
                elementwNum.add(string.substring(capitalsIndex.get(i), capitalsIndex.get(i + 1)));
            }
        }
        return elementwNum;
    }

    public static int moleculeNum(String string, List<Integer> capitalIndex) {
        int moleculeNum = 0;
        if (capitalIndex.get(0) != 0) {
            String moleNum = string.substring(0, capitalIndex.get(0));
            moleculeNum = Integer.parseInt(moleNum);
        } else {
            moleculeNum = 1;
        }
        return moleculeNum;
    }


//

    public static StringAndInteger elementAndNumbers(String elementwNum, IntLists2 indexProducer) {
        String element = null;
        int elementNum = 0;
        if (!isCapital(elementwNum.charAt(0))) {
            System.out.println("PLEASE INPUT DATA IN CORRECT FORMAT");
        }
        for (int i = 0; i < elementwNum.length(); i++) {
            if (isDigit(elementwNum.charAt(i))) {
                element = elementwNum.substring(0, indexProducer.coefficientIndex.get(i));
                elementNum = Integer.parseInt(elementwNum.substring(indexProducer.coefficientIndex.get(i)));
            } else {
                element = elementwNum;
                elementNum = 1;
            }
        }
        StringAndInteger enn = new StringAndInteger(element, elementNum);
        return enn;

    }

    public static String[] rxnStrings(){
        Scanner scanner = new Scanner(System.in);
        String whole = scanner.nextLine();
        String[] eqnSplitter = whole.split("-->");
        String[] rxnSplitter = eqnSplitter[0].split("\\ + ");
        return  rxnSplitter;
    }
    public static String[] prodStrings(){
        Scanner scanner = new Scanner(System.in);
        String whole = scanner.nextLine();
        String[] eqnSplitter = whole.split("-->");
        String[] prodSplitter = eqnSplitter[0].split("\\ + ");
        return  prodSplitter;
    }

    public static List<StringAndInteger> elementParser (String singleRxnMolecule) {
        String string = singleRxnMolecule;
        List<String> elementwNumList = elementwNumParser(string, indexProducer(string).capitalsIndex);
        List<StringAndInteger> elementInfo = new ArrayList<>();
        List<String> uniqueElements = new ArrayList<>();

        for (int i = 0; i < elementwNumList.size(); i++) {
            String element = elementAndNumbers(elementwNumList.get(i), indexProducer(elementwNumList.get(i))).elements;
            int elementNum = elementAndNumbers(elementwNumList.get(i), indexProducer(elementwNumList.get(i))).elementNum;
            StringAndInteger si = new StringAndInteger(element, elementNum);
            elementInfo.add(si);
            if (uniqueElements.indexOf(element) == 0)
                uniqueElements.add(element);
        }
        return elementInfo;
    }
    //
    private static List<String> uniqueElement (String[] rxnArr) {
        List<String> uniqueElements = new ArrayList<>();
        for (int j=0; j<rxnArr.length; j++) {
            String string = rxnArr[j];
            List<String> elementwNumList = elementwNumParser(string, indexProducer(string).capitalsIndex);
            for (int i = 0; i < elementwNumList.size(); i++) {
                String element = elementAndNumbers(elementwNumList.get(i), indexProducer(elementwNumList.get(i))).elements;
                if (uniqueElements.indexOf(element) == 0)
                    uniqueElements.add(element);
            }
        }
        return uniqueElements;
    }

    public static List<Ranker> rankList(List<String> uniqueElements, List<StringAndInteger>elementInfo1) {
        List<Ranker> rankList = new ArrayList<>();
        for (int j=0; j<uniqueElements.size(); j++) {
            for (int i = 0; i < elementInfo1.size(); i++){
                if (elementInfo1.get(i).elements.equals(uniqueElements.get(j))){
                    Ranker rank = new Ranker(elementInfo1.get(i), j);
                    rankList.add(rank);
                }
            }
        }
        return rankList;
    }

    public static List<Ranker> zeroPutterAtEnd (List<Ranker> rankList, List<String> uniqueElements) {
//            check if last unique element is present
        if (rankList.get(rankList.size()-1).stringAndInteger.elements != uniqueElements.get(uniqueElements.size()-1)) {
            List<Ranker> zeroAddedRankList = new ArrayList<>();
            for (int i=0; i<uniqueElements.size()-rankList.size(); i++) {
                StringAndInteger s = new StringAndInteger(null, 0);
                Ranker rank = new Ranker(s, rankList.get(rankList.size()-1).moleculeNum);
                rankList.add(rank);
            }
            return rankList;
        }
        return rankList;

    }
//              MAKE SURE THIS CONTAINS THE REACTANTS AND THE PRODUCTS
    public static List<Ranker> zeroPutterAtBeginning (List<Ranker> rankListZerosAtEnd, List<String> uniqueElements) {
        List<Ranker> rankList = rankListZerosAtEnd;
//               check if last unique element is present
        if (rankList.get(rankList.size()-1).stringAndInteger.elements != uniqueElements.get(uniqueElements.size()-1)) {
            List<Ranker> zeroAddedRankList = new ArrayList<>();
            for (int i=0; i<uniqueElements.size()-rankList.size(); i++) {
                StringAndInteger s = new StringAndInteger(null, 0);

                Ranker rank = new Ranker(s, rankList.get(0).moleculeNum);
                zeroAddedRankList.add(rank);
            }
            for (Ranker s: rankList) {
                zeroAddedRankList.add(s);
            }
            return zeroAddedRankList;
        }
        return rankList;
    }

    public static double[][] matrix(List<Ranker> fromzeroPutterAtBeginning, List<String> uniqueElements, String[] rxnStrings, String[] prodStrings){
        List<Ranker> rankList = fromzeroPutterAtBeginning;
        int m = uniqueElements.size();

//        indexed from 0????


        int n = rxnStrings.length + prodStrings.length-1;
        double[][] matrix = new double[m][n];
//        For each column
        for (int j=0; j<n; j++){
//            For each row in that given column
            for(int i=0; i<m; i++){
                matrix[i][j] = rankList.get(i).stringAndInteger.elementNum;
            }
        }
        return matrix;
    }


    public static List<Double> balancedNumbers (double[][] rref, List<String> uniqueElements, String[] rxnStrings, String[] prodStrings) {
        int m = uniqueElements.size();
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

    public static double[][] rref(double[][] matrix) {
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










//
//
//    public static List<MatrixRow> rowList (List<Ranker> zeroPutterAtBeginning) {
//        String[] rxnArr = rxnStrings();
//        String[] prodArr = prodStrings();
//        List<String> uniqueElements = uniqueElement(rxnArr);
//        List<MatrixRow> rowList = new ArrayList<>();
//        List<Ranker> rankList = zeroPutterAtBeginning;
//        for (int i=0; )
//
//
//
//
//    }

//
//
//    public static List<MatrixRow> columnMaker (List<MatrixRow> rowList) {
//        List<String> uniqueElements = elementParser().uniqueElements;
//        for (int i=0; i<rowList.size(); i++) {
//
//        }
//
//
//
//
//    }
//
//
//
//
//
//
//
//
//
//        String[] rxnSplitter = rxnStrings();
//        List<String> rxnElementsList = new ArrayList<>();
//        List<Integer> rxnElementNum = new ArrayList<>();
//        List<Integer> rxnMoleculePlacement = new ArrayList<>();
//        List<String> strings = new ArrayList<>();
//        static List<String> uniqueElements = new ArrayList<>();
//        Map<Integer, Map<Integer, String>> moleculeMap = new HashMap<>();
//        Map<Integer, String> elementMap = new HashMap<>();
//        for (int j = 0; j < rxnSplitter.length; j++) {
//            String string = rxnSplitter[j];
//            strings.add(string);
//            List<Integer> capitalIndex = indexProducer(string).capitalsIndex;
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//                List<Integer> elementNumMolNum = new ArrayList<>();
//                elementNumMolNum.add(j, elementNum);
//                rxnElementsList.add(element);
//                rxnElementNum.add(elementNum);
//                rxnMoleculePlacement.add(j);
//                if (uniqueElements.indexOf(element) == 0)
//                    uniqueElements.add(element);
//            }
//            for (int i=0; i<rxnElementNum.size(); i++){
//                if ()
//            }
//        }
//        for (int i=0; i<uniqueElements.size(); i++) {
//            elementMap.put(i, uniqueElements.get(i));
//        }
//
//
//    }
//
//
//    public static ColossalMap colossalMap() {
//        Scanner scanner = new Scanner(System.in);
//        String whole = scanner.nextLine();
//        String[] eqnSplitter = whole.split("-->");
//        String[] rxnSplitter = eqnSplitter[0].split("\\ + ");
//        String[] prodSplitter = eqnSplitter[1].split("\\ + ");
//        List<String> rxnElementsList = new ArrayList<>();
//        List<String> prodElementsList = new ArrayList<>();
//        List<Integer> rxnElementNum = new ArrayList<>();
//        List<Integer> prodElementNum = new ArrayList<>();
//        List<Integer> rxnMoleculePlacement = new ArrayList<>();
//        List<Integer> prodMoleculePlacement = new ArrayList<>();
//        Map<Integer, String> elementMap = new HashMap<>();
//        List<String> strings = new ArrayList<>();
//        List<String> uniqueElements = new ArrayList<>();
//
//        for (int j = 0; j < rxnSplitter.length; j++) {
//            String string = rxnSplitter[j];
//            strings.add(string);
//            List<Integer> capitalIndex = indexProducer(string).capitalsIndex;
//            List<String> elementwNumList = elementwNumParser(string, capitalIndex);
//
//
//        }
//        for (int i=0; i<uniqueElements.size(); i++) {
//            elementMap.put(i, uniqueElements.get(i));
//        }
//
//        for (int j = 0; j < prodSplitter.length; j++) {
//            String string = prodSplitter[j];
//            List<Integer> capitalIndex = indexProducer(string).capitalsIndex;
//            List<String> elementwNumList = elementwNumParser(string, capitalIndex);
//            for (int i = 0; i < elementwNumList.size(); i++) {
//                String element = elementAndNumbers(elementwNumList.get(i), indexProducer(elementwNumList.get(i))).elements;
//                int elementNum = elementAndNumbers(elementwNumList.get(i), indexProducer(elementwNumList.get(i))).elementNum;
//                List<Integer> elementNumMolNum = new ArrayList<>();
//                elementNumMolNum.add(j, elementNum);
//                prodElementsList.add(element);
//                prodElementNum.add(elementNum);
//                prodMoleculePlacement.add(j);
//                if (uniqueElements.indexOf(element) ==0)
//                    uniqueElements.add(element);
//            }
//        }
//        ColossalMap holder = new ColossalMap(rxnElementsList, prodElementsList, rxnElementNum,
//                prodElementNum, rxnMoleculePlacement, prodMoleculePlacement, elementMap);
//        return holder;
}