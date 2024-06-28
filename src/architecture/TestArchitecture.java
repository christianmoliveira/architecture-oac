package architecture;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import components.Memory;

public class TestArchitecture {
	
	//uncomment the anotation below to run the architecture showing components status
	//@Test
	public void testShowComponentes() {

		//a complete test (for visual purposes only).
		//a single code as follows
//		ldi 2
//		store 40
//		ldi -4
//		point:
//		store 41  //mem[41]=-4 (then -3, -2, -1, 0)
//		read 40
//		add 40    //mem[40] + mem[40]
//		store 40  //result must be in 40
//		read 41
//		inc
//		jn point
//		end
		
		Architecture arch = new Architecture(true);
		arch.getMemory().getDataList()[0]=7;
		arch.getMemory().getDataList()[1]=2;
		arch.getMemory().getDataList()[2]=6;
		arch.getMemory().getDataList()[3]=40;
		arch.getMemory().getDataList()[4]=7;
		arch.getMemory().getDataList()[5]=-4;
		arch.getMemory().getDataList()[6]=6;
		arch.getMemory().getDataList()[7]=41;
		arch.getMemory().getDataList()[8]=5;
		arch.getMemory().getDataList()[9]=40;
		arch.getMemory().getDataList()[10]=0;
		arch.getMemory().getDataList()[11]=40;
		arch.getMemory().getDataList()[12]=6;
		arch.getMemory().getDataList()[13]=40;
		arch.getMemory().getDataList()[14]=5;
		arch.getMemory().getDataList()[15]=41;
		arch.getMemory().getDataList()[16]=8;
		arch.getMemory().getDataList()[17]=4;
		arch.getMemory().getDataList()[18]=6;
		arch.getMemory().getDataList()[19]=-1;
		arch.getMemory().getDataList()[40]=0;
		arch.getMemory().getDataList()[41]=0;
		//now the program and the variables are stored. we can run
		arch.controlUnitEexec();
		
	}

	@Test
	public void testAddRegReg() {
		Architecture arch = new Architecture();

		//making PC points to position 30
		arch.getExtbus1().put(30);
		arch.getPC().store();

		//now setting the registers values
		arch.getExtbus1().put(3);
		arch.getRegistersList().get(0).store(); //RPG0 has 3
		arch.getExtbus1().put(52);
		arch.getRegistersList().get(1).store(); //RPG1 has 52

		//storing the number 1 in the memory, in position 31
		arch.getMemory().getDataList()[31]=1;
		//storing the number 0 in the memory, in position 32
		arch.getMemory().getDataList()[32]=0;

		//executing the command add REG1 REG0.
		arch.addRegReg();

		// --- Checking ---

		//testing if REG0 = 55
		arch.getRegistersList().get(0).read();
		assertEquals(55, arch.getExtbus1().get());

		//the flags bits 0 and 1 must be 0
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(0, arch.getFlags().getBit(1));

		//Testing if PC was pointing to 33
		arch.getPC().read();assertEquals(33, arch.getExtbus1().get());
	}

	@Test
	public void testAddMemReg() {
		Architecture arch = new Architecture();

		//making PC points to position 33
		arch.getExtbus1().put(33);
		arch.getPC().store();

		//storing the number 20 in position 40
		arch.getExtbus1().put(40);
		arch.getMemory().store();
		arch.getExtbus1().put(20);
		arch.getMemory().store();

		//storing 40 in position 34
		arch.getExtbus1().put(34);
		arch.getMemory().store();
		arch.getExtbus1().put(40);
		arch.getMemory().store();

		//now setting the register value
		arch.getExtbus1().put(17);
		arch.getRegistersList().get(1).store(); //RPG1 has 17

		//storing the number 1 in the memory, in position 35
		arch.getMemory().getDataList()[35]=1;

		//executing the command add Mem REG1
		arch.addMemReg();

		// --- Checking ---

		//testing if REG1 = 37
		arch.getRegistersList().get(1).read();
		assertEquals(37, arch.getExtbus1().get());

		//the flags bits 0 and 1 must be 0
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(0, arch.getFlags().getBit(1));

		//Testing if PC was pointing to 36
		arch.getPC().read();assertEquals(36, arch.getExtbus1().get());
	}

