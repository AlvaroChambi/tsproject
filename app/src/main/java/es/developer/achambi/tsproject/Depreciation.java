package es.developer.achambi.tsproject;

public enum Depreciation {
    UNTIL_ONE_YEAR( 1.0f ),
    UNTIL_TWO_YEARS( 0.84f ),
    UNTIL_THREE_YEARS( 0.67f ),
    UNTIL_FOUR_YEARS( 0.56f ),
    UNTIL_FIVE_YEARS( 0.47f ),
    UNTIL_SIX_YEARS( 0.39f ),
    UNTIL_SEVEN_YEARS( 0.34f ),
    UNTIL_EIGHT_YEARS( 0.28f ),
    UNTIL_NINE_YEARS( 0.24f ),
    UNTIL_TEN_YEARS( 0.19f ),
    UNTIL_ELEVEN_YEARS( 0.17f ),
    UNTIL_TWELVE_YEARS( 0.13f ),
    MORE_THAN_TWELVE_YEARS( 0.10f );

    private float depreciationPercent;

    Depreciation( float depreciationPercent ) {
        this.depreciationPercent = depreciationPercent;
    }

    public float getDepreciationPercent() {
        return depreciationPercent;
    }
}
