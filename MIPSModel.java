import java.util.ArrayList;

public class MIPSModel {
    private int[] registers;
    private int[] memory;
    private int pc;
    private int cycles;
    private int instructionCount;
    private boolean halted;
    
    public MIPSModel() {
        registers = new int[32];
        memory = new int[512];
        pc = 0;
        halted = false;
        cycles = 0;
        instructionCount = 0;
    }
    
    public int[] getRegisters() {
        return registers;
    }

    public void setRegisters(int[] registers) {
        this.registers = registers;
    }

    public int getInstructionCount() {
        return instructionCount;
    }

    public void setInstructionCount(int ic) {
        this.instructionCount = ic;
    }
    
    public int[] getMemory() {
        return memory;
    }

    public void setMemory(ArrayList<Integer> arr) {
        for(int i=0; i<arr.size(); i++){
            memory[i] = arr.get(i);
        }
        setInstructionCount(arr.size());
    }
    
    public int getPc() {
        return pc;
    }
    
    public void setPc(int pc) {
        this.pc = pc;
    }
    
    public boolean isHalted() {
        return halted;
    }
    
    public void setHalted(boolean halted) {
        this.halted = halted;
    }

    public int getCycles() {
        return cycles;
    }

    public void setCycles(int c) {
        this.cycles = c;
    }

    public int executeSetLessThan(int rs, int rt) {
        cycles++;
        return (registers[rs] - registers[rt]) < 0 ? 1 : 0;
    }

    public int executeAddReg(int rs, int rt) {
        cycles++;
        return registers[rs] + registers[rt];
    }

    public int executeSubReg(int rs, int rt) {
        cycles++;
       return registers[rs] - registers[rt];
    }

    public int executeAndReg(int rs, int rt) {
        cycles++;
        return registers[rs] & registers[rt];
    }

    public int executeOrReg(int rs, int rt) {
        cycles++;
        return registers[rs] | registers[rt];
    }

    public int executeAddImm(int rs, int imm) {
        cycles++;
        return registers[rs] + imm;
    }

    public void writeMem(int address, int value) {
        cycles++;
        memory[address] = value;
    }

    public void writeReg(int reg, int value) {
        cycles++;
        registers[reg] = value;
    }

    public int executeStore(int rs, int imm) {
        cycles++;
        return registers[rs] + imm;
    }

    public int executeAndImm(int rs, int imm) {
        cycles++;
        return registers[rs] & imm;
    }

    public int executeOrImm(int rs, int imm) {
        cycles++;
        return registers[rs] | imm;
    }

    public int executeSetLessThanImm(int rs, int imm) {
        cycles++;
        return (registers[rs] - imm) < 0 ? 1 : 0;
    }

    public int executeBeq(int rs, int rt, int imm) {
        cycles++;
        if (rs == rt)
            return pc + imm * 4;
        else
            return pc + 4;
    }

    public int executeLoadWord(int rs, int rt, int imm) {
        cycles++;
        return memory[registers[rs]+imm];
    }

    public int decodeInstruction(int instruction) {
        cycles++;
        return (instruction >> 26) & 0x3F;
    }

    public int[] executeInstruction(int instruction, int opcode) {
        cycles++;
        int immediate;
        int rs;
        int rt;
        int rd;

        switch (opcode) {
            case 0x00:  // R-format instruction
                int function = instruction & 0x3F;
                rs = (instruction >> 21) & 0x1F;
                rt = (instruction >> 16) & 0x1F;
                rd = (instruction >> 11) & 0x1F;
                switch (function) {
                    case 0x20:  // ADD
                        return new int[] {rd, executeAddReg(rs, rt)};
                    case 0x22:  // SUB
                        return new int[] {rd, executeSubReg(rs, rt)};
                    case 0x24:  // AND
                        return new int[] {rd, executeAndReg(rs, rt)};
                    case 0x25:  // OR
                        return new int[] {rd, executeOrReg(rs, rt)};
                    case 0x2A:  // SLT
                        return new int[] {rd, executeSetLessThan(rs, rt)};
                    default:
                        throw new IllegalArgumentException("Unknown R-format instruction: " + Integer.toHexString(instruction));
                }
            case 0x08:  // ADDI
                immediate = instruction & 0xFFFF;
                rs = (instruction >> 21) & 0x1F;
                rt = (instruction >> 16) & 0x1F;
                return new int[] {rt, executeAddImm(rs, immediate)};
            case 0x0C:  // ANDI
                immediate = instruction & 0xFFFF;
                rs = (instruction >> 21) & 0x1F;
                rt = (instruction >> 16) & 0x1F;
                return new int[] {rt, executeAndImm(rs, immediate)};
            case 0x0D:  // ORI
                immediate = instruction & 0xFFFF;
                rs = (instruction >> 21) & 0x1F;
                rt = (instruction >> 16) & 0x1F;
                return new int[] {rt, executeOrImm(rs, immediate)};
            case 0x0A:  // SLTI
                immediate = instruction & 0xFFFF;
                rs = (instruction >> 21) & 0x1F;
                rt = (instruction >> 16) & 0x1F;
                return new int[] {rt, executeSetLessThanImm(rs, immediate)};
            case 0x23:  // LOAD [add, reg, val]
                immediate = instruction & 0xFFFF;
                rs = (instruction >> 21) & 0x1F;
                rt = (instruction >> 16) & 0x1F;
                return new int[] {registers[rs] + immediate, rt, executeLoadWord(rs, rt, immediate)};
            case 0x2B:  // STORE
                immediate = instruction & 0xFFFF;
                rs = (instruction >> 21) & 0x1F;
                rt = (instruction >> 16) & 0x1F;
                return new int[] {rt, executeStore(rs, immediate)};
            case 0x04:  // BEQ
                immediate = instruction & 0xFFFF;
                rs = (instruction >> 21) & 0x1F;
                rt = (instruction >> 16) & 0x1F;
                return new int[] {executeBeq(rs, rt, immediate), 0};
            case 0x02:  // JUMP is a J type not 
                immediate = (instruction & 0x03FFFFFF) << 2;
                cycles++;
                return new int[] {immediate, 0};
            default:
                throw new IllegalArgumentException("Unknown instruction: " + Integer.toHexString(instruction));
        }
    }
}
