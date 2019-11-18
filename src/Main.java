import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.*;

public class Main {
    public static int PC = 64;
    public static Map<Object,ArrayList<String>> map = new HashMap<>();

    public static void main(String[] args) {
        ArrayList<String> instructions = readInput();
        //print(instructions);
        //write(instructions);
        //map = sortMap(map);
        printMap();


    }

    public static ArrayList<String> readInput(){
        String line = "";
        String category = "";
        ArrayList<String> instructions = new ArrayList<>();
        final String DUMMY = "10100000000000000000000000000000";
        boolean stupidCAP = false;

        try {
            File file = new File("sample.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                line = sc.nextLine();
                if (line .equals(DUMMY))
                    stupidCAP = true;

                if (stupidCAP) {
                    instructions.add(programData(line));
                }

                else {
                    category = line.substring(0, 3);
                    if (category.equals("001")) {
                        instructions.add(cat1(line));
                    } else if (category.equals("010")) {
                        instructions.add(cat2(line));
                    } else if (category.equals("011")) {
                        instructions.add(cat3(line));
                    } else if (category.equals("100")) {
                        instructions.add(cat4(line));
                    }
                }
                PC += 4;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return instructions;
    }

    public static void print(ArrayList<String> instructions){
        for (String s : instructions)
             System.out.println(s);
    }

    public static void printMap(){
        map = new TreeMap<Object, ArrayList<String>>(map);
        for (Map.Entry<Object, ArrayList<String>> m : map.entrySet()){
            System.out.print(m.getKey()+": ");
            for (String s : m.getValue()) {
                System.out.print(s + " ");
            }
            System.out.println();
        }
    }

    public static Map<Object, ArrayList<String>> sortMap(Map<Object, ArrayList<String>> unsorted){
        return new TreeMap<Object, ArrayList<String>>(map);
    }

    public static void write(ArrayList<String> instructions) {
        try {
            FileWriter fileWriter = new FileWriter("disassembly.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);

            for (String s : instructions)
                printWriter.println(s);

            printWriter.close();
        } catch (IOException e){
            System.out.println(e);
        }
    }

    public static int twosToDec(String input) {
        //Check if the number is negative.
        //We know it's negative if it starts with a 1
        if (input.charAt(0) == '1') {
            String invertedInt = invertDigits(input);
            int dec = Integer.parseInt(invertedInt, 2);
            dec = (dec + 1) * -1;
            return dec;
        } else {
            return Integer.parseInt(input, 2);
        }
    }

    public static String invertDigits(String binaryInt) {
        String result = binaryInt;
        result = result.replace("0", " "); //temp replace 0s
        result = result.replace("1", "0"); //replace 1s with 0s
        result = result.replace(" ", "1"); //put the 1s back in
        return result;
    }

    public static int toDec(String input){
        long bin = Long.parseLong(input);
        int pwr = 0;
        int dec = 0;
        while (bin != 0) {
            long temp = bin % 10;
            dec += temp * Math.pow(2, pwr);
            bin = bin / 10;
            pwr++;
        }
        return dec;
    }

    // If the value for the register is 31, this function will return the "ZR" string instead of "31"
    public static String checkXZR(String num){
        if (num .equals("31") || num.equals("11111"))
            return "ZR";
        else
            return num;
    }

    public static String cat1(String binary){
        String opcode = binary.substring(3,8);
        String src1 = checkXZR(Integer.toString(toDec(binary.substring(8,13))));
        String offsetStr = Integer.toString(twosToDec(binary.substring(14)));
        String instruction = "";

        if (opcode .equals("10000")){
            instruction = "CBZ";
        }
        else if (opcode .equals("10001")){
            instruction = "CBNZ";
        }
        else {
            System.out.println("Error. Opcode not supported");
        }
        String ARM = instruction + " X" + src1 + ", " + "#" + offsetStr;
        final String command = instruction;
        map.put(PC, new ArrayList<String>(){{ add(command); add(src1); add(offsetStr); }});
        //System.out.println(binary + '\t' + PC + '\t' + ARM);
        return binary + '\t' + PC + '\t' + ARM;
    }

    public static String cat2(String binary){
        String opcode = binary.substring(3,10);
        String dest = Integer.toString(toDec(binary.substring(10,15)));
        String src1 = checkXZR(Integer.toString(toDec(binary.substring(15,20))));
        String immVal = Integer.toString(twosToDec(binary.substring(20)));
        String instruction = "";

        if (opcode .equals("1000000"))
            instruction = "ORRI";
        else if (opcode .equals("1000001"))
            instruction = "EORI";
        else if (opcode .equals("1000010"))
            instruction = "ADDI";
        else if (opcode .equals("1000011"))
            instruction = "SUBI";
        else if (opcode .equals("1000100"))
            instruction = "SUBI";
        else
            System.out.println("Error: Opcode not supported");

        String ARM = instruction + " X" + dest + ", X" + src1 + ", #" + immVal;
        final String command = instruction;
        map.put(PC, new ArrayList<String>(){{ add(command); add(dest); add(src1); add(immVal); }});
        //System.out.println(binary + '\t' + PC + '\t' + ARM);
        return binary + '\t' + PC + '\t' + ARM;
    }

    public static String cat3(String binary){
        String opcode = binary.substring(3,11);
        String dest = Integer.toString(toDec(binary.substring(11,16)));
        String src1 = checkXZR(Integer.toString(toDec(binary.substring(16,21))));
        String src2 = checkXZR(Integer.toString(toDec(binary.substring(21,26))));
        String instruction = "";

        if (opcode .equals("10100000"))
            instruction = "EOR";
        else if (opcode .equals("10100010"))
            instruction = "ADD";
        else if (opcode .equals("10100011"))
            instruction = "SUB";
        else if (opcode .equals("10100100"))
            instruction = "AND";
        else if (opcode .equals("10100101"))
            instruction = "ORR";
        else if (opcode .equals("10100110"))
            instruction = "LSR";
        else if (opcode .equals("10100111"))
            instruction = "LSL";
        else
            System.out.println("Error: Opcode not supported");

        String ARM = instruction + " X" + dest + ", X" + src1 + ", X" + src2;
        final String command = instruction;
        map.put(PC, new ArrayList<String>(){{ add(command); add(dest); add(src1); add(src2); }});
        //System.out.println(binary + '\t' + PC + '\t' + ARM);
        return binary + '\t' + PC + '\t' + ARM;
    }

    public static String cat4(String binary){
        String opcode = binary.substring(3,11);
        String dest = Integer.toString(toDec(binary.substring(11,16)));
        String src1 = checkXZR(Integer.toString(toDec(binary.substring(16,21))));
        String immVal = Integer.toString(twosToDec(binary.substring(21)));
        String instruction = "";

        if (opcode .equals("10101010"))
            instruction = "LDUR";
        else if (opcode .equals("10101011"))
            instruction = "STUR";
        else
            System.out.println("ERROR: Opcode not supported");

        String ARM = instruction + " X" + dest + ", [X" + src1 + ", #" + immVal + "]";
        final String command = instruction;
        map.put(PC, new ArrayList<String>(){{ add(command); add(dest); add(src1); add(immVal); }});
        //System.out.println(binary + '\t' + PC + '\t' + ARM);
        return binary + '\t' + PC + '\t' + ARM;
    }

    public static String programData(String binary){
        String value = "";
        if (binary .equals("10100000000000000000000000000000"))
            value = "DUMMY";
        else
            value = Integer.toString(twosToDec(binary));
        //mapInsert("DATA", value);
        final String val = value;
        map.put(PC, new ArrayList<String>(){{ add(val); }});
        return binary + '\t' + PC + '\t' + value;
    }

    ////////////////////////////////// INSTRUCTIONS ////////////////////////////////////////////////

}