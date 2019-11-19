import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    public static int PC = 64;
    public static Map<Object, ArrayList<String>> map = new TreeMap<>();
    public static int[] reg = {0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public static int cycle = -1;
    public static int firstDataAddress = -1;
    //public static int location = -1;

    public static void main(String[] args) {
        ArrayList<String> instructions = readInput();
        //print(instructions);
        //write(instructions);
        //map = sortMap(map);
        //printMap();
        ArrayList<String> simulation = simulation();
        //printCycle();

    }

    ////////////////////////////  INPUT  ////////////////////////////////////////////////////////
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
                if (line .equals(DUMMY)) {
                    stupidCAP = true;
                    firstDataAddress = PC+4; // next spot
                }

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
        final String instr_string = " X" + src1 + ", " + "#" + offsetStr;
        map.put(PC, new ArrayList<String>(){{ add(command); add(instr_string); add("1"); add(src1); add(offsetStr); }});
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
        final String instr_string =  " X" + dest + ", X" + src1 + ", #" + immVal;
        map.put(PC, new ArrayList<String>(){{ add(command); add(instr_string); add("2"); add(dest); add(src1); add(immVal); }});
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
        final String instr_string = " X" + dest + ", X" + src1 + ", X" + src2;
        map.put(PC, new ArrayList<String>(){{ add(command); add(instr_string); add("3"); add(dest); add(src1); add(src2); }});
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
        final String instr_string = " X" + dest + ", [X" + src1 + ", #" + immVal + "]";
        map.put(PC, new ArrayList<String>(){{ add(command); add(instr_string); add("4"); add(dest); add(src1); add(immVal); }});
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
    public static ArrayList<String> simulation(){
        cycle = 0;
        for (int i = 64; i < 64+map.size()*4; i += 4){   //64+map.size()*4
            System.out.print(i + ": ");
            System.out.println(map.get(i).get(0));
            if (map.get(i).get(0) .equals("DUMMY"))
                break;
            //location = i;

            if (map.get(i).get(2) .equals("1")) {
                // LOCATION IS CHANGED HERE
                i = runCat1(i, map.get(i));      // returns new location -4 if branching to account for the loop
            }
            else if (map.get(i).get(2) .equals("2")){}
                //runCat2();
            else if (map.get(i).get(2) .equals("3")){}
                //runCat3();
            else if (map.get(i).get(2) .equals("4")){}
                //runCat4();
        }

        return new ArrayList<String>(){{add("HELLO WORLD");}};
    }

    public static int runCat1(int loc, ArrayList<String> list){
        // [0-5] = [command, instr_string, "1", src1, offsetStr]
        String command = list.get(0);
        String src1 = list.get(3);
        int offset = Integer.parseInt(list.get(4)); // in decimal
        boolean branch = false;
        int newLocation = loc; // initialize

        if (command .equals("CBZ")) {
            branch = CBZ(src1);
        }
        else if (command .equals("CBNZ")) {
            branch = CBNZ(src1);
        }

        if (branch) {
            // left shift (x4), then add to current address
            // -4 accounts for the simulation for loop incrementing to the next spot in memory
            newLocation = -4 +loc + offset*4;
        }
        return newLocation;
    }

    public static boolean CBZ(String src1){
        int index = Integer.parseInt(src1);
        if (reg[index] == 0)
            return true;
        else
            return false;
    }

    public static boolean CBNZ(String src1){
        int index = Integer.parseInt(src1);
        if (reg[index] != 0)
            return true;
        else
            return false;
    }

    ///////////////////////////////// HELPER FUNCTIONS ////////////////////////////////////////////
    public static void print(ArrayList<String> instructions){
        for (String s : instructions)
            System.out.println(s);
    }

    public static void printMap(){
        for (Map.Entry<Object, ArrayList<String>> m : map.entrySet()){
            System.out.print(m.getKey()+": ");
            for (String s : m.getValue()) {
                System.out.print(s + " ");
            }
            System.out.println();
        }
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

//    public static String decToTwosComp(int num){
//        if (num >= 0)
//            return Integer.toBinaryString(num);
//
//        String binary = Integer.toBinaryString(num);
//        String sign = binary.substring(0,1);
//        binary = binary.substring(1);
//        String onesComp = sign;
//        for (int i = 0; i < binary.length(); i++){
//            if (binary.charAt(i) == '1')
//                onesComp += '0';
//            else
//                onesComp += '1';
//        }
//        String twosComp = Integer.toBinaryString(toDec(onesComp) + 1);
//        return twosComp;
//    }

    // If the value for the register is 31, this function will return the "ZR" string instead of "31"
    public static String checkXZR(String num){
        if (num .equals("31") || num.equals("11111"))
            return "ZR";
        else
            return num;
    }

    public static void printCycle(){
        String instr_string = "0";

        //int firstDataAddress = 124;


        System.out.println("--------------------");
        System.out.println("Cycle " + cycle + ":" + '\t' + "temp" + '\t' + instr_string);
        System.out.println();
        System.out.println("Registers");
        System.out.println("X00:" + '\t' + reg[0] + '\t' + reg[1] + '\t' + reg[2] + '\t' + reg[3] + '\t' + reg[4] +
                '\t' + reg[5] + '\t' + reg[6] + '\t' + reg[7]);
        System.out.println("X08:" + '\t' + reg[8] + '\t' + reg[9] + '\t' + reg[10] + '\t' + reg[11] + '\t' + reg[12] +
                '\t' + reg[13] + '\t' + reg[14] + '\t' + reg[15]);
        System.out.println("X16:" + '\t' + reg[16] + '\t' + reg[17] + '\t' + reg[18] + '\t' + reg[19] + '\t' + reg[20] +
                '\t' + reg[21] + '\t' + reg[22] + '\t' + reg[23]);
        System.out.println("X24:" + '\t' + reg[24] + '\t' + reg[25] + '\t' + reg[26] + '\t' + reg[27] + '\t' + reg[28] +
                '\t' + reg[29] + '\t' + reg[30] + '\t' + reg[31]);
        System.out.println();

        System.out.println("Data");
        int counter = 1;
        int dataAddress = firstDataAddress;
        for (int i = firstDataAddress; i < 64+(27)*4; i+=4){
            if ( (counter%8) == 1){
                System.out.print(dataAddress + ":");
                dataAddress += (8*4);
            }
            System.out.print("\t" + map.get(i).get(0));

            if ((counter%8) == 0) {
                System.out.println();
                counter = 1;
            }
            else {
                counter++;
            }
        }
        System.out.println();
    }

}