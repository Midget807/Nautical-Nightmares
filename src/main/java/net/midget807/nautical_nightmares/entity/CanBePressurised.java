package net.midget807.nautical_nightmares.entity;

public interface CanBePressurised {
    int getPressure();
    void setPressure(int pressure);
    int getPressureAsStat();
    void setPressureAsStat(int pressureAsStat);

    float getPressurisedScale();
    int getPressurisedTicks();
    void setPressurisedTicks(int ticks);
    int getMinPressurisedDamageTicks();
    boolean isPressurised();

    boolean shouldPressurise();
    void setShouldPressurise(boolean shouldPressurise);
    boolean canPressurise();
    void setCanPressurise(boolean canPressurise);

    int getMaxPressure();
    void setMaxPressure(int maxPressure);
    int getMaxPressureAsStat();
    void setMaxPressureAsStat(int maxPressureAsStat);
}
