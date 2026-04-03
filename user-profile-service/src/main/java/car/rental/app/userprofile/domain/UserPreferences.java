package car.rental.app.userprofile.domain;

public class UserPreferences {

    private String preferredCurrency;
    private String preferredLanguage;
    private boolean marketingConsent;

    public UserPreferences() {}

    public UserPreferences(String preferredCurrency, String preferredLanguage, boolean marketingConsent) {
        this.preferredCurrency = preferredCurrency;
        this.preferredLanguage = preferredLanguage;
        this.marketingConsent = marketingConsent;
    }

    public String getPreferredCurrency() { return preferredCurrency; }
    public String getPreferredLanguage() { return preferredLanguage; }
    public boolean isMarketingConsent() { return marketingConsent; }

    public void setPreferredCurrency(String preferredCurrency) { this.preferredCurrency = preferredCurrency; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
    public void setMarketingConsent(boolean marketingConsent) { this.marketingConsent = marketingConsent; }
}
