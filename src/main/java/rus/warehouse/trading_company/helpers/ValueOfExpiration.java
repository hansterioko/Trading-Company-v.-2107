package rus.warehouse.trading_company.helpers;

import lombok.Getter;

public enum ValueOfExpiration {
    DAY("Днях"),
    YEAR("Годах"),
    HOUR("Часах"),
    WEEK("Неделях");

    @Getter
    private String title;

    ValueOfExpiration(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
