package com.example.fudaiclicker;

import android.accessibilityservice.AccessibilityService;
import android.os.SystemClock;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FudaiAccessibilityService extends AccessibilityService {
    private static final String DOUYIN_PACKAGE = "com.ss.android.ugc.aweme";
    private static final long CLICK_COOLDOWN_MS = 900;
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
            "福袋", "参与福袋", "领取福袋", "超级福袋", "粉丝福袋"
    ));

    private long lastClickAt = 0L;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null || event.getPackageName() == null) return;
        if (!DOUYIN_PACKAGE.contentEquals(event.getPackageName())) return;
        if (!getSharedPreferences(MainActivity.PREFS, MODE_PRIVATE)
                .getBoolean(MainActivity.KEY_ENABLED, false)) return;

        long now = SystemClock.elapsedRealtime();
        if (now - lastClickAt < CLICK_COOLDOWN_MS) return;

        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) return;

        AccessibilityNodeInfo target = findTarget(root);
        if (target != null) {
            AccessibilityNodeInfo clickable = findClickableAncestor(target);
            if (clickable != null && clickable.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                lastClickAt = now;
            }
        }
    }

    private AccessibilityNodeInfo findTarget(AccessibilityNodeInfo node) {
        if (node == null) return null;
        if (matches(node)) return node;

        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo found = findTarget(node.getChild(i));
            if (found != null) return found;
        }
        return null;
    }

    private boolean matches(AccessibilityNodeInfo node) {
        CharSequence text = node.getText();
        CharSequence desc = node.getContentDescription();
        String merged = ((text == null ? "" : text.toString()) + " " +
                (desc == null ? "" : desc.toString())).trim();
        if (merged.isEmpty()) return false;

        for (String keyword : KEYWORDS) {
            if (merged.contains(keyword)) return true;
        }
        return false;
    }

    private AccessibilityNodeInfo findClickableAncestor(AccessibilityNodeInfo node) {
        AccessibilityNodeInfo current = node;
        for (int depth = 0; current != null && depth < 5; depth++) {
            if (current.isClickable() && current.isEnabled() && current.isVisibleToUser()) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    @Override
    public void onInterrupt() {
        // No-op.
    }
}
