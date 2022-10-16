import AppEnum.MachineOperation;
import AppEnum.PaymentMethod;
import controller.SnackVendingMachineController;
import exception.InvalidOption;
import model.*;


import java.util.*;

public class Main {
    public static Card USDCard = new Card("USD", 1);
    public static Card JORCard = new Card("JOR", 1);

    private static SnackVendingMachineController controller;
    private static Queue<MachineOperation> queue;

    public static void main(String[] args) {
        controller = new SnackVendingMachineController();
        runMachine();
    }


    public static void runMachine() {
        queue = new LinkedList<>();
        queue.add(MachineOperation.DISPLAY_SNACKS);
        queue.add(MachineOperation.SELECT_SNACK);
        queue.add(MachineOperation.MONEY_INSERTION);
        queue.add(MachineOperation.ENOUGH_MONEY);
        queue.add(MachineOperation.EJECT_SNACK);
        queue.add(MachineOperation.EJECT_MONEY);
        machineWorkflow();
    }

    public static void machineWorkflow() {
        machine_work_flow:
        while (!queue.isEmpty()) {
            switch (queue.peek()) {
                case DISPLAY_SNACKS: {
                    displaySnacks();
                    queue.add(queue.remove());
                    break;
                }
                case SELECT_SNACK: {
                    try {
                        selectSnack();
                        queue.add(queue.remove());
                    } catch (InvalidOption ex) {
                        System.out.println(ex.getMessage());
                    } catch (EmptyStackException ex) {
                        System.out.println("product is not available!");
                    } catch (InputMismatchException ex) {
                        System.out.println("invalid option!");
                    } catch (ArrayIndexOutOfBoundsException exception) {
                        System.out.println("invalid selection!");
                    }
                    break;
                }

                case MONEY_INSERTION: {
                    try {
                        insertMoney();
                    } catch (IllegalArgumentException ex) {
                        System.out.println("exception: " + ex.getMessage());
                        continue;
                    } catch (InputMismatchException ex) {
                        System.out.println("exception: invalid input!");
                    } catch (Exception ex) {
                        System.out.println("Exception: " + ex.getMessage());
                    }
                    if (controller.getPaymentMethod() == PaymentMethod.CARD_SLOT) {
                        queue.add(queue.remove());
                        break;
                    }
                }
                case ENOUGH_MONEY: {
                    PaymentMethod paymentMethod = controller.getPaymentMethod();
                    if (paymentMethod == PaymentMethod.CARD_SLOT && !controller.isEnoughMoney()) {
                        System.out.println("card does not have enough money!");
                        System.out.println("card is ejected");
                        break machine_work_flow;
                    } else if (paymentMethod != PaymentMethod.CARD_SLOT && !controller.isEnoughMoney()) {
                        System.out.println("not enough money");
                    } else {
                        if (controller.getPaymentMethod() != PaymentMethod.CARD_SLOT)
                            queue.add(queue.remove());
                        queue.add(queue.remove());
                    }
                    break;
                }
                case EJECT_SNACK: {
                    ejectSnack();
                    queue.add(queue.remove());
                    break;
                }
                case EJECT_MONEY: {

                    ejectRemainingMoney();
                    queue.add(queue.remove());
                    break;
                }
            }
        }
        runMachine();
    }

    private static void displaySnacks() {
        System.out.println();
        SnackSlots snackSlots = controller.getSnacksSlot();
        for (int x = 0; x < snackSlots.getRows().length; x++) {
            Row row = snackSlots.getRows()[x];
            for (int y = 0; y < row.getColumns().length; y++) {
                Column column = row.getColumns()[y];
                String code = String.format("%d%d", x + 1, y + 1);
                Product product = column.getColProduct();
                System.out.printf("%8s: %8s %8s || ", code, product.getName(), product.getPrice());
            }
            System.out.println();
        }
    }

    private static void selectSnack() throws ArrayIndexOutOfBoundsException {
        System.out.println("select snack:");
        Scanner scanner = new Scanner(System.in);
        int code = scanner.nextInt();
        Product product = controller.selectSnack(code);
        System.out.println(String.format("Selected product: %s price: %f", product.getName(), product.getPrice()));
    }

    private static void insertMoney() throws IllegalArgumentException, Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("select payment method");
        int method;
        System.out.println("1) pay by coin");
        System.out.println("2) pay by banknote");
        if (controller.getPaymentMethod() == null) {
            System.out.println("3) pay by card");
            method = scanner.nextInt();
            if (method > 3 || method < 0) {
                throw new IllegalArgumentException("invalid selection!");
            }
        } else {
            method = scanner.nextInt();
            if (method > 2 || method < 0) {
                throw new IllegalArgumentException("invalid selection!");
            }
        }

        List<String> list = controller.displayPaymentMethodOptions(PaymentMethod.values()[method - 1]);
        System.out.println("valid input:");
        System.out.println(list);

        if (method - 1 == PaymentMethod.CARD_SLOT.ordinal()) {
            System.out.println("Note I made static cards ");
            System.out.println("select card:");
            System.out.println("1) card 1 (USD card)");
            System.out.println("2) card 2 (JOR card)");
            int card = scanner.nextInt();
            if (!(card > 0 && card < 3))
                throw new IllegalArgumentException("invalid selection!");
            //JOR Card
            if (card == 2) {
                throw new Exception("The valid card currency is USD");
            }
            controller.insertMoney(PaymentMethod.CARD_SLOT, String.valueOf(USDCard.getMoney()));
        } else {
            System.out.println("insert money:");
            String money = scanner.next();
            if (money.endsWith(MoneySlot.VALID_COIN) || money.endsWith(MoneySlot.VALID_CURRENCY)) {
                controller.insertMoney(PaymentMethod.values()[method - 1], money);
                System.out.println("inserted money : " + controller.displayInsertedMoney());
            } else
                throw new IllegalArgumentException("invalid selection!\nThe valid currency is USD");
        }
    }

    private static void ejectSnack() {
        Product product = controller.popSnack();
        if (controller.getPaymentMethod() == PaymentMethod.CARD_SLOT) {
            USDCard.pay(product.getPrice());
        }
        System.out.println(String.format("product %s is ejected successfully", product.getName()));
    }

    private static void ejectRemainingMoney() {
        System.out.println("ejected money: " + controller.ejectRemainingMoney());
    }
}
