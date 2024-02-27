package study.stepup.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayDeque;
import java.util.Deque;

import java.util.HashMap;
//import java.util.Iterator;
//import java.util.function.Predicate;

public class Account {
    private final Deque<Runnable> history = new ArrayDeque<>();
    @Getter
    private String name;
    private HashMap<MyCurrency, Integer> currencies = new HashMap<>();

    public Account(String name) {
        setName(name);
    }

    public void setName(String name) {
        if (!isHolderNameCorrect(name)) {
            throw new IllegalArgumentException("Некорректное имя счета");
        }
        String oldValue = this.getName();
        history.push(() -> this.intSetName(oldValue));
        intSetName(name);
    }

    public HashMap<MyCurrency, Integer> getCurrencies() {
        return new HashMap<>(currencies);
    }

    public Integer getCurrency(MyCurrency currency) {
        if (currencies.containsKey(currency)) {
            return currencies.get(currency);
        }
        return 0;
    }

    public void setCurrency(MyCurrency currency, Integer count) {
        if (count != null && count < 0) {
            throw new IllegalArgumentException("Некорректное количество валюты");
        }

        Integer oldValue = currencies.get(currency);
        history.push(() -> this.intSetCurrency(currency, oldValue));
        intSetCurrency(currency, count);
    }

     AccountMemento createMemento() {
        return new AccountMemento(name, getCurrencies());
    }

     void restoreFromMemento(AccountMemento memento) {
        this.name = memento.getName();
        this.currencies = memento.getCurrencies();
    }

    public void undo() {
        if (history.isEmpty()) {
            throw new IllegalArgumentException("Отсутствуют изменения для выполнения отката операции");
        }

        Runnable runnable = history.poll();
        runnable.run();
    }

    @Override
    public String toString() {
        return "Account{ name=" + getName() + ", currencies=" + currencies + "}";
    }


    private boolean isHolderNameCorrect(String name) {
        return name != null && !name.isEmpty();
    }

    private void intSetCurrency(MyCurrency currency, Integer count) {
        if (count == null) {
            currencies.remove(currency);
        } else {
            currencies.put(currency, count);
        }
    }

    private void intSetName(String name) {
        this.name = name;
    }

    @AllArgsConstructor
    @Getter
    static class AccountMemento {

        private final String name;
        private final HashMap<MyCurrency, Integer> currencies;

    }

}
