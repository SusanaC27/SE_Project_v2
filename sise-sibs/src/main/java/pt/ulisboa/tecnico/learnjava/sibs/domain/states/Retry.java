package pt.ulisboa.tecnico.learnjava.sibs.domain.states;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public class Retry implements State {

	@Override
	public void process(TransferOperation wrapper, Services services) throws AccountException {
		if (wrapper.counter > 0) {
			wrapper.counter--;
			State state = wrapper.getLastState();
			wrapper.setState(state);
		} else {
			wrapper.setState(new Error());
			wrapper.counter = 3;
			withdrawnInstance(wrapper, services);
			depositedInstance(wrapper, services);
		}
	}

	// Refactor for Write Short Units of Code - line 24 to line 28.
	public void withdrawnInstance(TransferOperation wrapper, Services services) throws AccountException {
		if (wrapper.getLastState() instanceof Withdrawn) {
			services.deposit(wrapper.getSourceIban(), wrapper.getValue());
		}
	}

	// Refactor for Write Short Units of Code - line 31 to line 36.
	public void depositedInstance(TransferOperation wrapper, Services services) throws AccountException {
		if (wrapper.getLastState() instanceof Deposited) {
			services.deposit(wrapper.getSourceIban(), wrapper.getValue());
			services.withdraw(wrapper.getTargetIban(), wrapper.getValue());
		}
	}

	@Override
	public void cancel(TransferOperation wrapper, Services services) throws AccountException {
	}
}
