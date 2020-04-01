package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.ClientPersonalInfo;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.Cancelled;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class CancelOperationMethodTest {
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String NIF = "123456789";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "Antonio";

	private Sibs sibs;
	private Bank sourceBank;
	private Bank targetBank;
	private Client sourceClient;
	private Client targetClient;
	private Services services;

	@Before
	public void setUp() throws BankException, AccountException, ClientException {
		this.services = new Services();
		this.sibs = new Sibs(100, this.services);
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.sourceClient = new Client(this.sourceBank, new ClientPersonalInfo(FIRST_NAME, LAST_NAME, NIF, 33),
				PHONE_NUMBER, ADDRESS);
		this.targetClient = new Client(this.targetBank, new ClientPersonalInfo(FIRST_NAME, LAST_NAME, NIF, 22),
				PHONE_NUMBER, ADDRESS);
	}

	@Test
	public void sucessCancelOperationTest()
			throws BankException, AccountException, ClientException, SibsException, OperationException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		int position = this.sibs.transfer(sourceIban, targetIban, 100);
		TransferOperation operation = (TransferOperation) this.sibs.getOperation(position);

		assertTrue(operation.getState() instanceof Registered);
		this.sibs.cancelOperation(operation.getOperationId());
		assertTrue(operation.getState() instanceof Cancelled);
	}

	@Test
	public void cancelOperationErrorTest()
			throws BankException, AccountException, ClientException, SibsException, OperationException {
		try {
			this.sibs.cancelOperation(1);
			fail();
		} catch (SibsException e) {
			assertEquals(0, this.sibs.getNumberOfOperations());
		}
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}
}
