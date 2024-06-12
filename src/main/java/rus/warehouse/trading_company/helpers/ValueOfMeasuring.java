package rus.warehouse.trading_company.helpers;

import lombok.Getter;

public enum ValueOfMeasuring {
    _391("ШТ");

    @Getter
    private String title;

    ValueOfMeasuring(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
