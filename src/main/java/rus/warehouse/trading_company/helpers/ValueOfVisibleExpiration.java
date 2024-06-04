package rus.warehouse.trading_company.helpers;

import lombok.Getter;

public enum ValueOfVisibleExpiration {
    DIGITAL("Числом"),
    NOTHING("Не требуется"),
    DATE("Датой");

    @Getter
    private String title;

    ValueOfVisibleExpiration(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
