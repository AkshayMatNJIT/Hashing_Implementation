//Name: Akshay Mutha

import java.io.*;
import java.util.*;

public class Hashing {
    static int[] HashTable = {};
    static char[] CharTable = {};
    static int[] tempHashTable = {};
    static char[] tempCharTable = {};
    static char[] reBuildTempCharTable= {};
    static int baseIndex = 1;
    static int flag = 0;

    public static void main(String[] args) {
        try {

            if(args.length!=1) {
                System.out.println("File name entered is not correct, please enter a correct file name");
                return;
            }

            File file = new File(args[0]);
            //File file = new File("");
            Scanner countOfFile = new Scanner(file);
            Scanner fileData = new Scanner(file);
            int totalOperations = 0;
            while(countOfFile.hasNextLine()) {
                countOfFile.nextLine();
                totalOperations += 1;
            }
            String [] operations = new String[totalOperations];
            String [] words = new String[totalOperations];
            int operationIncr = 0;
            int wordIncr = 0;
            String[] part = new String[2];

            while(fileData.hasNext()) {
                String data = fileData.nextLine();
                part = data.split(" ", 2);

                if(part[0].equals("14")) {
                    boolean val = true;
                    int z = 0;
                    try {
                        z = Integer.parseInt(part[1]);
                    } catch(Exception e) {
                        val = false;
                        System.out.println(e);
                    }
                    if(val == true) {
                        if(z>0) {
                            operations[operationIncr] = part[0];
                            words[wordIncr] = part[1];
                            operationIncr = operationIncr + 1;
                            wordIncr = wordIncr+1;
                        }
                        else {
                            System.out.println("Table size should be greater than 0");
                        }
                    }else {
                        System.out.println("Table size should be an integer");
                    }
                }
                else if(part[0].equals("11") || part[0].equals("12") || part[0].equals("10") || part[0].equals("15")) {
                    operations[operationIncr] = part[0];
                    words[wordIncr] = part[1];
                    operationIncr = operationIncr + 1;
                    wordIncr = wordIncr+1;
                }
                else if(part[0].equals("13")) {
                    operations[operationIncr] = part[0];
                    words[wordIncr] = null;
                    operationIncr = operationIncr + 1;
                    wordIncr = wordIncr+1;
                }else {
                    System.out.println("Not a valid command");
                }
            }

            try {
                for(int i =0;i<operations.length;i++) {
                    if(operations[i]==null) {
                        System.out.println("");
                    }
                    else if(operations[i].equals("14")) {
                        for(int j=i;j<operations.length;j++) {
                            if(operations[j]==null) {
                                System.out.println("Please enter a valid operation");
                            } else if(operations[j].equals("10")) {
                                insertData(words[j]);
                            }
                            else if(operations[j].equals("11")) {
                                toDelete(words[j]);
                            }
                            else if(operations[j].equals("12")) {
                                searchName(words[j]);
                            }
                            else if(operations[j].equals("13")) {
                                printArray();
                            }
                            else if(operations[j].equals("14") && flag == 0) {
                                flag =1;
                                buildStructure(words[j]);
                            }
                            else if(operations[j].equals("15")) {
                                System.out.println("Comment ignored");
                            }else {
                                if(flag==1) {
                                    System.out.println("Duplicate table creation command");
                                }else {
                                    System.out.println("Not a valid operation");
                                }
                            }
                        }
                        break;
                    }
                    else {
                        System.out.println("Table not created");
                    }
                }

            }catch (Exception e) {
                System.out.println(e);
            }
        }catch(Exception e) {
            System.out.println(e);
        }
    }

    public static void buildStructure(String limit) {
        HashTable = new int[Integer.parseInt(limit)];
        CharTable = new char[15* Integer.parseInt(limit)];

        for(int i = 0 ; i<CharTable.length;i++) {
            CharTable[i] = ' ';	//Initialise char table with empty spaces
        }
        for(int j = 0; j<HashTable.length;j++) {
            HashTable[j] = -1;	//Initialise hashtable with -1
        }
    }

    // First call to insert function
    public static void insertData(String personName) {
        insertWord(personName);
        insertKey(personName);
    }

    // Function for the insert operation
    public static void insertWord(String name) {
        if(isNoCollision(name)==false) {
            reSize();
        }

        if(isNoCollision(name)==true) {
            for(int i=0;i<CharTable.length;i++) {
                if((int) CharTable[i] == 32) {
                    baseIndex = i;
                    break;
                }
            }
            int tempIndex = 0;
            for(int j= baseIndex;j<(baseIndex + name.length());j++) {
                if(j>=CharTable.length) {
                    reBuildChar(name);
                }
                CharTable[j] = name.charAt(tempIndex);
                tempIndex = tempIndex+1;
            }
            if((baseIndex+name.length())>=CharTable.length) {
                reBuildChar(name);
            }
            CharTable[(baseIndex + name.length())]='\0';
        }
    }

    //Function to insert the key into integer array
    public static void insertKey(String fullName) {
        int hashSum = 0;
        int function = 0;
        int hashFunction = 0;

        for(int i=0;i<fullName.length();i++) {
            hashSum = hashSum+ ((int)fullName.charAt(i));
        }
        function = (hashSum-2)% HashTable.length;
        for(int j =0; j<HashTable.length;j++) {
            hashFunction =(function +(j*j))% HashTable.length;
            if(HashTable[hashFunction]==-1) {
                HashTable[hashFunction]= baseIndex;
                break;
            }
        }
    }

