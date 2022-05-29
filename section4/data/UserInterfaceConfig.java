package section4.data;

import java.util.Arrays;

public class UserInterfaceConfig {
    private String titleColor;
    private String titleText;
    private short titleFontSize;
    private short footerFontSize;
    private String[] titleFonts;
    private short[] titleTextSizes;

    public String getTitleColor() {
        return titleColor;
    }

    public String getTitleText() {
        return titleText;
    }

    public short getTitleFontSize() {
        return titleFontSize;
    }

    public short getFooterFontSize() {
        return footerFontSize;
    }

    public String[] getTitleFonts() {
        return titleFonts;
    }

    public short[] getTitleTextSizes() {
        return titleTextSizes;
    }

    @Override
    public String toString() {
        return "UserInterfaceConfig{" +
                "titleColor='" + titleColor + '\'' +
                ", titleText='" + titleText + '\'' +
                ", titleFontSize=" + titleFontSize +
                ", titleFonts=" + Arrays.toString(titleFonts) +
                ", titleTextSizes=" + Arrays.toString(titleTextSizes) +
                '}';
    }
}
