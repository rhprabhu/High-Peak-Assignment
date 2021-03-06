import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Main {
    static Set<List<Integer>> arrayList = new HashSet<>();
    static Map<Integer, String> nameValueMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Data data = readFileInput();
        getCombinations(data.inputArr, data.inputArr.length, data.m);
        writeOutput();
    }

    private static void writeOutput() throws IOException {
        Map<Integer, List<Integer>> map = new TreeMap<>();
        arrayList.forEach(entry -> {
            int max = entry.stream().mapToInt(v -> v).max().getAsInt();
            int min = entry.stream().mapToInt(v -> v).min().getAsInt();
            map.put(max - min, entry);
        });
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.txt")));
        bufferedWriter.write("The goodies selected for distribution are:\n\n");
        Map.Entry<Integer, List<Integer>> first = map.entrySet().iterator().next();
        first.getValue().stream().forEach(val -> {
            try {
                bufferedWriter.write(nameValueMap.get(val) + " " + val + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bufferedWriter.write("\nAnd the difference between the chosen goodie with highest price and the lowest price is " + first.getKey());
        bufferedWriter.close();
    }

    private static Data readFileInput() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("sample_input.txt")));
        String line = null;
        int m = 0;
        List<Integer> numbers = new ArrayList<>();
        List<String> names = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
            if (line.contains("Number of employees")) {
                m = Integer.parseInt(line.split(":")[1].trim());
            } else if (line.contains("Goodies and Prices")) {
                //do nothing
            } else {
                if (line.contains(":")) {
                    int val = Integer.parseInt(line.split(":")[1].trim());
                    numbers.add(val);
                    String name = line.split(":")[0];
                    names.add(name);
                    nameValueMap.put(val, name);
                }
            }
        }
        Data data = new Data();
        data.m = m;
        data.inputArr = new int[numbers.size()];
        data.itemNames = new String[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) {
            data.inputArr[i] = numbers.get(i);
            data.itemNames[i] = names.get(i);
        }
        return data;
    }

    static void combinationUtil(int arr[], int data[], int start,
                                int end, int index, int r) {
        // Current combination is ready to be printed, print it
        if (index == r) {
            List<Integer> l = new ArrayList<>();
            for (int j = 0; j < r; j++) {
                l.add(data[j]);
            }
            arrayList.add(l);
            return;
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
            data[index] = arr[i];
            combinationUtil(arr, data, i + 1, end, index + 1, r);
        }
    }

    static void getCombinations(int arr[], int n, int r) {
        // A temporary array to store all combination one by one
        int data[] = new int[r];

        // Print all combination using temprary array 'data[]'
        combinationUtil(arr, data, 0, n - 1, 0, r);
    }

    static class Data {
        int[] inputArr;
        String[] itemNames;
        int m;
    }
}
