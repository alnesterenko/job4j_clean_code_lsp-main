package ru.job4j.ood.isp.menu;

import java.util.*;

public class SimpleMenu implements Menu {

    private final List<MenuItem> rootElements = new ArrayList<>();

    @Override
    public boolean add(String parentName, String childName, ActionDelegate actionDelegate) {
        boolean result = false;
        MenuItem menuItem = new SimpleMenuItem(childName, actionDelegate);
        if (parentName == null) {
            result = rootElements.add(menuItem);
        } else {
            Optional<ItemInfo> searchResult = findItem(parentName);
            if (searchResult.isPresent()) {
                result = searchResult.get().menuItem.getChildren().add(menuItem);
            }
        }
        return  result;
    }

    @Override
    public Optional<MenuItemInfo> select(String itemName) {
        Optional<ItemInfo> tempItemInfo = findItem(itemName);
        return tempItemInfo.isPresent()
                ? Optional.of(new MenuItemInfo(tempItemInfo.get().menuItem, tempItemInfo.get().number))
                : Optional.empty();
    }

    @Override
    public Iterator<MenuItemInfo> iterator() {
        Iterator<MenuItemInfo> iterator = new Iterator<MenuItemInfo>() {
            private DFSIterator dfsIterator = new DFSIterator();

            @Override
            public boolean hasNext() {
                return dfsIterator.hasNext();
            }

            @Override
            public MenuItemInfo next() {
                ItemInfo tempItemInfo = dfsIterator.next();
                return new MenuItemInfo(tempItemInfo.menuItem, tempItemInfo.number);
            }
        };
        return iterator;
    }

    private Optional<ItemInfo> findItem(String name) {
        Optional<ItemInfo> result = Optional.empty();
        DFSIterator dfsIterator = new DFSIterator();
        while (dfsIterator.hasNext()) {
            ItemInfo currentItemInfo = dfsIterator.next();
            if (currentItemInfo.menuItem.getName().equals(name)) {
                result = Optional.ofNullable(currentItemInfo);
                break;
            }
        }
        return result;
    }

    private static class SimpleMenuItem implements MenuItem {

        private String name;
        private List<MenuItem> children = new ArrayList<>();
        private ActionDelegate actionDelegate;

        public SimpleMenuItem(String name, ActionDelegate actionDelegate) {
            this.name = name;
            this.actionDelegate = actionDelegate;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public List<MenuItem> getChildren() {
            return children;
        }

        @Override
        public ActionDelegate getActionDelegate() {
            return actionDelegate;
        }
    }

    private class DFSIterator implements Iterator<ItemInfo> {

        private Deque<MenuItem> stack = new LinkedList<>();

        private Deque<String> numbers = new LinkedList<>();

        DFSIterator() {
            int number = 1;
            for (MenuItem item : rootElements) {
                stack.addLast(item);
                numbers.addLast(String.valueOf(number++).concat("."));
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public ItemInfo next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            MenuItem current = stack.removeFirst();
            String lastNumber = numbers.removeFirst();
            List<MenuItem> children = current.getChildren();
            int currentNumber = children.size();
            for (var i = children.listIterator(children.size()); i.hasPrevious();) {
                stack.addFirst(i.previous());
                numbers.addFirst(lastNumber.concat(String.valueOf(currentNumber--)).concat("."));
            }
            return new ItemInfo(current, lastNumber);
        }
    }

    private class ItemInfo {

        private MenuItem menuItem;
        private String number;

        public ItemInfo(MenuItem menuItem, String number) {
            this.menuItem = menuItem;
            this.number = number;
        }
    }
}