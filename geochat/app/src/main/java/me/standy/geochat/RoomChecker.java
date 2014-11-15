package me.standy.geochat;

import android.net.wifi.ScanResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nastya on 16.11.14.
 */
public class RoomChecker {
    public static boolean checkRoom(List<ScanResult> scanResults1, List<ScanResult> scanResults2, long epsilon) {
        long dist = 0;
        Map<String, Integer> level1 = new HashMap<String, Integer>();
        Map<String, Integer> level2 = new HashMap<String, Integer>();
        for (ScanResult sr : scanResults1) {
            if (!scanResults2.contains(sr)) {
                level2.put(sr.SSID, -100);
            }
            level1.put(sr.SSID, sr.level);
        }
        for (ScanResult sr : scanResults2) {
            if (!scanResults1.contains(sr)) {
                level1.put(sr.SSID, -100);
            }
            level2.put(sr.SSID, sr.level);
        }

        int numberOfElements = level1.size();
        for (String ssid : level1.keySet()) {
            dist += (level1.get(ssid) - level2.get(ssid)) * (level1.get(ssid) - level2.get(ssid));
        }
        double diff = Math.sqrt(dist / numberOfElements);
        return diff <= epsilon;
    }
}
