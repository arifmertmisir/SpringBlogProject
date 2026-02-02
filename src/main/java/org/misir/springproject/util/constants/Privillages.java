package org.misir.springproject.util.constants;

public enum Privillages {
    RESET_ANY_USER_PASSWORD(1L, "RESET_ANY_USER_PASSWORD"),
    ACCESS_ADMIN_PANEL(2L, "ACCESS_ADMIN_PANEL");

    private Long id;
    private String privillage;
    Privillages(Long id, String privillage) {
        this.id = id;
        this.privillage = privillage;
    }
    public Long getId() {
        return id;
    }
    public String getprivillage() {
        return privillage;
    }
}
