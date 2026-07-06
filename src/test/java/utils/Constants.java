package utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

    public static final String ALERTMESSAGECOLOR = "rgb(223, 240, 216)";
    public static final String USERNWINNING = "WINNINGHAM, R (rwinning)";
    public static final String FISCALYEAR = "2025";
    public static final String EMAILFEMA = "FEMA-GPD-Systems-TDL@fema.gov";
    public static final String PROGRAMFEMA = "3.07 Test Program";
    public static final String FUNDINGOPPORTUNITY =
            "FY 2011 Emergency Management Performance Grants Region 4";
    public static final String COMMENT = "Test Comment";

    public static List<String> getStateByRegionList(String key) {

        Map<String, List<String>> regionsDataArtemis = new HashMap<>();

        regionsDataArtemis.put("1",
                Arrays.asList("", "CT", "MA", "ME", "NH", "RI", "VT"));

        regionsDataArtemis.put("2",
                Arrays.asList("", "NJ", "NY", "PR", "VI"));

        regionsDataArtemis.put("3",
                Arrays.asList("", "DC", "DE", "MD", "PA", "VA", "WV"));

        regionsDataArtemis.put("4",
                Arrays.asList("", "AL", "FL", "GA", "KY", "MS", "NC", "SC", "TN"));

        regionsDataArtemis.put("5",
                Arrays.asList("", "IL", "IN", "MI", "MN", "OH", "WI"));

        regionsDataArtemis.put("6",
                Arrays.asList("", "AR", "LA", "NM", "OK", "TX"));

        regionsDataArtemis.put("7",
                Arrays.asList("", "IA", "KS", "MO", "NE"));

        regionsDataArtemis.put("8",
                Arrays.asList("", "CO", "MT", "ND", "SD", "UT", "WY"));

        regionsDataArtemis.put("9",
                Arrays.asList("", "AS", "AZ", "CA", "FM", "GU", "HI", "MH",
                        "MP", "NV", "PW", "UM"));

        regionsDataArtemis.put("10",
                Arrays.asList("", "AK", "ID", "OR", "WA"));

        regionsDataArtemis.put("H0", getAllStatesList());
        regionsDataArtemis.put("H1", getAllStatesList());
        regionsDataArtemis.put("H2", getAllStatesList());
        regionsDataArtemis.put("H3", getAllStatesList());
        regionsDataArtemis.put("H4", getAllStatesList());
        regionsDataArtemis.put("H5", getAllStatesList());
        regionsDataArtemis.put("H6", getAllStatesList());
        regionsDataArtemis.put("H7", getAllStatesList());
        regionsDataArtemis.put("H8", getAllStatesList());
        regionsDataArtemis.put("H9", getAllStatesList());

        return regionsDataArtemis.get(key);
    }

    private static List<String> getAllStatesList() {
        return Arrays.asList(
                "", "AK", "AL", "AR", "AS", "AZ", "CA", "CO", "CT", "DC",
                "DE", "FL", "FM", "GA", "GU", "HI", "IA", "ID", "IL", "IN",
                "KS", "KY", "LA", "MA", "MD", "ME", "MH", "MI", "MN", "MO",
                "MP", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV",
                "NY", "OH", "OK", "OR", "PA", "PR", "PW", "RI", "SC", "SD",
                "TN", "TX", "UM", "UT", "VA", "VI", "VT", "WA", "WI", "WV",
                "WY");
    }

    public static String getStateAbbreviation(String fullStateName) {
        switch (fullStateName.toLowerCase()) {
            case "alabama":
                return "AL";
            case "new jersey":
                return "NJ";
            default:
                throw new IllegalArgumentException("Unknown state: " + fullStateName);
        }
    }

    public static final List<String> PROGRAMLIST = Arrays.asList(
            "", "AFGSCG", "BRIC", "BZPP", "CSEPP", "EMIST", "EMPG", "EOC",
            "FAC", "FM", "FMA", "FMAGP", "FMASC", "HHPD", "HMGPP", "HMGPPF",
            "HSGP", "IA", "IBSGP", "LETP", "LOG", "LPDM", "MICG",
            "NEPA TEST", "NGWSG", "NSGP", "OSHE", "PA", "PDM", "PDMC",
            "PEPAC", "PSGP", "RFC", "SRL", "TSGP", "UASI"
    );

    public static final List<Integer> FISCALYEARINTEGERLIST = Arrays.asList(
            0, 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008,
            2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017,
            2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025
    );

    // This list in your screenshot is very long.
    // I can only safely start it from visible values:
    public static final List<Integer> DISASTEREVENTCODE = Arrays.asList(
            0, 574, 575, 576, 577, 578, 579, 580, 581, 582, 583, 584,
            585, 586, 587, 588, 589, 590, 591, 592, 593, 594, 595,
            596, 597, 598, 599, 600
            // Continue list from your source file
    );

    public static final List<String> REGIONLIST = Arrays.asList(
            "", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "H0", "H1", "H2", "H3", "H4", "H5", "H6", "H7", "H8", "H9");

    public static final List<String> STATESLIST = Arrays.asList(
            "", "AK", "AL", "AR", "AS", "AZ", "CA", "CO", "CT", "DC", "DE",
            "FL", "FM", "GA", "GU", "HI", "IA", "ID", "IL", "IN", "KS", "KY",
            "LA", "MA", "MD", "ME", "MH", "MI", "MN", "MO", "MP", "MS", "MT",
            "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR",
            "PA", "PR", "PW", "RI", "SC", "SD", "TN", "TX", "UM", "UT", "VA",
            "VI", "VT", "WA", "WI", "WV", "WY");

    public static final String GETPROGRAMDESCRIPTION(String key) {
        final Map<String, String> PROGRAMDESCRIPTION = new HashMap<>();

        PROGRAMDESCRIPTION.put("FMA", "Flood Mitigation Assistance");
        PROGRAMDESCRIPTION.put("IA", "Temporary Housing");

        return PROGRAMDESCRIPTION.get(key);
    }

    public static final List<String> VALIDLISTOFREGIONS = Arrays.asList(
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "H0", "H1", "H2", "H3", "H4", "H5", "H6", "H7", "H8", "H9");

    public static final List<String> VALIDLISTOFREGIONSBRIC = Arrays.asList(
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

    public static final List<String> VALIDREGIONS = Arrays.asList(
            "", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

    public static final List<String> DOCUMENTATIONLEVELLIST = Arrays.asList(
            "", "N/A", "1", "2", "3");

// DFSC TEST DATA

    public static final String pdfExpected1 =
            "Disaster Housing Certification Part1\r\n"
                    + "Disaster # Object Class Number of Payments Certified Amount\r\n"
                    + "1468 4143 4 17137.05\r\n"
                    + "1468 4149 8 17490.4\r\n"
                    + "1488 4149 1 100\r\n"
                    + "4811 4143 1 15618.01\r\n"
                    + "4811 4149 1 4811.58\r\n"
                    + "7119 4141 47 89662\r\n"
                    + "7119 4143 22 118949.67\r\n"
                    + "7119 4147 5 133527\r\n"
                    + "7119 4149 59 73760.34\r\n"
                    + "7122 4143 1 500\r\n"
                    + "7611 4149 39 19300\r\n"
                    + "7612 4149 1 1400\r\n"
                    + "7615 4149 2 800\r\n"
                    + "9047 4143 6 23981.17\r\n"
                    + "9047 4149 15 30690.53";

    public static final String pdfExpected2 =
            "Disaster Housing Certification Part2\r\n"
                    + "Disaster Object Autodetermined Manual Autodetermined Manual\r\n"
                    + "# Class Payments Payments Amount Amount\r\n"
                    + "1468 4143 4 0 17137.05 0\r\n"
                    + "1468 4149 8 0 17490.4 0\r\n"
                    + "1488 4149 1 0 100 0\r\n"
                    + "4811 4143 1 0 15618.01 0\r\n"
                    + "4811 4149 1 0 4811.58 0\r\n"
                    + "7119 4141 47 0 89662 0\r\n"
                    + "7119 4143 22 0 118949.67 0\r\n"
                    + "7119 4147 5 0 133527 0\r\n"
                    + "7119 4149 59 0 73760.34 0\r\n"
                    + "7122 4143 0 1 0 500\r\n"
                    + "7611 4149 38 1 19000 300\r\n"
                    + "7612 4149 1 0 1400 0\r\n"
                    + "7615 4149 2 0 800 0\r\n"
                    + "9047 4143 6 0 23981.17 0\r\n"
                    + "9047 4149 13 2 29990.53 700";

    public static final String pdfExpected3 =
            "Disaster Housing Certification Part3\r\n"
                    + "Object Autodetermined Manual Total Autodetermined Manual Total\r\n"
                    + "Class Payments Payments Payments Amount Amount Amount\r\n"
                    + "4141 47 0 47 89662 0 89662\r\n"
                    + "4143 33 1 34 175685.9 500 176185.9\r\n"
                    + "4147 5 0 5 133527 0 133527\r\n"
                    + "4149 123 3 126 147352.85 1000 148352.85";

    public static final String pdfExpected4 =
            "Request Type: RAA - Request for Allocation Advice Role: FY: 2026 Event: 1021DR";

    public static final String pdfExpected5 =
            "Disaster Finance Applicant Applicant\r\n"
                    + "Amount Date Locked\r\n"
                    + "Number Program Name Number";

}
