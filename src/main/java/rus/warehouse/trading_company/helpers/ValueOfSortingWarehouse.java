package rus.warehouse.trading_company.helpers;

import lombok.Getter;

public enum ValueOfSortingWarehouse {
    BYID ("По внутреннему ID"),
    BYDATEEXPIRATED ("По концу срока годности"),
    BYDATEMANUFACTURE ("По дате выпуска"),
    BYPRICE ("По цене");

    @Getter
    private String title;

    ValueOfSortingWarehouse (String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
