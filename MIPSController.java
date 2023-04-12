import java.util.Scanner;

public class MIPSController {
    private MIPSModel model;
    private MIPSView view;
    private boolean toStep;

    public MIPSController(MIPSModel model, MIPSView view, boolean stepFlag) {
        this.model = model;
        this.view = view;
        this.toStep = stepFlag;
    }

    public void view() {
        view.displayRegisters(model.getRegisters());
        view.displayMemory(model.getMemory());
        view.displayPc(model.getPc());
        view.displayCycles(model.getCycles());
        view.displayCounts("Add IC", model.getAddIC());
        view.displayCounts("Sub IC", model.getSubIC());
        view.displayCounts("And IC", model.getAndIC());
        view.displayCounts("Or IC", model.getOrIC());
        view.displayCounts("Slt IC", model.getSltIC());
        view.displayCounts("AddI IC", model.getAddIIC());
        view.displayCounts("AndI IC", model.getAndIIC());
        view.displayCounts("OrI IC", model.getOrIIC());
        view.displayCounts("SltI IC", model.getSltIIC());
        view.displayCounts("LoadI IC", model.getLoadIIC());
        view.displayCounts("StoreI IC", model.getStoreIIC());
        view.displayCounts("BeqI IC", model.getBeqIIC());
        view.displayCounts("JumpI IC", model.getJumpIIC());
        view.displayCounts("ALU Add Count", model.getAddCount());
        view.displayCounts("ALU Sub Count", model.getSubCount());
        view.displayCounts("ALU And Count", model.getAndCount());
        view.displayCounts("ALU Or Count", model.getOrCount());
        view.displayCounts("ALU Slt Count", model.getSltCount());
        view.displayCounts("Mem Read", model.getMemReadCount());
        view.displayCounts("Mem Write", model.getMemWriteCount());
    }
    
    public void step() {
        if(toStep)
            view();
        // Check if the simulation should be halted
        if (model.getPc() >= model.getInstructionCount() * 4) {
            model.setHalted(true);
            view();
            return;
        }
        
        // Fetch the instruction from memory at the current PC
        int instruction = model.getMemory()[model.getPc() / 4];
        //view();
        
        // Decode the instruction
        int opcode = model.decodeInstruction(instruction);
        //view();

        // execute the corresponding operation
        int result[] = model.executeInstruction(instruction, opcode);
        //view();

        //if opcode is branch / jump and address is not negative
        if ((opcode == 0x04 || opcode == 0x02) && result[1] >= 0) {
            model.setPc(result[0]);
            //view();
            return;
        }
        
        // If opcode is store / load
        // mem access
        if (opcode == 0x2B || opcode == 0x23) {
            if (opcode == 0x23)
                model.writeMem(result[0] , result[2]);
            else
                model.writeMem(result[0], result[1]);
            //view();
        }

        // If opcode is load / rtype / itype then writeback
        if (opcode == 0x23 || opcode == 0x00 || opcode == 0x08 || opcode == 0x0C || opcode == 0x0D || opcode == 0x0A || opcode == 0x2B || opcode == 0x04) {
            if (opcode == 0x23)
                model.writeReg(result[1] , result[2]);
            else
                model.writeReg(result[0], result[1]);
            //view();
        }

        // Update the PC to point to the next instruction
        model.setPc(model.getPc() + 4);
    }
    
    public void run() {
        while (!model.isHalted()) {
            if(toStep){
                Scanner scanner = new Scanner(System.in);
                System.out.println("Press Enter to continue...");
                scanner.nextLine(); // Wait for Enter key press
            }
            step();
        }
        view.displayHalted();
    }
}
