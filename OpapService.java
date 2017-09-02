package com.company;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import org.apache.commons.math3.stat.Frequency;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//testing if gitkraken works as planned
public class Main {

    public static void main(String[] args) throws Exception {

        
        int drawno = 631486;//need to change this so it represents the day that the user run's it
        int sizeDraw = lastDrawByNo() - drawno;
        ArrayList<int[]> arl = new ArrayList<int[]>();

        System.out.println("Generating Statistics For The Day, Please Hold..............\n");
        for (int i = 0; i < sizeDraw; i++) {
            drawno++;

            arl.add(CurrentDraw("" + drawno));
        }

        statistics(arl);

    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }

    }

    private static int[] CurrentDraw(String draw) throws Exception {

        String json = readUrl("http://applications.opap.gr/DrawsRestServices/kino/" + draw + ".json\n" + "\n" + "");
        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(json);
        int numberArray[] = new int[20];

        if (jsonTree.isJsonObject()) {
            JsonObject jsonObject = jsonTree.getAsJsonObject();
            JsonElement f2 = jsonObject.get("draw");

            if (f2.isJsonObject()) {
                JsonObject f2Obj = f2.getAsJsonObject();
                JsonArray f3 = (JsonArray) f2Obj.get("results");

                for (int i = 0; i < 20; i++) {
                    numberArray[i] = f3.get(i).getAsInt();
                }

            }
        }

        return numberArray;
    }

    private static int lastDrawByNo() throws Exception {

        // String DrawNo = null;
        String json = readUrl("http://applications.opap.gr/DrawsRestServices/kino/last.json");
        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(json);
        int returnNum = 0;
        if (jsonTree.isJsonObject()) {
            JsonObject jsonObject = jsonTree.getAsJsonObject();
            JsonElement f2 = jsonObject.get("draw");

            if (f2.isJsonObject()) {
                JsonObject f2Obj = f2.getAsJsonObject();
                JsonElement f4 = f2Obj.get("drawTime");
                returnNum = f2Obj.get("drawNo").getAsInt();
                JsonElement f3 = f2Obj.get("results");
                System.out.println("Draw Number " + returnNum + "\n");
                System.out.println("Draw Time " + f4 + "\n");
                System.out.println("Draw Results " + f3 + "\n");

            }
        }

        return returnNum;
    }

    static int[] sortArray(int[] numArray) {

        int n = numArray.length;
        int temp = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {

                if (numArray[j - 1] > numArray[j]) {
                    temp = numArray[j - 1];
                    numArray[j - 1] = numArray[j];
                    numArray[j] = temp;
                }

            }
        }
        return numArray;

    }

    private static void statistics(ArrayList<int[]> arl) {

        Frequency f = new Frequency();
        int g=0;
        for (int[] i : arl) {

            for (int k = 0; k < 20; k++) {

                f.addValue(arl.get(g)[k]);

            }
            g++;
        }


        for (int j = 1; j <= 80; j++) {
            System.out.println(
                    "Frequency of " + j + " is |" + f.getCount(j) + "| Percentage | " + f.getPct(j) * 100 + " % |");
        }
    }

}
