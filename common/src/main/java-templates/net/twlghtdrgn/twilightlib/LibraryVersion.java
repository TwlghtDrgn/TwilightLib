package net.twlghtdrgn.twilightlib;

import java.util.stream.Stream;

@SuppressWarnings("unused")
public class LibraryVersion {
    private LibraryVersion() {}

    public static final String VERSION = "${project.version}";

    public static boolean checkMin(String version) {
        int[] data = split(version);
        int[] libData = split(VERSION);
        for (int i = 0; i < libData.length; i++) {
            if (data.length == i)
                return true;
            if (libData[i] < data[i])
                return false;
        }
        return true;
    }

    public static boolean checkMax(String version) {
        int[] data = split(version);
        int[] libData = split(VERSION);
        for (int i = 0; i < libData.length; i++) {
            if (data.length == i)
                return true;
            if (libData[i] > data[i])
                return false;
        }
        return true;
    }

    public static boolean isCompatible(String minVer, String maxVer) {
        return (checkMin(minVer) && checkMax(maxVer));
    }

    private static int[] split(String versionString) {
        if (versionString == null)
            return new int[0];
        return Stream.of(versionString.split("'\\."))
                .mapToInt(Integer::parseInt).toArray();
    }
}
