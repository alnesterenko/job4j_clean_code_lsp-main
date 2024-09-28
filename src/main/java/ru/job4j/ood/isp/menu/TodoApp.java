package ru.job4j.ood.isp.menu;

import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * 6. Создайте простенький класс TodoApp. Этот класс будет представлять собой консольное приложение,
 * которое позволяет:
 * Добавить элемент в корень меню;
 * Добавить элемент к родительскому элементу;
 * Вызвать действие, привязанное к пункту меню (действие можно сделать константой,
 * например, ActionDelete DEFAULT_ACTION = () -> System.out.println("Some action") и указывать при добавлении элемента в меню);
 * Вывести меню в консоль.
 */
public class TodoApp {
    public static final ActionDelegate STUB_ACTION = () -> System.out.println("STUB_ACTION");
    public static final ActionDelegate DEFAULT_ACTION = () -> System.out.println("DEFAULT_ACTION");
    public static final ActionDelegate STANDARD_ACTION = () -> System.out.println("STANDARD_ACTION");
    private static Scanner scanner = new Scanner(System.in);
    private final Menu todoAppMenu;
    private final Menu workingMenu;

    public TodoApp() {
        this.todoAppMenu = new SimpleMenu();
        todoAppMenu.add(Menu.ROOT, "Добавить элемент в корень меню;", this::addMenuItemToROOT);
        todoAppMenu.add(Menu.ROOT, "Добавить элемент к родительскому элементу;", this::addMenuItemToParentItem);
        todoAppMenu.add(Menu.ROOT, "Вызвать действие, привязанное к пункту меню;", this::useActionDelegateFromWorkingMenu);
        todoAppMenu.add(Menu.ROOT, "Вывести меню в консоль;", this::printWorkingMenu);
        todoAppMenu.add(Menu.ROOT, "Выйти из программы;", () -> System.out.println("Выход из программы"));
        this.workingMenu = new SimpleMenu();
    }

    public Menu getTodoAppMenu() {
        return todoAppMenu;
    }

    public Menu getWorkingMenu() {
        return workingMenu;
    }

    private void useActionDelegate(String number, Menu menuName) {
        if (numberValidation(number)) {
            var menuItemInfoOptional = getMenuItemInfoFromNumber(number, menuName);
                if (menuItemInfoOptional.isPresent()) {
                    menuItemInfoOptional.get().getActionDelegate().delegate();
            }
        }
    }

    private Optional<Menu.MenuItemInfo> getMenuItemInfoFromNumber(String number, Menu menuName) {
        Optional<Menu.MenuItemInfo> result = Optional.empty();
        for (Menu.MenuItemInfo currentMenuItemInfo : menuName) {
            if (currentMenuItemInfo.getNumber().equals(number)) {
                result = Optional.of(currentMenuItemInfo);
            }
        }
        return result;
    }

    private boolean numberValidation(String answer) {
        answer = answer.trim();
        return Pattern.matches("^(\\d+\\.)+$", answer);
    }

    private boolean stringValidation(String answer) {
        answer = answer.trim();
        return answer.length() > 0;
    }

    private String gettingMenuItemNameFromUser() {
        String answer;
        do {
            System.out.println("Введите название элемента.");
            System.out.println("Он не может состоять только из одних пробелов или пустой строки.");
            answer = TodoApp.scanner.nextLine();
        } while (!stringValidation(answer));
        return answer;
    }

    private String gettingParentNameMenuItemFromUser() {
        String answer;
        do {
            System.out.println("Введите название РОДИТЕЛЬСКОГО элемента.");
            System.out.println("Он не может состоять только из одних пробелов или пустой строки.");
            answer = TodoApp.scanner.nextLine();
        } while (!stringValidation(answer));
        return answer;
    }

    private ActionDelegate gettingActionDelegateFromUser() {
        String answer;
        ActionDelegate actionDelegate;
        do {
            System.out.println("Добавление действия к пункту меню. ");
            System.out.println("Введите константу для закрепления действия за пунктом меню.");
            System.out.println("Список констант: ");
            System.out.println("STUB_ACTION");
            System.out.println("DEFAULT_ACTION");
            System.out.println("STANDARD_ACTION");
            System.out.println("Или введите какую-нибудь тарабарщину, чисто чтоб поржать.))))");
            answer = TodoApp.scanner.nextLine();
        } while (!stringValidation(answer));
        String finalAnswer = answer;
        actionDelegate = switch (answer) {
            case "STUB_ACTION" -> STUB_ACTION;
            case "DEFAULT_ACTION" -> DEFAULT_ACTION;
            case "STANDARD_ACTION" -> STANDARD_ACTION;
            default -> () -> System.out.println(finalAnswer);
        };
        return actionDelegate;
    }

    private void addMenuItemToROOT() {
        System.out.println("Добавление элемента в корень меню.");
        if (workingMenu.add(Menu.ROOT, gettingMenuItemNameFromUser(), gettingActionDelegateFromUser())) {
            System.out.println("Элемент добавлен в корень списка!");
        } else {
            System.out.println("Что-то пошло не так!");
        }
    }

    private void addMenuItemToParentItem() {
        System.out.println("Добавление элемента к РОДИТЕЛЬСКОМУ элементу.");
        String parent = gettingParentNameMenuItemFromUser();
        if (workingMenu.add(parent,
                gettingMenuItemNameFromUser(),
                gettingActionDelegateFromUser())) {
            System.out.printf("Элемент добавлен к родителю %s!%s", parent, System.lineSeparator());
        } else {
            System.out.println("Что-то пошло не так!");
        }
    }

    private void useActionDelegateFromWorkingMenu() {
        System.out.println("Выбирите пункт меню, действие которого хотите использовать: ");
        System.out.println("(Вводить нужно с точкой.)");
        useActionDelegate(TodoApp.scanner.nextLine(), this.workingMenu);
    }

    private void printWorkingMenu() {
        MenuPrinter printer = new Printer();
        var workingMenu = this.getWorkingMenu();
        if (getMenuItemInfoFromNumber("1.", workingMenu).isEmpty()) {
            System.out.println("В меню пока ничего нет!");
        } else {
            printer.print(workingMenu);
        }
    }

    public static void main(String[] args) {
        TodoApp todoApp = new TodoApp();
        MenuPrinter printer = new Printer();
        String answer;
        do {
            printer.print(todoApp.getTodoAppMenu());
            System.out.println("Введите номер строки меню, который хотите использовать: ");
            System.out.println("(Вводить нужно с точкой.)");
            answer = TodoApp.scanner.nextLine();
            todoApp.useActionDelegate(answer, todoApp.getTodoAppMenu());
            System.out.println();
        } while (!"5.".equals(answer));
    }
}
