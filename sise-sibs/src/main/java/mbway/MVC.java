package mbway;

import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MVC {

	public static void main(String[] args)
			throws MbwayException, BankException, ClientException, AccountException, SibsException, OperationException {

		boolean state = true;
		MbwayView view = new MbwayView();
		MbwayController controller = new MbwayController(view);

		Scanner scanner = new Scanner(System.in);
		while (state) {
			System.out.println("\n" + "Type your command? ");
			System.out.println("\n" + "exit" + "\n" + "associate-mbway <Iban> <phone>" + "\n" + "confirm-mbway <code>"
					+ "\n" + "mbway-transfer <sourcephone> <targetphone> <amount>" + "\n"
					+ "friend <phone> <amount> (separate the decimal part with a dot '.')" + "\n"
					+ "split-bill <numberOfFriends> <total amount>");

			String command = scanner.nextLine();
			String[] parameters = command.split(" ");
			String input = parameters[0];

			switch (input) {
			case "exit":
				state = false;
				System.out.println("Exited sucessfully");
				scanner.close();
				break;

			case "associate-mbway":
				String phone = parameters[2];
				String iban = parameters[1];
				controller.associateMbway(phone, iban);
				break;

			case "confirm-mbway":
				System.out.println("Enter Confirmation Code:");
				String code = parameters[1];
				if (!controller.getmodelState()) {
					controller.mbwayConfirmation(code);
				} else {
					System.out.println("This account is already confirmed");
				}

				break;
			case "mbway-transfer":
				String sourcephone = parameters[1];
				String targetphone = parameters[2];
				String value = parameters[3];
				controller.mbwayTransfer(sourcephone, targetphone, value);

				break;

			case "friend":
				controller.verifyFriendinfo(parameters[1], parameters[2]);
				break;
			case "split-bill":
				controller.splitBill(parameters[2], parameters[1]);
				break;
			default:
				System.out.println("Unaccepted Input");
				break;

			}

		}
	}

}
