import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        MIPSModel model = new MIPSModel();
        MIPSView view = new MIPSView();
        MIPSController controller = new MIPSController(model, view);
        
        ArrayList<Integer> instructions = new ArrayList<>();
        
        try {
            String filename = args[0];
            FileInputStream file = new FileInputStream(filename);
            DataInputStream data = new DataInputStream(file);

            while (data.available() > 0) {
                int instruction = data.readInt();
                instructions.add(instruction);
            }

            data.close();
            file.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        // ArrayList<String> hexInstructions = new ArrayList<>();
        // for (int i = 0; i < instructions.size(); i++) {
        //     String hexInstruction = String.format("%08X", instructions.get(i));
        //     hexInstructions.add(hexInstruction);
        // }

        // print the hexadecimal representation of each instruction to the console
        // for (int i = 0; i < hexInstructions.size(); i++) {
        //     System.out.println(hexInstructions.get(i));
        // }

        // // Load program into memory
        // int[] program = new int[] {
        //     //0x00000000, // add $3, $0, $0
        //     //0x8c000000, // lw $0, 0($0)
        //     0x8c000000
        //     //0x00000000, // add $0, $0, $0
        //     //0x10010001, // beq $0, $1, 1
        //     //0x08000002, // j 2
        //     //0xac000000  // sw $0, 0($0)
        // };

        model.setMemory(instructions);
        
        controller.run();
    }
}
