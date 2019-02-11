package gg.vexus.nms;


import gg.vexus.nms.action.Action;
import gg.vexus.utils.Util;
import org.bukkit.*;

import java.util.regex.*;


public class NMS {
    private static String version() {
        final String version = Bukkit.getVersion();
        final Pattern pat = Pattern.compile("(\\(MC: )([\\d\\.]+)(\\))");
        final Matcher mat = pat.matcher(version);
        if (mat.find()) {
            return mat.group(2);
        }
        return "";
    }

    public static String nms() {
        final String version = version();
        final String s;
        switch ((s = version).hashCode()) {
            case 48571: {
                if (!s.equals("1.8")) {
                    return "[Failed to get NMS]";
                }
                return "v1_8_R1";
            }
            case 48572: {
                if (!s.equals("1.9")) {
                    return "[Failed to get NMS]";
                }
                return "v1_9_R1";
            }
            case 1505532: {
                if (!s.equals("1.10")) {
                    return "[Failed to get NMS]";
                }
                return "v1_10_R1";
            }
            case 1505533: {
                if (!s.equals("1.11")) {
                    return "[Failed to get NMS]";
                }
                return "v1_11_R1";
            }
            case 46677246: {
                if (!s.equals("1.7.2")) {
                    return "[Failed to get NMS]";
                }
                break;
            }
            case 46677247: {
                if (!s.equals("1.7.3")) {
                    return "[Failed to get NMS]";
                }
                break;
            }
            case 46677248: {
                if (!s.equals("1.7.4")) {
                    return "[Failed to get NMS]";
                }
                break;
            }
            case 46677249: {
                if (!s.equals("1.7.5")) {
                    return "[Failed to get NMS]";
                }
                break;
            }
            case 46677250: {
                if (!s.equals("1.7.6")) {
                    return "[Failed to get NMS]";
                }
                break;
            }
            case 46677251: {
                if (!s.equals("1.7.7")) {
                    return "[Failed to get NMS]";
                }
                break;
            }
            case 46677252: {
                if (!s.equals("1.7.8")) {
                    return "[Failed to get NMS]";
                }
                break;
            }
            case 46677253: {
                if (!s.equals("1.7.9")) {
                    return "[Failed to get NMS]";
                }
                break;
            }
            case 46678206: {
                if (!s.equals("1.8.1")) {
                    return "[Failed to get NMS]";
                }
                return "v1_8_R1";
            }
            case 46678207: {
                if (!s.equals("1.8.2")) {
                    return "[Failed to get NMS]";
                }
                return "v1_8_R1";
            }
            case 46678208: {
                if (!s.equals("1.8.3")) {
                    return "[Failed to get NMS]";
                }
                return "v1_8_R2";
            }
            case 46678209: {
                if (!s.equals("1.8.4")) {
                    return "[Failed to get NMS]";
                }
                return "v1_8_R3";
            }
            case 46678210: {
                if (!s.equals("1.8.5")) {
                    return "[Failed to get NMS]";
                }
                return "v1_8_R3";
            }
            case 46678211: {
                if (!s.equals("1.8.6")) {
                    return "[Failed to get NMS]";
                }
                return "v1_8_R3";
            }
            case 46678212: {
                if (!s.equals("1.8.7")) {
                    return "[Failed to get NMS]";
                }
                return "v1_8_R3";
            }
            case 46678213: {
                if (!s.equals("1.8.8")) {
                    return "[Failed to get NMS]";
                }
                return "v1_8_R3";
            }
            case 46678214: {
                if (!s.equals("1.8.9")) {
                    return "[Failed to get NMS]";
                }
                return "v1_8_R3";
            }
            case 46679167: {
                if (!s.equals("1.9.1")) {
                    return "[Failed to get NMS]";
                }
                return "v1_9_R1";
            }
            case 46679168: {
                if (!s.equals("1.9.2")) {
                    return "[Failed to get NMS]";
                }
                return "v1_9_R1";
            }
            case 46679170: {
                if (!s.equals("1.9.4")) {
                    return "[Failed to get NMS]";
                }
                return "v1_9_R2";
            }
            case 1446817727: {
                if (!s.equals("1.10.1")) {
                    return "[Failed to get NMS]";
                }
                return "v1_10_R1";
            }
            case 1446817728: {
                if (!s.equals("1.10.2")) {
                    return "[Failed to get NMS]";
                }
                return "v1_10_R1";
            }
            case 1446818688: {
                if (!s.equals("1.11.1")) {
                    return "[Failed to get NMS]";
                }
                return "v1_11_R1";
            }
            case 1446818689: {
                if (!s.equals("1.11.2")) {
                    return "[Failed to get NMS]";
                }
                return "v1_11_R1";
            }
            case 1446994643: {
                if (!s.equals("1.7.10")) {
                    return "[Failed to get NMS]";
                }
                break;
            }
        }
        return "v1_7";
    }

    public static Action action() {
        final String nms = nms();
        final String s;

        return null;
    }
}
