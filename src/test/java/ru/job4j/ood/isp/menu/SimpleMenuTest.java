package ru.job4j.ood.isp.menu;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class SimpleMenuTest {

    public static final ActionDelegate STUB_ACTION = System.out::println;

    @Test
    public void whenAddThenReturnSame() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add(Menu.ROOT, "Покормить собаку", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить продукты", STUB_ACTION);
        menu.add("Купить продукты", "Купить хлеб", STUB_ACTION);
        menu.add("Купить продукты", "Купить молоко", STUB_ACTION);
        assertThat(new Menu.MenuItemInfo("Сходить в магазин",
                List.of("Купить продукты"), STUB_ACTION, "1."))
                .isEqualTo(menu.select("Сходить в магазин").get());
        assertThat(new Menu.MenuItemInfo(
                "Купить продукты",
                List.of("Купить хлеб", "Купить молоко"), STUB_ACTION, "1.1."))
                .isEqualTo(menu.select("Купить продукты").get());
        assertThat(new Menu.MenuItemInfo(
                "Покормить собаку", List.of(), STUB_ACTION, "2."))
                .isEqualTo(menu.select("Покормить собаку").get());
        menu.forEach(i -> System.out.println(i.getNumber() + i.getName()));
    }

    @Test
    public void whenAddThenSelect() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Задача Общая.", STUB_ACTION);
        menu.add("Задача Общая.", "Задача Первая.", STUB_ACTION);
        menu.add("Задача Первая.", "Цель задачи.", STUB_ACTION);
        menu.add("Задача Первая.", "Ограничения и требования к решению.", STUB_ACTION);
        menu.add("Задача Общая.", "Задача Вторая.", STUB_ACTION);
        assertThat(new Menu.MenuItemInfo("Задача Общая.",
                List.of("Задача Первая.", "Задача Вторая."), STUB_ACTION, "1."))
                .isEqualTo(menu.select("Задача Общая.").get());
        assertThat(menu.select(null)).isEqualTo(Optional.empty());
        menu.forEach(i -> System.out.println(i.getNumber() + i.getName()));
    }
}