    // Function to print the array
    public static void printArray() {
        System.out.print("T:				A:");
        for(int i=0;i<CharTable.length;i++) {
            if((int)CharTable[i] == 32) {
                break;
            }
            if((int) CharTable[i] == 0) {
                System.out.print("\\");
            } else {
                System.out.print(CharTable[i]);
            }
        }
        System.out.println();
        for(int i =0; i < HashTable.length;i++) {
            if(HashTable[i] != -1) {
                System.out.println(i + " : " +HashTable[i]);
            }else {
                System.out.println(i + " : ");
            }
        }
        int occupiedSlots = 0;
        for(int i =0; i<HashTable.length;i++) {
            if(HashTable[i] != -1) {
                occupiedSlots = occupiedSlots+1;
            }
        }
        System.out.println();

        int count = 0;
        for(int i=0;i<CharTable.length;i++) {
            count = count +1;
            if((int)CharTable[i] ==32) {
                break;
            }
        }
    }

    //Function for delete operation
    public static void toDelete(String delete) {
        int deleteHashSum = 0;
        int deleteFunction;
        int deleteNumCount;
        int deleteHashFunction = 0;
        int iterate =-2;
        for(int i=0;i<delete.length();i++) {
            deleteHashSum = deleteHashSum + ((int) delete.charAt(i));
        }
        deleteFunction = (deleteHashSum-2) % HashTable.length;
        for(int j=0;j<HashTable.length;j++) {
            deleteNumCount = 0;
            deleteHashFunction = (deleteFunction +(j*j)) % HashTable.length;
            if(HashTable[deleteHashFunction]!= -1) {
                for(int k =0; k<delete.length();k++) {
                    if(delete.charAt(k) == CharTable[k + HashTable[deleteFunction]]){
                        deleteNumCount = deleteNumCount+1;
                    }else {
                        break;
                    }
                }
                if(deleteNumCount == delete.length()) {
                    iterate = HashTable[deleteHashFunction];
                    break;
                }
            }
        }
        if(iterate == -2) {
            System.out.println(delete + " Element not found, please reenter!");
        }else {
            while((int) CharTable[iterate] != 0) {
                CharTable[iterate]= '*';
                iterate = iterate+1;
            }
            HashTable[deleteHashFunction] = -1;
            System.out.println(delete + " deleted from slot " +deleteHashFunction);
        }
    }

    // Function for the search operation
    public static void searchName(String toSearch) {
        int found = -2;
        int searchHashSum =0;
        int searchFunction =0;
        int searchHashFunction =0;
        int numCount = 0;
        for(int i=0;i<toSearch.length();i++) {
            searchHashSum = searchHashSum +((int)toSearch.charAt(i));
        }
        searchFunction = (searchHashSum-2)%HashTable.length;
        for(int j =0; j<HashTable.length;j++) {
            numCount=0;
            searchHashFunction =(searchFunction+(j*j)) % HashTable.length;
            if(searchFunction<0) {
                break;
            }
            if(HashTable[searchHashFunction]!=-1) {
                for(int k=0;k<toSearch.length();k++) {
                    if(toSearch.charAt(k) == CharTable[k+HashTable[searchHashFunction]]) {
                        numCount = numCount+1;
                    } else {
                        break;
                    }
                }
                if(numCount == toSearch.length()) {
                    found = HashTable[searchFunction];
                    break;
                }
            }
        }
        if(found == -2) {
            System.out.println(toSearch + "  not found!!");
        } else {
            System.out.println(toSearch + "  found at slot: " + searchHashFunction);
        }
    }

    public static boolean isNoCollision(String fullName) {
        int hashSum = 0;
        int function = 0;
        int hashFunction = 0;
        for(int i=0;i<fullName.length();i++) {
            hashSum = hashSum+((int)fullName.charAt(i));
        }
        function = (hashSum-2) % HashTable.length;
        for(int j=0;j<HashTable.length;j++) {
            hashFunction = (function + (j*j)) % HashTable.length;
            if(HashTable[hashFunction] == -1) {
                return true;
            }
        }
        return false;
    }

    public static void reBuildChar(String reNameStr) {
        reBuildTempCharTable = new char[2*CharTable.length];
        for(int i=0;i<reBuildTempCharTable.length;i++) {
            reBuildTempCharTable[i] = ' ';
        }
        for(int i =0;i<CharTable.length;i++) {
            reBuildTempCharTable[i]= CharTable[i];
        }
        CharTable = reBuildTempCharTable;
    }

    public static void reSize() {
        int newIndex = 0;
        int totalSum =0;
        tempHashTable = new int[2* HashTable.length];
        tempCharTable = new char[2* CharTable.length];
        for(int i=0;i<tempCharTable.length;i++) {
            tempCharTable[i] = ' ';
        }
        for(int j=0;j<tempHashTable.length;j++) {
            tempHashTable[j]= -1;
        }
        for(int i=0;i<CharTable.length;i++) {
            tempCharTable[i] = CharTable[i];
        }
        for(int k=0;k<CharTable.length;k++) {
            if((int)CharTable[k] != 42) {
                if((int)CharTable[k] == 0) {
                    if(totalSum !=0) {
                        rePutData(totalSum, tempHashTable.length, newIndex);
                    }
                    newIndex= k+1;
                    totalSum=0;
                    continue;
                }
                totalSum = (totalSum-2) + (int) CharTable[k];
            }
            if((int)CharTable[k] == 32) {
                break;
            }
        }
        HashTable = tempHashTable;
    }

    public static void rePutData(int totalSum, int totalLength, int newIndex) {
        int redoFunction = 0;
        int redoHashFunction = 0;
        redoFunction = (totalSum-2) % totalLength;
        for(int j=0;j<tempHashTable.length;j++) {
            redoHashFunction =(redoFunction +(j*j)) % totalLength;
            if(tempHashTable[redoHashFunction] ==-1) {
                tempHashTable[redoHashFunction] = newIndex;
                break;
            }
        }
    }
}