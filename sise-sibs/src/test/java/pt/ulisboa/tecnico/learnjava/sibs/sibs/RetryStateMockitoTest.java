package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.Error;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.Withdrawn;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class RetryStateMockitoTest {
	private Sibs sibs;
	private String sourceIban;
	private String targetIban;

	@Before
	public void setUp() {
		this.sourceIban = "CGKCK1";
		this.targetIban = "BESCK1";
	}

	@Test
	public void failOneTimeAndProcessTest()
			throws BankException, AccountException, SibsException, OperationException, ClientException {
		Services mockedServices = mock(Services.class);
		this.sibs = new Sibs(100, mockedServices);

		when(mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		when(mockedServices.accountExists(this.targetIban)).thenReturn(true);

		this.sibs.transfer(this.sourceIban, this.targetIban, 100);
		doThrow(new AccountException()).when(mockedServices).deposit(this.targetIban, 100);
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Withdrawn);

		doNothing().when(mockedServices).deposit(this.targetIban, 100);
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Completed);
		assertEquals(1, this.sibs.getNumberOfOperations());
		assertEquals(100, this.sibs.getTotalValueOfOperations());
		assertEquals(100, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
	}

	@Test
	public void failFourTimesTest()
			throws BankException, AccountException, SibsException, OperationException, ClientException {
		Services mockedServices = mock(Services.class);
		this.sibs = new Sibs(100, mockedServices);

		when(mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		when(mockedServices.accountExists(this.targetIban)).thenReturn(true);

		this.sibs.transfer(this.sourceIban, this.targetIban, 100);
		doThrow(new AccountException()).when(mockedServices).deposit(this.targetIban, 100);
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Withdrawn);

		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Withdrawn);

		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Withdrawn);

		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Error);
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
		this.sibs = null;
	}
}
