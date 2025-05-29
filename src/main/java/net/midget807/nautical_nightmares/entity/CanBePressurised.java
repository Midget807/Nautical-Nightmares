package net.midget807.nautical_nightmares.entity;

public interface CanBePressurised {
    double getPressure();
    void setPressure(double pressure);
    double getPressureAsStat();
    void setPressureAsStat(double pressureAsStat);

    float getPressurisedScale();
    int getPressurisedTicks();
    void setPressurisedTicks(int ticks);
    int getMinPressurisedDamageTicks();
    boolean isPressurised();

    boolean shouldPressurise();
    void setShouldPressurise(boolean shouldPressurise);
    boolean canPressurise();
    void setCanPressurise(boolean canPressurise);
}