	@Test
	public void testAddRegMem() {
		Architecture arch = new Architecture();

		//making PC points to position 36
		arch.getExtbus1().put(36);
		arch.getPC().store();

		//now setting the register value
		arch.getExtbus1().put(36);
		arch.getRegistersList().get(2).store(); //RPG2 has 36

		//storing the number 2 in the memory, in position 37
		arch.getMemory().getDataList()[37]=2;

		//storing the number 12 in position 52
		arch.getExtbus1().put(52);
		arch.getMemory().store();
		arch.getExtbus1().put(12);
		arch.getMemory().store();

		//storing 52 in position 38
		arch.getExtbus1().put(38);
		arch.getMemory().store();
		arch.getExtbus1().put(52);
		arch.getMemory().store();

		//executing the command add REG2 Mem
		arch.addRegMem();

		// --- Checking ---

		//testing if Mem[52] = 48
		arch.getExtbus1().put(52);
		arch.getMemory().read();
		assertEquals(48, arch.getExtbus1().get());

		//the flags bits 0 and 1 must be 0
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(0, arch.getFlags().getBit(1));

		//Testing if PC was pointing to 39
		arch.getPC().read();assertEquals(39, arch.getExtbus1().get());
	}

	@Test
	public void testAddImmReg() {
		Architecture arch = new Architecture();

		//making PC points to position 39
		arch.getExtbus1().put(39);
		arch.getPC().store();

		//storing the value 100 in the memory, in position 40
		arch.getMemory().getDataList()[40]=100;

		//now setting the register value
		arch.getExtbus1().put(91);
		arch.getRegistersList().get(3).store(); //RPG3 has 91

		//storing the number 3 in the memory, in position 41
		arch.getMemory().getDataList()[41]=3;

		//executing the command add 100 REG3
		arch.addImmReg();

		// --- Checking ---

		//testing if REG3 = 191
		arch.getRegistersList().get(3).read();
		assertEquals(191, arch.getExtbus1().get());

		//the flags bits 0 and 1 must be 0
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(0, arch.getFlags().getBit(1));

		//Testing if PC was pointing to 42
		arch.getPC().read();assertEquals(42, arch.getExtbus1().get());
	}

