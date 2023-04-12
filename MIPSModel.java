import java.util.ArrayList;

public class MIPSModel {
    private int[] registers;
    private int[] memory;
    private int pc;
    private int cycles;
    private int instructionCount;
    private int addIC;
    private int subIC;
    private int andIC;
    private int orIC;
    private int sltIC;
    private int addIIC;
    private int andIIC;
    private int orIIC;
    private int sltIIC;
    private int loadIIC;
    private int storeIIC;
    private int beqIIC;
    private int jumpIIC;
    private int addCount;
    private int subCount;
    private int andCount;
    private int orCount;
    private int sltCount;
    private int memReadCount;
    private int memWriteCount;
    private boolean halted;
    
    public MIPSModel() {
        registers = new int[32];
        memory = new int[512];
        halted = false;
        pc = 0;
        cycles = 0;
        instructionCount = 0;
        subCount = 0;
        andCount = 0;
        orCount = 0;
        sltCount = 0;
        memReadCount = 0;
        memWriteCount = 0;
        addIC = 0;
        subIC = 0;
        andIC = 0;
        orIC = 0;
        sltIC = 0;
        addIIC = 0;
        andIIC = 0;
        orIIC = 0;
        sltIIC = 0;
        loadIIC = 0;
        storeIIC = 0;
        beqIIC = 0;
        jumpIIC = 0;
    }

    public int getAddIC() {
        return addIC;
    }

    public int getSubIC() {
        return subIC;
    }

    public int getAndIC() {
        return andIC;
    }

    public int getOrIC() {
        return orIC;
    }

    public int getSltIC() {
        return sltIC;
    }

    public int getAddIIC() {
        return addIIC;
    }

    public int getAndIIC() {
        return andIIC;
    }

    public int getOrIIC() {
        return orIIC;
    }

    public int getSltIIC() {
        return sltIIC;
    }

    public int getLoadIIC() {
        return loadIIC;
    }

    public int getStoreIIC() {
        return storeIIC;
    }

    public int getBeqIIC() {
        return beqIIC;
    }

    public int getJumpIIC() {
        return jumpIIC;
    }

    public int getAddCount() {
        return addCount;
    }

    public int getSubCount() {
        return subCount;
    }

    public int getAndCount() {
        return andCount;
    }

    public int getOrCount() {
        return orCount;
    }

    public int getSltCount() {
        return sltCount;
    }

    public int getMemReadCount() {
        return memReadCount;
    }

    public int getMemWriteCount() {
        return memWriteCount;
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
        sltCount++;
        subCount++;
        addCount++;
        return (registers[rs] - registers[rt]) < 0 ? 1 : 0;
    }

    public int executeAddReg(int rs, int rt) {
        cycles++;
        addCount++;
        return registers[rs] + registers[rt];
    }

    public int executeSubReg(int rs, int rt) {
        cycles++;
        subCount++;
        addCount++;
       return registers[rs] - registers[rt];
    }

    public int executeAndReg(int rs, int rt) {
        cycles++;
        andCount++;
        return registers[rs] & registers[rt];
    }

    public int executeOrReg(int rs, int rt) {
        cycles++;
        orCount++;
        return registers[rs] | registers[rt];
    }

    public int executeAddImm(int rs, int imm) {
        cycles++;
        addCount++;
        return registers[rs] + imm;
    }

    public void writeMem(int address, int value) {
        cycles++;
        memWriteCount++;
        memory[address] = value;
    }

    public void writeReg(int reg, int value) {
        cycles++;
        registers[reg] = value;
    }

    public int executeStore(int rs, int imm) {
        cycles++;
        addCount++;
        return registers[rs] + imm;
    }

    public int executeAndImm(int rs, int imm) {
        cycles++;
        andCount++;
        return registers[rs] & imm;
    }

    public int executeOrImm(int rs, int imm) {
        cycles++;
        orCount++;
        return registers[rs] | imm;
    }

    public int executeSetLessThanImm(int rs, int imm) {
        cycles++;
        sltCount++;
        subCount++;
        addCount++;
        return (registers[rs] - imm) < 0 ? 1 : 0;
    }

    public int executeBeq(int rs, int rt, int imm) {
        cycles++;
        addCount++;
        if (rs == rt)
            return pc + imm * 4;
        else
            return pc + 4;
    }

    public int executeLoadWord(int rs, int rt, int imm) {
        cycles++;
        addCount++;
        memReadCount++;
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
                        addIC++;
                        return new int[] {rd, executeAddReg(rs, rt)};
                    case 0x22:  // SUB
                        subIC++;
                        return new int[] {rd, executeSubReg(rs, rt)};
                    case 0x24:  // AND
                        andIC++;
                        return new int[] {rd, executeAndReg(rs, rt)};
                    case 0x25:  // OR
                        orIC++;
                        return new int[] {rd, executeOrReg(rs, rt)};
                    case 0x2A:  // SLT
                        sltIC++;
                        return new int[] {rd, executeSetLessThan(rs, rt)};
                    default:
                        throw new IllegalArgumentException("Unknown R-format instruction: " + Integer.toHexString(instruction));
                }
            case 0x08:  // ADDI
                addIIC++;
                immediate = instruction & 0xFFFF;
                rs = (instruction >> 21) & 0x1F;
                rt = (instruction >> 16) & 0x1F;
                return new int[] {rt, executeAddImm(rs, immediate)};
            case 0x0C:  // ANDI
                andIIC++;
                immediate = instruction & 0xFFFF;
                rs = (instruction >> 21) & 0x1F;
                rt = (instruction >> 16) & 0x1F;
                return new int[] {rt, executeAndImm(rs, immediate)};
            case 0x0D:  // ORI
                orIIC++;
                immediate = instruction & 0xFFFF;
                rs = (instruction >> 21) & 0x1F;
                rt = (instruction >> 16) & 0x1F;
                return new int[] {rt, executeOrImm(rs, immediate)};
            case 0x0A:  // SLTI
                sltIIC++;
                immediate = instruction & 0xFFFF;
                rs = (instruction >> 21) & 0x1F;
                rt = (instruction >> 16) & 0x1F;
                return new int[] {rt, executeSetLessThanImm(rs, immediate)};
            case 0x23:  // LOAD [add, reg, val]
                loadIIC++;
                immediate = instruction & 0xFFFF;
                rs = (instruction >> 21) & 0x1F;
                rt = (instruction >> 16) & 0x1F;
                return new int[] {registers[rs] + immediate, rt, executeLoadWord(rs, rt, immediate)};
            case 0x2B:  // STORE
                storeIIC++;
                immediate = instruction & 0xFFFF;
                rs = (instruction >> 21) & 0x1F;
                rt = (instruction >> 16) & 0x1F;
                return new int[] {rt, executeStore(rs, immediate)};
            case 0x04:  // BEQ
                beqIIC++;
                immediate = instruction & 0xFFFF;
                rs = (instruction >> 21) & 0x1F;
                rt = (instruction >> 16) & 0x1F;
                return new int[] {executeBeq(rs, rt, immediate), 0};
            case 0x02:  // JUMP is a J type not
                jumpIIC++; 
                immediate = (instruction & 0x03FFFFFF) << 2;
                cycles++;
                return new int[] {immediate, 0};
            default:
                throw new IllegalArgumentException("Unknown instruction: " + Integer.toHexString(instruction));
        }
    }
}
