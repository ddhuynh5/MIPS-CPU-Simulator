public class MIPSView {
    public void displayRegisters(int[] registers) {
        System.out.println("Registers:");
        for (int i = 0; i < registers.length; i++) {
            System.out.printf("$%02d: %08x%n", i, registers[i]);
        }
        System.out.println();
    }
    
    public void displayMemory(int[] memory) {
        System.out.println("Memory:");
        for (int i = 0; i < memory.length; i += 4) {
            System.out.printf("%08x: %08x%n", i, memory[i]);
        }
        System.out.println();
    }
    
    public void displayPc(int pc) {
        System.out.println("PC: " + pc);
    }

    public void displayCycles(int cycles) {
        System.out.println("Cycles: " + cycles);
    }
    
    public void displayHalted() {
        System.out.println("Simulation halted.");
    }

    public void displayCounts(String type, int counts) {
        System.out.println(type + " Counts: " + counts);
    }
}