	@Test
	public void testSubRegReg() {
		Architecture arch = new Architecture();


		// Positive result

		//making PC points to position 30
		arch.getExtbus1().put(30);
		arch.getPC().store();

		//now setting the registers values
		arch.getExtbus1().put(3);
		arch.getRegistersList().get(0).store(); //RPG0 has 3
		arch.getExtbus1().put(52);
		arch.getRegistersList().get(1).store(); //RPG1 has 52

		//storing the number 1 in the memory, in position 31
		arch.getMemory().getDataList()[31]=1;
		//storing the number 0 in the memory, in position 32
		arch.getMemory().getDataList()[32]=0;

		//executing the command sub REG1 REG0.
		arch.subRegReg();

		// --- Checking ---

		//testing if REG0 = 49
		arch.getRegistersList().get(0).read();
		assertEquals(49, arch.getExtbus1().get());

		//the flags bits 0 and 1 must be 0
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(0, arch.getFlags().getBit(1));

		//Testing if PC was pointing to 33
		arch.getPC().read();assertEquals(33, arch.getExtbus1().get());



		// Zero Result

		//making PC points to position 30
		arch.getExtbus1().put(30);
		arch.getPC().store();

		//now setting the registers values
		arch.getExtbus1().put(5);
		arch.getRegistersList().get(0).store(); //RPG0 has 5
		arch.getExtbus1().put(5);
		arch.getRegistersList().get(1).store(); //RPG1 has 5

		//storing the number 1 in the memory, in position 31
		arch.getMemory().getDataList()[31]=1;
		//storing the number 0 in the memory, in position 32
		arch.getMemory().getDataList()[32]=0;

		//executing the command sub REG1 REG0.
		arch.subRegReg();

		// --- Checking ---

		//testing if REG0 = 0
		arch.getRegistersList().get(0).read();
		assertEquals(0, arch.getExtbus1().get());

		//the flag bit 0 must be 1 and bit 1 must be 0
		assertEquals(1, arch.getFlags().getBit(0));
		assertEquals(0, arch.getFlags().getBit(1));

		//Testing if PC was pointing to 33
		arch.getPC().read();assertEquals(33, arch.getExtbus1().get());



		// Negative Result

		//making PC points to position 30
		arch.getExtbus1().put(30);
		arch.getPC().store();

		//now setting the registers values
		arch.getExtbus1().put(52);
		arch.getRegistersList().get(0).store(); //RPG0 has 52
		arch.getExtbus1().put(5);
		arch.getRegistersList().get(1).store(); //RPG1 has 5

		//storing the number 1 in the memory, in position 31
		arch.getMemory().getDataList()[31]=1;
		//storing the number 0 in the memory, in position 32
		arch.getMemory().getDataList()[32]=0;

		//executing the command sub REG1 REG0.
		arch.subRegReg();

		// --- Checking ---

		//testing if REG0 = -47
		arch.getRegistersList().get(0).read();
		assertEquals(-47, arch.getExtbus1().get());

		//the flag bit 0 must be 0 and bit 1 must be 1
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(1, arch.getFlags().getBit(1));

		//Testing if PC was pointing to 33
		arch.getPC().read();assertEquals(33, arch.getExtbus1().get());
	}

	@Test
	public void testSubMemReg() {
		Architecture arch = new Architecture();


		// Positive result

		//making PC points to position 33
		arch.getExtbus1().put(33);
		arch.getPC().store();

		//storing the number 20 in position 40
		arch.getExtbus1().put(40);
		arch.getMemory().store();
		arch.getExtbus1().put(20);
		arch.getMemory().store();

		//storing 40 in position 34
		arch.getExtbus1().put(34);
		arch.getMemory().store();
		arch.getExtbus1().put(40);
		arch.getMemory().store();

		//now setting the register value
		arch.getExtbus1().put(17);
		arch.getRegistersList().get(1).store(); //RPG1 has 17

		//storing the number 1 in the memory, in position 35
		arch.getMemory().getDataList()[35]=1;

		//executing the command sub Mem REG1
		arch.subMemReg();

		// --- Checking ---

		//testing if REG1 = 3
		arch.getRegistersList().get(1).read();
		assertEquals(3, arch.getExtbus1().get());

		//the flags bits 0 and 1 must be 0
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(0, arch.getFlags().getBit(1));

		//Testing if PC was pointing to 36
		arch.getPC().read();assertEquals(36, arch.getExtbus1().get());



		// Zero Result

		//making PC points to position 33
		arch.getExtbus1().put(33);
		arch.getPC().store();

		//storing the number 20 in position 40
		arch.getExtbus1().put(40);
		arch.getMemory().store();
		arch.getExtbus1().put(20);
		arch.getMemory().store();

		//storing 40 in position 34
		arch.getExtbus1().put(34);
		arch.getMemory().store();
		arch.getExtbus1().put(40);
		arch.getMemory().store();

		//now setting the register value
		arch.getExtbus1().put(20);
		arch.getRegistersList().get(1).store(); //RPG1 has 20

		//storing the number 1 in the memory, in position 35
		arch.getMemory().getDataList()[35]=1;

		//executing the command sub Mem REG1
		arch.subMemReg();

		// --- Checking ---

		//testing if REG1 = 0
		arch.getRegistersList().get(1).read();
		assertEquals(0, arch.getExtbus1().get());

		//the flag bit 0 must be 1 and bit 1 must be 0
		assertEquals(1, arch.getFlags().getBit(0));
		assertEquals(0, arch.getFlags().getBit(1));

		//Testing if PC was pointing to 36
		arch.getPC().read();assertEquals(36, arch.getExtbus1().get());



		// Negative Result

		//making PC points to position 33
		arch.getExtbus1().put(33);
		arch.getPC().store();

		//storing the number 9 in position 40
		arch.getExtbus1().put(40);
		arch.getMemory().store();
		arch.getExtbus1().put(9);
		arch.getMemory().store();

		//storing 40 in position 34
		arch.getExtbus1().put(34);
		arch.getMemory().store();
		arch.getExtbus1().put(40);
		arch.getMemory().store();

		//now setting the register value
		arch.getExtbus1().put(17);
		arch.getRegistersList().get(1).store(); //RPG1 has 17

		//storing the number 1 in the memory, in position 35
		arch.getMemory().getDataList()[35]=1;

		//executing the command sub Mem REG1
		arch.subMemReg();

		// --- Checking ---

		//testing if REG1 = -8
		arch.getRegistersList().get(1).read();
		assertEquals(-8, arch.getExtbus1().get());

		//the flag bit 0 must be 0 and bit 1 must be 1
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(1, arch.getFlags().getBit(1));

		//Testing if PC was pointing to 36
		arch.getPC().read();assertEquals(36, arch.getExtbus1().get());
	}

