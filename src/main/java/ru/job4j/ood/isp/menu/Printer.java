package ru.job4j.ood.isp.menu;

public class Printer implements MenuPrinter {

    @Override
    public void print(Menu menu) {
        double sizeModification = 1.2;
        for (Menu.MenuItemInfo oneMenuItemInfo : menu) {
            StringBuilder stringBuilder = new StringBuilder();
            String number = oneMenuItemInfo.getNumber();
            int numberLength = number.length();
            if (numberLength > 2) {
                numberLength = (int) (numberLength * sizeModification);
                for (int i = 0; i < numberLength; i++) {
                    stringBuilder.append("-");
                }
            }
            stringBuilder.append(number);
            stringBuilder.append(" ");
            stringBuilder.append(oneMenuItemInfo.getName());
            System.out.println(stringBuilder);
        }
    }
}
