import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class TestJava {
    public static void main(String[] args) {

        TaskDuplicates taskDuplicates = new TaskDuplicates();

        try {
            taskDuplicates.doTask();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        TaskWriter taskWriter = new TaskWriter();
//        taskWriter.writerSymbols();

    }

}

class TaskDuplicates {

    private final HashMap<String, Integer> allWords = new HashMap<>();
    private final Set<String> upperList = new LinkedHashSet<>();

    public ArrayList<String> strings = new ArrayList<>();
    public ArrayList<Integer> ints = new ArrayList<>();
    Pattern pattern = Pattern.compile("[A-Z]+[a-z]");

    void doTask() throws FileNotFoundException {

        Scanner in = new Scanner(new FileReader("src/harry.txt"));

        if (in.hasNextLine()) {
            readAndCalculate(in.nextLine());
        }

        while (in.hasNextLine()) {

            in.nextLine();
            readAndCalculate(in.nextLine());

        }

        in.close();

        for (HashMap.Entry<String, Integer> e : allWords.entrySet()) {
            float value = e.getValue();
            boolean isAdded = false;
            for (int i = 0; i < ints.size(); i++) {
                if (value > ints.get(i)) {
                    ints.add(i, (int) value);
                    strings.add(i, e.getKey());
                    isAdded = true;
                    break;
                }
            }
            if (!isAdded) {
                ints.add((int) value);
                strings.add(e.getKey());
            }
        }



//      allWords.entrySet().stream()
//              .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
//              .limit(20)
//              .forEach(System.out::println);
        List<String> sortedUpperList = new ArrayList<>(upperList);
        Collections.sort(sortedUpperList);

        TaskWriter taskWriter = new TaskWriter();
        taskWriter.writerSymbols(sortedUpperList, strings, ints);

        System.out.println("Count of names:" + upperList.size());
    }

    void readAndCalculate(String s) {
        boolean isNewSentence = true;
        String[] words = s.split("[^a-zA-Z']| ");
        String[] wordWithSymbols = s.split(" ");
        boolean isOpenSymbol = false;

        words = Arrays.stream(words).filter(str -> !str.equals("")).toArray(String[]::new);

        for (String k : wordWithSymbols) {

            if (k.charAt(0) == '"') isOpenSymbol = true;

            if (pattern.matcher(k).find() && !(isNewSentence || (k.charAt(0) == '"' && isOpenSymbol))) {
                upperList.add(k.replaceAll("[^a-zA-Z]", ""));
                isOpenSymbol = false;
            } else if (isNewSentence) {
                isNewSentence = false;
            }

            if (k.charAt(k.length() - 1) == '.' || k.charAt(k.length() - 1) == '!' || k.charAt(k.length() - 1) == '?') {
                isNewSentence = true;
            }
        }

//        for (int i = 0; i < words.length; i++) {
//
//            words[i] = words[i].replaceAll("[^a-zA-Z ]", "");
//        }
        for (String word : words) {
            if (allWords.containsKey(word.toLowerCase(Locale.ROOT))) {
                int increm = allWords.get(word.toLowerCase(Locale.ROOT));
                increm++;

                allWords.put(word.toLowerCase(Locale.ROOT), increm);
            } else {
                allWords.put(word.toLowerCase(Locale.ROOT), 1);
            }
        }
    }

}

class TaskWriter {

    void writerSymbols(List<String> sortedUpperList, ArrayList<String> strings, ArrayList<Integer> ints) {
        try (FileWriter writer = new FileWriter("test.txt", false)) {

            String header = """
                    \t\t\t\t\t\tTHIS FILE CONTAINS PROPER NAMES, THEIR QUANTITY AND FIRST 20 PAIRS FROM THE FILE 'harry.txt'
                    Author: Boliak Mykyta
                    """;

            writer.write(header);
            writer.write("_____________20 PAIRS_____________\n");
            for(int i = 0; i < 20; i++){
                writer.write(strings.get(i) + ":" + ints.get(i) + "\n");
            }
            writer.write("_____________Proper names_____________\n");



            int i = 1;
            for (String str : sortedUpperList) {
                writer.write(i + " - " + str + '\n');
                i++;
            }
            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

}