	@Test
	public void testSubRegMem() {
		Architecture arch = new Architecture();


		// Positive result

		//making PC points to position 36
		arch.getExtbus1().put(36);
		arch.getPC().store();

		//now setting the register value
		arch.getExtbus1().put(36);
		arch.getRegistersList().get(2).store(); //RPG2 has 36

		//storing the number 2 in the memory, in position 37
		arch.getMemory().getDataList()[37]=2;

		//storing the number 12 in position 52
		arch.getExtbus1().put(52);
		arch.getMemory().store();
		arch.getExtbus1().put(12);
		arch.getMemory().store();

		//storing 52 in position 38
		arch.getExtbus1().put(38);
		arch.getMemory().store();
		arch.getExtbus1().put(52);
		arch.getMemory().store();

		//executing the command sub REG2 Mem
		arch.subRegMem();

		// --- Checking ---

		//testing if Mem[52] = 24
		arch.getExtbus1().put(52);
		arch.getMemory().read();
		assertEquals(24, arch.getExtbus1().get());

		//the flags bits 0 and 1 must be 0
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(0, arch.getFlags().getBit(1));

		//Testing if PC was pointing to 39
		arch.getPC().read();assertEquals(39, arch.getExtbus1().get());



		// Zero Result

		//making PC points to position 36
		arch.getExtbus1().put(36);
		arch.getPC().store();

		//now setting the register value
		arch.getExtbus1().put(36);
		arch.getRegistersList().get(2).store(); //RPG2 has 36

		//storing the number 2 in the memory, in position 37
		arch.getMemory().getDataList()[37]=2;

		//storing the number 36 in position 52
		arch.getExtbus1().put(52);
		arch.getMemory().store();
		arch.getExtbus1().put(36);
		arch.getMemory().store();

		//storing 52 in position 38
		arch.getExtbus1().put(38);
		arch.getMemory().store();
		arch.getExtbus1().put(52);
		arch.getMemory().store();

		//executing the command sub REG2 Mem
		arch.subRegMem();

		// --- Checking ---

		//testing if Mem[52] = 0
		arch.getExtbus1().put(52);
		arch.getMemory().read();
		assertEquals(0, arch.getExtbus1().get());

		//the flag bit 0 must be 1 and bit 1 must be 0
		assertEquals(1, arch.getFlags().getBit(0));
		assertEquals(0, arch.getFlags().getBit(1));

		//Testing if PC was pointing to 39
		arch.getPC().read();assertEquals(39, arch.getExtbus1().get());



		// Negative Result

		//making PC points to position 36
		arch.getExtbus1().put(36);
		arch.getPC().store();

		//now setting the register value
		arch.getExtbus1().put(5);
		arch.getRegistersList().get(2).store(); //RPG2 has 5

		//storing the number 2 in the memory, in position 37
		arch.getMemory().getDataList()[37]=2;

		//storing the number 12 in position 52
		arch.getExtbus1().put(52);
		arch.getMemory().store();
		arch.getExtbus1().put(12);
		arch.getMemory().store();

		//storing 52 in position 38
		arch.getExtbus1().put(38);
		arch.getMemory().store();
		arch.getExtbus1().put(52);
		arch.getMemory().store();

		//executing the command sub REG2 Mem
		arch.subRegMem();

		// --- Checking ---

		//testing if Mem[52] = -7
		arch.getExtbus1().put(52);
		arch.getMemory().read();
		assertEquals(-7, arch.getExtbus1().get());

		//the flag bit 0 must be 0 and bit 1 must be 1
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(1, arch.getFlags().getBit(1));

		//Testing if PC was pointing to 39
		arch.getPC().read();assertEquals(39, arch.getExtbus1().get());
	}

