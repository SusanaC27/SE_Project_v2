package pt.ulisboa.tecnico.learnjava.sibs.domain.states;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class Registered implements State {

	@Override
	public void process(TransferOperation wrapper, Services services) throws AccountException, SibsException {
		try {
			services.withdraw(wrapper.getSourceIban(), wrapper.getValue());
			wrapper.setState(new Withdrawn());
		} catch (AccountException e) {
			throw new SibsException();
		}
	}

	@Override
	public void cancel(TransferOperation wrapper, Services services) throws AccountException {
		wrapper.setState(new Cancelled());
	}
}