package ru.job4j.ood.isp.menu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.*;

class PrinterTest {
    public static final ActionDelegate STUB_ACTION = System.out::println;
    public static final String LINESEPARATOR = System.lineSeparator();
    private ByteArrayOutputStream output;
    private PrintStream old;

    @BeforeEach
    public void setUpStreams() {
        old = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    public void cleanUpStreams() {
        System.setOut(old);
    }

    @Test
    public void whenNeedPrintSameAsInTheTask() {
        Menu menu = new SimpleMenu();
        MenuPrinter printer = new Printer();
        menu.add(Menu.ROOT, "Задача Общая.", STUB_ACTION);
        menu.add("Задача Общая.", "Задача Первая.", STUB_ACTION);
        menu.add("Задача Первая.", "Цель задачи.", STUB_ACTION);
        menu.add("Задача Первая.", "Ограничения и требования к решению.", STUB_ACTION);
        menu.add("Задача Общая.", "Задача Вторая.", STUB_ACTION);
        printer.print(menu);
        String expected = "1. Задача Общая." + LINESEPARATOR
                + "----1.1. Задача Первая." + LINESEPARATOR
                + "-------1.1.1. Цель задачи." + LINESEPARATOR
                + "-------1.1.2. Ограничения и требования к решению." + LINESEPARATOR
                + "----1.2. Задача Вторая." + LINESEPARATOR;
        assertThat(expected).isEqualTo(output.toString());
    }
}