	@Test
	public void testSubImmReg() {
		Architecture arch = new Architecture();


		// Positive result

		//making PC points to position 39
		arch.getExtbus1().put(39);
		arch.getPC().store();

		//storing the value 100 in the memory, in position 40
		arch.getMemory().getDataList()[40]=100;

		//now setting the register value
		arch.getExtbus1().put(91);
		arch.getRegistersList().get(3).store(); //RPG3 has 91

		//storing the number 3 in the memory, in position 41
		arch.getMemory().getDataList()[41]=3;

		//executing the command sub 100 REG3
		arch.subImmReg();

		// --- Checking ---

		//testing if REG3 = 9
		arch.getRegistersList().get(3).read();
		assertEquals(9, arch.getExtbus1().get());

		//the flags bits 0 and 1 must be 0
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(0, arch.getFlags().getBit(1));

		//Testing if PC was pointing to 42
		arch.getPC().read();assertEquals(42, arch.getExtbus1().get());



		// Zero Result

		//making PC points to position 39
		arch.getExtbus1().put(39);
		arch.getPC().store();

		//storing the value 100 in the memory, in position 40
		arch.getMemory().getDataList()[40]=100;

		//now setting the register value
		arch.getExtbus1().put(100);
		arch.getRegistersList().get(3).store(); //RPG3 has 100

		//storing the number 3 in the memory, in position 41
		arch.getMemory().getDataList()[41]=3;

		//executing the command sub 100 REG3
		arch.subImmReg();

		// --- Checking ---

		//testing if REG3 = 0
		arch.getRegistersList().get(3).read();
		assertEquals(0, arch.getExtbus1().get());

		//the flag bit 0 must be 1 and bit 1 must be 0
		assertEquals(1, arch.getFlags().getBit(0));
		assertEquals(0, arch.getFlags().getBit(1));

		//Testing if PC was pointing to 42
		arch.getPC().read();assertEquals(42, arch.getExtbus1().get());



		// Negative Result

		//making PC points to position 39
		arch.getExtbus1().put(39);
		arch.getPC().store();

		//storing the value 90 in the memory, in position 40
		arch.getMemory().getDataList()[40]=90;

		//now setting the register value
		arch.getExtbus1().put(100);
		arch.getRegistersList().get(3).store(); //RPG3 has 100

		//storing the number 3 in the memory, in position 41
		arch.getMemory().getDataList()[41]=3;

		//executing the command sub 90 REG3
		arch.subImmReg();

		// --- Checking ---

		//testing if REG3 = -10
		arch.getRegistersList().get(3).read();
		assertEquals(-10, arch.getExtbus1().get());

		//the flag bit 0 must be 0 and bit 1 must be 1
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(1, arch.getFlags().getBit(1));

		//Testing if PC was pointing to 42
		arch.getPC().read();assertEquals(42, arch.getExtbus1().get());
	}
}
