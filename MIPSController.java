public class MIPSController {
    private MIPSModel model;
    private MIPSView view;

    public MIPSController(MIPSModel model, MIPSView view) {
        this.model = model;
        this.view = view;
    }

    public void view() {
        view.displayRegisters(model.getRegisters());
        view.displayMemory(model.getMemory());
        view.displayPc(model.getPc());
        view.displayCycles(model.getCycles());
    }
    
    public void step() {
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
            step();
        }
        view.displayHalted();
    }
}
